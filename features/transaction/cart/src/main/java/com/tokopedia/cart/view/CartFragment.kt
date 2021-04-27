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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.FragmentCartBinding
import com.tokopedia.cart.domain.model.cartlist.*
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_CHECKOUTBROWSER
import com.tokopedia.cart.domain.model.cartlist.ButtonData.Companion.ID_HOMEPAGE
import com.tokopedia.cart.domain.model.cartlist.ButtonData.Companion.ID_RETRY
import com.tokopedia.cart.domain.model.cartlist.ButtonData.Companion.ID_START_SHOPPING
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData.Companion.ID_MAINTENANCE
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData.Companion.ID_OVERLOAD
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData.Companion.ID_TIMEOUT
import com.tokopedia.cart.view.CartActivity.Companion.INVALID_PRODUCT_ID
import com.tokopedia.cart.view.adapter.cart.CartAdapter
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.bottomsheet.showGlobalErrorBottomsheet
import com.tokopedia.cart.view.bottomsheet.showSummaryTransactionBottomsheet
import com.tokopedia.cart.view.compoundview.CartToolbar
import com.tokopedia.cart.view.compoundview.CartToolbarListener
import com.tokopedia.cart.view.compoundview.CartToolbarView
import com.tokopedia.cart.view.compoundview.CartToolbarWithBackView
import com.tokopedia.cart.view.di.DaggerCartComponent
import com.tokopedia.cart.view.mapper.RecentViewMapper
import com.tokopedia.cart.view.mapper.ViewHolderDataMapper
import com.tokopedia.cart.view.mapper.WishlistMapper
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_EMPTY_DEFAULT_IMG_URL
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_EMPTY_WITH_PROMO_IMG_URL
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.constant.CartConstant.IS_TESTING_FLOW
import com.tokopedia.purchase_platform.common.constant.CartConstant.PARAM_CART
import com.tokopedia.purchase_platform.common.constant.CartConstant.PARAM_DEFAULT
import com.tokopedia.purchase_platform.common.constant.CartConstant.STATE_RED
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
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
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.*
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.coroutines.*
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartFragment : BaseCheckoutFragment(), ICartListView, ActionListener, CartItemAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, CartToolbarListener,
        TickerAnnouncementActionListener, SellerCashbackListener {

    private var binding by autoClearedNullable<FragmentCartBinding>()

    lateinit var toolbar: CartToolbar

    @Inject
    lateinit var dPresenter: ICartListPresenter

    @Inject
    lateinit var cartItemDecoration: RecyclerView.ItemDecoration

    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart

    @Inject
    lateinit var viewHolderDataMapper: ViewHolderDataMapper

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var wishlistMapper: WishlistMapper

    @Inject
    lateinit var recentViewMapper: RecentViewMapper

    @Inject
    lateinit var compositeSubscription: CompositeSubscription

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

    private var cartListData: CartListData? = null
    private var wishLists: List<CartWishlistItemHolderData>? = null
    private var recentViewList: List<CartRecentViewItemHolderData>? = null
    private var recommendationList: MutableList<CartRecommendationItemHolderData>? = null
    private var recommendationSectionHeader: CartSectionHeaderHolderData? = null
    private var recommendationWishlistActionListener: WishListActionListener? = null
    private var cartUnavailableWishlistActionListener: WishListActionListener? = null
    private var lastSeenWishlistActionListener: WishListActionListener? = null
    private var wishlistsWishlistActionListener: WishListActionListener? = null

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
    private var accordionCollapseState = true
    private var hasCalledOnSaveInstanceState = false
    private var isCheckUncheckDirectAction = true
    private var toolbarType = ""

    companion object {

        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

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
        const val ADVERTISINGID = "ADVERTISINGID"
        const val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
        const val WISHLIST_SOURCE_AVAILABLE_ITEM = "WISHLIST_SOURCE_AVAILABLE_ITEM"
        const val WISHLIST_SOURCE_UNAVAILABLE_ITEM = "WISHLIST_SOURCE_UNAVAILABLE_ITEM"
        const val WORDING_GO_TO_HOMEPAGE = "Kembali ke Homepage"
        const val TOOLBAR_VARIANT_BASIC = AbTestPlatform.NAVIGATION_VARIANT_OLD
        const val TOOLBAR_VARIANT_NAVIGATION = AbTestPlatform.NAVIGATION_VARIANT_REVAMP

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
                cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE)
                cartAllPerformanceMonitoring = PerformanceMonitoring.start(CART_ALL_TRACE)
            }
        }

        initRemoteConfig()

        dPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding?.root
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = view.rootView?.height?.minus(view.height) ?: 0
            val displayMetrics = DisplayMetrics()
            val windowManager = context?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            windowManager?.let {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val heightDiffInDp = heightDiff.pxToDp(displayMetrics)
                if (heightDiffInDp > 100) {
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
                    refreshCart()
                } else {
                    addToCartExternal(productId)
                }
            } else {
                loadCartData(savedInstanceState)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if currently not refreshing, not ATC external flow and not on error state
        if (refreshHandler?.isRefreshing == false && !isAtcExternalFlow() && binding?.layoutGlobalError?.visibility != View.VISIBLE) {
            if (!::cartAdapter.isInitialized || (::cartAdapter.isInitialized && cartAdapter.itemCount == 0)) {
                dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
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
        saveInstanceCacheManager?.put(CartListData::class.java.simpleName, cartListData)
        wishLists.let {
            saveInstanceCacheManager?.put(CartWishlistItemHolderData::class.java.simpleName, wishLists)
        }
        recentViewList?.let {
            saveInstanceCacheManager?.put(CartRecentViewItemHolderData::class.java.simpleName, recentViewList)
        }
        recommendationList?.let {
            saveInstanceCacheManager?.put(CartRecommendationItemHolderData::class.java.simpleName, recommendationList)
        }
        recommendationSectionHeader?.let {
            saveInstanceCacheManager?.put(CartSectionHeaderHolderData::class.java.simpleName, recommendationSectionHeader)
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
        compositeSubscription.unsubscribe()
        super.onDetach()
    }

    override fun onDestroy() {
        cartAdapter.clearCompositeSubscription()
        dPresenter.detachView()
        delayShowPromoButtonJob?.cancel()
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
            NAVIGATION_SHOP_PAGE -> refreshCart()
            NAVIGATION_WISHLIST -> refreshCart()
        }
    }

    private fun onResultFromShipmentPage(resultCode: Int, data: Intent?) {
        FLAG_BEGIN_SHIPMENT_PROCESS = false
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false

        when (resultCode) {
            PaymentConstant.PAYMENT_CANCELLED -> {
                showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module))
                refreshCartWithProgressDialog()
            }
            PaymentConstant.PAYMENT_SUCCESS -> {
                showToastMessageGreen(getString(R.string.message_payment_success))
                refreshCartWithProgressDialog()
            }
            PaymentConstant.PAYMENT_FAILED -> {
                showToastMessageRed(getString(R.string.default_request_error_unknown))
                refreshCartWithProgressDialog()
            }
            CheckoutConstant.RESULT_CODE_COUPON_STATE_CHANGED -> {
                refreshCartWithProgressDialog()
            }
            CheckoutConstant.RESULT_CHECKOUT_CACHE_EXPIRED -> {
                val message = data?.getStringExtra(CheckoutConstant.EXTRA_CACHE_EXPIRED_ERROR_MESSAGE)
                showToastMessageRed(message ?: "")
            }
            else -> {
                refreshCartWithProgressDialog()
            }
        }
    }

    private fun onResultFromPdp() {
        if (!isTestingFlow()) {
            refreshCart()
        }
    }

    private fun onResultFromPromoPage(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val validateUseUiModel = data?.getParcelableExtra<ValidateUsePromoRevampUiModel>(ARGS_VALIDATE_USE_DATA_RESULT)
            if (validateUseUiModel != null) {
                dPresenter.setValidateUseLastResponse(validateUseUiModel)
                dPresenter.setUpdateCartAndValidateUseLastResponse(null)
                dPresenter.setLastApplyNotValid()
                updatePromoCheckoutStickyButton(validateUseUiModel.promoUiModel)
            }

            val clearPromoUiModel = data?.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)
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


    // Initialization Section

    override fun initInjector() {
        activity?.let {
            val baseMainApplication = it.application as BaseMainApplication
            DaggerCartComponent.builder()
                    .baseAppComponent(baseMainApplication.baseAppComponent)
                    .build()
                    .inject(this)
        }
        cartAdapter = CartAdapter(this, this, this, this)
    }

    private fun initRemoteConfig() {
        val EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
        toolbarType = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                EXP_NAME, TOOLBAR_VARIANT_BASIC
        )
    }

    override fun initView(view: View) {
        initToolbar(view)

        activity?.let {
            binding?.swipeRefreshLayout?.let { swipeRefreshLayout ->
                refreshHandler = RefreshHandler(it, swipeRefreshLayout, this)
            }
            progressDialog = AlertDialog.Builder(it)
                    .setView(R.layout.purchase_platform_progress_dialog_view)
                    .setCancelable(false)
                    .create()
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
        val gridLayoutManager = GridLayoutManager(context, 2)
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
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            startActivityForResult(intent, NAVIGATION_PDP)
        }
    }

    private fun routeToShopPage(shopId: String?) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SHOP, shopId)
            startActivityForResult(intent, NAVIGATION_SHOP_PAGE)
        }
    }

    private fun routeToCheckoutPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.CHECKOUT)
            startActivityForResult(intent, NAVIGATION_SHIPMENT)
        }
    }

    private fun routeToPromoCheckoutMarketplacePage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
            val promoRequest = generateParamsCouponList()
            val validateUseRequest = generateParamValidateUsePromoRevamp(false, -1, -1, true)
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


    private fun loadCachedData() {
        cartListData = saveInstanceCacheManager?.get<CartListData>(CartListData::class.java.simpleName, CartListData::class.java)
        wishLists = saveInstanceCacheManager?.get<List<CartWishlistItemHolderData>>(CartWishlistItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartWishlistItemHolderData>>() {}.type, null)
        recentViewList = saveInstanceCacheManager?.get<List<CartRecentViewItemHolderData>>(CartRecentViewItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecentViewItemHolderData>>() {}.type, null)
        recommendationList = saveInstanceCacheManager?.get<MutableList<CartRecommendationItemHolderData>>(CartRecommendationItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecommendationItemHolderData>>() {}.type, null)
        recommendationSectionHeader = saveInstanceCacheManager?.get<CartSectionHeaderHolderData>(CartSectionHeaderHolderData::class.java.simpleName,
                object : TypeToken<CartSectionHeaderHolderData>() {}.type, null)
    }

    override fun onBackPressed() {
        if (toolbarType == TOOLBAR_VARIANT_NAVIGATION) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
                }
            } else {
                binding?.topLayoutShadow?.gone()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (show) {
                    binding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
                    binding?.topLayoutShadow?.gone()
                } else {
                    binding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
                }
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
                    cartListData?.let { cartListData ->
                        showSummaryTransactionBottomsheet(cartListData, fragmentManager, context)
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
        val topItemPosition = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        if (topItemPosition == RecyclerView.NO_POSITION) return

        val adapterData = cartAdapter.getData()
        if (topItemPosition >= adapterData.size) return

        val firstVisibleItemData = adapterData[topItemPosition]
        if (firstVisibleItemData is CartSelectAllHolderData ||
                firstVisibleItemData is TickerAnnouncementHolderData ||
                firstVisibleItemData is CartChooseAddressHolderData ||
                firstVisibleItemData is CartItemTickerErrorHolderData ||
                firstVisibleItemData is CartShopHolderData ||
                firstVisibleItemData is ShipmentSellerCashbackModel) {
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
                    } else 2
                } else 0
            }
        }
    }

    private fun addEndlessRecyclerViewScrollListener(cartRecyclerView: RecyclerView, gridLayoutManager: GridLayoutManager) {
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
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
            delayShowPromoButtonJob = GlobalScope.launch(Dispatchers.Main) {
                delay(750L)
                binding?.llPromoCheckout?.animate()
                        ?.y(initialPromoButtonPosition)
                        ?.setDuration(500L)
                        ?.start()
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
        binding?.llPromoCheckout?.animate()
                ?.y(initialPromoButtonPosition)
                ?.setDuration(0)
                ?.start()
    }

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()
                ?.y(valueY)
                ?.setDuration(0)
                ?.start()
    }

    private fun initToolbar(view: View) {
        if (toolbarType == TOOLBAR_VARIANT_NAVIGATION) {
            initNavigationToolbar(view)
            binding?.toolbar?.gone()
            binding?.navToolbar?.show()
        } else {
            initBasicToolbar(view)
            binding?.navToolbar?.gone()
            binding?.toolbar?.show()
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
                setOnBackButtonClickListener(
                        disableDefaultGtmTracker = true,
                        backButtonClickListener = ::onBackPressed
                )
                setIcon(
                        IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                                .addIcon(
                                        iconId = IconList.ID_NAV_ANIMATED_WISHLIST,
                                        disableDefaultGtmTracker = true,
                                        onClick = ::onNavigationToolbarWishlistClicked
                                )
                                .addIcon(
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

            val appbar = binding?.toolbar
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
                    rxCompoundButtonCheckDebounce(it, 500L).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(isChecked: Boolean) {
                            handleCheckboxGlobalChangeEvent()
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
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
            dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)
            dPresenter.saveCheckboxState(cartAdapter.allCartItemHolderData)
            setGlobalDeleteVisibility()
            cartPageAnalytics.eventCheckUncheckGlobalCheckbox(isChecked)

            reloadAppliedPromoFromGlobalCheck()
        }
        isCheckUncheckDirectAction = true
    }

    private fun reloadAppliedPromoFromGlobalCheck() {
        val isChecked = binding?.topLayout?.checkboxGlobal?.isChecked ?: return
        val params = generateParamValidateUsePromoRevamp(isChecked, -1, -1, false)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
    }

    private fun checkGoToShipment(message: String?) {
        if (message.isNullOrEmpty()) {
            val redStatePromo = ArrayList<String>()
            if (dPresenter.isLastApplyValid()) {
                val lastApplyUiModel = cartListData?.lastApplyShopGroupSimplifiedData
                lastApplyUiModel?.let {
                    if (it.message.state.equals("red")) {
                        it.codes.forEach {
                            if (!redStatePromo.contains(it)) {
                                redStatePromo.add(it)
                            }
                        }
                    }

                    it.voucherOrders.forEach {
                        if (it.message.state.equals("red") && !redStatePromo.contains(it.code)) {
                            redStatePromo.add(it.code)
                        }
                    }
                }
            } else {
                val lastValidateUseData = dPresenter.getValidateUseLastResponse()
                lastValidateUseData?.promoUiModel?.let {
                    if (it.messageUiModel.state.equals("red")) {
                        it.codes.forEach {
                            if (!redStatePromo.contains(it)) {
                                redStatePromo.add(it)
                            }
                        }
                    }

                    it.voucherOrderUiModels.forEach {
                        val promoCode = it?.code ?: ""
                        if (promoCode.isNotBlank() && it?.messageUiModel?.state.equals("red") && !redStatePromo.contains(promoCode)) {
                            redStatePromo.add(promoCode)
                        }
                    }
                }
            }

            val lastUpdateCartAndValidateUseResponse = dPresenter.getUpdateCartAndValidateUseLastResponse()
            lastUpdateCartAndValidateUseResponse?.promoUiModel?.let {
                if (it.messageUiModel.state.equals("red")) {
                    it.codes.forEach {
                        if (!redStatePromo.contains(it)) {
                            redStatePromo.add(it)
                        }
                    }
                }

                it.voucherOrderUiModels.forEach {
                    val promoCode = it?.code ?: ""
                    if (promoCode.isNotBlank() && it?.messageUiModel?.state.equals("red") && !redStatePromo.contains(promoCode)) {
                        redStatePromo.add(promoCode)
                    }
                }
            }

            if (redStatePromo.isNotEmpty()) {
                dPresenter.doClearRedPromosBeforeGoToCheckout(redStatePromo)
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

    private fun loadCartData(savedInstanceState: Bundle?): Unit? {
        return if (savedInstanceState == null) {
            refreshCart()
        } else {
            if (cartListData != null) {
                dPresenter.setCartListData(cartListData!!)
                renderLoadGetCartDataFinish()
                renderInitialGetCartListDataSuccess(cartListData)
                stopCartPerformanceTrace()
            } else {
                refreshCart()
            }
        }
    }

    override fun refreshCart() {
        refreshCartWithSwipeToRefresh()
    }

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData?) {
        cartPageAnalytics.eventClickAtcCartClickTrashBin()
        val cartItemDatas = mutableListOf<CartItemData>()
        cartItemHolderData?.cartItemData?.let {
            cartItemDatas.add(it)
        }
        val allCartItemDataList = cartAdapter.allCartItemData

        if (cartItemDatas.size > 0) {
            dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, false, false)
            cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(
                    dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
            )
        }
    }

    override fun onCartItemQuantityPlusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus()
    }

    override fun onCartItemQuantityMinusButtonClicked() {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus()
    }

    override fun onCartItemQuantityReseted(position: Int, parentPosition: Int) {
        cartAdapter.resetQuantity(position, parentPosition)
    }

    override fun onCartItemProductClicked(cartItemData: CartItemData?) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemData?.originData?.productName
                ?: "")
        cartItemData?.originData?.productId?.let {
            routeToProductDetailPage(it)
        }
    }

    override fun onDisabledCartItemProductClicked(cartItemData: CartItemData) {
        cartPageAnalytics.eventClickAtcCartClickProductName(cartItemData.originData?.productName
                ?: "")
        cartItemData.originData?.productId?.let {
            routeToProductDetailPage(it)
        }
    }

    override fun onGlobalCheckboxCheckedChange(isChecked: Boolean, isCheckUncheckDirectAction: Boolean) {
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
        refreshCartWithProgressDialog()
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

    private fun getRecommendationWishlistActionListener(): WishListActionListener {
        if (recommendationWishlistActionListener == null) {
            recommendationWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.eventClickAddWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics.eventClickAddWishlistOnProductRecommendation()
                    }
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics.eventClickRemoveWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics.eventClickRemoveWishlistOnProductRecommendation()
                    }
                }
            }
        }
        return recommendationWishlistActionListener as WishListActionListener
    }

    private fun getCartUnavailableWishlistActionListener(): WishListActionListener {
        if (cartUnavailableWishlistActionListener == null) {
            cartUnavailableWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics.eventAddWishlistUnavailableSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics.eventRemoveWishlistUnvailableSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return cartUnavailableWishlistActionListener as WishListActionListener
    }

    private fun getLastSeenWishlistActionListener(): WishListActionListener {
        if (lastSeenWishlistActionListener == null) {
            lastSeenWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics.eventAddWishlistLastSeenSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics.eventRemoveWishlistLastSeenSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return lastSeenWishlistActionListener as WishListActionListener
    }

    private fun getWishlistsWishlistActionListener(): WishListActionListener {
        if (wishlistsWishlistActionListener == null) {
            wishlistsWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics.eventAddWishlistWishlistsSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics.eventRemoveWishlistWishlistsSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return wishlistsWishlistActionListener as WishListActionListener
    }

    override fun onAddDisabledItemToWishlist(data: DisabledCartItemHolderData, imageView: ImageView) {
        setProductImageAnimationData(imageView, true)

        cartPageAnalytics.eventClickMoveToWishlistOnUnavailableSection(userSession.userId, data.productId, data.errorType)
        val isLastItem = cartAdapter.allCartItemData.size == 1

        // If unavailable item > 1 and state is collapsed, then expand first
        var forceExpand = false
        if (cartAdapter.allDisabledCartItemData.size > 1 && accordionCollapseState) {
            collapseOrExpandDisabledItem()
            forceExpand = true
        }

        dPresenter.processAddCartToWishlist(data.productId, data.cartId.toString(), isLastItem, WISHLIST_SOURCE_UNAVAILABLE_ITEM, forceExpand)
    }

    override fun onAddLastSeenToWishlist(productId: String) {
        dPresenter.processAddToWishlist(productId, userSession.userId, getLastSeenWishlistActionListener())
    }

    override fun onAddWishlistToWishlist(productId: String) {
        dPresenter.processAddToWishlist(productId, userSession.userId, getWishlistsWishlistActionListener())
    }

    override fun onAddRecommendationToWishlist(productId: String) {
        dPresenter.processAddToWishlist(productId, userSession.userId, getRecommendationWishlistActionListener())
    }

    override fun onRemoveDisabledItemFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getCartUnavailableWishlistActionListener())
    }

    override fun onRemoveLastSeenFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getLastSeenWishlistActionListener())
    }

    override fun onRemoveWishlistFromWishlist(productId: String) {
        cartPageAnalytics.eventClickRemoveWishlist(userSession.userId, productId)
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getWishlistsWishlistActionListener())
    }

    override fun onRemoveRecommendationFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getRecommendationWishlistActionListener())
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
                                dPresenter.generateWishlistProductClickEmptyCartDataLayer(wishlist, position)
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
        (recentViewList as List<CartRecentViewItemHolderData>).withIndex().forEach { (position, recentView) ->
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
                            dPresenter.generateRecentViewProductClickEmptyCartDataLayer(recentView, position)
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
        for ((_, item) in recommendationList as List<CartRecommendationItemHolderData>) {
            if (item.productId.toString().equals(productId, ignoreCase = true)) {
                recommendationItemClick = item
                break
            }
            index++
        }

        recommendationItemClick?.let {
            cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(
                    index.toString(),
                    dPresenter.generateRecommendationDataOnClickAnalytics(it, FLAG_IS_CART_EMPTY, index)
            )
        }

        when {
            topAds -> {
                activity?.let { TopAdsUrlHitter(CartFragment::class.qualifiedName).hitClickUrl(it, clickUrl, productId, productName, imageUrl) }
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
                activity?.let { TopAdsUrlHitter(CartFragment::class.qualifiedName).hitImpressionUrl(it, url, productId, productName, imageUrl) }
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

    private fun sendImpressionOneRecommendationItem(it: List<CartRecommendationItemHolderData>, currentIndex: Int) {
        val cartRecommendationList = ArrayList<CartRecommendationItemHolderData>()
        cartRecommendationList.add(it[currentIndex])
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(
                dPresenter.generateRecommendationImpressionDataAnalytics(currentIndex, cartRecommendationList, FLAG_IS_CART_EMPTY)
        )
    }

    private fun sendImpressionTwoRecommendationItems(it: List<CartRecommendationItemHolderData>, currentIndex: Int) {
        val cartRecommendationList = ArrayList<CartRecommendationItemHolderData>()
        cartRecommendationList.add(it[currentIndex - 1])
        cartRecommendationList.add(it[currentIndex])
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(
                dPresenter.generateRecommendationImpressionDataAnalytics(currentIndex, cartRecommendationList, FLAG_IS_CART_EMPTY)
        )
    }

    override fun onButtonAddToCartClicked(productModel: Any) {
        dPresenter.processAddToCart(productModel)
    }

    override fun onShowActionSeeOtherProduct(productId: String, errorType: String) {
        cartPageAnalytics.eventClickSeeOtherProductOnUnavailableSection(userSession.userId, productId, errorType)
    }

    override fun onSimilarProductUrlClicked(similarProductUrl: String) {
        routeToApplink(similarProductUrl)
        cartPageAnalytics.eventClickMoreLikeThis()
    }

    override fun onFollowShopClicked(shopId: String, errorType: String) {
        cartPageAnalytics.eventClickFollowShop(userSession.userId, errorType, shopId)
        dPresenter.followShop(shopId)
    }

    override fun onSeeErrorProductsClicked() {
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

    override fun onCartShopNameClicked(shopId: String?, shopName: String?) {
        if (shopId != null && shopName != null) {
            cartPageAnalytics.eventClickAtcCartClickShop(shopId, shopName)
            routeToShopPage(shopId)
        }
    }

    override fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean) {
        dPresenter.setHasPerformChecklistChange(true)
        cartAdapter.setShopSelected(itemPosition, checked)
        onNeedToUpdateViewItem(itemPosition)
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)
        validateGoToCheckout()
        dPresenter.saveCheckboxState(cartAdapter.allCartItemHolderData)

        val params = generateParamValidateUsePromoRevamp(checked, -1, -1, false)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }

        setCheckboxGlobalState()
        setGlobalDeleteVisibility()
    }

    override fun onCartDataEnableToCheckout() {
        if (isAdded) {
            binding?.vDisabledGoToCourierPageButton?.gone()
            binding?.goToCourierPageButton?.isEnabled = true
            binding?.goToCourierPageButton?.setOnClickListener { checkGoToShipment("") }
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

    override fun onCartItemQuantityInputFormClicked(qty: String?) {
        qty?.let {
            cartPageAnalytics.eventClickAtcCartClickInputQuantity(it)
        }
    }

    override fun onCartItemLabelInputRemarkClicked() {
        cartPageAnalytics.eventClickAtcCartClickTulisCatatan()
    }

    override fun onCartItemCheckChanged(position: Int, parentPosition: Int, checked: Boolean) {
        dPresenter.setHasPerformChecklistChange(true)
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)
        setCheckboxGlobalState()
        setGlobalDeleteVisibility()

        validateGoToCheckout()
        cartAdapter.setItemSelected(position, parentPosition, checked)
        val params = generateParamValidateUsePromoRevamp(checked, parentPosition, position, false)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
        dPresenter.saveCheckboxState(cartAdapter.allCartItemHolderData)
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

    override fun onWishlistCheckChanged(productId: String?, cartId: Long?, imageView: ImageView?) {
        cartPageAnalytics.eventClickMoveToWishlistOnAvailableSection(userSession.userId, productId
                ?: "")
        imageView?.let {
            setProductImageAnimationData(it, false)
        }

        val isLastItem = cartAdapter.allCartItemData.size == 1
        dPresenter.processAddCartToWishlist(productId ?: "", cartId?.toString()
                ?: "", isLastItem, WISHLIST_SOURCE_AVAILABLE_ITEM)
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
                alpha = 0.5f
            } else {
                val size = resources.getDimensionPixelOffset(R.dimen.dp_72)
                layoutParams.width = size
                layoutParams.width = size
                alpha = 1.0f
            }

            x = xCoordinate.toFloat()
            y = yCoordinate.toFloat() - (height / 3)
        }
    }

    override fun onNeedToRefreshSingleShop(parentPosition: Int) {
        onNeedToUpdateViewItem(parentPosition)
    }

    override fun onNeedToRefreshMultipleShop() {
        val firstShopIndexAndCount = cartAdapter.getFirstShopAndShopCount()
        onNeedToUpdateMultipleViewItem(firstShopIndexAndCount.first, firstShopIndexAndCount.second)
    }

    override fun onNeedToRecalculate() {
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)
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
                binding?.llCartContainer?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N50))
            }

            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N50))
        }
    }

    override fun renderInitialGetCartListDataSuccess(cartListData: CartListData?) {
        recommendationPage = 1
        cartListData?.let {
            if (it.outOfServiceData.id != 0) {
                renderCartOutOfService(it.outOfServiceData)
                return@let
            }

            sendAnalyticsScreenNameCartPage()
            updateStateAfterFinishGetCartList(it)

            renderCheckboxGlobal(cartListData)
            renderTickerAnnouncement(it)
            renderChooseAddressWidget(cartListData.localizationChooseAddressData)

            validateRenderCart(it)
            validateShowPopUpMessage(cartListData)
            validateRenderWishlist()
            validateRenderRecentView()
            loadRecommendation()
            validateRenderPromo(cartListData)

            setInitialCheckboxGlobalState(cartListData)
            setGlobalDeleteVisibility()

            validateGoToCheckout()
        }
    }

    private fun updateStateAfterFinishGetCartList(it: CartListData) {
        this.cartListData = it
        endlessRecyclerViewScrollListener.resetState()
        refreshHandler?.finishRefresh()
        cartAdapter.resetData()
    }

    private fun sendAnalyticsScreenNameCartPage() {
        cartPageAnalytics.sendScreenName(activity, screenName)
    }

    private fun loadRecommendation() {
        dPresenter.processGetRecommendationData(recommendationPage, cartAdapter.allCartItemProductId)
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
            dPresenter.processGetWishlistData()
        } else {
            renderWishlist(null, false)
        }
    }

    private fun validateRenderCart(it: CartListData) {
        if (it.shopGroupAvailableDataList.isEmpty() && it.unavailableGroupData.isEmpty()) {
            renderCartEmpty(it)
            setTopLayoutVisibility(false)
        } else {
            renderCartNotEmpty(it)
            setTopLayoutVisibility(true)
        }
    }

    private fun validateRenderPromo(cartListData: CartListData) {
        if (dPresenter.isLastApplyValid()) {
            // Render promo from last apply
            validateRenderPromoFromLastApply(cartListData)

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
            val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
        }
    }

    private fun validateRenderPromoFromValidateUseCartPage() {
        dPresenter.getUpdateCartAndValidateUseLastResponse()?.promoUiModel?.let {
            val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
            renderPromoCheckout(lastApplyUiModel)
        }
    }

    private fun validateRenderPromoFromLastApply(cartListData: CartListData) {
        cartListData.lastApplyShopGroupSimplifiedData?.let { lastApplyData ->
            // show toaster if any promo applied has been changed
            if (lastApplyData.additionalInfo.errorDetail.message.isNotEmpty()) {
                showToastMessageGreen(lastApplyData.additionalInfo.errorDetail.message)
                PromoRevampAnalytics.eventCartViewPromoMessage(lastApplyData.additionalInfo.errorDetail.message)
            }
            renderPromoCheckout(lastApplyData)
        }
    }

    private fun validateShowPopUpMessage(cartListData: CartListData) {
        if (cartListData.popUpMessage.isNotBlank()) {
            showToastMessageGreen(cartListData.popUpMessage)
        }
    }

    private fun setInitialCheckboxGlobalState(cartListData: CartListData) {
        binding?.topLayout?.checkboxGlobal?.isChecked = cartListData.isAllSelected
    }

    private fun renderChooseAddressWidget(localizationChooseAddressData: LocalizationChooseAddressData) {
        activity?.let {
            if (localizationChooseAddressData.state == LocalizationChooseAddressData.STATE_EMPTY) {
                val chooseAddressWidgetPosition = cartAdapter.removeChooseAddressWidget()
                onNeedToRemoveViewItem(chooseAddressWidgetPosition)
            } else {
                validateLocalCacheAddress(it, localizationChooseAddressData)

                if (ChooseAddressUtils.isRollOutUser(it)) {
                    val cartChooseAddressHolderData = CartChooseAddressHolderData()
                    cartAdapter.addChooseAddressWidget(cartChooseAddressHolderData)
                }
            }
        }
    }

    private fun validateLocalCacheAddress(activity: FragmentActivity, localizationChooseAddressData: LocalizationChooseAddressData) {
        var snippetMode = false
        ChooseAddressUtils.getLocalizingAddressData(activity)?.let {
            if (it.address_id.toLongOrZero() == 0L && it.district_id.toLongOrZero() != 0L) {
                snippetMode = true
            }
        }

        if (!snippetMode) {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    context = activity,
                    addressId = localizationChooseAddressData.addressId,
                    cityId = localizationChooseAddressData.cityId,
                    districtId = localizationChooseAddressData.districtId,
                    lat = localizationChooseAddressData.latitude,
                    long = localizationChooseAddressData.longitude,
                    label = String.format("%s %s", localizationChooseAddressData.addressName, localizationChooseAddressData.receiverName),
                    postalCode = localizationChooseAddressData.postalCode)
        }
    }

    private fun renderCartOutOfService(outOfServiceData: OutOfServiceData) {
        binding?.apply {
            when (outOfServiceData.id) {
                ID_MAINTENANCE, ID_TIMEOUT, ID_OVERLOAD -> {
                    layoutGlobalError.setType(GlobalError.SERVER_ERROR)
                    outOfServiceData.buttons.firstOrNull()?.let { buttonData ->
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

            if (outOfServiceData.title.isNotBlank()) {
                layoutGlobalError.errorTitle.text = outOfServiceData.title
            }
            if (outOfServiceData.description.isNotBlank()) {
                layoutGlobalError.errorDescription.text = outOfServiceData.description
            }
            if (outOfServiceData.image.isNotBlank()) {
                layoutGlobalError.errorIllustration.setImage(outOfServiceData.image, 0f)
            }

            showErrorContainer()

            cartPageAnalytics.eventViewErrorPageWhenLoadCart(userSession.userId, outOfServiceData.getErrorType())
        }
    }

    private fun renderCartNotEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = false
        cartAdapter.removeCartEmptyData()

        renderTickerError(cartListData)
        renderCartAvailableItems(cartListData)
        renderCartUnavailableItems(cartListData)

        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = cartAdapter.allCartItemData
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
                dPresenter.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_0)
        )

        cartAdapter.notifyDataSetChanged()

        setActivityBackgroundColor()
    }

    private fun renderCartEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = true

        cartListData.lastApplyShopGroupSimplifiedData?.let { lastApplyData ->
            if (lastApplyData.additionalInfo.emptyCartInfo.message.isNotEmpty()) {
                renderCartEmptyWithPromo(lastApplyData)
            } else {
                renderCartEmptyDefault()
            }
        }
        enableSwipeRefresh()
        showEmptyCartContainer()
        notifyBottomCartParent()

        cartAdapter.notifyDataSetChanged()

        setActivityBackgroundColor()
        cartPageAnalytics.eventViewAtcCartImpressionCartEmpty()
    }

    private fun renderTickerAnnouncement(cartListData: CartListData) {
        val tickerData = cartListData.tickerData
        if (tickerData?.isValid(CART_PAGE) == true) {
            cartAdapter.addCartTicker(TickerAnnouncementHolderData(tickerData.id, tickerData.message))
        }
    }

    private fun renderPromoCheckout(lastApplyUiModel: LastApplyUiModel) {
        renderPromoCheckoutButton(lastApplyUiModel)
    }

    override fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>) {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.ACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title = getString(R.string.promo_funnel_label)
            promoCheckoutBtnCart.desc = ""
            promoCheckoutBtnCart.setOnClickListener {
                dPresenter.doUpdateCartForPromo()
                // analytics
                PromoRevampAnalytics.eventCartClickPromoSection(listPromoApplied, false)
            }
        }
    }

    private fun renderPromoCheckoutButtonNoItemIsSelected() {
        binding?.apply {
            promoCheckoutBtnCart.state = ButtonPromoCheckoutView.State.ACTIVE
            promoCheckoutBtnCart.margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
            promoCheckoutBtnCart.title = getString(R.string.promo_funnel_label)
            promoCheckoutBtnCart.desc = getString(R.string.promo_desc_no_selected_item)
            promoCheckoutBtnCart.setOnClickListener {
                showToastMessageGreen(getString(R.string.promo_choose_item_cart))
                PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
            }
        }
    }

    private fun renderPromoCheckoutButton(lastApplyData: LastApplyUiModel) {
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
                getString(R.string.promo_funnel_label)
            }
        }

        if (lastApplyData.additionalInfo.messageInfo.detail.isNotEmpty()) {
            isApplied = true
            binding?.promoCheckoutBtnCart?.desc = lastApplyData.additionalInfo.messageInfo.detail
        } else {
            isApplied = false

            if (cartAdapter.selectedCartItemData.isEmpty()) {
                binding?.promoCheckoutBtnCart?.desc = getString(R.string.promo_desc_no_selected_item)
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
                PromoRevampAnalytics.eventCartClickPromoSection(getAllPromosApplied(lastApplyData), isApplied)
            }
        }
        if (isApplied) {
            PromoRevampAnalytics.eventCartViewPromoAlreadyApplied()
        }

        cartListData?.shoppingSummaryData?.promoValue = lastApplyData.benefitSummaryInfo.finalBenefitAmount
    }

    private fun setLastApplyDataToShopGroup(lastApplyData: LastApplyUiModel) {
        cartListData?.lastApplyShopGroupSimplifiedData = lastApplyData
    }

    private fun renderPromoSummaryFromStickyPromo(lastApplyData: LastApplyUiModel) {
        cartListData?.promoSummaryData?.details?.clear()
        cartListData?.promoSummaryData?.details?.addAll(
                lastApplyData.additionalInfo.usageSummaries.map {
                    PromoSummaryDetailData(
                            description = it.description,
                            type = it.type,
                            amountStr = it.amountStr,
                            amount = it.amount.toDouble(),
                            currencyDetailStr = it.currencyDetailsStr
                    )
                }.toList()
        )
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
            params.orders.forEach {
                it?.let { orderItem ->
                    if (orderItem.codes.isNotEmpty()) {
                        orderItem.codes.forEach { merchantCode ->
                            allPromoApplied.add(merchantCode)
                        }
                    }
                }
            }
            params.codes.forEach {
                it?.let { globalCode ->
                    allPromoApplied.add(globalCode)
                }
            }
        }
        if (params.orders.isNotEmpty() && allPromoApplied.isNotEmpty()) isPromoApplied = true
        return isPromoApplied
    }

    private fun getAllAppliedPromoCodes(params: ValidateUsePromoRequest): List<String> {
        val allPromoApplied = arrayListOf<String>()
        if (params.orders.isNotEmpty()) {
            params.orders.forEach {
                it?.let { orderItem ->
                    if (orderItem.codes.isNotEmpty()) {
                        orderItem.codes.forEach { merchantCode ->
                            allPromoApplied.add(merchantCode)
                        }
                    }
                }
            }
            params.codes.forEach {
                it?.let { globalCode ->
                    allPromoApplied.add(globalCode)
                }
            }
        }
        return allPromoApplied
    }


    // NOTES:
    // if position = -1, then isChecked for all (shop level)
    // if ignoreIsChecked = true, then no position nor isChecked is gained
    private fun generateParamValidateUsePromoRevamp(isChecked: Boolean, parentPosition: Int, position: Int, ignoreIsChecked: Boolean): ValidateUsePromoRequest {
        val globalPromo = arrayListOf<String>()
        cartListData?.lastApplyShopGroupSimplifiedData?.codes?.forEach {
            globalPromo.add(it)
        }

        val listOrder = arrayListOf<OrdersItem>()
        var cartItemHolderData: ShopGroupAvailableData

        var countListShop = 0
        cartListData?.shopGroupAvailableDataList?.let { countListShop = it.size }

        if (!ignoreIsChecked && parentPosition != -1) {
            for (i in 0 until countListShop) {
                val listPromoCodes = arrayListOf<String>()
                cartListData?.shopGroupAvailableDataList?.get(i)?.let { it ->
                    cartItemHolderData = it

                    cartListData?.lastApplyShopGroupSimplifiedData?.voucherOrders?.forEach { lastApplyVoucherOrders ->
                        cartItemHolderData.cartString?.let { cartString ->
                            if (cartString.equals(lastApplyVoucherOrders.uniqueId, true)) {
                                listPromoCodes.add(lastApplyVoucherOrders.code)
                            }
                        }
                    }

                    var countListItem = 0
                    cartItemHolderData.cartItemDataList?.let { countListItem = it.size }
                    val countAdapterItemBeforeCartItem = cartAdapter.getItemCountBeforeCartItem()
                    val parentPositionDifference = countAdapterItemBeforeCartItem + 1
                    if (i == (parentPosition - parentPositionDifference)) {
                        val listProductDetail = arrayListOf<ProductDetailsItem>()
                        for (j in 0 until countListItem) {
                            if (position != -1 && j == position) {
                                cartItemHolderData.cartItemDataList?.get(j)?.isSelected = isChecked
                            }
                            cartItemHolderData.cartItemDataList?.get(j)?.isSelected?.let { isItemSelected ->
                                if (isItemSelected) {
                                    doAddToListProducts(cartItemHolderData, j, listProductDetail)
                                }
                            }
                        }
                        if (listProductDetail.isNotEmpty()) {
                            doAddToOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
                        }
                    } else {
                        val listProductDetail = arrayListOf<ProductDetailsItem>()
                        for (j in 0 until countListItem) {
                            cartItemHolderData.cartItemDataList?.get(j)?.isSelected?.let { isItemSelected ->
                                if (isItemSelected) {
                                    doAddToListProducts(cartItemHolderData, j, listProductDetail)
                                }
                            }
                        }
                        if (listProductDetail.isNotEmpty()) {
                            doAddToOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
                        }
                    }
                }
            }
        } else if (!ignoreIsChecked && parentPosition == -1) {
            cartAdapter.selectedCartShopHolderData.forEach { cartShop ->
                val listProductDetail = arrayListOf<ProductDetailsItem>()

                val listPromoCodes = arrayListOf<String>()
                if (isChecked) {
                    // ambil dari shopgroup
                    cartListData?.shopGroupAvailableDataList?.forEach { shopGroup ->
                        if (cartShop.shopGroupAvailableData?.cartString.equals(shopGroup.cartString)) {
                            shopGroup.promoCodes?.forEach {
                                listPromoCodes.add(it)
                            }
                        }
                    }
                } else {
                    cartListData?.lastApplyShopGroupSimplifiedData?.voucherOrders?.forEach { lastApplyVoucherOrders ->
                        cartShop.shopGroupAvailableData?.cartString?.let { cartString ->
                            if (cartString.equals(lastApplyVoucherOrders.uniqueId, true)) {
                                listPromoCodes.add(lastApplyVoucherOrders.code)
                            }
                        }
                    }
                }

                var countItemList: Int
                cartShop.shopGroupAvailableData?.let { shopGroupAvailableData ->
                    shopGroupAvailableData.cartItemDataList?.let { listCartItemHolderData ->
                        countItemList = listCartItemHolderData.size
                        for (j in 0 until countItemList) {
                            if (listCartItemHolderData[j].isSelected) {
                                doAddToListProducts(shopGroupAvailableData, j, listProductDetail)
                            }
                        }
                        if (listProductDetail.isNotEmpty()) {
                            doAddToOrderListRequest(shopGroupAvailableData, listProductDetail, listPromoCodes, listOrder)
                        }
                    }
                }
            }

        } else if (ignoreIsChecked) {
            for (i in 0 until countListShop) {
                cartListData?.shopGroupAvailableDataList?.get(i)?.let {
                    cartItemHolderData = it

                    val listPromoCodes = arrayListOf<String>()
                    cartListData?.lastApplyShopGroupSimplifiedData?.voucherOrders?.forEach { lastApplyVoucherOrders ->
                        it.cartString?.let { cartString ->
                            if (cartString.equals(lastApplyVoucherOrders.uniqueId, true)) {
                                listPromoCodes.add(lastApplyVoucherOrders.code)
                            }
                        }
                    }

                    if (it.promoCodes?.isNotEmpty() == true) {
                        it.promoCodes?.forEach {
                            if (!listPromoCodes.contains(it)) {
                                listPromoCodes.add(it)
                            }
                        }
                    }

                    var countListItem = 0
                    cartItemHolderData.cartItemDataList?.let { countListItem = it.size }
                    val listProductDetail = arrayListOf<ProductDetailsItem>()
                    for (j in 0 until countListItem) {
                        cartItemHolderData.cartItemDataList?.get(j)?.isSelected?.let { isItemSelected ->
                            if (isItemSelected) {
                                doAddToListProducts(cartItemHolderData, j, listProductDetail)
                            }
                        }
                    }
                    if (listProductDetail.isNotEmpty()) {
                        doAddToOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
                    }
                }
            }
        }

        val lastValidateUseResponse = dPresenter.getValidateUseLastResponse()
        if (lastValidateUseResponse?.promoUiModel != null) {
            // Goes here if user has applied / un applied promo code from promo page and there's still promo code applied

            // Clear promo first
            globalPromo.clear()
            for (ordersItem in listOrder) {
                ordersItem.codes.clear()
            }

            // Then set promo codes
            lastValidateUseResponse.promoUiModel.codes.forEach {
                if (!globalPromo.contains(it)) globalPromo.add(it)
            }
            lastValidateUseResponse.promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
                listOrder.forEach { order ->
                    if (voucherOrder?.uniqueId == order.uniqueId) {
                        if (!order.codes.contains(voucherOrder.code)) {
                            order.codes.add(voucherOrder.code)
                        }
                    }
                }
            }
        } else {
            if (!dPresenter.isLastApplyValid()) {
                // Goes here if user has reset promo code from promo page
                // We should be not send any promo code
                globalPromo.clear()
                for (ordersItem in listOrder) {
                    ordersItem.codes.clear()
                }
            }
        }

        return ValidateUsePromoRequest(
                codes = globalPromo.toMutableList(),
                state = PARAM_CART,
                skipApply = 0,
                cartType = PARAM_DEFAULT,
                orders = listOrder)
    }

    private fun doAddToListProducts(cartItemHolderData: ShopGroupAvailableData, j: Int, listProductDetail: ArrayList<ProductDetailsItem>) {
        cartItemHolderData.cartItemDataList?.get(j)?.let { cartItemData ->
            if (cartItemData.isSelected) {
                val productDetail = cartItemData.cartItemData?.originData?.productId?.toLong()?.let {
                    cartItemData.cartItemData?.updatedData?.quantity?.let { it1 ->
                        ProductDetailsItem(
                                productId = it,
                                quantity = it1
                        )
                    }
                }
                productDetail?.let { listProductDetail.add(it) }
            }
        }
    }

    private fun doAddToOrderListRequest(cartItemHolderData: ShopGroupAvailableData,
                                        listProductDetail: ArrayList<ProductDetailsItem>,
                                        listPromoCodes: ArrayList<String>,
                                        listOrder: ArrayList<OrdersItem>) {
        cartItemHolderData.shopId?.toLong()?.let { shopId ->
            cartItemHolderData.cartString?.let { cartString ->
                val order = OrdersItem(
                        shopId = shopId,
                        uniqueId = cartString,
                        productDetails = listProductDetail,
                        codes = listPromoCodes)
                listOrder.add(order)
            }
        }
    }

    private fun generateParamsCouponList(): PromoRequest {
        val listOrder = ArrayList<Order>()
        cartListData?.shopGroupAvailableDataList?.forEach { shop ->
            shop.shopId?.toLong()?.let { shopId ->
                shop.cartString?.let { cartString ->
                    val listProductDetail = arrayListOf<ProductDetail>()
                    var hasCheckedItem = false
                    shop.cartItemDataList?.forEach { cartItem ->
                        if (!hasCheckedItem && cartItem.isSelected) {
                            hasCheckedItem = true
                        }
                        val productDetail = ProductDetail(
                                productId = cartItem.cartItemData?.originData?.productId?.toLong()
                                        ?: 0,
                                quantity = cartItem.cartItemData?.updatedData?.quantity ?: 0
                        )
                        listProductDetail.add(productDetail)
                    }
                    val order = Order(
                            shopId = shopId,
                            uniqueId = cartString,
                            product_details = listProductDetail,
                            codes = shop.promoCodes?.toMutableList() ?: mutableListOf(),
                            isChecked = hasCheckedItem)
                    listOrder.add(order)
                }
            }
        }

        val globalPromo = arrayListOf<String>()

        val lastValidateUseResponse = dPresenter.getValidateUseLastResponse()
        if (lastValidateUseResponse?.promoUiModel != null) {
            lastValidateUseResponse.promoUiModel.codes.forEach {
                if (!globalPromo.contains(it)) globalPromo.add(it)
            }
            lastValidateUseResponse.promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
                listOrder.forEach { order ->
                    if (voucherOrder?.uniqueId == order.uniqueId) {
                        if (!order.codes.contains(voucherOrder.code)) {
                            order.codes.add(voucherOrder.code)
                        }
                    }
                }
            }
        }

        if (dPresenter.isLastApplyValid()) {
            cartListData?.lastApplyShopGroupSimplifiedData?.codes?.forEach {
                if (!globalPromo.contains(it)) globalPromo.add(it)
            }
            cartListData?.lastApplyShopGroupSimplifiedData?.voucherOrders?.forEach { lastApplyData ->
                listOrder.forEach { order ->
                    if (lastApplyData.uniqueId == order.uniqueId) {
                        if (lastApplyData.code.isNotBlank() && !order.codes.contains(lastApplyData.code)) {
                            order.codes.add(lastApplyData.code)
                        }
                    }
                }
            }
        }

        return PromoRequest(
                codes = globalPromo,
                state = "cart",
                isSuggested = 0,
                orders = listOrder)
    }

    private fun renderTickerError(cartListData: CartListData) {
        if (cartListData.isError && cartListData.shopGroupAvailableDataList.isNotEmpty()) {
            val cartItemTickerErrorHolderData = CartItemTickerErrorHolderData()
            cartItemTickerErrorHolderData.cartTickerErrorData = cartListData.cartTickerErrorData
            cartAdapter.addCartTickerError(cartItemTickerErrorHolderData)
        }
    }

    private fun renderCheckboxGlobal(cartListData: CartListData) {
        if (cartListData.shopGroupAvailableDataList.isNotEmpty()) {
            cartAdapter.addSelectAll(CartSelectAllHolderData(cartListData.isAllSelected))
        }
    }

    private fun renderCartAvailableItems(cartListData: CartListData) {
        cartAdapter.addAvailableDataList(cartListData.shopGroupAvailableDataList)
    }

    private fun renderCartUnavailableItems(cartListData: CartListData) {
        if (cartListData.unavailableGroupData.isNotEmpty()) {
            var showAccordion = false
            cartAdapter.addNotAvailableHeader(
                    viewHolderDataMapper.mapDisabledItemHeaderHolderData(cartListData.cartTickerErrorData?.errorCount
                            ?: 0)
            )
            if (!showAccordion && cartListData.unavailableGroupData.size > 1) {
                showAccordion = true
            }
            cartListData.unavailableGroupData.forEach {
                val disabledReasonHolderData = viewHolderDataMapper.mapDisabledReasonHolderData(it)
                cartAdapter.addNotAvailableReason(disabledReasonHolderData)
                if (!showAccordion && it.shopGroupWithErrorDataList.size > 1) {
                    showAccordion = true
                }
                it.shopGroupWithErrorDataList.forEach {
                    val cartItemHolderDataList = it.cartItemHolderDataList
                    if (cartItemHolderDataList.isNotEmpty()) {
                        if (!showAccordion && cartItemHolderDataList.size > 1) {
                            showAccordion = true
                        }
                        cartAdapter.addNotAvailableShop(viewHolderDataMapper.mapDisabledShopHolderData(it))
                        for ((index, value) in cartItemHolderDataList.withIndex()) {
                            cartAdapter.addNotAvailableProduct(viewHolderDataMapper.mapDisabledItemHolderData(value, index != cartItemHolderDataList.size - 1))
                        }
                    }
                }
            }

            if (showAccordion) {
                val accordionHolderData = viewHolderDataMapper.mapDisabledAccordionHolderData(cartListData)
                cartAdapter.addNotAvailableAccordion(accordionHolderData)
                collapseOrExpandDisabledItem(accordionHolderData)

                if (!accordionCollapseState) {
                    accordionHolderData.isCollapsed = false
                    collapseOrExpandDisabledItem(accordionHolderData)
                }
            }
        }
    }

    private fun renderCartEmptyDefault() {
        val cartEmptyHolderData = buildCartEmptyHolderData()
        cartAdapter.addCartEmptyData(cartEmptyHolderData)
    }

    private fun buildCartEmptyHolderData(): CartEmptyHolderData {
        val cartEmptyHolderData = CartEmptyHolderData(
                title = getString(R.string.checkout_module_keranjang_belanja_kosong_new),
                desc = getString(R.string.checkout_empty_cart_sub_message_new),
                imgUrl = CART_EMPTY_DEFAULT_IMG_URL,
                btnText = getString(R.string.checkout_module_mulai_belanja)
        )
        return cartEmptyHolderData
    }

    private fun renderCartEmptyWithPromo(lastApplyData: LastApplyUiModel) {
        val cartEmptyWithPromoHolderData = buildCartEmptyWithPromoHolderData(lastApplyData)

        // analytics
        cartAdapter.addCartEmptyData(cartEmptyWithPromoHolderData)
        val listPromos = getAllPromosApplied(lastApplyData)
        PromoRevampAnalytics.eventCartEmptyPromoApplied(listPromos)
    }

    private fun buildCartEmptyWithPromoHolderData(lastApplyData: LastApplyUiModel): CartEmptyHolderData {
        var title = getString(R.string.cart_empty_with_promo_title)
        var desc = getString(R.string.cart_empty_with_promo_desc)
        var imgUrl = CART_EMPTY_WITH_PROMO_IMG_URL

        if (lastApplyData.additionalInfo.emptyCartInfo.message.isNotEmpty()) title = lastApplyData.additionalInfo.emptyCartInfo.message
        if (lastApplyData.additionalInfo.emptyCartInfo.detail.isNotEmpty()) desc = lastApplyData.additionalInfo.emptyCartInfo.detail
        if (lastApplyData.additionalInfo.emptyCartInfo.imgUrl.isNotEmpty()) imgUrl = lastApplyData.additionalInfo.emptyCartInfo.imgUrl
        val cartEmptyWithPromoHolderData = CartEmptyHolderData(
                title = title,
                desc = desc,
                imgUrl = imgUrl,
                btnText = getString(R.string.cart_empty_with_promo_btn)
        )
        return cartEmptyWithPromoHolderData
    }

    override fun stopCartPerformanceTrace() {
        if (!isTraceCartStopped) {
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

    private fun showMainContainerLoadingInitData() {
        binding?.apply {
            layoutGlobalError.gone()
            rlContent.show()
            bottomLayout.gone()
            bottomLayoutShadow.gone()
            llPromoCheckout.gone()
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

    override fun renderToShipmentFormSuccess(eeCheckoutData: Map<String, Any>,
                                             cartItemDataList: List<CartItemData>,
                                             checkoutProductEligibleForCashOnDelivery: Boolean,
                                             condition: Int) {
        when (condition) {
            CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessDefault(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM -> cartPageAnalytics.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
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
        showToastMessageRed(message)

        refreshCart()
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

    override fun renderErrorToShipmentForm(outOfServiceData: OutOfServiceData) {
        renderCartOutOfService(outOfServiceData)
    }

    private fun disableSwipeRefresh() {
        refreshHandler?.setPullEnabled(false)
    }

    private fun enableSwipeRefresh() {
        refreshHandler?.setPullEnabled(true)
    }

    override fun getAllCartDataList(): List<CartItemData> {
        return cartAdapter.allCartItemData
    }

    override fun getAllAvailableCartDataList(): List<CartItemData> {
        return cartAdapter.allAvailableCartItemData
    }

    override fun getAllShopDataList(): List<CartShopHolderData> {
        return cartAdapter.allShopGroupDataList
    }

    override fun getAllSelectedCartDataList(): List<CartItemData>? {
        return cartAdapter.selectedCartItemData
    }

    override fun renderDetailInfoSubTotal(qty: String,
                                          subtotalBeforeSlashedPrice: Double,
                                          subtotalPrice: Double,
                                          selectAllItem: Boolean,
                                          unselectAllItem: Boolean,
                                          noAvailableItems: Boolean) {
        if (noAvailableItems) {
            binding?.llPromoCheckout?.gone()
        } else {
            if (binding?.bottomLayout?.visibility == View.VISIBLE) {
                binding?.llPromoCheckout?.show()
            }
        }

        renderTotalPrice(subtotalPrice, qty)
        updateShoppingSummaryData(qty, subtotalBeforeSlashedPrice, subtotalPrice)
    }

    private fun renderTotalPrice(subtotalPrice: Double, qty: String) {
        var totalPriceString = "-"
        if (subtotalPrice > 0) {
            totalPriceString = CurrencyFormatUtil.convertPriceValueToIdrFormat(subtotalPrice.toLong(), false).removeDecimalSuffix()
        }

        binding?.tvTotalPrices?.text = totalPriceString
        binding?.goToCourierPageButton?.text = String.format(getString(R.string.cart_item_button_checkout_count_format), qty)
        if (totalPriceString == "-") {
            binding?.imgChevronSummary?.gone()
            onCartDataDisableToCheckout()
        } else {
            binding?.imgChevronSummary?.show()
            onCartDataEnableToCheckout()
        }
    }

    private fun updateShoppingSummaryData(qty: String, subtotalBeforeSlashedPrice: Double, subtotalPrice: Double) {
        cartListData?.shoppingSummaryData?.qty = qty
        if (subtotalBeforeSlashedPrice == 0.0) {
            cartListData?.shoppingSummaryData?.totalValue = subtotalPrice.toInt()
        } else {
            cartListData?.shoppingSummaryData?.totalValue = subtotalBeforeSlashedPrice.toInt()
        }
        cartListData?.shoppingSummaryData?.discountValue = (subtotalBeforeSlashedPrice - subtotalPrice).toInt()
        cartListData?.shoppingSummaryData?.paymentTotal = subtotalPrice.toInt()
    }

    override fun updateCashback(cashback: Double) {
        cartAdapter.updateShipmentSellerCashback(cashback)
        cartListData?.shoppingSummaryData?.sellerCashbackValue = cashback.toInt()
    }

    override fun showToastMessageRed(message: String, actionText: String, ctaClickListener: View.OnClickListener?) {
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
                Toaster.build(it, tmpMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, actionText, tmpCtaClickListener)
                        .show()
            } else {
                Toaster.build(it, tmpMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, it.resources.getString(R.string.checkout_flow_toaster_action_ok), tmpCtaClickListener)
                        .show()
            }
        }
    }

    override fun showToastMessageRed(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (!(throwable is CartResponseErrorException || throwable is AkamaiErrorException)) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        showToastMessageRed(errorMessage)
    }

    override fun showToastMessageGreen(message: String, actionText: String, onClickListener: View.OnClickListener?) {
        view?.let { v ->
            var tmpCtaClickListener = View.OnClickListener { }

            if (onClickListener != null) {
                tmpCtaClickListener = onClickListener
            }

            initializeToasterLocation()
            if (actionText.isNotBlank()) {
                Toaster.toasterCustomCtaWidth = v.resources.getDimensionPixelOffset(R.dimen.dp_100)
                Toaster.build(v, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, actionText, tmpCtaClickListener)
                        .show()
            } else {
                Toaster.build(v, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, v.resources.getString(R.string.checkout_flow_toaster_action_ok), tmpCtaClickListener)
                        .show()
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
        showMainContainerLoadingInitData()
    }

    override fun renderLoadGetCartDataFinish() {
        if (refreshHandler?.isRefreshing == true) {
            refreshHandler?.isRefreshing = false
        }
        showMainContainer()
    }

    override fun onDeleteCartDataSuccess(deletedCartIds: List<String>,
                                         removeAllItems: Boolean,
                                         forceExpandCollapsedUnavailableItems: Boolean,
                                         isMoveToWishlist: Boolean,
                                         isFromGlobalCheckbox: Boolean) {
        var message = String.format(getString(R.string.message_product_already_deleted), deletedCartIds.size)

        if (isMoveToWishlist) {
            message = String.format(getString(R.string.message_product_already_moved_to_wishlist), deletedCartIds.size)
            refreshWishlistAfterItemRemoveAndMoveToWishlist()
        }

        if (isFromGlobalCheckbox || deletedCartIds.size > 1) {
            showToastMessageGreen(message)
        } else {
            showToastMessageGreen(message, getString(R.string.toaster_cta_cancel), View.OnClickListener { onUndoDeleteClicked(deletedCartIds) })
        }

        val updateListResult = cartAdapter.removeCartItemById(deletedCartIds, context)
        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        hideProgressLoading()

        setTopLayoutVisibility()

        if (removeAllItems) {
            refreshCart()
        } else {
            setLastItemAlwaysSelected()
        }

    }

    private fun setTopLayoutVisibility() {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (cartAdapter.hasAvailableItemLeft()) {
            binding?.topLayout?.root?.show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                    isShowToolbarShadow = true
                }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (binding?.appBarLayout?.elevation == HAS_ELEVATION.toFloat()) {
                    isShowToolbarShadow = true
                }
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

    override fun onUndoDeleteCartDataSuccess(undoDeleteCartData: UndoDeleteCartData) {
        dPresenter.processInitialGetCartData(getCartId(), false, false)
    }

    override fun onAddCartToWishlistSuccess(message: String, productId: String, cartId: String, isLastItem: Boolean, source: String, forceExpandCollapsedUnavailableItems: Boolean) {
        animateProductImage(message)

        when (source) {
            WISHLIST_SOURCE_AVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
            }
            WISHLIST_SOURCE_UNAVAILABLE_ITEM -> {
                cartPageAnalytics.eventAddWishlistUnavailableSection(FLAG_IS_CART_EMPTY, productId)
            }
        }

        val updateListResult = cartAdapter.removeCartItemById(listOf(cartId), context)
        removeLocalCartItem(updateListResult, forceExpandCollapsedUnavailableItems)

        setTopLayoutVisibility()

        if (isLastItem) {
            refreshCart()
        } else {
            setLastItemAlwaysSelected()
        }
    }

    private fun setLastItemAlwaysSelected() {
        val tmpIsLastItem = cartAdapter.setLastItemAlwaysSelected()
        if (tmpIsLastItem) {
            binding?.topLayout?.checkboxGlobal?.isChecked = true
        }
    }

    private fun animateProductImage(message: String) {
        val tmpAnimatedImage = binding?.tmpAnimatedImage ?: return
        var target: Pair<Int, Int>? = null

        if (toolbarType.equals(TOOLBAR_VARIANT_NAVIGATION, true)) {
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
        val animAlpha = ObjectAnimator.ofFloat(tmpAnimatedImage, "alpha", 1.0f, 0.0f)
        val animScaleX = ObjectAnimator.ofFloat(tmpAnimatedImage, "scaleX", 1.0f, 0.5f)
        val animScaleY = ObjectAnimator.ofFloat(tmpAnimatedImage, "scaleY", 1.0f, 0.5f)

        AnimatorSet().let {
            it.playTogether(animY, animX, animAlpha, animScaleX, animScaleY)
            it.interpolator = DecelerateInterpolator()
            it.duration = 1250
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    binding?.tmpAnimatedImage?.gone()

                    if (toolbarType.equals(TOOLBAR_VARIANT_NAVIGATION, true)) {
                        binding?.navToolbar?.triggerAnimatedVectorDrawableAnimation(IconList.ID_WISHLIST)
                    } else {
                        toolbar.animateWishlistIcon()
                    }

                    showToastMessageGreen(message)
                    dPresenter.processGetWishlistData()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            it.start()
        }
    }

    private fun refreshWishlistAfterItemRemoveAndMoveToWishlist() {
        dPresenter.processGetWishlistData()
    }

    private fun removeLocalCartItem(updateListResult: Pair<ArrayList<Int>, ArrayList<Int>>, forceExpandCollapsedUnavailableItems: Boolean) {
        updateListResult.first.forEach {
            onNeedToRemoveViewItem(it)
        }
        updateListResult.second.forEach {
            onNeedToUpdateViewItem(it)
        }

        // If action is on unavailable item, do collapse unavailable items if previously forced to expand (without user tap expand)
        if (cartAdapter.allDisabledCartItemData.size > 1) {
            if (forceExpandCollapsedUnavailableItems) {
                collapseOrExpandDisabledItem()
            }
        } else {
            cartAdapter.removeAccordionDisabledItem()
        }

        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList)
        notifyBottomCartParent()
    }

    private fun onNeedToRemoveViewItem(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        if (binding?.rvCart?.isComputingLayout == true) {
            binding?.rvCart?.post { cartAdapter.notifyItemRemoved(position) }
        } else {
            cartAdapter.notifyItemRemoved(position)
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

    private fun refreshCartWithSwipeToRefresh() {
        refreshHandler?.isRefreshing = true
        resetRecentViewList()
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData(getCartId())
        } else {
            dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
        }
    }

    private fun refreshCartWithProgressDialog() {
        resetRecentViewList()
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData(getCartId())
        } else {
            dPresenter.processInitialGetCartData(getCartId(), false, false)
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
        return if (!TextUtils.isEmpty(arguments?.getString(CartActivity.EXTRA_CART_ID))) {
            arguments?.getString(CartActivity.EXTRA_CART_ID) ?: "0"
        } else "0"
    }

    private fun getAtcProductId(): Long {
        return arguments?.getLong(CartActivity.EXTRA_PRODUCT_ID) ?: 0L
    }

    private fun isAtcExternalFlow(): Boolean {
        return getAtcProductId() != 0L
    }

    override fun renderRecentView(recommendationWidget: RecommendationWidget?) {
        var cartRecentViewItemHolderDataList: MutableList<CartRecentViewItemHolderData> = ArrayList()
        if (recommendationWidget != null) {
            cartRecentViewItemHolderDataList = recentViewMapper.convertToViewHolderModelList(recommendationWidget)
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

    override fun renderWishlist(wishlists: List<Wishlist>?, forceReload: Boolean) {
        var cartWishlistItemHolderDataList: MutableList<CartWishlistItemHolderData> = ArrayList()
        if (this.wishLists != null) {
            if (forceReload && wishlists != null) {
                cartWishlistItemHolderDataList = wishlistMapper.convertToViewHolderModelList(wishlists)
            } else {
                cartWishlistItemHolderDataList.addAll(this.wishLists!!)
            }
        } else if (wishlists != null) {
            cartWishlistItemHolderDataList = wishlistMapper.convertToViewHolderModelList(wishlists)
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

        if (recommendationWidget != null) {
            // Render from API
            val recommendationItems = recommendationWidget.recommendationItemList
            for (recommendationItem in recommendationItems) {
                val cartRecommendationItemHolderData = CartRecommendationItemHolderData(false, recommendationItem)
                cartRecommendationItemHolderDataList.add(cartRecommendationItemHolderData)
            }
        } else {
            // Render from Cache
            if (recommendationList?.size != 0) {
                recommendationList?.forEach {
                    it.hasSentImpressionAnalytics = false
                }
                cartRecommendationItemHolderDataList.addAll(this.recommendationList!!)
            }
        }

        var cartSectionHeaderHolderData: CartSectionHeaderHolderData? = null
        if (recommendationPage == 1) {
            if (recommendationSectionHeader != null) {
                cartSectionHeaderHolderData = recommendationSectionHeader
            } else {
                cartSectionHeaderHolderData = CartSectionHeaderHolderData()
                if (!TextUtils.isEmpty(recommendationWidget?.title)) {
                    cartSectionHeaderHolderData.title = recommendationWidget?.title ?: ""
                } else {
                    cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recommendation)
                }
                recommendationSectionHeader = cartSectionHeaderHolderData
            }
        }

        if (cartRecommendationItemHolderDataList.size > 0) {
            cartAdapter.addCartRecommendationData(cartSectionHeaderHolderData, cartRecommendationItemHolderDataList)
            if (recommendationList.isNullOrEmpty()) {
                recommendationList = cartRecommendationItemHolderDataList
            }
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

    override fun triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataResponseModel: AddToCartDataModel, productModel: Any) {
        var stringObjectMap: Map<String, Any>? = null
        var eventCategory = ""
        var eventAction = ""
        var eventLabel = ""
        when (productModel) {
            is CartWishlistItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_WISHLIST
                eventLabel = ""
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
            }
            is CartRecentViewItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_RECENT_VIEW_PAGE
                eventLabel = ""
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
            }
            is CartRecommendationItemHolderData -> {
                eventCategory = ConstantTransactionAnalytics.EventCategory.CART
                eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART
                eventLabel = ""
                stringObjectMap = dPresenter.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
            }
        }

        stringObjectMap?.let {
            cartPageAnalytics.sendEnhancedECommerceAddToCart(stringObjectMap, eventCategory, eventAction, eventLabel)
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
            if (cartItemData.selectedUnavailableActionId == ACTION_CHECKOUTBROWSER) {
                cartPageAnalytics.eventClickHapusButtonOnProductContainTobacco()
                break
            }
        }
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(
                dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
        )

        if (allDisabledCartItemDataList.isNotEmpty()) {
            val dialog = getMultipleDisabledItemsDialogDeleteConfirmation(allDisabledCartItemDataList.size)
            dialog?.setPrimaryCTAClickListener {
                dPresenter.processDeleteCartItem(allCartItemDataList, allDisabledCartItemDataList, false, false)
                cartPageAnalytics.eventClickDeleteAllUnavailableProduct(userSession.userId)
                cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(
                        dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
                )
                dialog.dismiss()
                Unit
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
                Unit
            }
            dialog?.show()
        }
    }

    override fun onDeleteDisabledItem(data: DisabledCartItemHolderData) {
        data.data?.let {
            cartPageAnalytics.eventClickDeleteProductOnUnavailableSection(userSession.userId, data.productId, data.errorType)
            if (data.data?.selectedUnavailableActionId ?: 0 == ACTION_CHECKOUTBROWSER) {
                cartPageAnalytics.eventClickTrashIconButtonOnProductContainTobacco()
            } else {
                cartPageAnalytics.eventClickAtcCartClickTrashBin()
            }
            val cartItemDatas = listOf(it)
            val allCartItemDataList = cartAdapter.allCartItemData

            // If unavailable item > 1 and state is collapsed, then expand first
            var forceExpand = false
            if (cartAdapter.allDisabledCartItemData.size > 1 && accordionCollapseState) {
                collapseOrExpandDisabledItem()
                forceExpand = true
            }

            dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, false, false, forceExpand)
            cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(
                    dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
            )
        }
    }

    override fun onTobaccoLiteUrlClicked(url: String, data: DisabledCartItemHolderData, actionData: ActionData) {
        cartPageAnalytics.eventClickCheckoutMelaluiBrowserOnUnavailableSection(userSession.userId, data.productId, data.errorType)
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
            promoCheckoutBtnCart.title = getString(R.string.promo_checkout_inactive_label)
            promoCheckoutBtnCart.desc = getString(R.string.promo_checkout_inactive_desc)
            promoCheckoutBtnCart.setOnClickListener {
                renderPromoCheckoutLoading()
                dPresenter.doValidateUse(generateParamValidateUsePromoRevamp(false, -1, -1, true))
            }
        }
    }

    override fun showPromoCheckoutStickyButtonLoading() {
        renderPromoCheckoutLoading()
    }

    override fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel) {
        val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
        renderPromoCheckoutButton(lastApplyUiModel)
        renderPromoSummaryFromStickyPromo(lastApplyUiModel)
        if (promoUiModel.globalSuccess) {
            setLastApplyDataToShopGroup(lastApplyUiModel)
        }
    }

    override fun updateListRedPromos(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        cartListData?.lastApplyShopGroupSimplifiedData?.listRedPromos = mapCreateListRedPromos(validateUsePromoRevampUiModel)
    }

    private fun mapCreateListRedPromos(validateUseUiModel: ValidateUsePromoRevampUiModel): List<String> {
        val listRedPromos = arrayListOf<String>()
        if (validateUseUiModel.promoUiModel.messageUiModel.state.equals(STATE_RED, true)) {
            validateUseUiModel.promoUiModel.codes.forEach {
                listRedPromos.add(it)
            }
        }
        validateUseUiModel.promoUiModel.voucherOrderUiModels.forEach {
            if (it?.messageUiModel?.state.equals(STATE_RED, true)) {
                it?.code?.let { it1 -> listRedPromos.add(it1) }
            }
        }
        return listRedPromos
    }

    override fun onCartItemQuantityChangedThenHitUpdateCartAndValidateUse() {
        validateGoToCheckout()
        val params = generateParamValidateUsePromoRevamp(false, -1, -1, true)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        }
    }

    private fun validateGoToCheckout() {
        cartAdapter.checkForShipmentForm()
    }

    override fun navigateToPromoRecommendation() {
        routeToPromoCheckoutMarketplacePage()
    }

    override fun generateGeneralParamValidateUse(): ValidateUsePromoRequest {
        return generateParamValidateUsePromoRevamp(false, -1, -1, true)
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
                    this::class.java.simpleName, url, productId, productName, imageUrl)
        }
    }

    override fun sendATCTrackingURL(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl
        val url = "${recommendationItem.clickUrl}&click_source=ATC_direct_click"

        activity?.let { TopAdsUrlHitter(CartFragment::class.qualifiedName).hitClickUrl(it, url, productId, productName, imageUrl) }
    }

    override fun onAccordionClicked(data: DisabledAccordionHolderData, buttonWording: String) {
        cartPageAnalytics.eventClickAccordionButtonOnUnavailableProduct(userSession.userId, buttonWording)
        data.isCollapsed = !data.isCollapsed
        accordionCollapseState = data.isCollapsed
        collapseOrExpandDisabledItem(data)
    }

    private fun collapseOrExpandDisabledItem() {
        cartAdapter.getDisabledAccordionHolderData()?.let {
            it.isCollapsed = !it.isCollapsed
            accordionCollapseState = it.isCollapsed
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

    override fun onEditNoteDone(parentPosition: Int) {
        onNeedToUpdateViewItem(parentPosition)
    }

    override fun onCashbackUpdated(amount: Int) {
        cartListData?.shoppingSummaryData?.sellerCashbackValue = amount
    }

    override fun onCartItemShowRemainingQty(productId: String?) {
        cartPageAnalytics.eventViewRemainingStockInfo(userSession.userId, productId ?: "")
    }

    override fun onCartItemShowInformationLabel(productId: String?, informationLabel: String?) {
        cartPageAnalytics.eventViewInformationLabelInProductCard(userSession.userId, productId
                ?: "", informationLabel ?: "")
    }

    override fun reCollapseExpandedDeletedUnavailableItems() {
        collapseOrExpandDisabledItem()
    }

    private fun getMultipleDisabledItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_disabled_multiple_item, count))
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

}