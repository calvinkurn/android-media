package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FEATURED_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_GROUP_POSITION_FULFILLMENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.constant.ShopPageConstant.GO_TO_MEMBERSHIP_DETAIL
import com.tokopedia.shop.common.constant.ShopPageConstant.START_PAGE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_PRODUCT_TAB_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_MIDDLE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_RENDER
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.data.model.AffiliateAtcProductModel
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.view.interfaces.ShopPageSharedListener
import com.tokopedia.shop.common.view.listener.InterfaceShopPageClickScrollToTop
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.viewmodel.ShopChangeProductGridSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopPageMiniCartSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.common.widget.MembershipBottomSheetSuccess
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageHeaderActivity
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.util.StaggeredGridLayoutManagerWrapper
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.*
import com.tokopedia.shop.product.view.viewholder.ShopProductAddViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductsEmptyViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import javax.inject.Inject

class ShopPageProductListFragment :
    BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
    MembershipStampAdapter.MembershipStampAdapterListener,
    ShopProductClickedListener,
    ShopCarouselSeeAllClickedListener,
    BaseEmptyViewHolder.Callback,
    ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
    MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener,
    ShopProductAddViewHolder.ShopProductAddViewHolderListener,
    ShopProductsEmptyViewHolder.ShopProductsEmptyViewHolderListener,
    ShopProductImpressionListener,
    ShopProductChangeGridSectionListener,
    SortFilterBottomSheet.Callback,
    InterfaceShopPageClickScrollToTop {

    companion object {
        private const val REQUEST_CODE_USER_LOGIN = 100
        private const val REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101
        private const val REQUEST_CODE_ETALASE = 205
        private const val REQUEST_CODE_LOGIN_USE_VOUCHER = 206
        private const val REQUEST_CODE_MERCHANT_VOUCHER = 207
        private const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 208
        private const val REQUEST_CODE_MEMBERSHIP_STAMP = 2091
        private const val REQUEST_CODE_ADD_PRODUCT = 3697
        private const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        const val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        const val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        const val SAVED_SHOP_ID = "saved_shop_id"
        const val SAVED_SHOP_REF = "saved_shop_ref"
        const val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        const val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        const val SAVED_SHOP_SORT_ID = "saved_shop_sort_id"
        const val SAVED_SHOP_SORT_NAME = "saved_shop_sort_name"
        const val SAVED_SHOP_PRODUCT_FILTER_PARAMETER = "SAVED_SHOP_PRODUCT_FILTER_PARAMETER"
        const val ALL_ETALASE_ID = "etalase"
        const val SOLD_ETALASE_ID = "sold"
        private const val REQUEST_CODE_SORT = 300
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_IS_OFFICIAL = "IS_OFFICIAL"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        private const val KEY_ENABLE_SHOP_DIRECT_PURCHASE = "ENABLE_SHOP_DIRECT_PURCHASE"

        @JvmStatic
        fun createInstance(
            shopId: String,
            shopName: String,
            isOfficial: Boolean,
            isGoldMerchant: Boolean,
            shopAttribution: String?,
            shopRef: String,
            isEnableDirectPurchase: Boolean
        ): ShopPageProductListFragment {
            val fragment = ShopPageProductListFragment()
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putBoolean(KEY_IS_OFFICIAL, isOfficial)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(SHOP_ATTRIBUTION, shopAttribution)
            bundle.putBoolean(KEY_ENABLE_SHOP_DIRECT_PURCHASE, isEnableDirectPurchase)
            fragment.arguments = bundle
            fragment.shopRef = shopRef
            return fragment
        }
    }

    override val isOwner: Boolean
        get() = viewModel?.isMyShop(shopId).orFalse()

    private val isLogin: Boolean
        get() = viewModel?.isLogin.orFalse()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ShopPageProductListViewModel? = null

    private var shopPageTracking: ShopPageTrackingBuyer? = null
    private var lastQuestId: Int = 0
    private var attribution: String = ""
    private var isLoadingNewProductData = false
    private var sortId
        get() = shopProductFilterParameter?.getSortId().orEmpty()
        set(value) {
            shopProductFilterParameter?.setSortId(value)
        }
    private val sortName
        get() = viewModel?.getSortNameById(sortId).orEmpty()

    private val userId: String
        get() = viewModel?.userId.orEmpty()

    private var urlNeedTobBeProceed: String? = null
    private var shopId: String = ""
    private var shopName: String = ""
    private var shopRef: String = ""
    private var isEnableDirectPurchase: Boolean = false
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var selectedEtalaseId = ""
    private var selectedEtalaseName = ""
    private var defaultEtalaseName = ""
    private var productListName: String = ""
    private var recyclerViewTopPadding = 0
    private var shopSortFilterHeight = 0
    private var isOnViewCreated = false
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var threeDotsClickShopProductUiModel: ShopProductUiModel? = null
    private var shopProductFilterParameterSharedViewModel: ShopProductFilterParameterSharedViewModel? = null
    private var shopChangeProductGridSharedViewModel: ShopChangeProductGridSharedViewModel? = null
    private var shopPageMiniCartSharedViewModel: ShopPageMiniCartSharedViewModel? = null
    private var threeDotsClickShopTrackingType = -1
    private var initialProductListData: ShopProduct.GetShopProduct? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var remoteConfig: RemoteConfig? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private val shopProductAdapter: ShopProductAdapter
        get() = adapter as ShopProductAdapter
    private val customDimensionShopPage: CustomDimensionShopPage
        get() {
            return CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
        }
    private var shopProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()
    private var gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    override fun chooseProductClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConst.PRODUCT_ADD)
        }
    }

    private fun showToastSuccess(message: String, ctaText: String = "", ctaAction: View.OnClickListener? = null) {
        activity?.run {
            view?.let {
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

    private fun loadNewProductData() {
        isLoadingNewProductData = true
        shopProductAdapter.clearAllNonDataElement()
        shopProductAdapter.clearProductList()
        showLoading()
        viewModel?.getProductListData(
            shopId,
            START_PAGE,
            ShopUtil.getProductPerPage(context),
            selectedEtalaseId,
            shopProductFilterParameter ?: ShopProductFilterParameter(),
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            isEnableDirectPurchase
        )
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view)?.let {
            it.clearOnScrollListeners()
            it.layoutManager = staggeredGridLayoutManager
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
            it.itemAnimator = null
            recyclerViewTopPadding = it.paddingTop
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopProductAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val firstCompletelyVisibleItemPosition = (layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0).orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    (parentFragment as? ShopPageHeaderFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex) {
                    hideScrollToTopButton()
                }
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                super.onScrollStateChanged(recyclerView, state)
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstCompletelyVisibleItemPosition = (layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0).orZero()
                    if (firstCompletelyVisibleItemPosition > 0) {
                        showScrollToTopButton()
                    }
                }
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopProductAdapter.showLoading()
                loadData(page)
            }
        }
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        if (context == null) {
            return
        }
        shopPageTracking!!.clickUseMerchantVoucher(isOwner, merchantVoucherViewModel, shopId, position)
        showSnackBarClose(getString(com.tokopedia.merchantvoucher.R.string.title_voucher_code_copied))
    }

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        shopPageTracking?.clickDetailMerchantVoucher(isOwner, merchantVoucherViewModel.voucherId.toString())
        context?.let {
            val intent = MerchantVoucherDetailActivity.createIntent(
                it,
                merchantVoucherViewModel.voucherId,
                merchantVoucherViewModel,
                shopId
            )
            startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
        }
    }

    override fun onButtonClaimClicked(questId: Int) {
        sendEventMembership()
        lastQuestId = questId
        viewModel?.claimMembershipBenefit(questId)
    }

    private fun sendEventMembership() {
        if (!isOwner) {
            shopPageTracking?.sendEventMembership(shopId, userId)
        }
    }

    override fun goToVoucherOrRegister(url: String?, clickOrigin: String?) {
        val intent: Intent = if (url == null) {
            sendEventMembership()
            RouteManager.getIntent(context, ApplinkConst.COUPON_LISTING)
        } else {
            if (clickOrigin == GO_TO_MEMBERSHIP_DETAIL) {
                sendEventMembership()
            } else {
                sendEventMembership()
            }
            RouteManager.getIntent(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        }
        startActivityForResult(intent, REQUEST_CODE_MEMBERSHIP_STAMP)
    }

    override fun onThreeDotsClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int) {
        threeDotsClickShopProductUiModel = shopProductUiModel
        threeDotsClickShopTrackingType = shopTrackType

        showProductCardOptions(
            this,
            ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = shopProductUiModel.isWishList,
                productId = shopProductUiModel.id ?: ""
            )
        )
    }

    override fun onProductClicked(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int) {
        if (defaultEtalaseName.isNotEmpty()) {
            if (!isOwner) {
                when (shopTrackType) {
                    ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.clickProduct(
                        isLogin,
                        getSelectedEtalaseChip(),
                        FEATURED_PRODUCT,
                        CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductUiModel.id,
                            attribution,
                            shopRef,
                            shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                            shopProductUiModel.isShowFreeOngkir
                        ),
                        shopProductUiModel,
                        ShopUtil.getActualPositionFromIndex(productPosition),
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        false,
                        shopProductUiModel.isUpcoming
                    )
                    ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickProduct(
                        isLogin,
                        getSelectedEtalaseChip(),
                        getSelectedEtalaseChip(),
                        CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductUiModel.id,
                            attribution,
                            shopRef,
                            shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                            shopProductUiModel.isShowFreeOngkir
                        ),
                        shopProductUiModel,
                        ShopUtil.getActualPositionFromIndex(productPosition) - shopProductAdapter.shopProductFirstViewModelPosition,
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.isUpcoming,
                        shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                        userId,
                        getSelectedTabName()
                    )
                    ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.clickProduct(
                        isLogin,
                        getSelectedEtalaseChip(),
                        shopProductAdapter.getEtalaseNameHighLight(shopProductUiModel),
                        CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductUiModel.id,
                            attribution,
                            shopRef,
                            shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                            shopProductUiModel.isShowFreeOngkir
                        ),
                        shopProductUiModel,
                        ShopUtil.getActualPositionFromIndex(productPosition),
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductAdapter.getEtalaseNameHighLightType(shopProductUiModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.isUpcoming
                    )
                }
            }
        }
        goToPDP(
            shopProductUiModel.productUrl,
            attribution,
            shopPageTracking?.getListNameOfProduct(PRODUCT, getSelectedEtalaseChip())
                ?: ""
        )
    }

    override fun onProductImpression(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int) {
        if (!isOwner) {
            when (shopTrackType) {
                ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.impressionProductList(
                    isLogin,
                    getSelectedEtalaseChip(),
                    FEATURED_PRODUCT,
                    CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopProductUiModel.id,
                        attribution,
                        shopRef,
                        shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                        shopProductUiModel.isShowFreeOngkir
                    ),
                    shopProductUiModel,
                    ShopUtil.getActualPositionFromIndex(productPosition),
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    false,
                    shopProductUiModel.isUpcoming
                )
                ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.impressionProductList(
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else getSelectedEtalaseChip(),
                    CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopProductUiModel.id,
                        attribution,
                        shopRef,
                        shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                        shopProductUiModel.isShowFreeOngkir
                    ),
                    shopProductUiModel,
                    ShopUtil.getActualPositionFromIndex(productPosition) - shopProductAdapter.shopProductFirstViewModelPosition,
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.isUpcoming,
                    shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                    userId,
                    getSelectedTabName()
                )
                ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.impressionProductList(
                    isLogin,
                    getSelectedEtalaseChip(),
                    shopProductAdapter.getEtalaseNameHighLight(shopProductUiModel),
                    CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopProductUiModel.id,
                        attribution,
                        shopRef,
                        shopProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                        shopProductUiModel.isShowFreeOngkir
                    ),
                    shopProductUiModel,
                    ShopUtil.getActualPositionFromIndex(productPosition),
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductAdapter.getEtalaseNameHighLightType(shopProductUiModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.isUpcoming
                )
            }
        }
    }

    override fun onProductAtcNonVariantQuantityEditorChanged(
        shopProductUiModel: ShopProductUiModel,
        quantity: Int
    ) {
        if (isLogin) {
            handleAtcFlow(quantity, shopId, shopProductUiModel)
        } else {
            redirectToLoginPage()
        }
    }

    override fun onProductAtcVariantClick(shopProductUiModel: ShopProductUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = shopProductUiModel.id.orEmpty(),
            pageSource = VariantPageSource.SHOP_PAGE_PAGESOURCE,
            shopId = shopId,
            startActivitResult = this::startActivityForResult,
            showQuantityEditor = true
        )
    }

    override fun onProductAtcDefaultClick(shopProductUiModel: ShopProductUiModel, quantity: Int) {
        if (isLogin) {
            if (isOwner) {
                val sellerViewAtcErrorMessage = getString(R.string.shop_page_seller_atc_error_message)
                showToasterError(sellerViewAtcErrorMessage)
            } else {
                handleAtcFlow(quantity, shopId, shopProductUiModel)
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
            ShopUtil.getActualPositionFromIndex(position) - shopProductAdapter.shopProductFirstViewModelPosition
        )
    }

    private fun trackImpressionProductAtc(
        shopProductUiModel: ShopProductUiModel,
        position: Int
    ) {
        if (isEnableDirectPurchase) {
            shopPageTracking?.onImpressionProductAtcDirectPurchaseButton(
                shopProductUiModel,
                ShopPageConstant.ShopProductCardAtc.CARD_PRODUCT,
                position,
                shopId,
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

    private fun getSelectedTabName(): String {
        return (parentFragment as? ShopPageHeaderFragment)?.getSelectedTabName().orEmpty()
    }

    override fun getSelectedEtalaseName(): String {
        return getSelectedEtalaseChip()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (shopProductAdapter.isLoading) {
                        return
                    }

                    val etalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val etalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME)
                    val isNeedToReloadData = data.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                    shopPageTracking?.clickMoreMenuChip(
                        isOwner,
                        etalaseName,
                        customDimensionShopPage
                    )
                    if (shopPageTracking != null) {
                        shopPageTracking!!.clickMenuFromMoreMenu(
                            viewModel?.isMyShop(shopId) ?: return,
                            etalaseName,
                            CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
                        )
                    }
                    val intent = ShopProductListResultActivity.createIntent(
                        activity,
                        shopId,
                        "",
                        etalaseId,
                        attribution,
                        "",
                        shopRef
                    )
                    intent.putExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
                    startActivity(intent)
                }
            }
            REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW -> if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                promoClicked(urlNeedTobBeProceed)
            }
            REQUEST_CODE_LOGIN_USE_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    loadMerchantVoucher()
                }
            }
            REQUEST_CODE_MEMBERSHIP_STAMP -> {
                loadMembership()
            }
            REQUEST_CODE_USER_LOGIN -> {
                (parentFragment as? InterfaceShopPageHeader)?.refreshData()
            }
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (shopProductAdapter.isLoading) {
                        return
                    }
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    shopPageTracking?.sortProduct(sortName, isOwner, customDimensionShopPage)
                    changeShopProductFilterParameterSharedData()
                    changeSortData(sortId)
                    scrollToChangeProductGridSegment()
                }
            }
            REQUEST_CODE_ADD_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val shopType = when {
                        isOfficialStore -> TrackShopTypeDef.OFFICIAL_STORE
                        isGoldMerchant -> TrackShopTypeDef.GOLD_MERCHANT
                        else -> TrackShopTypeDef.REGULAR_MERCHANT
                    }
                    shopPageTracking?.sendOpenScreenAddProduct(shopId, shopType)
                }
            }
            else -> {
            }
        }
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun scrollToChangeProductGridSegment() {
        // multiply with 2 to make first dy value on onScroll function greater than rv top padding
        getRecyclerView(view)?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
            shopProductAdapter.shopChangeProductGridSegment,
            shopSortFilterHeight + recyclerViewTopPadding
        )
    }

    private fun loadMembership() {
        viewModel?.getNewMembershipData(shopId)
    }

    private fun loadMerchantVoucher() {
        viewModel?.getNewMerchantVoucher(shopId, context)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        } else {
            threeDotsClickShopProductUiModel?.let { shopProductViewModel ->
                when (threeDotsClickShopTrackingType) {
                    ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.clickWishlist(
                        !shopProductViewModel.isWishList,
                        isLogin,
                        getSelectedEtalaseChip(),
                        FEATURED_PRODUCT,
                        CustomDimensionShopPageProduct.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            shopRef
                        ),
                        shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        false,
                        shopProductViewModel.isUpcoming
                    )
                    ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickWishlist(
                        !shopProductViewModel.isWishList,
                        isLogin,
                        getSelectedEtalaseChip(),
                        getSelectedEtalaseChip(),
                        CustomDimensionShopPageProduct.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            shopRef
                        ),
                        shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductViewModel.isUpcoming
                    )
                    else -> // highlight
                        shopPageTracking?.clickWishlist(
                            !shopProductViewModel.isWishList,
                            isLogin,
                            getSelectedEtalaseChip(),
                            shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                            CustomDimensionShopPageProduct.create(
                                shopId,
                                isOfficialStore,
                                isGoldMerchant,
                                shopProductViewModel.id,
                                shopRef
                            ),
                            shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                            shopProductAdapter.getEtalaseNameHighLightType(shopProductViewModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                            shopProductViewModel.isUpcoming
                        )
                }
            }
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }

        threeDotsClickShopProductUiModel = null
        threeDotsClickShopTrackingType = -1
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel?.clearGetShopProductUseCase()

        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlistV2(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlistV2(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlistV2(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(productCardOptionsModel.wishlistResult, context, v)
            }
        }
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            shopProductAdapter.updateWishListStatus(productCardOptionsModel.productId, true)
        }
    }

    private fun handleWishlistActionRemoveFromWishlistV2(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(productCardOptionsModel.wishlistResult, context, v)
            }
        }
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            shopProductAdapter.updateWishListStatus(
                productId = productCardOptionsModel.productId,
                wishList = false
            )
        }
    }

    override fun onSeeAllClicked(shopProductEtalaseChipItemViewModel: ShopEtalaseItemDataModel) {
        shopPageTracking?.clickHighLightSeeAll(customDimensionShopPage)
        val intent = ShopProductListResultActivity.createIntent(
            activity,
            shopId,
            "",
            shopProductEtalaseChipItemViewModel.etalaseId,
            attribution,
            sortId,
            shopRef
        )
        startActivity(intent)
    }

    override fun onSeeAllClicked() {
        shopPageTracking?.clickSeeAllMerchantVoucher(isOwner)

        context?.let {
            val intent = MerchantVoucherListActivity.createIntent(
                it,
                shopId,
                shopName
            )
            startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER)
        }
    }

    override fun onVoucherItemImpressed(merchantVoucherViewModel: MerchantVoucherViewModel, voucherPosition: Int) {}

    override fun onEmptyContentItemTextClicked() {
    }

    override fun onEmptyButtonClicked() {
    }

    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        val displaymetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels
        val userSession = UserSession(context)
        val _shopId = arguments?.getString(KEY_SHOP_ID, "") ?: ""
        val _isMyShop = ShopUtil.isMyShop(shopId = _shopId, userSessionShopId = userSession.shopId.orEmpty())
        return ShopProductAdapterTypeFactory(
            membershipStampAdapterListener = this,
            shopProductClickedListener = this,
            shopProductImpressionListener = this,
            shopCarouselSeeAllClickedListener = this,
            emptyProductOnClickListener = this,
            shopProductEtalaseListViewHolderListener = this,
            shopProductAddViewHolderListener = this,
            shopProductsEmptyViewHolderListener = this,
            shopProductEmptySearchListener = null,
            shopProductChangeGridSectionListener = this,
            shopShowcaseEmptySearchListener = null,
            shopProductSearchSuggestionListener = null,
            isGridSquareLayout = true,
            deviceWidth = deviceWidth,
            shopTrackType = ShopTrackProductTypeDef.PRODUCT,
            isShowTripleDot = !_isMyShop
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {
        return ShopProductAdapter(adapterTypeFactory)
    }

    override fun onItemClicked(t: BaseShopProductViewModel?) {}

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        activity?.run {
            DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopPageProductListFragment)
        }
    }

    override fun onGetListErrorWithExistingData(throwable: Throwable?) {
        clearCache()
        super.onGetListErrorWithExistingData(throwable)
    }

    override fun onRetryClicked() {
        clearCache()
        super.onRetryClicked()
    }

    override fun loadInitialData() {
        isLoadingNewProductData = true
        shopProductAdapter.clearAllElements()
        stopMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_PREPARE)
        startMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_MIDDLE)
        showLoading()
        initialProductListData?.let {
            viewModel?.setInitialProductList(shopId, ShopUtil.getProductPerPage(context), it, isEnableDirectPurchase)
        }
        viewModel?.getShopFilterData(shopId)
        isOnViewCreated = false
    }

    private fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(
                it,
                url.orEmpty(),
                shopId,
                viewModel?.isLogin.orFalse(),
                viewModel?.userDeviceId.orEmpty(),
                viewModel?.userId.orEmpty()
            )
            // Need to login
            if (!urlProceed) {
                urlNeedTobBeProceed = url
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW)
            }
        }
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, customDimensionShopPage.shopType)

            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(EXTRA_BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    private fun showSnackBarClose(stringToShow: String) {
        activity?.let {
            view?.let { view ->
                val snackbar = Snackbar.make(
                    view,
                    stringToShow,
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction(requireActivity().getString(R.string.label_close)) { snackbar.dismiss() }
                snackbar.setActionTextColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
                snackbar.show()
            }
        }
    }

    private fun onErrorAddToWishList(e: Throwable) {
        context?.let {
            if (!viewModel?.isLogin.orFalse() || e is UserNotLoginException) {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
                return
            }
        }
        activity?.let {
            NetworkErrorHelper.showCloseSnackbar(it, ErrorHandler.getErrorMessage(it, e))
        }
    }

    private fun goToPDP(pdpAppLink: String, attribution: String?, listNameOfProduct: String) {
        val updatedPdpAppLink = createAffiliateLink(pdpAppLink)
        context?.let {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            val intent = RouteManager.getIntent(context, updatedPdpAppLink)
            startActivity(intent)
        }
    }

    private fun createAffiliateLink(basePdpAppLink: String): String {
        return (activity as? ShopPageSharedListener)?.createPdpAffiliateLink(basePdpAppLink).orEmpty()
    }

    override fun loadData(page: Int) {
        viewModel?.getProductListData(
            shopId,
            page,
            ShopUtil.getProductPerPage(context),
            selectedEtalaseId,
            shopProductFilterParameter ?: ShopProductFilterParameter(),
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            isEnableDirectPurchase
        )
    }

    override fun onAddProductClicked() {
        context?.let {
            shopPageTracking?.clickAddProduct(customDimensionShopPage)
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_ADD)
            startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val title: String
        val description: String
        return if (isOwner) {
            if (selectedEtalaseId == ALL_ETALASE_ID || selectedEtalaseId.isEmpty()) {
                shopPageTracking?.impressionZeroProduct(
                    CustomDimensionShopPage.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant
                    )
                )
                ShopSellerEmptyProductAllEtalaseUiModel()
            } else if (isOwner && selectedEtalaseId == SOLD_ETALASE_ID) {
                title = getString(R.string.text_shop_no_product_seller)
                description = getString(R.string.text_shop_no_product_description_seller)
                ShopEmptyProductUiModel(isOwner, title, description)
            } else {
                title = getString(R.string.shop_product_limited_empty_products_title_owner)
                description = getString(R.string.shop_product_limited_empty_products_content_owner)
                ShopEmptyProductUiModel(isOwner, title, description)
            }
        } else {
            title = getString(R.string.text_shop_no_product)
            description = getString(R.string.text_shop_no_product_follow)
            ShopEmptyProductUiModel(isOwner, title, description)
        }
    }

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isShopProductTabSelected()) {
            startMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_PREPARE)
        }
        super.onCreate(savedInstanceState)
        context?.let {
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            staggeredGridLayoutManager = StaggeredGridLayoutManagerWrapper(it.resources.getInteger(R.integer.span_count_small_grid), StaggeredGridLayoutManager.VERTICAL)
        }
        remoteConfig = FirebaseRemoteConfigImpl(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopProductFilterParameterSharedViewModel::class.java)
        shopChangeProductGridSharedViewModel = ViewModelProvider(requireActivity()).get(ShopChangeProductGridSharedViewModel::class.java)
        shopPageMiniCartSharedViewModel = ViewModelProviders.of(requireActivity()).get(
            ShopPageMiniCartSharedViewModel::class.java
        )
        attribution = arguments?.getString(SHOP_ATTRIBUTION, "") ?: ""
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    private fun startMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startCustomMetric(it, tag)
            }
        }
    }

    private fun stopMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopCustomMetric(it, tag)
            }
        }
    }

    private fun invalidateMonitoringPlt() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.invalidateMonitoringPlt(it)
            }
        }
    }

    private fun stopMonitoringPerformance() {
        (activity as? ShopPageHeaderActivity)?.stopShopProductTabPerformanceMonitoring()
    }

    private fun isShopProductTabSelected(): Boolean {
        return (parentFragment as? InterfaceShopPageHeader)?.isTabSelected(this::class.java) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
        outState.putString(SAVED_SHOP_SORT_ID, sortId)
        outState.putString(SAVED_SHOP_SORT_NAME, sortName)
        outState.putParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER, shopProductFilterParameter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_shop_page_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getArgumentsData()
        savedInstanceState?.let {
            selectedEtalaseId = it.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = it.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            shopRef = it.getString(SAVED_SHOP_REF).orEmpty()
            isGoldMerchant = it.getBoolean(SAVED_SHOP_IS_GOLD_MERCHANT)
            isOfficialStore = it.getBoolean(SAVED_SHOP_IS_OFFICIAL)
            shopProductFilterParameter = it.getParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER)
        }
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        isOnViewCreated = true
        observeShopProductFilterParameterSharedViewModel()
        observeShopChangeProductGridSharedViewModel()
        observeViewModelLiveData()
        observeShopPageMiniCartSharedViewModel()
        observeAddCartLiveData()
        observeUpdateCartLiveData()
        observeDeleteCartLiveData()
        observeUpdatedShopProductListQuantityData()
        observeShopAtcTrackerLiveData()
        observeIsCreateAffiliateCookieAtcProduct()
    }

    private fun observeIsCreateAffiliateCookieAtcProduct() {
        viewModel?.createAffiliateCookieAtcProduct?.observe(viewLifecycleOwner) {
            it?.let {
                createAffiliateCookieAtcProduct(it)
            }
        }
    }

    private fun createAffiliateCookieAtcProduct(affiliateAtcProductModel: AffiliateAtcProductModel) {
        (activity as? ShopPageSharedListener)?.createAffiliateCookieAtcProduct(
            affiliateAtcProductModel.productId,
            affiliateAtcProductModel.isVariant,
            affiliateAtcProductModel.stockQty
        )
    }

    private fun observeShopAtcTrackerLiveData() {
        viewModel?.shopPageAtcTracker?.observe(viewLifecycleOwner, {
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
            shopId,
            customDimensionShopPage.shopType.orEmpty(),
            shopName,
            userId
        )
    }

    private fun sendUpdateCartProductQuantityTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageTracking?.onClickProductAtcQuantityButton(
            atcTrackerModel,
            shopId,
            userId
        )
    }

    private fun sendRemoveCartProductTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageTracking?.onClickProductAtcTrashButton(
            atcTrackerModel,
            shopId,
            userId
        )
    }

    private fun observeUpdatedShopProductListQuantityData() {
        viewModel?.updatedShopProductListQuantityData?.observe(viewLifecycleOwner, {
            shopProductAdapter.updateProductTabWidget(it)
        })
    }

    override fun onResume() {
        loadInitialDataAfterOnViewCreated()
        super.onResume()
        checkShowScrollToTopButton()
    }

    private fun checkShowScrollToTopButton() {
        if (isShowScrollToTopButton()) {
            showScrollToTopButton()
        } else {
            hideScrollToTopButton()
        }
    }

    private fun loadInitialDataAfterOnViewCreated() {
        if (isOnViewCreated) {
            loadInitialData()
            isOnViewCreated = false
        }
    }

    private fun getArgumentsData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
            shopName = it.getString(KEY_SHOP_NAME, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
            isEnableDirectPurchase = it.getBoolean(KEY_ENABLE_SHOP_DIRECT_PURCHASE, false)
        }
    }

    private fun observeShopChangeProductGridSharedViewModel() {
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.observe(
            viewLifecycleOwner,
            Observer {
                if (!shopProductAdapter.isLoading) {
                    gridType = it
                    changeProductListGridView(it)
                }
            }
        )
    }

    private fun observeShopPageMiniCartSharedViewModel() {
        shopPageMiniCartSharedViewModel?.miniCartSimplifiedData?.observe(viewLifecycleOwner, {
            viewModel?.setMiniCartData(it)
            val listProductTabWidget = shopProductAdapter.data
            if (listProductTabWidget.isNotEmpty()) {
                viewModel?.getShopProductDataWithUpdatedQuantity(listProductTabWidget.toMutableList())
            }
        })
    }

    private fun observeAddCartLiveData() {
        viewModel?.miniCartAdd?.observe(viewLifecycleOwner, {
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
        viewModel?.miniCartUpdate?.observe(viewLifecycleOwner, {
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
        viewModel?.miniCartRemove?.observe(viewLifecycleOwner, {
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

    private fun updateMiniCartWidget() {
        (parentFragment as? ShopPageHeaderFragment)?.updateMiniCartWidget()
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(
            viewLifecycleOwner,
            Observer {
                if (!shopProductAdapter.isLoading && getSelectedFragment() != this) {
                    shopProductFilterParameter = it
                    changeSortData(sortId)
                }
            }
        )
    }

    private fun getSelectedFragment(): Fragment? {
        return (parentFragment as? ShopPageHeaderFragment)?.getSelectedFragmentInstance()
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking!!.sendAllTrackingQueue()
        invalidateMonitoringPlt()
    }

    override fun onDestroy() {
        viewModel?.shopSortFilterData?.removeObservers(this)
        viewModel?.membershipData?.removeObservers(this)
        viewModel?.merchantVoucherData?.removeObservers(this)
        viewModel?.shopProductFeaturedData?.removeObservers(this)
        viewModel?.shopProductEtalaseHighlightData?.removeObservers(this)
        viewModel?.productListData?.removeObservers(this)
        viewModel?.claimMembershipResp?.removeObservers(this)
        viewModel?.newMembershipData?.removeObservers(this)
        viewModel?.newMerchantVoucherData?.removeObservers(this)
        viewModel?.bottomSheetFilterLiveData?.removeObservers(this)
        viewModel?.shopProductFilterCountLiveData?.removeObservers(this)
        viewModel?.flush()
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.removeObservers(this)
        super.onDestroy()
    }

    private fun observeViewModelLiveData() {
        viewModel?.shopSortFilterData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetEtalaseListData(it.data.etalaseList)
                    }
                    is Fail -> {
                        showErrorToasterWithRetry(it.throwable)
                    }
                }
            }
        )

        viewModel?.membershipData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetMembershipData(it.data)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel?.merchantVoucherData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetMerchantVoucherData(it.data)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel?.shopProductFeaturedData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetShopProductFeaturedData(it.data)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel?.shopProductEtalaseHighlightData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetShopProductEtalaseHighlightData(it.data)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel?.productListData?.observe(
            viewLifecycleOwner,
            Observer {
                stopMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_MIDDLE)
                startMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_RENDER)
                startMonitoringPltRenderPage()
                when (it) {
                    is Success -> {
                        val totalProduct = it.data.totalProductData
                        val currentPage = it.data.currentPage
                        if (!isOwner) {
                            val isProductListEmpty = (it.data.listShopProductUiModel.size.isZero() && currentPage == Int.ONE) || totalProduct.isZero()
                            updateProductChangeGridSection(isProductListEmpty, totalProduct)
                        }
                        onSuccessGetProductListData(it.data.hasNextPage, it.data.listShopProductUiModel)
                        productListName = it.data.listShopProductUiModel.joinToString(",") { product -> product.name.orEmpty() }
                    }
                    is Fail -> {
                        val throwable = it.throwable
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeViewModelLiveData.name,
                                liveDataName = ShopPageProductListViewModel::productListData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = ErrorHandler.getErrorMessage(context, throwable),
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_PRODUCT_TAB_BUYER_FLOW_TAG
                            )
                        }
                        showErrorToasterWithRetry(throwable)
                    }
                }
                getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        stopMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_RENDER)
                        stopMonitoringPltRenderPage()
                        stopMonitoringPerformance()
                        view?.let { view ->
                            getRecyclerView(view)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        )

        viewModel?.claimMembershipResp?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessClaimBenefit(it.data)
                    is Fail -> onErrorGetMembershipInfo(it.throwable)
                }
            }
        )

        viewModel?.newMembershipData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetMembershipData(it.data)
                        shopProductAdapter.refreshMembershipData()
                    }
                    is Fail -> onErrorGetMembershipInfo(it.throwable)
                }
            }
        )

        viewModel?.newMerchantVoucherData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetMerchantVoucherData(it.data)
                        shopProductAdapter.refreshMerchantVoucherData()
                    }
                    is Fail -> onErrorGetMerchantVoucher()
                }
            }
        )

        viewModel?.bottomSheetFilterLiveData?.observe(
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

        viewModel?.shopProductFilterCountLiveData?.observe(
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
    }

    private fun updateProductChangeGridSection(isProductListEmpty: Boolean, totalProduct: Int = 0) {
        shopProductAdapter.updateShopPageProductChangeGridSectionIcon(isProductListEmpty, totalProduct, gridType)
    }

    private fun onSuccessGetShopProductFilterCount(count: Int = Int.ZERO, isFulfillmentFilterActive: Boolean = false) {
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

    private fun showErrorToasterWithRetry(error: Throwable) {
        hideLoading()
        updateStateScrollListener()
        if (shopProductAdapter.shopProductUiModelList.size > 0) {
            onGetListErrorWithExistingData(error)
        } else {
            shopProductAdapter.clearAllElements()
            onGetListErrorWithEmptyData(error)
        }
    }

    private fun onErrorGetMerchantVoucher() {
        shopProductAdapter.clearMerchantVoucherData()
    }

    private fun onSuccessGetProductListData(hasNextPage: Boolean, productList: List<ShopProductUiModel>) {
        hideLoading()
        if (productList.isEmpty()) {
            shopProductAdapter.clearAllNonDataElement()
            if (isOwner && (selectedEtalaseId == ALL_ETALASE_ID || selectedEtalaseId.isEmpty())) {
                shopProductAdapter.addSellerAddProductDataModel()
            }
            shopProductAdapter.addEmptyDataModel(emptyDataViewModel)
        } else {
            if (isLoadingNewProductData) {
                shopProductAdapter.clearAllNonDataElement()
                shopProductAdapter.clearProductList()
                if (isOwner && (selectedEtalaseId == ALL_ETALASE_ID || selectedEtalaseId.isEmpty())) {
                    shopProductAdapter.addSellerAddProductDataModel()
                }
                endlessRecyclerViewScrollListener.resetState()
            }
            isLoadingNewProductData = false
            shopProductAdapter.setProductListDataModel(productList)
            updateScrollListenerState(hasNextPage)
        }
    }

    private fun onSuccessGetShopProductEtalaseHighlightData(data: ShopProductEtalaseHighlightUiModel) {
        if (data.getEtalaseHighlightCarouselUiModelList()?.isNotEmpty() == true) {
            shopProductAdapter.setShopProductEtalaseHighlightDataModel(data)
        }
    }

    private fun onSuccessGetShopProductFeaturedData(data: ShopProductFeaturedUiModel) {
        val listFeaturedProduct = data.shopProductFeaturedViewModelList
        if (listFeaturedProduct?.isNotEmpty() == true) {
            shopProductAdapter.setShopProductFeaturedDataModel(data)
        }
    }

    private fun onSuccessGetMerchantVoucherData(data: ShopMerchantVoucherUiModel) {
        shopPageTracking?.impressionSeeEntryPointMerchantVoucherCoupon(shopId, viewModel?.userId)
        shopProductAdapter.setMerchantVoucherDataModel(data)
    }

    private fun onSuccessGetMembershipData(data: MembershipStampProgressUiModel) {
        if (data.listOfData.isNotEmpty()) {
            shopProductAdapter.setMembershipDataModel(data)
        }
    }

    private fun onSuccessGetEtalaseListData(data: List<ShopEtalaseItemDataModel>) {
        defaultEtalaseName = data.firstOrNull()?.etalaseName.orEmpty()
        val etalaseItemDataModel = if (selectedEtalaseId.isEmpty()) {
            ShopEtalaseItemDataModel(
                etalaseId = data[0].etalaseId,
                etalaseName = data[0].etalaseName,
                etalaseBadge = data[0].etalaseBadge
            )
        } else {
            data.firstOrNull {
                it.etalaseId == selectedEtalaseId
            } ?: ShopEtalaseItemDataModel()
        }
        val shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
            selectedEtalaseId = selectedEtalaseId,
            selectedEtalaseName = selectedEtalaseName,
            selectedSortId = sortId,
            selectedSortName = sortName,
            filterIndicatorCounter = getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        shopProductAdapter.setSortFilterData(shopProductSortFilterUiModel)
        if (!viewModel?.isMyShop(shopId).orTrue()) {
            viewModel?.getBuyerViewContentData(
                shopId,
                data,
                isShopWidgetAlreadyShown(),
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
                context,
                isEnableDirectPurchase
            )
        }
        if (initialProductListData == null) {
            viewModel?.getProductListData(
                shopId,
                START_PAGE,
                ShopUtil.getProductPerPage(context),
                etalaseItemDataModel.etalaseId,
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
                isEnableDirectPurchase
            )
        }
    }

    private fun isShopWidgetAlreadyShown(): Boolean {
        return (parentFragment as? ShopPageHeaderFragment)?.isShopWidgetAlreadyShown() ?: false
    }

    private fun onSuccessClaimBenefit(data: MembershipClaimBenefitResponse) {
        if (data.membershipClaimBenefitResponse.title == "") {
            if (data.membershipClaimBenefitResponse.resultStatus.message.isNotEmpty()) {
                showToasterError(
                    data.membershipClaimBenefitResponse.resultStatus.message.firstOrNull()
                        ?: ""
                )
            } else {
                showToasterError(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown))
            }
        } else {
            val bottomSheetMembership = MembershipBottomSheetSuccess.newInstance(
                data.membershipClaimBenefitResponse.title,
                data.membershipClaimBenefitResponse.subTitle,
                data.membershipClaimBenefitResponse.resultStatus.code,
                lastQuestId
            )
            bottomSheetMembership.setListener(this)
            fragmentManager?.let {
                bottomSheetMembership.show(it, "membership_shop_page")
            }
        }
    }

    private fun showToasterError(message: String) {
        activity?.let {
            Toaster.make(requireView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun onErrorGetMembershipInfo(t: Throwable) {
        shopProductAdapter.clearMembershipData()
        activity?.let {
            Toaster.make(requireView(), ErrorHandler.getErrorMessage(context, t), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    fun clearCache() {
        viewModel?.clearCache()
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

    override fun onEtalaseFilterClicked() {
        if (!isOwner) {
            shopPageTracking?.clickEtalaseChip(
                getSelectedTabName(),
                shopId,
                userId
            )
        }
        redirectToEtalasePicker()
    }

    override fun onSortFilterClicked() {
        shopPageTracking?.clickSort(isOwner, customDimensionShopPage)
        redirectToShopSortPickerPage()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onClearFilterClicked() {
        if (shopProductAdapter.isLoading) {
            return
        }
        shopPageTracking?.clickClearFilter(
            isOwner,
            customDimensionShopPage
        )
        changeShopProductFilterParameterSharedData()
        changeSortData("")
        scrollToChangeProductGridSegment()
    }

    override fun setSortFilterMeasureHeight(measureHeight: Int) {
        shopSortFilterHeight = measureHeight
    }

    override fun onFilterClicked() {
        shopPageTracking?.clickFilterChips(productListName, customDimensionShopPage)
        showBottomSheetFilter()
    }

    private fun showBottomSheetFilter() {
        val mapParameter = if (sortId.isNotEmpty()) {
            shopProductFilterParameter?.getMapData()
        } else {
            shopProductFilterParameter?.getMapDataWithDefaultSortId()
        }
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
        viewModel?.getBottomSheetFilterData(shopId)
    }

    fun setInitialProductListData(initialProductListData: ShopProduct.GetShopProduct) {
        this.initialProductListData = initialProductListData
    }

    private fun changeProductListGridView(gridType: ShopProductViewGridType) {
        shopProductAdapter.updateShopPageProductChangeGridSectionIcon(gridType)
        shopProductAdapter.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(
        initialGridType: ShopProductViewGridType,
        finalGridType: ShopProductViewGridType
    ) {
        if (!isOwner) {
            shopPageTracking?.clickProductListToggle(initialGridType, finalGridType, shopId, userId)
        }
        changeProductListGridView(finalGridType)
        scrollToChangeProductGridSegment()
        shopChangeProductGridSharedViewModel?.changeSharedProductGridType(finalGridType)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        val isResetButtonVisible = sortFilterBottomSheet?.bottomSheetAction?.isVisible
        sortFilterBottomSheet = null
        shopProductFilterParameter?.clearParameter()
        shopProductFilterParameter?.setMapData(applySortFilterModel.mapParameter)
        if (isResetButtonVisible == false) {
            sortId = ""
        }
        changeShopProductFilterParameterSharedData()
        changeSortData(shopProductFilterParameter?.getSortId().orEmpty())
        scrollToChangeProductGridSegment()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    private fun changeSortData(sortId: String) {
        this.sortId = sortId
        shopProductAdapter.changeSelectedSortFilter(this.sortId, sortName)
        shopProductAdapter.changeSortFilterIndicatorCounter(
            getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        shopProductAdapter.refreshSticky()
        initialProductListData = null
        if (!isOnViewCreated) {
            shopProductAdapter.clearProductList()
            loadNewProductData()
        }
    }

    private fun changeShopProductFilterParameterSharedData() {
        shopProductFilterParameterSharedViewModel?.changeSharedSortData(
            shopProductFilterParameter ?: ShopProductFilterParameter()
        )
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        shopProductFilterParameterSharedViewModel?.setFulfillmentFilterActiveStatus(mapParameter)
        val isFulfillmentFilterActive = shopProductFilterParameterSharedViewModel?.isFulfillmentFilterActive.orFalse()
        if (isFulfillmentFilterActive) {
            // if fulfillment filter is active then avoid gql call to get total product
            onSuccessGetShopProductFilterCount(isFulfillmentFilterActive = isFulfillmentFilterActive)
        } else {
            viewModel?.getFilterResultCount(
                shopId,
                ShopUtil.getProductPerPage(context),
                tempShopProductFilterParameter,
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
            )
        }
    }

    private fun applySortFilterTracking(selectedSortName: String, selectedFilterMap: Map<String, String>) {
        if (!isOwner) {
            shopPageTracking?.clickApplyFilter(selectedSortName, selectedFilterMap, userId)
        }
    }

    override fun scrollToTop(isUserClick: Boolean) {
        if(isUserClick)
            isClickToScrollToTop = true
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.hideScrollToTopButton()
    }

    private fun showScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.showScrollToTopButton()
    }

    private fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        viewModel?.handleAtcFlow(
            quantity,
            shopId,
            ShopPageConstant.ShopProductCardAtc.CARD_PRODUCT,
            shopProductUiModel
        )
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }
}
