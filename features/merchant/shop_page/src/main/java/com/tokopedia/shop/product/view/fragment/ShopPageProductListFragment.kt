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
import androidx.annotation.DimenRes
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
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FEATURED_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_GROUP_POSITION_FULFILLMENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MEMBERSHIP_CLICK_MEMBER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MEMBERSHIP_COUPON_CHECK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MEMBERSHIP_COUPON_CLAIM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MEMBERSHIP_DETAIL_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SCREEN_ADD_PRODUCT
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.constant.ShopPageConstant.GO_TO_MEMBERSHIP_DETAIL
import com.tokopedia.shop.common.constant.ShopPageConstant.START_PAGE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_PRODUCT_TAB_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_MIDDLE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PRODUCT_RENDER
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.util.ShopPageProductChangeGridRemoteConfig
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.view.listener.InterfaceShopPageClickScrollToTop
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.viewmodel.ShopChangeProductGridSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.common.widget.MembershipBottomSheetSuccess
import com.tokopedia.shop.product.util.StaggeredGridLayoutManagerWrapper
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.NewShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPagePerformanceMonitoringListener
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ShopProductAddViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductsEmptyViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.android.synthetic.main.fragment_shop_page_home.*
import javax.inject.Inject

class ShopPageProductListFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        MembershipStampAdapter.MembershipStampAdapterListener,
        ShopProductClickedListener,
        ShopCarouselSeeAllClickedListener,
        BaseEmptyViewHolder.Callback,
        ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
        MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener,
        ShopProductAddViewHolder.ShopProductAddViewHolderListener,
        ShopProductsEmptyViewHolder.ShopProductsEmptyViewHolderListener,
        WishListActionListener,
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
        private const val GRID_SPAN_COUNT = 2
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
        private const val KEY_SHOP_HOME_TYPE = "SHOP_HOME_TYPE"
        private const val KEY_IS_OFFICIAL = "IS_OFFICIAL"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"

        @JvmStatic
        fun createInstance(
                shopId: String,
                shopName: String,
                isOfficial: Boolean,
                isGoldMerchant: Boolean,
                shopHomeType: String,
                shopAttribution: String?,
                shopRef: String
        ): ShopPageProductListFragment {
            val fragment = ShopPageProductListFragment()
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_HOME_TYPE, shopHomeType)
            bundle.putBoolean(KEY_IS_OFFICIAL, isOfficial)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(SHOP_ATTRIBUTION, shopAttribution)
            fragment.arguments = bundle
            fragment.shopRef = shopRef
            return fragment
        }
    }

    override val isOwner: Boolean
        get() = if (::viewModel.isInitialized) {
            viewModel.isMyShop(shopId)
        } else false

    private val isLogin: Boolean
        get() = if (::viewModel.isInitialized) {
            viewModel.isLogin
        } else false

    lateinit var viewModel: ShopPageProductListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
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
        get() = if (::viewModel.isInitialized) {
            viewModel.getSortNameById(sortId)
        } else {
            ""
        }
    private val userId: String
        get() = if (::viewModel.isInitialized) {
            viewModel.userId
        } else {
            ""
        }
    private var urlNeedTobBeProceed: String? = null
    private var shopId: String = ""
    private var shopName: String = ""
    private var shopHomeType: String = ""
    private var shopRef: String = ""
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
    private var threeDotsClickShopTrackingType = -1
    private var initialProductListData : ShopProduct.GetShopProduct? = null
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

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
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

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), message)
        }
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

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConsInternalHome.HOME_WISHLIST)
    }

    private fun loadNewProductData() {
        isLoadingNewProductData = true
        shopProductAdapter.clearAllNonDataElement()
        shopProductAdapter.clearProductList()
        showLoading()
        viewModel.getProductListData(
                shopId,
                START_PAGE,
                selectedEtalaseId,
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
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
                    (parentFragment as? NewShopPageFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex)
                    (parentFragment as? NewShopPageFragment)?.hideScrollToTopButton()
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                super.onScrollStateChanged(recyclerView, state)
                if(state == RecyclerView.SCROLL_STATE_IDLE){
                    val firstCompletelyVisibleItemPosition = (layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0).orZero()
                    if (firstCompletelyVisibleItemPosition > 0) {
                        (parentFragment as? NewShopPageFragment)?.showScrollToTopButton()
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
        shopPageTracking?.sendEventMembership(MEMBERSHIP_COUPON_CLAIM)
        lastQuestId = questId
        viewModel.claimMembershipBenefit(questId)
    }

    override fun goToVoucherOrRegister(url: String?, clickOrigin: String?) {
        val intent: Intent = if (url == null) {
            shopPageTracking?.sendEventMembership(MEMBERSHIP_COUPON_CHECK)
            RouteManager.getIntent(context, ApplinkConst.COUPON_LISTING)
        } else {
            if (clickOrigin == GO_TO_MEMBERSHIP_DETAIL) {
                shopPageTracking?.sendEventMembership(MEMBERSHIP_DETAIL_PAGE)
            } else {
                shopPageTracking?.sendEventMembership(MEMBERSHIP_CLICK_MEMBER)
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
        if(defaultEtalaseName.isNotEmpty()) {
            when (shopTrackType) {
                ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.clickProduct(
                        isOwner,
                        isLogin,
                        getSelectedEtalaseChip(),
                        if (isOwner) "" else FEATURED_PRODUCT,
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
                        productPosition + 1,
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        false,
                        shopProductUiModel.isUpcoming
                )
                ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickProduct(isOwner,
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
                        productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.isUpcoming
                )
                ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.clickProduct(isOwner,
                        isLogin,
                        getSelectedEtalaseChip(),
                        if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductUiModel),
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
                        productPosition + 1,
                        shopId,
                        shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductAdapter.getEtalaseNameHighLightType(shopProductUiModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        shopProductUiModel.isUpcoming
                )
            }
        }
        goToPDP(
                shopProductUiModel.id ?: "",
                attribution,
                shopPageTracking?.getListNameOfProduct(PRODUCT, getSelectedEtalaseChip())
                        ?: ""
        )
    }

    override fun onProductImpression(shopProductUiModel: ShopProductUiModel, shopTrackType: Int, productPosition: Int) {
        when (shopTrackType) {
            ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.impressionProductList(
                    isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else FEATURED_PRODUCT,
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
                    productPosition + 1,
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    false,
                    shopProductUiModel.isUpcoming
            )
            ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.impressionProductList(isOwner,
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
                    productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.isUpcoming
            )
            ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.impressionProductList(isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductUiModel),
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
                    productPosition + 1,
                    shopId,
                    shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductAdapter.getEtalaseNameHighLightType(shopProductUiModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductUiModel.isUpcoming
            )
        }
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

                    shopPageTracking?.clickEtalaseChip(
                            isOwner,
                            etalaseName,
                            CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
                    )
                    shopPageTracking?.clickMoreMenuChip(
                            isOwner,
                            etalaseName,
                            customDimensionShopPage
                    )
                    if (shopPageTracking != null) {
                        shopPageTracking!!.clickMenuFromMoreMenu(
                                viewModel.isMyShop(shopId),
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
                    intent.putExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
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
                    shopPageTracking?.sendScreenShopPage(shopId, shopType, SCREEN_ADD_PRODUCT)
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

    private fun scrollToChangeProductGridSegment() {
        //multiply with 2 to make first dy value on onScroll function greater than rv top padding
        getRecyclerView(view)?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
                shopProductAdapter.shopChangeProductGridSegment,
                shopSortFilterHeight + recyclerViewTopPadding
        )
    }

    private fun loadMembership() {
        viewModel.getNewMembershipData(shopId)
    }

    private fun loadMerchantVoucher() {
        viewModel.getNewMerchantVoucher(shopId)
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
                            getSelectedEtalaseChip(), FEATURED_PRODUCT,
                            CustomDimensionShopPageProduct.create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant,
                                    shopProductViewModel.id, shopRef
                            ),
                            shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                            false,
                            shopProductViewModel.isUpcoming)
                    ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickWishlist(
                            !shopProductViewModel.isWishList,
                            isLogin,
                            getSelectedEtalaseChip(), getSelectedEtalaseChip(),
                            CustomDimensionShopPageProduct.create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant,
                                    shopProductViewModel.id, shopRef),
                            shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                            shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                            shopProductViewModel.isUpcoming)
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
                                        shopProductViewModel.id, shopRef
                                ),
                                shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                                shopProductAdapter.getEtalaseNameHighLightType(shopProductViewModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                                shopProductViewModel.isUpcoming)
                }
            }
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }

        threeDotsClickShopProductUiModel = null
        threeDotsClickShopTrackingType = -1
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

    override fun onSeeAllClicked(shopProductEtalaseChipItemViewModel: ShopEtalaseItemDataModel) {
        shopPageTracking?.clickHighLightSeeAll(customDimensionShopPage)
        val intent = ShopProductListResultActivity.createIntent(activity,
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
        return ShopProductAdapterTypeFactory(
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                null,
                this,
                true,
                deviceWidth,
                ShopTrackProductTypeDef.PRODUCT
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
        initialProductListData?.let{
            viewModel.setInitialProductList(shopId, it)
        }
        viewModel.getShopFilterData(shopId)
        isOnViewCreated = false
    }

    private fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(
                    it,
                    url.orEmpty(),
                    shopId,
                    viewModel.isLogin,
                    viewModel.userDeviceId,
                    viewModel.userId
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
            val snackbar = Snackbar.make(it.findViewById(android.R.id.content), stringToShow,
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(requireActivity().getString(R.string.label_close)) { snackbar.dismiss() }
            snackbar.setActionTextColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            snackbar.show()
        }
    }

    private fun onErrorAddToWishList(e: Throwable) {
        context?.let {
            if (!viewModel.isLogin || e is UserNotLoginException) {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
                return
            }
        }
        activity?.let {
            NetworkErrorHelper.showCloseSnackbar(it, ErrorHandler.getErrorMessage(it, e))
        }
    }

    private fun goToPDP(productId: String, attribution: String?, listNameOfProduct: String) {
        context?.let {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            startActivity(intent)
        }
    }

    override fun loadData(page: Int) {
        viewModel.getProductListData(
                shopId,
                page,
                selectedEtalaseId,
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
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
                shopPageTracking?.impressionZeroProduct(CustomDimensionShopPage.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant
                ))
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
        context?.let { shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it)) }
        remoteConfig = FirebaseRemoteConfigImpl(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopProductFilterParameterSharedViewModel::class.java)
        shopChangeProductGridSharedViewModel = ViewModelProvider(requireActivity()).get(ShopChangeProductGridSharedViewModel::class.java)
        attribution = arguments?.getString(SHOP_ATTRIBUTION, "") ?: ""
        staggeredGridLayoutManager = StaggeredGridLayoutManagerWrapper(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    private fun startMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startCustomMetric(it, tag)
            }
        }
    }

    private fun stopMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopCustomMetric(it, tag)
            }
        }
    }

    private fun invalidateMonitoringPlt() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.invalidateMonitoringPlt(it)
            }
        }
    }

    private fun stopMonitoringPerformance() {
        (activity as? ShopPageActivity)?.stopShopProductTabPerformanceMonitoring()
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
    }

    override fun onResume() {
        loadInitialDataAfterOnViewCreated()
        super.onResume()
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
            shopHomeType = it.getString(KEY_SHOP_HOME_TYPE, "")
        }
    }

    private fun observeShopChangeProductGridSharedViewModel() {
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.observe(viewLifecycleOwner, Observer {
            if (!shopProductAdapter.isLoading) {
                gridType = it
                changeProductListGridView(it)
            }
        })
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(viewLifecycleOwner, Observer {
            if (!shopProductAdapter.isLoading) {
                shopProductFilterParameter = it
                changeSortData(sortId)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking!!.sendAllTrackingQueue()
        invalidateMonitoringPlt()
    }

    override fun onDestroy() {
        viewModel.shopSortFilterData.removeObservers(this)
        viewModel.membershipData.removeObservers(this)
        viewModel.merchantVoucherData.removeObservers(this)
        viewModel.shopProductFeaturedData.removeObservers(this)
        viewModel.shopProductEtalaseHighlightData.removeObservers(this)
        viewModel.productListData.removeObservers(this)
        viewModel.claimMembershipResp.removeObservers(this)
        viewModel.newMembershipData.removeObservers(this)
        viewModel.newMerchantVoucherData.removeObservers(this)
        viewModel.bottomSheetFilterLiveData.removeObservers(this)
        viewModel.shopProductFilterCountLiveData.removeObservers(this)
        viewModel.flush()
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.removeObservers(this)
        super.onDestroy()
    }

    private fun observeViewModelLiveData() {
        viewModel.shopSortFilterData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetEtalaseListData(it.data.etalaseList)
                }
                is Fail -> {
                    showErrorToasterWithRetry(it.throwable)
                }
            }
        })

        viewModel.membershipData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMembershipData(it.data)
                }
            }
        })

        viewModel.merchantVoucherData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMerchantVoucherData(it.data)
                }
            }
        })

        viewModel.shopProductFeaturedData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductFeaturedData(it.data)
                }
            }
        })

        viewModel.shopProductEtalaseHighlightData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductEtalaseHighlightData(it.data)
                }
            }
        })

        viewModel.productListData.observe(viewLifecycleOwner, Observer {
            stopMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_MIDDLE)
            startMonitoringPltCustomMetric(SHOP_TRACE_PRODUCT_RENDER)
            startMonitoringPltRenderPage()
            when (it) {
                is Success -> {
                    val totalProduct = it.data.totalProductData
                    if (shopProductAdapter.shopProductUiModelList.isEmpty() && totalProduct != 0) {
                        updateEtalaseTitleSection()
                    }
                    if(!isOwner)
                        updateProductChangeGridSection(totalProduct)
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
        })

        viewModel.claimMembershipResp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessClaimBenefit(it.data)
                is Fail -> onErrorGetMembershipInfo(it.throwable)
            }
        })

        viewModel.newMembershipData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMembershipData(it.data)
                    shopProductAdapter.refreshMembershipData()
                }
                is Fail -> onErrorGetMembershipInfo(it.throwable)
            }
        })

        viewModel.newMerchantVoucherData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMerchantVoucherData(it.data)
                    shopProductAdapter.refreshMerchantVoucherData()
                }
                is Fail -> onErrorGetMerchantVoucher()
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

    }

    private fun updateProductChangeGridSection(totalProduct: Int) {
        if (ShopPageProductChangeGridRemoteConfig.isFeatureEnabled(remoteConfig)) {
            shopProductAdapter.updateShopPageProductChangeGridSectionIcon(totalProduct, gridType)
        }
    }

    private fun updateEtalaseTitleSection() {
        if (!ShopPageProductChangeGridRemoteConfig.isFeatureEnabled(remoteConfig)) {
            shopProductAdapter.setShopProductEtalaseTitleData(ShopProductEtalaseTitleUiModel(
                    getSelectedEtalaseChip(),
                    ""
            ))
        }
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
        shopPageTracking?.impressionSeeEntryPointMerchantVoucherCoupon(shopId, viewModel.userId)
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
        if (initialProductListData?.data?.isNotEmpty() == true) {
            updateEtalaseTitleSection()
        }
        if (!viewModel.isMyShop(shopId)) {
            viewModel.getBuyerViewContentData(
                    shopId,
                    data,
                    isShowNewShopHomeTab(),
                    ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
            )
        }
        if (initialProductListData == null){
            viewModel.getProductListData(
                    shopId,
                    START_PAGE,
                    etalaseItemDataModel.etalaseId,
                    shopProductFilterParameter ?: ShopProductFilterParameter(),
                    ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
            )
        }
    }

    private fun isShowNewShopHomeTab(): Boolean {
        return shopHomeType == ShopHomeType.NATIVE
    }

    private fun onSuccessClaimBenefit(data: MembershipClaimBenefitResponse) {
        if (data.membershipClaimBenefitResponse.title == "") {
            if (data.membershipClaimBenefitResponse.resultStatus.message.isNotEmpty()) {
                showToasterError(data.membershipClaimBenefitResponse.resultStatus.message.firstOrNull()
                        ?: "")
            } else {
                showToasterError(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown))
            }
        } else {
            val bottomSheetMembership = MembershipBottomSheetSuccess.newInstance(data.membershipClaimBenefitResponse.title,
                    data.membershipClaimBenefitResponse.subTitle,
                    data.membershipClaimBenefitResponse.resultStatus.code, lastQuestId)
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
        if(::viewModel.isInitialized)
            viewModel.clearCache()
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

    fun setInitialProductListData(initialProductListData: ShopProduct.GetShopProduct) {
        this.initialProductListData = initialProductListData
    }

    private fun changeProductListGridView(gridType: ShopProductViewGridType){
        shopProductAdapter.updateShopPageProductChangeGridSectionIcon(gridType)
        shopProductAdapter.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(gridType: ShopProductViewGridType) {
        val productListName =  shopProductAdapter.shopProductUiModelList.joinToString(","){
            it.name.orEmpty()
        }
        shopPageTracking?.clickProductListToggle(productListName, isOwner, customDimensionShopPage)
        changeProductListGridView(gridType)
        scrollToChangeProductGridSegment()
        shopChangeProductGridSharedViewModel?.changeSharedProductGridType(gridType)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        val isResetButtonVisible = sortFilterBottomSheet?.bottomSheetAction?.isVisible
        sortFilterBottomSheet = null
        shopProductFilterParameter?.clearParameter()
        shopProductFilterParameter?.setMapData(applySortFilterModel.mapParameter)
        if(isResetButtonVisible == false){
            sortId = ""
        }
        changeShopProductFilterParameterSharedData()
        changeSortData(shopProductFilterParameter?.getSortId().orEmpty())
        scrollToChangeProductGridSegment()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    private fun changeSortData(sortId: String){
        this.sortId = sortId
        shopProductAdapter.changeSelectedSortFilter(this.sortId, sortName)
        shopProductAdapter.changeSortFilterIndicatorCounter(getIndicatorCount(
                shopProductFilterParameter?.getMapData()
        ))
        shopProductAdapter.refreshSticky()
        initialProductListData = null
        if(!isOnViewCreated) {
            shopProductAdapter.clearProductList()
            loadNewProductData()
        }
    }

    private fun changeShopProductFilterParameterSharedData(){
        shopProductFilterParameterSharedViewModel?.changeSharedSortData(
                shopProductFilterParameter?:ShopProductFilterParameter()
        )
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        viewModel.getFilterResultCount(
                shopId,
                tempShopProductFilterParameter,
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        )
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

    override fun scrollToTop() {
        isClickToScrollToTop = true
        recycler_view?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }
}