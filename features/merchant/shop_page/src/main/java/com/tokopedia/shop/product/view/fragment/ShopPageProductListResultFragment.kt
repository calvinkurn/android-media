package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.general.MiniCartGeneralWidget
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageEtalaseTracking
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEARCH
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.DEFAULT_SORT_ID
import com.tokopedia.shop.common.constant.IS_FULFILLMENT_KEY
import com.tokopedia.shop.common.constant.ShopCommonExtraConstant
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE
import com.tokopedia.shop.common.constant.ShopPageConstant.EMPTY_PRODUCT_SEARCH_IMAGE_URL
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopParamConstant.EXTRA_FOR_SHOP_SHARING
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.constant.ShowcaseRulesName
import com.tokopedia.shop.common.data.model.AffiliateAtcProductModel
import com.tokopedia.shop.common.data.model.RestrictionEngineDataRequest
import com.tokopedia.shop.common.data.model.RestrictionEngineDataRequestCampaign
import com.tokopedia.shop.common.data.model.RestrictionEngineDataRequestProduct
import com.tokopedia.shop.common.data.model.RestrictionEngineDataRequestShop
import com.tokopedia.shop.common.data.model.RestrictionEngineRequestParams
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseRules
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.model.ShopSharingInShowCaseUiModel
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.shop.databinding.FragmentShopProductListResultNewBinding
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.bottomsheet.ShopEtalaseNotFoundBottomSheet
import com.tokopedia.shop.product.view.datamodel.BaseShopProductViewModel
import com.tokopedia.shop.product.view.datamodel.GetShopProductSuggestionUiModel
import com.tokopedia.shop.product.view.datamodel.GetShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopEtalaseItemDataModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.listener.ShopProductSearchSuggestionListener
import com.tokopedia.shop.product.view.listener.ShopShowcaseEmptySearchListener
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListResultViewModel
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.createIntent
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import java.net.URLEncoder
import javax.inject.Inject

class ShopPageProductListResultFragment :
    BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
    BaseEmptyViewHolder.Callback,
    ShopProductClickedListener,
    ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
    ShopProductImpressionListener,
    ShopProductEmptySearchListener,
    ShopProductChangeGridSectionListener,
    ShopShowcaseEmptySearchListener,
    ShopProductSearchSuggestionListener,
    SortFilterBottomSheet.Callback,
    MiniCartWidgetListener, ShareBottomsheetListener {


    interface ShopPageProductListResultFragmentListener {
        fun onSortValueUpdated(sortValue: String)
        fun updateShopInfo(shopInfo: ShopInfo)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    private val viewModel: ShopPageProductListResultViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(ShopPageProductListResultViewModel::class.java)
    }

    private var shopPageTracking: ShopPageTrackingBuyer? = null
    private var shopPageEtalaseTracking: ShopPageEtalaseTracking? = null

    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }
    private var shopId: String? = null
    private var shopDomain: String = ""
    private var shopName: String? = null
    private var shopRef: String = ""
    private var keyword: String = ""
    private var productListName: String = ""
    var isAffiliate = false

    private var sortId
        get() = shopProductFilterParameter?.getSortId().orEmpty()
        set(value) {
            shopProductFilterParameter?.setSortId(value)
        }
    private val sortName
        get() = viewModel.getSortNameById(sortId)

    private var sourceRedirection: String = ""
    private var isShopPageProductSearchResultTrackerAlreadySent: Boolean = false
    private var attribution: String? = null
    private var selectedEtalaseList: ArrayList<ShopEtalaseItemDataModel>? = null
    private var isNeedToReloadData: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var shopInfo: ShopInfo? = null
    private var selectedEtalaseId: String = ""
    private var selectedEtalaseAlias: String = ""
    private var selectedEtalaseName: String = ""
    private var defaultEtalaseName = ""
    private var selectedEtalaseType: Int = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
    private var selectedEtalaseRules: List<ShopEtalaseRules>? = null
    private var onShopProductListFragmentListener: OnShopProductListFragmentListener? = null
    private var shopPageProductListResultFragmentListener: ShopPageProductListResultFragmentListener? =
        null
    private var needReloadData: Boolean = false
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var threeDotsClickShopProductUiModel: ShopProductUiModel? = null
    private var miniCart: MiniCartGeneralWidget? = null

    private var shopProductSortFilterUiModel: ShopProductSortFilterUiModel? = null
    private var keywordEmptyState = ""
    private var isEmptyState = false
    private var isAlreadyCheckRestrictionInfo = false
    private var remoteConfig: RemoteConfig? = null
    private var shopProductFilterParameter: ShopProductFilterParameter? =
        ShopProductFilterParameter()
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var partialShopNplFollowersViewLayout: View? = null
    private var partialShopNplFollowersView: PartialButtonShopFollowersView? = null
    private var srpPageId = ""
    private var srpPageTitle = ""
    private var navSource = ""
    private var isEnableDirectPurchase: Boolean = false
    private var isFulfillmentFilterActive: Boolean = false
    private var affiliateChannel: String = ""
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private val isMyShop: Boolean
        get() = shopId?.let { viewModel.isMyShop(it) } ?: false

    private val isLogin: Boolean
        get() = viewModel.isLogin

    val userId: String
        get() = viewModel.userId

    var localCacheModel: LocalCacheModel? = null
    private var rvDefaultPaddingBottom = 0
    private val viewBinding: FragmentShopProductListResultNewBinding? by viewBinding()
    private var shopSharingInShowCaseUiModel: ShopSharingInShowCaseUiModel? = null
    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        val userSession = UserSession(context)
        val _shopId = arguments?.getString(ShopParamConstant.EXTRA_SHOP_ID, "") ?: ""
        val _isMyShop =
            ShopUtil.isMyShop(shopId = _shopId, userSessionShopId = userSession.shopId.orEmpty())

        return ShopProductAdapterTypeFactory(
            membershipStampAdapterListener = null,
            shopProductClickedListener = this,
            shopProductImpressionListener = this,
            shopCarouselSeeAllClickedListener = null,
            emptyProductOnClickListener = this,
            shopProductEtalaseListViewHolderListener = this,
            shopProductAddViewHolderListener = null,
            shopProductsEmptyViewHolderListener = null,
            shopProductEmptySearchListener = this,
            shopProductChangeGridSectionListener = this,
            shopShowcaseEmptySearchListener = this,
            shopProductSearchSuggestionListener = this,
            isGridSquareLayout = true,
            deviceWidth = 0,
            shopTrackType = ShopTrackProductTypeDef.PRODUCT,
            isShowTripleDot = !_isMyShop
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
        sourceRedirection =
            arguments?.getString(ShopParamConstant.EXTRA_SOURCE_REDIRECTION, "").orEmpty()
        shopSharingInShowCaseUiModel = arguments?.getParcelable(EXTRA_FOR_SHOP_SHARING)
        if (savedInstanceState == null) {
            selectedEtalaseList = ArrayList()
            arguments?.let {
                selectedEtalaseId = it.getString(ShopParamConstant.EXTRA_ETALASE_ID, "")
                selectedEtalaseName = ""
                keyword = it.getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "")
                sortId = it.getString(ShopParamConstant.EXTRA_SORT_ID, Integer.MIN_VALUE.toString())
                shopId = it.getString(ShopParamConstant.EXTRA_SHOP_ID, "")
                shopDomain = it.getString(ShopParamConstant.EXTRA_SHOP_DOMAIN, "")
                shopRef = it.getString(ShopParamConstant.EXTRA_SHOP_REF, "")
                isNeedToReloadData =
                    it.getBoolean(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
            }
        } else {
            selectedEtalaseList =
                savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST)
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            selectedEtalaseType = savedInstanceState.getInt(
                SAVED_SELECTED_ETALASE_TYPE,
                SELECTED_ETALASE_TYPE_DEFAULT_VALUE
            )
            keyword = savedInstanceState.getString(SAVED_KEYWORD) ?: ""
//            sortId = savedInstanceState.getString(SAVED_SORT_VALUE, "")
            shopId = savedInstanceState.getString(SAVED_SHOP_ID)
            shopDomain = savedInstanceState.getString(SAVED_SHOP_DOMAIN, "")
            shopRef = savedInstanceState.getString(SAVED_SHOP_REF).orEmpty()
            needReloadData =
                savedInstanceState.getBoolean(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
            shopProductFilterParameter =
                savedInstanceState.getParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER)
            isShopPageProductSearchResultTrackerAlreadySent = savedInstanceState.getBoolean(
                SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT,
                false
            )
        }
        getIntentData()
        shopPageProductListResultFragmentListener?.onSortValueUpdated(sortId ?: "")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        context?.let {
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            shopPageEtalaseTracking = ShopPageEtalaseTracking(TrackingQueue(it))
        }
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return viewBinding?.swipeRefreshLayout
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_product_list_result_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        observeLiveData()
        initAffiliateCookie()
        getShopAffiliateChannel()

    }

    private fun getShopAffiliateChannel() {
        viewModel.getShopAffiliateChannel()
    }

    private fun initAffiliateCookie() {
        viewModel.initAffiliateCookie(
            affiliateCookieHelper,
            shopId.orEmpty()
        )
    }

    private fun initView() {
        miniCart = viewBinding?.miniCart
    }


    private fun getIntentData() {
        activity?.intent?.data?.let { it ->
            it.getQueryParameter(SearchApiConst.SRP_PAGE_ID)?.let { srpPageId ->
                sourceRedirection = SEARCH_AUTOCOMPLETE_PAGE_SOURCE
                this@ShopPageProductListResultFragment.srpPageId = srpPageId
            }
            srpPageTitle = it.getQueryParameter(SearchApiConst.SRP_PAGE_TITLE).orEmpty()
            navSource = it.getQueryParameter(SearchApiConst.NAVSOURCE).orEmpty()
        }
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
        isEmptyState = false
        shopProductAdapter.clearShopPageChangeGridSection()
        shopProductAdapter.clearShopPageProductResultEmptyState()
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
                isMyShop,
                isNeedToReloadData
            )
        } ?: run {
            viewModel.getShop(
                shopId = shopId.orEmpty(),
                shopDomain = shopDomain,
                isRefresh = isNeedToReloadData
            )
        }
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
            if (selectedEtalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN && rules.contains(
                    ShopEtalaseRules(name = ShowcaseRulesName.FOLLOWERS_ONLY)
                )
            ) {
                val userIdFromSession = userId.toIntOrZero()
                val campaignId = if (selectedEtalaseId.contains("_")) {
                    // get campaign id from showcaseId cmp_****
                    selectedEtalaseId.split("_").let { it[1].toIntOrZero() }
                } else 0
                val restrictionParam = RestrictionEngineRequestParams().apply {
                    userId = userIdFromSession.toLong()
                    dataRequest = mutableListOf(
                        RestrictionEngineDataRequest(
                            product = RestrictionEngineDataRequestProduct(productID = 0.toString()),
                            shop = RestrictionEngineDataRequestShop(shopID = shopId.toLongOrZero()),
                            campaign = RestrictionEngineDataRequestCampaign(campaignID = campaignId.toLong())
                        )
                    )
                }
                viewModel.getShopRestrictionInfo(restrictionParam, shopId.toString())
            }
        }
    }

    private fun loadProductData(shopInfo: ShopInfo, page: Int) {
        context?.let {
            viewModel.getShopProduct(
                shopInfo.shopCore.shopID,
                page,
                ShopUtil.getProductPerPage(context),
                selectedEtalaseId,
                keyword,
                selectedEtalaseType,
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                localCacheModel ?: LocalCacheModel(),
                isEnableDirectPurchase
            )
        }
    }

    private fun toggleFavoriteShop(shopId: String) {
        viewModel.toggleFavorite(
            shopId,
            this::onSuccessToggleFavoriteShop,
            this::onErrorToggleFavoriteShop
        )
    }

    private fun onSuccessToggleFavoriteShop(successVal: Boolean) {
        if (successVal) {
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
            NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, error))
        }
    }

    private fun loadProductDataEmptyState(shopInfo: ShopInfo, page: Int) {
        sortId = SORT_NEWEST
        context?.let {
            viewModel.getShopProductEmptyState(
                shopInfo.shopCore.shopID,
                page,
                ShopPageConstant.SHOP_PRODUCT_EMPTY_STATE_LIMIT,
                sortId.toIntOrZero(),
                "",
                keywordEmptyState,
                localCacheModel ?: LocalCacheModel(),
                isEnableDirectPurchase
            )
        }
    }

    private fun initRecyclerView(view: View) {
        recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            it.itemAnimator = null
            rvDefaultPaddingBottom = it.paddingBottom
        }
    }

    private fun observeLiveData() {
        viewModel.shopData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        isEnableDirectPurchase = getIsEnableDirectPurchase(it.data.shopDynamicTab)
                        initMiniCart()
                        onSuccessGetShopInfo(it.data.shopInfo)
                    }

                    is Fail -> {
                        onErrorGetShopInfo(it.throwable)
                        val throwable = it.throwable
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeLiveData.name,
                                liveDataName = ShopPageProductListResultViewModel::shopData.name,
                                userId = userId,
                                shopId = shopId.orEmpty(),
                                shopName = shopName.orEmpty(),
                                errorMessage = ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                            )
                        }
                    }
                }
            }
        )

        viewModel.shopSortFilterData.observe(
            viewLifecycleOwner,
            Observer {
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
                                errorMessage = ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                            )
                        }
                        showGetListError(it.throwable)
                    }
                }
            }
        )

        viewModel.productData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        val productList = it.data.listShopProductUiModel
                        val hasNextPage = it.data.hasNextPage
                        val totalProductData = it.data.totalProductData
                        val searchSuggestionData = it.data.shopProductSuggestion

                        if (!isShopPageProductSearchResultTrackerAlreadySent) {
                            sendShopPageSearchResultTracker(it.data)
                        }
                        renderProductList(
                            productList,
                            hasNextPage,
                            totalProductData,
                            searchSuggestionData
                        )
                        if (!isAlreadyCheckRestrictionInfo) {
                            loadShopRestrictionInfo()
                        }
                        isNeedToReloadData = false
                        productListName =
                            it.data.listShopProductUiModel.joinToString(",") { product -> product.name.orEmpty() }
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
                                errorMessage = ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_RESULT_BUYER_FLOW_TAG
                            )
                        }
                        showGetListError(it.throwable)
                    }
                }
            }
        )

        viewModel.productDataEmpty.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> renderProductListEmptyState(it.data)
                    is Fail -> showGetListError(it.throwable)
                }
            }
        )

        viewModel.bottomSheetFilterLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetBottomSheetFilterData(it.data)
                    }

                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel.shopProductFilterCountLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetShopProductFilterCount(count = it.data)
                    }

                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel.restrictionEngineData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        val shopRestrictionData = it.data
                        // handle view to follow shop for campaign type showcase
                        val isFollowShop = shopRestrictionData.status != PRODUCT_INELIGIBLE
                        if (isLogin && !isMyShop && !isFollowShop) {
                            val title = shopRestrictionData.actions[0].title
                            val description = shopRestrictionData.actions[0].description
                            val buttonLabel = shopRestrictionData.buttonLabel
                            val voucherIconUrl = shopRestrictionData.voucherIconUrl
                            showShopFollowersView(
                                title,
                                description,
                                isFollowShop,
                                buttonLabel,
                                voucherIconUrl
                            )
                        }
                    }

                    is Fail -> {}
                }
            }
        )


        viewModel.shopPageShopShareData.observe(
            viewLifecycleOwner
        ) { result ->
            if (result is Success) {
                shopSharingInShowCaseUiModel?.let {
                    it.shopSnippetUrl = result.data.shopSnippetUrl
                    it.shopCoreUrl = result.data.shopCore.url
                    it.location = result.data.location
                    it.tagline = result.data.shopCore.tagLine
                    it.shopStatus = result.data.statusInfo.shopStatus
                }
                showUniversalShareBottomSheet()
            }
        }

        viewModel.resultAffiliate.observe(viewLifecycleOwner) {
            if (it is Success) {
                isAffiliate = it.data.eligibleCommission?.isEligible.orFalse()
            }
        }
        observeMiniCartLiveData()
        observeAddCartLiveData()
        observeUpdateCartLiveData()
        observeDeleteCartLiveData()
        observeUpdatedShopProductListQuantityData()
        observeShopAtcTrackerLiveData()
        observeIsCreateAffiliateCookieAtcProduct()
        observeShopAffiliateChannel()
    }

    private fun observeShopAffiliateChannel() {
        viewModel.shopAffiliateChannel.observe(viewLifecycleOwner) {
            affiliateChannel = it
        }
    }

    private fun observeIsCreateAffiliateCookieAtcProduct() {
        viewModel.createAffiliateCookieAtcProduct.observe(viewLifecycleOwner) {
            it?.let {
                createAffiliateCookieAtcProduct(it)
            }
        }
    }

    private fun createAffiliateCookieAtcProduct(affiliateAtcProductModel: AffiliateAtcProductModel) {
        viewModel.createAffiliateCookieShopAtcProduct(
            affiliateCookieHelper,
            affiliateChannel,
            affiliateAtcProductModel.productId,
            affiliateAtcProductModel.isVariant,
            affiliateAtcProductModel.stockQty,
            shopId.orEmpty()
        )
    }

    private fun observeShopAtcTrackerLiveData() {
        viewModel.shopPageAtcTracker.observe(viewLifecycleOwner, {
            when (it.atcType) {
                ShopPageAtcTracker.AtcType.ADD -> {
                    sendClickAddToCartTracker(it)
                }

                ShopPageAtcTracker.AtcType.UPDATE_ADD, ShopPageAtcTracker.AtcType.UPDATE_REMOVE -> {
                    sendUpdateCartProductQuantityTracker(it)
                }

                else -> {
                    sendRemoveCartProductTracker(it)
                }
            }
        })
    }

    private fun sendClickAddToCartTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageTracking?.onClickProductAtcDirectPurchaseButton(
            atcTrackerModel,
            shopId.orEmpty(),
            customDimensionShopPage.shopType.orEmpty(),
            shopName.orEmpty(),
            userId
        )
    }

    private fun sendUpdateCartProductQuantityTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageTracking?.onClickProductAtcQuantityButton(
            atcTrackerModel,
            shopId.orEmpty(),
            userId
        )
    }

    private fun sendRemoveCartProductTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageTracking?.onClickProductAtcTrashButton(
            atcTrackerModel,
            shopId.orEmpty(),
            userId
        )
    }

    private fun observeMiniCartLiveData() {
        viewModel.miniCartData.observe(viewLifecycleOwner, {
            if (it.isShowMiniCartWidget) {
                showMiniCartWidget()
            } else {
                hideMiniCartWidget()
            }
            val listProductTabWidget = shopProductAdapter.data
            if (listProductTabWidget.isNotEmpty())
                viewModel.getShopProductDataWithUpdatedQuantity(listProductTabWidget.toMutableList())
        })
    }

    private fun hideMiniCartWidget() {
        recyclerView?.apply {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                0
            )
        }
        miniCart?.hide()
    }

    private fun showMiniCartWidget() {
        recyclerView?.apply {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                context?.resources?.getDimension(R.dimen.dp_82)?.toInt().orZero()
            )
        }
        miniCart?.show()
    }

    private fun getIsEnableDirectPurchase(
        shopDynamicTabData: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab
    ): Boolean {
        return shopDynamicTabData.tabData.firstOrNull {
            it.name == ShopPageHeaderTabName.HOME
        }?.shopLayoutFeature?.firstOrNull {
            it.name == ShopPageConstant.ShopLayoutFeatures.DIRECT_PURCHASE && it.isActive
        } != null
    }

    private fun sendShopPageSearchResultTracker(productResponseUiModel: GetShopProductUiModel) {
        when (sourceRedirection) {
            ShopSearchProductFragment.SEARCH_SUBMIT_RESULT_REDIRECTION -> sendShopPageProductSubmitSearchResultTracker(
                productResponseUiModel.listShopProductUiModel.isEmpty()
            )

            ShopSearchProductFragment.ETALASE_CLICK_RESULT_REDIRECTION -> sendShopPageProductSearchClickEtalaseProductResultTracker(
                productResponseUiModel.listShopProductUiModel.isEmpty()
            )

            SEARCH_AUTOCOMPLETE_PAGE_SOURCE -> sendShopPageProductAutoCompleteSearchResultTracker(
                productResponseUiModel
            )
        }
    }

    private fun sendShopPageProductAutoCompleteSearchResultTracker(productResponseUiModel: GetShopProductUiModel) {
        shopPageTracking?.sendShopPageAutoCompleteSearchResultTracker(
            keyword,
            productResponseUiModel.shopProductSuggestion.keyword_process,
            productResponseUiModel.shopProductSuggestion.response_code.toString(),
            navSource,
            shopId.orEmpty(),
            productResponseUiModel.totalProductData,
            shopName.orEmpty()
        )
    }

    private fun sendShopPageProductSubmitSearchResultTracker(isProductResultListEmpty: Boolean) {
        isShopPageProductSearchResultTrackerAlreadySent = true
        shopPageTracking?.sendShopPageProductSearchResultTracker(
            isMyShop,
            keyword,
            isProductResultListEmpty,
            customDimensionShopPage
        )
    }

    private fun sendShopPageProductSearchClickEtalaseProductResultTracker(isProductResultListEmpty: Boolean) {
        isShopPageProductSearchResultTrackerAlreadySent = true
        shopPageTracking?.sendShopPageProductSearchClickEtalaseProductResultTracker(
            isMyShop,
            keyword,
            isProductResultListEmpty,
            customDimensionShopPage
        )
    }

    private fun onSuccessGetShopProductFilterCount(
        count: Int = Int.ZERO,
        isFulfillmentFilterActive: Boolean = false
    ) {
        val countText = if (isFulfillmentFilterActive) {
            getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_no_count)
        } else {
            String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                count.thousandFormatted()
            )
        }
        sortFilterBottomSheet?.setResultCountText(countText)
    }

    private fun onSuccessGetBottomSheetFilterData(model: DynamicFilterModel) {
        model.defaultSortValue = DEFAULT_SORT_ID
        sortFilterBottomSheet?.setDynamicFilterModel(model)
    }

    private fun renderProductList(
        productList: List<ShopProductUiModel>,
        hasNextPage: Boolean,
        totalProductData: Int,
        searchSuggestionData: GetShopProductSuggestionUiModel
    ) {
        hideLoading()
        shopProductAdapter.clearAllNonDataElement()
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList()
            endlessRecyclerViewScrollListener.resetState()

            if (productList.isNotEmpty()) {
                shopProductSortFilterUiModel?.let { shopProductAdapter.setSortFilterData(it) }
                changeProductListGridView(ShopProductViewGridType.SMALL_GRID)
                if (searchSuggestionData.text.isNotEmpty()) {
                    shopProductAdapter.addSuggestionSearchTextSection(
                        searchSuggestionData.text,
                        searchSuggestionData.query
                    )
                }
            } else {
                if (keyword.isEmpty()) {
                    shopProductSortFilterUiModel?.apply {
                        isShowSortFilter = false
                    }?.let { shopProductAdapter.setSortFilterData(it) }
                }
            }
        }

        if (productList.isEmpty() && isLoadingInitialData) {
            showLoading()
            shopInfo?.let { loadProductDataEmptyState(it, defaultInitialPage) }
            isEmptyState = true
            updateScrollListenerState(false)
        } else {
            shopProductAdapter.updateShopPageProductChangeGridSectionIcon(
                isProductListEmpty = false,
                totalProductData
            )
            shopProductAdapter.setProductListDataModel(productList)
            updateScrollListenerState(hasNextPage)
            isLoadingInitialData = false
        }
    }

    private fun prepareShopFollowersView() {
        //    we can't use viewbinding for the code below, since the layout from shop_common hasn't implement viewbinding
        partialShopNplFollowersViewLayout = view?.findViewById(R.id.npl_follow_view)
        partialShopNplFollowersViewLayout?.visible()
        view?.let {
            recyclerView?.apply {
                setPadding(
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    resources.getDimension(R.dimen.dp_82).toInt()
                )
            }
            partialShopNplFollowersViewLayout?.translationY = it.height.toFloat()
        }
    }

    private fun showShopFollowersView(
        title: String,
        desc: String,
        isFollowShop: Boolean,
        buttonLabel: String?,
        voucherIconUrl: String?
    ) {
        prepareShopFollowersView()
        partialShopNplFollowersViewLayout?.let {
            partialShopNplFollowersView = PartialButtonShopFollowersView.build(
                it,
                object : PartialButtonShopFollowersListener {
                    override fun onButtonFollowNplClick() {
                        shopPageTracking?.clickFollowShowcaseNplButton(
                            shopId,
                            userId,
                            customDimensionShopPage
                        )
                        shopId?.let { shopId -> toggleFavoriteShop(shopId) }
                    }
                }
            )
            partialShopNplFollowersView?.renderView(
                title,
                desc,
                isFollowShop,
                buttonLabel,
                voucherIconUrl
            )
        }
    }

    private fun hideShopFollowersView() {
        recyclerView?.apply {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                rvDefaultPaddingBottom
            )
        }
        partialShopNplFollowersView?.setupVisibility = false
        partialShopNplFollowersViewLayout?.invisible()
    }

    private fun renderProductListEmptyState(productList: List<ShopProductUiModel>) {
        hideLoading()
        if (keyword.isEmpty()) {
            shopProductAdapter.addEmptyShowcaseResultState()
        } else {
            shopProductAdapter.addEmptySearchResultState()
        }
        if (productList.isNotEmpty()) {
            shopProductAdapter.addProductSuggestion(productList)
        }
        shopProductAdapter.notifyDataSetChanged()
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

    override fun onProductClicked(
        shopProductUiModel: ShopProductUiModel,
        @ShopTrackProductTypeDef shopTrackType: Int,
        productPosition: Int
    ) {
        if (!isEmptyState) {
            val isEtalaseCampaign =
                shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN
            shopPageTracking?.clickProductSearchResult(
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
                ShopUtil.getActualPositionFromIndex(productPosition),
                shopId.orEmpty(),
                isEtalaseCampaign,
                shopProductUiModel.isUpcoming,
                shopProductUiModel.etalaseType ?: DEFAULT_VALUE_ETALASE_TYPE,
                shopName.orEmpty(),
                navSource,
                shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                userId,
                ""
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
                ShopUtil.getActualPositionFromIndex(productPosition),
                shopId.orEmpty()
            )
        }
        startActivity(
            getProductIntent(
                shopProductUiModel.productUrl,
                attribution,
                shopPageTracking?.getListNameOfProduct(SEARCH, getSelectedEtalaseChip())
                    ?: ""
            )
        )
    }

    override fun onProductImpression(
        shopProductUiModel: ShopProductUiModel,
        shopTrackType: Int,
        productPosition: Int
    ) {
        if (!isEmptyState) {
            val isEtalaseCampaign =
                shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN
            shopPageTracking?.impressionProductListSearchResult(
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
                ShopUtil.getActualPositionFromIndex(productPosition),
                shopId.orEmpty(),
                isEtalaseCampaign,
                shopProductUiModel.isUpcoming,
                shopProductUiModel.etalaseType ?: DEFAULT_VALUE_ETALASE_TYPE,
                shopName.orEmpty(),
                navSource,
                shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                userId,
                ""
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
                ShopUtil.getActualPositionFromIndex(productPosition),
                shopId.orEmpty()
            )
        }
    }

    override fun onSearchProductsBySuggestedKeyword(suggestedKeyword: String) {
        keyword = suggestedKeyword
        activity?.run {
            startActivity(
                ShopProductListResultActivity.createIntent(
                    context = this,
                    shopId = shopId.orEmpty(),
                    keyword = keyword,
                    etalaseId = "",
                    attribution = attribution,
                    shopRef = shopRef
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            finish()
        }
    }

    override fun getSelectedEtalaseName(): String {
        return getSelectedEtalaseChip()
    }

    override fun onProductAtcNonVariantQuantityEditorChanged(
        shopProductUiModel: ShopProductUiModel,
        quantity: Int
    ) {
        if (isLogin) {
            handleAtcFlow(
                quantity,
                shopId.orEmpty(),
                shopProductUiModel
            )
        } else {
            redirectToLoginPage()
        }
    }

    override fun onProductAtcVariantClick(shopProductUiModel: ShopProductUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = shopProductUiModel.id,
            pageSource = VariantPageSource.SHOP_PAGE_PAGESOURCE,
            shopId = shopId.orEmpty(),
            startActivitResult = this::startActivityForResult,
            showQuantityEditor = true
        )
    }

    override fun onProductAtcDefaultClick(shopProductUiModel: ShopProductUiModel, quantity: Int) {
        if (isLogin) {
            if (isMyShop) {
                val sellerViewAtcErrorMessage =
                    getString(R.string.shop_page_seller_atc_error_message)
                showToasterError(sellerViewAtcErrorMessage)
            } else {
                handleAtcFlow(
                    quantity,
                    shopId.orEmpty(),
                    shopProductUiModel
                )
            }
        } else {
            redirectToLoginPage()
        }
    }

    override fun onImpressionProductAtc(
        shopProductUiModel: ShopProductUiModel,
        position: Int
    ) {
        trackImpressionProductAtc(
            shopProductUiModel,
            ShopUtil.getActualPositionFromIndex(position)
        )
    }

    private fun trackImpressionProductAtc(
        shopProductUiModel: ShopProductUiModel,
        position: Int
    ) {
        if (isEnableDirectPurchase) {
            shopPageTracking?.onImpressionProductAtcDirectPurchaseButton(
                shopProductUiModel,
                ShopPageConstant.ShopProductCardAtc.CARD_ETALASE,
                position,
                shopId.orEmpty(),
                userId
            )
        }
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        viewModel.handleAtcFlow(
            quantity,
            shopId,
            ShopPageConstant.ShopProductCardAtc.CARD_ETALASE,
            shopProductUiModel
        )
    }

    private fun getProductIntent(
        pdpAppLink: String,
        attribution: String?,
        listNameOfProduct: String
    ): Intent? {
        return if (context != null) {
            val updatedPdpAppLink = createPdpAffiliateLink(pdpAppLink)
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            RouteManager.getIntent(context, updatedPdpAppLink)
        } else {
            null
        }
    }

    private fun createPdpAffiliateLink(basePdpAppLink: String): String {
        return affiliateCookieHelper.createAffiliateLink(basePdpAppLink)
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

    private fun showToastSuccess(
        message: String,
        ctaText: String = "",
        ctaAction: View.OnClickListener? = null
    ) {
        activity?.run {
            view?.let {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                ctaAction?.let { ctaClickListener ->
                    Toaster.build(
                        it,
                        message,
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        ctaText,
                        ctaClickListener
                    ).show()
                } ?: Toaster.build(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText
                ).show()
            }
        }
    }

    private fun getMiniCartHeight(): Int {
        return miniCart?.height.orZero() - context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            ?.toInt().orZero()
    }

    private fun showToasterError(message: String) {
        activity?.let {
            Toaster.build(requireView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.shopId = shopInfo.shopCore.shopID
        this.shopDomain = shopInfo.shopCore.domain
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

    override fun onThreeDotsClicked(
        shopProductUiModel: ShopProductUiModel,
        @ShopTrackProductTypeDef shopTrackType: Int
    ) {
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

    private fun onSuccessGetSortFilterData(shopStickySortFilter: ShopStickySortFilter) {
        val etalaseList = shopStickySortFilter.etalaseList
        defaultEtalaseName = etalaseList.firstOrNull()?.etalaseName.orEmpty()
        selectedEtalaseId =
            shopStickySortFilter.etalaseList.firstOrNull { isEtalaseMatch(it) }?.etalaseId
                ?: ""
        selectedEtalaseAlias =
            shopStickySortFilter.etalaseList.firstOrNull { isEtalaseMatch(it) }?.alias
                ?: ""
        sortId = shopStickySortFilter.sortList.firstOrNull { it.value == sortId }?.value ?: ""
        selectedEtalaseName =
            etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.etalaseName
                ?: ""
        selectedEtalaseType = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.type
            ?: -1
        selectedEtalaseRules =
            etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.etalaseRules
        val selectedSortName =
            shopStickySortFilter.sortList.firstOrNull { it.value == sortId }?.name
                ?: ""

        if (selectedEtalaseId.isEmpty()){
            selectedEtalaseId = SEMUA_PRODUCT_ETALASE_ALIAS
            selectedEtalaseName = SEMUA_PRODUCT_ETALASE_NAME
            val shopEtalaseNotFound =  ShopEtalaseNotFoundBottomSheet.createInstance {
                context?.let {
                    RouteManager.route(it,UriUtil.buildUri(ApplinkConst.SHOP, shopId))
                }
            }
            shopEtalaseNotFound.show(childFragmentManager)
        }

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

            context?.let {
                viewModel.getShopProduct(
                    shopId ?: "",
                    defaultInitialPage,
                    ShopUtil.getProductPerPage(context),
                    selectedEtalaseId,
                    keyword,
                    selectedEtalaseType,
                    shopProductFilterParameter ?: ShopProductFilterParameter(),
                    localCacheModel ?: LocalCacheModel(),
                    isEnableDirectPurchase
                )
            }
        } else {
            hideLoading()
            endlessRecyclerViewScrollListener.resetState()
        }
    }

    private fun isNeededToShowSortFilter(): Boolean {
        return selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_CAMPAIGN &&
            selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN &&
            selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_FLASH_SALE
    }

    private fun isEtalaseMatch(model: ShopEtalaseItemDataModel): Boolean {
        return (
            model.etalaseId.lowercase() == selectedEtalaseId.lowercase() ||
                model.etalaseName.lowercase() == selectedEtalaseId.lowercase() ||
                model.alias.lowercase() == selectedEtalaseId.lowercase()
            ) && selectedEtalaseId.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                data?.let {
                    selectedEtalaseId =
                        data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID) ?: ""
                    selectedEtalaseName =
                        data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME) ?: ""
                    selectedEtalaseType = data.getIntExtra(
                        ShopShowcaseParamConstant.EXTRA_ETALASE_TYPE,
                        SELECTED_ETALASE_TYPE_DEFAULT_VALUE
                    )
                    needReloadData = data.getBooleanExtra(
                        ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA,
                        false
                    )

                    shopPageTracking?.clickMoreMenuChip(
                        isMyShop,
                        getSelectedEtalaseChip(),
                        customDimensionShopPage
                    )
                    shopInfo?.let {
                        shopPageTracking?.clickMenuFromMoreMenu(
                            viewModel.isMyShop(it.shopCore.shopID),
                            selectedEtalaseName,
                            CustomDimensionShopPage.create(
                                it.shopCore.shopID,
                                isOfficialStore,
                                isGoldMerchant
                            )
                        )
                    }
                    shopProductAdapter.changeSelectedEtalaseFilter(
                        selectedEtalaseId,
                        selectedEtalaseName
                    )
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
                    shopProductAdapter.changeSortFilterIndicatorCounter(
                        getIndicatorCount(
                            shopProductFilterParameter?.getMapData()
                        )
                    )
                    shopProductAdapter.refreshSticky()
                    loadInitialData()
                }
            }

            else -> {
            }
        }

        handleProductCardOptionsActivityResult(
            requestCode, resultCode, data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        } else {
            shopInfo?.let {
                val isEtalaseCampaign =
                    threeDotsClickShopProductUiModel?.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN ||
                        threeDotsClickShopProductUiModel?.etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN

                // shopTrackType is always from Product
                shopPageTracking?.clickWishlistProductResultPage(
                    !productCardOptionsModel.isWishlisted,
                    isLogin,
                    getSelectedEtalaseChip(),
                    CustomDimensionShopPageProduct.create(
                        it.shopCore.shopID,
                        it.goldOS.isOfficial == 1,
                        it.goldOS.isGold == 1,
                        productCardOptionsModel.productId,
                        shopRef
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
            handleWishlistActionAddToWishlistV2(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlistV2(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlistV2(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    productCardOptionsModel.wishlistResult,
                    context,
                    v
                )
            }
        }
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            shopProductAdapter.updateWishListStatus(productCardOptionsModel.productId, true)
        }
    }

    private fun handleWishlistActionRemoveFromWishlistV2(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    productCardOptionsModel.wishlistResult,
                    context,
                    v
                )
            }
        }

        if (productCardOptionsModel.wishlistResult.isSuccess) {
            shopProductAdapter.updateWishListStatus(
                productId = productCardOptionsModel.productId,
                wishList = false
            )
        }
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
        hideShopFollowersView()
    }

    override fun onResume() {
        super.onResume()
        if (selectedEtalaseRules != null) {
            // check RE for showcase type campaign eligibility
            isAlreadyCheckRestrictionInfo = true
            loadShopRestrictionInfo()
        }
        checkIfChooseAddressWidgetDataUpdated()
        updateMiniCartWidget()
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        context?.let { context ->
            localCacheModel?.let {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    context,
                    it
                )
                if (isUpdated)
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
        viewModel.shopData.removeObservers(this)
        viewModel.productData.removeObservers(this)
        viewModel.bottomSheetFilterLiveData.removeObservers(this)
        viewModel.shopProductFilterCountLiveData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopProductListFragmentListener = context as OnShopProductListFragmentListener
        shopPageProductListResultFragmentListener =
            context as ShopPageProductListResultFragmentListener
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
        outState.putString(SAVED_SHOP_DOMAIN, shopDomain)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
        outState.putParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER, shopProductFilterParameter)
        outState.putBoolean(
            SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT,
            isShopPageProductSearchResultTrackerAlreadySent
        )
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopInfo!!.shopCore.shopID)
            bundle.putString(
                ShopShowcaseParamConstant.EXTRA_SHOP_TYPE,
                customDimensionShopPage.shopType
            )

            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            )
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
        private const val PRODUCT_INELIGIBLE = "ineligible"

        val SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list"
        val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        val SAVED_SELECTED_ETALASE_TYPE = "saved_etalase_type"
        val SAVED_SHOP_ID = "saved_shop_id"
        val SAVED_SHOP_DOMAIN = "saved_shop_domain"
        val SAVED_SHOP_REF = "saved_shop_ref"
        val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        val SAVED_KEYWORD = "saved_keyword"
        val SAVED_SORT_VALUE = "saved_sort_name"
        val SAVED_SHOP_PRODUCT_FILTER_PARAMETER = "SAVED_SHOP_PRODUCT_FILTER_PARAMETER"
        val SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT =
            "SAVED_IS_SHOP_PRODUCT_SEARCH_RESULT_TRACKER_ALREADY_SENT"
        private const val SELECTED_ETALASE_TYPE_DEFAULT_VALUE = -10
        private const val SEARCH_AUTOCOMPLETE_PAGE_SOURCE = "SEARCH_AUTOCOMPLETE_PAGE_SOURCE"
        private const val DEFAULT_SHOWCASE_ID = "0"
        private const val SHOP_SEARCH_PAGE_NAV_SOURCE = "shop"
        private const val SEMUA_PRODUCT_ETALASE_NAME = "Semua Produk"
        private const val SEMUA_PRODUCT_ETALASE_ALIAS = "etalase"
        @JvmStatic
        fun createInstance(
            shopId: String,
            shopDomain: String,
            shopRef: String?,
            keyword: String?,
            etalaseId: String?,
            sort: String?,
            attribution: String?,
            isNeedToReloadData: Boolean? = false,
            sourceRedirection: String? = "",
            shopSharingInShowCaseUiModel: ShopSharingInShowCaseUiModel? = null
        ): ShopPageProductListResultFragment = ShopPageProductListResultFragment().also {
            it.arguments = Bundle().apply {
                putString(ShopParamConstant.EXTRA_SHOP_ID, shopId)
                putString(ShopParamConstant.EXTRA_SHOP_DOMAIN, shopDomain)
                putString(ShopParamConstant.EXTRA_SHOP_REF, shopRef.orEmpty())
                putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword ?: "")
                putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId ?: "")
                putString(ShopParamConstant.EXTRA_SORT_ID, sort ?: "")
                putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution ?: "")
                putBoolean(
                    ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA,
                    isNeedToReloadData
                        ?: false
                )
                putString(ShopParamConstant.EXTRA_SOURCE_REDIRECTION, sourceRedirection.orEmpty())
                putParcelable(EXTRA_FOR_SHOP_SHARING, shopSharingInShowCaseUiModel)
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
        shopProductAdapter.changeSortFilterIndicatorCounter(
            getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        selectedEtalaseId = ""
        selectedEtalaseName = ""
        selectedEtalaseType = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
        shopProductAdapter.changeSelectedEtalaseFilter(selectedEtalaseId, selectedEtalaseName)
        shopProductAdapter.refreshSticky()
        loadInitialData()
    }

    override fun setSortFilterMeasureHeight(measureHeight: Int) {}
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
        viewModel.getBottomSheetFilterData(shopId ?: "")
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
        shopProductAdapter.changeSortFilterIndicatorCounter(
            getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        shopProductAdapter.refreshSticky()
        loadInitialData()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        isFulfillmentFilterActive = mapParameter[IS_FULFILLMENT_KEY] == true.toString()
        if (isFulfillmentFilterActive) {
            // if fulfillment filter is active then avoid gql call to get total product
            onSuccessGetShopProductFilterCount(isFulfillmentFilterActive = isFulfillmentFilterActive)
        } else {
            viewModel.getFilterResultCount(
                shopId.orEmpty(),
                ShopUtil.getProductPerPage(context),
                keyword,
                selectedEtalaseId,
                tempShopProductFilterParameter,
                localCacheModel ?: LocalCacheModel()
            )
        }
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

    private fun changeProductListGridView(gridType: ShopProductViewGridType) {
        shopProductAdapter.updateShopPageProductChangeGridSectionIcon(gridType)
        shopProductAdapter.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(
        initialGridType: ShopProductViewGridType,
        finalGridType: ShopProductViewGridType
    ) {
        if (!isMyShop) {
            shopPageTracking?.clickProductListToggle(
                initialGridType,
                finalGridType,
                shopId.orEmpty(),
                userId
            )
        }
        changeProductListGridView(finalGridType)
    }

    private fun applySortFilterTracking(
        selectedSortName: String,
        selectedFilterMap: Map<String, String>
    ) {
        if (!isMyShop) {
            shopPageTracking?.clickApplyFilter(selectedSortName, selectedFilterMap, userId)
        }
    }

    fun onSearchBarClicked() {
        if (GlobalConfig.isSellerApp()) {
            redirectToShopSearchProductPage()
        } else {
            redirectToSearchAutoCompletePage()
        }
    }

    private fun redirectToShopSearchProductPage() {
        context?.let {
            startActivity(
                createIntent(
                    it,
                    shopId.orEmpty(),
                    shopInfo?.shopCore?.name.orEmpty(),
                    shopInfo?.goldOS?.isOfficial.orZero() == 1,
                    shopInfo?.goldOS?.isGold.orZero() == 1,
                    keyword,
                    attribution,
                    shopRef
                )
            )
        }
    }

    private fun redirectToSearchAutoCompletePage() {
        shopPageTracking?.clickSearch(isMyShop, customDimensionShopPage)
        val shopSrpAppLink = URLEncoder.encode(
            UriUtil.buildUri(
                ApplinkConst.SHOP_ETALASE,
                shopId,
                DEFAULT_SHOWCASE_ID
            ),
            "utf-8"
        )
        val searchPageUri = Uri.parse(ApplinkConstInternalDiscovery.AUTOCOMPLETE)
            .buildUpon()
            .appendQueryParameter(SearchApiConst.Q, keyword)
            .appendQueryParameter(SearchApiConst.SRP_PAGE_ID, shopId)
            .appendQueryParameter(SearchApiConst.SRP_PAGE_TITLE, shopName)
            .appendQueryParameter(SearchApiConst.NAVSOURCE, SHOP_SEARCH_PAGE_NAV_SOURCE)
            .appendQueryParameter(SearchApiConst.BASE_SRP_APPLINK, shopSrpAppLink)
            .build()
            .toString()
        RouteManager.route(context, searchPageUri)
    }

    override fun onShowcaseEmptyBackButtonClicked() {
        activity?.finish()
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    private fun initMiniCart() {
        if (isEnableDirectPurchase) {
            val shopIds = listOf(shopId.orEmpty())
            miniCart?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                isShopDirectPurchase = true,
                source = MiniCartSource.ShopPage,
                page = MiniCartAnalytics.Page.SHOP_PAGE
            )
        }
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.updateMiniCartData(miniCartSimplifiedData)
    }

    private fun observeAddCartLiveData() {
        viewModel.miniCartAdd.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                    showToastSuccess(
                        it.data.errorMessage.joinToString(separator = ", "),
                        getString(R.string.shop_page_atc_label_cta)
                    )
                }

                is Fail -> {
                    showToasterError(it.throwable.message.orEmpty())
                }
            }
        })
    }

    private fun observeUpdateCartLiveData() {
        viewModel.miniCartUpdate.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                }

                is Fail -> {
                    showToasterError(it.throwable.message.orEmpty())
                }
            }
        })
    }

    private fun observeDeleteCartLiveData() {
        viewModel.miniCartRemove.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                    showToastSuccess(
                        it.data.second,
                        getString(R.string.shop_page_atc_label_cta)
                    )
                }

                is Fail -> {
                    val message = it.throwable.message.orEmpty()
                    showToasterError(message)
                }
            }
        })
    }

    private fun observeUpdatedShopProductListQuantityData() {
        viewModel.updatedShopProductListQuantityData.observe(viewLifecycleOwner, {
            shopProductAdapter.updateProductTabWidget(it)
        })
    }

    private fun updateMiniCartWidget() {
        miniCart?.updateData()
    }

    private fun showUniversalShareBottomSheet() {
        shopPageEtalaseTracking?.clickShareEtalase(shopId.orEmpty(),
            selectedEtalaseId,isAffiliate,userId)

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance(view).apply {
            init(this@ShopPageProductListResultFragment)
            setImageOnlySharingOption(false)
            setMetaData(
                shopSharingInShowCaseUiModel?.shopName.orEmpty(),
                shopSharingInShowCaseUiModel?.avatar.orEmpty(),
                ""
            )
            enableDefaultShareIntent()
            setSubject(shopSharingInShowCaseUiModel?.shopName.toString())
            val shopEtalaseUrl = this@ShopPageProductListResultFragment.getString(
                R.string.shop_page_etalase_url,
                shopSharingInShowCaseUiModel?.shopCoreUrl,
                selectedEtalaseAlias
            )
            setLinkProperties(
                LinkProperties(
                    linkerType = LinkerData.SHOP_TYPE,
                    id = shopSharingInShowCaseUiModel?.shopId ?: "",
                    desktopUrl = shopEtalaseUrl,
                    deeplink = UriUtil.buildUri(
                        ApplinkConst.SHOP_ETALASE,
                        shopId,
                        selectedEtalaseAlias
                    )
                )
            )
            val shareString = this@ShopPageProductListResultFragment.getString(
                R.string.shop_etalase_page_share_text,
                selectedEtalaseName,
                shopSharingInShowCaseUiModel?.shopName
            )
            setShareText("$shareString%s")
        }
        //pageId = shopId-etalasename
        universalShareBottomSheet?.setUtmCampaignData(
            ShopPageTrackingConstant.SHOP_PAGE_SHARE_BOTTOM_SHEET_PAGE_NAME,
            userId.ifEmpty { "0" },
            shopId.orEmpty()+"-${selectedEtalaseName}",
            ShopPageTrackingConstant.SHOP_PAGE_SHARE_BOTTOM_SHEET_FEATURE_NAME

        )
        universalShareBottomSheet?.show(activity?.supportFragmentManager, this)
        shopPageEtalaseTracking?.impressionUniversalBottomSheetEtalase(shopId.orEmpty(),
            selectedEtalaseId,isAffiliate,userId)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        shopPageEtalaseTracking?.clickShareBottomSheetOption(
            shareModel.channel.orEmpty(),
            shopId.orEmpty(),
            userId,
            selectedEtalaseId,
            shareModel.campaign?.split("-")?.lastOrNull().orEmpty(),
            universalShareBottomSheet?.getUserType() ?: ""
        )
    }

    override fun onCloseOptionClicked() {
        shopPageEtalaseTracking?.clickCloseBottomSheetShareEtalase(shopId.orEmpty(),
            selectedEtalaseId,isAffiliate,userId)
    }

    fun clickShopShare() {
        val inputAffiliate = AffiliateInput().apply {
            pageDetail = PageDetail(
                pageId = shopId.orEmpty(),
                pageType = PageType.SHOP.value,
                siteId = ShopPageHeaderFragment.AFFILIATE_SITE_ID,
                verticalId = ShopPageHeaderFragment.AFFILIATE_VERTICAL_ID
            )
            pageType = PageType.SHOP.value
            product = Product()
            shop = Shop(
                shopID = shopId,
                shopStatus = shopSharingInShowCaseUiModel?.shopStatus,
                isOS = shopSharingInShowCaseUiModel?.isOfficial == true,
                isPM = shopSharingInShowCaseUiModel?.isGoldMerchant == true
            )
            affiliateLinkType = AffiliateLinkType.SHOP
        }

        viewModel.getShopShareData(shopId.orEmpty(), shopDomain, inputAffiliate)
    }
}
