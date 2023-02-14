package com.tokopedia.cart.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.LocalizationChooseAddress
import com.tokopedia.cart.databinding.FragmentCartBinding
import com.tokopedia.cart.view.CartActivity.Companion.INVALID_PRODUCT_ID
import com.tokopedia.cart.view.ICartListPresenter.Companion.GET_CART_STATE_AFTER_CHOOSE_ADDRESS
import com.tokopedia.cart.view.ICartListPresenter.Companion.GET_CART_STATE_DEFAULT
import com.tokopedia.cart.view.adapter.cart.CartAdapter
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.bottomsheet.CartBundlingBottomSheet
import com.tokopedia.cart.view.bottomsheet.CartBundlingBottomSheetListener
import com.tokopedia.cart.view.bottomsheet.showGlobalErrorBottomsheet
import com.tokopedia.cart.view.bottomsheet.showSummaryTransactionBottomsheet
import com.tokopedia.cart.view.compoundview.CartToolbar
import com.tokopedia.cart.view.compoundview.CartToolbarListener
import com.tokopedia.cart.view.compoundview.CartToolbarView
import com.tokopedia.cart.view.compoundview.CartToolbarWithBackView
import com.tokopedia.cart.view.decorator.CartItemDecoration
import com.tokopedia.cart.view.di.DaggerCartComponent
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.mapper.PromoRequestMapper
import com.tokopedia.cart.view.mapper.RecentViewMapper
import com.tokopedia.cart.view.mapper.WishlistMapper
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cart.view.uimodel.CartSelectAllHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.cartcommon.data.response.common.Button.Companion.ID_HOMEPAGE
import com.tokopedia.cartcommon.data.response.common.Button.Companion.ID_RETRY
import com.tokopedia.cartcommon.data.response.common.Button.Companion.ID_START_SHOPPING
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.common.OutOfService.Companion.ID_MAINTENANCE
import com.tokopedia.cartcommon.data.response.common.OutOfService.Companion.ID_OVERLOAD
import com.tokopedia.cartcommon.data.response.common.OutOfService.Companion.ID_TIMEOUT
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
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
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.constant.CartConstant.IS_TESTING_FLOW
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
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementActionListener
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.rxCompoundButtonCheckDebounce
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
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rx.Emitter
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Keep
class CartFragment :
    BaseCheckoutFragment(),
    ICartListView,
    ActionListener,
    CartItemAdapter.ActionListener,
    RefreshHandler.OnRefreshHandlerListener,
    CartToolbarListener,
    TickerAnnouncementActionListener,
    SellerCashbackListener,
    CartBundlingBottomSheetListener {

    private var binding by autoClearedNullable<FragmentCartBinding>()

    lateinit var toolbar: CartToolbar

    @Inject
    lateinit var dPresenter: ICartListPresenter

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
    lateinit var compositeSubscription: CompositeSubscription

    // RxJava Emitter to trigger update tokonow only products
    private var tokoNowProductUpdater: Emitter<Boolean>? = null

    lateinit var cartAdapter: CartAdapter
    private var refreshHandler: RefreshHandler? = null
    private var progressDialog: AlertDialog? = null

    private var cartPerformanceMonitoring: PerformanceMonitoring? = null
    private var isTraceCartStopped: Boolean = false
    private var cartAllPerformanceMonitoring: PerformanceMonitoring? = null
    private var isTraceCartAllStopped: Boolean = false

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var hasLoadRecommendation: Boolean = false

    private var saveInstanceCacheManager: SaveInstanceCacheManager? = null

    private var wishLists: List<CartWishlistItemHolderData>? = null
    private var recentViewList: List<CartRecentViewItemHolderData>? = null

    private var recommendationWishlistV2ActionListener: WishlistV2ActionListener? = null
    private var cartUnavailableWishlistV2ActionListener: WishlistV2ActionListener? = null
    private var lastSeenWishlistV2ActionListener: WishlistV2ActionListener? = null
    private var wishlistsWishlistV2ActionListener: WishlistV2ActionListener? = null

    private var hasTriedToLoadWishList: Boolean = false
    private var hasTriedToLoadRecentViewList: Boolean = false
    private var shouldReloadRecentViewList: Boolean = false
    private var hasTriedToLoadRecommendation: Boolean = false
    private var isToolbarWithBackButton = true
    private var delayShowPromoButtonJob: Job? = null
    private var TRANSLATION_LENGTH = 0f
    private var isKeyboardOpened = false
    private var initialPromoButtonPosition = 0f
    private var recommendationPage = 1
    private var unavailableItemAccordionCollapseState = true
    private var hasCalledOnSaveInstanceState = false
    private var isCheckUncheckDirectAction = true
    private var isNavToolbar = false

    // temporary variable to handle case edit bundle
    // this is useful if there are multiple same bundleId in cart
    private var toBeDeletedBundleGroupId = ""

    companion object {

        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

        private const val TOKONOW_UPDATER_DEBOUNCE = 500L

        private const val TOKONOW_SEE_OTHERS_OR_ALL_LIMIT = 10

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
        const val ADVERTISINGID = "ADVERTISINGID"
        const val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
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
        fun newInstance(bundle: Bundle?, args: String): CartFragment {
            var tmpBundle = bundle
            if (tmpBundle == null) {
                tmpBundle = Bundle()
            }
            tmpBundle.putString(CartFragment::class.java.simpleName, args)
            val fragment = CartFragment()
            fragment.arguments = tmpBundle
            return fragment
        }
    }

    // Lifecycle Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            saveInstanceCacheManager = SaveInstanceCacheManager(it, savedInstanceState)

            if (savedInstanceState != null) {
                loadCachedData()
            } else {
                EmbraceMonitoring.startMoments(EmbraceKey.KEY_MP_CART)
                EmbraceMonitoring.startMoments(EmbraceKey.KEY_MP_CART_INCOMPLETE)
                cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE)
                cartAllPerformanceMonitoring = PerformanceMonitoring.start(CART_ALL_TRACE)
            }
        }

        initRemoteConfig()

        dPresenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
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
                if (productId == INVALID_PRODUCT_ID) {
                    showToastMessageRed(MessageErrorException(AtcConstant.ATC_ERROR_GLOBAL))
                    refreshCartWithSwipeToRefresh()
                } else {
                    addToCartExternal(productId)
                }
            } else {
                refreshCartWithSwipeToRefresh()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if currently not refreshing, not ATC external flow and not on error state
        if (refreshHandler?.isRefreshing == false && !isAtcExternalFlow() && binding?.layoutGlobalError?.visibility != View.VISIBLE) {
            if (!::cartAdapter.isInitialized || (::cartAdapter.isInitialized && cartAdapter.itemCount == 0)) {
                dPresenter.processInitialGetCartData(
                    getCartId(),
                    dPresenter.getCartListData() == null,
                    true
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hasCalledOnSaveInstanceState = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hasCalledOnSaveInstanceState = true
        saveInstanceCacheManager?.onSave(outState)
        wishLists.let {
            saveInstanceCacheManager?.put(
                CartWishlistItemHolderData::class.java.simpleName,
                wishLists
            )
        }
        recentViewList?.let {
            saveInstanceCacheManager?.put(
                CartRecentViewItemHolderData::class.java.simpleName,
                recentViewList
            )
        }
    }

    override fun onStop() {
        updateCartAfterDetached()

        if (FLAG_SHOULD_CLEAR_RECYCLERVIEW) {
            clearRecyclerView()
        }

        super.onStop()
    }

    override fun onDetach() {
        tokoNowProductUpdater = null
        compositeSubscription.unsubscribe()
        super.onDetach()
    }

    override fun onDestroy() {
        cartAdapter.clearCompositeSubscription()
        dPresenter.detachView()
        delayShowPromoButtonJob?.cancel()
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            setActivityBackgroundColor()
            refreshHandler?.isRefreshing = true
            if (dPresenter.getCartListData() == null) {
                dPresenter.processInitialGetCartData(getCartId(), true, false)
            } else {
                if (dPresenter.dataHasChanged()) {
                    dPresenter.processToUpdateAndReloadCartData(getCartId())
                } else {
                    dPresenter.processInitialGetCartData(getCartId(), false, true)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            NAVIGATION_SHIPMENT -> onResultFromShipmentPage(resultCode, data)
            NAVIGATION_PDP -> onResultFromPdp()
            NAVIGATION_PROMO -> onResultFromPromoPage(resultCode, data)
            NAVIGATION_SHOP_PAGE -> refreshCartWithSwipeToRefresh()
            NAVIGATION_WISHLIST -> refreshCartWithSwipeToRefresh()
            NAVIGATION_TOKONOW_HOME_PAGE -> refreshCartWithSwipeToRefresh()
            NAVIGATION_EDIT_BUNDLE -> onResultFromEditBundle(resultCode, data)
            NAVIGATION_VERIFICATION -> refreshCartWithSwipeToRefresh()
            NAVIGATION_APPLINK -> refreshCartWithSwipeToRefresh()
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
                refreshCartWithProgressDialog(GET_CART_STATE_DEFAULT)
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
                dPresenter.setLastValidateUseRequest(it)
            }

            val validateUseUiModel = data?.getParcelableExtra<ValidateUsePromoRevampUiModel>(
                ARGS_VALIDATE_USE_DATA_RESULT
            )
            if (validateUseUiModel != null) {
                dPresenter.validateBoPromo(validateUseUiModel)
                dPresenter.setValidateUseLastResponse(validateUseUiModel)
                dPresenter.setUpdateCartAndValidateUseLastResponse(null)
                dPresenter.setLastApplyNotValid()
                updatePromoCheckoutStickyButton(validateUseUiModel.promoUiModel)
            }

            val clearPromoUiModel =
                data?.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)
            if (clearPromoUiModel != null) {
                if (validateUseUiModel == null) {
                    dPresenter.setLastApplyNotValid()
                    dPresenter.setValidateUseLastResponse(null)
                    dPresenter.setUpdateCartAndValidateUseLastResponse(null)
                }
                updatePromoCheckoutStickyButton(PromoUiModel(titleDescription = clearPromoUiModel.successDataModel.defaultEmptyPromoMessage))
            }
        }
    }

    private fun onResultFromEditBundle(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val oldBundleId = data?.getStringExtra(KEY_OLD_BUNDLE_ID) ?: ""
            val newBundleId = data?.getStringExtra(KEY_NEW_BUNLDE_ID) ?: ""
            val isChangeVariant = data?.getBooleanExtra(KEY_IS_CHANGE_VARIANT, false) ?: false
            if (((oldBundleId.isNotBlank() && newBundleId.isNotBlank() && oldBundleId != newBundleId) || isChangeVariant) && toBeDeletedBundleGroupId.isNotEmpty()) {
                val cartItems =
                    cartAdapter.getCartItemByBundleGroupId(oldBundleId, toBeDeletedBundleGroupId)
                toBeDeletedBundleGroupId = ""
                if (cartItems.isNotEmpty()) {
                    val allCartItemDataList = cartAdapter.allCartItemData
                    dPresenter.processDeleteCartItem(
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

    // Initialization Section

    override fun initInjector() {
        activity?.let {
            val baseMainApplication = it.application as BaseMainApplication
            DaggerCartComponent.builder().baseAppComponent(baseMainApplication.baseAppComponent)
                .build().inject(this)
        }
        cartAdapter = CartAdapter(this, this, this, this, userSession)
    }

    private fun initRemoteConfig() {
        isNavToolbar = isNavRevamp()
    }

    private fun isNavRevamp(): Boolean = true

    override fun initView(view: View) {
        initToolbar(view)

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

    private fun initViewListener() {
        binding?.apply {
            goToCourierPageButton.setOnClickListener { checkGoToShipment("") }
            imgChevronSummary.setOnClickListener { onClickChevronSummaryTransaction() }
            textTotalPaymentLabel.setOnClickListener { onClickChevronSummaryTransaction() }
            tvTotalPrices.setOnClickListener { onClickChevronSummaryTransaction() }
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

// Navigation Section

    private fun routeToHome() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
        }
    }

    private fun routeToWishlist() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.NEW_WISHLIST)
            startActivityForResult(intent, NAVIGATION_WISHLIST)
        }
    }

    private fun routeToProductDetailPage(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            startActivityForResult(intent, NAVIGATION_PDP)
        }
    }

    private fun routeToShopPage(shopId: String?) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SHOP, shopId)
            startActivityForResult(intent, NAVIGATION_SHOP_PAGE)
        }
    }

    private fun routeToShopProductPage(shopId: String?) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SHOP_PRODUCT, shopId)
            startActivityForResult(intent, NAVIGATION_SHOP_PAGE)
        }
    }

    private fun routeToTokoNowHomePage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalTokopediaNow.HOME)
            startActivityForResult(intent, NAVIGATION_TOKONOW_HOME_PAGE)
        }
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

    private fun routeToPromoCheckoutMarketplacePage() {
        activity?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
            val promoRequest = generateParamsCouponList()
            val validateUseRequest = generateParamValidateUsePromoRevamp()
            intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CART)
            intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
            intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequest)

            startActivityForResult(intent, NAVIGATION_PROMO)
        }
    }

    private fun routeToApplink(appLink: String) {
        activity?.let {
            RouteManager.route(it, appLink)
        }
    }

    private fun routeToApplinkWithResult(appLink: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, appLink)
            startActivityForResult(intent, NAVIGATION_APPLINK)
        }
    }

    private fun loadCachedData() {
        wishLists = saveInstanceCacheManager?.get<List<CartWishlistItemHolderData>>(
            CartWishlistItemHolderData::class.java.simpleName,
            object : TypeToken<ArrayList<CartWishlistItemHolderData>>() {}.type,
            null
        )
        recentViewList = saveInstanceCacheManager?.get<List<CartRecentViewItemHolderData>>(
            CartRecentViewItemHolderData::class.java.simpleName,
            object : TypeToken<ArrayList<CartRecentViewItemHolderData>>() {}.type,
            null
        )
    }

    override fun onBackPressed() {
        if (isNavToolbar) {
            cartPageAnalytics.eventClickBackNavToolbar(userSession.userId)
        } else {
            cartPageAnalytics.eventClickAtcCartClickArrowBack()
        }

        if (isAtcExternalFlow()) {
            routeToHome()
        }
        activity?.finish()
    }

    override fun onWishlistClicked() {
        cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
        routeToWishlist()
    }

    private fun updateCartAfterDetached() {
        val hasChanges = dPresenter.dataHasChanged()
        try {
            val cartItemDataList = getAllAvailableCartDataList()
            activity?.let {
                if (hasChanges && cartItemDataList.isNotEmpty() && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                    dPresenter.processUpdateCartData(true)
                }
            }
        } catch (e: IllegalStateException) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                e.printStackTrace()
            }
        }
    }

    override fun getOptionsMenuEnable(): Boolean {
        return true
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

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_cart
    }

    private fun onClickChevronSummaryTransaction() {
        cartPageAnalytics.eventClickDetailTagihan(userSession.userId)
        showBottomSheetSummaryTransaction()
    }

    private fun showBottomSheetSummaryTransaction() {
        if (!hasCalledOnSaveInstanceState) {
            context?.let { context ->
                fragmentManager?.let { fragmentManager ->
                    val promoSummaryUiModel = dPresenter.getPromoSummaryUiModel()
                    dPresenter.getSummaryTransactionUiModel()?.let { summaryTransactionUiModel ->
                        showSummaryTransactionBottomsheet(
                            summaryTransactionUiModel,
                            promoSummaryUiModel,
                            fragmentManager,
                            context
                        )
                    }
                }
            }
        }
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

    private fun handleStickyCheckboxGlobalVisibility(recyclerView: RecyclerView) {
        val topItemPosition =
            (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        if (topItemPosition == RecyclerView.NO_POSITION) return

        val adapterData = cartAdapter.getData()
        if (topItemPosition >= adapterData.size) return

        val firstVisibleItemData = adapterData[topItemPosition]
        if (firstVisibleItemData is CartSelectAllHolderData || firstVisibleItemData is TickerAnnouncementHolderData || firstVisibleItemData is CartChooseAddressHolderData || firstVisibleItemData is CartItemTickerErrorHolderData || firstVisibleItemData is CartShopHolderData || firstVisibleItemData is ShipmentSellerCashbackModel) {
            if (!cartAdapter.allAvailableCartItemData.isEmpty()) {
                if (binding?.topLayout?.root?.visibility == View.GONE) setTopLayoutVisibility(true)
            }
        } else {
            if (binding?.topLayout?.root?.visibility == View.VISIBLE) setTopLayoutVisibility(false)
        }
    }

    private fun setSpanSize(gridLayoutManager: GridLayoutManager) {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position != RecyclerView.NO_POSITION) {
                    if (position < cartAdapter.itemCount && cartAdapter.getItemViewType(position) == CartRecommendationViewHolder.LAYOUT) {
                        1
                    } else {
                        2
                    }
                } else {
                    0
                }
            }
        }
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

    private fun animatePromoButtonToStartingPosition() {
        binding?.apply {
            val initialPosition =
                bottomLayout.y - llPromoCheckout.height + PROMO_POSITION_BUFFER.dpToPx(resources.displayMetrics)
            llPromoCheckout.animate().y(initialPosition).setDuration(0).start()
        }
    }

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun initToolbar(view: View) {
        if (isNavToolbar) {
            initNavigationToolbar(view)
            binding?.toolbarCart?.gone()
            binding?.navToolbar?.show()
        } else {
            initBasicToolbar(view)
            binding?.navToolbar?.gone()
            binding?.toolbarCart?.show()
        }
        setToolbarShadowVisibility(false)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun initNavigationToolbar(view: View) {
        activity?.let {
            val statusBarBackground = binding?.statusBarBg

            val args = arguments?.getString(CartFragment::class.java.simpleName)
            if (args?.isNotEmpty() == true) {
                isToolbarWithBackButton = false
            }

            if (isToolbarWithBackButton) {
                statusBarBackground?.hide()
            } else {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        statusBarBackground?.visibility = View.INVISIBLE
                    }
                    else -> {
                        statusBarBackground?.show()
                    }
                }
            }

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

                if (isToolbarWithBackButton) {
                    setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                } else {
                    setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
                }
            }
        }
    }

    private fun onNavigationToolbarWishlistClicked() {
        cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
        routeToWishlist()
    }

    private fun onNavigationToolbarNavGlobalClicked() {
        cartPageAnalytics.eventClickTopNavMenuNavToolbar(userSession.userId)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun initBasicToolbar(view: View) {
        activity?.let {
            val args = arguments?.getString(CartFragment::class.java.simpleName)
            if (args?.isNotEmpty() == true) {
                isToolbarWithBackButton = false
            }

            val appbar = binding?.toolbarCart
            val statusBarBackground = binding?.statusBarBg
            statusBarBackground?.layoutParams?.height = DisplayMetricUtils.getStatusBarHeight(it)

            if (isToolbarWithBackButton) {
                toolbar = toolbarRemoveWithBackView() as CartToolbar
                statusBarBackground?.hide()
            } else {
                toolbar = toolbarRemoveView() as CartToolbar
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        statusBarBackground?.visibility = View.INVISIBLE
                    }
                    else -> {
                        statusBarBackground?.show()
                    }
                }
            }
            toolbar.let {
                appbar?.addView(toolbar as View)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
            }
        }
    }

    private fun toolbarRemoveWithBackView(): CartToolbarWithBackView? {
        activity?.let {
            return CartToolbarWithBackView(it).apply {
                listener = this@CartFragment
            }
        }

        return null
    }

    private fun toolbarRemoveView(): CartToolbarView? {
        activity?.let {
            return CartToolbarView(it).apply {
                listener = this@CartFragment
            }
        }

        return null
    }

    private fun initTopLayout() {
        binding?.topLayout?.checkboxGlobal?.let {
            compositeSubscription.add(
                rxCompoundButtonCheckDebounce(it, DELAY_CHECK_BOX_GLOBAL).subscribe(object :
                        Subscriber<Boolean>() {
                        override fun onNext(isChecked: Boolean) {
                            handleCheckboxGlobalChangeEvent()
                        }

                        override fun onCompleted() {
                            // no-op
                        }

                        override fun onError(e: Throwable?) {
                            // no-op
                        }
                    })
            )
        }

        binding?.topLayout?.textActionDelete?.setOnClickListener {
            onGlobalDeleteClicked()
        }
    }

    private fun handleCheckboxGlobalChangeEvent() {
        val isChecked = binding?.topLayout?.checkboxGlobal?.isChecked ?: return
        if (isCheckUncheckDirectAction) {
            cartAdapter.setAllAvailableItemCheck(isChecked)
            dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
            dPresenter.saveCheckboxState(cartAdapter.allAvailableCartItemHolderData)
            setGlobalDeleteVisibility()
            cartPageAnalytics.eventCheckUncheckGlobalCheckbox(isChecked)

            reloadAppliedPromoFromGlobalCheck()
        }
        isCheckUncheckDirectAction = true
    }

    private fun reloadAppliedPromoFromGlobalCheck() {
        val params = generateParamValidateUsePromoRevamp()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
    }

    private fun checkGoToShipment(message: String?) {
        if (message.isNullOrEmpty()) {
            var hasRedStatePromo = false
            val redStateGlobalPromo = ArrayList<String>()
            val clearOrders = ArrayList<ClearPromoOrder>()
            val cartListData = dPresenter.getCartListData()
            if (dPresenter.isLastApplyValid()) {
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
                                    cartListData.availableSection.availableGroupGroups.find { group -> group.cartString == voucher.uniqueId }
                                availableGroup?.let { availableGroup ->
                                    clearOrders.add(
                                        ClearPromoOrder(
                                            uniqueId = voucher.uniqueId,
                                            boType = availableGroup.boMetadata.boType,
                                            codes = arrayListOf(voucher.code),
                                            shopId = availableGroup.shop.shopId.toLongOrZero(),
                                            warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                            isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                            poDuration = availableGroup.cartDetails.getOrNull(0)?.products?.getOrNull(0)?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() } ?: "0"
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
                val lastValidateUseData = dPresenter.getValidateUseLastResponse()
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
                                    cartListData.availableSection.availableGroupGroups.find { group -> group.cartString == voucher.uniqueId }
                                availableGroup?.let { availableGroup ->
                                    clearOrders.add(
                                        ClearPromoOrder(
                                            uniqueId = voucher.uniqueId,
                                            boType = availableGroup.boMetadata.boType,
                                            codes = arrayListOf(voucher.code),
                                            shopId = availableGroup.shop.shopId.toLongOrZero(),
                                            warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                            isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                            poDuration = availableGroup.cartDetails.getOrNull(0)?.products?.getOrNull(0)?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() } ?: "0"
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
                    dPresenter.getUpdateCartAndValidateUseLastResponse()
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
                                    cartListData.availableSection.availableGroupGroups.find { group -> group.cartString == voucher.uniqueId }
                                availableGroup?.let { availableGroup ->
                                    clearOrders.add(
                                        ClearPromoOrder(
                                            uniqueId = voucher.uniqueId,
                                            boType = availableGroup.boMetadata.boType,
                                            codes = arrayListOf(voucher.code),
                                            shopId = availableGroup.shop.shopId.toLongOrZero(),
                                            warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero(),
                                            isPo = availableGroup.shipmentInformation.preorder.isPreorder,
                                            poDuration = availableGroup.cartDetails.getOrNull(0)?.products?.getOrNull(0)?.productPreorder?.durationDay?.let { poDuration -> poDuration.toString() } ?: "0"
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

            val clearPromo = ClearPromoRequest(
                ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                orderData = ClearPromoOrderData(redStateGlobalPromo, clearOrders)
            )
            if (hasRedStatePromo) {
                dPresenter.doClearRedPromosBeforeGoToCheckout(clearPromo)
            } else {
                goToCheckoutPage()
            }
        } else {
            showToastMessageRed(message)
            cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
            cartPageAnalytics.eventViewErrorWhenCheckout(message)
        }
    }

    override fun onSuccessClearRedPromosThenGoToCheckout() {
        goToCheckoutPage()
    }

    private fun goToCheckoutPage() {
        dPresenter.processUpdateCartData(false)
    }

    private fun isTestingFlow(): Boolean {
        return arguments?.getBoolean(IS_TESTING_FLOW, false) ?: false
    }

    private fun addToCartExternal(productId: Long) {
        dPresenter.processAddToCartExternal(productId)
    }

    override fun refreshCartWithSwipeToRefresh() {
        refreshHandler?.isRefreshing = true
        resetRecentViewList()
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData(getCartId())
        } else {
            dPresenter.processInitialGetCartData(
                getCartId(),
                dPresenter.getCartListData() == null,
                true
            )
        }
    }

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData) {
        cartPageAnalytics.eventClickAtcCartClickTrashBin()
        val allCartItemDataList = cartAdapter.allCartItemData
        val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
        if (cartItemHolderData.isBundlingItem) {
            val cartShopHolderData =
                cartAdapter.getCartShopHolderDataByCartItemHolderData(cartItemHolderData)
            cartShopHolderData?.let {
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
            if (cartAdapter.allDisabledCartItemData.size > 1 && unavailableItemAccordionCollapseState) {
                collapseOrExpandDisabledItem()
                forceExpand = true
            }

            dPresenter.processDeleteCartItem(
                allCartItemDataList,
                toBeDeletedProducts,
                false,
                forceExpand
            )
            cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(
                dPresenter.generateDeleteCartDataAnalytics(toBeDeletedProducts)
            )
        }
    }

    override fun onCartItemQuantityPlusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus()
    }

    override fun onCartItemQuantityMinusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus()
    }

    override fun onCartItemQuantityReseted(position: Int, cartItemHolderData: CartItemHolderData) {
        cartAdapter.resetQuantity(position, cartItemHolderData)
    }

    override fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemHolderData.productName)
        routeToProductDetailPage(cartItemHolderData.productId)
    }

    override fun onDisabledCartItemProductClicked(cartItemHolderData: CartItemHolderData) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemHolderData.productName)
        routeToProductDetailPage(cartItemHolderData.productId)
    }

    override fun onGlobalCheckboxCheckedChange(
        isChecked: Boolean,
        isCheckUncheckDirectAction: Boolean
    ) {
        this.isCheckUncheckDirectAction = isCheckUncheckDirectAction
        binding?.topLayout?.checkboxGlobal?.isChecked = isChecked
    }

    override fun onGlobalDeleteClicked() {
        cartPageAnalytics.eventClickGlobalDelete()
        val allCartItemDataList = cartAdapter.allCartItemData
        val deletedCartItems = cartAdapter.selectedCartItemData
        val dialog = getMultipleItemsDialogDeleteConfirmation(deletedCartItems.size)
        dialog?.setPrimaryCTAClickListener {
            dPresenter.processDeleteCartItem(
                allCartItemData = allCartItemDataList,
                removedCartItems = deletedCartItems,
                addWishList = false,
                isFromGlobalCheckbox = true
            )
            dialog.dismiss()
        }
        dialog?.setSecondaryCTAClickListener {
            dPresenter.processDeleteCartItem(
                allCartItemData = allCartItemDataList,
                removedCartItems = deletedCartItems,
                addWishList = true,
                isFromGlobalCheckbox = true
            )
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun onNeedToGoneLocalizingAddressWidget() {
        val chooseAddressWidgetPosition = cartAdapter.removeChooseAddressWidget()
        onNeedToRemoveViewItem(chooseAddressWidgetPosition)
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        val clearBoPromo = generateParamClearBo()
        if (clearBoPromo != null) {
            dPresenter.clearAllBo(clearBoPromo)
        }
        refreshCartWithProgressDialog(GET_CART_STATE_AFTER_CHOOSE_ADDRESS)
    }

    override fun onClickShopNow() {
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart()
        goToHome()
    }

    private fun goToHome() {
        routeToHome()
    }

    override fun onShowAllItem(appLink: String) {
        routeToApplink(appLink)
    }

    override fun onClickAddOnCart(productId: String, addOnId: String) {
        activity?.let {
            RouteManager.route(it, UriUtil.buildUri(ApplinkConst.GIFTING, addOnId))
        }
        cartPageAnalytics.eventClickAddOnsWidget(productId)
    }

    override fun addOnImpression(productId: String) {
        cartPageAnalytics.eventViewAddOnsWidget(productId)
    }

    override fun onViewFreeShippingPlusBadge() {
        cartPageAnalytics.eventViewGotoplusTicker()
    }

    override fun showCartBundlingBottomSheet(data: CartBundlingBottomSheetData) {
        val bottomSheet = CartBundlingBottomSheet.newInstance(data)
        bottomSheet.setListener(this)
        bottomSheet.show(childFragmentManager)
    }

    override fun onNewBundleProductAddedToCart() {
        refreshCartWithSwipeToRefresh()
    }

    private fun onErrorAddWishList(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter.notifyByProductId(productId, false)
        cartAdapter.notifyWishlist(productId, false)
        cartAdapter.notifyRecentView(productId, false)
    }

    private fun onSuccessAddWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success))
        cartAdapter.notifyByProductId(productId, true)
        cartAdapter.notifyWishlist(productId, true)
        cartAdapter.notifyRecentView(productId, true)
    }

    private fun onSuccessAddWishlistV2(
        result: AddToWishlistV2Response.Data.WishlistAddV2,
        productId: String
    ) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
            }
        }
        cartAdapter.notifyByProductId(productId, true)
        cartAdapter.notifyWishlist(productId, true)
        cartAdapter.notifyRecentView(productId, true)
    }

    private fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter.notifyByProductId(productId, true)
        cartAdapter.notifyWishlist(productId, true)
        cartAdapter.notifyRecentView(productId, true)
    }

    private fun onSuccessRemoveWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success))
        cartAdapter.notifyByProductId(productId, false)
        cartAdapter.removeWishlist(productId)
        cartAdapter.notifyRecentView(productId, false)
    }

    private fun onSuccessRemoveWishlistV2(
        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
        productId: String
    ) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, v)
            }
        }
        cartAdapter.notifyByProductId(productId, false)
        cartAdapter.removeWishlist(productId)
        cartAdapter.notifyRecentView(productId, false)
    }

    private fun getRecommendationWishlistV2ActionListener(): WishlistV2ActionListener {
        if (recommendationWishlistV2ActionListener == null) {
            recommendationWishlistV2ActionListener = object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorAddWishList(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessAddWishlistV2(result, productId)
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.eventClickAddWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics.eventClickAddWishlistOnProductRecommendation()
                    }
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessRemoveWishlistV2(result, productId)
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.eventClickRemoveWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics.eventClickRemoveWishlistOnProductRecommendation()
                    }
                }
            }
        }
        return recommendationWishlistV2ActionListener as WishlistV2ActionListener
    }

    private fun getCartUnavailableWishlistV2ActionListener(): WishlistV2ActionListener {
        if (cartUnavailableWishlistV2ActionListener == null) {
            cartUnavailableWishlistV2ActionListener = object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorAddWishList(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessAddWishlistV2(result, productId)
                    cartPageAnalytics.eventAddWishlistUnavailableSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessRemoveWishlistV2(result, productId)
                    cartPageAnalytics.eventRemoveWishlistUnvailableSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }
            }
        }
        return cartUnavailableWishlistV2ActionListener as WishlistV2ActionListener
    }

    private fun getLastSeenWishlistV2ActionListener(): WishlistV2ActionListener {
        if (lastSeenWishlistV2ActionListener == null) {
            lastSeenWishlistV2ActionListener = object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorAddWishList(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessAddWishlistV2(result, productId)
                    cartPageAnalytics.eventAddWishlistLastSeenSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessRemoveWishlistV2(result, productId)
                    cartPageAnalytics.eventRemoveWishlistLastSeenSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }
            }
        }
        return lastSeenWishlistV2ActionListener as WishlistV2ActionListener
    }

    private fun getWishlistsWishlistV2ActionListener(): WishlistV2ActionListener {
        if (wishlistsWishlistV2ActionListener == null) {
            wishlistsWishlistV2ActionListener = object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorAddWishList(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessAddWishlistV2(result, productId)
                    cartPageAnalytics.eventAddWishlistWishlistsSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(
                        ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        ),
                        productId
                    )
                }

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                    this@CartFragment.onSuccessRemoveWishlistV2(result, productId)
                    cartPageAnalytics.eventRemoveWishlistWishlistsSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }
            }
        }
        return wishlistsWishlistV2ActionListener as WishlistV2ActionListener
    }

    override fun onAddLastSeenToWishlist(productId: String) {
        dPresenter.processAddToWishlistV2(
            productId,
            userSession.userId,
            getLastSeenWishlistV2ActionListener()
        )
    }

    override fun onAddWishlistToWishlist(productId: String) {
        dPresenter.processAddToWishlistV2(
            productId,
            userSession.userId,
            getWishlistsWishlistV2ActionListener()
        )
    }

    override fun onAddRecommendationToWishlist(productId: String) {
        dPresenter.processAddToWishlistV2(
            productId,
            userSession.userId,
            getRecommendationWishlistV2ActionListener()
        )
    }

    override fun onRemoveDisabledItemFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlistV2(
            productId,
            userSession.userId,
            getCartUnavailableWishlistV2ActionListener()
        )
    }

    override fun onRemoveLastSeenFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlistV2(
            productId,
            userSession.userId,
            getLastSeenWishlistV2ActionListener()
        )
    }

    override fun onRemoveWishlistFromWishlist(productId: String) {
        cartPageAnalytics.eventClickRemoveWishlist(userSession.userId, productId)

        dPresenter.processRemoveFromWishlistV2(
            productId,
            userSession.userId,
            getWishlistsWishlistV2ActionListener()
        )
    }

    override fun onRemoveRecommendationFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlistV2(
            productId,
            userSession.userId,
            getRecommendationWishlistV2ActionListener()
        )
    }

    private fun onProductClicked(productId: String) {
        routeToProductDetailPage(productId)
    }

    override fun onWishlistProductClicked(productId: String) {
        var position = 0

        wishLists?.let {
            for (wishlist in wishLists as List<CartWishlistItemHolderData>) {
                if (wishlist.id.equals(productId, ignoreCase = true)) {
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(
                            position.toString(),
                            dPresenter.generateWishlistProductClickEmptyCartDataLayer(
                                wishlist,
                                position
                            )
                        )
                    } else {
                        cartPageAnalytics.enhancedEcommerceClickProductWishListOnCartList(
                            position.toString(),
                            dPresenter.generateWishlistProductClickDataLayer(wishlist, position)
                        )
                    }
                }
                position++
            }

            onProductClicked(productId)
        }
    }

    override fun onWishlistImpression() {
        wishLists?.let {
            cartPageAnalytics.enhancedEcommerceProductViewWishList(
                dPresenter.generateWishlistDataImpressionAnalytics(it, FLAG_IS_CART_EMPTY)
            )
        }
    }

    override fun onRecentViewProductImpression(element: CartRecentViewItemHolderData) {
        recentViewList?.let {
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

    override fun onRecentViewProductClicked(productId: String) {
        (recentViewList as List<CartRecentViewItemHolderData>).withIndex()
            .forEach { (position, recentView) ->
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
                            dPresenter.generateRecentViewProductClickEmptyCartDataLayer(
                                recentView,
                                position
                            )
                        )
                    } else {
                        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnCartList(
                            position.toString(),
                            dPresenter.generateRecentViewProductClickDataLayer(recentView, position)
                        )
                    }
                }
            }
        onProductClicked(productId)
    }

    override fun onRecentViewImpression() {
        recentViewList?.let {
            cartPageAnalytics.enhancedEcommerceProductViewLastSeen(
                dPresenter.generateRecentViewDataImpressionAnalytics(it, FLAG_IS_CART_EMPTY)
            )
        }
    }

    override fun onRecommendationProductClicked(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val clickUrl = recommendationItem.clickUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        var index = 1
        var recommendationItemClick: RecommendationItem? = null
        val recommendationList = cartAdapter.getRecommendationItem()
        for ((_, item) in recommendationList) {
            if (item.productId.toString().equals(productId, ignoreCase = true)) {
                recommendationItemClick = item
                break
            }
            index++
        }

        recommendationItemClick?.let {
            cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(
                dPresenter.generateRecommendationDataOnClickAnalytics(it, FLAG_IS_CART_EMPTY, index)
            )
        }

        when {
            topAds -> {
                activity?.let {
                    TopAdsUrlHitter(CartFragment::class.qualifiedName).hitClickUrl(
                        it,
                        clickUrl,
                        productId,
                        productName,
                        imageUrl
                    )
                }
            }
        }
        onProductClicked(productId)
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
        if (recommendationList.isNullOrEmpty()) return
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

    private fun sendImpressionOneRecommendationItem(
        it: List<CartRecommendationItemHolderData>,
        currentIndex: Int
    ) {
        val cartRecommendationList = ArrayList<CartRecommendationItemHolderData>()
        cartRecommendationList.add(it[currentIndex])
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(
            dPresenter.generateRecommendationImpressionDataAnalytics(
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
            dPresenter.generateRecommendationImpressionDataAnalytics(
                currentIndex,
                cartRecommendationList,
                FLAG_IS_CART_EMPTY
            )
        )
    }

    override fun onButtonAddToCartClicked(productModel: Any) {
        if (dPresenter.dataHasChanged()) {
            dPresenter.processUpdateCartData(true)
        }
        dPresenter.processAddToCart(productModel)
    }

    override fun onShowActionSeeOtherProduct(productId: String, errorType: String) {
        cartPageAnalytics.eventClickSeeOtherProductOnUnavailableSection(
            userSession.userId,
            productId,
            errorType
        )
    }

    override fun onSimilarProductUrlClicked(data: CartItemHolderData) {
        if (data.isBundlingItem) {
            cartPageAnalytics.eventClickMoreLikeThisOnBundleProduct(data.bundleId, data.bundleType)
        } else {
            cartPageAnalytics.eventClickMoreLikeThis()
        }
        routeToApplink(data.selectedUnavailableActionLink)
    }

    override fun onFollowShopClicked(shopId: String, errorType: String) {
        cartPageAnalytics.eventClickFollowShop(userSession.userId, errorType, shopId)
        dPresenter.followShop(shopId)
    }

    override fun onVerificationClicked(applink: String) {
        activity?.also {
            val intent = RouteManager.getIntentNoFallback(it, applink) ?: return
            startActivityForResult(intent, NAVIGATION_VERIFICATION)
        }
    }

    override fun onSeeErrorProductsClicked() {
        scrollToUnavailableSection()
    }

    private fun scrollToUnavailableSection() {
        binding?.rvCart?.layoutManager?.let {
            val linearSmoothScroller = object : LinearSmoothScroller(binding?.rvCart?.context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            linearSmoothScroller.targetPosition = cartAdapter.disabledItemHeaderPosition
            it.startSmoothScroll(linearSmoothScroller)
        }
    }

    override fun onShowCartTicker(tickerId: String) {
        cartPageAnalytics.eventViewInformationAndWarningTickerInCart(tickerId)
    }

    override fun getDefaultCartErrorMessage(): String {
        return if (isAdded) {
            getString(R.string.cart_error_message_no_count)
        } else {
            ""
        }
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
        dPresenter.setHasPerformChecklistChange(true)
        cartAdapter.setShopSelected(index, checked)
        dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
        if (checked) {
            val data = cartAdapter.getData()[index]
            if (data is CartShopHolderData) {
                checkCartShopGroupTicker(data)
            }
        }
        onNeedToUpdateViewItem(index)
        validateGoToCheckout()
        dPresenter.saveCheckboxState(cartAdapter.allAvailableCartItemHolderData)

        val params = generateParamValidateUsePromoRevamp()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }

        setCheckboxGlobalState()
        setGlobalDeleteVisibility()
    }

    override fun onCartShopGroupTickerClicked(cartShopHolderData: CartShopHolderData) {
        when (cartShopHolderData.cartShopGroupTicker.action) {
            CartShopGroupTickerData.ACTION_REDIRECT_PAGE -> {
                if (cartShopHolderData.cartShopGroupTicker.applink.isNotBlank()) {
                    routeToApplinkWithResult(cartShopHolderData.cartShopGroupTicker.applink)
                }
            }
            CartShopGroupTickerData.ACTION_OPEN_BOTTOM_SHEET_BUNDLING -> {
                showCartBundlingBottomSheet(cartShopHolderData.cartShopGroupTicker.cartBundlingBottomSheetData)
            }
            else -> {
                // no-op
            }
        }
        if (cartShopHolderData.cartShopGroupTicker.enableBoAffordability) {
            cartPageAnalytics.eventClickArrowInBoTickerToReachShopPage(
                cartShopHolderData.cartShopGroupTicker.cartIds,
                cartShopHolderData.shopId
            )
        }
    }

    override fun onCartShopGroupTickerRefreshClicked(
        index: Int,
        cartShopHolderData: CartShopHolderData
    ) {
        cartShopHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.LOADING
        cartShopHolderData.isNeedToRefreshWeight = true
        onNeedToUpdateViewItem(index)
        dPresenter.checkCartShopGroupTicker(cartShopHolderData)
    }

    override fun onViewCartShopGroupTicker(cartShopHolderData: CartShopHolderData) {
        cartPageAnalytics.eventViewBoTickerWording(
            cartShopHolderData.cartShopGroupTicker.state == CartShopGroupTickerState.SUCCESS_AFFORD,
            cartShopHolderData.cartShopGroupTicker.cartIds,
            cartShopHolderData.shopId
        )
    }

    override fun onCartDataEnableToCheckout() {
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
        if (isAdded) {
            binding?.goToCourierPageButton?.isEnabled = false
            binding?.vDisabledGoToCourierPageButton?.show()
            binding?.vDisabledGoToCourierPageButton?.setOnClickListener {
                if (cartAdapter.allAvailableCartItemData.isNotEmpty()) {
                    showToastMessageGreen(getString(R.string.message_no_cart_item_selected))
                }
            }
        }
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        cartPageAnalytics.eventClickAtcCartClickInputQuantity(qty)
    }

    override fun onCartItemLabelInputRemarkClicked() {
        cartPageAnalytics.eventClickAtcCartClickTulisCatatan()
    }

    override fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData) {
        cartAdapter.setItemSelected(position, cartItemHolderData)
        updateStateAfterCheckChanged()
    }

    private fun updateStateAfterCheckChanged() {
        dPresenter.setHasPerformChecklistChange(true)
        dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
        setCheckboxGlobalState()
        setGlobalDeleteVisibility()
        validateGoToCheckout()
        val params = generateParamValidateUsePromoRevamp()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
        dPresenter.saveCheckboxState(cartAdapter.allAvailableCartItemHolderData)
    }

    override fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData) {
        val cartShopHolderData =
            cartAdapter.getCartShopHolderDataByCartItemHolderData(cartItemHolderData)
        cartShopHolderData?.let {
            it.productUiModelList.forEachIndexed { index, data ->
                if (data.isBundlingItem && data.bundleId == cartItemHolderData.bundleId && data.bundleGroupId == cartItemHolderData.bundleGroupId) {
                    cartAdapter.setItemSelected(index, cartItemHolderData)
                }
            }
            updateStateAfterCheckChanged()
        }
    }

    private fun setCheckboxGlobalState() {
        isCheckUncheckDirectAction = false
        val isAllAvailableItemCheked = cartAdapter.isAllAvailableItemCheked()
        if (binding?.topLayout?.checkboxGlobal?.isChecked == isAllAvailableItemCheked) {
            isCheckUncheckDirectAction = true
        }
        binding?.topLayout?.checkboxGlobal?.isChecked = isAllAvailableItemCheked
    }

    private fun setGlobalDeleteVisibility() {
        if (cartAdapter.hasSelectedCartItem()) {
            binding?.topLayout?.textActionDelete?.show()
        } else {
            binding?.topLayout?.textActionDelete?.invisible()
        }
    }

    private fun updatePromoCheckoutManualIfNoSelected(listPromoApplied: List<String>) {
        if (cartAdapter.selectedCartShopHolderData.isEmpty()) {
            renderPromoCheckoutButtonNoItemIsSelected()
        } else {
            renderPromoCheckoutButtonActiveDefault(listPromoApplied)
        }
    }

    override fun onWishlistCheckChanged(
        productId: String,
        cartId: String,
        imageView: ImageView,
        isError: Boolean,
        errorType: String
    ) {
        if (isError) {
            cartPageAnalytics.eventClickMoveToWishlistOnUnavailableSection(
                userSession.userId,
                productId,
                errorType
            )
        } else {
            cartPageAnalytics.eventClickMoveToWishlistOnAvailableSection(
                userSession.userId,
                productId
            )
        }
        setProductImageAnimationData(imageView, false)
        val isLastItem = cartAdapter.allCartItemData.size == 1
        var forceExpand = false
        if (isError) {
            // If unavailable item > 1 and state is collapsed, then expand first
            if (cartAdapter.allDisabledCartItemData.size > 1 && unavailableItemAccordionCollapseState) {
                collapseOrExpandDisabledItem()
                forceExpand = true
            }
        }
        dPresenter.processAddCartToWishlist(
            productId,
            cartId,
            isLastItem,
            if (isError) WISHLIST_SOURCE_UNAVAILABLE_ITEM else WISHLIST_SOURCE_AVAILABLE_ITEM,
            forceExpand
        )
    }

    private fun setProductImageAnimationData(imageView: ImageView, isUnavailableItem: Boolean) {
        val imageSource: Bitmap? = imageView.drawable?.toBitmap()
        val location = IntArray(2)
        imageView.getLocationOnScreen(location)
        val xCoordinate = location[0]
        val yCoordinate = location[1]

        binding?.tmpAnimatedImage?.apply {
            imageSource?.let {
                setImageBitmap(imageSource)
            }
            if (isUnavailableItem) {
                val size = resources.getDimensionPixelOffset(R.dimen.dp_56)
                layoutParams.width = size
                layoutParams.width = size
                alpha = ANIMATED_IMAGE_ALPHA
            } else {
                val size = resources.getDimensionPixelOffset(R.dimen.dp_72)
                layoutParams.width = size
                layoutParams.width = size
                alpha = ANIMATED_IMAGE_FILLED
            }

            x = xCoordinate.toFloat()
            y = yCoordinate.toFloat() - (height / COORDINATE_HEIGHT_DIVISOR)
        }
    }

    override fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData) {
        val (data, index) = cartAdapter.getCartShopHolderDataAndIndexByCartString(cartItemHolderData.cartString)
        if (data != null) {
            checkCartShopGroupTicker(data)
            onNeedToUpdateViewItem(index)
        }
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        val (data, index) = cartAdapter.getCartShopHolderDataAndIndexByCartString(cartItemHolderData.cartString)
        if (data != null) {
            data.isNeedToRefreshWeight = true
            checkCartShopGroupTicker(data)
            onNeedToUpdateViewItem(index)
        }
    }

    override fun onNeedToRefreshMultipleShop() {
        val firstShopIndexAndCount = cartAdapter.getFirstShopAndShopCountWithIterateFunction {
            checkCartShopGroupTicker(it)
        }
        onNeedToUpdateMultipleViewItem(firstShopIndexAndCount.first, firstShopIndexAndCount.second)
    }

    override fun onNeedToRecalculate() {
        dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
    }

    override fun showProgressLoading() {
        if (progressDialog?.isShowing == false) progressDialog?.show()
    }

    override fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) progressDialog?.dismiss()
        if (refreshHandler?.isRefreshing == true) {
            refreshHandler?.finishRefresh()
        }
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

    override fun renderInitialGetCartListDataSuccess(cartData: CartData) {
        recommendationPage = 1
        dPresenter.processUpdateCartCounter()
        if (cartData.outOfService.isOutOfService()) {
            renderCartOutOfService(cartData.outOfService)
            return
        }

        sendAnalyticsScreenNameCartPage()
        updateStateAfterFinishGetCartList(cartData)

        renderCheckboxGlobal(cartData)
        renderTickerAnnouncement(cartData)
        renderChooseAddressWidget(cartData)

        validateRenderCart(cartData)
        validateShowPopUpMessage(cartData)
        validateRenderPromo(cartData)

        setInitialCheckboxGlobalState(cartData)
        setGlobalDeleteVisibility()

        validateGoToCheckout()
        scrollToLastAddedProductShop()

        renderAdditionalWidget()
        resetArguments()
    }

    private fun scrollToLastAddedProductShop() {
        val cartId: String = getCartId()
        if (cartId.isNotBlank()) {
            var hasTokoNowProduct = false
            loop@ for (shop in cartAdapter.allShopGroupDataList) {
                hasTokoNowProduct = true
                break@loop
            }

            if (hasTokoNowProduct) {
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

    private fun renderAdditionalWidget() {
        validateRenderWishlist()
        validateRenderRecentView()
        loadRecommendation()
    }

    private fun updateStateAfterFinishGetCartList(cartData: CartData) {
        endlessRecyclerViewScrollListener.resetState()
        refreshHandler?.finishRefresh()
        cartAdapter.resetData()
    }

    private fun sendAnalyticsScreenNameCartPage() {
        cartPageAnalytics.sendScreenName(activity, screenName)
    }

    private fun loadRecommendation() {
        dPresenter.processGetRecommendationData(
            recommendationPage,
            cartAdapter.allCartItemProductId
        )
    }

    private fun validateRenderRecentView() {
        if (recentViewList == null || shouldReloadRecentViewList) {
            dPresenter.processGetRecentViewData(cartAdapter.allCartItemProductId)
        } else {
            renderRecentView(null)
        }
    }

    private fun validateRenderWishlist() {
        if (wishLists == null) {
            dPresenter.processGetWishlistV2Data()
        } else {
            renderWishlistV2(null, false)
        }
    }

    private fun validateRenderCart(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isEmpty() && cartData.unavailableSections.isEmpty()) {
            renderCartEmpty(cartData)
            setTopLayoutVisibility(false)
        } else {
            renderCartNotEmpty(cartData)
            setTopLayoutVisibility(cartData.availableSection.availableGroupGroups.isNotEmpty())
        }
        cartAdapter.notifyDataSetChanged()
    }

    private fun validateRenderPromo(cartData: CartData) {
        // reset promo position
        initialPromoButtonPosition = 0f
        if (dPresenter.isLastApplyValid()) {
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

    private fun validateRenderPromoFromValidateUsePromoPage() {
        dPresenter.getValidateUseLastResponse()?.promoUiModel?.let {
            val lastApplyUiModel =
                LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
        }
    }

    private fun validateRenderPromoFromValidateUseCartPage() {
        dPresenter.getUpdateCartAndValidateUseLastResponse()?.promoUiModel?.let {
            val lastApplyUiModel =
                LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
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

    private fun validateShowPopUpMessage(cartData: CartData) {
        if (cartData.popupErrorMessage.isNotBlank()) {
            showToastMessageRed(cartData.popupErrorMessage)
            cartPageAnalytics.eventViewToasterErrorInCartPage(cartData.popupErrorMessage)
        } else if (cartData.popUpMessage.isNotBlank()) {
            showToastMessageGreen(cartData.popUpMessage)
        }
    }

    private fun setInitialCheckboxGlobalState(cartData: CartData) {
        binding?.topLayout?.checkboxGlobal?.isChecked = cartData.isGlobalCheckboxState
    }

    private fun renderChooseAddressWidget(cartData: CartData) {
        activity?.let {
            if (cartData.localizationChooseAddress.state == LocalizationChooseAddress.STATE_EMPTY) {
                val chooseAddressWidgetPosition = cartAdapter.removeChooseAddressWidget()
                onNeedToRemoveViewItem(chooseAddressWidgetPosition)
            } else {
                validateLocalCacheAddress(it, cartData.localizationChooseAddress)
                val cartChooseAddressHolderData = CartUiModelMapper.mapChooseAddressUiModel()
                cartAdapter.addItem(cartChooseAddressHolderData)
            }
        }
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
        dPresenter.setLocalizingAddressData(lca)
    }

    private fun renderCartOutOfService(outOfService: OutOfService) {
        binding?.apply {
            when (outOfService.id) {
                ID_MAINTENANCE, ID_TIMEOUT, ID_OVERLOAD -> {
                    layoutGlobalError.setType(GlobalError.SERVER_ERROR)
                    outOfService.buttons.firstOrNull()?.let { buttonData ->
                        layoutGlobalError.errorAction.text = buttonData.message
                        layoutGlobalError.setActionClickListener {
                            when (buttonData.id) {
                                ID_START_SHOPPING, ID_HOMEPAGE -> {
                                    goToHome()
                                }
                                ID_RETRY -> {
                                    refreshErrorPage()
                                }
                            }
                        }
                    }
                }
            }

            if (outOfService.title.isNotBlank()) {
                layoutGlobalError.errorTitle.text = outOfService.title
            }
            if (outOfService.description.isNotBlank()) {
                layoutGlobalError.errorDescription.text = outOfService.description
            }
            if (outOfService.image.isNotBlank()) {
                layoutGlobalError.errorIllustration.setImage(outOfService.image, 0f)
            }

            showErrorContainer()

            cartPageAnalytics.eventViewErrorPageWhenLoadCart(
                userSession.userId,
                outOfService.getErrorType()
            )
        }
    }

    private fun renderCartNotEmpty(cartData: CartData) {
        FLAG_IS_CART_EMPTY = false

        renderTickerError(cartData)
        renderCartAvailableItems(cartData)
        renderCartUnavailableItems(cartData)

        dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = cartAdapter.allCartItemData
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
            dPresenter.generateCheckoutDataAnalytics(
                cartItemDataList,
                EnhancedECommerceActionField.STEP_0
            ),
            userSession.userId,
            dPresenter.getPromoFlag()
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

    private fun renderTickerAnnouncement(cartData: CartData) {
        val ticker = cartData.tickers.firstOrNull()
        if (ticker != null && ticker.isValid(CART_PAGE)) {
            cartAdapter.addItem(CartUiModelMapper.mapTickerAnnouncementUiModel(ticker))
        }
    }

    private fun renderPromoCheckout(lastApplyUiModel: LastApplyUiModel) {
        renderPromoCheckoutButton(lastApplyUiModel)
    }

    override fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>) {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.ACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title =
                getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
            promoCheckoutBtnCart.desc = ""
            promoCheckoutBtnCart.setOnClickListener {
                dPresenter.doUpdateCartForPromo()
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

    private fun renderPromoCheckoutButton(lastApplyData: LastApplyUiModel) {
        val tickerPromoData = dPresenter.getTickerPromoData()
        if (dPresenter.getShowChoosePromoWidget()) {
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

                if (cartAdapter.selectedCartItemData.isEmpty()) {
                    binding?.promoCheckoutBtnCart?.desc =
                        getString(R.string.promo_desc_no_selected_item)
                } else {
                    binding?.promoCheckoutBtnCart?.desc = ""
                }
            }

            binding?.promoCheckoutBtnCart?.title = title
            binding?.promoCheckoutBtnCart?.setOnClickListener {
                if (cartAdapter.selectedCartItemData.isEmpty()) {
                    showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                    PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
                } else {
                    dPresenter.doUpdateCartForPromo()
                    // analytics
                    PromoRevampAnalytics.eventCartClickPromoSection(
                        getAllPromosApplied(
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

        dPresenter.updatePromoSummaryData(lastApplyData)
    }

    private fun getAllPromosApplied(lastApplyData: LastApplyUiModel): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyData.voucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    private fun getAllPromosApplied(lastApplyPromoData: LastApplyPromoData): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyPromoData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyPromoData.listVoucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    private fun renderPromoCheckoutLoading() {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.LOADING
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.setOnClickListener { }
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

    private fun generateParamValidateUsePromoRevamp(): ValidateUsePromoRequest {
        return when {
            dPresenter.isLastApplyValid() -> {
                val lastApplyPromo =
                    dPresenter.getCartListData()?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateValidateUseRequestParams(
                    lastApplyPromo,
                    cartAdapter.selectedCartShopHolderData,
                    null
                )
            }
            dPresenter.getValidateUseLastResponse() != null -> {
                val promoUiModel =
                    dPresenter.getValidateUseLastResponse()?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateValidateUseRequestParams(
                    promoUiModel,
                    cartAdapter.selectedCartShopHolderData,
                    dPresenter.getLastValidateUseRequest()
                )
            }
            else -> {
                PromoRequestMapper.generateValidateUseRequestParams(
                    null,
                    cartAdapter.selectedCartShopHolderData,
                    null
                )
            }
        }
    }

    private fun generateParamsCouponList(): PromoRequest {
        return when {
            dPresenter.isLastApplyValid() -> {
                val lastApplyPromo =
                    dPresenter.getCartListData()?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateCouponListRequestParams(
                    lastApplyPromo,
                    cartAdapter.allAvailableShopGroupDataList
                )
            }
            dPresenter.getValidateUseLastResponse() != null -> {
                val promoUiModel =
                    dPresenter.getValidateUseLastResponse()?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateCouponListRequestParams(
                    promoUiModel,
                    cartAdapter.allAvailableShopGroupDataList
                )
            }
            else -> {
                PromoRequestMapper.generateCouponListRequestParams(
                    null,
                    cartAdapter.allAvailableShopGroupDataList
                )
            }
        }
    }

    private fun generateParamClearBo(): ClearPromoOrderData? {
        return when {
            dPresenter.isLastApplyValid() -> {
                val lastApplyPromo =
                    dPresenter.getCartListData()?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateClearBoParam(
                    lastApplyPromo,
                    cartAdapter.allAvailableShopGroupDataList
                )
            }
            dPresenter.getValidateUseLastResponse() != null -> {
                val promoUiModel =
                    dPresenter.getValidateUseLastResponse()?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateClearBoParam(
                    promoUiModel,
                    cartAdapter.allAvailableShopGroupDataList
                )
            }
            else -> null
        }
    }

    private fun renderTickerError(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isNotEmpty() && cartData.unavailableSections.isNotEmpty()) {
            val cartItemTickerErrorHolderData = CartUiModelMapper.mapTickerErrorUiModel(cartData)
            cartAdapter.addItem(cartItemTickerErrorHolderData)
        }
    }

    private fun renderCheckboxGlobal(cartData: CartData) {
        // Just add view item to fill space. This view item will always be covered by sticky global checkbox view.
        if (cartData.availableSection.availableGroupGroups.isNotEmpty()) {
            cartAdapter.addItem(CartSelectAllHolderData())
        }
    }

    private fun renderCartAvailableItems(cartData: CartData) {
        if (cartData.availableSection.availableGroupGroups.isNotEmpty()) {
            val availableShopList = CartUiModelMapper.mapAvailableShopUiModel(cartData)
            cartAdapter.addItems(availableShopList)
        }
    }

    private fun renderCartUnavailableItems(cartData: CartData) {
        if (cartData.unavailableSections.isNotEmpty()) {
            val unavailableDataMapResult =
                CartUiModelMapper.mapUnavailableShopUiModel(activity, cartData)
            val unavailableSectionList = unavailableDataMapResult.first
            val accordionUiModel = unavailableDataMapResult.second
            cartAdapter.addItems(unavailableSectionList)
            if (accordionUiModel != null) {
                collapseOrExpandDisabledItem(accordionUiModel)
                if (!unavailableItemAccordionCollapseState) {
                    accordionUiModel.isCollapsed = false
                    collapseOrExpandDisabledItem(accordionUiModel)
                }
            }
        }
    }

    private fun renderCartEmptyDefault() {
        val cartEmptyHolderData = CartUiModelMapper.mapCartEmptyUiModel(activity)
        cartAdapter.addItem(cartEmptyHolderData)
    }

    private fun renderCartEmptyWithPromo(lastApplyPromoData: LastApplyPromoData) {
        val cartEmptyWithPromoHolderData =
            CartUiModelMapper.mapCartEmptyWithPromoUiModel(activity, lastApplyPromoData)

        // analytics
        cartAdapter.addItem(cartEmptyWithPromoHolderData)
        val listPromos = getAllPromosApplied(lastApplyPromoData)
        PromoRevampAnalytics.eventCartEmptyPromoApplied(listPromos)
    }

    override fun stopCartPerformanceTrace(isSuccessLoadCart: Boolean) {
        if (!isTraceCartStopped) {
            EmbraceMonitoring.stopMoments(EmbraceKey.KEY_MP_CART)
            if (!isSuccessLoadCart) {
                EmbraceMonitoring.stopMoments(EmbraceKey.KEY_MP_CART_INCOMPLETE)
            }
            cartPerformanceMonitoring?.stopTrace()
            isTraceCartStopped = true
        }
    }

    override fun stopAllCartPerformanceTrace() {
        if (!isTraceCartAllStopped && hasTriedToLoadRecentViewList && hasTriedToLoadWishList && hasTriedToLoadRecommendation) {
            cartAllPerformanceMonitoring?.stopTrace()
            isTraceCartAllStopped = true
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
                    goToHome()
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

    private fun refreshErrorPage() {
        setTopLayoutVisibility(false)
        binding?.layoutGlobalError?.gone()
        binding?.rlContent?.show()
        refreshHandler?.isRefreshing = true
        cartAdapter.resetData()
        dPresenter.processInitialGetCartData(getCartId(), true, false)
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
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

    private fun showErrorContainer() {
        binding?.apply {
            rlContent.gone()
            layoutGlobalError.show()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
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

    override fun renderErrorInitialGetCartListData(throwable: Throwable) {
        showErrorLayout(throwable)
    }

    override fun renderToShipmentFormSuccess(
        eeCheckoutData: Map<String, Any>,
        cartItemDataList: List<CartItemHolderData>,
        checkoutProductEligibleForCashOnDelivery: Boolean,
        condition: Int
    ) {
        when (condition) {
            CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                dPresenter.getPromoFlag()
            )
            CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                dPresenter.getPromoFlag()
            )
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                dPresenter.getPromoFlag()
            )
            CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                dPresenter.getPromoFlag()
            )
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(
                eeCheckoutData,
                checkoutProductEligibleForCashOnDelivery,
                userSession.userId,
                dPresenter.getPromoFlag()
            )
        }
        navigateToShipmentPage()
    }

    private fun navigateToShipmentPage() {
        FLAG_BEGIN_SHIPMENT_PROCESS = true
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = true
        routeToCheckoutPage()
    }

    private fun clearRecyclerView() {
        cartAdapter.clearCompositeSubscription()
        binding?.rvCart?.removeAllViews()
        binding?.rvCart?.recycledViewPool?.clear()
    }

    override fun renderErrorToShipmentForm(message: String, ctaText: String) {
        cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
        cartPageAnalytics.eventViewErrorWhenCheckout(message)
        showToastMessageRed(
            message = message,
            actionText = ctaText,
            ctaClickListener = View.OnClickListener {
                scrollToUnavailableSection()
            }
        )

        refreshCartWithSwipeToRefresh()
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

    private fun retryGoToShipment() {
        dPresenter.processUpdateCartData(false)
    }

    override fun renderErrorToShipmentForm(throwable: Throwable) {
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

    override fun renderErrorToShipmentForm(outOfService: OutOfService) {
        renderCartOutOfService(outOfService)
    }

    private fun disableSwipeRefresh() {
        refreshHandler?.setPullEnabled(false)
    }

    private fun enableSwipeRefresh() {
        refreshHandler?.setPullEnabled(true)
    }

    override fun getAllCartDataList(): List<CartItemHolderData> {
        return cartAdapter.allCartItemData
    }

    override fun getAllAvailableCartDataList(): List<CartItemHolderData> {
        return cartAdapter.allAvailableCartItemData
    }

    override fun getAllShopDataList(): List<CartShopHolderData> {
        return cartAdapter.allShopGroupDataList
    }

    override fun getAllSelectedCartDataList(): List<CartItemHolderData> {
        return cartAdapter.selectedCartItemData
    }

    override fun renderDetailInfoSubTotal(
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

    override fun updateCashback(cashback: Double) {
        val result = cartAdapter.updateShipmentSellerCashback(cashback)
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

    override fun showToastMessageRed(
        message: String,
        actionText: String,
        ctaClickListener: View.OnClickListener?
    ) {
        view?.let {
            var tmpMessage = message
            if (TextUtils.isEmpty(tmpMessage)) {
                tmpMessage = CART_ERROR_GLOBAL
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

    override fun showToastMessageRed(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (!(throwable is CartResponseErrorException || throwable is AkamaiErrorException || throwable is ResponseErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        showToastMessageRed(errorMessage)
    }

    override fun showToastMessageRed() {
        showToastMessageRed("")
    }

    override fun showToastMessageGreen(
        message: String,
        actionText: String,
        onClickListener: View.OnClickListener?
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

    private fun initializeToasterLocation() {
        activity?.let {
            if (it is CartActivity) {
                Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_140)
            } else {
                Toaster.toasterCustomBottomHeight = resources.getDimensionPixelSize(R.dimen.dp_210)
            }
        }
    }

    override fun renderLoadGetCartData() {
        binding?.apply {
            layoutGlobalError.gone()
            rlContent.show()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
        }
    }

    override fun renderLoadGetCartDataFinish() {
        if (refreshHandler?.isRefreshing == true) {
            refreshHandler?.isRefreshing = false
        }
        showMainContainer()
    }

    override fun onDeleteCartDataSuccess(
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
                getString(R.string.toaster_cta_cancel),
                View.OnClickListener { onUndoDeleteClicked(deletedCartIds) }
            )
        }

        val needRefresh = removeAllItems || isFromEditBundle
        val updateListResult =
            cartAdapter.removeProductByCartId(deletedCartIds, needRefresh, isFromGlobalCheckbox)
        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        hideProgressLoading()

        setTopLayoutVisibility()

        when {
            removeAllItems -> {
                refreshCartWithSwipeToRefresh()
            }
            isFromEditBundle -> {
                refreshCartWithProgressDialog(GET_CART_STATE_DEFAULT)
            }
            else -> {
                setLastItemAlwaysSelected()
            }
        }
    }

    private fun setTopLayoutVisibility() {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (cartAdapter.hasAvailableItemLeft()) {
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
            binding?.topLayout?.root?.show()
            if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                isShowToolbarShadow = true
            }
        } else {
            binding?.topLayout?.root?.gone()
        }

        setToolbarShadowVisibility(isShowToolbarShadow)
    }

    private fun onUndoDeleteClicked(cartIds: List<String>) {
        cartPageAnalytics.eventClickUndoAfterDeleteProduct(userSession.userId)
        dPresenter.processUndoDeleteCartItem(cartIds)
    }

    override fun onUndoDeleteCartDataSuccess() {
        dPresenter.processInitialGetCartData(getCartId(), false, false)
    }

    override fun onAddCartToWishlistSuccess(
        message: String,
        productId: String,
        cartId: String,
        isLastItem: Boolean,
        source: String,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        animateProductImage(message)

        when (source) {
            WISHLIST_SOURCE_AVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
            }
            WISHLIST_SOURCE_UNAVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistUnavailableSection(FLAG_IS_CART_EMPTY, productId)
            }
        }

        val updateListResult = cartAdapter.removeProductByCartId(listOf(cartId), isLastItem, false)
        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        setTopLayoutVisibility()

        if (isLastItem) {
            refreshCartWithSwipeToRefresh()
        } else {
            setLastItemAlwaysSelected()
        }
    }

    override fun onAddCartToWishlistV2Success(
        result: AddToWishlistV2Response.Data.WishlistAddV2,
        productId: String,
        cartId: String,
        isLastItem: Boolean,
        source: String,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        if (result.success) {
            animateProductImageV2(result)

            when (source) {
                WISHLIST_SOURCE_AVAILABLE_ITEM -> {
                    cartPageAnalytics.eventAddWishlistAvailableSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }
                WISHLIST_SOURCE_UNAVAILABLE_ITEM -> {
                    cartPageAnalytics.eventAddWishlistUnavailableSection(
                        FLAG_IS_CART_EMPTY,
                        productId
                    )
                }
            }

            val updateListResult =
                cartAdapter.removeProductByCartId(listOf(cartId), isLastItem, false)
            removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

            setTopLayoutVisibility()

            if (isLastItem) {
                refreshCartWithSwipeToRefresh()
            } else {
                setLastItemAlwaysSelected()
            }
        } else {
            context?.let { context ->
                view?.let { v ->
                    AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
                }
            }
        }
    }

    private fun setLastItemAlwaysSelected() {
        val tmpIsLastItem = cartAdapter.setLastItemAlwaysSelected()
        if (tmpIsLastItem) {
            binding?.topLayout?.checkboxGlobal?.isChecked = true
        }
    }

    @SuppressLint("Recycle")
    private fun animateProductImage(message: String) {
        val tmpAnimatedImage = binding?.tmpAnimatedImage ?: return
        var target: Pair<Int, Int>? = null

        if (isNavToolbar) {
            val targetX = getScreenWidth() - resources.getDimensionPixelSize(R.dimen.dp_64)
            target = Pair(targetX, 0)
        } else {
            target = toolbar.getWishlistIconPosition()
        }

        tmpAnimatedImage.show()

        val targetX = target.first
        val targetY = target.second

        val deltaX = targetX - (tmpAnimatedImage.width / 2)
        val deltaY = targetY - (tmpAnimatedImage.height / 2)

        val animY = ObjectAnimator.ofFloat(tmpAnimatedImage, "y", deltaY.toFloat())
        val animX = ObjectAnimator.ofFloat(tmpAnimatedImage, "x", deltaX.toFloat())
        val animAlpha = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "alpha",
            ANIMATED_IMAGE_FILLED,
            ANIMATED_IMAGE_ALPHA
        )
        val animScaleX = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "scaleX",
            ANIMATED_SCALE_FULL,
            ANIMATED_SCALE_HALF
        )
        val animScaleY = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "scaleY",
            ANIMATED_SCALE_FULL,
            ANIMATED_SCALE_HALF
        )

        AnimatorSet().let {
            it.playTogether(animY, animX, animAlpha, animScaleX, animScaleY)
            it.interpolator = DecelerateInterpolator()
            it.duration = IMAGE_ANIMATION_DURATION
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    binding?.tmpAnimatedImage?.gone()

                    if (isNavToolbar) {
                        binding?.navToolbar?.triggerAnimatedVectorDrawableAnimation(IconList.ID_WISHLIST)
                    } else {
                        toolbar.animateWishlistIcon()
                    }

                    showToastMessageGreen(message)
                    dPresenter.processGetWishlistV2Data()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            it.start()
        }
    }

    @SuppressLint("Recycle")
    private fun animateProductImageV2(result: AddToWishlistV2Response.Data.WishlistAddV2) {
        val tmpAnimatedImage = binding?.tmpAnimatedImage ?: return
        var target: Pair<Int, Int>? = null

        if (isNavToolbar) {
            val targetX = getScreenWidth() - resources.getDimensionPixelSize(R.dimen.dp_64)
            target = Pair(targetX, 0)
        } else {
            target = toolbar.getWishlistIconPosition()
        }

        tmpAnimatedImage.show()

        val targetX = target.first
        val targetY = target.second

        val deltaX = targetX - (tmpAnimatedImage.width / 2)
        val deltaY = targetY - (tmpAnimatedImage.height / 2)

        val animY = ObjectAnimator.ofFloat(tmpAnimatedImage, "y", deltaY.toFloat())
        val animX = ObjectAnimator.ofFloat(tmpAnimatedImage, "x", deltaX.toFloat())
        val animAlpha = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "alpha",
            ANIMATED_IMAGE_FILLED,
            ANIMATED_IMAGE_ALPHA
        )
        val animScaleX = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "scaleX",
            ANIMATED_SCALE_FULL,
            ANIMATED_SCALE_HALF
        )
        val animScaleY = ObjectAnimator.ofFloat(
            tmpAnimatedImage,
            "scaleY",
            ANIMATED_SCALE_FULL,
            ANIMATED_SCALE_HALF
        )

        AnimatorSet().let {
            it.playTogether(animY, animX, animAlpha, animScaleX, animScaleY)
            it.interpolator = DecelerateInterpolator()
            it.duration = IMAGE_ANIMATION_DURATION
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    binding?.tmpAnimatedImage?.gone()

                    if (isNavToolbar) {
                        binding?.navToolbar?.triggerAnimatedVectorDrawableAnimation(IconList.ID_WISHLIST)
                    } else {
                        toolbar.animateWishlistIcon()
                    }

                    context?.let { context ->
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                                result,
                                context,
                                v
                            )
                        }
                    }
                    dPresenter.processGetWishlistV2Data()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            it.start()
        }
    }

    private fun refreshWishlistAfterItemRemoveAndMoveToWishlist() {
        dPresenter.processGetWishlistV2Data()
    }

    private fun removeLocalCartItem(
        updateListResult: Pair<List<Int>, List<Int>>,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        val updateIndices = updateListResult.second
        val removeIndices = updateListResult.first
        if (removeIndices.size > 1) {
            // on multiple deletion, notify data set changed to prevent indices race condition
            cartAdapter.notifyDataSetChanged()
        } else {
            updateIndices.forEach {
                onNeedToUpdateViewItem(it)
            }
            removeIndices.forEach {
                onNeedToRemoveViewItem(it)
            }
        }

        // If action is on unavailable item, do collapse unavailable items if previously forced to expand (without user tap expand)
        if (cartAdapter.allDisabledCartItemData.size > 1) {
            if (forceExpandCollapsedUnavailableItems) {
                collapseOrExpandDisabledItem()
            }
        } else {
            cartAdapter.removeAccordionDisabledItem()
        }

        val allShopGroupDataList = cartAdapter.allShopGroupDataList

        // Check if cart list has more than 1 shop, and it's a toko now
        if (allShopGroupDataList.size == 1 && allShopGroupDataList[0].isTokoNow == true) {
            allShopGroupDataList[0].let {
                it.isCollapsible = false
                it.isCollapsed = false
                it.isShowPin = false
                val index = cartAdapter.getCartShopHolderIndexByCartString(it.cartString)
                if (index != RecyclerView.NO_POSITION) {
                    onNeedToUpdateViewItem(index)
                }
            }
        }

        // Check if cart list has more than 1 shop, and first shop is toko now
        if (allShopGroupDataList.size > 1 && allShopGroupDataList[0].isTokoNow == true) {
            allShopGroupDataList[0].let {
                if (it.productUiModelList.size == 1) {
                    it.isCollapsible = false
                    it.isCollapsed = false
                    val index = cartAdapter.getCartShopHolderIndexByCartString(it.cartString)
                    if (index != RecyclerView.NO_POSITION) {
                        onNeedToUpdateViewItem(index)
                    }
                }
            }
        }

        dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
        notifyBottomCartParent()
    }

    private fun onNeedToInserViewItem(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemInserted(position) }
        } else {
            cartAdapter.notifyItemInserted(position)
        }
    }

    private fun onNeedToInsertMultipleViewItem(positionStart: Int, itemCount: Int) {
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRangeInserted(positionStart, itemCount) }
        } else {
            cartAdapter.notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    private fun onNeedToRemoveViewItem(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRemoved(position) }
        } else {
            cartAdapter.notifyItemRemoved(position)
        }
    }

    private fun onNeedToRemoveMultipleViewItem(positionStart: Int, itemCount: Int) {
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRangeRemoved(positionStart, itemCount) }
        } else {
            cartAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    private fun onNeedToUpdateViewItem(position: Int) {
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

    override fun onRefresh(view: View?) {
        refreshCartWithSwipeToRefresh()
    }

    private fun refreshCartWithProgressDialog(getCartState: Int) {
        resetRecentViewList()
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData(getCartId(), getCartState)
        } else {
            dPresenter.processInitialGetCartData(getCartId(), false, false, getCartState)
        }
    }

    override fun getScreenName(): String {
        return ConstantTransactionAnalytics.ScreenName.CART
    }

    override fun notifyBottomCartParent() {
        if (activity is CartNotifyListener) {
            (activity as CartNotifyListener).onNotifyCart()
        }
    }

    // get newly added cart id if open cart after ATC on PDP
    override fun getCartId(): String {
        val cartId = arguments?.getString(CartActivity.EXTRA_CART_ID).orEmpty()
        return cartId.ifEmpty { "0" }
    }

    private fun getAtcProductId(): Long {
        return arguments?.getLong(CartActivity.EXTRA_PRODUCT_ID) ?: 0L
    }

    private fun isAtcExternalFlow(): Boolean {
        return getAtcProductId() != 0L
    }

    private fun resetArguments() {
        arguments?.putString(CartActivity.EXTRA_CART_ID, null)
        arguments?.putLong(CartActivity.EXTRA_PRODUCT_ID, 0)
    }

    override fun renderRecentView(recommendationWidget: RecommendationWidget?) {
        var cartRecentViewItemHolderDataList: MutableList<CartRecentViewItemHolderData> =
            ArrayList()
        if (recommendationWidget != null) {
            cartRecentViewItemHolderDataList =
                recentViewMapper.convertToViewHolderModelList(recommendationWidget)
        } else {
            this.recentViewList?.let {
                cartRecentViewItemHolderDataList.addAll(it)
            }
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recent_view)

        val cartRecentViewHolderData = CartRecentViewHolderData()
        cartRecentViewHolderData.recentViewList = cartRecentViewItemHolderDataList
        cartAdapter.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.recentViewList = cartRecentViewItemHolderDataList
        shouldReloadRecentViewList = false
    }

    override fun renderWishlistV2(
        wishlists: List<GetWishlistV2Response.Data.WishlistV2.Item>?,
        forceReload: Boolean
    ) {
        var cartWishlistItemHolderDataList: MutableList<CartWishlistItemHolderData> = ArrayList()
        if (this.wishLists != null) {
            if (forceReload && wishlists != null) {
                cartWishlistItemHolderDataList =
                    wishlistMapper.convertToViewHolderModelListV2(wishlists)
            } else {
                cartWishlistItemHolderDataList.addAll(this.wishLists!!)
            }
        } else if (wishlists != null) {
            cartWishlistItemHolderDataList =
                wishlistMapper.convertToViewHolderModelListV2(wishlists)
        }

        val cartWishlistHolderData = cartAdapter.getCartWishlistHolderData()
        cartWishlistHolderData.wishList = cartWishlistItemHolderDataList

        if (this.wishLists == null || !forceReload) {
            val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
            cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_wishlist)
            cartSectionHeaderHolderData.showAllAppLink = ApplinkConst.NEW_WISHLIST
            cartAdapter.addCartWishlistData(cartSectionHeaderHolderData, cartWishlistHolderData)
        } else {
            val wishlistIndex = cartAdapter.updateCartWishlistData(cartWishlistHolderData)
            onNeedToUpdateViewItem(wishlistIndex)
        }

        this.wishLists = cartWishlistItemHolderDataList
    }

    override fun renderRecommendation(recommendationWidget: RecommendationWidget?) {
        val cartRecommendationItemHolderDataList = ArrayList<CartRecommendationItemHolderData>()

        val recommendationItems = recommendationWidget?.recommendationItemList ?: emptyList()
        for (recommendationItem in recommendationItems) {
            val cartRecommendationItemHolderData =
                CartRecommendationItemHolderData(false, recommendationItem)
            cartRecommendationItemHolderDataList.add(cartRecommendationItemHolderData)
        }

        var cartSectionHeaderHolderData: CartSectionHeaderHolderData? = null
        if (recommendationPage == 1) {
            cartSectionHeaderHolderData = CartSectionHeaderHolderData()
            if (!TextUtils.isEmpty(recommendationWidget?.title)) {
                cartSectionHeaderHolderData.title = recommendationWidget?.title ?: ""
            } else {
                cartSectionHeaderHolderData.title =
                    getString(R.string.checkout_module_title_recommendation)
            }
        }

        if (cartRecommendationItemHolderDataList.size > 0) {
            cartAdapter.addCartRecommendationData(
                cartSectionHeaderHolderData,
                cartRecommendationItemHolderDataList,
                recommendationPage
            )
        } else {
            cartAdapter.addCartTopAdsHeadlineData(cartSectionHeaderHolderData, recommendationPage)
        }

        recommendationPage++
    }

    override fun showItemLoading() {
        cartAdapter.addCartLoadingData()
    }

    override fun hideItemLoading() {
        cartAdapter.removeCartLoadingData()
        endlessRecyclerViewScrollListener.updateStateAfterGetData()
        hasLoadRecommendation = true
    }

    override fun triggerSendEnhancedEcommerceAddToCartSuccess(
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
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(
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
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(
                    productModel,
                    addToCartDataResponseModel,
                    FLAG_IS_CART_EMPTY
                )
            }
            is CartRecommendationItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART
                eventLabel = ""
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(
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

    override fun setHasTriedToLoadRecentView() {
        hasTriedToLoadRecentViewList = true
    }

    override fun setHasTriedToLoadWishList() {
        hasTriedToLoadWishList = true
    }

    override fun setHasTriedToLoadRecommendation() {
        hasTriedToLoadRecommendation = true
    }

    override fun onDeleteAllDisabledProduct() {
        val allDisabledCartItemDataList = cartAdapter.allDisabledCartItemData
        val allCartItemDataList = cartAdapter.allCartItemData

        for (cartItemData in allDisabledCartItemDataList) {
            if (cartItemData.selectedUnavailableActionId == Action.ACTION_CHECKOUTBROWSER) {
                cartPageAnalytics.eventClickHapusButtonOnProductContainTobacco()
                break
            }
        }
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(
            dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
        )

        if (allDisabledCartItemDataList.isNotEmpty()) {
            val dialog =
                getMultipleDisabledItemsDialogDeleteConfirmation(allDisabledCartItemDataList.size)
            dialog?.setPrimaryCTAClickListener {
                var forceExpand = false
                if (allDisabledCartItemDataList.size > 1 && unavailableItemAccordionCollapseState) {
                    collapseOrExpandDisabledItem()
                    forceExpand = true
                }
                dPresenter.processDeleteCartItem(
                    allCartItemDataList,
                    allDisabledCartItemDataList,
                    false,
                    forceExpand
                )
                cartPageAnalytics.eventClickDeleteAllUnavailableProduct(userSession.userId)
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(
                    dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
                )
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog?.show()
        }
    }

    override fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action) {
        cartPageAnalytics.eventClickCheckoutMelaluiBrowserOnUnavailableSection(
            userSession.userId,
            data.productId,
            data.errorType
        )
        cartPageAnalytics.eventClickBrowseButtonOnTickerProductContainTobacco()
        dPresenter.redirectToLite(url)
    }

    override fun onShowTickerTobacco() {
        cartPageAnalytics.eventViewTickerProductContainTobacco()
    }

    override fun getAdsId(): String? {
        val localCacheHandler = LocalCacheHandler(activity, ADVERTISINGID)
        val adsId = localCacheHandler.getString(KEY_ADVERTISINGID)

        return adsId
    }

    override fun goToLite(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun updateCartCounter(counter: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (counter > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, counter)
        cache.applyEditor()
    }

    override fun showPromoCheckoutStickyButtonInactive() {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.INACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title =
                getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_label)
            promoCheckoutBtnCart.desc =
                getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_desc)
            promoCheckoutBtnCart.setOnClickListener {
                renderPromoCheckoutLoading()
                dPresenter.doValidateUse(generateParamValidateUsePromoRevamp())
            }
        }
    }

    override fun showPromoCheckoutStickyButtonLoading() {
        renderPromoCheckoutLoading()
    }

    override fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel) {
        val lastApplyUiModel =
            LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
        renderPromoCheckoutButton(lastApplyUiModel)
        dPresenter.updatePromoSummaryData(lastApplyUiModel)
        if (promoUiModel.globalSuccess) {
            dPresenter.setValidateUseLastResponse(ValidateUsePromoRevampUiModel(promoUiModel = promoUiModel))
        }
    }

    override fun onCartItemQuantityChanged(
        cartItemHolderData: CartItemHolderData,
        newQuantity: Int
    ) {
        if (cartItemHolderData.isBundlingItem) {
            val cartShopHolderData =
                cartAdapter.getCartShopHolderDataByCartItemHolderData(cartItemHolderData)
            cartShopHolderData?.let {
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
        val params = generateParamValidateUsePromoRevamp()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else if (cartItemHolderData.isTokoNow) {
            if (tokoNowProductUpdater == null) {
                compositeSubscription.add(
                    Observable.create({ e: Emitter<Boolean> ->
                        tokoNowProductUpdater = e
                        tokoNowProductUpdater?.onNext(true)
                    }, Emitter.BackpressureMode.LATEST)
                        .debounce(TOKONOW_UPDATER_DEBOUNCE, TimeUnit.MILLISECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                            dPresenter.processUpdateCartData(true, true)
                        }
                )
            } else {
                tokoNowProductUpdater?.onNext(true)
            }
        }
    }

    private fun validateGoToCheckout() {
        cartAdapter.checkForShipmentForm()
    }

    override fun navigateToPromoRecommendation() {
        routeToPromoCheckoutMarketplacePage()
    }

    override fun generateGeneralParamValidateUse(): ValidateUsePromoRequest {
        return generateParamValidateUsePromoRevamp()
    }

    override fun checkHitValidateUseIsNeeded(params: ValidateUsePromoRequest): Boolean {
        return isNeedHitUpdateCartAndValidateUse(params)
    }

    override fun resetRecentViewList() {
        shouldReloadRecentViewList = true
    }

    override fun sendATCTrackingURLRecent(productModel: CartRecentViewItemHolderData) {
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

    override fun sendATCTrackingURL(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl
        val url = "${recommendationItem.clickUrl}&click_source=ATC_direct_click"

        activity?.let {
            TopAdsUrlHitter(CartFragment::class.qualifiedName).hitClickUrl(
                it,
                url,
                productId,
                productName,
                imageUrl
            )
        }
    }

    override fun sendATCTrackingURL(bannerShopProductUiModel: BannerShopProductUiModel) {
        val productId = bannerShopProductUiModel.productId.toString()
        val productName = bannerShopProductUiModel.productName
        val imageUrl = bannerShopProductUiModel.imageUrl
        val url = "${bannerShopProductUiModel.adsClickUrl}&click_source=ATC_direct_click"

        activity?.let {
            TopAdsUrlHitter(CartFragment::class.qualifiedName).hitClickUrl(
                it,
                url,
                productId,
                productName,
                imageUrl
            )
        }
    }

    override fun onCollapseAvailableItem(index: Int) {
        val cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(index)
        if (cartShopHolderData != null) {
            cartShopHolderData.isCollapsed = true
            onNeedToUpdateViewItem(index)
            val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
            if (layoutManager != null) {
                val offset = resources.getDimensionPixelOffset(R.dimen.dp_40)
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(index, offset)
            }
        }
    }

    override fun onExpandAvailableItem(index: Int) {
        val cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(index)
        if (cartShopHolderData != null) {
            if (cartShopHolderData.productUiModelList.size > TOKONOW_SEE_OTHERS_OR_ALL_LIMIT) {
                cartPageAnalytics.eventClickLihatOnPlusLainnyaOnNowProduct(cartShopHolderData.shopId)
            } else {
                cartPageAnalytics.eventClickLihatSelengkapnyaOnNowProduct(cartShopHolderData.shopId)
            }
            cartShopHolderData.isCollapsed = false
            onNeedToUpdateViewItem(index)
        }
    }

    override fun onCollapsedProductClicked(index: Int, cartItemHolderData: CartItemHolderData) {
        val (cartShopHolderData, shopIndex) = cartAdapter.getCartShopHolderDataAndIndexByCartString(
            cartItemHolderData.cartString
        )
        if (cartShopHolderData != null) {
            cartPageAnalytics.eventClickCollapsedProductImage(cartShopHolderData.shopId)
            cartShopHolderData.isCollapsed = false
            cartShopHolderData.clickedCollapsedProductIndex = index
            onNeedToUpdateViewItem(shopIndex)
        }
    }

    override fun scrollToClickedExpandedProduct(index: Int, offset: Int) {
        binding?.rvCart?.post {
            val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
            if (layoutManager != null) {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(index, offset)
            }
        }
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

    private fun collapseOrExpandDisabledItem() {
        cartAdapter.getDisabledAccordionHolderData()?.let {
            it.isCollapsed = !it.isCollapsed
            unavailableItemAccordionCollapseState = it.isCollapsed
            collapseOrExpandDisabledItem(it)
        }
    }

    private fun collapseOrExpandDisabledItem(data: DisabledAccordionHolderData) {
        cartAdapter.collapseOrExpandDisabledItemAccordion(data)
        if (data.isCollapsed) {
            cartAdapter.collapseDisabledItems()
        } else {
            cartAdapter.expandDisabledItems()
        }
    }

    override fun onCashbackUpdated(amount: Int) {
        // No-op
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

    override fun reCollapseExpandedDeletedUnavailableItems() {
        collapseOrExpandDisabledItem()
    }

    private fun getMultipleDisabledItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(
                    getString(
                        R.string.label_dialog_title_delete_disabled_multiple_item,
                        count
                    )
                )
                setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_disabled_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
                setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            }
        }

        return null
    }

    private fun getMultipleItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_multiple_item, count))
                setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete_simple))
                setSecondaryCTAText(getString(R.string.label_move_to_wishlist))
            }
        }

        return null
    }

    override fun onEditBundleClicked(cartItemHolderData: CartItemHolderData) {
        activity?.let {
            cartPageAnalytics.eventClickUbahInProductBundlingPackageProductCard(
                cartItemHolderData.bundleId,
                cartItemHolderData.bundleType
            )
            val intent = RouteManager.getIntent(it, cartItemHolderData.editBundleApplink)
            toBeDeletedBundleGroupId = cartItemHolderData.bundleGroupId
            startActivityForResult(intent, NAVIGATION_EDIT_BUNDLE)
        }
    }

    override fun updateCartShopGroupTicker(cartShopHolderData: CartShopHolderData) {
        val (data, index) = cartAdapter.getCartShopHolderDataAndIndexByCartString(cartShopHolderData.cartString)
        if (data != null) {
            data.isNeedToRefreshWeight = true
            onNeedToUpdateViewItem(index)
        }
    }

    override fun checkCartShopGroupTicker(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.cartShopGroupTicker.enableTicker && !cartShopHolderData.isError && cartShopHolderData.hasSelectedProduct) {
            cartShopHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.LOADING
            dPresenter.checkCartShopGroupTicker(cartShopHolderData)
        }
    }
}
