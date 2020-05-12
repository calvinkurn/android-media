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
import androidx.core.view.ViewCompat
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
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.ToasterError
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
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopPageConstant.GO_TO_MEMBERSHIP_DETAIL
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.widget.MembershipBottomSheetSuccess
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ShopProductAddViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductEtalaseListViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductsEmptyViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListViewModel
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
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
        ShopProductEtalaseListViewHolder.ShopProductEtalaseChipListViewHolderListener,
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
        private const val GRID_SPAN_COUNT = 2
        private const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        const val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        const val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        const val SAVED_SHOP_ID = "saved_shop_id"
        const val SAVED_SHOP_REF = "saved_shop_ref"
        const val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        const val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        const val SAVED_NEED_TO_RELOAD = "saved_need_to_reload"
        const val ALL_ETALASE_ID = "etalase"
        const val SOLD_ETALASE_ID = "sold"
        const val SHOP_INFO_CACHE_MANAGER_ID = "SHOP_INFO_CACHE_MANAGER_ID"

        const val BUNDLE_IS_NEED_TO_GO_TO_ADD_SHOWCASE = "isNeedToGoToAddShowcase"
        const val BUNDLE_IS_NEED_TO_GO_TO_ADD_SHOWCASE_VALUE = true
        const val BUNDLE_SELECTED_ETALASE_ID = "selectedEtalaseId"
        const val BUNDLE_IS_SHOW_DEFAULT = "isShowDefault"
        const val BUNDLE_IS_SHOW_ZERO_PRODUCT = "isShowZeroProduct"
        const val BUNDLE_SHOP_ID = "shopId"
        const val BUNDLE = "bundle"

        @JvmStatic
        fun createInstance(shopAttribution: String?, shopRef: String): ShopPageProductListFragment {
            val fragment = ShopPageProductListFragment()
            val bundle = Bundle()
            bundle.putString(SHOP_ATTRIBUTION, shopAttribution)
            fragment.arguments = bundle
            fragment.shopRef = shopRef
            return fragment
        }
    }

    override val isOwner: Boolean
        get() = if (shopInfo != null && ::viewModel.isInitialized) {
            shopId?.let { viewModel.isMyShop(it) } ?: false
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
    private var isPaddingSet = false
    private var attribution: String = ""
    private var isLoadingNewProductData = false
    private val sortName = Integer.toString(Integer.MIN_VALUE)
    private var urlNeedTobBeProceed: String? = null
    private var shopInfo: ShopInfo? = null
    private var shopId: String? = null
    private var shopRef: String = ""
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var selectedEtalaseId = ""
    private var selectedEtalaseName = ""
    private var recyclerViewTopPadding = 0
    private var threeDotsClickShopProductViewModel: ShopProductViewModel? = null
    private var threeDotsClickShopTrackingType = -1
    private val customDimensionShopPage: CustomDimensionShopPage
        get() {
            return CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
        }

    override fun chooseProductClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
        }
    }

    override fun onEtalaseChipClicked(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel) {
        if (shopProductAdapter.isLoading) {
            return
        }
        shopProductAdapter.changeSelectedEtalaseId(shopProductEtalaseChipItemViewModel)
        selectedEtalaseId = shopProductEtalaseChipItemViewModel.etalaseId
        selectedEtalaseName = shopProductEtalaseChipItemViewModel.etalaseName
        shopInfo?.let {
            shopId = it.shopCore.shopID
            shopPageTracking?.clickEtalaseChip(
                    isOwner,
                    selectedEtalaseName,
                    CustomDimensionShopPage.create(
                            shopId,
                            it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1
                    ))
        }
        if (recyclerView?.hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH) != true) {
            recyclerView?.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        }
        //multiply with 2 to make first dy value on onScroll function greater than rv top padding
        recyclerView?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        shopProductAdapter.refreshSticky()
        gridLayoutManager.scrollToPositionWithOffset(
                shopProductAdapter.shopProductEtalaseTitlePosition,
                stickySingleHeaderView.containerHeight
        )
        loadNewProductData()
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    override fun onSuccessAddWishlist(productId: String) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), message)
        }
    }

    private fun loadNewProductData() {
        shopId?.let {
            isLoadingNewProductData = true
            shopProductAdapter.clearAllNonDataElement()
            shopProductAdapter.clearProductList()
            showLoading()
            viewModel.getNewProductListData(
                    it,
                    selectedEtalaseId
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
        recyclerViewTopPadding = recyclerView?.paddingTop ?: 0
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView?.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopProductAdapter.showLoading()
                loadData(page)
            }
        }
    }

    override fun onAddEtalaseChipClicked() {
        shopPageTracking?.clickAddEtalase(customDimensionShopPage)
        redirectToAddEtalasePage()
    }

    private fun redirectToAddEtalasePage() {
        context?.let {
            val bundle = Bundle()
            bundle.putBoolean(BUNDLE_IS_NEED_TO_GO_TO_ADD_SHOWCASE, BUNDLE_IS_NEED_TO_GO_TO_ADD_SHOWCASE_VALUE)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra("bundle", bundle)
            startActivityForResult(intent, REQUEST_CODE_ADD_ETALASE)
        }
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        if (context == null) {
            return
        }
        shopPageTracking!!.clickUseMerchantVoucher(isOwner, merchantVoucherViewModel, shopId, position)
        showSnackBarClose(getString(R.string.title_voucher_code_copied))
    }

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        shopPageTracking?.clickDetailMerchantVoucher(isOwner, merchantVoucherViewModel.voucherId.toString())
        context?.let {
            val intent = MerchantVoucherDetailActivity.createIntent(it, merchantVoucherViewModel.voucherId,
                    merchantVoucherViewModel, shopInfo?.shopCore?.shopID ?: "")
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
        if (shopInfo != null) {
            when (shopTrackType) {
                ShopTrackProductTypeDef.FEATURED -> shopInfo?.let {
                    shopPageTracking?.clickProduct(
                            isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else FEATURED_PRODUCT,
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1,
                            shopId
                    )
                }
                ShopTrackProductTypeDef.PRODUCT -> shopInfo?.let {
                    shopPageTracking?.clickProduct(isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else selectedEtalaseName,
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                            shopId
                    )
                }
                ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopInfo?.let {
                    shopPageTracking?.clickProduct(isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1,
                            shopId
                    )
                }
            }
        }
        goToPDP(
                shopProductViewModel.id ?: "",
                attribution,
                shopPageTracking?.getListNameOfProduct(PRODUCT, shopProductAdapter.shopProductEtalaseListViewModel?.selectedEtalaseName)
                        ?: ""
        )
    }

    override fun onProductImpression(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int) {
        if (shopInfo != null) {
            when (shopTrackType) {
                ShopTrackProductTypeDef.FEATURED -> shopInfo?.let {
                    shopPageTracking?.impressionProductList(
                            isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else FEATURED_PRODUCT,
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1,
                            shopId
                    )
                }
                ShopTrackProductTypeDef.PRODUCT -> shopInfo?.let {
                    shopPageTracking?.impressionProductList(isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else selectedEtalaseName,
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1 - shopProductAdapter.shopProductFirstViewModelPosition,
                            shopId
                    )
                }
                ShopTrackProductTypeDef.ETALASE_HIGHLIGHT -> shopInfo?.let {
                    shopPageTracking?.impressionProductList(isOwner,
                            isLogin,
                            selectedEtalaseName,
                            if (isOwner) "" else shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                            CustomDimensionShopPageAttribution.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id,
                                    attribution,
                                    shopRef
                            ),
                            shopProductViewModel,
                            productPosition + 1,
                            shopId
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && shopInfo != null
                    && data != null) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                val etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                val etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                val etalaseBadge = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE)
                shopPageTracking?.clickMoreMenuChip(
                        isOwner,
                        etalaseName,
                        customDimensionShopPage
                )
                if (shopPageTracking != null && shopInfo != null) {
                    shopPageTracking!!.clickMenuFromMoreMenu(
                            viewModel.isMyShop(shopInfo?.shopCore?.shopID ?: ""),
                            etalaseName,
                            CustomDimensionShopPage.create(shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1))
                }
                // if etalase id is on the list, refresh this page; if etalase id is in other list, go to new page.
                if (shopProductAdapter.isEtalaseInChip(etalaseId)) {
                    shopProductAdapter.changeSelectedEtalaseId(ShopProductEtalaseChipItemViewModel(
                            etalaseId = etalaseId,
                            etalaseName = etalaseName,
                            etalaseBadge = etalaseBadge
                    ))
                    selectedEtalaseId = etalaseId
                    shopProductAdapter.refreshSticky()
                    loadNewProductData()
                } else {
                    if (shopInfo != null) {
                        val intent = ShopProductListActivity.createIntent(activity,
                                shopInfo?.shopCore?.shopID ?: "", "",
                                etalaseId, attribution, sortName, shopRef)
                        startActivity(intent)
                    }
                }
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
            REQUEST_CODE_ADD_ETALASE -> {
                if (resultCode == Activity.RESULT_OK) {
                    loadNewProductData()
                }
            }
            REQUEST_CODE_USER_LOGIN -> {
                (parentFragment as? ShopPageFragment)?.refreshData()
            }
            else -> {
            }
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object: ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadMembership() {
        shopId?.let {
            viewModel.getNewMembershipData(it)
        }
    }

    private fun loadMerchantVoucher() {
        shopId?.let {
            viewModel.getNewMerchantVoucher(it)
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (shopInfo != null) {
            threeDotsClickShopProductViewModel?.let { shopProductViewModel ->
                when (threeDotsClickShopTrackingType) {
                    ShopTrackProductTypeDef.FEATURED -> shopPageTracking?.clickWishlist(
                            !shopProductViewModel.isWishList,
                            isLogin,
                            selectedEtalaseName, FEATURED_PRODUCT,
                            CustomDimensionShopPageProduct.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id, shopRef
                            ))
                    ShopTrackProductTypeDef.PRODUCT -> shopPageTracking?.clickWishlist(
                            !shopProductViewModel.isWishList,
                            isLogin,
                            selectedEtalaseName, shopProductAdapter.shopProductEtalaseListViewModel?.selectedEtalaseName
                            ?: "",
                            CustomDimensionShopPageProduct.create(
                                    shopInfo?.shopCore?.shopID ?: "",
                                    (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                    (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                    shopProductViewModel.id, shopRef))
                    else -> // highlight
                        shopPageTracking?.clickWishlist(
                                !shopProductViewModel.isWishList,
                                isLogin,
                                selectedEtalaseName,
                                shopProductAdapter.getEtalaseNameHighLight(shopProductViewModel),
                                CustomDimensionShopPageProduct.create(
                                        shopInfo?.shopCore?.shopID ?: "",
                                        (shopInfo?.goldOS?.isOfficial ?: -1) == 1,
                                        (shopInfo?.goldOS?.isGold ?: -1) == 1,
                                        shopProductViewModel.id, shopRef
                                ))
                }
            }
        }

        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        } else {
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
        }
        else {
            onErrorAddWishList(getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist), productCardOptionsModel.productId)
        }
    }

    private fun handleWishlistActionRemoveFromWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishlist(productCardOptionsModel.productId)
        }
        else {
            onErrorRemoveWishlist(getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist), productCardOptionsModel.productId)
        }
    }

    override fun onSeeAllClicked(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel) {
        shopInfo?.let {
            shopPageTracking?.clickHighLightSeeAll(customDimensionShopPage)
            val intent = ShopProductListActivity.createIntent(activity,
                    it.shopCore.shopID, "",
                    shopProductEtalaseChipItemViewModel.etalaseId, attribution, sortName, shopRef)
            startActivity(intent)
        }
    }

    override fun onSeeAllClicked() {
        shopPageTracking?.clickSeeAllMerchantVoucher(isOwner)

        context?.let {
            val intent = MerchantVoucherListActivity.createIntent(it, shopInfo?.shopCore?.shopID
                    ?: "",
                    shopInfo?.shopCore?.name ?: "")
            startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER)
        }
    }

    override fun onVoucherItemImpressed(merchantVoucherViewModel: MerchantVoucherViewModel, voucherPosition: Int) {}

    override fun onEmptyContentItemTextClicked() {
    }

    override fun onEmptyButtonClicked() {
    }

    override fun onEtalaseMoreListClicked() {
        if (shopInfo != null) {
            shopPageTracking?.clickMoreMenuChip(
                    isOwner,
                    selectedEtalaseName,
                    customDimensionShopPage)
            redirectToEtalasePicker()
        }
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
        DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun loadInitialData() {
        shopId?.let {
            isLoadingNewProductData = true
            shopProductAdapter.clearAllElements()
            showLoading()
            viewModel.getEtalaseData(it)
        }
    }

    private fun promoClicked(url: String?) {
        activity?.let {
            val urlProceed = ShopProductOfficialStoreUtils.proceedUrl(it, url, shopInfo?.shopCore?.shopID
                    ?: "",
                    viewModel.isLogin,
                    viewModel.userDeviceId,
                    viewModel.userId)
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
            bundle.putString(BUNDLE_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(BUNDLE_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(BUNDLE_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(BUNDLE_SHOP_ID, shopInfo!!.shopCore.shopID)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(BUNDLE, bundle)
            startActivity(intent)
        }
    }

    private fun showSnackBarClose(stringToShow: String) {
        activity?.let {
            val snackbar = Snackbar.make(it.findViewById(android.R.id.content), stringToShow,
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(R.string.close)) { snackbar.dismiss() }
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    private fun onErrorAddToWishList(e: Throwable) {
        context?.let{
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
        shopId?.let {
            viewModel.getNextProductListData(
                    it,
                    selectedEtalaseId,
                    page
            )
        }
    }

    override fun onAddProductClicked() {
        context?.let {
            shopPageTracking?.clickAddProduct(customDimensionShopPage)
            RouteManager.route(it, ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val title: String
        val description: String
        return if (shopInfo != null && isOwner) {
            if (selectedEtalaseId == ALL_ETALASE_ID) {
                if (shopInfo != null) {
                    shopPageTracking?.impressionZeroProduct(CustomDimensionShopPage.create(shopInfo?.shopCore?.shopID
                            ?: "",
                            (shopInfo?.goldOS?.isOfficial ?: -1) == 1, (shopInfo?.goldOS?.isGold
                            ?: -1) == 1))
                }
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
        super.onCreate(savedInstanceState)
        context?.let { shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it)) }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListViewModel::class.java)
        attribution = arguments?.getString(SHOP_ATTRIBUTION, "") ?: ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
        shopInfo?.let {
            outState.putString(
                    SHOP_INFO_CACHE_MANAGER_ID,
                    saveShopInfoModelToCacheManager(it)
            )
        }
    }

    private fun saveShopInfoModelToCacheManager(shopInfo: ShopInfo): String? {
        return context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true)
            cacheManager.put(ShopInfo.TAG, shopInfo)
            cacheManager.id
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_shop_page_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            selectedEtalaseId = it.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = it.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            shopId = it.getString(SAVED_SHOP_ID)
            shopRef = it.getString(SAVED_SHOP_REF).orEmpty()
            val shopInfoCacheManagerId = it.getString(SHOP_INFO_CACHE_MANAGER_ID, "")
            isGoldMerchant = it.getBoolean(SAVED_SHOP_IS_GOLD_MERCHANT)
            isOfficialStore = it.getBoolean(SAVED_SHOP_IS_OFFICIAL)
            shopInfo = context?.run {
                SaveInstanceCacheManager(this, shopInfoCacheManagerId).run {
                    get(ShopInfo.TAG, ShopInfo::class.java)
                }
            }
        }
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        if (shopInfo != null) {
            loadInitialData()
        }
        observeViewModelLiveData()
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
        super.onDestroy()
    }

    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }
    private val gridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return gridLayoutManager
    }

    private fun observeViewModelLiveData() {
        viewModel.etalaseListData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetEtalaseListData(it.data)
                }
                is Fail -> {
                    showErrorToasterWithRetry(it.throwable)
                }
            }
        })

        viewModel.membershipData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMembershipData(it.data)
                }
            }
        })

        viewModel.merchantVoucherData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMerchantVoucherData(it.data)
                }
            }
        })

        viewModel.shopProductFeaturedData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductFeaturedData(it.data)
                }
            }
        })

        viewModel.shopProductEtalaseHighlightData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductEtalaseHighlightData(it.data)
                }
            }
        })

        viewModel.shopProductEtalaseTitleData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductEtalaseTitleData(it.data)
                }
            }
        })

        viewModel.productListData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetProductListData(it.data.first, it.data.second)
                }
                is Fail -> {
                    showErrorToasterWithRetry(it.throwable)
                }
            }
        })

        viewModel.claimMembershipResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessClaimBenefit(it.data)
                is Fail -> onErrorGetMembershipInfo(it.throwable)
            }
        })

        viewModel.newMembershipData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetMembershipData(it.data)
                    shopProductAdapter.refreshMembershipData()
                }
                is Fail -> onErrorGetMembershipInfo(it.throwable)
            }
        })

        viewModel.newMerchantVoucherData.observe(this, Observer {
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
            if (isOwner && selectedEtalaseId == ALL_ETALASE_ID) {
                shopProductAdapter.addSellerAddProductDataModel()
            }
            shopProductAdapter.addEmptyDataModel(emptyDataViewModel)
        } else {
            if (isLoadingNewProductData) {
                shopProductAdapter.clearAllNonDataElement()
                shopProductAdapter.clearProductList()
                if (isOwner && selectedEtalaseId == ALL_ETALASE_ID) {
                    shopProductAdapter.addSellerAddProductDataModel()
                }
                endlessRecyclerViewScrollListener.resetState()
            }
            isLoadingNewProductData = false
            shopProductAdapter.setProductListDataModel(productList)
            updateScrollListenerState(hasNextPage)
        }
        stopPerformanceMonitoring()
    }

    private fun onSuccessGetShopProductEtalaseTitleData(data: ShopProductEtalaseTitleViewModel) {
        shopProductAdapter.setShopProductEtalaseTitleData(data)
    }

    private fun onSuccessGetShopProductEtalaseHighlightData(data: ShopProductEtalaseHighlightViewModel) {
        shopInfo?.let {
            if (data.etalaseHighlightCarouselViewModelList.isNotEmpty()) {
                shopProductAdapter.setShopProductEtalaseHighlightDataModel(data)
            }
        }
    }

    private fun onSuccessGetShopProductFeaturedData(data: ShopProductFeaturedViewModel) {
        val listFeaturedProduct = data.shopProductFeaturedViewModelList
        if (listFeaturedProduct.isNotEmpty() && shopInfo != null) {
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

    private fun onSuccessGetEtalaseListData(data: List<ShopProductEtalaseChipItemViewModel>) {
        val shopProductEtalaseListViewModel = createShopProductEtalaseListViewModel(data)
        shopProductAdapter.setEtalaseDataModel(shopProductEtalaseListViewModel)
        selectedEtalaseId = shopProductEtalaseListViewModel.selectedEtalaseId
        selectedEtalaseName = shopProductEtalaseListViewModel.selectedEtalaseName
        shopId?.let { shopId ->
            if (viewModel.isMyShop(shopId)) {
                viewModel.getSellerShopPageProductTabData(shopId, shopProductEtalaseListViewModel)
            } else {
                viewModel.getBuyerShopPageProductTabData(shopId, shopProductEtalaseListViewModel, isShowNewShopHomeTab())
            }
        }
    }

    private fun isShowNewShopHomeTab(): Boolean {
        return shopInfo?.shopHomeType?.let {
            it == ShopHomeType.NATIVE
        } ?: false
    }

    private fun createShopProductEtalaseListViewModel(
            data: List<ShopProductEtalaseChipItemViewModel>
    ): ShopProductEtalaseListViewModel {
        val currentEtalaseId: String
        val currentEtalaseName: String
        val currentEtalaseBadge: String
        val selectedEtalaseChip = data.firstOrNull { it.etalaseId == selectedEtalaseId }
        if (selectedEtalaseChip != null) {
            currentEtalaseId = selectedEtalaseChip.etalaseId
            currentEtalaseName = selectedEtalaseChip.etalaseName
            currentEtalaseBadge = selectedEtalaseChip.etalaseBadge
        } else {
            currentEtalaseId = data[0].etalaseId
            currentEtalaseName = data[0].etalaseName
            currentEtalaseBadge = data[0].etalaseBadge
        }
        val listShopEtalaseDataModel = mutableListOf<BaseShopProductEtalaseViewModel>()
        if (data.size > ETALASE_TO_SHOW) {
            listShopEtalaseDataModel.addAll(data.subList(0, ETALASE_TO_SHOW))
        } else {
            listShopEtalaseDataModel.addAll(data)
        }
        if (isOwner) {
            listShopEtalaseDataModel.add(0, ShopProductAddEtalaseChipViewModel())
        }
        return ShopProductEtalaseListViewModel(
                listShopEtalaseDataModel,
                currentEtalaseId,
                currentEtalaseName,
                currentEtalaseBadge
        )
    }

    private fun onSuccessClaimBenefit(data: MembershipClaimBenefitResponse) {
        if (data.membershipClaimBenefitResponse.title == "") {
            if (data.membershipClaimBenefitResponse.resultStatus.message.isNotEmpty()) {
                showToasterError(data.membershipClaimBenefitResponse.resultStatus.message.firstOrNull()
                        ?: "")
            } else {
                showToasterError(getString(R.string.default_request_error_unknown))
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
            ToasterError.showClose(it, message)
        }
    }

    private fun onErrorGetMembershipInfo(t: Throwable) {
        shopProductAdapter.clearMembershipData()
        activity?.let {
            ToasterError.showClose(it, ErrorHandler.getErrorMessage(context, t))
        }
    }

    private fun stopPerformanceMonitoring(){
        (activity as? ShopPageActivity)?.stopShopProductTabPerformanceMonitoring()
    }

    fun clearCache() {
        viewModel.clearCache()
    }

    fun setShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        this.shopId = shopInfo.shopCore.shopID
    }

    fun getSelectedEtalaseId(): String {
        return shopProductAdapter.shopProductEtalaseListViewModel?.let {
            it.selectedEtalaseId
        } ?: ""
    }
}