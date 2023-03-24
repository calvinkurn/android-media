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
import com.tokopedia.catalog_library.model.datamodel.CatalogBrandCategoryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogProductLoadMoreDM
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.ui.bottomsheet.CatalogLibraryComponentBottomSheet
import com.tokopedia.catalog_library.util.ActionKeys
import com.tokopedia.catalog_library.util.CatalogAnalyticsCategoryLandingPage
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_CONTAINER_CATEGORY_HEADER
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogLihatSemuaPageVM
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_catalog_homepage.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogBrandLandingPageFragment : CatalogProductsBaseFragment(), CatalogLibraryListener {

    private var globalError: GlobalError? = null
    private var brandIdStr = ""
    private var categoryIdIdStr = ""
    private var catalogLandingRecyclerView: RecyclerView? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val brandLandingPageViewModel: CatalogLihatSemuaPageVM by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(CatalogLihatSemuaPageVM::class.java)
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
            it.setUpForBrandLanding()
        }

    @Inject
    lateinit var trackingQueue: TrackingQueue
    private val productsTrackingSet = HashSet<String>()
    private val topCatalogsTrackingSet = HashSet<String>()

    companion object {
        const val ARG_BRAND_ID = "ARG_BRAND_ID"
        fun newInstance(brandId: String?): CatalogBrandLandingPageFragment {
            return CatalogBrandLandingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BRAND_ID, brandId)
                }
            }
        }
    }

    override var baseRecyclerView: RecyclerView?
        get() = catalogLandingRecyclerView
        set(value) {}

    override fun getScreenName(): String {
        return ""
    }

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
        initHeaderTitle(view)
        setBrandId(brandIdStr)
        setUpBase()
        initData()
    }

    private fun extractArguments() {
        brandIdStr = arguments?.getString(ARG_BRAND_ID, "") ?: ""
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
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
        brandLandingPageViewModel.catalogLihatLiveDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        (component as? CatalogBrandCategoryDM)?.selectedCategoryId = categoryIdIdStr
                        catalogLibraryUiUpdater.updateModel(component)
                    }
                    updateUi()
                }

                is Fail -> {
                    onError(it.throwable)
                }
            }
        }
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.clp_header).apply {
            headerTitle = getString(R.string.brand_terpopuler)
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun updateUi() {
        catalogLandingRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDM>) {
        catalogLandingPageAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        if(catalogLibraryUiUpdater.hasData()){
            return
        }
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
            global_error_page.hide()
            addShimmer()
            updateUi()
            getDataFromViewModel()
        }
    }

    private fun getDataFromViewModel() {
        brandLandingPageViewModel.getLihatSemuaByBrandData(categoryIdIdStr,brandIdStr,false)
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.setUpForBrandLanding()
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

        if (productsList.isEmpty()) {
            catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        } else {
            catalogLibraryUiUpdater.updateModel(CatalogProductLoadMoreDM())
        }
        updateUi()
    }

    override fun onShimmerAdded() {

    }

    override fun onErrorFetchingProducts(throwable: Throwable) {
        onError(throwable)
    }

    override fun onBrandCategoryArrowClick() {
        super.onBrandCategoryArrowClick()
        openBottomSheet(brandIdStr)
    }

    override fun onBrandCategoryTabSelected(categoryName: String, categoryId : String) {
        super.onBrandCategoryTabSelected(categoryName, categoryId)
        onChangeCategory(categoryId)
    }

    override fun onChangeCategory(categoryId: String) {
        super.onChangeCategory(categoryId)
        categoryIdIdStr = categoryId
        val categoryModel = catalogLibraryUiUpdater.mapOfData[CATALOG_CONTAINER_CATEGORY_HEADER] as? CatalogBrandCategoryDM
        catalogLibraryUiUpdater.clearAll()
        categoryModel?.copy()?.let { cm ->
            cm.selectedCategoryId = categoryId
            catalogLibraryUiUpdater.updateModel(cm)
        }
        setCategory(categoryId)
        getProducts()
    }

    private fun openBottomSheet(brandIdStr: String) {
        val catalogComparisonBottomSheet = CatalogLibraryComponentBottomSheet.newInstance(
            categoryIdIdStr,
            brandIdStr
        )
        catalogComparisonBottomSheet.show(childFragmentManager, CatalogLibraryComponentBottomSheet::class.java.name)
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
        trackingQueue.sendAll()
    }
}
