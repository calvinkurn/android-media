package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopPageConstant.GO_TO_MEMBERSHIP_DETAIL
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.widget.MembershipBottomSheetSuccess
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPagePerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageProductTabPerformanceMonitoringListener
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
import com.tokopedia.shop.product.view.viewmodel.ShopSortSharedViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.android.synthetic.main.fragment_new_shop_page_product_list.*
import javax.inject.Inject

class ShopPageProductListFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        MembershipStampAdapter.MembershipStampAdapterListener,
        ShopProductClickedListener,
        ShopCarouselSeeAllClickedListener,
        BaseEmptyViewHolder.Callback,
        ShopProductSortFilterViewHolder.ShopProductEtalaseChipListViewHolderListener,
        MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener,
        ShopProductAddViewHolder.ShopProductAddViewHolderListener,
        ShopProductsEmptyViewHolder.ShopProductsEmptyViewHolderListener,
        WishListActionListener,
        ShopProductImpressionListener {

    companion object {
        private const val ETALASE_TO_SHOW = 5
        private const val REQUEST_CODE_USER_LOGIN = 100
        private const val REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW = 101
        private const val REQUEST_CODE_ETALASE = 205
        private const val REQUEST_CODE_LOGIN_USE_VOUCHER = 206
        private const val REQUEST_CODE_MERCHANT_VOUCHER = 207
        private const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 208
        private const val REQUEST_CODE_MEMBERSHIP_STAMP = 2091
        private const val REQUEST_CODE_ADD_ETALASE = 288
        private const val REQUEST_CODE_ADD_PRODUCT = 3697
        private const val GRID_SPAN_COUNT = 2
        private const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        private const val SELECTED_ETALASE_TYPE_DEFAULT_VALUE = -10
        const val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        const val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        const val SAVED_SHOP_ID = "saved_shop_id"
        const val SAVED_SHOP_REF = "saved_shop_ref"
        const val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        const val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        const val SAVED_SHOP_SORT_ID = "saved_shop_sort_id"
        const val SAVED_SHOP_SORT_NAME = "saved_shop_sort_name"
        const val SAVED_NEED_TO_RELOAD = "saved_need_to_reload"
        const val ALL_ETALASE_ID = "etalase"
        const val SOLD_ETALASE_ID = "sold"
        private const val REQUEST_CODE_SORT = 300

        const val BUNDLE_SELECTED_ETALASE_ID = "selectedEtalaseId"
        const val BUNDLE_IS_SHOW_DEFAULT = "isShowDefault"
        const val BUNDLE_IS_SHOW_ZERO_PRODUCT = "isShowZeroProduct"
        const val BUNDLE_SHOP_ID = "shopId"
        const val BUNDLE = "bundle"
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_SHOP_HOME_TYPE = "SHOP_HOME_TYPE"
        private const val KEY_IS_OFFICIAL = "IS_OFFICIAL"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        private const val IS_ACTIVITY_DESTROYED = "is_activity_destroyed"

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
    private var recyclerView: RecyclerView? = null
    private var attribution: String = ""
    private var isLoadingNewProductData = false
    private var sortId = ""
    private var sortName = ""
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
    private var recyclerViewTopPadding = 0
    private var threeDotsClickShopProductViewModel: ShopProductViewModel? = null
    private var shopSortSharedViewModel: ShopSortSharedViewModel? = null
    private var threeDotsClickShopTrackingType = -1
    private var initialProductListData : Pair<Boolean, List<ShopProductViewModel>>? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val shopProductAdapter: ShopProductAdapter
        get() = adapter as ShopProductAdapter
    private var selectedEtalaseType: Int = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
    private val customDimensionShopPage: CustomDimensionShopPage
        get() {
            return CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
        }

    override fun chooseProductClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConst.PRODUCT_ADD)
        }
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    override fun onSuccessAddWishlist(productId: String) {
        showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist))
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

    private fun loadNewProductData() {
        isLoadingNewProductData = true
        shopProductAdapter.clearAllNonDataElement()
        shopProductAdapter.clearProductList()
        showLoading()
        viewModel.getNewProductListData(shopId, selectedEtalaseId, sortId)
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view)?.let {
            it.clearOnScrollListeners()
            it.layoutManager = staggeredGridLayoutManager
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
        recyclerViewTopPadding = recyclerView?.paddingTop ?: 0
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopProductAdapter) {
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

    override fun onThreeDotsClicked(shopProductViewModel: ShopProductViewModel, shopTrackType: Int) {
        threeDotsClickShopProductViewModel = shopProductViewModel
        threeDotsClickShopTrackingType = shopTrackType

        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopProductViewModel.isWishList,
                        productId = shopProductViewModel.id ?: ""
                )
        )
    }

    override fun onProductClicked(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int) {
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
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    false,
                    shopProductViewModel.isUpcoming
            )
            ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickProduct(isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else getSelectedEtalaseChip(),
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming
            )
            ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.clickProduct(isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductAdapter.getEtalaseNameHighLightType(shopProductViewModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming
            )
        }
        goToPDP(
                shopProductViewModel.id ?: "",
                attribution,
                shopPageTracking?.getListNameOfProduct(PRODUCT, getSelectedEtalaseChip())
                        ?: ""
        )
    }

    override fun onProductImpression(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int) {
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
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    false,
                    shopProductViewModel.isUpcoming
            )
            ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.impressionProductList(isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else getSelectedEtalaseChip(),
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming
            )
            ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopPageTracking?.impressionProductList(isOwner,
                    isLogin,
                    getSelectedEtalaseChip(),
                    if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductAdapter.getEtalaseNameHighLightType(shopProductViewModel) == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                val etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_ID)
                val etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_NAME)
                val isNeedToReloadData = data.getBooleanExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                shopPageTracking?.clickMoreMenuChip(
                        isOwner,
                        getSelectedEtalaseChip(),
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
            REQUEST_CODE_USER_LOGIN_FOR_WEBVIEW -> if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(urlNeedTobBeProceed)) {
                promoClicked(urlNeedTobBeProceed)
            }
            REQUEST_CODE_LOGIN_USE_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.clearMerchantVoucherCache()
                    loadMerchantVoucher()
                }
            }
            REQUEST_CODE_MEMBERSHIP_STAMP -> {
                loadMembership()
            }
            REQUEST_CODE_USER_LOGIN -> {
                (parentFragment as? ShopPageFragment)?.refreshData()
            }
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (shopProductAdapter.isLoading) {
                        return
                    }
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    shopPageTracking?.sortProduct(sortName, isOwner, customDimensionShopPage)
                    shopSortSharedViewModel?.changeSharedSortData(sortId, sortName)
                    shopProductAdapter.changeSelectedSortFilter(sortId, sortName)
                    shopProductAdapter.refreshSticky()
                    updateInitialProductListSortId(sortId)
                    scrollToProductEtalaseSegment()
                    loadNewProductData()
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

    private fun scrollToProductEtalaseSegment() {
        //multiply with 2 to make first dy value on onScroll function greater than rv top padding
        recyclerView?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
                shopProductAdapter.shopProductEtalaseTitlePosition,
                stickySingleHeaderView.containerHeight
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
            threeDotsClickShopProductViewModel?.let { shopProductViewModel ->
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

        threeDotsClickShopProductViewModel = null
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
                this,
                null,
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
        startMonitoringPltNetworkRequest()
        showLoading()
        initialProductListData?.let{
            viewModel.setInitialProductList(it)
        }
        viewModel.getEtalaseData(shopId)
    }

    private fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(
                    it,
                    url,
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

//            bundle.putString(BUNDLE_SELECTED_ETALASE_ID, selectedEtalaseId)
//            bundle.putBoolean(BUNDLE_IS_SHOW_DEFAULT, true)
//            bundle.putBoolean(BUNDLE_IS_SHOW_ZERO_PRODUCT, false)
//            bundle.putString(BUNDLE_SHOP_ID, shopId)

            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)

            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    private fun showSnackBarClose(stringToShow: String) {
        activity?.let {
            val snackbar = Snackbar.make(it.findViewById(android.R.id.content), stringToShow,
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(com.tokopedia.design.R.string.close)) { snackbar.dismiss() }
            snackbar.setActionTextColor(Color.WHITE)
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
        viewModel.getNextProductListData(
                shopId,
                selectedEtalaseId,
                page,
                sortId
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
                ShopSellerEmptyProductAllEtalaseViewModel()
            } else if (isOwner && selectedEtalaseId == SOLD_ETALASE_ID) {
                title = getString(R.string.text_shop_no_product_seller)
                description = getString(R.string.text_shop_no_product_description_seller)
                ShopEmptyProductViewModel(isOwner, title, description)
            } else {
                title = getString(R.string.shop_product_limited_empty_products_title_owner)
                description = getString(R.string.shop_product_limited_empty_products_content_owner)
                ShopEmptyProductViewModel(isOwner, title, description)
            }
        } else {
            title = getString(R.string.text_shop_no_product)
            description = getString(R.string.text_shop_no_product_follow)
            ShopEmptyProductViewModel(isOwner, title, description)
        }
    }

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initPltMonitoring()
        super.onCreate(savedInstanceState)
        context?.let { shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it)) }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListViewModel::class.java)
        shopSortSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopSortSharedViewModel::class.java)
        attribution = arguments?.getString(SHOP_ATTRIBUTION, "") ?: ""
        staggeredGridLayoutManager = StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initPltMonitoring() {
        (activity as? ShopPageProductTabPerformanceMonitoringListener)?.initShopPageProductTabPerformanceMonitoring()
    }

    private fun startMonitoringPltNetworkRequest() {
        (activity as? ShopPageProductTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageProductTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPageProductTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageProductTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageProductTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageProductTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
        stopPerformanceMonitoring()
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
            sortId = it.getString(SAVED_SHOP_SORT_ID, "")
            sortName = it.getString(SAVED_SHOP_SORT_NAME, "")
        }
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        loadInitialData()
        observeShopSortSharedViewModel()
        observeViewModelLiveData()
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

    private fun observeShopSortSharedViewModel() {
        shopSortSharedViewModel?.sharedSortData?.observe(viewLifecycleOwner, Observer {
            if (!shopProductAdapter.isLoading) {
                sortId = it.first
                sortName = it.second
                shopProductAdapter.changeSelectedSortFilter(sortId, sortName)
                shopProductAdapter.refreshSticky()
                loadNewProductData()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking!!.sendAllTrackingQueue()
    }

    override fun onDestroy() {
        viewModel.etalaseListData.removeObservers(this)
        viewModel.membershipData.removeObservers(this)
        viewModel.merchantVoucherData.removeObservers(this)
        viewModel.shopProductFeaturedData.removeObservers(this)
        viewModel.shopProductEtalaseHighlightData.removeObservers(this)
        viewModel.productListData.removeObservers(this)
        viewModel.claimMembershipResp.removeObservers(this)
        viewModel.newMembershipData.removeObservers(this)
        viewModel.newMerchantVoucherData.removeObservers(this)
        viewModel.flush()
        shopSortSharedViewModel?.sharedSortData?.removeObservers(this)
        super.onDestroy()
    }

    private fun observeViewModelLiveData() {
        viewModel.etalaseListData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetEtalaseListData(it.data)
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

        viewModel.shopProductEtalaseTitleData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductEtalaseTitleData(it.data)
                }
            }
        })

        viewModel.productListData.observe(viewLifecycleOwner, Observer {
            startMonitoringPltRenderPage()
            when (it) {
                is Success -> {
                    onSuccessGetProductListData(it.data.first, it.data.second)
                }
                is Fail -> {
                    showErrorToasterWithRetry(it.throwable)
                }
            }
            getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    stopMonitoringPltRenderPage()
                    getRecyclerView(view)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
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
    }

    private fun showErrorToasterWithRetry(error: Throwable) {
        hideLoading()
        updateStateScrollListener()
        if (shopProductAdapter.shopProductViewModelList.size > 0) {
            onGetListErrorWithExistingData(error)
        } else {
            shopProductAdapter.clearAllElements()
            onGetListErrorWithEmptyData(error)
        }
        stopPerformanceMonitoring()
    }

    private fun onErrorGetMerchantVoucher() {
        shopProductAdapter.clearMerchantVoucherData()
    }

    private fun onSuccessGetProductListData(hasNextPage: Boolean, productList: List<ShopProductViewModel>) {
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

    private fun onSuccessGetShopProductEtalaseTitleData(data: ShopProductEtalaseTitleViewModel) {
        shopProductAdapter.setShopProductEtalaseTitleData(data)
    }

    private fun onSuccessGetShopProductEtalaseHighlightData(data: ShopProductEtalaseHighlightViewModel) {
        if (data.etalaseHighlightCarouselViewModelList.isNotEmpty()) {
            shopProductAdapter.setShopProductEtalaseHighlightDataModel(data)
        }
    }

    private fun onSuccessGetShopProductFeaturedData(data: ShopProductFeaturedViewModel) {
        val listFeaturedProduct = data.shopProductFeaturedViewModelList
        if (listFeaturedProduct.isNotEmpty()) {
            shopProductAdapter.setShopProductFeaturedDataModel(data)
        }
    }

    private fun onSuccessGetMerchantVoucherData(data: ShopMerchantVoucherViewModel) {
        shopPageTracking?.impressionUseMerchantVoucher(isOwner, data.shopMerchantVoucherViewModelArrayList, shopId)
        data.shopMerchantVoucherViewModelArrayList?.let {
            if (it.isNotEmpty())
                shopProductAdapter.setMerchantVoucherDataModel(data)
        }
    }

    private fun onSuccessGetMembershipData(data: MembershipStampProgressViewModel) {
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
                selectedSortName = sortName
        )
        shopProductAdapter.setSortFilterData(shopProductSortFilterUiModel)
        if (!viewModel.isMyShop(shopId)) {
            viewModel.getBuyerShopPageProductTabData(
                    shopId,
                    data,
                    etalaseItemDataModel,
                    sortId,
                    isShowNewShopHomeTab(),
                    initialProductListData
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
            Toaster.make(view!!, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun onErrorGetMembershipInfo(t: Throwable) {
        shopProductAdapter.clearMembershipData()
        activity?.let {
            Toaster.make(view!!, ErrorHandler.getErrorMessage(context, t), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun stopPerformanceMonitoring() {
        (activity as? ShopPagePerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
        (activity as? ShopPageActivity)?.stopShopProductTabPerformanceMonitoring()
    }

    fun clearCache() {
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
        shopPageTracking?.clickEtalaseChip(
                isOwner,
                getSelectedEtalaseChip(),
                CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
        )
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
        sortId = ""
        sortName = ""
        shopSortSharedViewModel?.changeSharedSortData(sortId, sortName)
        shopProductAdapter.changeSelectedSortFilter(sortId, sortName)
        shopProductAdapter.refreshSticky()
        //multiply with 2 to make first dy value on onScroll function greater than rv top padding
        recyclerView?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
                shopProductAdapter.shopProductEtalaseTitlePosition,
                stickySingleHeaderView.containerHeight
        )
        updateInitialProductListSortId(sortId)
        loadNewProductData()
    }

    fun setInitialProductListData(initialProductListData: Pair<Boolean, List<ShopProductViewModel>>) {
        this.initialProductListData = initialProductListData
    }

    private fun updateInitialProductListSortId(sortId: String){
        (parentFragment as? ShopPageFragment)?.updateSortId(sortId)
    }

}