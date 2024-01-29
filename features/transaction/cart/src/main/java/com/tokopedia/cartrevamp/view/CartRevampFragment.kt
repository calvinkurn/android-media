package com.tokopedia.cartrevamp.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
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
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartOnBoardingData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.LocalizationChooseAddress
import com.tokopedia.cart.databinding.FragmentCartRevampBinding
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.CartFragment
import com.tokopedia.cart.view.ICartListPresenter.Companion.GET_CART_STATE_AFTER_CHOOSE_ADDRESS
import com.tokopedia.cart.view.di.DaggerCartRevampComponent
import com.tokopedia.cart.view.uimodel.CartDeleteButtonSource
import com.tokopedia.cartcommon.data.response.common.Button
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartrevamp.view.adapter.cart.CartAdapter
import com.tokopedia.cartrevamp.view.adapter.cart.CartItemAdapter
import com.tokopedia.cartrevamp.view.bottomsheet.CartBundlingBottomSheet
import com.tokopedia.cartrevamp.view.bottomsheet.CartBundlingBottomSheetListener
import com.tokopedia.cartrevamp.view.bottomsheet.CartNoteBottomSheet
import com.tokopedia.cartrevamp.view.bottomsheet.showGlobalErrorBottomsheet
import com.tokopedia.cartrevamp.view.compoundview.CartToolbarListener
import com.tokopedia.cartrevamp.view.customview.CartViewBinderHelper
import com.tokopedia.cartrevamp.view.decorator.CartItemDecoration
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.mapper.BmGmTickerRequestMapper
import com.tokopedia.cartrevamp.view.mapper.CartUiModelMapper
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.cartrevamp.view.mapper.RecentViewMapper
import com.tokopedia.cartrevamp.view.mapper.WishlistMapper
import com.tokopedia.cartrevamp.view.pref.CartOnBoardingPreferences
import com.tokopedia.cartrevamp.view.uimodel.AddCartToWishlistV2Event
import com.tokopedia.cartrevamp.view.uimodel.AddToCartEvent
import com.tokopedia.cartrevamp.view.uimodel.AddToCartExternalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartDeleteItemData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartMainCoachMarkUiModel
import com.tokopedia.cartrevamp.view.uimodel.CartNoteBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cartrevamp.view.uimodel.CartState
import com.tokopedia.cartrevamp.view.uimodel.CartTrackerEvent
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DeleteCartEvent
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.EntryPointInfoEvent
import com.tokopedia.cartrevamp.view.uimodel.FollowShopEvent
import com.tokopedia.cartrevamp.view.uimodel.GetBmGmGroupProductTickerState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.cartrevamp.view.uimodel.RemoveFromWishlistEvent
import com.tokopedia.cartrevamp.view.uimodel.SeamlessLoginEvent
import com.tokopedia.cartrevamp.view.uimodel.UndoDeleteEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartAndGetLastApplyEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.cartrevamp.view.util.CartPageAnalyticsUtil
import com.tokopedia.cartrevamp.view.viewholder.CartItemViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.cartrevamp.view.viewholder.CartSelectedAmountViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.util.analytics.PromoUsageEntryPointAnalytics
import com.tokopedia.promousage.util.logger.PromoErrorException
import com.tokopedia.promousage.view.bottomsheet.PromoUsageBottomSheet
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
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_ACTIVE
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_INACTIVE
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_LOADING
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_DETAIL_TYPE_BMGM
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.revamp.PromoEntryPointImprovementRollenceManager
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.abs
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@Keep
class CartRevampFragment :
    BaseCheckoutFragment(),
    ActionListener,
    CartToolbarListener,
    CartItemAdapter.ActionListener,
    RefreshHandler.OnRefreshHandlerListener,
    SellerCashbackListener,
    CartBundlingBottomSheetListener,
    PromoUsageBottomSheet.Listener {

    private var binding by autoClearedNullable<FragmentCartRevampBinding>()

    private var cartAdapter: CartAdapter? = null
    private var refreshHandler: RefreshHandler? = null
    private var progressDialog: LoaderDialog? = null

    @Inject
    lateinit var cartItemDecoration: CartItemDecoration

    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart

    @Inject
    lateinit var promoEntryPointAnalytics: PromoUsageEntryPointAnalytics

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

    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var hasLoadRecommendation: Boolean = false

    private var hasTriedToLoadWishList: Boolean = false
    private var hasTriedToLoadRecentViewList: Boolean = false
    private var shouldReloadRecentViewList: Boolean = false
    private var hasTriedToLoadRecommendation: Boolean = false
    private var delayShowPromoButtonJob: Job? = null
    private var delayShowSelectedAmountJob: Job? = null
    private var promoTranslationLength = 0f
    private var selectedAmountTranslationLength = 0f
    private var isKeyboardOpened = false
    private var initialSelectedAmountPosition = 0f
    private var initialPromoButtonPosition = 0f
    private var unavailableItemAccordionCollapseState = true
    private var hasCalledOnSaveInstanceState = false
    private var isCheckUncheckDirectAction = true
    private var plusCoachMark: CoachMark2? = null
    private var mainFlowCoachMark: CoachMark2? = null
    private var bulkActionCoachMark: CoachMark2? = null

    private var isFirstCheckEvent: Boolean = true
    private var hasShowBulkActionCoachMark: Boolean = false
    private var bulkActionCoachMarkLastActiveIndex: Int = 0
    private var isMockMainFlowCoachMarkShown = false

    private var wishlistIcon: IconUnify? = null
    private var animatedWishlistImage: ImageView? = null

    private val binderHelper = CartViewBinderHelper()

    private var editBundleActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResultFromEditBundle(result.resultCode, result.data)
        }
    private var shipmentActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResultFromShipmentPage(result.resultCode, result.data)
        }
    private var pdpActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onResultFromPdp()
        }
    private var promoActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResultFromPromoPage(result.resultCode, result.data)
        }
    private var activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshCartWithSwipeToRefresh()
        }
    private var addonResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResultFromAddOnBottomSheet(result.resultCode, result.data)
        }

    private val cartSwipeToDeleteOnBoardingPreferences: CartOnBoardingPreferences by lazy {
        CartOnBoardingPreferences(requireContext())
    }
    private val swipeToDeleteOnBoardingFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private var enablePromoEntryPointNewInterface: Boolean = false

    companion object {
        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

        private const val SPAN_SIZE_ZERO = 0
        private const val SPAN_SIZE_ONE = 1
        private const val SPAN_SIZE_TWO = 2

        private const val CART_BULK_ACTION_COACH_MARK = "cart_bulk_action_coach_mark"

        const val CART_TRACE = "mp_cart"
        const val CART_ALL_TRACE = "mp_cart_all"
        const val CART_PAGE = "cart"
        const val WISHLIST_SOURCE_AVAILABLE_ITEM = "WISHLIST_SOURCE_AVAILABLE_ITEM"
        const val WISHLIST_SOURCE_UNAVAILABLE_ITEM = "WISHLIST_SOURCE_UNAVAILABLE_ITEM"
        const val WORDING_GO_TO_HOMEPAGE = "Kembali ke Homepage"
        const val HEIGHT_DIFF_CONSTRAINT = 100
        const val DELAY_SHOW_PROMO_BUTTON_AFTER_SCROLL = 750L
        const val DELAY_SHOW_SELECTED_AMOUNT_AFTER_SCROLL = 750L
        const val PROMO_ANIMATION_DURATION = 500L
        const val SELECTED_AMOUNT_ANIMATION_DURATION = 500L
        const val COACHMARK_VISIBLE_DELAY_DURATION = 500L
        const val DELAY_SHOW_SWIPE_TO_DELETE_ONBOARDING = 1000L
        const val DELAY_CHECK_BOX_GLOBAL = 500L
        const val KEY_OLD_BUNDLE_ID = "old_bundle_id"
        const val KEY_NEW_BUNDLE_ID = "new_bundle_id"
        const val KEY_IS_CHANGE_VARIANT = "is_variant_changed"

        private const val QUANTITY_MAX_LIMIT = 999

        private const val MAIN_FLOW_ONBOARDING_NOTES_INDEX = 0
        private const val MAIN_FLOW_ONBOARDING_WISHLIST_INDEX = 1
        private const val MAIN_FLOW_ONBOARDING_SELECT_ALL_INDEX = 2
        private const val BULK_ACTION_ONBOARDING_SELECTED_AMOUNT_DELETE_INDEX = 3
        private const val BULK_ACTION_ONBOARDING_MIN_QUANTITY_INDEX = 4

        private const val TOKONOW_UPDATER_DEBOUNCE = 500L
        private const val TOKONOW_SEE_OTHERS_OR_ALL_LIMIT = 10
        private const val BMGM_TICKER_RELOAD_ACTION = "RELOAD"

        private const val SWIPE_TO_DELETE_ANIMATION_DELAY = 300L
        private const val SWIPE_TO_DELETE_STOP_ANIMATION_DELAY = 200L
        private const val SWIPE_TO_DELETE_TRANSLATION_LENGTH = -60f

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
        cartAdapter = CartAdapter(
            this,
            this,
            this,
            userSession,
            binderHelper
        )
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
        }

        initViewListener()
        initRecyclerView()
        initTopLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            if (viewModel.cartModel.wishlists == null && viewModel.cartModel.recentViewList == null) {
                EmbraceMonitoring.startMoments(EmbraceKey.KEY_MP_CART)
                cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE)
                cartAllPerformanceMonitoring =
                    PerformanceMonitoring.start(CART_ALL_TRACE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        plusCoachMark = CoachMark2(context ?: requireContext())
        plusCoachMark?.let {
            cartAdapter?.setCoachMark(it)
        }

        // Check if currently not refreshing, not ATC external flow and not on error state
        if (refreshHandler?.isRefreshing == false && !isAtcExternalFlow() && binding?.layoutGlobalError?.visibility != View.VISIBLE) {
            if (cartAdapter?.itemCount == 0) {
                viewModel.processInitialGetCartData(
                    getCartId(),
                    viewModel.cartDataList.value.isEmpty(),
                    true
                )
            }
        }
    }

    override fun onPause() {
        plusCoachMark?.dismissCoachMark()
        plusCoachMark = null

        super.onPause()
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
        initSharedFlow()
        initViewModel()
        initCoachMark()
        binding?.rvCart?.setViewBinderHelper(binderHelper)
    }

    override fun getFragment(): Fragment {
        return this
    }

    override fun getScreenName(): String {
        return ConstantTransactionAnalytics.ScreenName.CART
    }

    override fun onClickShopNow() {
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart()
        routeToHome()
    }

    override fun onCartGroupNameClicked(
        appLink: String,
        shopId: String,
        shopName: String,
        isOWOC: Boolean
    ) {
        sendCartImpressionAnalytic()
        if (!isOWOC && shopId.isNotEmpty()) {
            cartPageAnalytics.eventClickAtcCartClickShop(shopId, shopName)
        }
        routeToApplink(appLink)
    }

    override fun onCartShopNameClicked(shopId: String?, shopName: String?, isTokoNow: Boolean) {
        if (shopId != null && shopName != null) {
            cartPageAnalytics.eventClickAtcCartClickShop(shopId, shopName)
            sendCartImpressionAnalytic()
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
        viewModel.reCalculateSubTotal()
        val data = viewModel.cartDataList.value[index]
        var isCollapsed = false
        var productSize = 1
        if (data is CartGroupHolderData) {
            isCollapsed = data.isCollapsed
            productSize += data.productUiModelList.size
            if (checked) {
                checkCartShopGroupTicker(data)
            }
            checkBmGmOffers(data)
        }
        if (isCollapsed) {
            onNeedToUpdateViewItem(index)
            onNeedToUpdateViewItem(index + 1)
        } else {
            onNeedToUpdateMultipleViewItem(index, productSize + 1)
        }
        validateGoToCheckout()
        viewModel.saveCheckboxState()

        val params = viewModel.generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }

        setCheckboxGlobalState()
        setSelectedAmountVisibility()

        if (viewModel.selectedAmountState.value.second > 0 && !hasShowBulkActionCoachMark && !CoachMarkPreference.hasShown(
                requireContext(),
                CART_BULK_ACTION_COACH_MARK
            )
        ) {
            showBulkActionCoachMark()
        } else if (!CartDataHelper.hasSelectedCartItem(viewModel.cartDataList.value)) {
            hasShowBulkActionCoachMark = false
            bulkActionCoachMark?.dismissCoachMark()
            bulkActionCoachMarkLastActiveIndex = 0
        }
    }

    override fun onCartShopGroupTickerClicked(cartGroupHolderData: CartGroupHolderData) {
        when (cartGroupHolderData.cartShopGroupTicker.action) {
            CartShopGroupTickerData.ACTION_REDIRECT_PAGE -> {
                if (cartGroupHolderData.cartShopGroupTicker.applink.isNotBlank()) {
                    val intent = RouteManager.getIntent(
                        requireContext(),
                        cartGroupHolderData.cartShopGroupTicker.applink
                    )
                    activityResultLauncher.launch(intent)
                }
            }

            CartShopGroupTickerData.ACTION_OPEN_BOTTOM_SHEET_BUNDLING -> {
                showCartBundlingBottomSheet(cartGroupHolderData.cartShopGroupTicker.cartBundlingBottomSheetData)
                cartPageAnalytics.eventClickCartShopGroupTickerForBundleCrossSell(
                    cartGroupHolderData.cartShopGroupTicker.tickerText
                )
            }

            else -> {
                // no-op
            }
        }
        if (cartGroupHolderData.cartShopGroupTicker.enableBoAffordability) {
            cartPageAnalytics.eventClickArrowInBoTickerToReachShopPage(
                cartGroupHolderData.cartShopGroupTicker.cartIds,
                cartGroupHolderData.shop.shopId
            )
        }
    }

    override fun onCartShopGroupTickerRefreshClicked(
        index: Int,
        cartGroupHolderData: CartGroupHolderData
    ) {
        cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.LOADING
        onNeedToUpdateViewItem(index)
        viewModel.checkCartShopGroupTicker(cartGroupHolderData)
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
            if (cartGroupHolderData.cartShopGroupTicker.needToShowLoading()) {
                cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.LOADING
            }
            viewModel.checkCartShopGroupTicker(cartGroupHolderData)
        }
    }

    private fun checkBmGmOffers(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.cartGroupBmGmHolderData.hasBmGmOffer) {
            val listCartStringOrderWithBmGmOfferId = arrayListOf<String>()
            cartGroupHolderData.productUiModelList.forEach {
                val cartStringOrderWithBmGmOfferId =
                    "${it.cartStringOrder}||${it.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}"
                if (it.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM &&
                    !listCartStringOrderWithBmGmOfferId.contains(cartStringOrderWithBmGmOfferId)
                ) {
                    listCartStringOrderWithBmGmOfferId.add(cartStringOrderWithBmGmOfferId)
                }
            }
            if (listCartStringOrderWithBmGmOfferId.isNotEmpty()) {
                listCartStringOrderWithBmGmOfferId.forEach { cartStringOrderAndBmGmOfferId ->
                    val cartStringOrderOfferIdSplit = cartStringOrderAndBmGmOfferId.split("||")
                    if (cartStringOrderOfferIdSplit.isNotEmpty() && cartStringOrderOfferIdSplit.size > 1) {
                        val (index, cartItems) = CartDataHelper.getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
                            viewModel.cartDataList.value,
                            cartStringOrderOfferIdSplit[0],
                            cartStringOrderOfferIdSplit[1].toLongOrZero()
                        )
                        if (index > RecyclerView.NO_POSITION) {
                            for (cartItem in cartItems) {
                                cartItem.cartBmGmTickerData.stateTickerBmGm =
                                    CART_BMGM_STATE_TICKER_LOADING
                            }
                            cartAdapter?.notifyItemChanged(index)
                            getGroupProductTicker(cartItems)
                        }
                    }
                }
            }
        }
    }

    private fun onCartDataEnableToCheckout() {
        if (isAdded) {
            binding?.vDisabledGoToCourierPageButton?.gone()
            binding?.goToCourierPageButton?.isEnabled = true
            binding?.goToCourierPageButton?.setOnClickListener {
                guardCartClick {
                    EmbraceMonitoring.startMoments(EmbraceKey.KEY_ACT_BUY)
                    checkGoToShipment("")
                }
            }
        }
    }

    private fun onCartDataDisableToCheckout(isClickable: Boolean = false) {
        if (isAdded) {
            binding?.goToCourierPageButton?.isEnabled = isClickable
            binding?.vDisabledGoToCourierPageButton?.show()
            if (isClickable) {
                binding?.vDisabledGoToCourierPageButton?.setOnClickListener {
                    guardCartClick {
                        if (CartDataHelper.getAllAvailableCartItemData(viewModel.cartDataList.value)
                            .isNotEmpty()
                        ) {
                            showToastMessageGreen(getString(R.string.message_no_cart_item_selected))
                        }
                    }
                }
            } else {
                binding?.vDisabledGoToCourierPageButton?.setOnClickListener(null)
            }
        }
    }

    override fun onShowAllItem(appLink: String) {
        routeToApplink(appLink)
    }

    override fun onRemoveWishlistFromWishlist(productId: String) {
        cartPageAnalytics.eventClickRemoveWishlist(userSession.userId, productId)

        viewModel.processRemoveFromWishlistV2(
            productId,
            userSession.userId,
            false
        )
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
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val clickUrl = recommendationItem.clickUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        var index = 1
        var recommendationItemClick: RecommendationItem? = null
        val recommendationList = CartDataHelper.getRecommendationItem(
            cartAdapter?.itemCount ?: 0,
            viewModel.cartDataList.value
        )
        for ((_, item) in recommendationList) {
            if (item.productId.toString().equals(productId, ignoreCase = true)) {
                recommendationItemClick = item
                break
            }
            index++
        }

        recommendationItemClick?.let {
            cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(
                viewModel.generateRecommendationDataOnClickAnalytics(
                    it,
                    FLAG_IS_CART_EMPTY,
                    index
                )
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
        val recommendationList = CartDataHelper.getRecommendationItem(
            cartAdapter?.itemCount ?: 0,
            viewModel.cartDataList.value
        )
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
        val allDisabledCartItemDataList = CartDataHelper.getAllDisabledCartItemData(
            viewModel.cartDataList.value,
            viewModel.cartModel
        )
        for (cartItemData in allDisabledCartItemDataList) {
            if (cartItemData.selectedUnavailableActionId == Action.ACTION_CHECKOUTBROWSER) {
                cartPageAnalytics.eventClickHapusButtonOnProductContainTobacco()
                break
            }
        }
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(
            viewModel.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
        )

        if (allDisabledCartItemDataList.isNotEmpty()) {
            val dialog =
                getMultipleDisabledItemsDialogDeleteConfirmation(allDisabledCartItemDataList.size)
            dialog?.setPrimaryCTAClickListener {
                var forceExpand = false
                if (allDisabledCartItemDataList.size > 3 && unavailableItemAccordionCollapseState) {
                    collapseOrExpandDisabledItem()
                    forceExpand = true
                }
                val cartDeleteItemData = CartDeleteItemData(
                    removedCartItems = allDisabledCartItemDataList,
                    addWishList = false,
                    forceExpandCollapsedUnavailableItems = forceExpand
                )
                viewModel.processDeleteCartItem(cartDeleteItemData)
                cartPageAnalytics.eventClickDeleteAllUnavailableProduct(userSession.userId)
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(
                    viewModel.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
                )
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog?.show()
        }
    }

    override fun onSeeErrorProductsClicked() {
        scrollToUnavailableSection()
    }

    override fun onCollapseAvailableItem(index: Int) {
        val cartShopBottomHolderData =
            CartDataHelper.getCartShopBottomHolderDataFromIndex(viewModel.cartDataList.value, index)
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
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    index - 1 - cartShopBottomHolderData.shopData.productUiModelList.size,
                    0
                )
            }
        }
    }

    override fun onExpandAvailableItem(index: Int) {
        val cartShopBottomHolderData = CartDataHelper.getCartShopBottomHolderDataFromIndex(
            viewModel.cartDataList.value,
            index
        )
        if (cartShopBottomHolderData != null) {
            if (cartShopBottomHolderData.shopData.productUiModelList.size > TOKONOW_SEE_OTHERS_OR_ALL_LIMIT) {
                cartPageAnalytics.eventClickLihatOnPlusLainnyaOnNowProduct(cartShopBottomHolderData.shopData.shop.shopId)
            } else {
                cartPageAnalytics.eventClickLihatSelengkapnyaOnNowProduct(cartShopBottomHolderData.shopData.shop.shopId)
            }
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
        val (shopIndex, groupData) = CartDataHelper.getCartGroupHolderDataAndIndexByCartString(
            viewModel.cartDataList.value,
            cartItemHolderData.cartString,
            false
        )
        if (shopIndex >= 0) {
            val cartGroupHolderData = groupData.first()
            if (cartGroupHolderData is CartGroupHolderData) {
                val newCartGroupHolderData = cartGroupHolderData.copy(
                    isCollapsed = false,
                    clickedCollapsedProductIndex = index
                )
                cartPageAnalytics.eventClickCollapsedProductImage(newCartGroupHolderData.shop.shopId)
                viewModel.cartDataList.value[shopIndex] = newCartGroupHolderData
                viewModel.addItems(shopIndex + 1, newCartGroupHolderData.productUiModelList)
                val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    layoutManager.scrollToPositionWithOffset(
                        shopIndex + 1 + index,
                        60.dpToPx(requireContext().resources.displayMetrics)
                    )
                }
                val cartShopBottomHolderData = groupData.last()
                if (cartShopBottomHolderData is CartShopBottomHolderData) {
                    viewModel.cartDataList.value[shopIndex + 1 + newCartGroupHolderData.productUiModelList.size] =
                        CartShopBottomHolderData(newCartGroupHolderData)
                }
                viewModel.cartDataList.notifyObserver()
            }
        }
    }

    override fun scrollToClickedExpandedProduct(index: Int, offset: Int) {
        binding?.rvCart?.post {
            val layoutManager: RecyclerView.LayoutManager? = binding?.rvCart?.layoutManager
            if (layoutManager is LinearLayoutManager) {
                layoutManager.scrollToPositionWithOffset(index, offset)
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
        val cartDataList = viewModel.cartDataList.value
        val disabledItemHeaderHolderDataPosition =
            cartDataList.indexOfFirst { it is DisabledItemHeaderHolderData }
        (cartDataList[disabledItemHeaderHolderDataPosition] as DisabledItemHeaderHolderData).isDividerShown =
            !data.isCollapsed
        onNeedToUpdateViewItem(disabledItemHeaderHolderDataPosition)
        unavailableItemAccordionCollapseState = data.isCollapsed
        collapseOrExpandDisabledItem(data)
    }

    override fun onToggleUnavailableItemAccordion() {
        val cartDataList = viewModel.cartDataList.value
        val accordionHolderDataPosition =
            cartDataList.indexOfFirst { it is DisabledAccordionHolderData }
        val data = cartDataList.getOrNull(accordionHolderDataPosition)

        if (data != null && data is DisabledAccordionHolderData) {
            data.isCollapsed = !data.isCollapsed
            val disabledItemHeaderHolderDataPosition =
                cartDataList.indexOfFirst { it is DisabledItemHeaderHolderData }
            (cartDataList[disabledItemHeaderHolderDataPosition] as DisabledItemHeaderHolderData).isDividerShown =
                !data.isCollapsed
            onNeedToUpdateViewItem(disabledItemHeaderHolderDataPosition)
            unavailableItemAccordionCollapseState = data.isCollapsed
            collapseOrExpandDisabledItem(data)
        }
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

    override fun onGlobalDeleteClicked() {
        cartPageAnalytics.eventClickGlobalDelete()
        val deletedCartItems = CartDataHelper.getSelectedCartItemData(viewModel.cartDataList.value)
        val dialog = getMultipleItemsDialogDeleteConfirmation(deletedCartItems.size)
        val listCartStringOrderNeedUpdated =
            CartDataHelper.checkSelectedCartItemDataWithOfferBmGm(viewModel.cartDataList.value)
        dialog?.setPrimaryCTAClickListener {
            val cartDeleteItemData = CartDeleteItemData(
                removedCartItems = deletedCartItems,
                addWishList = false,
                isFromGlobalCheckbox = true,
                listCartStringOrderAndBmGmOfferId = listCartStringOrderNeedUpdated
            )
            viewModel.processDeleteCartItem(cartDeleteItemData)
            dialog.dismiss()
        }
        dialog?.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog?.show()
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

    override fun onCartItemDeleteButtonClicked(
        cartItemHolderData: CartItemHolderData,
        deleteSource: CartDeleteButtonSource
    ) {
        when (deleteSource) {
            CartDeleteButtonSource.TrashBin -> {
                cartPageAnalytics.eventClickAtcCartClickTrashBin()
            }
            CartDeleteButtonSource.SwipeToDelete -> {
                val analyticItems =
                    CartPageAnalyticsUtil.generateRemoveCartFromSubtractButtonAnalytics(cartItemHolderData)
                cartPageAnalytics.sendEventClickRemoveCartFromSwipe(analyticItems, userSession.userId)
            }
            else -> { /* no-op */ }
        }
        val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
        if (cartItemHolderData.isBundlingItem) {
            val cartGroupHolderData = CartDataHelper.getCartGroupHolderDataByCartItemHolderData(
                viewModel.cartDataList.value,
                cartItemHolderData
            )
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
            val allDisabledCartItemData = CartDataHelper.getAllDisabledCartItemData(
                viewModel.cartDataList.value,
                viewModel.cartModel
            )
            if (allDisabledCartItemData.size > 3 && unavailableItemAccordionCollapseState) {
                collapseOrExpandDisabledItem()
                forceExpand = true
            }

            val listCartStringOrderOfferId = arrayListOf<String>()
            if (cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM) {
                listCartStringOrderOfferId.add("${cartItemHolderData.cartStringOrder}||${cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}")
            }

            val cartDeleteItemData = CartDeleteItemData(
                removedCartItems = toBeDeletedProducts,
                addWishList = false,
                forceExpandCollapsedUnavailableItems = forceExpand,
                isFromGlobalCheckbox = false,
                isFromEditBundle = false,
                listCartStringOrderAndBmGmOfferId = listCartStringOrderOfferId,
                deleteSource = deleteSource
            )
            viewModel.processDeleteCartItem(cartDeleteItemData)
            if (deleteSource == CartDeleteButtonSource.TrashBin) {
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
        sendCartImpressionAnalytic()
        routeToProductDetailPage(cartItemHolderData.productId)
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        cartPageAnalytics.eventClickAtcCartClickInputQuantity(qty)
    }

    override fun onCartItemCheckChanged(position: Int, cartItemHolderData: CartItemHolderData) {
        val selected = !cartItemHolderData.isSelected
        viewModel.setItemSelected(position, cartItemHolderData, selected)
        updateStateAfterCheckChanged(selected)
    }

    override fun onCartItemCheckboxClickChanged(
        position: Int,
        cartItemHolderData: CartItemHolderData,
        isChecked: Boolean
    ) {
        if (cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM) {
            val selected = !cartItemHolderData.isSelected
            viewModel.setItemSelected(position, cartItemHolderData, selected)
            updateStateAfterCheckChanged(selected)

            cartItemHolderData.cartBmGmTickerData.stateTickerBmGm = CART_BMGM_STATE_TICKER_LOADING
            cartItemHolderData.isSelected = isChecked
            cartAdapter?.notifyItemChanged(position)
            getGroupProductTicker(cartItemHolderData)
        }
    }

    override fun onBundleItemCheckChanged(cartItemHolderData: CartItemHolderData) {
        val (index, groupData) = CartDataHelper.getCartGroupHolderDataAndIndexByCartString(
            viewModel.cartDataList.value,
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
            updateStateAfterCheckChanged(selected)
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
        val allCartItemData = CartDataHelper.getAllCartItemData(
            viewModel.cartDataList.value,
            viewModel.cartModel
        )
        val isLastItem = allCartItemData.size == 1
        this.wishlistIcon = wishlistIcon
        if (cartItemHolderData.isWishlisted) {
            this.animatedWishlistImage = animatedWishlistImage
            viewModel.processAddCartToWishlist(
                cartItemHolderData.productId,
                userSession.userId,
                isLastItem,
                if (cartItemHolderData.isError) WISHLIST_SOURCE_UNAVAILABLE_ITEM else WISHLIST_SOURCE_AVAILABLE_ITEM
            )
        } else {
            viewModel.processRemoveFromWishlistV2(
                cartItemHolderData.productId,
                userSession.userId,
                true,
                position
            )
        }
    }

    override fun onNoteClicked(
        data: CartItemHolderData,
        noteIcon: ImageView,
        noteLottieIcon: LottieAnimationView,
        position: Int
    ) {
        cartPageAnalytics.eventClickNoteIcon()
        val bottomSheet = CartNoteBottomSheet.newInstance(
            CartNoteBottomSheetData(
                productName = data.productName,
                productImage = data.productImage,
                variant = data.variant,
                note = data.notes
            )
        )
        bottomSheet.setListener(listener = { newNote ->
            cartPageAnalytics.eventClickSaveOnNoteBottomSheet(data.notes.isEmpty(), data.cartId)
            data.notes = newNote
            playNoteAnimation(newNote, noteIcon, noteLottieIcon, position)
        })
        if (bottomSheet.isAdded || childFragmentManager.isStateSaved) return
        bottomSheet.show(childFragmentManager, CartNoteBottomSheet.TAG)
    }

    override fun onNeedToRefreshSingleShop(
        cartItemHolderData: CartItemHolderData,
        itemPosition: Int
    ) {
        val (index, groupData) = CartDataHelper.getCartGroupHolderDataAndIndexByCartString(
            viewModel.cartDataList.value,
            cartItemHolderData.cartString,
            false
        )
        if (index >= 0) {
            val shopHeaderData = groupData.first()
            if (shopHeaderData is CartGroupHolderData) {
                checkCartShopGroupTicker(shopHeaderData)
                onNeedToUpdateViewItem(index)
            }
            onNeedToUpdateViewItem(itemPosition)
        }
    }

    override fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData) {
        val (index, groupData) = CartDataHelper.getCartGroupHolderDataAndIndexByCartString(
            viewModel.cartDataList.value,
            cartItemHolderData.cartString,
            false
        )
        if (index >= 0) {
            val shopHeaderData = groupData.first()
            if (shopHeaderData is CartGroupHolderData) {
                checkCartShopGroupTicker(shopHeaderData)
                onNeedToUpdateViewItem(index)
            }
        }
    }

    override fun onNeedToRecalculate() {
        viewModel.reCalculateSubTotal()
    }

    override fun onCartItemQuantityChanged(
        cartItemHolderData: CartItemHolderData,
        newQuantity: Int
    ) {
        if (cartItemHolderData.isBundlingItem) {
            val cartGroupHolderData = CartDataHelper.getCartGroupHolderDataByCartItemHolderData(
                viewModel.cartDataList.value,
                cartItemHolderData
            )
            cartGroupHolderData?.let { cartGroup ->
                cartGroup.productUiModelList.forEach { cartItem ->
                    if (cartItem.isBundlingItem && cartItem.bundleId == cartItemHolderData.bundleId && cartItem.bundleGroupId == cartItemHolderData.bundleGroupId) {
                        cartItem.bundleQuantity = newQuantity
                    }
                }
            }
        } else {
            cartItemHolderData.quantity = newQuantity
        }

        validateGoToCheckout()
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            if (cartItemHolderData.isTokoNow) {
                viewModel.emitTokonowUpdated()
            }
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }

        if (cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM && cartAdapter != null) {
            val (index, cartItems) = CartDataHelper.getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
                viewModel.cartDataList.value,
                cartItemHolderData.cartStringOrder,
                cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId
            )
            if (index > RecyclerView.NO_POSITION) {
                for (cartItem in cartItems) {
                    cartItem.cartBmGmTickerData.stateTickerBmGm = CART_BMGM_STATE_TICKER_LOADING
                }

                cartAdapter?.notifyItemChanged(index)
                val cartGroupHolderData = CartDataHelper.getCartGroupHolderDataByCartItemHolderData(
                    viewModel.cartDataList.value,
                    cartItemHolderData
                )
                if (cartGroupHolderData != null) {
                    getGroupProductTicker(cartItemHolderData)
                }
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
            editBundleActivityResult.launch(intent)
        }
    }

    override fun onTobaccoLiteUrlClicked(url: String, data: CartItemHolderData, action: Action) {
        cartPageAnalytics.eventClickCheckoutMelaluiBrowserOnUnavailableSection(
            userSession.userId,
            data.productId,
            data.errorType
        )
        cartPageAnalytics.eventClickBrowseButtonOnTickerProductContainTobacco()
        viewModel.redirectToLite(url, DeviceInfo.getAdsId(requireContext()))
    }

    override fun onShowTickerTobacco() {
        cartPageAnalytics.eventViewTickerProductContainTobacco()
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
        cartPageAnalytics.eventClickFollowShop(userSession.userId, errorType, shopId)
        viewModel.followShop(shopId)
    }

    override fun onVerificationClicked(applink: String) {
        activity?.also {
            val intent = RouteManager.getIntentNoFallback(it, applink) ?: return
            startActivityWithRefreshHandler(intent)
        }
    }

    override fun onProductAddOnClicked(cartItemData: CartItemHolderData) {
        val productId = cartItemData.productId
        val cartId = cartItemData.cartId
        val addOnIds = arrayListOf<String>()
        val deselectAddOnIds = arrayListOf<String>()
        cartItemData.addOnsProduct.listData.forEach {
            if (it.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK) {
                addOnIds.add(it.id)
            }
            if (it.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK) {
                deselectAddOnIds.add(it.id)
            }
        }

        val price: Double
        val discountedPrice: Double
        if (cartItemData.campaignId == "0") {
            price = cartItemData.productPrice
            discountedPrice = cartItemData.productPrice
        } else {
            price = cartItemData.productOriginalPrice
            discountedPrice = cartItemData.productPrice
        }

        val applinkAddon =
            ApplinkConst.ADDON.replace(AddOnConstant.QUERY_PARAM_ADDON_PRODUCT, productId)
        val applink = UriUtil.buildUriAppendParams(
            applinkAddon,
            mapOf(
                AddOnConstant.QUERY_PARAM_CART_ID to cartId,
                AddOnConstant.QUERY_PARAM_SELECTED_ADDON_IDS to addOnIds.toString().replace("[", "")
                    .replace("]", ""),
                AddOnConstant.QUERY_PARAM_DESELECTED_ADDON_IDS to deselectAddOnIds.toString()
                    .replace("[", "").replace("]", ""),
                AddOnConstant.QUERY_PARAM_PAGE_ATC_SOURCE to AddOnConstant.SOURCE_NORMAL_CHECKOUT,
                AddOnConstant.QUERY_PARAM_WAREHOUSE_ID to cartItemData.warehouseId,
                AddOnConstant.QUERY_PARAM_IS_TOKOCABANG to cartItemData.isFulfillment,
                AddOnConstant.QUERY_PARAM_CATEGORY_ID to cartItemData.categoryId,
                AddOnConstant.QUERY_PARAM_SHOP_ID to cartItemData.shopHolderData.shopId,
                AddOnConstant.QUERY_PARAM_QUANTITY to cartItemData.quantity,
                AddOnConstant.QUERY_PARAM_PRICE to price.toBigDecimal().toPlainString()
                    .removeSingleDecimalSuffix(),
                AddOnConstant.QUERY_PARAM_DISCOUNTED_PRICE to discountedPrice.toBigDecimal()
                    .toPlainString()
                    .removeSingleDecimalSuffix()
            )
        )

        activity?.let {
            val intent = RouteManager.getIntent(it, applink)
            addonResultLauncher.launch(intent)
        }
    }

    override fun onCashbackUpdated(amount: Int) {
    }

    override fun onBackPressed() {
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

    override fun onNewBundleProductAddedToCart() {
        refreshCartWithSwipeToRefresh()
    }

    override fun onMultipleBundleActionButtonClicked(selectedBundle: BundleDetailUiModel) {
        cartPageAnalytics.eventClickCartBundlingBottomSheetBundleWidgetAction(
            userSession.userId,
            selectedBundle.bundleId,
            ConstantTransactionAnalytics.EventLabel.BUNDLE_TYPE_MULTIPLE,
            viewModel.generateCartBundlingPromotionsAnalyticsData(selectedBundle)
        )
    }

    override fun onSingleBundleActionButtonClicked(selectedBundle: BundleDetailUiModel) {
        cartPageAnalytics.eventClickCartBundlingBottomSheetBundleWidgetAction(
            userSession.userId,
            selectedBundle.bundleId,
            ConstantTransactionAnalytics.EventLabel.BUNDLE_TYPE_SINGLE,
            viewModel.generateCartBundlingPromotionsAnalyticsData(selectedBundle)
        )
    }

    override fun impressionMultipleBundle(selectedMultipleBundle: BundleDetailUiModel) {
        cartPageAnalytics.eventViewCartBundlingBottomSheetBundle(
            userSession.userId,
            selectedMultipleBundle.bundleId,
            ConstantTransactionAnalytics.EventLabel.BUNDLE_TYPE_MULTIPLE
        )
    }

    override fun impressionSingleBundle(selectedBundle: BundleDetailUiModel) {
        cartPageAnalytics.eventViewCartBundlingBottomSheetBundle(
            userSession.userId,
            selectedBundle.bundleId,
            ConstantTransactionAnalytics.EventLabel.BUNDLE_TYPE_SINGLE
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            setActivityBackgroundColor()
            refreshHandler?.isRefreshing = true
            if (viewModel.cartDataList.value.isEmpty()) {
                viewModel.processInitialGetCartData(
                    cartId = getCartId(),
                    initialLoad = true,
                    isLoadingTypeRefresh = false
                )
            } else {
                if (viewModel.dataHasChanged()) {
                    viewModel.processToUpdateAndReloadCartData(getCartId())
                } else {
                    viewModel.processInitialGetCartData(
                        cartId = getCartId(),
                        initialLoad = false,
                        isLoadingTypeRefresh = true
                    )
                }
            }
        }
    }

    override fun onStop() {
        updateCartAfterDetached()

        if (FLAG_SHOULD_CLEAR_RECYCLERVIEW) {
            clearRecyclerView()
        }

        super.onStop()
    }

    override fun onAddOnsProductWidgetImpression(addOnType: Int, productId: String) {
        cartPageAnalytics.eventViewAddOnsProductWidgetCart(addOnType, productId)
    }

    override fun onClickAddOnsProductWidgetCart(addOnType: Int, productId: String) {
        cartPageAnalytics.eventClickAddOnsWidgetCart(addOnType, productId)
    }

    override fun sendRemoveCartFromSubtractButtonAnalytic(cartItemHolderData: CartItemHolderData) {
        val analyticItems =
            CartPageAnalyticsUtil.generateRemoveCartFromSubtractButtonAnalytics(cartItemHolderData)
        cartPageAnalytics.sendRemoveCartFromSubtractButtonEvent(analyticItems, userSession.userId)
    }

    override fun onAvailableCartItemImpression(availableCartItems: List<CartItemHolderData>) {
        viewModel.addAvailableCartItemImpression(availableCartItems)
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
        cartRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener!!)
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

                if (dy != 0) {
                    bulkActionCoachMark?.dismissCoachMark()
                }

                if (shouldShowSwipeToDeleteDefaultProductOnBoarding() || shouldShowSwipeToDeleteBundlingProductOnBoarding()) {
                    lifecycleScope.launch {
                        swipeToDeleteOnBoardingFlow.emit(true)
                    }
                }

                handleSelectedAmountVisibilityOnScroll(dy)
                handlePromoButtonVisibilityOnScroll(dy)
                handleFloatingSelectedAmountVisibility(recyclerView)
            }
        })
    }

    private fun addToCartExternal(productId: Long) {
        viewModel.processAddToCartExternal(productId)
    }

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun animateSelectedAmountToHiddenPosition(valueY: Float) {
        binding?.rlTopLayout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun animatePromoButtonToStartingPosition() {
        binding?.apply {
            val initialPosition = bottomLayout.y - llPromoCheckout.height
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
        val disabledAccordionHolderData =
            CartDataHelper.getDisabledAccordionHolderData(viewModel.cartDataList.value)
        disabledAccordionHolderData?.let {
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

    private fun clearRecyclerView() {
//        cartAdapter.clearCompositeSubscription()
        binding?.rvCart?.removeAllViews()
        binding?.rvCart?.recycledViewPool?.clear()
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

    private fun getBoPromoCodes(): List<String> {
        return when {
            viewModel.cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    viewModel.cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                lastApplyPromo.lastApplyPromoData
                    .listVoucherOrders
                    .filter { it.isTypeLogistic() }
                    .map { it.code }
            }

            viewModel.cartModel.lastValidateUseRequest != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                promoUiModel.voucherOrderUiModels
                    .filter { it.isTypeLogistic() }
                    .map { it.code }
            }

            else -> {
                emptyList()
            }
        }
    }

    private fun generateParamClearBo(): ClearPromoOrderData? {
        return when {
            viewModel.cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    viewModel.cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateClearBoParam(
                    lastApplyPromo,
                    CartDataHelper.getAllAvailableShopGroupDataList(viewModel.cartDataList.value)
                )
            }

            viewModel.cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateClearBoParam(
                    promoUiModel,
                    CartDataHelper.getAllAvailableShopGroupDataList(viewModel.cartDataList.value)
                )
            }

            else -> null
        }
    }

    private fun generateParamGetLastApplyPromo(): ValidateUsePromoRequest {
        return when {
            viewModel.cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    viewModel.cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    lastApplyPromo,
                    CartDataHelper.getSelectedCartGroupHolderData(viewModel.cartDataList.value),
                    null
                )
            }

            viewModel.cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    promoUiModel,
                    CartDataHelper.getSelectedCartGroupHolderData(viewModel.cartDataList.value),
                    viewModel.cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    null,
                    CartDataHelper.getSelectedCartGroupHolderData(viewModel.cartDataList.value),
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
                    CartDataHelper.getAllAvailableShopGroupDataList(viewModel.cartDataList.value),
                    null
                )
            }

            viewModel.cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    viewModel.cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateCouponListRequestParams(
                    promoUiModel,
                    CartDataHelper.getAllAvailableShopGroupDataList(viewModel.cartDataList.value),
                    viewModel.cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateCouponListRequestParams(
                    null,
                    CartDataHelper.getAllAvailableShopGroupDataList(viewModel.cartDataList.value),
                    null
                )
            }
        }
    }

    private fun getCurrentTotalPrice(): Double {
        return viewModel.cartModel.latestCartTotalAmount
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
                                        )?.productPreorder?.durationDay?.toString()
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
                                        )?.productPreorder?.durationDay?.toString()
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
                                        )?.productPreorder?.durationDay?.toString()
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

    private fun goToLite(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun goToPromoPage() {
        viewModel.doUpdateCartForPromo()
    }

    private fun handleCheckboxGlobalChangeEvent() {
        val isChecked = binding?.checkboxGlobal?.isChecked ?: return
        if (isCheckUncheckDirectAction) {
            viewModel.setAllAvailableItemCheck(isChecked)
            viewModel.updateSelectedAmount()
            viewModel.reCalculateSubTotal()
            viewModel.saveCheckboxState()
            setSelectedAmountVisibility()
            cartPageAnalytics.eventCheckUncheckGlobalCheckbox(isChecked)

            if (isChecked && !isFirstCheckEvent && !CoachMarkPreference.hasShown(
                    requireContext(),
                    CART_BULK_ACTION_COACH_MARK
                )
            ) {
                showBulkActionCoachMark()
            }

            if (isFirstCheckEvent) {
                isFirstCheckEvent = false
            }

            reloadAppliedPromoFromGlobalCheck()
        }
        isCheckUncheckDirectAction = true
    }

    private fun handleSelectedAmountVisibilityOnIdle(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && initialSelectedAmountPosition > 0) {
            delayShowSelectedAmountJob?.cancel()
            delayShowSelectedAmountJob =
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    delay(DELAY_SHOW_SELECTED_AMOUNT_AFTER_SCROLL)
                    binding?.apply {
                        val initialPosition = navToolbar.y
                        rlTopLayout.animate().y(initialPosition)
                            .setDuration(SELECTED_AMOUNT_ANIMATION_DURATION).start()
                    }
                    if (hasShowBulkActionCoachMark && viewModel.selectedAmountState.value.second > 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            showBulkActionCoachMark()
                        }, COACHMARK_VISIBLE_DELAY_DURATION)
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
                    val initialPosition = bottomLayout.y - llPromoCheckout.height
                    initialPromoButtonPosition = initialPosition
                    llPromoCheckout.animate().y(initialPosition)
                        .setDuration(PROMO_ANIMATION_DURATION).start()
                }
            }
        }
    }

    private fun handleSelectedAmountVisibilityOnScroll(dy: Int) {
        val rlTopLayout = binding?.rlTopLayout ?: return
        val navToolbar = binding?.navToolbar ?: return
        val valueY = (rlTopLayout.y - abs(dy))
            .coerceAtLeast(navToolbar.y - rlTopLayout.height)
        selectedAmountTranslationLength += dy
        if (dy != 0) {
            if (initialSelectedAmountPosition == 0f && selectedAmountTranslationLength - dy == 0f) {
                initialSelectedAmountPosition = navToolbar.y + navToolbar.height
            }

            if (selectedAmountTranslationLength != 0f && valueY >= navToolbar.y - rlTopLayout.height) {
                animateSelectedAmountToHiddenPosition(valueY)
            }
        }
    }

    private fun handlePromoButtonVisibilityOnScroll(dy: Int) {
        val llPromoCheckout = binding?.llPromoCheckout ?: return
        val bottomY = binding?.bottomLayout?.y ?: return

        promoTranslationLength += dy
        if (dy != 0) {
            // Always get initial position of View, to prevent jumping in to middle of the screen
            initialPromoButtonPosition = bottomY - llPromoCheckout.height
            val valueY = (llPromoCheckout.y + abs(dy))
                .coerceAtMost(llPromoCheckout.height + initialPromoButtonPosition)
                .coerceAtLeast(initialPromoButtonPosition)

            if (promoTranslationLength != 0f) {
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

        if ((
            CartDataHelper.getAllAvailableCartItemData(adapterData).isNotEmpty() &&
                CartDataHelper.hasSelectedCartItem(adapterData) &&
                firstVisibleItemData !is CartSelectedAmountHolderData
            ) || binderHelper.openCount > 1
        ) {
            disableSwipeRefresh()
            setTopLayoutVisibility(true)
        } else {
            setTopLayoutVisibility(false)
            enableSwipeRefresh()
            handleSelectedAmountVisibilityOnIdle(RecyclerView.SCROLL_STATE_IDLE)
        }
    }

    private fun isAtcExternalFlow(): Boolean {
        return getAtcProductId() != 0L
    }

    private fun initializeToasterLocation() {
        activity?.let {
            if (it is CartActivity) {
                val bottomLayoutHeight = binding?.bottomLayout?.height.toZeroIfNull()
                val promoHeight = binding?.llPromoCheckout?.height.toZeroIfNull()
                Toaster.toasterCustomBottomHeight = bottomLayoutHeight + promoHeight
            } else {
                Toaster.toasterCustomBottomHeight =
                    requireContext().resources.getDimensionPixelSize(R.dimen.dp_210)
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
                    backButtonClickListener = {
                        onBackPressed()
                        sendCartImpressionAnalytic()
                    }
                )
                setIcon(
                    IconBuilder(IconBuilderFlag(pageSource = NavSource.CART)).addIcon(
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
                    Timber.d(e)
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
            goToCourierPageButton.setOnClickListener {
                guardCartClick { checkGoToShipment("") }
            }
        }
    }

    private fun initCoachMark() {
        mainFlowCoachMark = CoachMark2(requireContext())
        bulkActionCoachMark = CoachMark2(requireContext())
    }

    @OptIn(FlowPreview::class)
    private fun initSharedFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                swipeToDeleteOnBoardingFlow.debounce(DELAY_SHOW_SWIPE_TO_DELETE_ONBOARDING)
                    .collectLatest {
                        handleProductSwipeToDeleteOnBoarding()
                    }
            }
        }
    }

    private fun handleProductSwipeToDeleteOnBoarding() {
        val cartRecyclerView = binding?.rvCart ?: return
        val layoutManager = cartRecyclerView.layoutManager as? GridLayoutManager ?: return
        val shouldShowDefaultProductOnBoarding = shouldShowSwipeToDeleteDefaultProductOnBoarding()
        val shouldShowBundlingProductOnBoarding = shouldShowSwipeToDeleteBundlingProductOnBoarding()
        if (shouldShowDefaultProductOnBoarding || shouldShowBundlingProductOnBoarding) {
            val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (firstVisibleItemPosition == RecyclerView.NO_POSITION) return

            val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition == RecyclerView.NO_POSITION) return

            val visibleItemPair = CartDataHelper.getDefaultAndMultipleBundlingProductPositionInPair(
                viewModel.cartDataList.value,
                firstVisibleItemPosition,
                lastVisibleItemPosition
            )

            val defaultProductData =
                cartRecyclerView.findViewHolderForAdapterPosition(visibleItemPair.first)

            if (defaultProductData is CartItemViewHolder && shouldShowDefaultProductOnBoarding) {
                val cartItemBinding = defaultProductData.getItemViewBinding()
                startSwipeLayoutOnboardingAnimation(cartItemBinding.llProductContainer)
                cartSwipeToDeleteOnBoardingPreferences.setHasShownSwipeToDeleteDefaultProductOnBoarding()
            }

            val bundlingProductData =
                cartRecyclerView.findViewHolderForAdapterPosition(visibleItemPair.second)

            if (bundlingProductData is CartItemViewHolder && shouldShowBundlingProductOnBoarding) {
                val cartItemBinding = bundlingProductData.getItemViewBinding()
                startSwipeLayoutOnboardingAnimation(cartItemBinding.clProductBundlingInfo)
                cartSwipeToDeleteOnBoardingPreferences.setHasShownSwipeToDeleteBundlingProductOnBoarding()
            }
        }
    }

    private fun startSwipeLayoutOnboardingAnimation(targetView: View) {
        val animation = targetView.animate()
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.duration = SWIPE_TO_DELETE_ANIMATION_DELAY
        animation.translationX(SWIPE_TO_DELETE_TRANSLATION_LENGTH.dpToPx())
        animation.setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                // no-op
            }

            override fun onAnimationEnd(p0: Animator) {
                Handler(Looper.getMainLooper()).postDelayed({
                    stopSwipeLayoutOnboardingAnimation(targetView)
                }, SWIPE_TO_DELETE_ANIMATION_DELAY)
            }

            override fun onAnimationCancel(p0: Animator) {
                // no-op
            }

            override fun onAnimationRepeat(p0: Animator) {
                // no-op
            }
        })
        animation.start()
    }

    private fun stopSwipeLayoutOnboardingAnimation(targetView: View) {
        val animation = targetView.animate()
        animation.interpolator = OvershootInterpolator()
        animation.duration = SWIPE_TO_DELETE_STOP_ANIMATION_DELAY
        animation.translationX((Float.ZERO).dpToPx())
        animation.setListener(null)
        animation.start()
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tokoNowProductUpdater.debounce(TOKONOW_UPDATER_DEBOUNCE).collectLatest {
                    viewModel.processUpdateCartData(
                        fireAndForget = true,
                        onlyTokoNowProducts = true
                    )
                }
            }
        }

        observeAddCartToWishlistEvent()

        observeAddToCart()

        observeAddToCartExternal()

        observeCartDataList()

        observeCartEvent()

        observeCartTrackerEvent()

        observeCheckoutButton()

        observeDeleteCartEvent()

        observeFollowShopEvent()

        observeGlobalEvent()

        observeProgressLoading()

        observeRecentView()

        observeRecommendation()

        observeRemoveFromWishlist()

        observeSeamlessLogin()

        observeSelectedAmount()

        observeSubTotal()

        observeUpdateCartEvent()

        observeUndoDeleteEvent()

        observeUpdateCartAndGetLastApply()

        observeWishlist()

        observeEntryPointInfo()

        observeBmGmGroupProductTicker()
    }

    private fun initToolbar() {
        initNavigationToolbar()
        binding?.navToolbar?.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    @OptIn(FlowPreview::class)
    private fun initTopLayout() {
        binding?.checkboxGlobal?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && binderHelper.openCount > 0) {
                binderHelper.closeAll()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
        binding?.checkboxGlobal?.checks()?.debounce(DELAY_CHECK_BOX_GLOBAL)?.onEach { pair ->
            handleCheckboxGlobalChangeEvent()
            if (pair.first) {
                val listCartStringOrderWithBmGmOfferId =
                    CartDataHelper.getAllCartStringOrderWithBmGmOfferId(viewModel.cartDataList.value)
                if (listCartStringOrderWithBmGmOfferId.isNotEmpty()) {
                    listCartStringOrderWithBmGmOfferId.forEach { cartStringOrderWithBmGmOfferId ->
                        val cartStringOrderWithBmGmOfferIdSplit =
                            cartStringOrderWithBmGmOfferId.split("||")
                        if (cartStringOrderWithBmGmOfferIdSplit.isNotEmpty() && cartStringOrderWithBmGmOfferIdSplit.size > 1) {
                            val (index, cartItems) = CartDataHelper.getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
                                viewModel.cartDataList.value,
                                cartStringOrderWithBmGmOfferIdSplit[0],
                                cartStringOrderWithBmGmOfferIdSplit[1].toLongOrZero()
                            )
                            if (index > RecyclerView.NO_POSITION) {
                                for (cartItem in cartItems) {
                                    cartItem.cartBmGmTickerData.stateTickerBmGm =
                                        CART_BMGM_STATE_TICKER_LOADING
                                }

                                cartAdapter?.notifyItemChanged(index)
                                getGroupProductTicker(
                                    cartItems
                                )
                            }
                        }
                    }
                }
            }
        }?.launchIn(lifecycleScope)

        binding?.topLayout?.textActionDelete?.setOnClickListener {
            guardCartClick { onGlobalDeleteClicked() }
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

    private fun loadRecommendation() {
        viewModel.processGetRecommendationData()
    }

    private fun navigateToPromoRecommendation() {
        routeToPromoCheckoutMarketplacePage()
    }

    private fun navigateToShipmentPage() {
        FLAG_BEGIN_SHIPMENT_PROCESS = true
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = true
        sendCartImpressionAnalytic()
        routeToCheckoutPage()
    }

    private fun notifyBottomCartParent() {
        if (activity is CartNotifyListener) {
            (activity as CartNotifyListener).onNotifyCart()
        }
    }

    private fun observeAddCartToWishlistEvent() {
        viewModel.addCartToWishlistV2Event.observe(viewLifecycleOwner) { addCartToWishlistV2Event ->
            when (addCartToWishlistV2Event) {
                is AddCartToWishlistV2Event.Success -> {
                    if (wishlistIcon != null && animatedWishlistImage != null) {
                        onAddCartToWishlistSuccess(
                            addCartToWishlistV2Event.productId,
                            addCartToWishlistV2Event.isLastItem,
                            addCartToWishlistV2Event.source,
                            wishlistIcon!!,
                            animatedWishlistImage!!
                        )
                    } else {
                        showToastMessageRed()
                    }
                }

                is AddCartToWishlistV2Event.Failed -> {
                    showToastMessageRed(addCartToWishlistV2Event.throwable)
                }
            }
        }
    }

    private fun observeAddToCart() {
        viewModel.addToCartEvent.observe(viewLifecycleOwner) { addToCartEvent ->
            when (addToCartEvent) {
                is AddToCartEvent.Success -> {
                    hideProgressLoading()
                    triggerSendEnhancedEcommerceAddToCartSuccess(
                        addToCartEvent.addToCartDataModel,
                        addToCartEvent.productModel
                    )
                    resetRecentViewList()
                    viewModel.processInitialGetCartData(
                        cartId = "0",
                        initialLoad = false,
                        isLoadingTypeRefresh = false
                    )
                    if (addToCartEvent.addToCartDataModel.data.message.size > 0) {
                        showToastMessageGreen(addToCartEvent.addToCartDataModel.data.message[0])
                        notifyBottomCartParent()
                    }
                }

                is AddToCartEvent.Failed -> {
                    hideProgressLoading()
                    showToastMessageRed(addToCartEvent.throwable)
                }
            }
        }
    }

    private fun observeAddToCartExternal() {
        viewModel.addToCartExternalEvent.observe(viewLifecycleOwner) { addToCartExternalEvent ->
            when (addToCartExternalEvent) {
                is AddToCartExternalEvent.Success -> {
                    hideProgressLoading()
                    if (addToCartExternalEvent.model.message.isNotEmpty()) {
                        showToastMessageGreen(addToCartExternalEvent.model.message[0])
                    }
                    refreshCartWithSwipeToRefresh()
                }

                is AddToCartExternalEvent.Failed -> {
                    hideProgressLoading()
                    showToastMessageRed(addToCartExternalEvent.throwable)
                    refreshCartWithSwipeToRefresh()
                }
            }
        }
    }

    private fun observeCartDataList() {
        viewModel.cartDataList.observe(viewLifecycleOwner) { data ->
            cartAdapter?.updateList(data)
        }
    }

    private fun observeCartEvent() {
        viewModel.loadCartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Success -> {
                    renderLoadGetCartDataFinish()
                    renderInitialGetCartListDataSuccess(state.data)
                    stopCartPerformanceTrace()
                }

                is CartState.Failed -> {
                    renderLoadGetCartDataFinish()
                    renderErrorInitialGetCartListData(state.throwable)
                    stopCartPerformanceTrace()
                }
            }
        }
    }

    private fun observeCartTrackerEvent() {
        viewModel.cartTrackerEvent.observe(viewLifecycleOwner) { cartTrackerEvent ->
            when (cartTrackerEvent) {
                is CartTrackerEvent.ATCTrackingURLRecent -> {
                    sendATCTrackingURLRecent(cartTrackerEvent.productModel)
                }

                is CartTrackerEvent.ATCTrackingURLRecommendation -> {
                    sendATCTrackingURL(cartTrackerEvent.recommendationItem)
                }

                is CartTrackerEvent.ATCTrackingURLBanner -> {
                    sendATCTrackingURL(cartTrackerEvent.bannerShop)
                }
            }
        }
    }

    private fun observeCheckoutButton() {
        viewModel.cartCheckoutButtonState.observe(viewLifecycleOwner) { data ->
            when (data) {
                CartCheckoutButtonState.ENABLE -> onCartDataEnableToCheckout()
                CartCheckoutButtonState.DISABLE -> onCartDataDisableToCheckout(true)
            }
        }
    }

    private fun observeDeleteCartEvent() {
        viewModel.deleteCartEvent.observe(viewLifecycleOwner) { deleteCartEvent ->
            when (deleteCartEvent) {
                is DeleteCartEvent.Success -> {
                    deleteCartEvent.apply {
                        renderLoadGetCartDataFinish()
                        onDeleteCartDataSuccess(
                            toBeDeletedCartIds,
                            removeAllItems,
                            cartDeleteItemData
                        )

                        val params = generateParamGetLastApplyPromo()
                        if (!removeAllItems && (isNeedHitUpdateCartAndValidateUse(params))) {
                            viewModel.doUpdateCartAndGetLastApply(params)
                        } else if (!removeAllItems) {
                            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
                        }
                        viewModel.processUpdateCartCounter()
                        viewModel.updateSelectedAmount()
                    }
                }

                is DeleteCartEvent.Failed -> {
                    deleteCartEvent.apply {
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

    private fun observeFollowShopEvent() {
        viewModel.followShopEvent.observe(viewLifecycleOwner) { followShopEvent ->
            when (followShopEvent) {
                is FollowShopEvent.Success -> {
                    hideProgressLoading()
                    showToastMessageGreen(followShopEvent.dataFollowShop.followShop?.message ?: "")
                    viewModel.processInitialGetCartData(
                        cartId = "0",
                        initialLoad = false,
                        isLoadingTypeRefresh = false
                    )
                }

                is FollowShopEvent.Failed -> {
                    Timber.e(followShopEvent.throwable)
                    hideProgressLoading()
                    showToastMessageRed(followShopEvent.throwable)
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
                    if (data.recommendationWidgets.isNotEmpty() && data.recommendationWidgets[0].recommendationItemList.isNotEmpty()) {
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

    private fun observeRemoveFromWishlist() {
        viewModel.removeFromWishlistEvent.observe(viewLifecycleOwner) { removeFromWishlistEvent ->
            when (removeFromWishlistEvent) {
                is RemoveFromWishlistEvent.Success -> {
                    this@CartRevampFragment.onSuccessRemoveWishlistV2(
                        removeFromWishlistEvent.data,
                        removeFromWishlistEvent.productId
                    )
                    cartPageAnalytics.eventRemoveWishlistWishlistsSection(
                        FLAG_IS_CART_EMPTY,
                        removeFromWishlistEvent.productId
                    )
                }

                is RemoveFromWishlistEvent.Failed -> {
                    this@CartRevampFragment.onErrorRemoveWishlist(
                        ErrorHandler.getErrorMessage(
                            context,
                            removeFromWishlistEvent.throwable
                        ),
                        removeFromWishlistEvent.productId
                    )
                }

                is RemoveFromWishlistEvent.RemoveWishlistFromCartSuccess -> {
                    this.wishlistIcon?.let {
                        onRemoveFromWishlistSuccess(
                            it,
                            removeFromWishlistEvent.position
                        )
                    }
                }

                is RemoveFromWishlistEvent.RemoveWishlistFromCartFailed -> {
                    showToastMessageRed(removeFromWishlistEvent.throwable)
                }
            }
        }
    }

    private fun observeSeamlessLogin() {
        viewModel.seamlessLoginEvent.observe(viewLifecycleOwner) { seamlessLoginEvent ->
            when (seamlessLoginEvent) {
                is SeamlessLoginEvent.Success -> {
                    hideProgressLoading()
                    goToLite(seamlessLoginEvent.url)
                }

                is SeamlessLoginEvent.Failed -> {
                    hideProgressLoading()
                    if (seamlessLoginEvent.msg.isNotBlank()) {
                        showToastMessageRed(seamlessLoginEvent.msg)
                    } else {
                        showToastMessageRed()
                    }
                }
            }
        }
    }

    private fun observeSelectedAmount() {
        viewModel.selectedAmountState.observe(viewLifecycleOwner) { selectedAmountPair ->
            val selectedAmountIdx = selectedAmountPair.first

            if (selectedAmountIdx != -1) {
                val selectedAmount = selectedAmountPair.second
                if (selectedAmount != 0) {
                    binding?.topLayout?.textSelectedAmount?.text = String.format(
                        getString(R.string.cart_label_selected_amount),
                        selectedAmount
                    )
                }
                onNeedToUpdateViewItem(selectedAmountIdx)

                viewModel.cartDataList.value.getOrNull(selectedAmountIdx + 1)?.let { data ->
                    if (data is CartGroupHolderData) {
                        data.isPreviousHasSelectedAmountWidget = selectedAmount > 0
                        onNeedToUpdateViewItem(selectedAmountIdx + 1)
                    }
                }
            }
        }
    }

    private fun observeSubTotal() {
        viewModel.subTotalState.observe(viewLifecycleOwner) { data ->
            updateCashback(data.subtotalCashback)
            renderDetailInfoSubTotal(data.qty, data.subtotalPrice, data.noAvailableItems)
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

    private fun observeEntryPointInfo() {
        enablePromoEntryPointNewInterface = PromoEntryPointImprovementRollenceManager(
            RemoteConfigInstance.getInstance().abTestPlatform
        ).enableNewInterface()
        initPromoButton(enablePromoEntryPointNewInterface)
        viewModel.entryPointInfoEvent.observe(viewLifecycleOwner) { data ->
            when (data) {
                is EntryPointInfoEvent.Loading -> {
                    binding?.promoCheckoutBtnCart?.showLoading()
                }

                is EntryPointInfoEvent.InactiveNew -> {
                    if (data.isNoItemSelected && enablePromoEntryPointNewInterface) {
                        val message = getString(R.string.promo_desc_no_selected_item)
                        val iconUrl = PromoEntryPointInfo.ICON_URL_ENTRY_POINT_NO_ITEM_SELECTED_NEW
                        binding?.promoCheckoutBtnCart?.showActiveNew(
                            leftImageUrl = iconUrl,
                            wording = message,
                            onClickListener = {
                                guardCartClick {
                                    showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                                }
                            }
                        )
                        promoEntryPointAnalytics
                            .sendImpressionPromoEntryPointEvent(
                                userId = userSession.userId,
                                entryPoint = PromoPageEntryPoint.CART_PAGE,
                                entryPointMessages = listOf(message),
                                entryPointInfo = data.entryPointInfo,
                                lastApply = data.lastApply,
                                recommendedPromoCodes = data.recommendedPromoCodes
                            )
                    } else {
                        val message = if (data.isNoItemSelected) {
                            getString(R.string.promo_desc_no_selected_item)
                        } else {
                            data.entryPointInfo.messages.firstOrNull()
                                .ifNull { getString(purchase_platformcommonR.string.promo_funnel_label) }
                        }
                        if (message.isNotBlank()) {
                            val iconUrl = when {
                                data.entryPointInfo.iconUrl.isNotBlank() -> {
                                    data.entryPointInfo.iconUrl
                                }

                                data.isNoItemSelected -> {
                                    PromoEntryPointInfo.ICON_URL_ENTRY_POINT_NO_ITEM_SELECTED
                                }

                                else -> {
                                    ""
                                }
                            }
                            binding?.promoCheckoutBtnCart?.showInactiveNew(
                                leftImageUrl = iconUrl,
                                wording = message,
                                onClickListener = {
                                    guardCartClick {
                                        if (data.isNoItemSelected) {
                                            showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                                        } else if (data.entryPointInfo.isClickable) {
                                            checkGoToPromo()
                                            promoEntryPointAnalytics.sendClickPromoEntryPointEvent(
                                                userId = userSession.userId,
                                                entryPoint = PromoPageEntryPoint.CART_PAGE,
                                                entryPointMessages = listOf(message),
                                                entryPointInfo = data.entryPointInfo,
                                                lastApply = data.lastApply,
                                                recommendedPromoCodes = data.recommendedPromoCodes
                                            )
                                        }
                                    }
                                }
                            )
                            promoEntryPointAnalytics
                                .sendImpressionPromoEntryPointEvent(
                                    userId = userSession.userId,
                                    entryPoint = PromoPageEntryPoint.CART_PAGE,
                                    entryPointMessages = listOf(message),
                                    entryPointInfo = data.entryPointInfo,
                                    lastApply = data.lastApply,
                                    recommendedPromoCodes = data.recommendedPromoCodes
                                )
                        }
                    }
                }

                is EntryPointInfoEvent.Inactive -> {
                    if (data.isNoItemSelected && enablePromoEntryPointNewInterface) {
                        val message = getString(R.string.promo_desc_no_selected_item)
                        binding?.promoCheckoutBtnCart?.showActive(
                            wording = message,
                            onClickListener = {
                                guardCartClick {
                                    showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                                    PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
                                }
                            }
                        )
                    } else {
                        val message = if (data.isNoItemSelected) {
                            getString(R.string.promo_desc_no_selected_item)
                        } else {
                            data.message
                        }
                        if (message.isNotBlank()) {
                            binding?.promoCheckoutBtnCart?.showInactive(
                                message,
                                onClickListener = {
                                    guardCartClick {
                                        if (data.isNoItemSelected) {
                                            showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                                            PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                is EntryPointInfoEvent.ActiveNew -> {
                    val messages = data.entryPointInfo.messages.filter { it.isNotBlank() }
                    if (messages.size > 1) {
                        binding?.promoCheckoutBtnCart?.showActiveFlipping(
                            leftImageUrl = data.entryPointInfo.iconUrl,
                            wordings = messages,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            flippingDurationInMs = 5_000,
                            maximumFlippingCount = 5,
                            onClickListener = {
                                guardCartClick {
                                    if (data.entryPointInfo.isClickable) {
                                        checkGoToPromo()
                                        promoEntryPointAnalytics.sendClickPromoEntryPointEvent(
                                            userId = userSession.userId,
                                            entryPoint = PromoPageEntryPoint.CART_PAGE,
                                            entryPointMessages = messages,
                                            entryPointInfo = data.entryPointInfo,
                                            lastApply = data.lastApply,
                                            recommendedPromoCodes = data.recommendedPromoCodes
                                        )
                                    }
                                }
                            }
                        )
                        promoEntryPointAnalytics
                            .sendImpressionPromoEntryPointEvent(
                                userId = userSession.userId,
                                entryPoint = PromoPageEntryPoint.CART_PAGE,
                                entryPointMessages = messages,
                                entryPointInfo = data.entryPointInfo,
                                lastApply = data.lastApply,
                                recommendedPromoCodes = data.recommendedPromoCodes
                            )
                    } else if (messages.size == 1) {
                        binding?.promoCheckoutBtnCart?.showActiveNew(
                            leftImageUrl = data.entryPointInfo.iconUrl,
                            wording = messages.first(),
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            onClickListener = {
                                guardCartClick {
                                    if (data.entryPointInfo.isClickable) {
                                        checkGoToPromo()
                                        promoEntryPointAnalytics.sendClickPromoEntryPointEvent(
                                            userId = userSession.userId,
                                            entryPoint = PromoPageEntryPoint.CART_PAGE,
                                            entryPointMessages = messages,
                                            entryPointInfo = data.entryPointInfo,
                                            lastApply = data.lastApply,
                                            recommendedPromoCodes = data.recommendedPromoCodes
                                        )
                                    }
                                }
                            }
                        )
                        promoEntryPointAnalytics
                            .sendImpressionPromoEntryPointEvent(
                                userId = userSession.userId,
                                entryPoint = PromoPageEntryPoint.CART_PAGE,
                                entryPointMessages = messages,
                                entryPointInfo = data.entryPointInfo,
                                lastApply = data.lastApply,
                                recommendedPromoCodes = data.recommendedPromoCodes
                            )
                    } else {
                        binding?.promoCheckoutBtnCart?.gone()
                    }
                }

                is EntryPointInfoEvent.ActiveDefault -> {
                    binding?.promoCheckoutBtnCart?.showActive(
                        wording = getString(purchase_platformcommonR.string.promo_funnel_label),
                        rightIcon = IconUnify.CHEVRON_RIGHT,
                        onClickListener = {
                            guardCartClick {
                                checkGoToPromo()
                                PromoRevampAnalytics.eventCartClickPromoSection(
                                    listPromoCodes = data.appliedPromos,
                                    isApplied = false,
                                    userId = userSession.userId
                                )
                            }
                        }
                    )
                }

                is EntryPointInfoEvent.Active -> {
                    if (data.message.isNotBlank()) {
                        binding?.promoCheckoutBtnCart?.showActive(
                            wording = data.message,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            onClickListener = {
                                guardCartClick {
                                    checkGoToPromo()
                                    PromoRevampAnalytics.eventCartClickPromoSection(
                                        listPromoCodes = viewModel.getAllPromosApplied(data.lastApply),
                                        isApplied = false,
                                        userId = userSession.userId
                                    )
                                }
                            }
                        )
                    }
                }

                is EntryPointInfoEvent.AppliedNew -> {
                    if (data.message.isNotBlank()) {
                        binding?.promoCheckoutBtnCart?.showActiveNew(
                            leftImageUrl = data.leftIconUrl,
                            wording = data.message,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            onClickListener = {
                                guardCartClick {
                                    checkGoToPromo()
                                    promoEntryPointAnalytics.sendClickPromoEntryPointEvent(
                                        userId = userSession.userId,
                                        entryPoint = PromoPageEntryPoint.CART_PAGE,
                                        entryPointMessages = listOf(data.message),
                                        entryPointInfo = null,
                                        lastApply = data.lastApply,
                                        recommendedPromoCodes = data.recommendedPromoCodes
                                    )
                                }
                            }
                        )
                        promoEntryPointAnalytics
                            .sendImpressionPromoEntryPointEvent(
                                userId = userSession.userId,
                                entryPoint = PromoPageEntryPoint.CART_PAGE,
                                entryPointMessages = listOf(data.message),
                                entryPointInfo = null,
                                lastApply = data.lastApply,
                                recommendedPromoCodes = data.recommendedPromoCodes
                            )
                    }
                }

                is EntryPointInfoEvent.Applied -> {
                    if (data.message.isNotBlank()) {
                        binding?.promoCheckoutBtnCart?.showApplied(
                            title = data.message,
                            desc = data.detail,
                            rightIcon = IconUnify.CHEVRON_RIGHT,
                            summaries = emptyList(),
                            onClickListener = {
                                guardCartClick {
                                    checkGoToPromo()
                                    PromoRevampAnalytics.eventCartClickPromoSection(
                                        listPromoCodes = viewModel.getAllPromosApplied(data.lastApply),
                                        isApplied = true,
                                        userId = userSession.userId
                                    )
                                }
                            }
                        )
                        PromoRevampAnalytics.eventCartViewPromoAlreadyApplied()
                    }
                }

                is EntryPointInfoEvent.Error -> {
                    binding?.promoCheckoutBtnCart?.showError {
                        renderPromoCheckout(data.lastApply)
                    }
                }
            }
        }
    }

    private fun initPromoButton(enableNewInterface: Boolean) {
        binding?.promoCheckoutBtnCart?.enableNewInterface = enableNewInterface
        binding?.promoCheckoutBtnCart?.init()
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

                is CartGlobalEvent.AdapterItemChanged -> {
                    onNeedToUpdateViewItem(event.position)
                }

                is CartGlobalEvent.CheckGroupShopCartTicker -> {
                    checkCartShopGroupTicker(event.cartGroupHolderData)
                }

                is CartGlobalEvent.UpdateCartShopGroupTicker -> {
                    updateCartShopGroupTicker(event.cartGroupHolderData)
                }

                is CartGlobalEvent.OnNeedUpdateWishlistAdapterData -> {
                    event.wishlistHolderData.wishList.removeAt(event.wishlistIndex)
                    cartAdapter?.cartWishlistAdapter?.updateWishlistItems(event.wishlistHolderData.wishList)
                }
            }
        }
    }

    private fun observeProgressLoading() {
        viewModel.cartProgressLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showProgressLoading()
            } else {
                hideProgressLoading()
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
                    renderPromoCheckoutButtonActiveDefault(emptyList(), true)
                }
            }
        }
    }

    private fun observeBmGmGroupProductTicker() {
        viewModel.bmGmGroupProductTickerState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is GetBmGmGroupProductTickerState.Success -> {
                    val (index, cartItems) = CartDataHelper.getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
                        viewModel.cartDataList.value,
                        data.pairOfferIdBmGmTickerResponse.first
                            .cartStringOrder,
                        data.pairOfferIdBmGmTickerResponse.first.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId
                    )
                    if (index > RecyclerView.NO_POSITION) {
                        for (cartItem in cartItems) {
                            if (data.pairOfferIdBmGmTickerResponse.second.getGroupProductTicker.data.action == BMGM_TICKER_RELOAD_ACTION) {
                                cartItem.cartBmGmTickerData.stateTickerBmGm =
                                    CART_BMGM_STATE_TICKER_INACTIVE
                            } else if (data.pairOfferIdBmGmTickerResponse.second.getGroupProductTicker.data.action.isEmpty()) {
                                cartItem.cartBmGmTickerData.stateTickerBmGm =
                                    CART_BMGM_STATE_TICKER_ACTIVE
                                val listOfferMessage = arrayListOf<String>()
                                data.pairOfferIdBmGmTickerResponse.second.getGroupProductTicker.data.listMessage.forEachIndexed { i, s ->
                                    listOfferMessage.add(s.text)
                                }
                                cartItem.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerMessage =
                                    listOfferMessage
                                cartItem.cartBmGmTickerData.bmGmCartInfoData.bmGmData.totalDiscount =
                                    data.pairOfferIdBmGmTickerResponse.second.getGroupProductTicker.data.discountAmount
                            }
                        }

                        viewModel.reCalculateSubTotal()
                        cartAdapter?.notifyItemChanged(index)
                    }
                }

                is GetBmGmGroupProductTickerState.Failed -> {
                    val (index, cartItems) = CartDataHelper.getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
                        viewModel.cartDataList.value,
                        data.pairOfferIdThrowable.first
                            .cartStringOrder,
                        data.pairOfferIdThrowable.first.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId
                    )

                    if (index > RecyclerView.NO_POSITION) {
                        for (cartItem in cartItems) {
                            cartItem.cartBmGmTickerData.stateTickerBmGm =
                                CART_BMGM_STATE_TICKER_INACTIVE
                        }
                        cartAdapter?.notifyItemChanged(index)
                    }
                }

                else -> {}
            }
        }
    }

    private fun onAddCartToWishlistSuccess(
        productId: String,
        isLastItem: Boolean,
        source: String,
        wishlistIcon: IconUnify,
        animatedWishlistImage: ImageView
    ) {
        animateWishlisted(wishlistIcon, animatedWishlistImage)

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
        }
    }

    private fun animateWishlisted(
        wishlistIcon: IconUnify,
        animatedWishlistImage: ImageView
    ) {
        context?.let { ctx ->
            wishlistIcon.invisible()
            animatedWishlistImage.show()
            val animation: AnimatedVectorDrawableCompat? =
                AnimatedVectorDrawableCompat.create(ctx, R.drawable.wishlist)
            if (animation != null) {
                animatedWishlistImage.background = animation
                animation.start()

                Handler(Looper.getMainLooper()).postDelayed({
                    context?.let { ctx2 ->
                        val inWishlistColor = ContextCompat.getColor(
                            ctx2,
                            unifyprinciplesR.color.Unify_RN500
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
                    }
                }, 1000L)
            }
            viewModel.processGetWishlistV2Data()
        }
    }

    private fun onRemoveFromWishlistSuccess(wishlistIcon: IconUnify, position: Int) {
        context?.let { ctx ->
            val notInWishlistColor = ContextCompat.getColor(
                ctx,
                unifyprinciplesR.color.Unify_NN500
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
    }

    private fun onDeleteCartDataSuccess(
        deletedCartIds: List<String>,
        removeAllItems: Boolean,
        cartDeleteItemData: CartDeleteItemData
    ) {
        var message =
            String.format(getString(R.string.message_product_already_deleted), deletedCartIds.size)

        if (cartDeleteItemData.addWishList) {
            message = String.format(
                getString(R.string.message_product_already_moved_to_wishlist),
                deletedCartIds.size
            )
            refreshWishlistAfterItemRemoveAndMoveToWishlist()
        } else if (cartDeleteItemData.isFromEditBundle) {
            message = getString(R.string.message_toaster_cart_change_bundle_success)
        }

        if (cartDeleteItemData.isFromGlobalCheckbox || cartDeleteItemData.isFromEditBundle) {
            showToastMessageGreen(message)
        } else {
            showToastMessageGreen(
                message,
                getString(R.string.toaster_cta_cancel)
            ) { onUndoDeleteClicked(deletedCartIds, cartDeleteItemData.deleteSource) }
        }

        val needRefresh = removeAllItems || cartDeleteItemData.isFromEditBundle
        val updateListResult =
            viewModel.removeProductByCartId(deletedCartIds, needRefresh, cartDeleteItemData.isFromGlobalCheckbox)
        removeLocalCartItem(updateListResult, cartDeleteItemData.forceExpandCollapsedUnavailableItems)

        hideProgressLoading()

        setTopLayoutVisibility()

        val availableCartItems = CartDataHelper.getAllAvailableCartItemData(
            viewModel.cartDataList.value
        )

        when {
            removeAllItems || availableCartItems.isEmpty() -> {
                refreshCartWithSwipeToRefresh()
            }

            cartDeleteItemData.isFromEditBundle -> {
                refreshCartWithProgressDialog()
            }
        }

        if (cartDeleteItemData.listCartStringOrderAndBmGmOfferId.isNotEmpty()) {
            cartDeleteItemData.listCartStringOrderAndBmGmOfferId.forEach { cartStringOrderAndOfferId ->
                val splitCartStringOrderAndOfferId = cartStringOrderAndOfferId.split("||")
                if (splitCartStringOrderAndOfferId.isNotEmpty() && splitCartStringOrderAndOfferId.size > 1) {
                    getGroupProductTicker(
                        CartDataHelper.getListProductByOfferIdAndCartStringOrder(
                            viewModel.cartDataList.value,
                            splitCartStringOrderAndOfferId[1].toLongOrZero(),
                            splitCartStringOrderAndOfferId[0]
                        )
                    )
                }
            }
        }
    }

    private fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        viewModel.updateWishlistDataByProductId(productId, true)
        viewModel.updateWishlistHolderData(productId, true)
        viewModel.updateRecentViewData(productId, true)
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
        viewModel.updateWishlistHolderData(productId, false)
        viewModel.removeWishlist(productId)
        viewModel.updateRecentViewData(productId, false)
    }

    private fun onUndoDeleteCartDataSuccess() {
        viewModel.processInitialGetCartData(
            cartId = getCartId(),
            initialLoad = false,
            isLoadingTypeRefresh = false
        )
    }

    private fun onNavigationToolbarNavGlobalClicked() {
        cartPageAnalytics.eventClickTopNavMenuNavToolbar(userSession.userId)
    }

    private fun onNavigationToolbarWishlistClicked() {
        guardCartClick {
            cartPageAnalytics.eventClickWishlistIcon(userSession.userId)
            routeToWishlist()
        }
    }

    private fun onNeedToInsertMultipleViewItem(positionStart: Int, itemCount: Int) {
        cartAdapter?.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter?.notifyItemRangeInserted(positionStart, itemCount) }
        } else {
            cartAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    private fun onNeedToRemoveMultipleViewItem(positionStart: Int, itemCount: Int) {
        cartAdapter?.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter?.notifyItemRangeRemoved(positionStart, itemCount) }
        } else {
            cartAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    private fun onNeedToUpdateViewItem(position: Int) {
        cartAdapter?.updateListWithoutDiffUtil(viewModel.cartDataList.value)
        if (position == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter?.notifyItemChanged(position) }
        } else {
            cartAdapter?.notifyItemChanged(position)
        }
    }

    private fun onNeedToUpdateMultipleViewItem(positionStart: Int, count: Int) {
        if (positionStart == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter?.notifyItemRangeChanged(positionStart, count) }
        } else {
            cartAdapter?.notifyItemRangeChanged(positionStart, count)
        }
    }

    private fun onProductClicked(productId: String) {
        routeToProductDetailPage(productId)
    }

    private fun onResultFromAddOnBottomSheet(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val addOnProductDataResult =
                data?.getParcelableExtra(AddOnExtraConstant.EXTRA_ADDON_PAGE_RESULT)
                    ?: AddOnPageResult()

            if (addOnProductDataResult.aggregatedData.isGetDataSuccess) {
                var newAddOnTitle = ""
                var newAddOnPrice = ""
                if (addOnProductDataResult.aggregatedData.title.isNotEmpty()) {
                    newAddOnTitle = addOnProductDataResult.aggregatedData.title
                    newAddOnPrice = "(${
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        addOnProductDataResult.aggregatedData.price,
                        false
                    ).removeDecimalSuffix()
                    })"
                }

                viewModel.updateAddOnByCartId(
                    addOnProductDataResult.cartId.toString(),
                    newAddOnTitle,
                    newAddOnPrice,
                    addOnProductDataResult.aggregatedData.selectedAddons
                )
            } else {
                showToastMessageRed(addOnProductDataResult.aggregatedData.getDataErrorMessage)
            }
        }
    }

    private fun onResultFromEditBundle(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val oldBundleId = data?.getStringExtra(KEY_OLD_BUNDLE_ID) ?: ""
            val newBundleId = data?.getStringExtra(KEY_NEW_BUNDLE_ID) ?: ""
            val isChangeVariant =
                data?.getBooleanExtra(KEY_IS_CHANGE_VARIANT, false) ?: false
            val toBeDeletedBundleGroupId = viewModel.cartModel.toBeDeletedBundleGroupId
            if (((oldBundleId.isNotBlank() && newBundleId.isNotBlank() && oldBundleId != newBundleId) || isChangeVariant) && toBeDeletedBundleGroupId.isNotEmpty()) {
                val cartItems = CartDataHelper.getCartItemByBundleGroupId(
                    viewModel.cartDataList.value,
                    oldBundleId,
                    toBeDeletedBundleGroupId
                )
                viewModel.cartModel.toBeDeletedBundleGroupId = ""
                if (cartItems.isNotEmpty()) {
                    val cartDeleteItemData = CartDeleteItemData(
                        removedCartItems = cartItems,
                        addWishList = false,
                        forceExpandCollapsedUnavailableItems = false,
                        isFromGlobalCheckbox = true,
                        isFromEditBundle = true
                    )
                    viewModel.processDeleteCartItem(cartDeleteItemData)
                }
            } else {
                refreshCartWithSwipeToRefresh()
            }
        }
    }

    private fun onResultFromPdp() {
        refreshCartWithSwipeToRefresh()
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
                refreshCartWithProgressDialog()
            }
        }
    }

    private fun onUndoDeleteClicked(cartIds: List<String>, deleteSource: CartDeleteButtonSource) {
        cartPageAnalytics.eventClickUndoAfterDeleteProduct(
            userSession.userId,
            deleteSource.eventLabel
        )
        viewModel.processUndoDeleteCartItem(cartIds)
    }

    private fun playNoteAnimation(
        newNote: String,
        noteIcon: ImageView,
        noteLottieIcon: LottieAnimationView,
        position: Int
    ) {
        if (newNote.isNotBlank()) {
            noteIcon.invisible()
            noteLottieIcon.show()
            if (!noteLottieIcon.isAnimating) {
                noteLottieIcon.playAnimation()
                noteLottieIcon.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        // no-op
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        noteLottieIcon.gone()
                        noteIcon.visible()
                        onNeedToUpdateViewItem(position)
                    }

                    override fun onAnimationCancel(p0: Animator) {
                        noteLottieIcon.gone()
                        noteIcon.visible()
                        onNeedToUpdateViewItem(position)
                    }

                    override fun onAnimationRepeat(p0: Animator) {
                        // no-op
                    }
                })
            }
        } else {
            onNeedToUpdateViewItem(position)
        }
    }

    private fun refreshCartWithProgressDialog(getCartState: Int = CartViewModel.GET_CART_STATE_DEFAULT) {
        resetRecentViewList()
        if (viewModel.dataHasChanged()) {
            showMainContainer()
            viewModel.processToUpdateAndReloadCartData(getCartId(), getCartState)
        } else {
            viewModel.processInitialGetCartData(
                getCartId(),
                initialLoad = false,
                isLoadingTypeRefresh = false,
                getCartState = getCartState
            )
        }
    }

    private fun refreshCartWithSwipeToRefresh() {
        bulkActionCoachMark?.dismissCoachMark()
        hasShowBulkActionCoachMark = false
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
        viewModel.processInitialGetCartData(
            cartId = getCartId(),
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
    }

    private fun refreshWishlistAfterItemRemoveAndMoveToWishlist() {
        viewModel.processGetWishlistV2Data()
    }

    private fun reloadAppliedPromoFromGlobalCheck() {
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
    }

    private fun removeLocalCartItem(
        updateListResult: ArrayList<Any>,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        val allDisabledCartItemData = CartDataHelper.getAllDisabledCartItemData(
            updateListResult,
            viewModel.cartModel
        )

        if (allDisabledCartItemData.size <= 3) {
            viewModel.removeAccordionDisabledItem(updateListResult)
        }

        viewModel.updateCartGroupFirstItemStatus(updateListResult)
        viewModel.updateCartDataList(updateListResult)

        if (allDisabledCartItemData.size > 3) {
            if (forceExpandCollapsedUnavailableItems) {
                collapseOrExpandDisabledItem()
            }
        }

        viewModel.reCalculateSubTotal()
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

        renderCartAvailableItems(cartData)
        renderCartUnavailableItems(cartData)

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = CartDataHelper.getAllCartItemData(
            viewModel.cartDataList.value,
            viewModel.cartModel
        )
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
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
                showGlobalErrorBottomsheet(parentFragmentManager, context, ::retryGoToShipment)
            }
        }
    }

    private fun renderInitialGetCartListDataSuccess(cartData: CartData) {
        viewModel.processUpdateCartCounter()
        if (cartData.outOfService.isOutOfService()) {
            renderCartOutOfService(cartData.outOfService, true)
            return
        }

        setMainFlowCoachMark(cartData)

        sendAnalyticsScreenNameCartPage()
        updateStateAfterFinishGetCartList()

        renderTickerAnnouncement(cartData)

        activity?.let {
            validateLocalCacheAddress(it, cartData.localizationChooseAddress)
        }

        validateRenderCart(cartData)
        validateShowPopUpMessage(cartData)
        validateRenderPromo(cartData)

        viewModel.updateCartGroupFirstItemStatus(viewModel.cartDataList.value)

        renderSelectedAmount()
        setInitialCheckboxGlobalState(cartData)
        setSelectedAmountVisibility()

        viewModel.reCalculateSubTotal()

        if (!cartData.isGlobalCheckboxState) {
            isFirstCheckEvent = false
        }

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
            viewModel.getEntryPointInfoFromLastApply(lastApplyData)
        } else {
            binding?.promoCheckoutBtnCart?.gone()
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

    private fun renderPromoCheckoutButtonActiveDefault(
        listPromoApplied: List<String>,
        isError: Boolean = false
    ) {
        viewModel.getEntryPointInfoDefault(listPromoApplied, isError)
        if (isError) {
            viewModel.updatePromoSummaryData(
                LastApplyUiModel(
                    additionalInfo = LastApplyAdditionalInfoUiModel(
                        usageSummaries = emptyList()
                    )
                )
            )
        }
    }

    private fun renderPromoCheckoutButtonNoItemIsSelected() {
        viewModel.getEntryPointInfoNoItemSelected()
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
        binding?.navToolbar?.let { navToolbar ->
            initialSelectedAmountPosition = navToolbar.y + navToolbar.height
            viewModel.addItems(0, listOf(CartSelectedAmountHolderData()))
            viewModel.updateSelectedAmount()
        }
    }

    private fun renderTickerAnnouncement(cartData: CartData) {
        val ticker = cartData.tickers.firstOrNull()
        if (ticker != null && ticker.isValid(CART_PAGE)) {
            viewModel.addItem(CartUiModelMapper.mapTickerAnnouncementUiModel(ticker))
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
            if (viewModel.selectedAmountState.value.second <= 0) {
                String.format(getString(R.string.cart_text_buy))
            } else {
                val quantityNumber = qty.toIntOrZero()
                val reachMaximumLimit = quantityNumber > QUANTITY_MAX_LIMIT
                val stringResourceId =
                    if (reachMaximumLimit) R.string.cart_item_button_checkout_count_format_reach_maximum_limit else R.string.cart_item_button_checkout_count_format
                String.format(
                    getString(stringResourceId),
                    quantityNumber.coerceAtMost(QUANTITY_MAX_LIMIT)
                )
            }
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

        val cartWishlistHolderData = CartDataHelper.getCartWishlistHolderData(
            viewModel.cartDataList.value
        )
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
            val intent = RouteManager.getIntent(it, appLink)
            activityResultLauncher.launch(intent)
        }
    }

    private fun routeToCheckoutPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.CHECKOUT)
            intent.putExtra(
                CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE,
                CheckoutConstant.CHECKOUT_PAGE_SOURCE_CART
            )
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
            pdpActivityResult.launch(intent)
        }
    }

    private fun routeToPromoCheckoutMarketplacePage() {
        activity?.let {
            if (viewModel.isPromoRevamp()) {
                val bottomSheetPromo = PromoUsageBottomSheet.newInstance(
                    entryPoint = PromoPageEntryPoint.CART_PAGE,
                    promoRequest = generateParamsCouponList(),
                    boPromoCodes = getBoPromoCodes(),
                    validateUsePromoRequest = generateParamGetLastApplyPromo(),
                    totalAmount = getCurrentTotalPrice(),
                    listener = this@CartRevampFragment
                )
                bottomSheetPromo.show(childFragmentManager)
            } else {
                val intent =
                    RouteManager.getIntent(it, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                val promoRequest = generateParamsCouponList()
                val validateUseRequest = generateParamGetLastApplyPromo()
                intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CART)
                intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
                intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequest)
                promoActivityResult.launch(intent)
            }
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
            val allShopGroupDataList = CartDataHelper.getAllShopGroupDataList(
                viewModel.cartDataList.value
            )
            loop@ for (shop in allShopGroupDataList) {
                hasProducts = true
                break@loop
            }

            if (hasProducts) {
                val shopIndex = CartDataHelper.getCartShopHolderIndexByCartId(
                    viewModel.cartDataList.value,
                    cartId
                )
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
            linearSmoothScroller.targetPosition = CartDataHelper.getDisabledItemHeaderPosition(
                viewModel.cartDataList.value
            )
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

    private fun sendCartImpressionAnalytic() {
        val cartItems = viewModel.cartModel.availableCartItemImpressionList
        val analyticData = CartPageAnalyticsUtil.generateCartImpressionAnalytic(cartItems)
        cartPageAnalytics.sendCartImpressionEvent(analyticData, userSession.userId)
        viewModel.cartModel.availableCartItemImpressionList = mutableSetOf()
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
                        unifyprinciplesR.color.Unify_NN50
                    )
                )
            }

            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_NN50
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

    private fun setSelectedAmountVisibility() {
        binding?.rvCart?.layoutManager?.let { layoutManager ->
            if (layoutManager is GridLayoutManager) {
                val topItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (topItemPosition == RecyclerView.NO_POSITION) return

                val adapterData = viewModel.cartDataList.value
                if (topItemPosition >= adapterData.size) return

                val firstVisibleItemData = adapterData[topItemPosition]

                if (CartDataHelper.getAllAvailableCartItemData(adapterData).isNotEmpty() &&
                    CartDataHelper.hasSelectedCartItem(adapterData) &&
                    firstVisibleItemData !is CartSelectedAmountHolderData
                ) {
                    binding?.rlTopLayout?.visible()
                } else {
                    binding?.rlTopLayout?.invisible()
                }
            }
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
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        hasLoadRecommendation = true
    }

    private fun showMainContainer() {
        binding?.apply {
            layoutGlobalError.gone()
            rlContent.show()
            bottomLayout.show()
            if (enablePromoEntryPointNewInterface) {
                bottomLayoutShadow.gone()
            } else {
                bottomLayoutShadow.show()
            }
            llPromoCheckout.show()
            llPromoCheckout.post {
                if (initialPromoButtonPosition == 0f) {
                    initialPromoButtonPosition = bottomLayout.y - llPromoCheckout.height
                }
            }
            rlTopLayout.post {
                if (initialSelectedAmountPosition == 0f) {
                    initialSelectedAmountPosition = navToolbar.y + navToolbar.height
                }
            }
        }
    }

    private fun showProgressLoading() {
        if (progressDialog == null) {
            progressDialog = LoaderDialog(requireContext())
            progressDialog?.show()
        }
    }

    private fun hideProgressLoading() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    private fun setCheckboxGlobalState() {
        isCheckUncheckDirectAction = false
        val isAllAvailableItemChecked =
            CartDataHelper.isAllAvailableItemChecked(viewModel.cartDataList.value)
        if (binding?.checkboxGlobal?.isChecked == isAllAvailableItemChecked) {
            isCheckUncheckDirectAction = true
        }
        binding?.checkboxGlobal?.isChecked = isAllAvailableItemChecked
    }

    private fun setSpanSize(gridLayoutManager: GridLayoutManager) {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position != RecyclerView.NO_POSITION) {
                    if (position < (
                        cartAdapter?.itemCount
                            ?: 0
                        ) && cartAdapter?.getItemViewType(position) == CartRecommendationViewHolder.LAYOUT
                    ) {
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

    private fun setTopLayoutVisibility() {
        if (CartDataHelper.hasAvailableItemLeft(viewModel.cartDataList.value)) {
            binding?.topLayout?.root?.show()
        } else {
            binding?.topLayout?.root?.gone()
        }
    }

    private fun setTopLayoutVisibility(isShow: Boolean) {
        if (isShow) {
            binding?.rlTopLayout?.show()
        } else {
            binding?.rlTopLayout?.invisible()
        }
    }

    private fun setMainFlowCoachMark(cartData: CartData) {
        if (cartData.onboardingData.size > MAIN_FLOW_ONBOARDING_SELECT_ALL_INDEX) {
            val mainFlowCoachMarkItems = arrayListOf<CoachMark2Item>()
            generateSelectAllCoachMark(mainFlowCoachMarkItems)
            mainFlowCoachMark?.let {
                cartAdapter?.setMainCoachMark(
                    CartMainCoachMarkUiModel(
                        it,
                        mainFlowCoachMarkItems,
                        cartData.onboardingData.getOrNull(MAIN_FLOW_ONBOARDING_NOTES_INDEX)
                            ?: CartOnBoardingData(),
                        cartData.onboardingData.getOrNull(MAIN_FLOW_ONBOARDING_WISHLIST_INDEX)
                            ?: CartOnBoardingData()
                    )
                )
            }
        }
    }

    private fun showBulkActionCoachMark() {
        context?.let {
            bulkActionCoachMark?.dismissCoachMark()
            plusCoachMark?.dismissCoachMark()
            mainFlowCoachMark?.dismissCoachMark()
            if ((
                viewModel.cartModel.cartListData?.onboardingData?.size
                    ?: 0
                ) < BULK_ACTION_ONBOARDING_MIN_QUANTITY_INDEX
            ) {
                return
            }

            bulkActionCoachMark = CoachMark2(it)
            bulkActionCoachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    val selectedAmountCoachMarkIndex = 1
                    if (currentIndex == selectedAmountCoachMarkIndex && isMockMainFlowCoachMarkShown) {
                        val topItemPosition =
                            (binding?.rvCart?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                        if (topItemPosition == RecyclerView.NO_POSITION) return

                        val adapterData = viewModel.cartDataList.value
                        if (topItemPosition >= adapterData.size) return

                        val firstVisibleItemData = adapterData[topItemPosition]

                        val needToScroll = CartDataHelper.hasReachAllShopItems(firstVisibleItemData)

                        if (needToScroll) {
                            binding?.rvCart?.smoothScrollToPosition(0)
                        }

                        isMockMainFlowCoachMarkShown = false
                    }

                    bulkActionCoachMarkLastActiveIndex = currentIndex
                }
            })
            bulkActionCoachMark?.simpleCloseIcon?.setOnClickListener {
                guardCartClick {
                    bulkActionCoachMark?.dismissCoachMark()
                    hasShowBulkActionCoachMark = false
                }
            }
            bulkActionCoachMark?.stepCloseIcon?.setOnClickListener {
                guardCartClick {
                    bulkActionCoachMark?.dismissCoachMark()
                    hasShowBulkActionCoachMark = false
                }
            }
            bulkActionCoachMark?.onFinishListener = {
                hasShowBulkActionCoachMark = false
            }

            val layoutManager: GridLayoutManager =
                binding?.rvCart?.layoutManager as GridLayoutManager
            val position = layoutManager.findFirstCompletelyVisibleItemPosition()

            generateBulkActionCoachMark(position)
        }
    }

    private fun generateBulkActionCoachMark(position: Int) {
        isMockMainFlowCoachMarkShown = false
        val onBoardingData = viewModel.cartModel.cartListData?.onboardingData ?: return
        if (onBoardingData.size < BULK_ACTION_ONBOARDING_MIN_QUANTITY_INDEX + 1) {
            return
        }
        val bulkActionCoachMarkItems: ArrayList<CoachMark2Item> = arrayListOf()
        val selectedAmountData = onBoardingData[BULK_ACTION_ONBOARDING_SELECTED_AMOUNT_DELETE_INDEX]
        val minQuantityOnboardingData = onBoardingData[BULK_ACTION_ONBOARDING_MIN_QUANTITY_INDEX]

        val data = viewModel.cartDataList.value

        binding?.rvCart?.apply {
            post {
                context?.let {
                    if (position > 0) {
                        binding?.topLayout?.textActionDelete?.let {
                            bulkActionCoachMarkItems.add(
                                CoachMark2Item(
                                    it,
                                    "",
                                    selectedAmountData.text,
                                    CoachMark2.POSITION_BOTTOM
                                )
                            )
                        }
                    } else {
                        val selectedAmountViewHolder = findViewHolderForAdapterPosition(0)
                        if (selectedAmountViewHolder is CartSelectedAmountViewHolder) {
                            val textActionDeleteView =
                                selectedAmountViewHolder.getTextActionDeleteView()
                            bulkActionCoachMarkItems.add(
                                CoachMark2Item(
                                    textActionDeleteView,
                                    "",
                                    selectedAmountData.text,
                                    CoachMark2.POSITION_BOTTOM
                                )
                            )
                        }
                    }

                    val nearestItemHolderDataPosition =
                        CartDataHelper.getNearestCartItemHolderDataPosition(position, data)

                    if (nearestItemHolderDataPosition != RecyclerView.NO_POSITION) {
                        val nearestCartItemViewHolder =
                            findViewHolderForAdapterPosition(nearestItemHolderDataPosition)
                        if (nearestCartItemViewHolder is CartItemViewHolder) {
                            val minusButtonAnchorView = if (nearestCartItemViewHolder.isUsingNewQuantityEditor()) {
                                nearestCartItemViewHolder.getNewQuantityEditorAnchorView().anchorMinusButton
                            } else {
                                nearestCartItemViewHolder.getOldQuantityEditorAnchorView().subtractButton
                            }
                            minusButtonAnchorView?.let { anchorView ->
                                bulkActionCoachMarkItems.add(
                                    CoachMark2Item(
                                        anchorView,
                                        "",
                                        minQuantityOnboardingData.text,
                                        CoachMark2.POSITION_BOTTOM
                                    )
                                )
                            }
                        } else if (nearestCartItemViewHolder == null && bulkActionCoachMarkItems.isNotEmpty()) {
                            binding?.root?.let {
                                bulkActionCoachMarkItems.add(
                                    CoachMark2Item(
                                        it,
                                        "",
                                        minQuantityOnboardingData.text,
                                        CoachMark2.POSITION_BOTTOM
                                    )
                                )
                                isMockMainFlowCoachMarkShown = true
                            }
                        }
                    } else if (bulkActionCoachMarkItems.isNotEmpty()) {
                        binding?.root?.let {
                            bulkActionCoachMarkItems.add(
                                CoachMark2Item(
                                    it,
                                    "",
                                    minQuantityOnboardingData.text,
                                    CoachMark2.POSITION_BOTTOM
                                )
                            )
                            isMockMainFlowCoachMarkShown = true
                        }
                    }

                    if (bulkActionCoachMarkItems.isNotEmpty()) {
                        bulkActionCoachMark?.showCoachMark(
                            bulkActionCoachMarkItems,
                            null,
                            bulkActionCoachMarkLastActiveIndex
                        )
                        hasShowBulkActionCoachMark = true
                        CoachMarkPreference.setShown(it, CART_BULK_ACTION_COACH_MARK, true)
                    }
                }
            }
        }
    }

    private fun generateSelectAllCoachMark(mainFlowCoachMarkItems: ArrayList<CoachMark2Item>) {
        viewModel.cartModel.cartListData?.onboardingData?.getOrNull(
            MAIN_FLOW_ONBOARDING_SELECT_ALL_INDEX
        )
            ?.let { onboardingData ->
                binding?.checkboxGlobal?.let {
                    mainFlowCoachMarkItems.add(
                        CoachMark2Item(
                            it,
                            "",
                            onboardingData.text,
                            CoachMark2.POSITION_TOP
                        )
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
                    v.resources.getString(purchase_platformcommonR.string.checkout_flow_toaster_action_ok),
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
                    it.resources.getString(purchase_platformcommonR.string.checkout_flow_toaster_action_ok),
                    tmpCtaClickListener
                ).show()
            }
        }
    }

    private fun showToastMessageRed(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (!(throwable is CartResponseErrorException || throwable is AkamaiErrorException || throwable is ResponseErrorException || throwable is PromoErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        showToastMessageRed(errorMessage)
    }

    private fun showToastMessageRed() {
        showToastMessageRed("")
    }

    private fun stopCartPerformanceTrace() {
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

    private fun updateCartAfterDetached() {
        val hasChanges = viewModel.dataHasChanged()
        try {
            val cartItemDataList =
                CartDataHelper.getAllAvailableCartItemData(viewModel.cartDataList.value)
            activity?.let {
                if (hasChanges && cartItemDataList.isNotEmpty() && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                    viewModel.processUpdateCartData(true)
                }
            }
        } catch (e: IllegalStateException) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                Timber.d(e)
            }
        }
    }

    private fun updateCartCounter(counter: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (counter > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, counter)
        cache.applyEditor()
    }

    private fun updateCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        val (index, _) = CartDataHelper.getCartGroupHolderDataAndIndexByCartString(
            viewModel.cartDataList.value,
            cartGroupHolderData.cartString,
            cartGroupHolderData.isError
        )
        if (index >= 0) {
            onNeedToUpdateViewItem(index)
        }
    }

    private fun updateCashback(cashback: Double) {
        val result = updateShipmentSellerCashback(cashback)
        result?.let {
            when (result.first) {
                CartAdapter.SELLER_CASHBACK_ACTION_INSERT -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        onNeedToInsertMultipleViewItem(result.second, 1)
                    }
                }

                CartAdapter.SELLER_CASHBACK_ACTION_UPDATE -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        onNeedToUpdateViewItem(result.second)
                    }
                }

                CartAdapter.SELLER_CASHBACK_ACTION_DELETE -> {
                    if (result.second != RecyclerView.NO_POSITION) {
                        onNeedToRemoveMultipleViewItem(result.second, 1)
                    }
                }
            }
        }
    }

    private fun updatePromoCheckoutManualIfNoSelected(listPromoApplied: List<String>) {
        val selectedCartGroupHolderData =
            CartDataHelper.getSelectedCartGroupHolderData(viewModel.cartDataList.value)
        if (selectedCartGroupHolderData.isEmpty()) {
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

    private fun updateStateAfterCheckChanged(isChecked: Boolean) {
        viewModel.cartModel.hasPerformChecklistChange = true
        viewModel.reCalculateSubTotal()
        setCheckboxGlobalState()
        setSelectedAmountVisibility()

        context?.let { ctx ->
            if (isChecked && !hasShowBulkActionCoachMark && !CoachMarkPreference.hasShown(
                    ctx,
                    CART_BULK_ACTION_COACH_MARK
                )
            ) {
                showBulkActionCoachMark()
            } else if (!CartDataHelper.hasSelectedCartItem(viewModel.cartDataList.value)) {
                hasShowBulkActionCoachMark = false
                bulkActionCoachMark?.dismissCoachMark()
                bulkActionCoachMarkLastActiveIndex = 0
            }
        }

        validateGoToCheckout()
        val params = generateParamGetLastApplyPromo()
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            viewModel.doUpdateCartAndGetLastApply(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
        viewModel.saveCheckboxState()
    }

    private fun updateStateAfterFinishGetCartList() {
        endlessRecyclerViewScrollListener?.resetState()
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
        val isAvailableGroupEmpty = cartData.availableSection.availableGroupGroups.isEmpty()
        val isUnavailableGroupEmpty = cartData.unavailableSections.isEmpty()
        if ((isAvailableGroupEmpty && isUnavailableGroupEmpty) || (isAvailableGroupEmpty && !isUnavailableGroupEmpty)) {
            renderCartEmpty(cartData)
            setTopLayoutVisibility(false)
        }
        renderCartNotEmpty(cartData)
        setTopLayoutVisibility(cartData.availableSection.availableGroupGroups.isNotEmpty())
    }

    private fun validateRenderPromo(cartData: CartData) {
        // reset promo position
        val bottomLayoutY = binding?.bottomLayout?.y ?: 0f
        val llPromoCheckoutHeight = binding?.llPromoCheckout?.height?.toFloat() ?: 0f
        initialPromoButtonPosition = bottomLayoutY - llPromoCheckoutHeight
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
            viewModel.processGetRecentViewData()
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

    private fun CompoundButton.checks(): Flow<Pair<Boolean, Boolean>> = callbackFlow {
        setOnCheckedChangeListener { buttonView, isChecked ->
            trySend(Pair(buttonView.isPressed, isChecked)).isSuccess
        }
        awaitClose { setOnCheckedChangeListener(null) }
    }

    override fun onClosePageWithApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest
    ) {
        viewModel.cartModel.lastValidateUseRequest = lastValidateUsePromoRequest
        viewModel.validateBoPromo(validateUse)
        viewModel.cartModel.apply {
            lastValidateUseResponse = validateUse
            lastUpdateCartAndGetLastApplyResponse = null
            isLastApplyResponseStillValid = false
        }
        updatePromoCheckoutStickyButton(validateUse.promoUiModel)
    }

    override fun onClosePageWithClearPromo(
        entryPoint: PromoPageEntryPoint,
        clearPromo: ClearPromoUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        isFlowMvcLockToCourier: Boolean,
        clearedPromos: List<PromoItem>
    ) {
        viewModel.cartModel.apply {
            isLastApplyResponseStillValid = false
            lastValidateUseResponse = null
            lastUpdateCartAndGetLastApplyResponse = null
        }
        updatePromoCheckoutStickyButton(
            PromoUiModel(titleDescription = clearPromo.successDataModel.defaultEmptyPromoMessage)
        )
    }

    override fun onClosePageWithNoAction() {
        // no-op
    }

    override fun onApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest
    ) {
        viewModel.cartModel.lastValidateUseRequest = lastValidateUsePromoRequest
        viewModel.validateBoPromo(validateUse)
        viewModel.cartModel.apply {
            lastValidateUseResponse = validateUse
            lastUpdateCartAndGetLastApplyResponse = null
            isLastApplyResponseStillValid = false
        }
        updatePromoCheckoutStickyButton(validateUse.promoUiModel)
        goToCheckoutPage()
    }

    override fun onApplyPromoNoAction() {
        goToCheckoutPage()
    }

    override fun onApplyPromoFailed(throwable: Throwable) {
        showToastMessageRed(throwable)
    }

    override fun onClearPromoSuccess(
        entryPoint: PromoPageEntryPoint,
        clearPromo: ClearPromoUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        isFlowMvcLockToCourier: Boolean
    ) {
        viewModel.cartModel.apply {
            isLastApplyResponseStillValid = false
            lastValidateUseResponse = null
            lastUpdateCartAndGetLastApplyResponse = null
        }
        updatePromoCheckoutStickyButton(
            PromoUiModel(titleDescription = clearPromo.successDataModel.defaultEmptyPromoMessage)
        )
        goToCheckoutPage()
    }

    override fun onClearPromoFailed(throwable: Throwable) {
        showToastMessageRed(throwable)
    }

    private fun getGroupProductTicker(cartItemHolderData: CartItemHolderData) {
        viewModel.getBmGmGroupProductTicker(
            cartItemHolderData,
            BmGmTickerRequestMapper.generateGetGroupProductTickerRequestParams(
                viewModel.cartDataList.value,
                cartItemHolderData
            )
        )
    }

    private fun getGroupProductTicker(listProduct: List<CartItemHolderData>) {
        if (listProduct.isNotEmpty()) {
            val cartItemData = listProduct.first()
            viewModel.getBmGmGroupProductTicker(
                cartItemData,
                BmGmTickerRequestMapper.generateGetGroupProductTickerRequestParams(
                    listProduct,
                    cartItemData.bundleId.toLongOrZero(),
                    cartItemData.bundleGroupId,
                    cartItemData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId,
                    cartItemData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerJsonData,
                    cartItemData.cartStringOrder
                )
            )
        }
    }

    override fun onBmGmChevronRightClicked(
        offerLandingPageLink: String,
        offerId: Long,
        widgetCaption: String,
        shopId: String
    ) {
        cartPageAnalytics.eventClickBmGmTickerOffer(
            offerId,
            widgetCaption,
            shopId,
            userSession.userId
        )
        RouteManager.route(context, offerLandingPageLink)
    }

    override fun onBmGmTickerReloadClicked() {
        refreshCartWithSwipeToRefresh()
    }

    override fun onCartViewBmGmTicker(offerId: Long, widgetCaption: String, shopId: String) {
        cartPageAnalytics.eventViewBmGmTickerOffer(
            offerId,
            widgetCaption,
            shopId,
            userSession.userId
        )
    }

    override fun clearAllFocus() {
        val view = activity?.currentFocus
        view?.clearFocus()
    }

    private inline fun guardCartClick(onClick: () -> Unit) {
        if (binderHelper.openCount > 0) {
            binderHelper.closeAll()
        } else {
            onClick()
        }
    }

    private fun shouldShowSwipeToDeleteDefaultProductOnBoarding(): Boolean {
        return !cartSwipeToDeleteOnBoardingPreferences.getHasShownSwipeToDeleteDefaultProductOnBoarding()
    }

    private fun shouldShowSwipeToDeleteBundlingProductOnBoarding(): Boolean {
        return !cartSwipeToDeleteOnBoardingPreferences.getHasShownSwipeToDeleteBundlingProductOnBoarding()
    }

    override fun onChangeAddressClicked() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(object :
                ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
                override fun onLocalizingAddressServerDown() {
                    // no-op
                }

                override fun onAddressDataChanged() {
                    val clearBoPromo = generateParamClearBo()
                    if (clearBoPromo != null) {
                        viewModel.clearAllBo(clearBoPromo)
                    }
                    refreshCartWithProgressDialog(GET_CART_STATE_AFTER_CHOOSE_ADDRESS)
                }

                override fun getLocalizingAddressHostSourceBottomSheet(): String {
                    return CART_PAGE
                }

                override fun onLocalizingAddressLoginSuccessBottomSheet() {
                    // no-op
                }

                override fun onDismissChooseAddressBottomSheet() {
                    // no-op
                }
            })
        chooseAddressBottomSheet.show(childFragmentManager)
    }

    override fun onSwipeToDeleteClosed(productId: String) {
        cartPageAnalytics.eventClickSwipeOnProductCard(productId)
    }
}
