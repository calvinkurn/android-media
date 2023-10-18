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
import com.tokopedia.catalog_library.viewmodel.CatalogHomepageViewModel
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

class CatalogHomepageFragment : CatalogProductsBaseFragment(), CatalogLibraryListener {

    private var catalogHomeRecyclerView: RecyclerView? = null
    private var globalError: GlobalError? = null
    private val trackingSet = HashSet<String>()

    companion object {
        fun getInstance(): CatalogHomepageFragment {
            return CatalogHomepageFragment()
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var trackingQueue: TrackingQueue? = null

    private val homepageViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogHomepageViewModel::class.java)
        }
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }

    private val catalogHomeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf()).also {
            it.shimmerForHomePage()
        }

    override var baseRecyclerView: RecyclerView?
        get() = catalogHomeRecyclerView
        set(value) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog_homepage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromViewModel()
        initViews(view)
        setUpBase()
        setObservers()
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initHeaderTitle(view)
        setupRecyclerView(view)
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.catalog_home_header).apply {
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun getDataFromViewModel() {
        homepageViewModel?.getSpecialData()
        homepageViewModel?.getRelevantData()
        homepageViewModel?.getPopularBrands()
    }

    override var source: String = CatalogLibraryConstant.SOURCE_HOMEPAGE

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun setupRecyclerView(view: View) {
        catalogHomeRecyclerView = view.findViewById(R.id.catalog_home_rv)
        catalogHomeRecyclerView?.apply {
            layoutManager = getLinearLayoutManager()
            adapter = catalogHomeAdapter
            setHasFixedSize(true)
        }
        updateUi()
    }

    private fun setObservers() {
        homepageViewModel?.catalogLibraryLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
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

    private fun updateUi() {
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDM>) {
        catalogHomeAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }
        catalogHomeRecyclerView?.hide()
        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogHomeRecyclerView?.show()
            globalError?.hide()
            getDataFromViewModel()
            getProducts()
        }
    }

    override fun onLihatSemuaTextClick(applink: String) {
        super.onLihatSemuaTextClick(applink)
        if (applink.contains(CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)) {
            CatalogAnalyticsHomePage.sendClickLihatSemuaOnPopularBrandsEvent(
                userSessionInterface?.userId ?: ""
            )
        } else {
            CatalogAnalyticsHomePage.sendClickLihatSemuaOnSpecialCategoriesEvent(
                userSessionInterface?.userId ?: ""
            )
        }
        RouteManager.route(context, applink)
    }

    override fun onPopularBrandsHomeClick(brandName: String, brandId: String, position: String) {
        super.onPopularBrandsHomeClick(brandName, brandId, position)
        RouteManager.route(context, "${CatalogLibraryConstant.APP_LINK_BRANDS}$brandId/$brandName")
        CatalogAnalyticsHomePage.sendClickBrandOnPopularBrandsEvent(
            "$brandName - $brandId - $position",
            userSessionInterface?.userId ?: ""
        )
    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }

    override fun onCategoryItemClicked(categoryIdentifier: String?) {
        super.onCategoryItemClicked(categoryIdentifier)
        RouteManager.route(
            context,
            "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryIdentifier"
        )
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

    override fun categoryHorizontalCarouselImpression(
        creativeName: String,
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String,
        trackerId: String,
        eventAction: String
    ) {
        val uniqueTrackingKey = "$eventAction-$creativeSlot"
        if (!trackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsHomePage.sendImpressionOnCatalogsEvent(
                trackerId,
                eventAction,
                creativeName,
                trackingQueue,
                creativeSlot,
                itemId,
                itemName,
                userId
            )
            trackingSet.add(uniqueTrackingKey)
        }
    }

    override fun catalogProductsHomePageImpression(
        catgoryName: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val uniqueTrackingKey = "${ActionKeys.IMPRESSION_ON_CATALOG_LIST}-$position"
        if (!trackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsHomePage.sendImpressionOnCatalogListEvent(
                trackingQueue,
                product,
                position,
                userId
            )
            trackingSet.add(uniqueTrackingKey)
        }
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }
}
