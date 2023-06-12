package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDM
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDM
import com.tokopedia.catalog_library.ui.bottomsheet.CatalogLibraryComponentBottomSheet
import com.tokopedia.catalog_library.util.CatalogAnalyticsBrandLandingPage
import com.tokopedia.catalog_library.util.CatalogAnalyticsLihatSemuaPage
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.util.TrackerId.Companion.IMPRESSION_ON_CATEGORY_LIST_BRAND_LANDING
import com.tokopedia.catalog_library.viewmodel.CatalogLihatSemuaPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLihatSemuaPageFragment : CatalogLibraryBaseFragment(), CatalogLibraryListener {

    private var catalogLihatPageRecyclerView: RecyclerView? = null
    private var sortAsc: ChipsUnify? = null
    private var sortDesc: ChipsUnify? = null
    private var globalError: GlobalError? = null
    var order = CatalogLibraryConstant.ASCENDING_ORDER_STR
    private var isOriginBrand = false
    private var categoryId = ""
    private var brandId = ""
    private var brandName = ""
    private val categoryTrackingSet = HashSet<String>()

    companion object {
        const val DEFAULT_ASC_SORT_ORDER = "0"
        const val DESC_SORT_ORDER = "1"
        private const val ARG_IS_ORIGIN_BRAND = "ARG_IS_ORIGIN_BRAND"
        private const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        private const val ARG_BRAND_ID = "ARG_BRAND_ID"
        private const val ARG_BRAND_NAME = "ARG_BRAND_NAME"
        fun newInstance(
            isOriginBrand: Boolean = false,
            categoryId: String = "",
            brandId: String = "",
            brandName: String = ""
        ): CatalogLihatSemuaPageFragment {
            return CatalogLihatSemuaPageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_ORIGIN_BRAND, isOriginBrand)
                    putString(ARG_CATEGORY_ID, categoryId)
                    putString(ARG_BRAND_ID, brandId)
                    putString(ARG_BRAND_NAME, brandName)
                }
            }
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

    private val lihatViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it)[CatalogLihatSemuaPageViewModel::class.java]
        }
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }
    private val catalogLibraryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf())

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
        return inflater.inflate(R.layout.fragment_catalog_lihat_semua_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        initViews(view)
        setObservers()
        makeApiCall()
    }

    private fun extractArguments() {
        isOriginBrand = arguments?.getBoolean(ARG_IS_ORIGIN_BRAND) ?: false
        categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: ""
        brandId = arguments?.getString(ARG_BRAND_ID) ?: ""
    }

    private fun makeApiCall(orderType: String = DEFAULT_ASC_SORT_ORDER) {
        if (isOriginBrand) {
            lihatViewModel?.getLihatSemuaByBrandData(categoryId, brandId)
        } else {
            lihatViewModel?.getLihatSemuaPageData(orderType)
        }
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.lihat_semua_page_header).apply {
            if (isOriginBrand) {
                hide()
            }
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initChips(view)
        initHeaderTitle(view)
        setupRecyclerView(view)
        addShimmer()
    }

    private fun initChips(view: View) {
        sortAsc = view.findViewById(R.id.chip_sort_asc)
        sortDesc = view.findViewById(R.id.chip_sort_desc)
        sortAsc?.setOnClickListener {
            sortAsc?.chipType = ChipsUnify.TYPE_SELECTED
            sortDesc?.chipType = ChipsUnify.TYPE_NORMAL
            makeApiCall()
            order = CatalogLibraryConstant.ASCENDING_ORDER_STR
            CatalogAnalyticsLihatSemuaPage.sendClickAscendingDescendingSortEvent(
                "${CatalogLibraryConstant.GRID_VIEW_STR} - ${CatalogLibraryConstant.DESCENDING_ORDER_STR}" +
                    " - click sort: ${CatalogLibraryConstant.ASCENDING_ORDER_STR}",
                userSessionInterface?.userId ?: ""
            )
        }
        sortDesc?.setOnClickListener {
            sortAsc?.chipType = ChipsUnify.TYPE_NORMAL
            sortDesc?.chipType = ChipsUnify.TYPE_SELECTED
            makeApiCall(DESC_SORT_ORDER)
            order = CatalogLibraryConstant.DESCENDING_ORDER_STR
            CatalogAnalyticsLihatSemuaPage.sendClickAscendingDescendingSortEvent(
                "${CatalogLibraryConstant.GRID_VIEW_STR} - ${CatalogLibraryConstant.ASCENDING_ORDER_STR}" +
                    " - click sort: ${CatalogLibraryConstant.DESCENDING_ORDER_STR}",
                userSessionInterface?.userId ?: ""
            )
        }
        if (isOriginBrand) {
            sortAsc?.hide()
            sortDesc?.hide()
        }
    }

    private fun setupRecyclerView(view: View) {
        catalogLihatPageRecyclerView = view.findViewById(R.id.catalog_lihat_semua_rv)
        catalogLihatPageRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = catalogLibraryAdapter
            setHasFixedSize(true)
            if (!isOriginBrand) {
                this.setPadding(0, 0, 0, 110.toPx())
            }
        }
    }

    private fun setObservers() {
        lihatViewModel?.catalogLihatLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_LIHAT_SEMUA)
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
        catalogLihatPageRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDM>) {
        catalogLibraryAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        catalogLihatPageRecyclerView?.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }

        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogLihatPageRecyclerView?.show()
            globalError?.hide()
            makeApiCall()
        }
    }

    override fun onCategoryItemClicked(categoryIdentifier: String?) {
        super.onCategoryItemClicked(categoryIdentifier)
        RouteManager.route(
            context,
            "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryIdentifier"
        )
    }

    override fun onCategoryItemClicked(categoryIdentifier: String?, categoryName: String) {
        super.onCategoryItemClicked(categoryIdentifier, categoryName)
        CatalogAnalyticsBrandLandingPage.sendClickCategoryOnBottomSheetEvent(
            "brand page: $brandName - $brandId - category: $categoryName - $categoryIdentifier",
            categoryIdentifier ?: "",
            userSessionInterface?.userId ?: ""
        )
        (parentFragment as? CatalogLibraryComponentBottomSheet)?.onChangeCategory(categoryName, categoryIdentifier ?: "")
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.apply {
            updateModel(
                CatalogShimmerDM(
                    CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                    CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                    CatalogLibraryConstant.CATALOG_SHIMMER_LIHAT_SEMUA
                )
            )
            updateUi()
        }
    }

    override fun onAccordionStateChange(expanded: Boolean, element: CatalogLihatDM) {
        super.onAccordionStateChange(expanded, element)
        if (isOriginBrand) {
            val expandCollapse = if (expanded) "expand" else "collapse"
            CatalogAnalyticsBrandLandingPage.sendClickCollapseExpandOnBottomSheetEvent(
                "brand page: ${"brandName"} - $brandId - category: ${element.catalogLibraryDataList?.rootCategoryName} - ${element.catalogLibraryDataList?.rootCategoryId} - action: $expandCollapse",
                userSessionInterface?.userId ?: ""
            )
        } else {
            val expandCollapse = if (expanded) "open" else "close"
            CatalogAnalyticsLihatSemuaPage.sendClickDropUpButtonEvent(
                "L1 name: ${element.catalogLibraryDataList?.rootCategoryName} - L1 ID: ${element.catalogLibraryDataList?.rootCategoryId} - action: $expandCollapse - sort & filter: grid view - $order",
                userSessionInterface?.userId ?: ""
            )
        }
    }

    override fun categoryListImpression(
        trackerId: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        parentCategoryName: String,
        parentCategoryId: String,
        categoryName: String,
        categoryId: String,
        isGrid: Boolean,
        isAsc: Boolean,
        position: Int,
        userId: String
    ) {
        val uniqueTrackingKey =
            "$eventAction-$parentCategoryId-$position"
        if (!categoryTrackingSet.contains(uniqueTrackingKey)) {
            CatalogAnalyticsLihatSemuaPage.sendImpressionOnCategoryListEvent(
                trackerId, eventCategory, eventAction,
                extractEventLabel(eventLabel, trackerId, categoryName, categoryId),
                trackingQueue,
                parentCategoryName,
                parentCategoryId,
                categoryName,
                categoryId,
                isGrid,
                isAsc,
                position,
                userId
            )
            categoryTrackingSet.add(uniqueTrackingKey)
        }
    }

    private fun extractEventLabel(eventLabel: String, trackerId: String, categoryName: String, categoryId: String): String {
        return if (trackerId == IMPRESSION_ON_CATEGORY_LIST_BRAND_LANDING) {
            "brand page: $brandName - $brandId - category: $categoryName - $categoryId"
        } else {
            eventLabel
        }
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }
}
