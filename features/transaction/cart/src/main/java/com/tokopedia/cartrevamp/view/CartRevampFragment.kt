package com.tokopedia.cartrevamp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
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
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.FragmentCartRevampBinding
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.CartFragment
import com.tokopedia.cart.view.ICartListPresenter
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
import com.tokopedia.cartrevamp.view.uimodel.AddToCartEvent
import com.tokopedia.cartrevamp.view.uimodel.AddToWishlistV2Event
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cartrevamp.view.uimodel.CartState
import com.tokopedia.cartrevamp.view.uimodel.CartTrackerEvent
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DeleteCartEvent
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.cartrevamp.view.uimodel.UndoDeleteEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartAndGetLastApplyEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.cartrevamp.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
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
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.ARGS_CLEAR_PROMO_RESULT
import com.tokopedia.purchase_platform.common.constant.ARGS_LAST_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_DATA_RESULT
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.abs

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
    private var delayShowPromoButtonJob: Job? = null
    private var delayShowSelectedAmountJob: Job? = null
    private var TRANSLATION_LENGTH = 0f
    private var SELECTED_AMOUNT_TRANSLATION_LENGTH = 0f
    private var isKeyboardOpened = false
    private var initialSelectedAmountPosition = 0f
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

        private const val TOKONOW_SEE_OTHERS_OR_ALL_LIMIT = 10

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
        const val DELAY_SHOW_SELECTED_AMOUNT_AFTER_SCROLL = 750L
        const val PROMO_ANIMATION_DURATION = 500L
        const val SELECTED_AMOUNT_ANIMATION_DURATION = 500L
        const val PROMO_POSITION_BUFFER = 10
        const val SELECTED_AMOUNT_POSITION_BUFFER = 10
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

        private const val TOKONOW_UPDATER_DEBOUNCE = 500L

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
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart()
        routeToHome()
    }

    override fun onCartGroupNameClicked(appLink: String) {
        routeToApplink(appLink)
    }

    override fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean) {
        if (shopId != null && shopName != null) {
            cartPageAnalytics.eventClickAtcCartClickShop(shopId, shopName)
            if (isTokoNow) {
                routeToTokoNowHomePage()
            } else {
                routeToShopPage(shopId)
            }
        }
    }

    override fun onShopItemCheckChanged(index: Int, checked: Boolean) {
        viewModel.cartModel.hasPerformChecklistChange = true
        viewModel.setShopSelected(index, checked)
        viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
        val data = viewModel.cartDataList.value[index]
        var isCollapsed = false
        var productSize = 1
        if (data is CartGroupHolderData) {
            isCollapsed = data.isCollapsed
            productSize += data.productUiModelList.size
            if (checked) {
                checkCartShopGroupTicker(data)
            }
        }
        if (isCollapsed) {
            onNeedToUpdateViewItem(index)
            onNeedToUpdateViewItem(index + 1)
        } else {
            onNeedToUpdateMultipleViewItem(index, productSize + 1)
        }
        validateGoToCheckout()
        viewModel.saveCheckboxState(viewModel.getAllAvailableCartItemHolderData())

        val params = viewModel.generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }

        setCheckboxGlobalState()
        setSelectedAmountVisibility()
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
        cartPageAnalytics.eventViewBoTickerWording(
            cartGroupHolderData.cartShopGroupTicker.state == CartShopGroupTickerState.SUCCESS_AFFORD,
            cartGroupHolderData.cartShopGroupTicker.cartIds,
            cartGroupHolderData.shop.shopId
        )
    }

    override fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.cartShopGroupTicker.enableCartAggregator && !cartGroupHolderData.isError && cartGroupHolderData.hasSelectedProduct) {
            cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.LOADING
            viewModel.checkCartShopGroupTicker(cartGroupHolderData)
        }
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
        var position = 0

        viewModel.cartModel.wishlists?.let { wishlists ->
            for (wishlist in wishlists) {
                if (wishlist.id.equals(productId, ignoreCase = true)) {
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(
                            position.toString(),
                            viewModel.generateWishlistProductClickEmptyCartDataLayer(
                                wishlist,
                                position
                            )
                        )
                    } else {
                        cartPageAnalytics.enhancedEcommerceClickProductWishListOnCartList(
                            position.toString(),
                            viewModel.generateWishlistProductClickDataLayer(wishlist, position)
                        )
                    }
                }
                position++
            }

            onProductClicked(productId)
        }
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
        viewModel.cartModel.recentViewList?.withIndex()
            ?.forEach { (position, recentView) ->
                if (recentView.id.equals(productId, ignoreCase = true)) {
                    if (recentView.isTopAds) {
                        TopAdsUrlHitter(context?.applicationContext).hitClickUrl(
                            this::class.java.simpleName,
                            recentView.clickUrl,
                            recentView.id,
                            recentView.name,
                            recentView.imageUrl
                        )
                    }
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnEmptyCart(
                            position.toString(),
                            viewModel.generateRecentViewProductClickEmptyCartDataLayer(
                                recentView,
                                position
                            )
                        )
                    } else {
                        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnCartList(
                            position.toString(),
                            viewModel.generateRecentViewProductClickDataLayer(recentView, position)
                        )
                    }
                }
            }
        onProductClicked(productId)
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
        if (viewModel.dataHasChanged()) {
            viewModel.processUpdateCartData(true)
        }
        viewModel.processAddToCart(productModel)
    }

    override fun onDeleteAllDisabledProduct() {
        TODO("Not yet implemented")
    }

    override fun onSeeErrorProductsClicked() {
        TODO("Not yet implemented")
    }

    override fun onCollapseAvailableItem(index: Int) {
        val cartShopBottomHolderData = cartAdapter.getCartShopBottomHolderDataFromIndex(index)
        if (cartShopBottomHolderData != null) {
            cartShopBottomHolderData.shopData.isCollapsed = true
            viewModel.cartDataList.value.removeAll(cartShopBottomHolderData.shopData.productUiModelList.toSet())
            onNeedToUpdateViewItem(index)
            onNeedToRemoveMultipleViewItem(
                index - cartShopBottomHolderData.shopData.productUiModelList.size,
                cartShopBottomHolderData.shopData.productUiModelList.size
            )
            onNeedToUpdateViewItem(index - 1 - cartShopBottomHolderData.shopData.productUiModelList.size)
            val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
            if (layoutManager != null) {
                // TODO: remove offset
//                val offset = resources.getDimensionPixelOffset(R.dimen.dp_40)
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    index - 1 - cartShopBottomHolderData.shopData.productUiModelList.size,
                    0
                )
            }
        }
    }

    override fun onExpandAvailableItem(index: Int) {
        val cartShopBottomHolderData = cartAdapter.getCartShopBottomHolderDataFromIndex(index)
        if (cartShopBottomHolderData != null) {
            // TODO: reinsert analytics
//            if (cartShopBottomHolderData.shopData.productUiModelList.size > TOKONOW_SEE_OTHERS_OR_ALL_LIMIT) {
//                cartPageAnalytics.eventClickLihatOnPlusLainnyaOnNowProduct(cartShopBottomHolderData.shopData.shop.shopId)
//            } else {
//                cartPageAnalytics.eventClickLihatSelengkapnyaOnNowProduct(cartShopBottomHolderData.shopData.shop.shopId)
//            }
            cartShopBottomHolderData.shopData.isCollapsed = false
            viewModel.addItems(index, cartShopBottomHolderData.shopData.productUiModelList)
            onNeedToInsertMultipleViewItem(
                index,
                cartShopBottomHolderData.shopData.productUiModelList.size
            )
            onNeedToUpdateViewItem(index - 1)
            onNeedToUpdateViewItem(index + cartShopBottomHolderData.shopData.productUiModelList.size)
        }
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
        cartPageAnalytics.eventClickAccordionButtonOnUnavailableProduct(
            userSession.userId,
            buttonWording
        )
        data.isCollapsed = !data.isCollapsed
        unavailableItemAccordionCollapseState = data.isCollapsed
        collapseOrExpandDisabledItem(data)
    }

    override fun onDisabledCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemHolderData.productName)
        routeToProductDetailPage(cartItemHolderData.productId)
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
        cartPageAnalytics.eventClickGlobalDelete()
        val allCartItemDataList = viewModel.getAllCartItemData()
        val deletedCartItems = viewModel.getSelectedCartItemData()
        val dialog = getMultipleItemsDialogDeleteConfirmation(deletedCartItems.size)
        dialog?.setPrimaryCTAClickListener {
            viewModel.processDeleteCartItem(
                allCartItemData = allCartItemDataList,
                removedCartItems = deletedCartItems,
                addWishList = false,
                isFromGlobalCheckbox = true
            )
            dialog.dismiss()
        }
        dialog?.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog?.show()
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
        cartPageAnalytics.eventViewGotoplusTicker()
    }

    override fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData) {
        TODO("Not yet implemented")
    }

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData, isFromDeleteButton: Boolean) {
        if (isFromDeleteButton) {
            cartPageAnalytics.eventClickAtcCartClickTrashBin()
        }
        val allCartItemDataList = viewModel.getAllCartItemData()
        val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
        if (cartItemHolderData.isBundlingItem) {
            val cartGroupHolderData =
                cartAdapter.getCartGroupHolderDataByCartItemHolderData(cartItemHolderData)
            cartGroupHolderData?.let {
                it.productUiModelList.forEach { product ->
                    if (product.isBundlingItem && product.bundleId == cartItemHolderData.bundleId && product.bundleGroupId == cartItemHolderData.bundleGroupId) {
                        toBeDeletedProducts.add(product)
                    }
                }
            }
        } else {
            toBeDeletedProducts.add(cartItemHolderData)
        }

        if (toBeDeletedProducts.size > 0) {
            // If unavailable item > 1 and state is collapsed, then expand first
            var forceExpand = false
            if (viewModel.getAllDisabledCartItemData().size > 1 && unavailableItemAccordionCollapseState) {
                collapseOrExpandDisabledItem()
                forceExpand = true
            }

            viewModel.processDeleteCartItem(
                allCartItemDataList,
                toBeDeletedProducts,
                false,
                forceExpand
            )
            if (isFromDeleteButton) {
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(
                    viewModel.generateDeleteCartDataAnalytics(toBeDeletedProducts)
                )
            }
        }
    }

    override fun onCartItemQuantityPlusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus()
    }

    override fun onCartItemQuantityMinusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus()
    }

    override fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemHolderData.productName)
        routeToProductDetailPage(cartItemHolderData.productId)
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        TODO("Not yet implemented")
    }

    override fun onCartItemLabelInputRemarkClicked() {
        TODO("Not yet implemented")
    }

    override fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData) {
        viewModel.setItemSelected(position, cartItemHolderData, !cartItemHolderData.isSelected)
        updateStateAfterCheckChanged()
    }

    override fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData) {
        val (index, groupData) = cartAdapter.getCartGroupHolderDataAndIndexByCartString(
            cartItemHolderData.cartString,
            false
        )
        if (index > 0) {
            val selected = !cartItemHolderData.isSelected
            groupData.forEachIndexed { position, data ->
                if (data is CartItemHolderData && data.isBundlingItem && data.bundleId == cartItemHolderData.bundleId && data.bundleGroupId == cartItemHolderData.bundleGroupId) {
                    viewModel.setItemSelected(index + position, cartItemHolderData, selected)
                }
            }
            updateStateAfterCheckChanged()
        }
    }

    override fun onWishlistCheckChanged(
        cartItemHolderData: CartItemHolderData,
        wishlistIcon: IconUnify,
        animatedWishlistImage: ImageView,
        position: Int
    ) {
        if (cartItemHolderData.isError) {
            cartPageAnalytics.eventClickMoveToWishlistOnUnavailableSection(
                userSession.userId,
                cartItemHolderData.productId,
                cartItemHolderData.errorType
            )
        } else {
            cartPageAnalytics.eventClickMoveToWishlistOnAvailableSection(
                userSession.userId,
                cartItemHolderData.productId
            )
        }
        val isLastItem = viewModel.getAllCartItemData().size == 1
        // TODO: remove
//        var forceExpand = false
//        if (isError) {
//            // If unavailable item > 1 and state is collapsed, then expand first
//            if (viewModel.allDisabledCartItemData.size > 1 && unavailableItemAccordionCollapseState) {
//                collapseOrExpandDisabledItem()
//                forceExpand = true
//            }
//        }
        if (cartItemHolderData.isWishlisted) {
            viewModel.processAddCartToWishlist(
                cartItemHolderData.productId,
                userSession.userId,
                isLastItem,
                if (cartItemHolderData.isError) WISHLIST_SOURCE_UNAVAILABLE_ITEM else WISHLIST_SOURCE_AVAILABLE_ITEM,
                wishlistIcon,
                animatedWishlistImage
            )
        } else {
            viewModel.processRemoveFromWishlistV2(
                cartItemHolderData.productId,
                userSession.userId,
                object : WishlistV2ActionListener {
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        // no-op
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        // no-op
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        showToastMessageRed(throwable)
                    }

                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        onRemoveFromWishlistSuccess(wishlistIcon, position)
                    }
                }
            )
        }
    }

    override fun onNeedToRefreshSingleShop(
        cartItemHolderData: CartItemHolderData,
        itemPosition: Int
    ) {
        val (index, groupData) = cartAdapter.getCartGroupHolderDataAndIndexByCartString(
            cartItemHolderData.cartString,
            false
        )
        if (index >= 0) {
            val shopHeaderData = groupData.first()
            if (shopHeaderData is CartGroupHolderData) {
                onNeedToUpdateViewItem(index)
            }
            onNeedToUpdateViewItem(itemPosition)
            val shopBottomData = groupData.last()
            if (shopBottomData is CartShopBottomHolderData) {
                checkCartShopGroupTicker(shopBottomData.shopData)
                onNeedToUpdateViewItem(index + groupData.lastIndex)
            }
        }
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onNeedToRecalculate() {
        viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
    }

    override fun onCartItemQuantityChanged(
        cartItemHolderData: CartItemHolderData,
        newQuantity: Int
    ) {
        if (cartItemHolderData.isBundlingItem) {
            val cartGroupHolderData =
                cartAdapter.getCartGroupHolderDataByCartItemHolderData(cartItemHolderData)
            cartGroupHolderData?.let {
                it.productUiModelList.forEach {
                    if (it.isBundlingItem && it.bundleId == cartItemHolderData.bundleId && it.bundleGroupId == cartItemHolderData.bundleGroupId) {
                        it.bundleQuantity = newQuantity
                    }
                }
            }
        } else {
            cartItemHolderData.quantity = newQuantity
        }

        validateGoToCheckout()
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            viewModel.doUpdateCartAndGetLastApply(params)
        } else if (cartItemHolderData.isTokoNow) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.emitTokonowUpdated(true)
            }
        }
    }

    override fun onCartItemShowRemainingQty(productId: String) {
        cartPageAnalytics.eventViewRemainingStockInfo(userSession.userId, productId)
    }

    override fun onCartItemShowInformationLabel(productId: String, informationLabel: String) {
        cartPageAnalytics.eventViewInformationLabelInProductCard(
            userSession.userId,
            productId,
            informationLabel
        )
    }

    override fun onEditBundleClicked(cartItemHolderData: CartItemHolderData) {
        activity?.let {
            cartPageAnalytics.eventClickUbahInProductBundlingPackageProductCard(
                cartItemHolderData.bundleId,
                cartItemHolderData.bundleType
            )
            val intent = RouteManager.getIntent(it, cartItemHolderData.editBundleApplink)
            viewModel.cartModel.toBeDeletedBundleGroupId = cartItemHolderData.bundleGroupId
            val editBundleActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    onResultFromEditBundle(result.resultCode, result.data)
                }
            editBundleActivityResult.launch(intent)
        }
    }

    override fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action) {
        TODO("Not yet implemented")
    }

    override fun onShowTickerTobacco() {
        TODO("Not yet implemented")
    }

    override fun onSimilarProductUrlClicked(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            cartPageAnalytics.eventClickMoreLikeThisOnBundleProduct(data.bundleId, data.bundleType)
        } else {
            cartPageAnalytics.eventClickMoreLikeThis()
        }
        routeToApplink(data.selectedUnavailableActionLink)
    }

    override fun onShowActionSeeOtherProduct(productId: String, errorType: String) {
        cartPageAnalytics.eventClickSeeOtherProductOnUnavailableSection(
            userSession.userId,
            productId,
            errorType
        )
    }

    override fun onFollowShopClicked(shopId: String, errorType: String) {
        TODO("Not yet implemented")
    }

    override fun onVerificationClicked(applink: String) {
        activity?.also {
            val intent = RouteManager.getIntentNoFallback(it, applink) ?: return
            startActivityWithRefreshHandler(intent)
        }
    }

    override fun onProductAddOnClicked(addOnId: CartItemHolderData) {
        TODO("Not yet implemented")
    }

    override fun onCashbackUpdated(amount: Int) {
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
        cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
        routeToWishlist()
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
                handleSelectedAmountVisibilityOnIdle(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (binding?.bottomLayout?.visibility == View.GONE) {
                    binding?.llPromoCheckout?.gone()
                }

                if (recyclerView.canScrollVertically(-1)) {
                    disableSwipeRefresh()
                } else {
                    enableSwipeRefresh()
                }

                handleSelectedAmountVisibilityOnScroll(dy)
                handlePromoButtonVisibilityOnScroll(dy)
                handleFloatingSelectedAmountVisibility(recyclerView)
            }
        })
    }

    private fun addToCartExternal(productId: Long) {
//        dPresenter.processAddToCartExternal(productId)
    }

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun animateSelectedAmountToHiddenPosition(valueY: Float) {
        binding?.rlTopLayout?.animate()?.y(valueY)?.setDuration(0)?.start()
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

    private fun collapseOrExpandDisabledItem() {
        viewModel.getDisabledAccordionHolderData()?.let {
            it.isCollapsed = !it.isCollapsed
            unavailableItemAccordionCollapseState = it.isCollapsed
            collapseOrExpandDisabledItem(it)
        }
    }

    private fun collapseOrExpandDisabledItem(data: DisabledAccordionHolderData) {
        val index = viewModel.cartDataList.value.indexOf(data)
        if (index > 0) {
            onNeedToUpdateViewItem(index)
        }
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

    private fun getAllAppliedPromoCodes(params: ValidateUsePromoRequest): List<String> {
        val allPromoApplied = arrayListOf<String>()
        if (params.orders.isNotEmpty()) {
            params.orders.forEach { orderItem ->
                if (orderItem.codes.isNotEmpty()) {
                    orderItem.codes.forEach { merchantCode ->
                        allPromoApplied.add(merchantCode)
                    }
                }
            }
            params.codes.forEach { globalCode ->
                allPromoApplied.add(globalCode)
            }
        }
        return allPromoApplied
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

    private fun getMultipleItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_multiple_item, count))
                setDescription(getString(R.string.cart_label_dialog_message_remove_cart_multiple_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete_simple))
                setSecondaryCTAText(getString(R.string.cart_label_dialog_action_cancel_simple))
            }
        }

        return null
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
        val isChecked = binding?.checkboxGlobal?.isChecked ?: return
        if (isCheckUncheckDirectAction) {
            viewModel.setAllAvailableItemCheck(isChecked)
            viewModel.updateSelectedAmount()
            viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
            viewModel.saveCheckboxState(viewModel.getAllAvailableCartItemHolderData())
            setSelectedAmountVisibility()
            cartPageAnalytics.eventCheckUncheckGlobalCheckbox(isChecked)

            reloadAppliedPromoFromGlobalCheck()
        }
        isCheckUncheckDirectAction = true
    }

    private fun handleSelectedAmountVisibilityOnIdle(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && initialSelectedAmountPosition > 0) {
            // Delay after recycler view idle, then show promo button
            delayShowSelectedAmountJob?.cancel()
            delayShowSelectedAmountJob =
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    delay(DELAY_SHOW_SELECTED_AMOUNT_AFTER_SCROLL)
                    binding?.apply {
                        val initialPosition = navToolbar.y
                        rlTopLayout.animate().y(initialPosition)
                            .setDuration(SELECTED_AMOUNT_ANIMATION_DURATION).start()
                    }
                }
        }
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

    private fun handleSelectedAmountVisibilityOnScroll(dy: Int) {
        val rlTopLayout = binding?.rlTopLayout ?: return
        var valueY = rlTopLayout.y - (abs(dy) / 1.5f)
        SELECTED_AMOUNT_TRANSLATION_LENGTH += dy
        if (dy != 0) {
            if (initialSelectedAmountPosition == 0f && SELECTED_AMOUNT_TRANSLATION_LENGTH - dy == 0f) {
                initialSelectedAmountPosition = binding?.llPromoCheckout?.y ?: 0f
            }

            if (SELECTED_AMOUNT_TRANSLATION_LENGTH != 0f) {
                animateSelectedAmountToHiddenPosition(valueY)
            }
        }
    }

    private fun handlePromoButtonVisibilityOnScroll(dy: Int) {
        val llPromoCheckout = binding?.llPromoCheckout ?: return
        val valueY = llPromoCheckout.y + abs(dy)

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
            }
        }
    }

    private fun handleFloatingSelectedAmountVisibility(recyclerView: RecyclerView) {
        val topItemPosition =
            (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        if (topItemPosition == RecyclerView.NO_POSITION) return

        val adapterData = viewModel.cartDataList.value
        if (topItemPosition >= adapterData.size) return

        val firstVisibleItemData = adapterData[topItemPosition]

        if (viewModel.getAllAvailableCartItemData().isNotEmpty() &&
            viewModel.hasSelectedCartItem() &&
            firstVisibleItemData !is CartSelectedAmountHolderData
        ) {
            setTopLayoutVisibility(true)
        } else {
            setTopLayoutVisibility(false)
            handleSelectedAmountVisibilityOnIdle(RecyclerView.SCROLL_STATE_IDLE)
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
            textTotalPaymentLabel.setOnClickListener { onClickChevronSummaryTransaction() }
            tvTotalPrices.setOnClickListener { onClickChevronSummaryTransaction() }
        }
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.tokoNowProductUpdater.debounce(TOKONOW_UPDATER_DEBOUNCE).collectLatest {
                viewModel.processUpdateCartData(true, true)
            }
        }

        observeAddCartToWishlistEvent()

        observeAddToCart()

        observeCartDataList()

        observeCartEvent()

        observeCartTrackerEvent()

        observeCheckoutButton()

        observeDeleteCartEvent()

        observeGlobalEvent()

        observeRecentView()

        observeRecommendation()

        observeSelectedAmount()

        observeUpdateCartEvent()

        observeUndoDeleteEvent()

        observeUpdateCartAndGetLastApply()

        observeWishlist()
    }

    private fun initToolbar() {
        initNavigationToolbar()
        binding?.navToolbar?.show()
        setToolbarShadowVisibility(false)
    }

    @OptIn(FlowPreview::class)
    private fun initTopLayout() {
        binding?.checkboxGlobal?.checks()?.debounce(DELAY_CHECK_BOX_GLOBAL)?.onEach {
            handleCheckboxGlobalChangeEvent()
        }?.launchIn(lifecycleScope)

        binding?.topLayout?.textActionDelete?.setOnClickListener {
            onGlobalDeleteClicked()
        }
    }

    private fun isNeedHitUpdateCartAndValidateUse(params: ValidateUsePromoRequest): Boolean {
        var isPromoApplied = false
        val allPromoApplied = arrayListOf<String>()
        if (params.orders.isNotEmpty()) {
            params.orders.forEach { orderItem ->
                if (orderItem.codes.isNotEmpty()) {
                    orderItem.codes.forEach { merchantCode ->
                        allPromoApplied.add(merchantCode)
                    }
                }
            }
            params.codes.forEach { globalCode ->
                allPromoApplied.add(globalCode)
            }
        }
        if (params.orders.isNotEmpty() && allPromoApplied.isNotEmpty()) isPromoApplied = true
        return isPromoApplied
    }

    private fun isTestingFlow(): Boolean {
        return arguments?.getBoolean(CartConstant.IS_TESTING_FLOW, false) ?: false
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

    private fun observeAddCartToWishlistEvent() {
        viewModel.addToWishlistV2Event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AddToWishlistV2Event.Success -> {
                    onAddCartToWishlistSuccess(
                        event.data.message,
                        event.productId,
                        event.isLastItem,
                        event.source,
                        event.wishlistIcon,
                        event.animatedWishlistImage
                    )
                }
                is AddToWishlistV2Event.Failed -> {
                    showToastMessageRed(event.throwable)
                }
            }
        }
    }

    private fun observeAddToCart() {
        viewModel.addToCartEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AddToCartEvent.Success -> {
                    if (event.addToCartDataModel.status.equals(
                            AddToCartDataModel.STATUS_OK,
                            true
                        ) && event.addToCartDataModel.data.success == 1
                    ) {
                        triggerSendEnhancedEcommerceAddToCartSuccess(
                            event.addToCartDataModel,
                            event.productModel
                        )
                        resetRecentViewList()
                        viewModel.processInitialGetCartData("0", false, false)
                        if (event.addToCartDataModel.data.message.size > 0) {
                            showToastMessageGreen(event.addToCartDataModel.data.message[0])
                            notifyBottomCartParent()
                        }
                    } else {
                        if (event.addToCartDataModel.errorMessage.size > 0) {
                            showToastMessageRed(event.addToCartDataModel.errorMessage[0])
                        }
                    }
                }

                is AddToCartEvent.Failed -> {
                    hideProgressLoading()
                    showToastMessageRed(event.throwable)
                }
            }
        }
    }

    private fun observeCartDataList() {
        viewModel.cartDataList.observe(viewLifecycleOwner) { data ->
            cartAdapter.updateList(data)
        }
    }

    private fun observeCartEvent() {
        viewModel.loadCartState.observe(viewLifecycleOwner) { state ->
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

    private fun observeCartTrackerEvent() {
        viewModel.cartTrackerEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CartTrackerEvent.ATCTrackingURLRecent -> {
                    sendATCTrackingURLRecent(event.productModel)
                }

                is CartTrackerEvent.ATCTrackingURLRecommendation -> {
                    sendATCTrackingURL(event.recommendationItem)
                }

                is CartTrackerEvent.ATCTrackingURLBanner -> {
                    sendATCTrackingURL(event.bannerShop)
                }
            }
        }
    }

    private fun observeCheckoutButton() {
        viewModel.cartCheckoutButtonState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartCheckoutButtonState.ENABLE -> onCartDataEnableToCheckout()
                CartCheckoutButtonState.DISABLE -> onCartDataDisableToCheckout()
            }
        }
    }

    private fun observeDeleteCartEvent() {
        viewModel.deleteCartEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DeleteCartEvent.Success -> {
                    event.apply {
                        renderLoadGetCartDataFinish()
                        onDeleteCartDataSuccess(
                            toBeDeletedCartIds,
                            removeAllItems,
                            forceExpandCollapsedUnavailableItems,
                            addWishList,
                            isFromGlobalCheckbox,
                            isFromEditBundle
                        )

                        val params = generateParamGetLastApplyPromo()
                        if (!removeAllItems && (isNeedHitUpdateCartAndValidateUse(params))) {
                            renderPromoCheckoutLoading()
                            viewModel.doUpdateCartAndGetLastApply(params)
                        }
                        viewModel.processUpdateCartCounter()
                    }
                }
                is DeleteCartEvent.Failed -> {
                    event.apply {
                        if (forceExpandCollapsedUnavailableItems) {
                            collapseOrExpandDisabledItem()
                        }
                        hideProgressLoading()
                        showToastMessageRed(throwable)
                    }
                }
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
            }
        }
    }

    private fun observeSelectedAmount() {
        viewModel.selectedAmountState.observe(viewLifecycleOwner) { selectedAmount ->
            if (selectedAmount != 0) {
                binding?.topLayout?.textSelectedAmount?.text = String.format(
                    getString(R.string.cart_label_selected_amount),
                    selectedAmount
                )
            }
            onNeedToUpdateViewItem(0)
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
            }
        }
    }

    private fun observeGlobalEvent() {
        viewModel.globalEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
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
                    updateCashback(event.subtotalCashback)
                    renderDetailInfoSubTotal(event.qty, event.subtotalPrice, event.noAvailableItems)
                }

                is CartGlobalEvent.AdapterItemChanged -> {
                    onNeedToUpdateViewItem(event.position)
                }

                is CartGlobalEvent.CheckGroupShopCartTicker -> {
                    checkCartShopGroupTicker(event.cartGroupHolderData)
                }

                is CartGlobalEvent.UpdateCartShopGroupTicker -> {
                    updateCartShopGroupTicker(event.cartGroupHolderData)
                }
            }
        }
    }

    private fun observeUpdateCartEvent() {
        viewModel.updateCartForCheckoutState.observe(viewLifecycleOwner) { data ->
            data?.let {
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
                }
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
            }
        }
    }

    private fun observeUndoDeleteEvent() {
        viewModel.undoDeleteEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                UndoDeleteEvent.Success -> {
                    hideProgressLoading()
                    onUndoDeleteCartDataSuccess()
                }
                is UndoDeleteEvent.Failed -> {
                    hideProgressLoading()
                    showToastMessageRed(event.throwable)
                }
            }
        }
    }

    private fun observeUpdateCartAndGetLastApply() {
        viewModel.updateCartAndGetLastApplyEvent.observe(viewLifecycleOwner) { data ->
            when (data) {
                is UpdateCartAndGetLastApplyEvent.Success -> {
                    updatePromoCheckoutStickyButton(data.promoUiModel)
                }
                is UpdateCartAndGetLastApplyEvent.Failed -> {
                    if (data.throwable is AkamaiErrorException) {
                        viewModel.doClearAllPromo()
                        if (!viewModel.cartModel.promoTicker.enable) {
                            showToastMessageRed(data.throwable)
                        }
                    }
                    renderPromoCheckoutButtonActiveDefault(emptyList())
                }
            }
        }
    }

    private fun onAddCartToWishlistSuccess(
        message: String,
        productId: String,
        isLastItem: Boolean,
        source: String,
        wishlistIcon: IconUnify,
        animatedWishlistImage: ImageView
    ) {
        animateWishlisted(message, wishlistIcon, animatedWishlistImage)

        when (source) {
            WISHLIST_SOURCE_AVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
            }

            WISHLIST_SOURCE_UNAVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistUnavailableSection(FLAG_IS_CART_EMPTY, productId)
            }
        }

//        val updateListResult = viewModel.removeProductByCartId(listOf(cartId), isLastItem, false)
//        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        setTopLayoutVisibility()

        if (isLastItem) {
            refreshCartWithSwipeToRefresh()
        } else {
            viewModel.setLastItemAlwaysSelected()
        }
    }

    private fun animateWishlisted(message: String, wishlistIcon: IconUnify, animatedWishlistImage: ImageView) {
        wishlistIcon.invisible()
        animatedWishlistImage.show()
        val animation: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.wishlist)
        if (animation != null) {
            animatedWishlistImage.background = animation
            animation.start()

            Handler(Looper.getMainLooper()).postDelayed({
                val inWishlistColor = ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
                wishlistIcon.setImage(
                    IconUnify.HEART_FILLED,
                    inWishlistColor,
                    inWishlistColor,
                    inWishlistColor,
                    inWishlistColor
                )
                animatedWishlistImage.gone()
                wishlistIcon.show()
            }, 1000L)
        }
        // TODO: remove toaster if no needed anymore
//        showToastMessageGreen(message)
        viewModel.processGetWishlistV2Data()
    }

    private fun onRemoveFromWishlistSuccess(wishlistIcon: IconUnify, position: Int) {
        val notInWishlistColor = ContextCompat.getColor(
            requireContext(),
            com.tokopedia.unifyprinciples.R.color.Unify_NN500
        )
        wishlistIcon.setImage(
            IconUnify.HEART,
            notInWishlistColor,
            notInWishlistColor,
            notInWishlistColor,
            notInWishlistColor
        )
        onNeedToUpdateViewItem(position)
    }

    private fun onClickChevronSummaryTransaction() {
        cartPageAnalytics.eventClickDetailTagihan(userSession.userId)
        showBottomSheetSummaryTransaction()
    }

    private fun onDeleteCartDataSuccess(
        deletedCartIds: List<String>,
        removeAllItems: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean,
        isMoveToWishlist: Boolean,
        isFromGlobalCheckbox: Boolean,
        isFromEditBundle: Boolean
    ) {
        var message =
            String.format(getString(R.string.message_product_already_deleted), deletedCartIds.size)

        if (isMoveToWishlist) {
            message = String.format(
                getString(R.string.message_product_already_moved_to_wishlist),
                deletedCartIds.size
            )
            refreshWishlistAfterItemRemoveAndMoveToWishlist()
        } else if (isFromEditBundle) {
            message = getString(R.string.message_toaster_cart_change_bundle_success)
        }

        if (isFromGlobalCheckbox || isFromEditBundle) {
            showToastMessageGreen(message)
        } else {
            showToastMessageGreen(
                message,
                getString(R.string.toaster_cta_cancel)
            ) { onUndoDeleteClicked(deletedCartIds) }
        }

        val needRefresh = removeAllItems || isFromEditBundle
        val updateListResult =
            viewModel.removeProductByCartId(deletedCartIds, needRefresh, isFromGlobalCheckbox)
        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        hideProgressLoading()

        setTopLayoutVisibility()

        when {
            removeAllItems -> {
                refreshCartWithSwipeToRefresh()
            }

            isFromEditBundle -> {
                refreshCartWithProgressDialog(ICartListPresenter.GET_CART_STATE_DEFAULT)
            }

            else -> {
                setLastItemAlwaysSelected()
            }
        }
    }

    private fun onUndoDeleteCartDataSuccess() {
        viewModel.processInitialGetCartData(getCartId(), false, false)
    }

    private fun onNavigationToolbarNavGlobalClicked() {
        cartPageAnalytics.eventClickTopNavMenuNavToolbar(userSession.userId)
    }

    private fun onNavigationToolbarWishlistClicked() {
        cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
        routeToWishlist()
    }

    private fun onNeedToInsertMultipleViewItem(positionStart: Int, itemCount: Int) {
        cartAdapter.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRangeInserted(positionStart, itemCount) }
        } else {
            cartAdapter.notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    private fun onNeedToRemoveMultipleViewItem(positionStart: Int, itemCount: Int) {
        cartAdapter.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRangeRemoved(positionStart, itemCount) }
        } else {
            cartAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    private fun onNeedToUpdateViewItem(position: Int) {
        cartAdapter.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (position == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemChanged(position) }
        } else {
            cartAdapter.notifyItemChanged(position)
        }
    }

    private fun onNeedToUpdateMultipleViewItem(positionStart: Int, count: Int) {
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRangeChanged(positionStart, count) }
        } else {
            cartAdapter.notifyItemRangeChanged(positionStart, count)
        }
    }

    private fun onProductClicked(productId: String) {
        routeToProductDetailPage(productId)
    }

    private fun onResultFromEditBundle(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val oldBundleId = data?.getStringExtra(CartFragment.KEY_OLD_BUNDLE_ID) ?: ""
            val newBundleId = data?.getStringExtra(CartFragment.KEY_NEW_BUNLDE_ID) ?: ""
            val isChangeVariant = data?.getBooleanExtra(CartFragment.KEY_IS_CHANGE_VARIANT, false) ?: false
            val toBeDeletedBundleGroupId = viewModel.cartModel.toBeDeletedBundleGroupId
            if (((oldBundleId.isNotBlank() && newBundleId.isNotBlank() && oldBundleId != newBundleId) || isChangeVariant) && toBeDeletedBundleGroupId.isNotEmpty()) {
                val cartItems =
                    cartAdapter.getCartItemByBundleGroupId(oldBundleId, toBeDeletedBundleGroupId)
                viewModel.cartModel.toBeDeletedBundleGroupId = ""
                if (cartItems.isNotEmpty()) {
                    val allCartItemDataList = viewModel.getAllCartItemData()
                    viewModel.processDeleteCartItem(
                        allCartItemData = allCartItemDataList,
                        removedCartItems = cartItems,
                        addWishList = false,
                        forceExpandCollapsedUnavailableItems = false,
                        isFromGlobalCheckbox = true,
                        isFromEditBundle = true
                    )
                }
            } else {
                refreshCartWithSwipeToRefresh()
            }
        }
    }

    private fun onResultFromPdp() {
        if (!isTestingFlow()) {
            refreshCartWithSwipeToRefresh()
        }
    }

    private fun onResultFromPromoPage(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<ValidateUsePromoRequest>(ARGS_LAST_VALIDATE_USE_REQUEST)?.let {
                viewModel.cartModel.lastValidateUseRequest = it
            }

            val validateUseUiModel = data?.getParcelableExtra<ValidateUsePromoRevampUiModel>(
                ARGS_VALIDATE_USE_DATA_RESULT
            )
            if (validateUseUiModel != null) {
                viewModel.validateBoPromo(validateUseUiModel)
                viewModel.cartModel.apply {
                    lastValidateUseResponse = validateUseUiModel
                    lastUpdateCartAndGetLastApplyResponse = null
                    isLastApplyResponseStillValid = false
                }
                updatePromoCheckoutStickyButton(validateUseUiModel.promoUiModel)
            }

            val clearPromoUiModel =
                data?.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)
            if (clearPromoUiModel != null) {
                if (validateUseUiModel == null) {
                    viewModel.cartModel.apply {
                        isLastApplyResponseStillValid = false
                        lastValidateUseResponse = null
                        lastUpdateCartAndGetLastApplyResponse = null
                    }
                }
                updatePromoCheckoutStickyButton(PromoUiModel(titleDescription = clearPromoUiModel.successDataModel.defaultEmptyPromoMessage))
            }
        }
    }

    private fun onResultFromShipmentPage(resultCode: Int, data: Intent?) {
        FLAG_BEGIN_SHIPMENT_PROCESS = false
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false

        when (resultCode) {
            CheckoutConstant.RESULT_CHECKOUT_CACHE_EXPIRED -> {
                val message =
                    data?.getStringExtra(CheckoutConstant.EXTRA_CACHE_EXPIRED_ERROR_MESSAGE)
                showToastMessageRed(message ?: "")
            }

            else -> {
                refreshCartWithProgressDialog(CartViewModel.GET_CART_STATE_DEFAULT)
            }
        }
    }

    private fun onUndoDeleteClicked(cartIds: List<String>) {
        cartPageAnalytics.eventClickUndoAfterDeleteProduct(userSession.userId)
        viewModel.processUndoDeleteCartItem(cartIds)
    }

    private fun refreshCartWithProgressDialog(getCartState: Int) {
        resetRecentViewList()
        if (viewModel.dataHasChanged()) {
            showMainContainer()
            viewModel.processToUpdateAndReloadCartData(getCartId(), getCartState)
        } else {
            viewModel.processInitialGetCartData(getCartId(), false, false, getCartState)
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

    private fun refreshWishlistAfterItemRemoveAndMoveToWishlist() {
        viewModel.processGetWishlistV2Data()
    }

    private fun reloadAppliedPromoFromGlobalCheck() {
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeLocalCartItem(
        updateListResult: Pair<List<Int>, List<Int>>,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        val updateIndices = updateListResult.second
        val removeIndices = updateListResult.first
//        if (removeIndices.size > 1) {
//            // on multiple deletion, notify data set changed to prevent indices race condition
//            cartAdapter.notifyDataSetChanged()
//        } else {
//            updateIndices.forEach {
//                onNeedToUpdateViewItem(it)
//            }
//            removeIndices.forEach {
//                onNeedToRemoveViewItem(it)
//            }
//        }

        // If action is on unavailable item, do collapse unavailable items if previously forced to expand (without user tap expand)
        if (viewModel.getAllDisabledCartItemData().size > 1) {
            if (forceExpandCollapsedUnavailableItems) {
                collapseOrExpandDisabledItem()
            }
        } else {
            viewModel.removeAccordionDisabledItem()
        }

        val allShopGroupDataList = viewModel.getAllShopGroupDataList()

        // Check if cart list has exactly 1 shop, and it's a toko now
        if (allShopGroupDataList.size == 1 && allShopGroupDataList[0].isTokoNow) {
            allShopGroupDataList[0].let {
                val (index, groupData) = cartAdapter.getCartGroupHolderDataAndIndexByCartString(
                    it.cartString,
                    it.isError
                )
                if (index != RecyclerView.NO_POSITION) {
                    if (it.isCollapsed) {
                        viewModel.addItems(index + 1, it.productUiModelList)
                    }
                    it.isCollapsible = false
                    it.isCollapsed = false
                    it.isShowPin = false
//                    onNeedToUpdateViewItem(index)
//                    if (groupData.last() is CartShopBottomHolderData) {
//                        val bottomIndex = index + groupData.lastIndex
//                        cartAdapter.getData()[bottomIndex] = CartShopBottomHolderData(it)
//                        onNeedToUpdateViewItem(bottomIndex)
//                    }
                }
            }
        }

        // Check if cart list has more than 1 shop, and first shop is toko now
        if (allShopGroupDataList.size > 1 && allShopGroupDataList[0].isTokoNow) {
            allShopGroupDataList[0].let {
                if (it.productUiModelList.size == 1) {
                    val (index, groupData) = cartAdapter.getCartGroupHolderDataAndIndexByCartString(
                        it.cartString,
                        it.isError
                    )
                    if (index != RecyclerView.NO_POSITION) {
                        if (it.isCollapsed) {
                            viewModel.addItems(index + 1, it.productUiModelList)
                        }
                        it.isCollapsible = false
                        it.isCollapsed = false
//                        onNeedToUpdateViewItem(index)
//                        if (groupData.last() is CartShopBottomHolderData) {
//                            val bottomIndex = index + groupData.lastIndex
//                            cartAdapter.getData()[bottomIndex] = CartShopBottomHolderData(it)
//                            onNeedToUpdateViewItem(bottomIndex)
//                        }
                    }
                }
            }
        }

        // use notify data set changed due to massive update
        viewModel.cartDataList.notifyObserver()

        viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
        notifyBottomCartParent()
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

    private fun renderDetailInfoSubTotal(
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

        renderSelectedAmount()
        setInitialCheckboxGlobalState(cartData)
        setSelectedAmountVisibility()

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

    private fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>) {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.ACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title =
                getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
            promoCheckoutBtnCart.desc = ""
            promoCheckoutBtnCart.setOnClickListener {
                checkGoToPromo()
                // analytics
                PromoRevampAnalytics.eventCartClickPromoSection(
                    listPromoApplied,
                    false,
                    userSession.userId
                )
            }
        }
    }

    private fun renderPromoCheckoutButtonNoItemIsSelected() {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.ACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title =
                getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
            promoCheckoutBtnCart.desc = getString(R.string.promo_desc_no_selected_item)
            promoCheckoutBtnCart.setOnClickListener {
                showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
            }
        }
    }

    private fun renderPromoCheckoutLoading() {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.LOADING
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.setOnClickListener { }
        }
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
        viewModel.cartModel.recentViewList = cartRecentViewItemHolderDataList
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

        viewModel.cartModel.recommendationPage = viewModel.cartModel.recommendationPage + 1
    }

    private fun renderSelectedAmount() {
//        if (viewModel.hasSelectedCartItem()) {
        initialSelectedAmountPosition = 0f
        viewModel.addItems(0, listOf(CartSelectedAmountHolderData()))
//        }
        viewModel.updateSelectedAmount()
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
            onCartDataDisableToCheckout()
        } else {
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
            val wishlistIndex = viewModel.updateCartWishlistData(cartWishlistHolderData)
            onNeedToUpdateViewItem(wishlistIndex)
        }

        viewModel.cartModel.wishlists = cartWishlistItemHolderDataList
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

    private fun routeToApplink(appLink: String) {
        activity?.let {
            RouteManager.route(it, appLink)
        }
    }

    private fun routeToCheckoutPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.CHECKOUT)
            intent.putExtra(
                CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE,
                CheckoutConstant.CHECKOUT_PAGE_SOURCE_CART
            )
            val shipmentActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    onResultFromShipmentPage(result.resultCode, result.data)
                }
            shipmentActivityResult.launch(intent)
        }
    }

    private fun routeToHome() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
        }
    }

    private fun routeToProductDetailPage(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            val pdpActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    onResultFromPdp()
                }
            pdpActivityResult.launch(intent)
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

            val promoActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    onResultFromPromoPage(result.resultCode, result.data)
                }
            promoActivityResult.launch(intent)
        }
    }

    private fun routeToShopPage(shopId: String?) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SHOP, shopId)
            startActivityWithRefreshHandler(intent)
        }
    }

    private fun routeToTokoNowHomePage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalTokopediaNow.HOME)
            startActivityWithRefreshHandler(intent)
        }
    }

    private fun routeToWishlist() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.NEW_WISHLIST)
            startActivityWithRefreshHandler(intent)
        }
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

    private fun sendATCTrackingURLRecent(productModel: CartRecentViewItemHolderData) {
        val productId = productModel.id
        val productName = productModel.name
        val imageUrl = productModel.imageUrl
        val url = "${productModel.clickUrl}&click_source=ATC_direct_click"
        activity?.let {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                url,
                productId,
                productName,
                imageUrl
            )
        }
    }

    private fun sendATCTrackingURL(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl
        val url = "${recommendationItem.clickUrl}&click_source=ATC_direct_click"

        activity?.let {
            TopAdsUrlHitter(CartRevampFragment::class.qualifiedName).hitClickUrl(
                it,
                url,
                productId,
                productName,
                imageUrl
            )
        }
    }

    private fun sendATCTrackingURL(bannerShopProductUiModel: BannerShopProductUiModel) {
        val productId = bannerShopProductUiModel.productId.toString()
        val productName = bannerShopProductUiModel.productName
        val imageUrl = bannerShopProductUiModel.imageUrl
        val url = "${bannerShopProductUiModel.adsClickUrl}&click_source=ATC_direct_click"

        activity?.let {
            TopAdsUrlHitter(CartRevampFragment::class.qualifiedName).hitClickUrl(
                it,
                url,
                productId,
                productName,
                imageUrl
            )
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

    private fun setInitialCheckboxGlobalState(cartData: CartData) {
        binding?.checkboxGlobal?.isChecked = cartData.isGlobalCheckboxState
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

    private fun setLastItemAlwaysSelected() {
        val tmpIsLastItem = viewModel.setLastItemAlwaysSelected()
        if (tmpIsLastItem) {
            binding?.checkboxGlobal?.isChecked = true
        }
    }

    private fun setSelectedAmountVisibility() {
        if (viewModel.hasSelectedCartItem()) {
            binding?.rlTopLayout?.visible()
        } else {
            binding?.rlTopLayout?.invisible()
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
            rlTopLayout.post {
                if (initialSelectedAmountPosition == 0f) {
                    initialSelectedAmountPosition = navToolbar.y
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

    private fun setCheckboxGlobalState() {
        isCheckUncheckDirectAction = false
        val isAllAvailableItemCheked = cartAdapter.isAllAvailableItemCheked()
        if (binding?.checkboxGlobal?.isChecked == isAllAvailableItemCheked) {
            isCheckUncheckDirectAction = true
        }
        binding?.checkboxGlobal?.isChecked = isAllAvailableItemCheked
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
//        if (binding?.rlTopLayout?.visibility == View.VISIBLE) {
//            if (show) {
//                binding?.topLayoutShadow?.show()
//                binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
//            } else {
//                binding?.topLayoutShadow?.gone()
//            }
//        } else {
//            if (show) {
//                binding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
//                binding?.topLayoutShadow?.gone()
//            } else {
//                binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
//            }
//        }
    }

    private fun setTopLayoutVisibility() {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (viewModel.hasAvailableItemLeft()) {
            binding?.topLayout?.root?.show()
            if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                isShowToolbarShadow = true
            }
        } else {
            binding?.topLayout?.root?.gone()
        }

        setToolbarShadowVisibility(isShowToolbarShadow)
    }

    private fun setTopLayoutVisibility(isShow: Boolean) {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (isShow) {
            binding?.rlTopLayout?.show()
            if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                isShowToolbarShadow = true
            }
        } else {
            binding?.rlTopLayout?.invisible()
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

    private fun startActivityWithRefreshHandler(intent: Intent) {
        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                refreshCartWithSwipeToRefresh()
            }
        activityResultLauncher.launch(intent)
    }

    private fun triggerSendEnhancedEcommerceAddToCartSuccess(
        addToCartDataResponseModel: AddToCartDataModel,
        productModel: Any
    ) {
        var stringObjectMap: Map<String, Any>? = null
        var eventCategory = ""
        var eventAction = ""
        var eventLabel = ""
        when (productModel) {
            is CartWishlistItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_WISHLIST
                eventLabel = ""
                stringObjectMap = viewModel.generateAddToCartEnhanceEcommerceDataLayer(
                    productModel,
                    addToCartDataResponseModel,
                    FLAG_IS_CART_EMPTY
                )
            }

            is CartRecentViewItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction =
                    ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_RECENT_VIEW_PAGE
                eventLabel = ""
                stringObjectMap = viewModel.generateAddToCartEnhanceEcommerceDataLayer(
                    productModel,
                    addToCartDataResponseModel,
                    FLAG_IS_CART_EMPTY
                )
            }

            is CartRecommendationItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART
                eventLabel = ""
                stringObjectMap = viewModel.generateAddToCartEnhanceEcommerceDataLayer(
                    productModel,
                    addToCartDataResponseModel,
                    FLAG_IS_CART_EMPTY
                )
            }
        }

        stringObjectMap?.let {
            cartPageAnalytics.sendEnhancedECommerceAddToCart(
                stringObjectMap,
                eventCategory,
                eventAction,
                eventLabel
            )
        }
    }

    private fun updateCartCounter(counter: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (counter > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, counter)
        cache.applyEditor()
    }

    fun updateCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        // TODO: change logic
        val (index, groupData) = cartAdapter.getCartGroupHolderDataAndIndexByCartString(cartGroupHolderData.cartString, cartGroupHolderData.isError)
        if (index >= 0) {
            onNeedToUpdateViewItem(index + groupData.lastIndex)
        }
    }

    private fun updateCashback(cashback: Double) {
        val result = updateShipmentSellerCashback(cashback)
        result?.let {
            when (result.first) {
                CartAdapter.SELLER_CASHBACK_ACTION_INSERT -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        cartAdapter.notifyItemInserted(result.second)
                    }
                }

                CartAdapter.SELLER_CASHBACK_ACTION_UPDATE -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        onNeedToUpdateViewItem(result.second)
                    }
                }

                CartAdapter.SELLER_CASHBACK_ACTION_DELETE -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        cartAdapter.notifyItemRemoved(result.second)
                    }
                }
            }
        }
    }

    private fun updatePromoCheckoutManualIfNoSelected(listPromoApplied: List<String>) {
        if (viewModel.getSelectedCartGroupHolderData().isEmpty()) {
            renderPromoCheckoutButtonNoItemIsSelected()
        } else {
            renderPromoCheckoutButtonActiveDefault(listPromoApplied)
        }
    }

    private fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel) {
        val lastApplyUiModel =
            LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
        renderPromoCheckoutButton(lastApplyUiModel)
        viewModel.updatePromoSummaryData(lastApplyUiModel)
        if (promoUiModel.globalSuccess) {
            viewModel.cartModel.lastValidateUseResponse =
                ValidateUsePromoRevampUiModel(promoUiModel = promoUiModel)
        }
    }

    private fun updateShipmentSellerCashback(cashback: Double): Pair<Int, Int>? {
        if (cashback > 0) {
            if (viewModel.cartModel.shipmentSellerCashbackModel == null || viewModel.cartDataList.value.indexOf(
                    viewModel.cartModel.shipmentSellerCashbackModel!!
                ) == -1
            ) {
                var index = 0
                for (item in viewModel.cartDataList.value) {
                    if (item is CartGroupHolderData) {
                        index = viewModel.cartDataList.value.indexOf(item)
                    }
                }
                viewModel.cartModel.shipmentSellerCashbackModel = ShipmentSellerCashbackModel()
                viewModel.cartModel.shipmentSellerCashbackModel?.let {
                    it.sellerCashback = cashback.toInt()
                    it.isVisible = true
                    it.sellerCashbackFmt =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                            .removeDecimalSuffix()
                    viewModel.cartDataList.value.add(++index, it)
                    return Pair(CartAdapter.SELLER_CASHBACK_ACTION_INSERT, index)
                }
            } else {
                viewModel.cartModel.shipmentSellerCashbackModel?.let {
                    it.sellerCashback = cashback.toInt()
                    it.isVisible = true
                    it.sellerCashbackFmt =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                            .removeDecimalSuffix()
                    val index = viewModel.cartDataList.value.indexOf(it)
                    return Pair(CartAdapter.SELLER_CASHBACK_ACTION_UPDATE, index)
                }
            }
        } else {
            viewModel.cartModel.shipmentSellerCashbackModel?.let {
                val index = viewModel.cartDataList.value.indexOf(it)
                viewModel.cartDataList.value.remove(it)
                viewModel.cartModel.shipmentSellerCashbackModel = null
                return Pair(CartAdapter.SELLER_CASHBACK_ACTION_DELETE, index)
            }
        }

        return null
    }

    private fun updateStateAfterCheckChanged() {
        viewModel.cartModel.hasPerformChecklistChange = true
        viewModel.reCalculateSubTotal(viewModel.getAllAvailableShopGroupDataList())
        setCheckboxGlobalState()
        setSelectedAmountVisibility()
        validateGoToCheckout()
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
        viewModel.saveCheckboxState(viewModel.getAllAvailableCartItemHolderData())
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

    private fun CompoundButton.checks(): Flow<Boolean> = callbackFlow {
        setOnCheckedChangeListener { _, isChecked ->
            trySend(isChecked).isSuccess
        }
        awaitClose { setOnCheckedChangeListener(null) }
    }
}
