package com.tokopedia.cartrevamp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.FragmentCartRevampBinding
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.CartFragment
import com.tokopedia.cartcommon.data.response.common.Button
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartrevamp.data.model.response.promo.LastApplyPromo
import com.tokopedia.cartrevamp.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.LocalizationChooseAddress
import com.tokopedia.cartrevamp.view.adapter.cart.CartAdapter
import com.tokopedia.cartrevamp.view.adapter.cart.CartItemAdapter
import com.tokopedia.cartrevamp.view.bottomsheet.showGlobalErrorBottomsheet
import com.tokopedia.cartrevamp.view.bottomsheet.showSummaryTransactionBottomsheet
import com.tokopedia.cartrevamp.view.compoundview.CartToolbarListener
import com.tokopedia.cartrevamp.view.decorator.CartItemDecoration
import com.tokopedia.cartrevamp.view.di.DaggerCartRevampComponent
import com.tokopedia.cartrevamp.view.mapper.CartUiModelMapper
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.cartrevamp.view.mapper.RecentViewMapper
import com.tokopedia.cartrevamp.view.mapper.WishlistMapper
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectAllHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartState
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.cartrevamp.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.ArrayList
import javax.inject.Inject

@Keep
class CartRevampFragment :
    BaseCheckoutFragment(),
    ActionListener,
    CartToolbarListener,
    CartItemAdapter.ActionListener,
    RefreshHandler.OnRefreshHandlerListener,
    SellerCashbackListener {

    private var binding by autoClearedNullable<FragmentCartRevampBinding>()

    lateinit var cartAdapter: CartAdapter
    private var refreshHandler: RefreshHandler? = null
    private var progressDialog: AlertDialog? = null

    @Inject
    lateinit var cartItemDecoration: CartItemDecoration

    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var wishlistMapper: WishlistMapper

    @Inject
    lateinit var recentViewMapper: RecentViewMapper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }

    private var cartPerformanceMonitoring: PerformanceMonitoring? = null
    private var isTraceCartStopped: Boolean = false
    private var cartAllPerformanceMonitoring: PerformanceMonitoring? = null
    private var isTraceCartAllStopped: Boolean = false

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var hasLoadRecommendation: Boolean = false

    private var hasTriedToLoadWishList: Boolean = false
    private var hasTriedToLoadRecentViewList: Boolean = false
    private var shouldReloadRecentViewList: Boolean = false
    private var hasTriedToLoadRecommendation: Boolean = false
    private val isToolbarWithBackButton = true
    private var delayShowPromoButtonJob: Job? = null
    private var TRANSLATION_LENGTH = 0f
    private var isKeyboardOpened = false
    private var initialPromoButtonPosition = 0f
    private var unavailableItemAccordionCollapseState = true
    private var hasCalledOnSaveInstanceState = false
    private var isCheckUncheckDirectAction = true
    private var isNavToolbar = false
    private var plusCoachMark: CoachMark2? = null

    companion object {
        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

        private const val SPAN_SIZE_ZERO = 0
        private const val SPAN_SIZE_ONE = 1
        private const val SPAN_SIZE_TWO = 2

        const val GET_CART_STATE_AFTER_CHOOSE_ADDRESS = 1

        const val HAS_ELEVATION = 9
        const val NO_ELEVATION = 0
        const val CART_TRACE = "mp_cart"
        const val CART_ALL_TRACE = "mp_cart_all"
        const val CART_PAGE = "cart"
        const val NAVIGATION_PDP = 123
        const val NAVIGATION_SHOP_PAGE = 234
        const val NAVIGATION_WISHLIST = 345
        const val NAVIGATION_PROMO = 456
        const val NAVIGATION_SHIPMENT = 567
        const val NAVIGATION_TOKONOW_HOME_PAGE = 678
        const val NAVIGATION_EDIT_BUNDLE = 789
        const val NAVIGATION_VERIFICATION = 890
        const val NAVIGATION_APPLINK = 809
        const val NAVIGATION_ADDON = 808
        const val WISHLIST_SOURCE_AVAILABLE_ITEM = "WISHLIST_SOURCE_AVAILABLE_ITEM"
        const val WISHLIST_SOURCE_UNAVAILABLE_ITEM = "WISHLIST_SOURCE_UNAVAILABLE_ITEM"
        const val WORDING_GO_TO_HOMEPAGE = "Kembali ke Homepage"
        const val HEIGHT_DIFF_CONSTRAINT = 100
        const val DELAY_SHOW_PROMO_BUTTON_AFTER_SCROLL = 750L
        const val PROMO_ANIMATION_DURATION = 500L
        const val PROMO_POSITION_BUFFER = 10
        const val DELAY_CHECK_BOX_GLOBAL = 500L
        const val ANIMATED_IMAGE_ALPHA = 0.5f
        const val ANIMATED_IMAGE_FILLED = 1.0f
        const val ANIMATED_SCALE_HALF = 0.5f
        const val ANIMATED_SCALE_FULL = 1.0f
        const val IMAGE_ANIMATION_DURATION = 1250L
        const val COORDINATE_HEIGHT_DIVISOR = 3
        const val KEY_OLD_BUNDLE_ID = "old_bundle_id"
        const val KEY_NEW_BUNLDE_ID = "new_bundle_id"
        const val KEY_IS_CHANGE_VARIANT = "is_variant_changed"

        @JvmStatic
        fun newInstance(bundle: Bundle?, args: String): CartRevampFragment {
            var tmpBundle = bundle
            if (tmpBundle == null) {
                tmpBundle = Bundle()
            }
            tmpBundle.putString(CartRevampFragment::class.java.simpleName, args)
            val fragment = CartRevampFragment()
            fragment.arguments = tmpBundle
            return fragment
        }
    }

    override fun initInjector() {
        activity?.let {
            val baseMainApplication = it.application as BaseMainApplication
            DaggerCartRevampComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .build().inject(this)
        }
        cartAdapter = CartAdapter(this, this, this, userSession)
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_cart_revamp
    }

    override fun getOptionsMenuEnable(): Boolean {
        return true
    }

    override fun initView(view: View) {
        initToolbar()

        activity?.let {
            binding?.swipeRefreshLayout?.let { swipeRefreshLayout ->
                refreshHandler = RefreshHandler(it, swipeRefreshLayout, this)
            }
            progressDialog = AlertDialog.Builder(it)
                .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
                .setCancelable(false).create()
        }

        initViewListener()
        initRecyclerView()
        initTopLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            // TODO: check if  savedInstanceState is not nullable
            if (viewModel.cartModel.wishlists == null && viewModel.cartModel.recentViewList == null) {
                EmbraceMonitoring.startMoments(EmbraceKey.KEY_MP_CART)
                cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE)
                cartAllPerformanceMonitoring =
                    PerformanceMonitoring.start(CART_ALL_TRACE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartRevampBinding.inflate(inflater, container, false)
        val view = binding?.root
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = view.rootView?.height?.minus(view.height) ?: 0
            val displayMetrics = DisplayMetrics()
            val windowManager =
                context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            windowManager?.let {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
                if (heightDiffInDp > HEIGHT_DIFF_CONSTRAINT) {
                    if (!isKeyboardOpened) {
                        binding?.bottomLayout?.gone()
                        binding?.llPromoCheckout?.gone()
                    }
                    isKeyboardOpened = true
                } else if (isKeyboardOpened) {
                    binding?.bottomLayout?.show()
                    binding?.llPromoCheckout?.show()
                    isKeyboardOpened = false
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityBackgroundColor()
        activity?.let {
            setHasOptionsMenu(true)
            it.title = it.getString(R.string.title_activity_cart)

            val productId = getAtcProductId()
            if (isAtcExternalFlow()) {
                if (productId == CartActivity.INVALID_PRODUCT_ID) {
                    showToastMessageRed(MessageErrorException(AtcConstant.ATC_ERROR_GLOBAL))
                    refreshCartWithSwipeToRefresh()
                } else {
                    addToCartExternal(productId)
                }
            } else {
                refreshCartWithSwipeToRefresh()
            }
        }
        initViewModel()
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun onClickShopNow() {
        TODO("Not yet implemented")
    }

    override fun getDefaultCartErrorMessage(): String {
        TODO("Not yet implemented")
    }

    override fun onCartGroupNameClicked(appLink: String) {
        TODO("Not yet implemented")
    }

    override fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onShopItemCheckChanged(index: Int, checked: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onCartShopGroupTickerClicked(cartGroupHolderData: CartGroupHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCartShopGroupTickerRefreshClicked(
        index: Int,
        cartShopBottomHolderData: CartShopBottomHolderData
    ) {
        TODO("Not yet implemented")
    }

    override fun onViewCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        TODO("Not yet implemented")
    }

    override fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCartDataEnableToCheckout() {
        // TODO: remove this from interface listener
        if (isAdded) {
            binding?.vDisabledGoToCourierPageButton?.gone()
            binding?.goToCourierPageButton?.isEnabled = true
            binding?.goToCourierPageButton?.setOnClickListener {
                EmbraceMonitoring.startMoments(EmbraceKey.KEY_ACT_BUY)
                checkGoToShipment("")
            }
        }
    }

    override fun onCartDataDisableToCheckout() {
        // TODO: remove this from interface listener
        if (isAdded) {
            binding?.goToCourierPageButton?.isEnabled = false
            binding?.vDisabledGoToCourierPageButton?.show()
            binding?.vDisabledGoToCourierPageButton?.setOnClickListener {
                if (viewModel.getAllAvailableCartItemData().isNotEmpty()) {
                    showToastMessageGreen(getString(R.string.message_no_cart_item_selected))
                }
            }
        }
    }

    override fun onShowAllItem(appLink: String) {
        TODO("Not yet implemented")
    }

    override fun onAddLastSeenToWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onAddWishlistToWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onAddRecommendationToWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveDisabledItemFromWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveLastSeenFromWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveWishlistFromWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRemoveRecommendationFromWishlist(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onWishlistProductClicked(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onWishlistImpression() {
        viewModel.cartModel.wishlists?.let {
            cartPageAnalytics.enhancedEcommerceProductViewWishList(
                viewModel.generateWishlistDataImpressionAnalytics(
                    it,
                    FLAG_IS_CART_EMPTY
                )
            )
        }
    }

    override fun onRecentViewProductClicked(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRecentViewImpression() {
        viewModel.cartModel.recentViewList?.let {
            cartPageAnalytics.enhancedEcommerceProductViewLastSeen(
                viewModel.generateRecentViewDataImpressionAnalytics(
                    it,
                    FLAG_IS_CART_EMPTY
                )
            )
        }
    }

    override fun onRecommendationProductClicked(recommendationItem: RecommendationItem) {
        TODO("Not yet implemented")
    }

    override fun onRecommendationProductImpression(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val url = recommendationItem.trackerImageUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        when {
            topAds -> {
                activity?.let {
                    TopAdsUrlHitter(CartFragment::class.qualifiedName).hitImpressionUrl(
                        it,
                        url,
                        productId,
                        productName,
                        imageUrl
                    )
                }
            }
        }
    }

    override fun onRecommendationImpression(recommendationItem: CartRecommendationItemHolderData) {
        val recommendationList = cartAdapter.getRecommendationItem()
        if (recommendationList.isEmpty()) return
        recommendationList.let {
            val currentIndex = it.indexOf(recommendationItem)
            if (it.size >= 2) {
                if (currentIndex == it.size - 1) {
                    if (currentIndex % 2 == 0) {
                        // edge case : recommendation list size is odd number
                        // send last single item impression
                        sendImpressionOneRecommendationItem(it, currentIndex)
                    } else {
                        // edge case : recommendation list contains exactly 2 items
                        // send 2 items impression
                        sendImpressionTwoRecommendationItems(it, currentIndex)
                    }
                } else if (currentIndex > 0 && currentIndex % 2 == 1) {
                    // send analytics on impression recommendation item odd position
                    // send analytics every 2 item impression
                    sendImpressionTwoRecommendationItems(it, currentIndex)
                }
            } else {
                // edge case : recommendation list contains exactly 1 item
                // edge case : send single item impression if recommendation list only contain 1 item
                sendImpressionOneRecommendationItem(it, currentIndex)
            }
        }
    }

    override fun onButtonAddToCartClicked(productModel: Any) {
        TODO("Not yet implemented")
    }

    override fun onDeleteAllDisabledProduct() {
        TODO("Not yet implemented")
    }

    override fun onSeeErrorProductsClicked() {
        TODO("Not yet implemented")
    }

    override fun onCollapseAvailableItem(index: Int) {
        TODO("Not yet implemented")
    }

    override fun onExpandAvailableItem(index: Int) {
        TODO("Not yet implemented")
    }

    override fun onCollapsedProductClicked(index: Int, cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun scrollToClickedExpandedProduct(index: Int, offset: Int) {
        TODO("Not yet implemented")
    }

    override fun onToggleUnavailableItemAccordion(
        data: DisabledAccordionHolderData,
        buttonWording: String
    ) {
        TODO("Not yet implemented")
    }

    override fun onDisabledCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onRecentViewProductImpression(element: CartRecentViewItemHolderData) {
        viewModel.cartModel.recentViewList?.let {
            if (element.isTopAds) {
                TopAdsUrlHitter(context?.applicationContext).hitImpressionUrl(
                    this::class.java.simpleName,
                    element.trackerImageUrl,
                    element.id,
                    element.name,
                    element.imageUrl
                )
            }
        }
    }

    override fun onGlobalCheckboxCheckedChange(
        isChecked: Boolean,
        isCheckUncheckDirectAction: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onGlobalDeleteClicked() {
        TODO("Not yet implemented")
    }

    override fun onNeedToGoneLocalizingAddressWidget() {
        TODO("Not yet implemented")
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        TODO("Not yet implemented")
    }

    override fun onClickAddOnCart(productId: String, addOnId: String) {
        TODO("Not yet implemented")
    }

    override fun onClickEpharmacyInfoCart(
        enablerLabel: String,
        shopId: String,
        productUiModelList: MutableList<CartItemHolderData>
    ) {
        TODO("Not yet implemented")
    }

    override fun addOnImpression(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onViewFreeShippingPlusBadge() {
        TODO("Not yet implemented")
    }

    override fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData) {
        TODO("Not yet implemented")
    }

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCartItemQuantityPlusButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onCartItemQuantityMinusButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        TODO("Not yet implemented")
    }

    override fun onCartItemLabelInputRemarkClicked() {
        TODO("Not yet implemented")
    }

    override fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onWishlistCheckChanged(
        productId: String,
        cartId: String,
        imageView: ImageView,
        isError: Boolean,
        errorType: String
    ) {
        TODO("Not yet implemented")
    }

    override fun onNeedToRefreshSingleShop(
        cartItemHolderData: CartItemHolderData,
        itemPosition: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onNeedToRecalculate() {
        TODO("Not yet implemented")
    }

    override fun onCartItemQuantityChanged(
        cartItemHolderData: CartItemHolderData,
        newQuantity: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onCartItemShowRemainingQty(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onCartItemShowInformationLabel(productId: String, informationLabel: String) {
        TODO("Not yet implemented")
    }

    override fun onEditBundleClicked(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action) {
        TODO("Not yet implemented")
    }

    override fun onShowTickerTobacco() {
        TODO("Not yet implemented")
    }

    override fun onSimilarProductUrlClicked(data: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onShowActionSeeOtherProduct(productId: String, errorType: String) {
        TODO("Not yet implemented")
    }

    override fun onFollowShopClicked(shopId: String, errorType: String) {
        TODO("Not yet implemented")
    }

    override fun onVerificationClicked(applink: String) {
        TODO("Not yet implemented")
    }

    override fun onProductAddOnClicked(addOnId: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCashbackUpdated(amount: Int) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        // TODO: always false
//        if (!isNavToolbar) {
//            cartPageAnalytics.eventClickAtcCartClickArrowBack()
//        }

        if (isAtcExternalFlow()) {
            routeToHome()
        }
        activity?.finish()
    }

    override fun onWishlistClicked() {
        TODO("Not yet implemented")
    }

    override fun onRefresh(view: View?) {
        refreshCartWithSwipeToRefresh()
    }

    private fun addEndlessRecyclerViewScrollListener(
        cartRecyclerView: RecyclerView,
        gridLayoutManager: GridLayoutManager
    ) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (hasLoadRecommendation) {
                        loadRecommendation()
                    }
                }
            }
        cartRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun addRecyclerViewScrollListener(cartRecyclerView: RecyclerView) {
        cartRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding?.bottomLayout?.visibility == View.GONE) {
                    binding?.llPromoCheckout?.gone()
                    return
                }

                handlePromoButtonVisibilityOnIdle(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (binding?.bottomLayout?.visibility == View.GONE) {
                    binding?.llPromoCheckout?.gone()
                }

                if (recyclerView.canScrollVertically(-1)) {
                    disableSwipeRefresh()
                    setToolbarShadowVisibility(true)
                } else {
                    enableSwipeRefresh()
                    setToolbarShadowVisibility(false)
                }

                handlePromoButtonVisibilityOnScroll(dy)
                handleStickyCheckboxGlobalVisibility(recyclerView)
            }
        })
    }

    private fun addToCartExternal(productId: Long) {
//        dPresenter.processAddToCartExternal(productId)
    }

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun animatePromoButtonToStartingPosition() {
        binding?.apply {
            val initialPosition =
                bottomLayout.y - llPromoCheckout.height + PROMO_POSITION_BUFFER.dpToPx(
                    resources.displayMetrics
                )
            llPromoCheckout.animate().y(initialPosition).setDuration(0).start()
        }
    }

    private fun checkGoToShipment(message: String?) {
        if (message.isNullOrEmpty()) {
            val redStatePromoTripleData = getRedStatePromo()
            val hasRedStatePromo = redStatePromoTripleData.first
            val redStateGlobalPromo = redStatePromoTripleData.second
            val clearOrders = redStatePromoTripleData.third

            val clearPromo = ClearPromoRequest(
                ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                orderData = ClearPromoOrderData(redStateGlobalPromo, clearOrders)
            )
            if (hasRedStatePromo) {
                viewModel.doClearRedPromosBeforeGoToCheckout(clearPromo)
            } else {
                goToCheckoutPage()
            }
        } else {
            showToastMessageRed(message)
            cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
            cartPageAnalytics.eventViewErrorWhenCheckout(message)
        }
    }

    private fun checkGoToPromo() {
        val redStatePromoTripleData = getRedStatePromo()
        val hasRedStatePromo = redStatePromoTripleData.first
        val redStateGlobalPromo = redStatePromoTripleData.second
        val clearOrders = redStatePromoTripleData.third

        val clearPromo = ClearPromoRequest(
            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
            orderData = ClearPromoOrderData(redStateGlobalPromo, clearOrders)
        )
        if (hasRedStatePromo) {
            viewModel.doClearRedPromosBeforeGoToPromo(clearPromo)
        } else {
            goToPromoPage()
        }
    }

    private fun collapseOrExpandDisabledItem(data: DisabledAccordionHolderData) {
        viewModel.collapseOrExpandDisabledItemAccordion(data)
        if (data.isCollapsed) {
            viewModel.collapseDisabledItems()
        } else {
            viewModel.expandDisabledItems()
        }
    }

    private fun disableSwipeRefresh() {
        refreshHandler?.setPullEnabled(false)
    }

    private fun enableSwipeRefresh() {
        refreshHandler?.setPullEnabled(true)
    }

    private fun getAtcProductId(): Long {
        return arguments?.getLong(CartActivity.EXTRA_PRODUCT_ID) ?: 0L
    }

    private fun getCartId(): String {
        val cartId = arguments?.getString(CartActivity.EXTRA_CART_ID).orEmpty()
        return cartId.ifEmpty { "0" }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun generateParamGetLastApplyPromo(): ValidateUsePromoRequest {
        return when {
            viewModel.cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    viewModel.cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    lastApplyPromo,
                    viewModel.getSelectedCartGroupHolderData(),
                    null
                )
            }

            viewModel.cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    promoUiModel,
                    viewModel.getSelectedCartGroupHolderData(),
                    viewModel.cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    null,
                    viewModel.getSelectedCartGroupHolderData(),
                    null
                )
            }
        }
    }

    private fun generateParamsCouponList(): PromoRequest {
        return when {
            viewModel.cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    viewModel.cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateCouponListRequestParams(
                    lastApplyPromo,
                    viewModel.getAllAvailableShopGroupDataList(),
                    null
                )
            }

            viewModel.cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateCouponListRequestParams(
                    promoUiModel,
                    viewModel.getAllAvailableShopGroupDataList(),
                    viewModel.cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateCouponListRequestParams(
                    null,
                    viewModel.getAllAvailableShopGroupDataList(),
                    null
                )
            }
        }
    }

    private fun getRedStatePromo(): Triple<Boolean, ArrayList<String>, ArrayList<ClearPromoOrder>> {
        var hasRedStatePromo = false
        val redStateGlobalPromo = ArrayList<String>()
        val clearOrders = ArrayList<ClearPromoOrder>()
        val cartListData = viewModel.cartModel.cartListData
        if (viewModel.cartModel.isLastApplyResponseStillValid) {
            val lastApplyPromoData = cartListData?.promo?.lastApplyPromo?.lastApplyPromoData
            lastApplyPromoData?.let {
                if (it.message.state == "red") {
                    it.codes.forEach { code ->
                        if (!redStateGlobalPromo.contains(code)) {
                            redStateGlobalPromo.add(code)
                            hasRedStatePromo = true
                        }
                    }
                }

                it.listVoucherOrders.forEach { voucher ->
                    if (voucher.message.state == "red") {
                        val clearOrder =
                            clearOrders.find { order -> order.uniqueId == voucher.uniqueId }
                        if (clearOrder == null) {
                            val availableGroup =
                                cartListData.availableSection.availableGroupGroups.find { group ->
                                    group.cartString == voucher.cartStringGroup
                                }
                            val availableShop =
                                availableGroup?.groupShopCartData?.find { shopOrder ->
                                    shopOrder.cartStringOrder == voucher.uniqueId
                                }
                            availableShop?.let {
                                clearOrders.add(
                                    ClearPromoOrder(
                                        uniqueId = voucher.uniqueId,
                                        boType = availableGroup.boMetadata.boType,
                                        codes = arrayListOf(voucher.code),
                                        shopId = availableShop.shop.shopId.toLongOrZero(),
                                        warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                        isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                        poDuration = availableShop.cartDetails.getOrNull(0)?.products?.getOrNull(
                                            0
                                        )?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() }
                                            ?: "0",
                                        cartStringGroup = availableGroup.cartString
                                    )
                                )
                                hasRedStatePromo = true
                            }
                        } else if (!clearOrder.codes.contains(voucher.code)) {
                            clearOrder.codes.add(voucher.code)
                            hasRedStatePromo = true
                        }
                    }
                }
            }
        } else if (cartListData != null) {
            val lastValidateUseData = viewModel.cartModel.lastValidateUseResponse
            lastValidateUseData?.promoUiModel?.let {
                if (it.messageUiModel.state == "red") {
                    it.codes.forEach { code ->
                        if (!redStateGlobalPromo.contains(code)) {
                            redStateGlobalPromo.add(code)
                            hasRedStatePromo = true
                        }
                    }
                }

                it.voucherOrderUiModels.forEach { voucher ->
                    if (voucher.messageUiModel.state == "red" && voucher.code.isNotBlank()) {
                        val clearOrder =
                            clearOrders.find { order -> order.uniqueId == voucher.uniqueId }
                        if (clearOrder == null) {
                            val availableGroup =
                                cartListData.availableSection.availableGroupGroups.find { group ->
                                    group.cartString == voucher.cartStringGroup
                                }
                            val availableShop =
                                availableGroup?.groupShopCartData?.find { shopOrder ->
                                    shopOrder.cartStringOrder == voucher.uniqueId
                                }
                            availableShop?.let {
                                clearOrders.add(
                                    ClearPromoOrder(
                                        uniqueId = voucher.uniqueId,
                                        boType = availableGroup.boMetadata.boType,
                                        codes = arrayListOf(voucher.code),
                                        shopId = availableShop.shop.shopId.toLongOrZero(),
                                        warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                        isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                        poDuration = availableShop.cartDetails.getOrNull(0)?.products?.getOrNull(
                                            0
                                        )?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() }
                                            ?: "0",
                                        cartStringGroup = availableGroup.cartString
                                    )
                                )
                                hasRedStatePromo = true
                            }
                        } else if (!clearOrder.codes.contains(voucher.code)) {
                            clearOrder.codes.add(voucher.code)
                            hasRedStatePromo = true
                        }
                    }
                }
            }
        }

        if (cartListData != null) {
            val lastUpdateCartAndValidateUseResponse =
                viewModel.cartModel.lastUpdateCartAndGetLastApplyResponse
            lastUpdateCartAndValidateUseResponse?.promoUiModel?.let {
                if (it.messageUiModel.state == "red") {
                    it.codes.forEach { code ->
                        if (!redStateGlobalPromo.contains(code)) {
                            redStateGlobalPromo.add(code)
                            hasRedStatePromo = true
                        }
                    }
                }

                it.voucherOrderUiModels.forEach { voucher ->
                    if (voucher.messageUiModel.state == "red" && voucher.code.isNotBlank()) {
                        val clearOrder =
                            clearOrders.find { order -> order.uniqueId == voucher.uniqueId }
                        if (clearOrder == null) {
                            val availableGroup =
                                cartListData.availableSection.availableGroupGroups.find { group ->
                                    group.cartString == voucher.cartStringGroup
                                }
                            val availableShop =
                                availableGroup?.groupShopCartData?.find { shopOrder ->
                                    shopOrder.cartStringOrder == voucher.uniqueId
                                }
                            availableShop?.let {
                                clearOrders.add(
                                    ClearPromoOrder(
                                        uniqueId = voucher.uniqueId,
                                        boType = availableGroup.boMetadata.boType,
                                        codes = arrayListOf(voucher.code),
                                        shopId = availableShop.shop.shopId.toLongOrZero(),
                                        warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                        isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                        poDuration = availableShop.cartDetails.getOrNull(0)?.products?.getOrNull(
                                            0
                                        )?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() }
                                            ?: "0",
                                        cartStringGroup = availableGroup.cartString
                                    )
                                )
                                hasRedStatePromo = true
                            }
                        } else if (!clearOrder.codes.contains(voucher.code)) {
                            clearOrder.codes.add(voucher.code)
                            hasRedStatePromo = true
                        }
                    }
                }
            }
        }
        return Triple(hasRedStatePromo, redStateGlobalPromo, clearOrders)
    }

    private fun goToCheckoutPage() {
        viewModel.processUpdateCartData(false)
    }

    private fun goToPromoPage() {
        viewModel.doUpdateCartForPromo()
    }

    private fun handleCheckboxGlobalChangeEvent() {
        val isChecked = binding?.topLayout?.checkboxGlobal?.isChecked ?: return
        if (isCheckUncheckDirectAction) {
            viewModel.setAllAvailableItemCheck(isChecked)
            viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
            viewModel.saveCheckboxState(viewModel.getAllAvailableCartItemHolderData())
            setGlobalDeleteVisibility()
            cartPageAnalytics.eventCheckUncheckGlobalCheckbox(isChecked)

//            reloadAppliedPromoFromGlobalCheck()
        }
        isCheckUncheckDirectAction = true
    }

    private fun handlePromoButtonVisibilityOnIdle(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && initialPromoButtonPosition > 0) {
            // Delay after recycler view idle, then show promo button
            delayShowPromoButtonJob?.cancel()
            delayShowPromoButtonJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                delay(DELAY_SHOW_PROMO_BUTTON_AFTER_SCROLL)
                binding?.apply {
                    val initialPosition =
                        bottomLayout.y - llPromoCheckout.height + PROMO_POSITION_BUFFER.dpToPx(
                            resources.displayMetrics
                        )
                    llPromoCheckout.animate().y(initialPosition)
                        .setDuration(PROMO_ANIMATION_DURATION).start()
                }
            }
        }
    }

    private fun handlePromoButtonVisibilityOnScroll(dy: Int) {
        val llPromoCheckout = binding?.llPromoCheckout ?: return
        val valueY = llPromoCheckout.y + dy
        TRANSLATION_LENGTH += dy
        if (dy != 0) {
            if (initialPromoButtonPosition == 0f && TRANSLATION_LENGTH - dy == 0f) {
                // Initial position of View if previous initialization attempt failed
                initialPromoButtonPosition = llPromoCheckout.y
            }

            if (TRANSLATION_LENGTH != 0f) {
                if (dy < 0 && valueY < initialPromoButtonPosition) {
                    // Prevent scroll up move button exceed initial view position
                    animatePromoButtonToStartingPosition()
                } else if (valueY <= llPromoCheckout.height + initialPromoButtonPosition) {
                    // Prevent scroll down move button too far
                    animatePromoButtonToHiddenPosition(valueY)
                }
            } else {
                // Set to initial position if scroll up to top
                animatePromoButtonToStartingPosition()
            }
        }
    }

    private fun handleStickyCheckboxGlobalVisibility(recyclerView: RecyclerView) {
        val topItemPosition =
            (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        if (topItemPosition == RecyclerView.NO_POSITION) return

        val adapterData = viewModel.cartDataList.value
        if (topItemPosition >= adapterData.size) return

        val firstVisibleItemData = adapterData[topItemPosition]
        if (firstVisibleItemData is CartSelectAllHolderData ||
            firstVisibleItemData is TickerAnnouncementHolderData ||
            firstVisibleItemData is CartChooseAddressHolderData ||
            firstVisibleItemData is CartItemTickerErrorHolderData ||
            firstVisibleItemData is CartGroupHolderData ||
            firstVisibleItemData is CartItemHolderData ||
            firstVisibleItemData is CartShopBottomHolderData ||
            firstVisibleItemData is ShipmentSellerCashbackModel
        ) {
            if (viewModel.getAllAvailableCartItemData().isNotEmpty()) {
                if (binding?.topLayout?.root?.visibility == View.GONE) setTopLayoutVisibility(true)
            }
        } else {
            if (binding?.topLayout?.root?.visibility == View.VISIBLE) setTopLayoutVisibility(false)
        }
    }

    private fun isAtcExternalFlow(): Boolean {
        return getAtcProductId() != 0L
    }

    private fun initializeToasterLocation() {
        activity?.let {
            if (it is CartActivity) {
                Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_140)
            } else {
                Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_210)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun initNavigationToolbar() {
        activity?.let {
            val statusBarBackground = binding?.statusBarBg

            statusBarBackground?.hide()

            binding?.navToolbar?.apply {
                viewLifecycleOwner.lifecycle.addObserver(this)
                setOnBackButtonClickListener(
                    disableDefaultGtmTracker = true,
                    backButtonClickListener = ::onBackPressed
                )
                setIcon(
                    IconBuilder(IconBuilderFlag(pageSource = CART_PAGE)).addIcon(
                        iconId = IconList.ID_NAV_ANIMATED_WISHLIST,
                        disableDefaultGtmTracker = true,
                        onClick = ::onNavigationToolbarWishlistClicked
                    ).addIcon(
                        iconId = IconList.ID_NAV_GLOBAL,
                        disableDefaultGtmTracker = true,
                        onClick = ::onNavigationToolbarNavGlobalClicked
                    )
                )

                setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            }
        }
    }

    private fun initRecyclerView() {
        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun supportsPredictiveItemAnimations() = false

            override fun onLayoutChildren(
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding?.rvCart?.apply {
            layoutManager = gridLayoutManager
            adapter = cartAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            addItemDecoration(cartItemDecoration)
            setSpanSize(gridLayoutManager)
            addRecyclerViewScrollListener(this)
            addEndlessRecyclerViewScrollListener(this, gridLayoutManager)
        }
    }

    private fun initViewListener() {
        binding?.apply {
            goToCourierPageButton.setOnClickListener { checkGoToShipment("") }
            imgChevronSummary.setOnClickListener { onClickChevronSummaryTransaction() }
            textTotalPaymentLabel.setOnClickListener { onClickChevronSummaryTransaction() }
            tvTotalPrices.setOnClickListener { onClickChevronSummaryTransaction() }
        }
    }

    private fun initViewModel() {
        observeCartDataList()

        observeCartEvent()

        observeCheckoutButton()

        observeGlobalEvent()

        observeRecentView()

        observeRecommendation()

        observeUpdateCartEvent()

        observeWishlist()
    }

    private fun initToolbar() {
        initNavigationToolbar()
        binding?.toolbarCart?.gone()
        binding?.navToolbar?.show()
        setToolbarShadowVisibility(false)
    }

    private fun initTopLayout() {
        binding?.topLayout?.checkboxGlobal?.let {
//            compositeSubscription.add(
//                rxCompoundButtonCheckDebounce(it, DELAY_CHECK_BOX_GLOBAL).subscribe(object :
//                    Subscriber<Boolean>() {
//                    override fun onNext(isChecked: Boolean) {
//                        handleCheckboxGlobalChangeEvent()
//                    }
//
//                    override fun onCompleted() {
//                        // no-op
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        // no-op
//                    }
//                })
//            )
        }

        binding?.topLayout?.textActionDelete?.setOnClickListener {
            onGlobalDeleteClicked()
        }
    }

    private fun loadRecommendation() {
        viewModel.processGetRecommendationData(
            viewModel.cartModel.recommendationPage,
            viewModel.getAllCartItemProductId()
        )
    }

    private fun navigateToPromoRecommendation() {
        routeToPromoCheckoutMarketplacePage()
    }

    private fun navigateToShipmentPage() {
        FLAG_BEGIN_SHIPMENT_PROCESS = true
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = true
        routeToCheckoutPage()
    }

    private fun notifyBottomCartParent() {
        if (activity is CartNotifyListener) {
            (activity as CartNotifyListener).onNotifyCart()
        }
    }

    private fun observeCartDataList() {
        viewModel.cartDataList.observe(viewLifecycleOwner) { data ->
            cartAdapter.updateList(data)
        }
    }

    private fun observeCartEvent() {
        viewModel.loadCartState.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state) {
                    is CartState.Success -> {
                        renderLoadGetCartDataFinish()
                        renderInitialGetCartListDataSuccess(state.data)
                        stopCartPerformanceTrace(true)
                    }

                    is CartState.Failed -> {
                        renderLoadGetCartDataFinish()
                        renderErrorInitialGetCartListData(state.throwable)
                        stopCartPerformanceTrace(false)
                    }
                }
            }
        }
    }

    private fun observeCheckoutButton() {
        viewModel.cartCheckoutButtonState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartCheckoutButtonState.ENABLE -> onCartDataEnableToCheckout()
                else -> onCartDataDisableToCheckout()
            }
        }
    }

    private fun observeRecentView() {
        viewModel.recentViewState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is LoadRecentReviewState.Success -> {
                    hideItemLoading()
                    if (data.recommendationWidgets.firstOrNull()?.recommendationItemList?.isNotEmpty() == true) {
                        renderRecentView(data.recommendationWidgets[0])
                    }
                    setHasTriedToLoadRecentView()
                    stopAllCartPerformanceTrace()
                }

                is LoadRecentReviewState.Failed -> {
                    setHasTriedToLoadRecentView()
                    stopAllCartPerformanceTrace()
                }

                else -> {}
            }
        }
    }

    private fun observeRecommendation() {
        viewModel.recommendationState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is LoadRecommendationState.Success -> {
                    hideItemLoading()
                    if (data.recommendationWidgets[0].recommendationItemList.isNotEmpty()) {
                        renderRecommendation(data.recommendationWidgets[0])
                    }
                    setHasTriedToLoadRecommendation()
                    stopAllCartPerformanceTrace()
                }

                is LoadRecommendationState.Failed -> {
                    hideItemLoading()
                    setHasTriedToLoadRecommendation()
                    stopAllCartPerformanceTrace()
                }

                else -> {}
            }
        }
    }

    private fun observeWishlist() {
        viewModel.wishlistV2State.observe(viewLifecycleOwner) { data ->
            when (data) {
                is LoadWishlistV2State.Success -> {
                    if (data.wishlists.isNotEmpty()) {
                        renderWishlistV2(data.wishlists, data.forceReload)
                    }
                    setHasTriedToLoadWishList()
                    stopAllCartPerformanceTrace()
                }

                is LoadWishlistV2State.Failed -> {
                    setHasTriedToLoadWishList()
                    stopAllCartPerformanceTrace()
                }

                else -> {}
            }
        }
    }

    private fun observeGlobalEvent() {
        viewModel.globalEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CartGlobalEvent.Normal -> {}
                is CartGlobalEvent.ItemLoading -> {
                    if (event.isLoading) {
                        showItemLoading()
                    } else {
                        hideItemLoading()
                    }
                }

                is CartGlobalEvent.ProgressLoading -> {
                    if (event.isLoading) {
                        showProgressLoading()
                    } else {
                        hideProgressLoading()
                    }
                }

                is CartGlobalEvent.LoadGetCartData -> renderLoadGetCartData()
                is CartGlobalEvent.CartCounterUpdated -> updateCartCounter(event.counter)
                is CartGlobalEvent.SuccessClearRedPromosThenGoToPromo -> {
                    hideProgressLoading()
                    goToPromoPage()
                }

                is CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout -> {
                    hideProgressLoading()
                    goToCheckoutPage()
                }
                is CartGlobalEvent.UpdateAndReloadCartFailed -> {
                    hideProgressLoading()
                    showToastMessageRed(event.throwable)
                }
                is CartGlobalEvent.SubTotalUpdated -> {
                    renderDetailInfoSubTotal(event.qty, event.subtotalPrice, event.noAvailableItems)
                }
            }
        }
    }

    private fun observeUpdateCartEvent() {
        viewModel.updateCartForCheckoutState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is UpdateCartCheckoutState.Success -> {
                    renderToShipmentFormSuccess(
                        data.eeCheckoutData,
                        data.checkoutProductEligibleForCashOnDelivery,
                        data.condition
                    )
                }

                is UpdateCartCheckoutState.ErrorOutOfService -> {
                    renderErrorToShipmentForm(data.outOfService)
                }

                is UpdateCartCheckoutState.UnknownError -> {
                    renderErrorToShipmentForm(data.message, data.ctaText)
                }

                is UpdateCartCheckoutState.Failed -> {
                    hideProgressLoading()
                    renderErrorToShipmentForm(data.throwable)
                }

                else -> {}
            }
        }

        viewModel.updateCartForPromoState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is UpdateCartPromoState.Success -> {
                    hideProgressLoading()
                    navigateToPromoRecommendation()
                }

                is UpdateCartPromoState.Failed -> {
                    hideProgressLoading()
                    showToastMessageRed(data.throwable)
                }

                else -> {}
            }
        }
    }

    private fun onClickChevronSummaryTransaction() {
        cartPageAnalytics.eventClickDetailTagihan(userSession.userId)
        showBottomSheetSummaryTransaction()
    }

    private fun onNavigationToolbarNavGlobalClicked() {
        cartPageAnalytics.eventClickTopNavMenuNavToolbar(userSession.userId)
    }

    private fun onNavigationToolbarWishlistClicked() {
        cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
        routeToWishlist()
    }

    private fun setGlobalDeleteVisibility() {
        // TODO: change cartadapter
        if (viewModel.hasSelectedCartItem()) {
            binding?.topLayout?.textActionDelete?.show()
        } else {
            binding?.topLayout?.textActionDelete?.invisible()
        }
    }

    private fun refreshCartWithSwipeToRefresh() {
        refreshHandler?.isRefreshing = true
        resetRecentViewList()
        if (viewModel.dataHasChanged()) {
            showMainContainer()
            viewModel.processToUpdateAndReloadCartData(getCartId())
        } else {
            viewModel.processInitialGetCartData(
                getCartId(),
                viewModel.cartModel.cartListData == null,
                true
            )
        }
    }

    private fun refreshErrorPage() {
        setTopLayoutVisibility(false)
        binding?.layoutGlobalError?.gone()
        binding?.rlContent?.show()
        refreshHandler?.isRefreshing = true
        viewModel.resetData()
        viewModel.processInitialGetCartData(getCartId(), true, false)
    }

    private fun reloadAppliedPromoFromGlobalCheck() {
//        val params = generateParamGetLastApplyPromo()
//        if (isNeedHitUpdateCartAndValidateUse(params)) {
//            renderPromoCheckoutLoading()
//            dPresenter.doUpdateCartAndGetLastApply(params)
//        } else {
//            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
//        }
    }

    private fun renderAdditionalWidget() {
        validateRenderWishlist()
        validateRenderRecentView()
        loadRecommendation()
    }

    private fun renderCartAvailableItems(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isNotEmpty()) {
            val availableShopList = CartUiModelMapper.mapAvailableGroupUiModel(cartData)
            viewModel.addItems(availableShopList)
        }
    }

    private fun renderCartUnavailableItems(cartData: CartData) {
        if (cartData.unavailableSections.isNotEmpty()) {
            val unavailableDataMapResult =
                CartUiModelMapper.mapUnavailableShopUiModel(activity, cartData)
            val unavailableSectionList = unavailableDataMapResult.first
            val accordionUiModel = unavailableDataMapResult.second
            viewModel.addItems(unavailableSectionList)
            if (accordionUiModel != null) {
                collapseOrExpandDisabledItem(accordionUiModel)
                if (!unavailableItemAccordionCollapseState) {
                    accordionUiModel.isCollapsed = false
                    collapseOrExpandDisabledItem(accordionUiModel)
                }
            }
        }
    }

    private fun renderCartEmpty(cartData: CartData) {
        FLAG_IS_CART_EMPTY = true

        val lastApplyPromoData = cartData.promo.lastApplyPromo.lastApplyPromoData
        if (lastApplyPromoData.additionalInfo.emptyCartInfo.message.isNotEmpty()) {
            renderCartEmptyWithPromo(lastApplyPromoData)
        } else {
            renderCartEmptyDefault()
        }
        enableSwipeRefresh()
        showEmptyCartContainer()
        notifyBottomCartParent()

        setActivityBackgroundColor()
        cartPageAnalytics.eventViewAtcCartImpressionCartEmpty()
    }

    private fun renderCartEmptyDefault() {
        val cartEmptyHolderData = CartUiModelMapper.mapCartEmptyUiModel(activity)
        viewModel.addItem(cartEmptyHolderData)
    }

    private fun renderCartEmptyWithPromo(lastApplyPromoData: LastApplyPromoData) {
        val cartEmptyWithPromoHolderData =
            CartUiModelMapper.mapCartEmptyWithPromoUiModel(activity, lastApplyPromoData)

        // analytics
        viewModel.addItem(cartEmptyWithPromoHolderData)
        val listPromos = viewModel.getAllPromosApplied(lastApplyPromoData)
        PromoRevampAnalytics.eventCartEmptyPromoApplied(listPromos)
    }

    private fun renderCartNotEmpty(cartData: CartData) {
        FLAG_IS_CART_EMPTY = false

        renderTickerError(cartData)
        renderCartAvailableItems(cartData)
        renderCartUnavailableItems(cartData)

        viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = viewModel.getAllCartItemData()
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
            viewModel.generateCheckoutDataAnalytics(
                cartItemDataList,
                EnhancedECommerceActionField.STEP_0
            ),
            userSession.userId,
            viewModel.getPromoFlag()
        )
        cartData.unavailableSections.forEach { unavailableSection ->
            unavailableSection.unavailableGroups.forEach { unavailableGroup ->
                cartPageAnalytics.eventLoadCartWithUnavailableProduct(
                    unavailableGroup.shop.shopId,
                    unavailableSection.title
                )
            }
        }

        setActivityBackgroundColor()
    }

    private fun renderCartOutOfService(outOfService: OutOfService, isLoadCart: Boolean) {
        binding?.apply {
            var errorType = outOfService.id
            when (outOfService.id) {
                OutOfService.ID_MAINTENANCE, OutOfService.ID_TIMEOUT, OutOfService.ID_OVERLOAD -> {
                    layoutGlobalError.setType(GlobalError.SERVER_ERROR)
                    outOfService.buttons.firstOrNull()?.let { buttonData ->
                        layoutGlobalError.errorAction.text = buttonData.message
                        layoutGlobalError.setActionClickListener {
                            when (buttonData.id) {
                                Button.ID_START_SHOPPING, Button.ID_HOMEPAGE -> {
                                    routeToHome()
                                }

                                Button.ID_RETRY -> {
                                    refreshErrorPage()
                                }
                            }
                        }
                    }
                    errorType = outOfService.getErrorType()
                }
            }

            var message = ""
            if (outOfService.title.isNotBlank()) {
                layoutGlobalError.errorTitle.text = outOfService.title
                message += "- ${outOfService.title}"
            }
            if (outOfService.description.isNotBlank()) {
                layoutGlobalError.errorDescription.text = outOfService.description
                message += "- ${outOfService.description}"
            }
            if (outOfService.image.isNotBlank()) {
                layoutGlobalError.errorIllustration.setImage(outOfService.image, 0f)
            }

            showErrorContainer()

            if (isLoadCart) {
                CartLogger.logOnErrorLoadCartPage(CartResponseErrorException("$errorType $message"))
            }
            cartPageAnalytics.eventViewErrorPageWhenLoadCart(
                userSession.userId,
                errorType
            )
        }
    }

    fun renderDetailInfoSubTotal(
        qty: String,
        subtotalPrice: Double,
        noAvailableItems: Boolean
    ) {
        if (noAvailableItems) {
            binding?.llPromoCheckout?.gone()
        } else {
            if (binding?.bottomLayout?.visibility == View.VISIBLE) {
                binding?.llPromoCheckout?.show()
            }
        }

        renderTotalPrice(subtotalPrice, qty)
    }

    private fun renderErrorInitialGetCartListData(throwable: Throwable) {
        showErrorLayout(throwable)
    }

    private fun renderErrorToShipmentForm(message: String, ctaText: String = "") {
        cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
        cartPageAnalytics.eventViewErrorWhenCheckout(message)
        showToastMessageRed(
            message = message,
            actionText = ctaText,
            ctaClickListener = {
                scrollToUnavailableSection()
            }
        )

        refreshCartWithSwipeToRefresh()
    }

    private fun renderErrorToShipmentForm(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (throwable !is CartResponseErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            renderGlobalErrorBottomsheet(errorMessage)
        } else {
            renderErrorToShipmentForm(errorMessage)
        }
    }

    private fun renderErrorToShipmentForm(outOfService: OutOfService) {
        renderCartOutOfService(outOfService, false)
    }

    private fun renderGlobalErrorBottomsheet(message: String) {
        cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
        cartPageAnalytics.eventViewErrorWhenCheckout(message)

        if (!hasCalledOnSaveInstanceState) {
            context?.let { context ->
                fragmentManager?.let { fragmentManager ->
                    showGlobalErrorBottomsheet(fragmentManager, context, ::retryGoToShipment)
                }
            }
        }
    }

    private fun renderInitialGetCartListDataSuccess(cartData: CartData) {
        viewModel.updateCartModel(
            viewModel.cartModel.copy(
                recommendationPage = 1
            )
        )
        viewModel.processUpdateCartCounter()
        if (cartData.outOfService.isOutOfService()) {
            renderCartOutOfService(cartData.outOfService, true)
            return
        }

        sendAnalyticsScreenNameCartPage()
        updateStateAfterFinishGetCartList()

        renderTickerAnnouncement(cartData)
        activity?.let {
            validateLocalCacheAddress(it, cartData.localizationChooseAddress)
        }

        validateRenderCart(cartData)
        validateShowPopUpMessage(cartData)
        validateRenderPromo(cartData)

        // TODO: change to bottom global checkbox
//        setInitialCheckboxGlobalState(cartData)
        setGlobalDeleteVisibility()

        validateGoToCheckout()
        scrollToLastAddedProductShop()

        renderAdditionalWidget()
        resetArguments()
    }

    private fun renderLoadGetCartData() {
        binding?.apply {
            layoutGlobalError.gone()
            rlContent.show()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
        }
    }

    private fun renderLoadGetCartDataFinish() {
        if (refreshHandler?.isRefreshing == true) {
            refreshHandler?.isRefreshing = false
        }
        showMainContainer()
    }

    private fun renderPromoCheckout(lastApplyUiModel: LastApplyUiModel) {
        renderPromoCheckoutButton(lastApplyUiModel)
    }

    private fun renderPromoCheckoutButton(lastApplyData: LastApplyUiModel) {
        val tickerPromoData = viewModel.cartModel.promoTicker
        if (viewModel.cartModel.showChoosePromoWidget) {
            binding?.promoCheckoutBtnCart?.visible()
            binding?.llPromoCheckoutShadow?.visible()

            val isApplied: Boolean

            binding?.promoCheckoutBtnCart?.state = ButtonPromoCheckoutView.State.ACTIVE
            binding?.promoCheckoutBtnCart?.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM

            val title: String = when {
                lastApplyData.additionalInfo.messageInfo.message.isNotEmpty() -> {
                    lastApplyData.additionalInfo.messageInfo.message
                }

                lastApplyData.defaultEmptyPromoMessage.isNotBlank() -> {
                    lastApplyData.defaultEmptyPromoMessage
                }

                else -> {
                    getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                }
            }

            if (lastApplyData.additionalInfo.messageInfo.detail.isNotEmpty()) {
                isApplied = true
                binding?.promoCheckoutBtnCart?.desc =
                    lastApplyData.additionalInfo.messageInfo.detail
            } else {
                isApplied = false

                if (viewModel.getSelectedCartItemData().isEmpty()) {
                    binding?.promoCheckoutBtnCart?.desc =
                        getString(R.string.promo_desc_no_selected_item)
                } else {
                    binding?.promoCheckoutBtnCart?.desc = ""
                }
            }

            binding?.promoCheckoutBtnCart?.title = title
            binding?.promoCheckoutBtnCart?.setOnClickListener {
                if (viewModel.getSelectedCartItemData().isEmpty()) {
                    showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                    PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
                } else {
                    checkGoToPromo()
                    // analytics
                    PromoRevampAnalytics.eventCartClickPromoSection(
                        viewModel.getAllPromosApplied(
                            lastApplyData
                        ),
                        isApplied,
                        userSession.userId
                    )
                }
            }
            if (isApplied) {
                PromoRevampAnalytics.eventCartViewPromoAlreadyApplied()
            }
        } else {
            binding?.promoCheckoutBtnCart?.gone()
            binding?.llPromoCheckoutShadow?.gone()
        }
        if (tickerPromoData.enable) {
            binding?.promoCheckoutTickerCart?.visible()
            binding?.promoCheckoutTickerCart?.context?.let {
                binding?.promoCheckoutTickerCartText?.text =
                    HtmlLinkHelper(it, tickerPromoData.text).spannedString
            }
            binding?.promoCheckoutTickerCartImage?.loadImage(tickerPromoData.iconUrl)
        } else {
            binding?.promoCheckoutTickerCart?.gone()
        }

        viewModel.updatePromoSummaryData(lastApplyData)
    }

    private fun renderRecentView(recommendationWidget: RecommendationWidget?) {
        var cartRecentViewItemHolderDataList: MutableList<CartRecentViewItemHolderData> =
            ArrayList()
        if (recommendationWidget != null) {
            cartRecentViewItemHolderDataList =
                recentViewMapper.convertToViewHolderModelList(recommendationWidget)
        } else {
            viewModel.cartModel.recentViewList?.let {
                cartRecentViewItemHolderDataList.addAll(it)
            }
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recent_view)

        val cartRecentViewHolderData = CartRecentViewHolderData()
        cartRecentViewHolderData.recentViewList = cartRecentViewItemHolderDataList
        viewModel.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        viewModel.updateCartModel(
            viewModel.cartModel.copy(
                recentViewList = cartRecentViewItemHolderDataList
            )
        )
        shouldReloadRecentViewList = false
    }

    private fun renderRecommendation(recommendationWidget: RecommendationWidget?) {
        val cartRecommendationItemHolderDataList = ArrayList<CartRecommendationItemHolderData>()

        val recommendationItems = recommendationWidget?.recommendationItemList ?: emptyList()
        for (recommendationItem in recommendationItems) {
            val cartRecommendationItemHolderData =
                CartRecommendationItemHolderData(
                    false,
                    recommendationItem
                )
            cartRecommendationItemHolderDataList.add(cartRecommendationItemHolderData)
        }

        var cartSectionHeaderHolderData: CartSectionHeaderHolderData? = null
        if (viewModel.cartModel.recommendationPage == 1) {
            cartSectionHeaderHolderData = CartSectionHeaderHolderData()
            if (!TextUtils.isEmpty(recommendationWidget?.title)) {
                cartSectionHeaderHolderData.title = recommendationWidget?.title ?: ""
            } else {
                cartSectionHeaderHolderData.title =
                    getString(R.string.checkout_module_title_recommendation)
            }
        }

        if (cartRecommendationItemHolderDataList.size > 0) {
            viewModel.addCartRecommendationData(
                cartSectionHeaderHolderData,
                cartRecommendationItemHolderDataList,
                viewModel.cartModel.recommendationPage
            )
        } else {
            viewModel.addCartTopAdsHeadlineData(
                cartSectionHeaderHolderData,
                viewModel.cartModel.recommendationPage
            )
        }

        viewModel.updateCartModel(
            viewModel.cartModel.copy(
                recommendationPage = viewModel.cartModel.recommendationPage + 1
            )
        )
    }

    private fun renderTickerAnnouncement(cartData: CartData) {
        val ticker = cartData.tickers.firstOrNull()
        if (ticker != null && ticker.isValid(CART_PAGE)) {
            viewModel.addItem(CartUiModelMapper.mapTickerAnnouncementUiModel(ticker))
        }
    }

    private fun renderTickerError(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isNotEmpty() && cartData.unavailableSections.isNotEmpty()) {
            val cartItemTickerErrorHolderData = CartUiModelMapper.mapTickerErrorUiModel(cartData)
            viewModel.addItem(cartItemTickerErrorHolderData)
        }
    }

    private fun renderToShipmentFormSuccess(
        eeCheckoutData: Map<String, Any>,
        checkoutProductEligibleForCashOnDelivery: Boolean,
        condition: Int
    ) {
        when (condition) {
            CartViewModel.ITEM_CHECKED_ALL_WITHOUT_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                viewModel.getPromoFlag()
            )

            CartViewModel.ITEM_CHECKED_ALL_WITH_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                viewModel.getPromoFlag()
            )

            CartViewModel.ITEM_CHECKED_PARTIAL_SHOP -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                viewModel.getPromoFlag()
            )

            CartViewModel.ITEM_CHECKED_PARTIAL_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                viewModel.getPromoFlag()
            )

            CartViewModel.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                viewModel.getPromoFlag()
            )
        }
        navigateToShipmentPage()
    }

    private fun renderTotalPrice(subtotalPrice: Double, qty: String) {
        var totalPriceString = "-"
        if (subtotalPrice > 0) {
            totalPriceString =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(subtotalPrice.toLong(), false)
                    .removeDecimalSuffix()
        }

        binding?.tvTotalPrices?.text = totalPriceString
        binding?.goToCourierPageButton?.text =
            String.format(getString(R.string.cart_item_button_checkout_count_format), qty)
        if (totalPriceString == "-") {
            binding?.imgChevronSummary?.gone()
            onCartDataDisableToCheckout()
        } else {
            binding?.imgChevronSummary?.show()
            onCartDataEnableToCheckout()
        }
    }

    private fun renderWishlistV2(
        wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>?,
        forceReload: Boolean
    ) {
        var cartWishlistItemHolderDataList: MutableList<CartWishlistItemHolderData> = ArrayList()
        if (viewModel.cartModel.wishlists != null) {
            if (forceReload && wishlists != null) {
                cartWishlistItemHolderDataList =
                    wishlistMapper.convertToViewHolderModelListV2(wishlists)
            } else {
                cartWishlistItemHolderDataList.addAll(viewModel.cartModel.wishlists!!)
            }
        } else if (wishlists != null) {
            cartWishlistItemHolderDataList =
                wishlistMapper.convertToViewHolderModelListV2(wishlists)
        }

        val cartWishlistHolderData = viewModel.getCartWishlistHolderData()
        cartWishlistHolderData.wishList = cartWishlistItemHolderDataList

        if (viewModel.cartModel.wishlists == null || !forceReload) {
            val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
            cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_wishlist)
            cartSectionHeaderHolderData.showAllAppLink = ApplinkConst.NEW_WISHLIST
            viewModel.addCartWishlistData(cartSectionHeaderHolderData, cartWishlistHolderData)
        } else {
            viewModel.updateCartWishlistData(cartWishlistHolderData)
//            onNeedToUpdateViewItem(wishlistIndex)
        }

        viewModel.updateCartModel(
            viewModel.cartModel.copy(
                wishlists = cartWishlistItemHolderDataList
            )
        )
    }

    private fun resetArguments() {
        arguments?.putString(CartActivity.EXTRA_CART_ID, null)
        arguments?.putLong(CartActivity.EXTRA_PRODUCT_ID, 0)
    }

    private fun resetRecentViewList() {
        shouldReloadRecentViewList = true
    }

    private fun retryGoToShipment() {
        viewModel.processUpdateCartData(false)
    }

    private fun routeToCheckoutPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.CHECKOUT)
            intent.putExtra(
                CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE,
                CheckoutConstant.CHECKOUT_PAGE_SOURCE_CART
            )
            startActivityForResult(intent, NAVIGATION_SHIPMENT)
        }
    }

    private fun routeToHome() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
        }
    }

    private fun routeToPromoCheckoutMarketplacePage() {
        activity?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
            val promoRequest = generateParamsCouponList()
            val validateUseRequest = generateParamGetLastApplyPromo()
            intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CART)
            intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
            intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequest)

            startActivityForResult(intent, NAVIGATION_PROMO)
        }
    }

    private fun routeToWishlist() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.NEW_WISHLIST)
            startActivityForResult(intent, NAVIGATION_WISHLIST)
        }
    }

    private fun sendImpressionOneRecommendationItem(
        it: List<CartRecommendationItemHolderData>,
        currentIndex: Int
    ) {
        val cartRecommendationList = ArrayList<CartRecommendationItemHolderData>()
        cartRecommendationList.add(it[currentIndex])
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(
            viewModel.generateRecommendationImpressionDataAnalytics(
                currentIndex,
                cartRecommendationList,
                FLAG_IS_CART_EMPTY
            )
        )
    }

    private fun sendImpressionTwoRecommendationItems(
        it: List<CartRecommendationItemHolderData>,
        currentIndex: Int
    ) {
        val cartRecommendationList = ArrayList<CartRecommendationItemHolderData>()
        cartRecommendationList.add(it[currentIndex - 1])
        cartRecommendationList.add(it[currentIndex])
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(
            viewModel.generateRecommendationImpressionDataAnalytics(
                currentIndex,
                cartRecommendationList,
                FLAG_IS_CART_EMPTY
            )
        )
    }

    private fun setHasTriedToLoadRecentView() {
        hasTriedToLoadRecentViewList = true
    }

    private fun setHasTriedToLoadWishList() {
        hasTriedToLoadWishList = true
    }

    private fun setHasTriedToLoadRecommendation() {
        hasTriedToLoadRecommendation = true
    }

    private fun scrollToLastAddedProductShop() {
        val cartId: String = getCartId()
        if (cartId.isNotBlank()) {
            var hasProducts = false
            loop@ for (shop in viewModel.getAllShopGroupDataList()) {
                hasProducts = true
                break@loop
            }

            if (hasProducts) {
                val shopIndex = cartAdapter.getCartShopHolderIndexByCartId(cartId)
                if (shopIndex != RecyclerView.NO_POSITION) {
                    val offset =
                        context?.resources?.getDimensionPixelSize(R.dimen.select_all_view_holder_height)
                            ?: 0
                    val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
                    if (layoutManager != null) {
                        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            shopIndex,
                            offset
                        )
                    }
                }
            }
        }
    }

    private fun scrollToUnavailableSection() {
        binding?.rvCart?.layoutManager?.let {
            val linearSmoothScroller = object : LinearSmoothScroller(binding?.rvCart?.context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            linearSmoothScroller.targetPosition = viewModel.getDisabledItemHeaderPosition()
            it.startSmoothScroll(linearSmoothScroller)
        }
    }

    private fun sendAnalyticsScreenNameCartPage() {
        cartPageAnalytics.sendScreenName(activity, screenName)
    }

    private fun setActivityBackgroundColor() {
        activity?.let {
            if (activity !is CartActivity) {
                binding?.llCartContainer?.setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_N50
                    )
                )
            }

            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N50
                )
            )
        }
    }

    private fun showEmptyCartContainer() {
        binding?.apply {
            layoutGlobalError.gone()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
        }
    }

    private fun showErrorLayout(throwable: Throwable) {
        activity?.let {
            enableSwipeRefresh()
            it.invalidateOptionsMenu()
            refreshHandler?.finishRefresh()
            showErrorContainer()
            setToolbarShadowVisibility(true)
            val errorType = getGlobalErrorType(throwable)
            binding?.layoutGlobalError?.setType(errorType)
            if (errorType == GlobalError.SERVER_ERROR) {
                binding?.layoutGlobalError?.errorAction?.text = WORDING_GO_TO_HOMEPAGE
                binding?.layoutGlobalError?.setActionClickListener {
                    routeToHome()
                }
            } else {
                binding?.layoutGlobalError?.setActionClickListener {
                    refreshErrorPage()
                }
            }
            binding?.layoutGlobalError?.show()
            if (throwable is AkamaiErrorException) {
                showToastMessageRed(throwable)
            }
        }
    }

    private fun showItemLoading() {
        viewModel.addCartLoadingData()
    }

    private fun hideItemLoading() {
        viewModel.removeCartLoadingData()
        endlessRecyclerViewScrollListener.updateStateAfterGetData()
        hasLoadRecommendation = true
    }

    private fun showMainContainer() {
        binding?.apply {
            layoutGlobalError.gone()
            rlContent.show()
            bottomLayout.show()
            bottomLayoutShadow.show()
            llPromoCheckout.show()
            llPromoCheckout.post {
                if (initialPromoButtonPosition == 0f) {
                    initialPromoButtonPosition = llPromoCheckout.y
                }
            }
        }
    }

    private fun showProgressLoading() {
        if (progressDialog?.isShowing == false) progressDialog?.show()
    }

    private fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) progressDialog?.dismiss()
        if (refreshHandler?.isRefreshing == true) {
            refreshHandler?.finishRefresh()
        }
    }

    private fun setSpanSize(gridLayoutManager: GridLayoutManager) {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position != RecyclerView.NO_POSITION) {
                    if (position < cartAdapter.itemCount && cartAdapter.getItemViewType(position) == CartRecommendationViewHolder.LAYOUT) {
                        SPAN_SIZE_ONE
                    } else {
                        SPAN_SIZE_TWO
                    }
                } else {
                    SPAN_SIZE_ZERO
                }
            }
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (binding?.topLayout?.root?.visibility == View.VISIBLE) {
            if (show) {
                binding?.topLayoutShadow?.show()
                binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
            } else {
                binding?.topLayoutShadow?.gone()
            }
        } else {
            if (show) {
                binding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
                binding?.topLayoutShadow?.gone()
            } else {
                binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    private fun setTopLayoutVisibility(isShow: Boolean) {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (isShow) {
            binding?.topLayout?.root?.show()
            if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                isShowToolbarShadow = true
            }
        } else {
            binding?.topLayout?.root?.gone()
        }

        setToolbarShadowVisibility(isShowToolbarShadow)
    }

    private fun showBottomSheetSummaryTransaction() {
        context?.let { context ->
            val promoSummaryUiModel = viewModel.cartModel.promoSummaryUiModel
            viewModel.cartModel.summaryTransactionUiModel?.let { summaryTransactionUiModel ->
                showSummaryTransactionBottomsheet(
                    summaryTransactionUiModel,
                    promoSummaryUiModel,
                    parentFragmentManager,
                    context
                )
            }
        }
    }

    private fun showErrorContainer() {
        binding?.apply {
            rlContent.gone()
            layoutGlobalError.show()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
        }
    }

    private fun showToastMessageGreen(
        message: String,
        actionText: String = "",
        onClickListener: View.OnClickListener? = null
    ) {
        view?.let { v ->
            var tmpCtaClickListener = View.OnClickListener { }

            if (onClickListener != null) {
                tmpCtaClickListener = onClickListener
            }

            initializeToasterLocation()
            if (actionText.isNotBlank()) {
                Toaster.toasterCustomCtaWidth = v.resources.getDimensionPixelOffset(R.dimen.dp_100)
                Toaster.build(
                    v,
                    message,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    actionText,
                    tmpCtaClickListener
                ).show()
            } else {
                Toaster.build(
                    v,
                    message,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    v.resources.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok),
                    tmpCtaClickListener
                ).show()
            }
        }
    }

    private fun showToastMessageRed(
        message: String,
        actionText: String = "",
        ctaClickListener: View.OnClickListener? = null
    ) {
        view?.let {
            var tmpMessage = message
            if (TextUtils.isEmpty(tmpMessage)) {
                tmpMessage = CartConstant.CART_ERROR_GLOBAL
            }

            var tmpCtaClickListener = View.OnClickListener { }

            if (ctaClickListener != null) {
                tmpCtaClickListener = ctaClickListener
            }

            initializeToasterLocation()
            if (actionText.isNotBlank()) {
                Toaster.build(
                    it,
                    tmpMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    actionText,
                    tmpCtaClickListener
                ).show()
            } else {
                Toaster.build(
                    it,
                    tmpMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    it.resources.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok),
                    tmpCtaClickListener
                ).show()
            }
        }
    }

    private fun showToastMessageRed(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (!(throwable is CartResponseErrorException || throwable is AkamaiErrorException || throwable is ResponseErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        showToastMessageRed(errorMessage)
    }

    private fun showToastMessageRed() {
        showToastMessageRed("")
    }

    private fun stopCartPerformanceTrace(isSuccessLoadCart: Boolean) {
        if (!isTraceCartStopped) {
            EmbraceMonitoring.stopMoments(EmbraceKey.KEY_MP_CART)
            cartPerformanceMonitoring?.stopTrace()
            isTraceCartStopped = true
        }
    }

    private fun stopAllCartPerformanceTrace() {
        if (!isTraceCartAllStopped && hasTriedToLoadRecentViewList && hasTriedToLoadWishList && hasTriedToLoadRecommendation) {
            cartAllPerformanceMonitoring?.stopTrace()
            isTraceCartAllStopped = true
        }
    }

    private fun updateCartCounter(counter: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (counter > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, counter)
        cache.applyEditor()
    }

    private fun updateStateAfterFinishGetCartList() {
        endlessRecyclerViewScrollListener.resetState()
        refreshHandler?.finishRefresh()
        viewModel.resetData()
    }

    private fun validateLocalCacheAddress(
        activity: FragmentActivity,
        localizationChooseAddress: LocalizationChooseAddress
    ) {
        var snippetMode = false
        var lca = ChooseAddressUtils.getLocalizingAddressData(activity)
        if (lca.address_id.toLongOrZero() == 0L && lca.district_id.toLongOrZero() != 0L) {
            snippetMode = true
        }

        val newTokoNowData = localizationChooseAddress.tokoNow
        val shouldReplaceTokoNowData = newTokoNowData.isModified
        if (!snippetMode && localizationChooseAddress.state == LocalizationChooseAddress.STATE_ADDRESS_ID_NOT_MATCH) {
            lca = ChooseAddressUtils.setLocalizingAddressData(
                addressId = localizationChooseAddress.addressId,
                cityId = localizationChooseAddress.cityId,
                districtId = localizationChooseAddress.districtId,
                lat = localizationChooseAddress.latitude,
                long = localizationChooseAddress.longitude,
                label = "${localizationChooseAddress.addressName} ${localizationChooseAddress.receiverName}",
                postalCode = localizationChooseAddress.postalCode,
                shopId = if (shouldReplaceTokoNowData) newTokoNowData.shopId else lca.shop_id,
                warehouseId = if (shouldReplaceTokoNowData) newTokoNowData.warehouseId else lca.warehouse_id,
                warehouses = if (shouldReplaceTokoNowData) {
                    TokonowWarehouseMapper.mapWarehousesResponseToLocal(
                        newTokoNowData.warehouses
                    )
                } else {
                    lca.warehouses
                },
                serviceType = if (shouldReplaceTokoNowData) newTokoNowData.serviceType else lca.service_type
            )
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = activity,
                localData = lca
            )
        } else if (shouldReplaceTokoNowData) {
            // no need to update lca variable, because tokonow data is not used in presenter
            ChooseAddressUtils.updateTokoNowData(
                context = activity,
                shopId = newTokoNowData.shopId,
                warehouseId = newTokoNowData.warehouseId,
                warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(newTokoNowData.warehouses),
                serviceType = newTokoNowData.serviceType
            )
        }
        viewModel.setLocalizingAddressData(lca)
    }

    private fun validateGoToCheckout() {
        viewModel.checkForShipmentForm()
    }

    private fun validateRenderCart(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isEmpty() && cartData.unavailableSections.isEmpty()) {
            renderCartEmpty(cartData)
            setTopLayoutVisibility(false)
        } else {
            renderCartNotEmpty(cartData)
            setTopLayoutVisibility(cartData.availableSection.availableGroupGroups.isNotEmpty())
        }
    }

    private fun validateRenderPromo(cartData: CartData) {
        // reset promo position
        initialPromoButtonPosition = 0f
        if (viewModel.cartModel.isLastApplyResponseStillValid) {
            // Render promo from last apply
            validateRenderPromoFromLastApply(cartData)

            // Render promo from last validate use from cart page (check / uncheck result) if any
            validateRenderPromoFromValidateUseCartPage()
        } else {
            // Render promo from last validate use from promo page
            validateRenderPromoFromValidateUsePromoPage()

            // Render promo from last validate use from cart page (check / uncheck result) if any
            validateRenderPromoFromValidateUseCartPage()
        }
    }

    private fun validateRenderPromoFromLastApply(cartData: CartData) {
        val lastApplyPromoData = cartData.promo.lastApplyPromo.lastApplyPromoData
        // show toaster if any promo applied has been changed
        if (cartData.promo.showChoosePromoWidget && lastApplyPromoData.additionalInfo.errorDetail.message.isNotEmpty()) {
            showToastMessageGreen(lastApplyPromoData.additionalInfo.errorDetail.message)
            PromoRevampAnalytics.eventCartViewPromoMessage(lastApplyPromoData.additionalInfo.errorDetail.message)
        }
        val lastApplyUiModel = CartUiModelMapper.mapLastApplySimplified(lastApplyPromoData)
        renderPromoCheckout(lastApplyUiModel)
    }

    private fun validateRenderPromoFromValidateUsePromoPage() {
        viewModel.cartModel.lastValidateUseResponse?.promoUiModel?.let {
            val lastApplyUiModel =
                LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
        }
    }

    private fun validateRenderPromoFromValidateUseCartPage() {
        viewModel.cartModel.lastUpdateCartAndGetLastApplyResponse?.promoUiModel?.let {
            val lastApplyUiModel =
                LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
        }
    }

    private fun validateRenderRecentView() {
        if (viewModel.cartModel.recentViewList == null || shouldReloadRecentViewList) {
            viewModel.processGetRecentViewData(viewModel.getAllCartItemProductId())
        } else {
            renderRecentView(null)
        }
    }

    private fun validateRenderWishlist() {
        if (viewModel.cartModel.wishlists == null) {
            viewModel.processGetWishlistV2Data()
        } else {
            renderWishlistV2(null, false)
        }
    }

    private fun validateShowPopUpMessage(cartData: CartData) {
        if (cartData.popupErrorMessage.isNotBlank()) {
            showToastMessageRed(cartData.popupErrorMessage)
            cartPageAnalytics.eventViewToasterErrorInCartPage(cartData.popupErrorMessage)
        } else if (cartData.popUpMessage.isNotBlank()) {
            showToastMessageGreen(cartData.popUpMessage)
        }
    }
}
