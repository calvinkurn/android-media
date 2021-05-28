package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE
import com.tokopedia.shop.common.constant.ShopPageConstant.EMPTY_PRODUCT_SEARCH_IMAGE_URL
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseRules
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.util.ShopPageProductChangeGridRemoteConfig
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListResultViewModel
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.createIntent
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment
import com.tokopedia.shop.search.view.viewmodel.ShopSearchProductViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import javax.inject.Inject

class ShopPageProductListResultFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        WishListActionListener, BaseEmptyViewHolder.Callback, ShopProductClickedListener,
        ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
        ShopProductImpressionListener, ShopProductEmptySearchListener, ShopProductChangeGridSectionListener,
        SortFilterBottomSheet.Callback {

    interface ShopPageProductListResultFragmentListener {
        fun onSortValueUpdated(sortValue: String)
        fun updateShopInfo(shopInfo: ShopInfo)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopPageProductListResultViewModel

    private var shopPageTracking: ShopPageTrackingBuyer? = null
    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }
    private var shopId: String? = null
    private var shopName: String? = null
    private var shopRef: String = ""
    private var keyword: String = ""
    private var productListName: String = ""
    private var sortId
        get() = shopProductFilterParameter?.getSortId().orEmpty()
        set(value) {
            shopProductFilterParameter?.setSortId(value)
        }
    private val sortName
        get() = if (::viewModel.isInitialized) {
            viewModel.getSortNameById(sortId)
        } else {
            ""
        }
    private var sourceRedirection: String = ""
    private var isShopPageProductSearchResultTrackerAlreadySent: Boolean = false
    private var attribution: String? = null
    private var selectedEtalaseList: ArrayList<ShopEtalaseItemDataModel>? = null
    private var isNeedToReloadData: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var shopInfo: ShopInfo? = null
    private var selectedEtalaseId: String = ""
    private var selectedEtalaseName: String = ""
    private var defaultEtalaseName = ""
    private var selectedEtalaseType: Int = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
    private var selectedEtalaseRules: List<ShopEtalaseRules>? = null
    private var onShopProductListFragmentListener: OnShopProductListFragmentListener? = null
    private var shopPageProductListResultFragmentListener: ShopPageProductListResultFragmentListener? = null
    private var needReloadData: Boolean = false
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var threeDotsClickShopProductUiModel: ShopProductUiModel? = null

    private var shopProductSortFilterUiModel: ShopProductSortFilterUiModel? = null
    private var keywordEmptyState = ""
    private var isEmptyState = false
    private var isAlreadyCheckRestrictionInfo = false
    private var remoteConfig: RemoteConfig? = null
    private var shopProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var partialShopNplFollowersViewLayout: View? = null
    private var partialShopNplFollowersView: PartialButtonShopFollowersView? = null

    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private val isMyShop: Boolean
        get() = if (::viewModel.isInitialized) {
            shopId?.let { viewModel.isMyShop(it) } ?: false
        } else false

    private val isLogin: Boolean
        get() = if (::viewModel.isInitialized) {
            viewModel.isLogin
        } else false

    private val userId: String
        get() = if (::viewModel.isInitialized) {
            viewModel.userId
        } else "0"
    var localCacheModel: LocalCacheModel? = null

    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        return ShopProductAdapterTypeFactory(
                null,
                this,
                this,
                null,
                this,
                this,
                null,
                null,
                this,
                this,
                true,
                0,
                ShopTrackProductTypeDef.PRODUCT
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {
        return ShopProductAdapter(adapterTypeFactory)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.urlRes = EMPTY_PRODUCT_SEARCH_IMAGE_URL
        if (TextUtils.isEmpty(keyword)) {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.title = getString(R.string.shop_product_empty_title_desc)
            } else {
                emptyModel.title = getString(R.string.shop_product_empty_title_etalase_desc)
            }
        } else {
            emptyModel.title = getString(R.string.shop_product_empty_product_title)
        }
        emptyModel.content = getString(R.string.shop_product_empty_product_content)
        return emptyModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        remoteConfig = FirebaseRemoteConfigImpl(context)
        arguments?.let { attribution = it.getString(ShopParamConstant.EXTRA_ATTRIBUTION, "") }
        sourceRedirection = arguments?.getString(ShopParamConstant.EXTRA_SOURCE_REDIRECTION, "").orEmpty()
        if (savedInstanceState == null) {
            selectedEtalaseList = ArrayList()
            arguments?.let {
                selectedEtalaseId = it.getString(ShopParamConstant.EXTRA_ETALASE_ID, "")
                selectedEtalaseName = ""
                keyword = it.getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "")
                sortId = it.getString(ShopParamConstant.EXTRA_SORT_ID, Integer.MIN_VALUE.toString())
                shopId = it.getString(ShopParamConstant.EXTRA_SHOP_ID, "")
                shopRef = it.getString(ShopParamConstant.EXTRA_SHOP_REF, "")
                isNeedToReloadData = it.getBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
            }
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST)
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            selectedEtalaseType = savedInstanceState.getInt(SAVED_SELECTED_ETALASE_TYPE, SELECTED_ETALASE_TYPE_DEFAULT_VALUE)
            keyword = savedInstanceState.getString(SAVED_KEYWORD) ?: ""
//            sortId = savedInstanceState.getString(SAVED_SORT_VALUE, "")
            shopId = savedInstanceState.getString(SAVED_SHOP_ID)
            shopRef = savedInstanceState.getString(SAVED_SHOP_REF).orEmpty()
            needReloadData = savedInstanceState.getBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
            shopProductFilterParameter = savedInstanceState.getParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER)
            isShopPageProductSearchResultTrackerAlreadySent = savedInstanceState.getBoolean(SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT, false)
        }
        shopPageProductListResultFragmentListener?.onSortValueUpdated(sortId ?: "")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        context?.let {
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListResultViewModel::class.java)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_product_list_result_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        observeLiveData()
    }

    private fun updateCurrentPageLocalCacheModelData() {
        localCacheModel = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return staggeredGridLayoutManager
    }

    override fun onSwipeRefresh() {
        viewModel.clearCache()
        // check RE for showcase type campaign eligibility
        loadShopRestrictionInfo()
        super.onSwipeRefresh()
    }

    public override fun loadInitialData() {
        updateCurrentPageLocalCacheModelData()
        isLoadingInitialData = true
        shopProductAdapter.clearProductList()
        shopProductAdapter.clearAllNonDataElement()
        showLoading()
        if (isNeedToReloadData) {
            viewModel.clearCache()
        }
        loadData(defaultInitialPage)
    }

    override fun loadData(page: Int) {
        shopInfo?.let {
            viewModel.getShopFilterData(
                    it,
                    isMyShop
            )
        } ?: viewModel.getShop(shopId, isRefresh = isNeedToReloadData)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView?.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopProductAdapter.showLoading()
                shopInfo?.let {
                    loadProductData(it, page)
                }
            }
        }
    }

    private fun loadShopRestrictionInfo() {
        // hit restriction engine when showcase type is -2 (campaign type) and rules name "followers_only"
        selectedEtalaseRules?.let { rules ->
            if(selectedEtalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN && rules.contains(ShopEtalaseRules(name = ShowcaseRulesName.FOLLOWERS_ONLY))) {
                val userIdFromSession = userId.toIntOrZero()
                val campaignId = if(selectedEtalaseId.contains("_")) {
                    // get campaign id from showcaseId cmp_****
                    selectedEtalaseId.split("_").let { it[1].toIntOrZero() }
                } else 0
                val restrictionParam = RestrictionEngineRequestParams().apply {
                    userId = userIdFromSession
                    dataRequest = mutableListOf(
                        RestrictionEngineDataRequest(
                                product = RestrictionEngineDataRequestProduct(productID = 0.toString()),
                                shop = RestrictionEngineDataRequestShop(shopID = shopId.toIntOrZero()),
                                campaign = RestrictionEngineDataRequestCampaign(campaignID = campaignId)
                        )
                    )
                }
                viewModel.getShopRestrictionInfo(restrictionParam, shopId.toString())
            }
        }
    }

    private fun loadProductData(shopInfo: ShopInfo, page: Int) {
        context?.let{
            viewModel.getShopProduct(
                    shopInfo.shopCore.shopID,
                    page,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    selectedEtalaseId,
                    keyword,
                    selectedEtalaseType,
                    shopProductFilterParameter ?: ShopProductFilterParameter(),
                    localCacheModel ?: LocalCacheModel()
            )
        }
    }

    private fun toggleFavoriteShop(shopId: String) {
        viewModel.toggleFavorite(shopId, this::onSuccessToggleFavoriteShop, this::onErrorToggleFavoriteShop)
    }

    private fun onSuccessToggleFavoriteShop(successVal: Boolean) {
        if(successVal) {
            // get new shop info to sync already favorited data
            hideShopFollowersView()
            showToastSuccess(
                    message = getString(R.string.text_success_follow_shop),
                    ctaText = getString(R.string.shop_page_product_action_no_upload_product),
                    ctaAction = View.OnClickListener {
                        shopPageTracking?.clickCTASuccessFollowNplToaster(
                                shopId,
                                userId,
                                customDimensionShopPage
                        )
                    }
            )
        }
    }

    private fun onErrorToggleFavoriteShop(error: Throwable) {
        activity?.run {
            NetworkErrorHelper.showCloseSnackbar(this,
                    com.tokopedia.network.utils.ErrorHandler.getErrorMessage(this, error)
            )
        }
    }

    private fun loadProductDataEmptyState(shopInfo: ShopInfo, page: Int) {
        selectedEtalaseId = ""
        sortId = SORT_NEWEST
        context?.let {
            viewModel.getShopProductEmptyState(
                    shopInfo.shopCore.shopID,
                    page,
                    ShopPageConstant.SHOP_PRODUCT_EMPTY_STATE_LIMIT,
                    sortId.toIntOrZero(),
                    selectedEtalaseId,
                    keywordEmptyState,
                    localCacheModel ?: LocalCacheModel()
            )
        }
    }

    private fun initRecyclerView(view: View) {
        recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    private fun observeLiveData() {
        viewModel.shopInfoResp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> {
                    onErrorGetShopInfo(it.throwable)
                    val throwable = it.throwable
                    if (!ShopUtil.isExceptionIgnored(throwable)) {
                        ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeLiveData.name,
                                liveDataName = ShopPageProductListResultViewModel::shopInfoResp.name,
                                userId = userId,
                                shopId = shopId.orEmpty(),
                                shopName = shopName.orEmpty(),
                                errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                        )
                    }
                }
            }
        })

        viewModel.shopSortFilterData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetSortFilterData(it.data)
                is Fail -> {
                    val throwable = it.throwable
                    if (!ShopUtil.isExceptionIgnored(throwable)) {
                        ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeLiveData.name,
                                liveDataName = ShopPageProductListResultViewModel::shopSortFilterData.name,
                                userId = userId,
                                shopId = shopId.orEmpty(),
                                shopName = shopName.orEmpty(),
                                errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                        )
                    }
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val productList = it.data.listShopProductUiModel
                    val hasNextPage = it.data.hasNextPage
                    val totalProductData =  it.data.totalProductData
                    if(!isShopPageProductSearchResultTrackerAlreadySent) {
                        when (sourceRedirection) {
                            ShopSearchProductFragment.SEARCH_SUBMIT_RESULT_REDIRECTION -> sendShopPageProductSearchResultTracker(productList.isEmpty())
                            ShopSearchProductFragment.ETALASE_CLICK_RESULT_REDIRECTION -> sendShopPageProductSearchClickEtalaseProductResultTracker(productList.isEmpty())
                        }
                    }
                    renderProductList(productList, hasNextPage, totalProductData)
                    if(!isAlreadyCheckRestrictionInfo) {
                        loadShopRestrictionInfo()
                    }
                    isNeedToReloadData = false
                    productListName = it.data.listShopProductUiModel.joinToString(","){ product -> product.name.orEmpty() }
                }
                is Fail -> {
                    val throwable = it.throwable
                    if (!ShopUtil.isExceptionIgnored(throwable)) {
                        ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeLiveData.name,
                                liveDataName = ShopPageProductListResultViewModel::productData.name,
                                userId = userId,
                                shopId = shopId.orEmpty(),
                                shopName = shopName.orEmpty(),
                                errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                        )
                    }
                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productDataEmpty.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderProductListEmptyState(it.data)
                is Fail -> showGetListError(it.throwable)
            }
        })

        viewModel.bottomSheetFilterLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetBottomSheetFilterData(it.data)
                }
            }
        })

        viewModel.shopProductFilterCountLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductFilterCount(it.data)
                }
            }
        })

        viewModel.restrictionEngineData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val shopRestrictionData = it.data
                    // handle view to follow shop for campaign type showcase
                    val isFollowShop = shopRestrictionData.status != PRODUCT_INELIGIBLE
                    if(isLogin && !isMyShop && !isFollowShop) {
                        val title = shopRestrictionData.actions[0].title
                        val description = shopRestrictionData.actions[0].description
                        val buttonLabel = shopRestrictionData.buttonLabel
                        val voucherIconUrl = shopRestrictionData.voucherIconUrl
                        showShopFollowersView(title, description, isFollowShop, buttonLabel, voucherIconUrl)
                    }
                }
                is Fail -> { }
            }
        })
    }

    private fun sendShopPageProductSearchResultTracker(isProductResultListEmpty: Boolean) {
        isShopPageProductSearchResultTrackerAlreadySent = true
        shopPageTracking?.sendShopPageProductSearchResultTracker(
                isMyShop, keyword, isProductResultListEmpty, customDimensionShopPage
        )
    }

    private fun sendShopPageProductSearchClickEtalaseProductResultTracker(isProductResultListEmpty: Boolean) {
        isShopPageProductSearchResultTrackerAlreadySent = true
        shopPageTracking?.sendShopPageProductSearchClickEtalaseProductResultTracker(
                isMyShop, keyword, isProductResultListEmpty, customDimensionShopPage
        )
    }

    private fun onSuccessGetShopProductFilterCount(count: Int) {
        val countText = String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                count.thousandFormatted()
        )
        sortFilterBottomSheet?.setResultCountText(countText)
    }

    private fun onSuccessGetBottomSheetFilterData(model: DynamicFilterModel) {
        model.defaultSortValue = DEFAULT_SORT_ID
        sortFilterBottomSheet?.setDynamicFilterModel(model)
    }

    private fun renderProductList(
            productList: List<ShopProductUiModel>,
            hasNextPage: Boolean,
            totalProductData: Int
    ) {
        hideLoading()
        shopProductAdapter.clearAllNonDataElement()
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList()
            endlessRecyclerViewScrollListener.resetState()

            if (productList.isNotEmpty()) {
                shopProductSortFilterUiModel?.let { shopProductAdapter.setSortFilterData(it) }
                if(ShopPageProductChangeGridRemoteConfig.isFeatureEnabled(remoteConfig)) {
                    changeProductListGridView(ShopProductViewGridType.SMALL_GRID)
                }
            }
        }

        if (productList.isEmpty() && isLoadingInitialData) {
            showLoading()
            shopInfo?.let { loadProductDataEmptyState(it, defaultInitialPage) }
            isEmptyState = true
        } else {
            shopProductAdapter.updateShopPageProductChangeGridSectionIcon(totalProductData)
            shopProductAdapter.setProductListDataModel(productList)
            updateScrollListenerState(hasNextPage)
            isLoadingInitialData = false
        }
    }

    private fun prepareShopFollowersView() {
        partialShopNplFollowersViewLayout = view?.findViewById(R.id.npl_follow_view)
        partialShopNplFollowersViewLayout?.visible()
        view?.let {
            recyclerView?.setPadding(
                    toDp(12),
                    toDp(0),
                    toDp(12),
                    toDp(82)
            )
            partialShopNplFollowersViewLayout?.translationY = it.height.toFloat()
        }
    }

    private fun showShopFollowersView(title: String, desc: String, isFollowShop: Boolean, buttonLabel: String?, voucherIconUrl: String?) {
        prepareShopFollowersView()
        partialShopNplFollowersViewLayout?.let {
            partialShopNplFollowersView = PartialButtonShopFollowersView.build(it, object: PartialButtonShopFollowersListener {
                override fun onButtonFollowNplClick() {
                    shopPageTracking?.clickFollowShowcaseNplButton(
                            shopId,
                            userId,
                            customDimensionShopPage
                    )
                    shopId?.let { shopId -> toggleFavoriteShop(shopId) }
                }
            })
            partialShopNplFollowersView?.renderView(title, desc, isFollowShop, buttonLabel, voucherIconUrl)
        }
    }

    private fun hideShopFollowersView() {
        recyclerView?.setPadding(
                toDp(12),
                toDp(0),
                toDp(12),
                toDp(8)
        )
        partialShopNplFollowersView?.setupVisibility = false
        partialShopNplFollowersViewLayout?.invisible()
    }

    private fun toDp(number: Int): Int {
        return (number * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    private fun renderProductListEmptyState(productList: List<ShopProductUiModel>) {
        hideLoading()
        shopProductAdapter.clearAllElements()
        shopProductAdapter.addEmptyStateData(productList)
    }

    override fun onItemClicked(baseShopProductViewModel: BaseShopProductViewModel) {
        // no op
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun showGetListError(throwable: Throwable) {
        hideLoading()
        updateStateScrollListener()
        if (shopProductAdapter.shopProductUiModelList.size > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    override fun onProductClicked(shopProductUiModel: ShopProductUiModel, @ShopTrackProductTypeDef shopTrackType: Int,
                                  productPosition: Int) {
        if (!isEmptyState) {
            val isEtalaseCampaign = shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN
            shopPageTracking?.clickProductSearchResult(
                    isMyShop,
                    isLogin,
                    getSelectedEtalaseChip(),
                    "",
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductUiModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductUiModel,
                    productPosition + 1,
                    shopId.orEmpty(),
                    isEtalaseCampaign,
                    shopProductUiModel.isUpcoming,
                    keyword,
                    shopProductUiModel.etalaseType ?: DEFAULT_VALUE_ETALASE_TYPE
            )
        } else {
            shopPageTracking?.clickProductListEmptyState(
                    isLogin,
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductUiModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductUiModel,
                    productPosition + 1,
                    shopId.orEmpty()
            )
        }
        startActivity(getProductIntent(shopProductUiModel.id ?: "", attribution,
                shopPageTracking?.getListNameOfProduct(OldShopPageTrackingConstant.SEARCH, getSelectedEtalaseChip())
                        ?: ""))
    }

    override fun onProductImpression(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int) {
        if (!isEmptyState) {
            val isEtalaseCampaign = shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN
            shopPageTracking?.impressionProductListSearchResult(
                    isMyShop,
                    isLogin,
                    getSelectedEtalaseChip(),
                    "",
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductUiModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductUiModel,
                    productPosition + 1,
                    shopId.orEmpty(),
                    isEtalaseCampaign,
                    shopProductUiModel.isUpcoming,
                    keyword,
                    shopProductUiModel.etalaseType ?: DEFAULT_VALUE_ETALASE_TYPE
            )
        } else {
            shopPageTracking?.impressionProductListEmptyState(
                    isLogin,
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductUiModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductUiModel,
                    productPosition + 1,
                    shopId.orEmpty()
            )
        }
    }

    override fun getSelectedEtalaseName(): String {
        return getSelectedEtalaseChip()
    }

    private fun getProductIntent(productId: String, attribution: String?, listNameOfProduct: String): Intent? {
        return if (context != null) {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun onSuccessAddWishlist(productId: String) {
        showToastSuccess(
                message = getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist),
                ctaText = getString(com.tokopedia.wishlist.common.R.string.lihat_label),
                ctaAction = {
                    goToWishlist()
                }
        )
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    override fun onErrorAddWishList(errorMessage: String, productId: String) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    private fun onErrorAddToWishList(e: Throwable) {
        if (!viewModel.isLogin) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
        }
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onEmptyButtonClicked() {
        redirectToSearchResultPage()
    }

    private fun redirectToSearchResultPage() {
        RouteManager.route(
                context,
                "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword"
        )
    }

    private fun showToastSuccess(message: String, ctaText: String = "", ctaAction: View.OnClickListener? = null) {
        activity?.run {
            ctaAction?.let { ctaClickListener ->
                Toaster.build(findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        ctaText,
                        ctaClickListener
                ).show()
            } ?: Toaster.build(findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText
            ).show()
        }
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.shopId = shopInfo.shopCore.shopID
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        this.shopName = shopInfo.shopCore.name
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
        onShopProductListFragmentListener?.updateUIByShopName(shopInfo.shopCore.name)
        shopPageProductListResultFragmentListener?.updateShopInfo(shopInfo)
        loadData(defaultInitialPage)
    }

    private fun onErrorGetShopInfo(e: Throwable) {
        showGetListError(e)
    }

    override fun onThreeDotsClicked(shopProductUiModel: ShopProductUiModel, @ShopTrackProductTypeDef shopTrackType: Int) {
        threeDotsClickShopProductUiModel = shopProductUiModel
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopProductUiModel.isWishList,
                        productId = shopProductUiModel.id ?: ""
                )
        )
    }

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConsInternalHome.HOME_WISHLIST)
    }

    private fun onSuccessGetSortFilterData(shopStickySortFilter: ShopStickySortFilter) {
        val etalaseList = shopStickySortFilter.etalaseList
        defaultEtalaseName = etalaseList.firstOrNull()?.etalaseName.orEmpty()
        selectedEtalaseId = shopStickySortFilter.etalaseList.firstOrNull { isEtalaseMatch(it) }?.etalaseId
                ?: ""
        sortId = shopStickySortFilter.sortList.firstOrNull { it.value == sortId }?.value ?: ""
        selectedEtalaseName = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.etalaseName
                ?: ""
        selectedEtalaseType = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.type
                ?: -1
        selectedEtalaseRules = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.etalaseRules
        val selectedSortName = shopStickySortFilter.sortList.firstOrNull { it.value == sortId }?.name
                ?: ""
        shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
                selectedEtalaseId = selectedEtalaseId.takeIf { it.isNotEmpty() } ?: "",
                selectedEtalaseName = selectedEtalaseName.takeIf { it.isNotEmpty() } ?: "",
                selectedSortId = sortId,
                selectedSortName = selectedSortName,
                isShowSortFilter = isNeededToShowSortFilter(),
                filterIndicatorCounter = getIndicatorCount(
                        shopProductFilterParameter?.getMapData()
                )
        )
        if (!isEmptyState) {
            context?.let{
                viewModel.getShopProduct(
                        shopId ?: "",
                        defaultInitialPage,
                        ShopPageConstant.DEFAULT_PER_PAGE,
                        selectedEtalaseId,
                        keyword,
                        selectedEtalaseType,
                        shopProductFilterParameter ?: ShopProductFilterParameter(),
                        localCacheModel ?: LocalCacheModel()
                )
            }
        } else {
            hideLoading()
            endlessRecyclerViewScrollListener.resetState()
        }
    }

    private fun isNeededToShowSortFilter(): Boolean {
        return selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_CAMPAIGN &&
                selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN
    }

    private fun isEtalaseMatch(model: ShopEtalaseItemDataModel): Boolean {
        return (model.etalaseId.toLowerCase() == selectedEtalaseId.toLowerCase() ||
                model.etalaseName.toLowerCase() == selectedEtalaseId.toLowerCase() ||
                model.alias.toLowerCase() == selectedEtalaseId.toLowerCase()) && selectedEtalaseId.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                data?.let {
                    selectedEtalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    selectedEtalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME)
                    selectedEtalaseType = data.getIntExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_TYPE, SELECTED_ETALASE_TYPE_DEFAULT_VALUE)
                    needReloadData = data.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)

                    shopPageTracking?.clickEtalaseChip(
                            isMyShop,
                            getSelectedEtalaseChip(),
                            CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
                    )

                    shopPageTracking?.clickMoreMenuChip(
                            isMyShop,
                            getSelectedEtalaseChip(),
                            customDimensionShopPage
                    )
                    shopInfo?.let {
                        shopPageTracking?.clickMenuFromMoreMenu(viewModel.isMyShop(it.shopCore.shopID),
                                selectedEtalaseName, CustomDimensionShopPage.create(it.shopCore.shopID, isOfficialStore, isGoldMerchant))
                    }
                    shopProductAdapter.changeSelectedEtalaseFilter(selectedEtalaseId, selectedEtalaseName)
                    shopProductAdapter.refreshSticky()
                    if (needReloadData) {
                        isNeedToReloadData = true
                        loadInitialData()
                        needReloadData = false
                    }
                }
            }

            REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                data?.let {
                    sortId = it.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    val sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    shopPageTracking?.sortProduct(sortName, isMyShop, customDimensionShopPage)
                    this.isLoadingInitialData = true
                    shopProductAdapter.changeSelectedSortFilter(sortId ?: "", sortName)
                    shopProductAdapter.changeSortFilterIndicatorCounter(getIndicatorCount(
                            shopProductFilterParameter?.getMapData()
                    ))
                    shopProductAdapter.refreshSticky()
                    loadInitialData()
                }
            }
            else -> {
            }
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        } else {
            shopInfo?.let {
                val isEtalaseCampaign = threeDotsClickShopProductUiModel?.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                        threeDotsClickShopProductUiModel?.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN

                //shopTrackType is always from Product
                shopPageTracking?.clickWishlistProductResultPage(
                        !productCardOptionsModel.isWishlisted,
                        isLogin,
                        getSelectedEtalaseChip(),
                        CustomDimensionShopPageProduct.create(
                                it.shopCore.shopID,
                                it.goldOS.isOfficial == 1,
                                it.goldOS.isGold == 1, productCardOptionsModel.productId, shopRef
                        ),
                        isEtalaseCampaign,
                        threeDotsClickShopProductUiModel?.isUpcoming ?: false,
                        threeDotsClickShopProductUiModel?.etalaseType ?: DEFAULT_VALUE_ETALASE_TYPE
                )
            }

            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }
        threeDotsClickShopProductUiModel = null
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel.clearGetShopProductUseCase()

        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlist(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlist(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessAddWishlist(productCardOptionsModel.productId)
        } else {
            onErrorAddWishList(getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist), productCardOptionsModel.productId)
        }
    }

    private fun handleWishlistActionRemoveFromWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishlist(productCardOptionsModel.productId)
        } else {
            onErrorRemoveWishlist(getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist), productCardOptionsModel.productId)
        }
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
        hideShopFollowersView()
    }

    override fun onResume() {
        super.onResume()
        if(selectedEtalaseRules != null) {
            // check RE for showcase type campaign eligibility
            isAlreadyCheckRestrictionInfo = true
            loadShopRestrictionInfo()
        }
        checkIfChooseAddressWidgetDataUpdated()
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        context?.let{context ->
            localCacheModel?.let{
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        context,
                        it
                )
                if(isUpdated)
                    onSwipeRefresh()
            }
        }
    }

    override fun initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onDestroy() {
        viewModel.shopSortFilterData.removeObservers(this)
        viewModel.shopInfoResp.removeObservers(this)
        viewModel.productData.removeObservers(this)
        viewModel.bottomSheetFilterLiveData.removeObservers(this)
        viewModel.shopProductFilterCountLiveData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopProductListFragmentListener = context as OnShopProductListFragmentListener
        shopPageProductListResultFragmentListener = context as ShopPageProductListResultFragmentListener

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putInt(SAVED_SELECTED_ETALASE_TYPE, selectedEtalaseType)
        outState.putString(SAVED_SORT_VALUE, sortId)
        outState.putString(SAVED_KEYWORD, keyword)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
        outState.putParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER, shopProductFilterParameter)
        outState.putBoolean(SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT, isShopPageProductSearchResultTrackerAlreadySent)
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopInfo!!.shopCore.shopID)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, customDimensionShopPage.shopType)

            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(EXTRA_BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    private fun redirectToShopSortPickerPage() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortId)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    private fun getSelectedEtalaseChip(): String {
        return selectedEtalaseName.takeIf { it.isNotEmpty() } ?: defaultEtalaseName
    }

    companion object {

        private val REQUEST_CODE_USER_LOGIN = 100
        private val REQUEST_CODE_ETALASE = 200

        @JvmStatic
        val REQUEST_CODE_SORT = 300

        private val GRID_SPAN_COUNT = 2
        private const val SORT_NEWEST = "1"
        private const val ALREADY_FOLLOW_SHOP = 1
        private const val PRODUCT_INELIGIBLE = "ineligible"

        val SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list"
        val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        val SAVED_SELECTED_ETALASE_TYPE = "saved_etalase_type"
        val SAVED_SHOP_ID = "saved_shop_id"
        val SAVED_SHOP_REF = "saved_shop_ref"
        val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        val SAVED_KEYWORD = "saved_keyword"
        val SAVED_SORT_VALUE = "saved_sort_name"
        val SAVED_SHOP_PRODUCT_FILTER_PARAMETER = "SAVED_SHOP_PRODUCT_FILTER_PARAMETER"
        val SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT = "SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT"
        private const val SELECTED_ETALASE_TYPE_DEFAULT_VALUE = -10

        @JvmStatic
        fun createInstance(shopId: String,
                           shopRef: String?,
                           keyword: String?,
                           etalaseId: String?,
                           sort: String?,
                           attribution: String?,
                           isNeedToReloadData: Boolean? = false,
                           sourceRedirection: String? = ""
        ): ShopPageProductListResultFragment = ShopPageProductListResultFragment().also {
            it.arguments = Bundle().apply {
                putString(ShopParamConstant.EXTRA_SHOP_ID, shopId)
                putString(ShopParamConstant.EXTRA_SHOP_REF, shopRef.orEmpty())
                putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword ?: "")
                putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId ?: "")
                putString(ShopParamConstant.EXTRA_SORT_ID, sort ?: "")
                putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution ?: "")
                putBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData
                        ?: false)
                putString(ShopParamConstant.EXTRA_SOURCE_REDIRECTION, sourceRedirection.orEmpty())
            }
        }
    }

    override fun onEtalaseFilterClicked() {
        redirectToEtalasePicker()
    }

    override fun onSortFilterClicked() {
        shopPageTracking?.clickSort(isMyShop, customDimensionShopPage)
        redirectToShopSortPickerPage()
    }

    override fun onClearFilterClicked() {
        if (shopProductAdapter.isLoading) {
            return
        }
        shopPageTracking?.clickClearFilter(
                isMyShop,
                customDimensionShopPage
        )
        sortId = ""
        shopProductAdapter.changeSelectedSortFilter(sortId ?: "", sortName)
        shopProductAdapter.changeSortFilterIndicatorCounter(getIndicatorCount(
                shopProductFilterParameter?.getMapData()
        ))
        selectedEtalaseId = ""
        selectedEtalaseName = ""
        selectedEtalaseType = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
        shopProductAdapter.changeSelectedEtalaseFilter(selectedEtalaseId, selectedEtalaseName)
        shopProductAdapter.refreshSticky()
        loadInitialData()
    }

    override fun setSortFilterMeasureHeight(measureHeight: Int) { }
    override fun onFilterClicked() {
        shopPageTracking?.clickFilterChips(productListName, customDimensionShopPage)
        showBottomSheetFilter()
    }

    private fun showBottomSheetFilter() {
        val mapParameter = if (sortId.isNotEmpty())
            shopProductFilterParameter?.getMapData()
        else
            shopProductFilterParameter?.getMapDataWithDefaultSortId()
        sortFilterBottomSheet = SortFilterBottomSheet()
        sortFilterBottomSheet?.show(
                requireFragmentManager(),
                mapParameter,
                null,
                this
        )
        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
        }
        viewModel.getBottomSheetFilterData()
    }


    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        val isResetButtonVisible = sortFilterBottomSheet?.bottomSheetAction?.isVisible
        sortFilterBottomSheet = null
        shopProductFilterParameter?.clearParameter()
        shopProductFilterParameter?.setMapData(applySortFilterModel.mapParameter)
        sortId = if (isResetButtonVisible == false) {
            ""
        } else {
            shopProductFilterParameter?.getSortId().orEmpty()
        }
        val sortName = viewModel.getSortNameById(sortId)
        shopProductAdapter.changeSelectedSortFilter(sortId, sortName)
        shopProductAdapter.changeSortFilterIndicatorCounter(getIndicatorCount(
                shopProductFilterParameter?.getMapData()
        ))
        shopProductAdapter.refreshSticky()
        loadInitialData()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        viewModel.getFilterResultCount(
                shopId.orEmpty(),
                keyword,
                selectedEtalaseId,
                tempShopProductFilterParameter,
                localCacheModel ?: LocalCacheModel()
        )
    }


    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onPrimaryButtonEmptyClicked() {
        shopPageTracking?.clickPrimaryBtnEmptyStateSearch(shopId, shopInfo?.shopHomeType)
        context?.let {
            startActivity(
                    createIntent(
                            it,
                            shopId.orEmpty(),
                            shopInfo?.shopCore?.name.orEmpty(),
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            keyword,
                            attribution,
                            shopRef
                    )
            )
        }
    }

    override fun onSecondaryButtonEmptyClicked() {
        shopPageTracking?.clickSecondaryBtnEmptyStateSearch(shopId, shopInfo?.shopHomeType)
        RouteManager.route(
                context,
                "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword"
        )
    }

    private fun changeProductListGridView(gridType: ShopProductViewGridType){
        shopProductAdapter.updateShopPageProductChangeGridSectionIcon(gridType)
        shopProductAdapter.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(gridType: ShopProductViewGridType) {
        val productListName =  shopProductAdapter.shopProductUiModelList.joinToString(","){
            it.name.orEmpty()
        }
        shopPageTracking?.clickProductListToggle(productListName, isMyShop, customDimensionShopPage)
        changeProductListGridView(gridType)
    }

    private fun applySortFilterTracking(selectedSortName: String, selectedFilterMap: Map<String, String>) {
        if (selectedSortName.isNotBlank()) {
            shopPageTracking?.clickFilterSortBy(productListName, selectedSortName, customDimensionShopPage)
        }
        if (!selectedFilterMap[PMAX_PARAM_KEY].isNullOrBlank() || !selectedFilterMap[PMIN_PARAM_KEY].isNullOrBlank()) {
            shopPageTracking?.clickFilterPrice(productListName, selectedFilterMap[PMIN_PARAM_KEY] ?: "0", selectedFilterMap[PMAX_PARAM_KEY] ?: "0", customDimensionShopPage)
        }
        if (!selectedFilterMap[RATING_PARAM_KEY].isNullOrBlank()) {
            shopPageTracking?.clickFilterRating(productListName, selectedFilterMap[RATING_PARAM_KEY] ?: "0", customDimensionShopPage)
        }
    }

    fun onSearchBarClicked() {
        redirectToShopSearchProduct()
    }

    private fun redirectToShopSearchProduct() {
        context?.let {
            shopPageTracking?.clickSearch(isMyShop, customDimensionShopPage)
            startActivity(createIntent(
                    it,
                    shopId.orEmpty(),
                    shopInfo?.shopCore?.name.orEmpty(),
                    shopInfo?.goldOS?.isOfficial.orZero() == 1,
                    shopInfo?.goldOS?.isGold.orZero() == 1,
                    keyword,
                    attribution,
                    shopRef
            ))
        }
    }

}
