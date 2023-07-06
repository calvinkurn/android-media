package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogProductLoadMoreDM
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.util.*
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SORT_TYPE_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SORT_TYPE_VIRAL
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.TOTAL_ROWS_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.TOTAL_ROWS_VIRAL
import com.tokopedia.catalog_library.viewmodel.CatalogLandingPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLandingPageFragment : CatalogProductsBaseFragment(), CatalogLibraryListener {

    private var globalError: GlobalError? = null
    private var categoryIdStr = ""
    private var categoryName = ""
    private var catalogLandingRecyclerView: RecyclerView? = null
    private val productsTrackingSet = HashSet<String>()
    private val topCatalogsTrackingSet = HashSet<String>()
    private var trackingSent = false

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var trackingQueue: TrackingQueue? = null

    private val landingPageViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogLandingPageViewModel::class.java)
        }
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }
    private val catalogLandingPageAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf()).also {
            it.shimmerForLandingPage()
        }

    companion object {
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        fun newInstance(categoryId: String?): CatalogLandingPageFragment {
            return CatalogLandingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)
                }
            }
        }
    }

    override var baseRecyclerView: RecyclerView?
        get() = catalogLandingRecyclerView
        set(value) {}

    override var source: String = CatalogLibraryConstant.SOURCE_CATEGORY_LANDING_PAGE

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        initViews(view)
        setCategory(categoryIdStr)
        setUpBase()
        initData()
    }

    private fun extractArguments() {
        categoryIdStr = arguments?.getString(ARG_CATEGORY_ID, "") ?: ""
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initHeaderTitle(view)
        setupRecyclerView(view)
    }

    private fun initData() {
        setObservers()
        updateUi()
        getDataFromViewModel()
    }

    private fun setupRecyclerView(view: View) {
        catalogLandingRecyclerView = view.findViewById(R.id.category_landing_rv)
        catalogLandingRecyclerView?.apply {
            layoutManager = getLinearLayoutManager()
            adapter = catalogLandingPageAdapter
            setHasFixedSize(true)
        }
    }

    private fun setObservers() {
        landingPageViewModel?.catalogLandingPageLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        catalogLibraryUiUpdater.updateModel(component)
                    }
                    updateUi()
                }

                is Fail -> {
                    (it.throwable as? CatalogLibraryResponseException)?.let { exception ->
                        catalogLibraryUiUpdater.removeModel(exception.shimmerType)
                        updateUi()
                    }
                }
            }

            landingPageViewModel?.categoryName?.observe(viewLifecycleOwner) { categoryName ->
                this.categoryName = categoryName
                sendOpenScreenEvent()
                view?.let { v -> initHeaderTitle(v) }
            }
        }
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.clp_header).apply {
            headerTitle = categoryName
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun updateUi() {
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDM>) {
        catalogLandingPageAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        catalogLandingRecyclerView?.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }

        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogLandingRecyclerView?.show()
            globalError?.hide()
            addShimmer()
            updateUi()
            getDataFromViewModel()
            getProducts()
        }
    }

    private fun getDataFromViewModel() {
        landingPageViewModel?.getCatalogTopFiveData(
            categoryIdStr,
            SORT_TYPE_TOP_FIVE,
            TOTAL_ROWS_TOP_FIVE
        )
        landingPageViewModel?.getCatalogMostViralData(
            categoryIdStr,
            SORT_TYPE_VIRAL,
            TOTAL_ROWS_VIRAL
        )
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.shimmerForLandingPage()
    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }

    override fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDM>) {
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT)
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        productsList.forEach { component ->
            catalogLibraryUiUpdater.updateModel(component)
        }
        updateUi()
    }

    override fun onShimmerAdded() {
        catalogLibraryUiUpdater.updateModel(CatalogProductLoadMoreDM())
        updateUi()
    }

    override fun onErrorFetchingProducts(throwable: Throwable) {
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT)
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        updateUi()
        if (productCount == 0) {
            onError(throwable)
        }
    }

    override fun catalogProductsCategoryLandingImpression(
        catgoryName: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val uniqueTrackingKey = "${ActionKeys.IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY}-$position"
        if (!productsTrackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsCategoryLandingPage.sendImpressionOnCatalogListEvent(
                trackingQueue,
                catgoryName,
                product,
                position,
                userId
            )
            productsTrackingSet.add(uniqueTrackingKey)
        }
    }

    override fun topFiveImpressionCategoryLandingImpression(
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        position: Int,
        userId: String
    ) {
        val uniqueTrackingKey = "${ActionKeys.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY}-$position"
        if (!topCatalogsTrackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsCategoryLandingPage.sendImpressionOnTopCatalogsInCategoryEvent(
                trackingQueue,
                categoryName,
                categoryId,
                catalogName,
                catalogId,
                position,
                userId
            )
            topCatalogsTrackingSet.add(uniqueTrackingKey)
        }
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }

    private fun sendOpenScreenEvent() {
        if (!trackingSent) {
            CatalogAnalyticsCategoryLandingPage.openScreenCategoryLandingPage(
                categoryName,
                userSessionInterface?.isLoggedIn.toString(),
                categoryIdStr,
                userSessionInterface?.userId ?: ""
            )
        }
        trackingSent = true
    }
}
