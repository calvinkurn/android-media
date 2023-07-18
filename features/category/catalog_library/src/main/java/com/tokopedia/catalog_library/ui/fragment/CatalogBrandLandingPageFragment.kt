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
import com.tokopedia.catalog_library.util.CatalogAnalyticsBrandLandingPage
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_CONTAINER_CATEGORY_HEADER
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodel.CatalogLihatSemuaPageViewModel
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
    private var brandNameStr = ""
    private var categoryIdStr = ""
    private var categoryNameStr = ""
    private var catalogLandingRecyclerView: RecyclerView? = null
    private var header: HeaderUnify? = null
    private val productsTrackingSet = HashSet<String>()

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var trackingQueue: TrackingQueue? = null

    private val brandLandingPageViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogLihatSemuaPageViewModel::class.java)
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
            it.shimmerForBrandLandingPage()
        }

    companion object {
        const val ARG_BRAND_ID = "ARG_BRAND_ID"
        const val ARG_BRAND_NAME = "ARG_BRAND_NAME"
        fun newInstance(brandId: String?, brandName: String?): CatalogBrandLandingPageFragment {
            return CatalogBrandLandingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BRAND_ID, brandId)
                    putString(ARG_BRAND_NAME, brandName)
                }
            }
        }
    }

    override var baseRecyclerView: RecyclerView?
        get() = catalogLandingRecyclerView
        set(value) {}

    override var source: String = CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE

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
        initHeaderTitle(view)
        setBrandId(brandIdStr)
        setUpBase()
        initData()
    }

    private fun extractArguments() {
        brandIdStr = arguments?.getString(ARG_BRAND_ID, "") ?: ""
        brandNameStr = arguments?.getString(ARG_BRAND_NAME, "") ?: ""
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
        brandLandingPageViewModel?.catalogLihatLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        (component as? CatalogBrandCategoryDM)?.selectedCategoryId = categoryIdStr
                        catalogLibraryUiUpdater.updateModel(component)
                    }
                    updateUi()
                }

                is Fail -> {
                    onError(it.throwable)
                }
            }
        }

        brandLandingPageViewModel?.brandNameLiveData?.observe(viewLifecycleOwner) { brandName ->
            if (!brandName.isNullOrBlank()) {
                header?.headerTitle = brandName
                brandNameStr = brandName
            }
        }
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.clp_header).apply {
            header = this
            headerTitle = brandNameStr
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
            getDataFromViewModel()
            getProducts()
        }
    }

    private fun getDataFromViewModel() {
        brandLandingPageViewModel?.getLihatSemuaByBrandData(categoryIdStr, brandIdStr, false)
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.shimmerForBrandLandingPage()
        updateUi()
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

    override fun onBrandCategoryArrowClick() {
        super.onBrandCategoryArrowClick()
        openBottomSheet(brandIdStr)
    }

    override fun onBrandCategoryTabSelected(categoryName: String, categoryId: String, position: Int) {
        super.onBrandCategoryTabSelected(categoryName, categoryId, position)
        CatalogAnalyticsBrandLandingPage.sendOnTabClickEvent(
            trackingQueue,
            brandNameStr,
            brandIdStr,
            categoryNameStr,
            categoryIdStr,
            position,
            userSessionInterface?.userId ?: ""
        )
        onChangeCategory(categoryName, categoryId, true)
    }

    override fun onChangeCategory(categoryName: String, categoryId: String, isTabSelected: Boolean) {
        super.onChangeCategory(categoryName, categoryId, isTabSelected)
        categoryIdStr = categoryId
        categoryNameStr = categoryName
        val categoryModel = catalogLibraryUiUpdater.mapOfData[CATALOG_CONTAINER_CATEGORY_HEADER] as? CatalogBrandCategoryDM
        catalogLibraryUiUpdater.clearAll()
        if (isTabSelected) {
            categoryModel?.selectedCategoryId = categoryId
            categoryModel?.let { catalogLibraryUiUpdater.updateModel(it) }
        } else {
            categoryModel?.copy()?.let { cm ->
                cm.selectedCategoryId = categoryId
                catalogLibraryUiUpdater.updateModel(cm)
            }
        }
        setCategory(categoryId)
        getProducts()
    }

    private fun openBottomSheet(brandIdStr: String) {
        CatalogAnalyticsBrandLandingPage.sendClickExpandBottomSheetButtonEvent(
            "brand page: $brandNameStr - $brandIdStr - category tab: {category-name/$categoryNameStr} - {category-id/$categoryIdStr}",
            userSessionInterface?.userId ?: ""
        )
        val catalogComparisonBottomSheet = CatalogLibraryComponentBottomSheet.newInstance(
            categoryIdStr,
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
        val uniqueTrackingKey = "${ActionKeys.IMPRESSION_ON_CATALOG}-$position"
        if (!productsTrackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsBrandLandingPage.sendImpressionOnCatalogListEvent(
                trackingQueue,
                brandNameStr,
                brandIdStr,
                catgoryName,
                product,
                position,
                userId
            )
            productsTrackingSet.add(uniqueTrackingKey)
        }
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }

    fun dismissKategoriBottomSheet() {
        CatalogAnalyticsBrandLandingPage.sendClickCloseBottomSheetButtonEvent(
            "$brandNameStr - $brandIdStr",
            userSessionInterface?.userId ?: ""
        )
    }
}
