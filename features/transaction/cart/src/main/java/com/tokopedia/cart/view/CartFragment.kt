package com.tokopedia.cart.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.recentview.RecentView
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.cart.view.adapter.CartAdapter
import com.tokopedia.cart.view.adapter.CartItemAdapter
import com.tokopedia.cart.view.compoundview.ToolbarRemoveView
import com.tokopedia.cart.view.compoundview.ToolbarRemoveWithBackView
import com.tokopedia.cart.view.di.DaggerCartComponent
import com.tokopedia.cart.view.mapper.RecentViewMapper
import com.tokopedia.cart.view.mapper.WishlistMapper
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.constant.CartConstant.ACTION_OK
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_EMPTY_DEFAULT_IMG_URL
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_EMPTY_WITH_PROMO_IMG_URL
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.constant.CartConstant.PARAM_CART
import com.tokopedia.purchase_platform.common.constant.CartConstant.PARAM_DEFAULT
import com.tokopedia.purchase_platform.common.constant.CartConstant.STATE_RED
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.RESULT_CODE_COUPON_STATE_CHANGED
import com.tokopedia.purchase_platform.common.data.api.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.data.model.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.data.model.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.data.model.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.insurance.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceItemActionListener
import com.tokopedia.purchase_platform.common.feature.promo.data.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementActionListener
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.coroutines.*
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartFragment : BaseCheckoutFragment(), ICartListView, ActionListener, CartItemAdapter.ActionListener,
        RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, ToolbarRemoveView.ToolbarCartListener,
        InsuranceItemActionListener, TickerAnnouncementActionListener {

    lateinit var appBarLayout: AppBarLayout
    lateinit var cartRecyclerView: RecyclerView
    lateinit var btnToShipment: UnifyButton
    lateinit var tvTotalPrice: TextView
    lateinit var rlContent: RelativeLayout
    lateinit var cbSelectAll: CheckBox
    lateinit var llHeader: LinearLayout
    lateinit var btnRemove: Typography
    lateinit var cardHeader: CardView
    lateinit var bottomLayout: LinearLayout
    lateinit var bottomLayoutShadow: View
    lateinit var llNetworkErrorView: LinearLayout
    lateinit var llCartContainer: LinearLayout
    lateinit var llPromoCheckout: LinearLayout
    lateinit var promoCheckoutBtn: ButtonPromoCheckoutView

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
    private var recommendationList: List<CartRecommendationItemHolderData>? = null
    private var recommendationSectionHeader: CartSectionHeaderHolderData? = null
    private var recommendationWishlistActionListener: WishListActionListener? = null
    private var cartAvailableWishlistActionListener: WishListActionListener? = null
    private var cartUnavailableWishlistActionListener: WishListActionListener? = null
    private var lastSeenWishlistActionListener: WishListActionListener? = null
    private var wishlistsWishlistActionListener: WishListActionListener? = null

    private var hasTriedToLoadWishList: Boolean = false
    private var hasTriedToLoadRecentViewList: Boolean = false
    private var shouldReloadRecentViewList: Boolean = false
    private var hasTriedToLoadRecommendation: Boolean = false
    private var isInsuranceEnabled = false
    private var isToolbarWithBackButton = true
    private var noAvailableItems = false
    private var listRedPromos: List<String> = emptyList()
    private var prevCbSelectAllIsSelected: Boolean = false
    private var cbChangeJob: Job? = null
    private var isButtonAnimating = false
    private var _animator: Animator? = null
    private val ANIMATION_TYPE = "translationY"
    private val ANIMATION_DURATION_IN_MILIS = 1000L
    private val TRANSLATION_LENGTH = 1800f

    companion object {

        private const val LOYALTY_ACTIVITY_REQUEST_CODE = 12345
        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

        private val SHOP_INDEX_PROMO_GLOBAL = -1

        private val HAS_ELEVATION = 12
        private val NO_ELEVATION = 0
        private val CART_TRACE = "mp_cart"
        private val CART_ALL_TRACE = "mp_cart_all"
        private val CART_PAGE = "cart"
        private val NAVIGATION_PDP = 64728
        private val NAVIGATION_PROMO = 7451
        private val NAVIGATION_SHIPMENT = 983
        private val ADVERTISINGID = "ADVERTISINGID"
        private val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
        val GO_TO_DETAIL = 2
        val GO_TO_LIST = 1

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

    private fun getDialogDeleteConfirmation(): DialogUnify? {
        activity?.apply {
            return DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_item))
                setDescription(getString(R.string.label_dialog_message_remove_cart_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
                setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            }
        }

        return null
    }

    private fun getDisabledItemDialogDeleteConfirmation(): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_disabled_item))
                setDescription(getString(R.string.label_dialog_message_remove_cart_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
                setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            }
        }

        return null
    }

    private fun getInsuranceDialogDeleteConfirmation(): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_item_macro_insurance))
                setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item_with_insurance))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete_and_add_to_wishlist_macro_insurance))
                setSecondaryCTAText(getString(R.string.label_dialog_action_delete_macro_insurance))
            }
        }

        return null
    }

    private fun getMultipleItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.let {
            return DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_multiple_item, count))
                setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
                setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            }
        }

        return null
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

    private fun initRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        isInsuranceEnabled = remoteConfig.getBoolean(APP_ENABLE_INSURANCE_RECOMMENDATION, false)
    }

    private fun loadCachedData() {
        cartListData = saveInstanceCacheManager?.get<CartListData>(CartListData::class.java.simpleName, CartListData::class.java)
        wishLists = saveInstanceCacheManager?.get<List<CartWishlistItemHolderData>>(CartWishlistItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartWishlistItemHolderData>>() {}.type, null)
        recentViewList = saveInstanceCacheManager?.get<List<CartRecentViewItemHolderData>>(CartRecentViewItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecentViewItemHolderData>>() {}.type, null)
        recommendationList = saveInstanceCacheManager?.get<List<CartRecommendationItemHolderData>>(CartRecommendationItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecommendationItemHolderData>>() {}.type, null)
        recommendationSectionHeader = saveInstanceCacheManager?.get<CartSectionHeaderHolderData>(CartSectionHeaderHolderData::class.java.simpleName,
                object : TypeToken<CartSectionHeaderHolderData>() {}.type, null)
    }

    override fun onStop() {
        updateCartAfterDetached()

        if (FLAG_SHOULD_CLEAR_RECYCLERVIEW) {
            clearRecyclerView()
        }

        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        if (refreshHandler?.isRefreshing == false) {
            if (!::cartAdapter.isInitialized || (::cartAdapter.isInitialized && cartAdapter.itemCount == 0)) {
                dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
            }
        }
    }

    private fun updateCartAfterDetached() {
        val hasChanges = dPresenter.dataHasChanged()
        try {
            val cartItemDataList = getAllSelectedCartDataList()
            activity?.let {
                if (hasChanges && cartItemDataList?.isNotEmpty() == true && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                    dPresenter.processUpdateCartData(true)
                }
            }
        } catch (e: IllegalStateException) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        cartAdapter.unsubscribeSubscription()
        dPresenter.detachView()
        super.onDestroy()
    }

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

    override fun getOptionsMenuEnable(): Boolean {
        return true
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (show) {
                appBarLayout.elevation = HAS_ELEVATION.toFloat()
            } else {
                appBarLayout.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun initView(view: View) {
        setupToolbar(view)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        cartRecyclerView = view.findViewById(R.id.rv_cart)
        btnToShipment = view.findViewById(R.id.go_to_courier_page_button)
        tvTotalPrice = view.findViewById(R.id.tv_total_prices)
        rlContent = view.findViewById(R.id.rl_content)
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view)
        cardHeader = view.findViewById(R.id.card_header)
        bottomLayout = view.findViewById(R.id.bottom_layout)
        bottomLayoutShadow = view.findViewById(R.id.bottom_layout_shadow)
        cbSelectAll = view.findViewById(R.id.cb_select_all)
        llHeader = view.findViewById(R.id.ll_header)
        btnRemove = view.findViewById(R.id.btn_delete_all_cart)
        llCartContainer = view.findViewById(R.id.ll_cart_container)
        llPromoCheckout = view.findViewById(R.id.ll_promo_checkout)
        promoCheckoutBtn = view.findViewById(R.id.promo_checkout_btn_cart)

        activity?.let {
            refreshHandler = RefreshHandler(it, view.findViewById(R.id.swipe_refresh_layout), this)
            progressDialog = AlertDialog.Builder(it)
                    .setView(R.layout.purchase_platform_progress_dialog_view)
                    .setCancelable(false)
                    .create()
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // Remove default cardview margin on Kitkat or lower
            activity?.let {
                val pixel = Utils.convertDpToPixel(-6f, it)
                (cardHeader.layoutParams as ViewGroup.MarginLayoutParams).setMargins(pixel, pixel, pixel, pixel)
            }
        }

        initViewListener()
        setupRecyclerView()
    }

    private fun initViewListener() {
        btnToShipment.setOnClickListener { checkGoToShipment("") }
        cbSelectAll.setOnClickListener { onSelectAllClicked() }
        llHeader.setOnClickListener { onSelectAllClicked() }
        btnRemove.setOnClickListener {
            if (btnRemove.isVisible) {
                onToolbarRemoveAllCart()
            }
        }
        setCbSelectAllOnCheckedChangeListener()
    }

    private fun setCbSelectAllOnCheckedChangeListener() {
        var prevIsChecked = cartListData?.isAllSelected
        cbSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                cbChangeJob?.cancel()
                cbChangeJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)
                    if (isChecked == prevIsChecked) {
                        if (isChecked != cartListData?.isAllSelected) {
                            onSelectAllClicked()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        cartRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = cartAdapter
            addItemDecoration(cartItemDecoration)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    super.onDrawOver(c, parent, state)
                    val firstCartSectionHeaderPosition = cartAdapter.firstCartSectionHeaderPosition
                    if (firstCartSectionHeaderPosition > -1 && parent.layoutManager is GridLayoutManager) {
                        if ((parent.layoutManager as GridLayoutManager).findFirstVisibleItemPosition() >= firstCartSectionHeaderPosition) {
                            if (cardHeader.visibility != View.GONE && !noAvailableItems && bottomLayout.visibility == View.VISIBLE) {
                                cardHeader.gone()
                                llPromoCheckout.gone()
                                setToolbarShadowVisibility(true)
                            }
                        } else if (cardHeader.visibility != View.VISIBLE && !noAvailableItems && bottomLayout.visibility == View.VISIBLE) {
                            cardHeader.show()
                            llPromoCheckout.show()
                            setToolbarShadowVisibility(false)
                        }
                    }
                }
            })

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        _animator?.end()
                        ObjectAnimator.ofFloat(llPromoCheckout, ANIMATION_TYPE, 0f).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isButtonAnimating = false
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isButtonAnimating = true
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isButtonAnimating = false
                                }
                            })
                            if (!isButtonAnimating) {
                                start()
                            }
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(-1)) {
                        disableSwipeRefresh()
                    } else {
                        enableSwipeRefresh()
                    }
                    if (dy > 0) {
                        ObjectAnimator.ofFloat(llPromoCheckout, ANIMATION_TYPE, TRANSLATION_LENGTH).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isButtonAnimating = false
                                    _animator = null
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isButtonAnimating = true
                                    _animator = animation
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isButtonAnimating = false
                                    _animator = null

                                }
                            })
                            if (!isButtonAnimating) {
                                start()
                            }
                        }
                    } else if (dy < 0) {
                        ObjectAnimator.ofFloat(llPromoCheckout, ANIMATION_TYPE, 0f).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isButtonAnimating = false
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isButtonAnimating = true
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isButtonAnimating = false
                                }
                            })
                            if (!isButtonAnimating) {
                                start()
                            }
                        }
                    }
                }
            })

            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position != RecyclerView.NO_POSITION) {
                        if (position < cartAdapter.itemCount && cartAdapter.getItemViewType(position) == CartRecommendationViewHolder.LAYOUT) {
                            1
                        } else 2
                    } else 0
                }
            }

            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (hasLoadRecommendation) {
                        dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.currentPage, cartAdapter.allCartItemProductId)
                    }
                }
            }
            addOnScrollListener(endlessRecyclerViewScrollListener)
        }
    }

    private fun setupToolbar(view: View) {
        activity?.let {
            val args = arguments?.getString(CartFragment::class.java.simpleName)
            if (args?.isNotEmpty() == true) {
                isToolbarWithBackButton = false
            }

            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            val statusBarBackground = view.findViewById<View>(R.id.status_bar_bg)
            statusBarBackground.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)

            val toolbar: View?
            if (isToolbarWithBackButton) {
                toolbar = toolbarRemoveWithBackView()
                statusBarBackground.hide()
            } else {
                toolbar = toolbarRemoveView()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    statusBarBackground.visibility = View.INVISIBLE
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    statusBarBackground.show()
                } else {
                    statusBarBackground.hide()
                }
            }
            toolbar?.let {
                appbar.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
            }
        }
    }

    private fun toolbarRemoveWithBackView(): ToolbarRemoveWithBackView? {
        activity?.let {
            return ToolbarRemoveWithBackView(it).apply {
                navigateUp(it)
                setTitle(getString(R.string.cart))
            }
        }

        return null
    }

    private fun toolbarRemoveView(): ToolbarRemoveView? {
        activity?.let {
            return ToolbarRemoveView(it).apply {
                setTitle(getString(R.string.cart))
            }
        }

        return null
    }

    override fun onToolbarRemoveAllCart() {
        sendAnalyticsOnClickRemoveButtonHeader()
        val toBeDeletedCartItemDataList = getAllSelectedCartDataList()
        val allCartItemDataList = cartAdapter.allCartItemData
        if (toBeDeletedCartItemDataList?.isNotEmpty() == true) {
            val dialog = getMultipleItemsDialogDeleteConfirmation(toBeDeletedCartItemDataList.size)
            dialog?.setPrimaryCTAClickListener {
                if (toBeDeletedCartItemDataList.isNotEmpty()) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItemDataList, false, true)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateDeleteCartDataAnalytics(toBeDeletedCartItemDataList)
                    )
                }
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog?.show()
        } else {
            showToastMessageRed(getString(R.string.message_delete_empty_selection))
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
            sendAnalyticsOnButtonCheckoutClickedFailed()
            sendAnalyticsOnGoToShipmentFailed(message)
        }
    }

    override fun onSuccessClearRedPromosThenGoToCheckout() {
        goToCheckoutPage()
    }

    private fun goToCheckoutPage() {
        val insuranceCartShopsArrayList = cartAdapter.isInsuranceCartProductUnSelected

        if (insuranceCartShopsArrayList.isNotEmpty()) {
            deleteMacroInsurance(insuranceCartShopsArrayList, false)
        } else if (cartAdapter.isInsuranceSelected) {
            cartPageAnalytics.sendEventPurchaseInsurance(userSession.userId,
                    cartAdapter.selectedInsuranceProductId,
                    cartAdapter.selectedInsuranceProductTitle)
        }
        dPresenter.processUpdateCartData(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityBackgroundColor()
        activity?.let {
            setHasOptionsMenu(true)
            it.title = it.getString(R.string.title_activity_cart)
            if (savedInstanceState == null) {
                refreshHandler?.startRefresh()
            } else {
                if (cartListData != null) {
                    dPresenter.setCartListData(cartListData!!)
                    renderLoadGetCartDataFinish()
                    renderInitialGetCartListDataSuccess(cartListData)
                    stopCartPerformanceTrace()
                } else {
                    refreshHandler?.startRefresh()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickRemoveIconCartItem()
        val cartItemDatas = mutableListOf<CartItemData>()
        cartItemHolderData.cartItemData?.let {
            cartItemDatas.add(it)
        }
        val allCartItemDataList = cartAdapter.allCartItemData

        val dialog: DialogUnify?

        val macroInsurancePresent = cartAdapter.insuranceCartShops.isNotEmpty()
        val removeAllItem = allCartItemDataList.size == cartItemDatas.size
        val removeMacroInsurance = macroInsurancePresent && removeAllItem

        if (removeMacroInsurance) {
            dialog = getInsuranceDialogDeleteConfirmation()
            dialog?.setPrimaryCTAClickListener {
                if (cartItemDatas.isNotEmpty()) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, true, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                            dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
                    )
                }
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
                    )
                }
                dialog.dismiss()
            }

        } else {
            dialog = getDialogDeleteConfirmation()
            dialog?.setPrimaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
                    )
                }
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
        }

        dialog?.show()
    }

    override fun onCartItemQuantityPlusButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickButtonPlusCartItem()
        cartAdapter.increaseQuantity(position, parentPosition)
    }

    override fun onCartItemQuantityMinusButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickButtonMinusCartItem()
        cartAdapter.decreaseQuantity(position, parentPosition)
    }

    override fun onCartItemQuantityReseted(position: Int, parentPosition: Int, needRefreshItemView: Boolean) {
        cartAdapter.resetQuantity(position, parentPosition)
    }

    override fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickProductNameCartItem(cartItemHolderData.cartItemData?.originData?.productName
                ?: "")
        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, cartItemHolderData.cartItemData?.originData?.productId), NAVIGATION_PDP)
    }

    override fun onClickShopNow() {
        cartPageAnalytics.eventClickAtcCartClickBelanjaSekarangOnEmptyCart()
        RouteManager.route(activity, ApplinkConst.HOME)
    }

    override fun onShowAllItem(appLink: String) {
        RouteManager.route(activity, appLink)
    }

    private fun onErrorAddWishList(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter.notifyByProductId(productId, false)
        cartAdapter.notifyWishlist(productId, false)
        cartAdapter.notifyRecentView(productId, false)
        cartAdapter.notifyRecommendation(productId, false)
    }

    private fun onSuccessAddWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success))
        cartAdapter.notifyByProductId(productId, true)
        cartAdapter.notifyWishlist(productId, true)
        cartAdapter.notifyRecentView(productId, true)
        cartAdapter.notifyRecommendation(productId, true)
    }

    private fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter.notifyByProductId(productId, true)
        cartAdapter.notifyWishlist(productId, true)
        cartAdapter.notifyRecentView(productId, true)
        cartAdapter.notifyRecommendation(productId, true)
    }

    private fun onSuccessRemoveWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success))
        cartAdapter.notifyByProductId(productId, false)
        cartAdapter.notifyWishlist(productId, false)
        cartAdapter.notifyRecentView(productId, false)
        cartAdapter.notifyRecommendation(productId, false)
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

    private fun getCartAvailableWishlistActionListener(): WishListActionListener {
        if (cartAvailableWishlistActionListener == null) {
            cartAvailableWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics.eventAddWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics.eventRemoveWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return cartAvailableWishlistActionListener as WishListActionListener
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

    override fun onAddDisabledItemToWishlist(productId: String) {
        dPresenter.processAddToWishlist(productId, userSession.userId, getCartUnavailableWishlistActionListener())
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
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getWishlistsWishlistActionListener())
    }

    override fun onRemoveRecommendationFromWishlist(productId: String) {
        dPresenter.processRemoveFromWishlist(productId, userSession.userId, getRecommendationWishlistActionListener())
    }

    private fun onProductClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_INFO, productId)
            startActivityForResult(intent, NAVIGATION_PDP)
        }
    }

    override fun onWishlistProductClicked(productId: String) {
        var position = 0

        wishLists?.let {
            for (wishlist in wishLists as List<CartWishlistItemHolderData>) {
                if (wishlist.id.equals(productId, ignoreCase = true)) {
                    if (FLAG_IS_CART_EMPTY) {
                        sendAnalyticsOnClickProductWishlistOnEmptyCart(position.toString(),
                                dPresenter.generateWishlistProductClickEmptyCartDataLayer(wishlist, position))
                    } else {
                        sendAnalyticsOnClickProductWishlistOnCartList(position.toString(),
                                dPresenter.generateWishlistProductClickDataLayer(wishlist, position))
                    }
                }
                position++
            }

            onProductClicked(productId)
        }
    }

    override fun onRecentViewProductClicked(productId: String) {
        var position = 0

        for (recentView in recentViewList as List<CartRecentViewItemHolderData>) {
            if (recentView.id.equals(productId, ignoreCase = true)) {
                if (FLAG_IS_CART_EMPTY) {
                    sendAnalyticsOnClickProductRecentViewOnEmptyCart(position.toString(),
                            dPresenter.generateRecentViewProductClickEmptyCartDataLayer(recentView, position))
                } else {
                    sendAnalyticsOnClickProductRecentViewOnCartList(position.toString(),
                            dPresenter.generateRecentViewProductClickDataLayer(recentView, position))
                }
            }
            position++
        }

        onProductClicked(productId)
    }

    override fun onRecommendationProductClicked(productId: String, topAds: Boolean, clickUrl: String) {
        var index = 1
        var recommendationItemClick: RecommendationItem? = null
        for ((recommendationItem) in recommendationList as List<CartRecommendationItemHolderData>) {
            if (recommendationItem.productId.toString().equals(productId, ignoreCase = true)) {
                recommendationItemClick = recommendationItem
                break
            }
            index++
        }

        recommendationItemClick?.let {
            sendAnalyticsOnClickProductRecommendation(index.toString(),
                    dPresenter.generateRecommendationDataOnClickAnalytics(it, FLAG_IS_CART_EMPTY, index))
        }

        when {
            topAds -> {
                ImpresionTask().execute(clickUrl)
            }
        }
        onProductClicked(productId)
    }

    override fun onRecommendationProductImpression(topAds: Boolean, trackingImageUrl: String) {
        when {
            topAds -> {
                ImpresionTask().execute(trackingImageUrl)
            }
        }
    }

    override fun onButtonAddToCartClicked(productModel: Any) {
        dPresenter.processAddToCart(productModel)
    }

    override fun onShowTickerOutOfStock(productId: String) {
        cartPageAnalytics.eventViewTickerOutOfStock(productId)
    }

    override fun onSimilarProductUrlClicked(similarProductUrl: String) {
        RouteManager.route(context, similarProductUrl)
        cartPageAnalytics.eventClickMoreLikeThis()
    }

    override fun onSelectAllClicked() {
        val checked = dPresenter.getCartListData()?.isAllSelected == false
        if (checked) {
            sendAnalyticsOnButtonSelectAllChecked()
        } else {
            sendAnalyticsOnButtonSelectAllUnchecked()
        }
        dPresenter.getCartListData()?.isAllSelected = checked
        cbSelectAll.isChecked = checked
        cartAdapter.setAllShopSelected(checked)
        dPresenter.setAllInsuranceProductsChecked(cartAdapter.insuranceCartShops, checked)
        cartAdapter.notifyDataSetChanged()
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
    }

    override fun onSeeErrorProductsClicked() {
        cartRecyclerView.layoutManager?.let {
            val linearSmoothScroller = object : LinearSmoothScroller(cartRecyclerView.context) {
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

    override fun onCartShopNameClicked(cartShopHolderData: CartShopHolderData) {
        sendAnalyticsOnClickShopCartItem(cartShopHolderData.shopGroupAvailableData.shopId
                ?: "", cartShopHolderData.shopGroupAvailableData.shopName ?: "")

        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SHOP, cartShopHolderData.shopGroupAvailableData.shopId)
            it.startActivity(intent)
        }
    }

    override fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean) {
        dPresenter.setHasPerformChecklistChange(true)
        cartAdapter.setShopSelected(itemPosition, checked)
        cartAdapter.notifyDataSetChanged()
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        cartAdapter.checkForShipmentForm()
    }

    override fun onCartDataEnableToCheckout() {
        if (isAdded) {
            btnToShipment.setOnClickListener { checkGoToShipment("") }
        }
    }

    override fun onCartDataDisableToCheckout() {
        if (isAdded) {
            btnToShipment.setOnClickListener { checkGoToShipment(getString(R.string.message_checkout_empty_selection)) }
        }
    }

    override fun onCartItemAfterErrorChecked() {
        cartAdapter.checkForShipmentForm()
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        sendAnalyticsOnClickQuantityCartItemInput(qty)
    }

    override fun onCartItemLabelInputRemarkClicked() {
        sendAnalyticsOnClickCreateNoteCartItem()
    }

    override fun onCartItemCheckChanged(position: Int, parentPosition: Int, checked: Boolean): Boolean {
        dPresenter.setHasPerformChecklistChange(true)
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)

        cartAdapter.checkForShipmentForm()
        val isSelected = cartAdapter.setItemSelected(position, parentPosition, checked)
        val params = generateParamValidateUsePromoRevamp(checked, parentPosition, position, false)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
        return isSelected
    }

    private fun updatePromoCheckoutManualIfNoSelected(listPromoApplied: List<String>) {
        if (cartAdapter.selectedCartShopHolderData.isEmpty()) {
            renderPromoCheckoutButtonNoItemIsSelected()
        } else {
            renderPromoCheckoutButtonActiveDefault(listPromoApplied)
        }
    }

    override fun onWishlistCheckChanged(productId: String, isChecked: Boolean) {
        if (isChecked) {
            dPresenter.processAddToWishlist(productId, userSession.userId, getCartAvailableWishlistActionListener())
        } else {
            dPresenter.processRemoveFromWishlist(productId, userSession.userId, getCartAvailableWishlistActionListener())
        }
    }

    override fun onNeedToRefreshSingleShop(parentPosition: Int) {
        cartAdapter.notifyItemChanged(parentPosition)
    }

    override fun onNeedToRefreshMultipleShop() {
        cartAdapter.notifyDataSetChanged()
    }

    override fun onNeedToRecalculate() {
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
    }

    override fun onCartItemShowTickerPriceDecrease(productId: String) {
        cartPageAnalytics.eventViewTickerPriceDecrease(productId)
    }

    override fun onCartItemShowTickerStockDecreaseAndAlreadyAtcByOtherUser(productId: String) {
        cartPageAnalytics.eventViewTickerStockDecreaseAndAlreadyAtcByOtherUser(productId)
    }

    override fun onCartItemShowTickerOutOfStock(productId: String) {
        cartPageAnalytics.eventViewTickerOutOfStock(productId)
    }

    override fun onCartItemSimilarProductUrlClicked(similarProductUrl: String) {
        RouteManager.route(context, similarProductUrl)
        cartPageAnalytics.eventClickMoreLikeThis()
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
                llCartContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.checkout_module_color_background))
            }

            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, R.color.checkout_module_color_background))
        }
    }

    override fun renderInitialGetCartListDataSuccess(cartListData: CartListData?) {
        cartListData?.let {
            sendAnalyticsScreenName(screenName)

            endlessRecyclerViewScrollListener.resetState()
            refreshHandler?.finishRefresh()
            this.cartListData = it
            cartAdapter.resetData()

            renderTickerAnnouncement(it)

            if (it.shopGroupAvailableDataList.isEmpty() && it.shopGroupWithErrorDataList.isEmpty()) {
                renderCartEmpty(it)
            } else {
                renderCartNotEmpty(it)
            }

            if (recentViewList == null || shouldReloadRecentViewList) {
                dPresenter.processGetRecentViewData(cartAdapter.allCartItemProductId)
            } else {
                renderRecentView(null)
            }

            if (wishLists == null) {
                dPresenter.processGetWishlistData()
            } else {
                renderWishlist(null)
            }

            if (recommendationList == null) {
                dPresenter.processGetRecommendationData(endlessRecyclerViewScrollListener.currentPage, cartAdapter.allCartItemProductId)
            } else {
                renderRecommendation(null)
            }

            if (dPresenter.isLastApplyValid()) {
                // Render promo from last apply
                cartListData.lastApplyShopGroupSimplifiedData?.let { lastApplyData ->
                    // show toaster if any promo applied has been changed
                    if (lastApplyData.additionalInfo.errorDetail.message.isNotEmpty()) {
                        showToaster(lastApplyData.additionalInfo.errorDetail.message, isShowOk = false)
                        PromoRevampAnalytics.eventCartViewPromoMessage(lastApplyData.additionalInfo.errorDetail.message)
                    }
                    renderPromoCheckout(lastApplyData)
                }

                // Render promo from last validate use from cart page (check / uncheck result) if any
                dPresenter.getUpdateCartAndValidateUseLastResponse()?.promoUiModel?.let {
                    val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
                    renderPromoCheckout(lastApplyUiModel)
                }
            } else {
                // Render promo from last validate use from promo page
                dPresenter.getValidateUseLastResponse()?.promoUiModel?.let {
                    val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
                    renderPromoCheckout(lastApplyUiModel)
                }

                // Render promo from last validate use from cart page (check / uncheck result) if any
                dPresenter.getUpdateCartAndValidateUseLastResponse()?.promoUiModel?.let {
                    val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(it)
                    renderPromoCheckout(lastApplyUiModel)
                }
            }
        }
    }

    private fun renderCartNotEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = false
        cartAdapter.removeCartEmptyData()

        renderTickerError(cartListData)
        renderCartAvailable(cartListData)
        renderCartNotAvailable(cartListData)
        loadMacroInsurance(cartListData)

        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        cbSelectAll.isChecked = cartListData.isAllSelected

        cartAdapter.checkForShipmentForm()

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = cartAdapter.allCartItemData
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
                dPresenter.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_0)
        )

        setToolbarShadowVisibility(cartListData.shopGroupAvailableDataList.isEmpty())
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
        sendAnalyticsOnDataCartIsEmpty()
        showEmptyCartContainer()
        notifyBottomCartParent()

        setToolbarShadowVisibility(true)
        cartAdapter.notifyDataSetChanged()

        setActivityBackgroundColor()
    }

    private fun renderTickerAnnouncement(cartListData: CartListData) {
        val tickerData = cartListData.tickerData
        if (tickerData?.isValid(CART_PAGE) == true) {
            cartAdapter.addCartTicker(TickerAnnouncementHolderData(tickerData.id.toString(), tickerData.message))
        }
    }

    private fun renderPromoCheckout(lastApplyUiModel: LastApplyUiModel) {
        doRenderPromoCheckoutButton(lastApplyUiModel)
    }

    override fun renderPromoCheckoutButtonActiveDefault(listPromoApplied: List<String>) {
        promoCheckoutBtn.state = ButtonPromoCheckoutView.State.ACTIVE
        promoCheckoutBtn.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        promoCheckoutBtn.title = getString(R.string.promo_funnel_label)
        promoCheckoutBtn.desc = ""
        promoCheckoutBtn.setOnClickListener {
            dPresenter.doUpdateCartForPromo()
            // analytics
            PromoRevampAnalytics.eventCartClickPromoSection(listPromoApplied, false)
        }
    }

    private fun renderPromoCheckoutButtonNoItemIsSelected() {
        promoCheckoutBtn.state = ButtonPromoCheckoutView.State.ACTIVE
        promoCheckoutBtn.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        promoCheckoutBtn.title = getString(R.string.promo_funnel_label)
        promoCheckoutBtn.desc = getString(R.string.promo_desc_no_selected_item)
        promoCheckoutBtn.setOnClickListener {
            showToaster(getString(R.string.promo_choose_item_cart), isShowOk = false)
            PromoRevampAnalytics.eventCartViewPromoMessage(getString(R.string.promo_choose_item_cart))
        }
    }

    private fun doRenderPromoCheckoutButton(lastApplyData: LastApplyUiModel) {
        val isApplied: Boolean

        promoCheckoutBtn.state = ButtonPromoCheckoutView.State.ACTIVE
        promoCheckoutBtn.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM

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
            promoCheckoutBtn.desc = lastApplyData.additionalInfo.messageInfo.detail
        } else {
            isApplied = false

            if (cartAdapter.selectedCartItemData.isEmpty()) {
                promoCheckoutBtn.desc = getString(R.string.promo_desc_no_selected_item)
            } else {
                promoCheckoutBtn.desc = ""
            }
        }

        promoCheckoutBtn.title = title
        promoCheckoutBtn.setOnClickListener {
            if (cartAdapter.selectedCartItemData.isEmpty()) {
                showToaster(getString(R.string.promo_choose_item_cart), isShowOk = false)
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
    }

    private fun setLastApplyDataToShopGroup(lastApplyData: LastApplyUiModel) {
        cartListData?.lastApplyShopGroupSimplifiedData = lastApplyData
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
        promoCheckoutBtn.state = ButtonPromoCheckoutView.State.LOADING
        promoCheckoutBtn.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        promoCheckoutBtn.setOnClickListener { }
    }

    private fun isNeedHitUpdateCartAndValidateUse(params: ValidateUsePromoRequest): Boolean {
        var isPromoApplied = false
        var allPromoApplied = arrayListOf<String>()
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
        var allPromoApplied = arrayListOf<String>()
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
                    if (i == (parentPosition - 2)) {
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
                            doAddtoOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
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
                            doAddtoOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
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
                        if (cartShop.shopGroupAvailableData.cartString.equals(shopGroup.cartString)) {
                            shopGroup.promoCodes?.forEach {
                                listPromoCodes.add(it)
                            }
                        }
                    }
                } else {
                    cartListData?.lastApplyShopGroupSimplifiedData?.voucherOrders?.forEach { lastApplyVoucherOrders ->
                        cartShop.shopGroupAvailableData.cartString?.let { cartString ->
                            if (cartString.equals(lastApplyVoucherOrders.uniqueId, true)) {
                                listPromoCodes.add(lastApplyVoucherOrders.code)
                            }
                        }
                    }
                }

                var countItemList: Int
                cartShop.shopGroupAvailableData.cartItemDataList?.let { listCartItemHolderData ->
                    countItemList = listCartItemHolderData.size
                    for (j in 0 until countItemList) {
                        if (listCartItemHolderData[j].isSelected) {
                            doAddToListProducts(cartShop.shopGroupAvailableData, j, listProductDetail)
                        }
                    }
                    if (listProductDetail.isNotEmpty()) {
                        doAddtoOrderListRequest(cartShop.shopGroupAvailableData, listProductDetail, listPromoCodes, listOrder)
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
                        doAddtoOrderListRequest(cartItemHolderData, listProductDetail, listPromoCodes, listOrder)
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
                cartType = PARAM_DEFAULT,
                orders = listOrder)
    }

    private fun doAddToListProducts(cartItemHolderData: ShopGroupAvailableData, j: Int, listProductDetail: ArrayList<ProductDetailsItem>) {
        cartItemHolderData.cartItemDataList?.get(j)?.let { cartItemData ->
            if (cartItemData.isSelected) {
                val productDetail = cartItemData.cartItemData?.originData?.productId?.toInt()?.let {
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

    private fun doAddtoOrderListRequest(cartItemHolderData: ShopGroupAvailableData,
                                        listProductDetail: ArrayList<ProductDetailsItem>,
                                        listPromoCodes: ArrayList<String>,
                                        listOrder: ArrayList<OrdersItem>) {
        cartItemHolderData.shopId?.toLong()?.let { shopId ->
            cartItemHolderData.cartString?.let { cartString ->
                val order = OrdersItem(
                        shopId = shopId.toInt(),
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
        if (cartListData.isError) {
            val cartItemTickerErrorHolderData = CartItemTickerErrorHolderData()
            cartItemTickerErrorHolderData.cartTickerErrorData = cartListData.cartTickerErrorData
            cartAdapter.addCartTickerError(cartItemTickerErrorHolderData)
        }
    }

    private fun renderCartAvailable(cartListData: CartListData) {
        cartAdapter.addAvailableDataList(cartListData.shopGroupAvailableDataList)
    }

    private fun renderCartNotAvailable(cartListData: CartListData) {
        if (cartListData.shopGroupWithErrorDataList.isNotEmpty()) {
            cartAdapter.addNotAvailableHeader(
                    viewHolderDataMapper.mapDisabledItemHeaderHolderData(cartListData.cartTickerErrorData?.errorCount
                            ?: 0)
            )
            for (shopGroupWithErrorData in cartListData.shopGroupWithErrorDataList) {
                val cartItemHolderDataList = shopGroupWithErrorData.cartItemHolderDataList
                if (cartItemHolderDataList.isNotEmpty()) {
                    cartAdapter.addNotAvailableShop(viewHolderDataMapper.mapDisabledShopHolderData(shopGroupWithErrorData))
                    for ((index, value) in cartItemHolderDataList.withIndex()) {
                        cartAdapter.addNotAvailableProduct(viewHolderDataMapper.mapDisabledItemHolderData(value, index != cartItemHolderDataList.size - 1))
                    }
                }
            }
        }
    }

    private fun renderCartEmptyDefault() {
        val cartEmptyHolderData = CartEmptyHolderData(
                title = getString(R.string.checkout_module_keranjang_belanja_kosong_new),
                desc = getString(R.string.checkout_empty_cart_sub_message_new),
                imgUrl = CART_EMPTY_DEFAULT_IMG_URL,
                btnText = getString(R.string.checkout_module_mulai_belanja)
        )
        cartAdapter.addCartEmptyData(cartEmptyHolderData)
    }

    private fun renderCartEmptyWithPromo(lastApplyData: LastApplyUiModel) {
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

        // analytics
        cartAdapter.addCartEmptyData(cartEmptyWithPromoHolderData)
        val listPromos = getAllPromosApplied(lastApplyData)
        PromoRevampAnalytics.eventCartEmptyPromoApplied(listPromos)
    }

    private fun loadMacroInsurance(cartListData: CartListData) {
        if (cartListData.shopGroupAvailableDataList.isNotEmpty() && isInsuranceEnabled) {
            dPresenter.getInsuranceTechCart()
        }
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

    private fun showErrorLayout(message: String) {
        activity?.let {
            enableSwipeRefresh()
            it.invalidateOptionsMenu()
            refreshHandler?.finishRefresh()
            showErrorContainer()
            setToolbarShadowVisibility(true)
            NetworkErrorHelper.showEmptyState(it, llNetworkErrorView, message) {
                llNetworkErrorView.gone()
                rlContent.show()
                refreshHandler?.isRefreshing = true
                cartAdapter.resetData()
                dPresenter.processInitialGetCartData(getCartId(), dPresenter.getCartListData() == null, false)
            }
        }
    }

    private fun showMainContainerLoadingInitData() {
        llNetworkErrorView.gone()
        rlContent.show()
        bottomLayout.gone()
        bottomLayoutShadow.gone()
        cardHeader.gone()
        llPromoCheckout.gone()
    }

    private fun showMainContainer() {
        llNetworkErrorView.gone()
        rlContent.show()
        bottomLayout.show()
        bottomLayoutShadow.show()
        cardHeader.show()
    }

    private fun showErrorContainer() {
        rlContent.gone()
        llNetworkErrorView.show()
        bottomLayout.gone()
        bottomLayoutShadow.gone()
        cardHeader.gone()
        llPromoCheckout.gone()
    }

    private fun showEmptyCartContainer() {
        llNetworkErrorView.gone()
        bottomLayout.gone()
        bottomLayoutShadow.gone()
        cardHeader.gone()
        llPromoCheckout.gone()
    }

    private fun showSnackbarRetry(message: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, message) { dPresenter.processInitialGetCartData(getCartId(), dPresenter.getCartListData() == null, false) }
                .showRetrySnackbar()
    }

    override fun renderErrorInitialGetCartListData(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (throwable !is CartResponseErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        if (cartAdapter.itemCount > 0) {
            showSnackbarRetry(errorMessage)
        } else {
            showErrorLayout(errorMessage)
        }
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
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
            startActivityForResult(intent, NAVIGATION_SHIPMENT)
        }
    }

    private fun clearRecyclerView() {
        cartAdapter.unsubscribeSubscription()
        cartRecyclerView.removeAllViews()
        cartRecyclerView.recycledViewPool.clear()
    }

    override fun renderErrorToShipmentForm(message: String) {
        sendAnalyticsOnButtonCheckoutClickedFailed()
        sendAnalyticsOnGoToShipmentFailed(message)
        showToastMessageRed(message)

        refreshHandler?.startRefresh()
    }

    override fun renderErrorToShipmentForm(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (throwable !is CartResponseErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        renderErrorToShipmentForm(errorMessage)
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

    override fun renderDetailInfoSubTotal(qty: String, subtotalPrice: String, selectAllItem: Boolean, unselectAllItem: Boolean, noAvailableItems: Boolean) {
        dPresenter.getCartListData()?.isAllSelected = selectAllItem
        if (cbSelectAll.isChecked != selectAllItem) {
            cbSelectAll.isChecked = selectAllItem
        }
        btnRemove.visibility = if (unselectAllItem) View.INVISIBLE else View.VISIBLE
        this.noAvailableItems = noAvailableItems
        if (noAvailableItems) {
            cardHeader.visibility = View.GONE
            llPromoCheckout.gone()
            cartAdapter.removeCartSelectAll()
        } else {
            cardHeader.visibility = View.VISIBLE
            llPromoCheckout.show()
            cartAdapter.addCartSelectAll()
        }
        tvTotalPrice.text = subtotalPrice
        btnToShipment.text = String.format(getString(R.string.cart_item_button_checkout_count_format), qty)
    }

    override fun updateCashback(cashback: Double) {
        cartAdapter.updateShipmentSellerCashback(cashback)
    }

    override fun showToastMessageRed(message: String) {
        var tmpMessage = message
        if (TextUtils.isEmpty(tmpMessage)) {
            tmpMessage = CART_ERROR_GLOBAL
        }

        if (view != null) {
            NetworkErrorHelper.showRedCloseSnackbar(view, tmpMessage)
        } else if (activity != null) {
            Toast.makeText(activity, tmpMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun showToastMessageRed(throwable: Throwable) {
        var errorMessage = throwable.message ?: ""
        if (throwable !is CartResponseErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        }

        showToastMessageRed(errorMessage)
    }

    override fun showToastMessageGreen(message: String) {
        if (view != null) {
            NetworkErrorHelper.showGreenCloseSnackbar(view, message)
        } else if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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

    override fun onDeleteCartDataSuccess(deletedCartIds: List<String>) {
        cartAdapter.removeCartItemById(deletedCartIds, context)
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        setToolbarShadowVisibility(cartAdapter.allAvailableCartItemData.isEmpty())
        notifyBottomCartParent()
    }

    override fun onRefresh(view: View?) {
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData(getCartId())
        } else {
            if (dPresenter.getCartListData()?.shopGroupAvailableDataList?.isNotEmpty() == true) {
                showMainContainer()
            }
            dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            NAVIGATION_SHIPMENT -> onResultFromRequestCodeCartShipment(resultCode, data)
            NAVIGATION_PDP -> {
                refreshHandler?.isRefreshing = true
                resetRecentViewList()
                dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
            }
            NAVIGATION_PROMO -> {
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
        }
    }

    private fun onResultFromRequestCodeCartShipment(resultCode: Int, data: Intent?) {
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false

        if (resultCode == PaymentConstant.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module))
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success))
            refreshHandler?.isRefreshing = true
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_FAILED) {
            showToastMessageRed(getString(R.string.default_request_error_unknown))
            sendAnalyticsScreenName(screenName)
            refreshHandler?.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            sendAnalyticsScreenName(screenName)
            refreshHandler?.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false)
            }
        } else if (resultCode == RESULT_CODE_COUPON_STATE_CHANGED) {
            refreshHandler?.isRefreshing = true
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        }
    }

    override fun sendAnalyticsOnClickBackArrow() {
        cartPageAnalytics.eventClickAtcCartClickArrowBack()
    }

    override fun sendAnalyticsOnClickRemoveButtonHeader() {
        cartPageAnalytics.eventClickAtcCartClickHapusOnTopRightCorner()
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromTrashBin(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickRemoveIconCartItem() {
        cartPageAnalytics.eventClickAtcCartClickTrashBin()
    }

    override fun sendAnalyticsOnClickButtonPlusCartItem() {
        cartPageAnalytics.eventClickAtcCartClickButtonPlus()
    }

    override fun sendAnalyticsOnClickButtonMinusCartItem() {
        cartPageAnalytics.eventClickAtcCartClickButtonMinus()
    }

    override fun sendAnalyticsOnClickProductNameCartItem(productName: String) {
        cartPageAnalytics.eventClickAtcCartClickProductName(productName)
    }

    override fun sendAnalyticsOnClickShopCartItem(shopId: String, shopName: String) {
        cartPageAnalytics.eventClickAtcCartClickShop(shopId, shopName)
    }

    override fun sendAnalyticsOnClickCancelPromoCodeAndCouponBanner() {
        cartPageAnalytics.eventClickAtcCartClickXOnBannerPromoCode()
    }

    override fun sendAnalyticsOnClickRemoveCartConstrainedProduct(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickQuantityCartItemInput(quantity: String) {
        cartPageAnalytics.eventClickAtcCartClickInputQuantity(quantity)
    }

    override fun sendAnalyticsOnClickCreateNoteCartItem() {
        cartPageAnalytics.eventClickAtcCartClickTulisCatatan()
    }

    override fun sendAnalyticsOnDataCartIsEmpty() {
        cartPageAnalytics.eventViewAtcCartImpressionCartEmpty()
    }

    override fun sendAnalyticsScreenName(screenName: String) {
        cartPageAnalytics.sendScreenName(activity, screenName)
    }

    override fun getScreenName(): String {
        return ConstantTransactionAnalytics.ScreenName.CART
    }

    override fun sendAnalyticsOnButtonCheckoutClickedFailed() {
        cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
    }

    override fun sendAnalyticsOnGoToShipmentFailed(errorMessage: String) {
        cartPageAnalytics.eventViewErrorWhenCheckout(errorMessage)
    }

    override fun sendAnalyticsOnButtonSelectAllChecked() {
        cartPageAnalytics.eventClickCheckoutCartClickPilihSemuaProdukChecklist()
    }

    override fun sendAnalyticsOnButtonSelectAllUnchecked() {
        cartPageAnalytics.eventClickCheckoutCartClickPilihSemuaProdukUnChecklist()
    }

    override fun sendAnalyticsOnViewPromoManualApply(type: String) {
        cartPageAnalytics.eventViewPromoManualApply(type)
    }

    override fun sendAnalyticsOnViewProductRecommendation(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceViewRecommendationOnCart(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecommendation(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceClickProductRecommendationOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewProductWishlist(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceProductViewWishList(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductWishlistOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductWishlistOnCartList(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceClickProductWishListOnCartList(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewProductRecentView(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceProductViewLastSeen(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecentViewOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecentViewOnCartList(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics.enhancedEcommerceClickProductLastSeenOnCartList(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewPromoAutoApply() {
        cartPageAnalytics.eventViewPromoAutoApply()
    }

    override fun notifyBottomCartParent() {
        if (activity is CartNotifyListener) {
            (activity as CartNotifyListener).onNotifyCart()
        }
    }

    override fun getInsuranceCartShopData(): ArrayList<InsuranceCartDigitalProduct>? {
        try {
            val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
            for (insuranceCartShops in cartAdapter.insuranceCartShops) {
                if (insuranceCartShops.shopItemsList.isNotEmpty()) {
                    for (insuranceCartShopItem in insuranceCartShops.shopItemsList) {
                        if (insuranceCartShopItem.digitalProductList.isNotEmpty()) {
                            for (insuranceCartDigitalProduct in insuranceCartShopItem.digitalProductList) {
                                insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct)
                            }
                        }
                    }
                }

            }
            return insuranceCartDigitalProductArrayList
        } catch (e: Exception) {
            return null
        }

    }

    override fun removeInsuranceProductItem(productId: List<Long>) {
        cartAdapter.removeInsuranceDataItem(productId)
    }

    override fun showMessageUpdateInsuranceProductSuccess() {
        val message = activity?.resources?.getString(R.string.update_insurance_data_success) ?: ""
        if (message.isNotBlank()) showToastMessageGreen(message)
    }

    override fun showMessageRemoveInsuranceProductSuccess() {
        val message = activity?.resources?.getString(R.string.remove_macro_insurance_success) ?: ""
        if (message.isNotBlank()) showToastMessageGreen(message)
    }

    override fun renderInsuranceCartData(insuranceCartResponse: InsuranceCartResponse?, isRecommendation: Boolean) {

        /*
         * render insurance cart data on ui, both micro and macro, if is_product_level == true,
         * then insurance product is of type micro insurance and should be tagged at product level,
         * for micro insurance product add insurance data in shopGroup list*/

        if (insuranceCartResponse?.cartShopsList?.isNotEmpty() == true) {
            for (insuranceCartShops in insuranceCartResponse.cartShopsList) {
                val shopId = insuranceCartShops.shopId
                for ((productId, digitalProductList) in insuranceCartShops.shopItemsList) {
                    for (insuranceCartDigitalProduct in digitalProductList) {
                        insuranceCartDigitalProduct.shopId = shopId.toString()
                        insuranceCartDigitalProduct.productId = productId.toString()
                        if (!insuranceCartDigitalProduct.isProductLevel) {
                            cartAdapter.addInsuranceDataList(insuranceCartShops, isRecommendation)
                        }
                    }
                }
            }
            cartAdapter.notifyDataSetChanged()
        }

    }

    // get newly added cart id if open cart after ATC on PDP
    override fun getCartId(): String {
        return if (!TextUtils.isEmpty(arguments?.getString(CartActivity.EXTRA_CART_ID))) {
            arguments?.getString(CartActivity.EXTRA_CART_ID) ?: "0"
        } else "0"
    }

    override fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops,
                                            updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>) {
        dPresenter.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList)
    }

    override fun sendEventDeleteInsurance(insuranceTitle: String) {
        cartPageAnalytics.sendEventDeleteInsurance(insuranceTitle)
    }

    override fun sendEventInsuranceImpression(title: String) {
        cartPageAnalytics.sendEventInsuranceImpression(title)
    }

    override fun sendEventInsuranceImpressionForShipment(title: String) {

    }

    override fun sendEventChangeInsuranceState(isChecked: Boolean, insuranceTitle: String) {
        cartPageAnalytics.sendEventChangeInsuranceState(isChecked, insuranceTitle)
    }

    override fun deleteMacroInsurance(insuranceCartDigitalProductList: ArrayList<InsuranceCartDigitalProduct>, showConfirmationDialog: Boolean) {
        if (showConfirmationDialog) {
            activity?.let {
                val view = layoutInflater.inflate(R.layout.remove_insurance_product, null, false)
                val alertDialog = AlertDialog.Builder(it)
                        .setView(view)
                        .setCancelable(true)
                        .show()

                view.findViewById<View>(R.id.button_positive).setOnClickListener {
                    dPresenter.processDeleteCartInsurance(insuranceCartDigitalProductList, showConfirmationDialog)
                    alertDialog.dismiss()
                }

                view.findViewById<View>(R.id.button_negative).setOnClickListener { alertDialog.dismiss() }
            }
        } else {
            dPresenter.processDeleteCartInsurance(insuranceCartDigitalProductList, showConfirmationDialog)
        }
    }

    override fun onInsuranceSelectStateChanges() {
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
    }

    override fun renderRecentView(recentViewList: List<RecentView>?) {
        var cartRecentViewItemHolderDataList: MutableList<CartRecentViewItemHolderData> = ArrayList()
        if (recentViewList != null) {
            cartRecentViewItemHolderDataList = recentViewMapper.convertToViewHolderModelList(recentViewList)
        } else if (this.recentViewList != null) {
            cartRecentViewItemHolderDataList.addAll(this.recentViewList!!)
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recent_view)

        val cartRecentViewHolderData = CartRecentViewHolderData()
        cartRecentViewHolderData.recentViewList = cartRecentViewItemHolderDataList
        cartAdapter.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.recentViewList = cartRecentViewItemHolderDataList
        shouldReloadRecentViewList = false

        sendAnalyticsOnViewProductRecentView(
                dPresenter.generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList, FLAG_IS_CART_EMPTY)
        )
    }

    override fun renderWishlist(wishlists: List<Wishlist>?) {
        var cartWishlistItemHolderDataList: MutableList<CartWishlistItemHolderData> = ArrayList()
        if (this.wishLists != null) {
            cartWishlistItemHolderDataList.addAll(this.wishLists!!)
        } else if (wishlists != null) {
            cartWishlistItemHolderDataList = wishlistMapper.convertToViewHolderModelList(wishlists)
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_wishlist)
        cartSectionHeaderHolderData.showAllAppLink = ApplinkConst.WISHLIST

        val cartRecentViewHolderData = CartWishlistHolderData()
        cartRecentViewHolderData.wishList = cartWishlistItemHolderDataList
        cartAdapter.addCartWishlistData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.wishLists = cartWishlistItemHolderDataList

        sendAnalyticsOnViewProductWishlist(
                dPresenter.generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList, FLAG_IS_CART_EMPTY)
        )
    }

    override fun renderRecommendation(recommendationWidget: RecommendationWidget?) {
        val cartRecommendationItemHolderDataList = ArrayList<CartRecommendationItemHolderData>()

        if (recommendationWidget != null) {
            // Render from API
            val recommendationItems = recommendationWidget.recommendationItemList
            for (recommendationItem in recommendationItems) {
                val cartRecommendationItemHolderData = CartRecommendationItemHolderData(recommendationItem)
                cartRecommendationItemHolderDataList.add(cartRecommendationItemHolderData)
            }
        } else {
            // Render from Cache
            if (recommendationList?.size != 0) {
                cartRecommendationItemHolderDataList.addAll(this.recommendationList!!)
            }
        }

        var cartSectionHeaderHolderData: CartSectionHeaderHolderData? = null
        if ((endlessRecyclerViewScrollListener.currentPage == 0 && recommendationWidget == null) ||
                (recommendationWidget != null && endlessRecyclerViewScrollListener.currentPage == 1 && recommendationSectionHeader == null)) {
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
            recommendationList = cartRecommendationItemHolderDataList

            recommendationList?.let {
                sendAnalyticsOnViewProductRecommendation(
                        dPresenter.generateRecommendationImpressionDataAnalytics(it, FLAG_IS_CART_EMPTY)
                )
            }
        }
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

        val dialog = getMultipleDisabledItemsDialogDeleteConfirmation(allDisabledCartItemDataList.size)

        for (cartItemData in allDisabledCartItemDataList) {
            if (cartItemData.nicotineLiteMessageData != null) {
                cartPageAnalytics.eventClickHapusButtonOnProductContainTobacco()
                break
            }
        }
        sendAnalyticsOnClickRemoveCartConstrainedProduct(
                dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
        )

        dialog?.setPrimaryCTAClickListener {
            if (allDisabledCartItemDataList.size > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, allDisabledCartItemDataList, false, false)
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(
                        dPresenter.generateDeleteCartDataAnalytics(allDisabledCartItemDataList)
                )
            }
            dialog.dismiss()
        }
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun onDeleteDisabledItem(data: CartItemData) {
        if (data.nicotineLiteMessageData != null) {
            cartPageAnalytics.eventClickTrashIconButtonOnProductContainTobacco()
        } else {
            sendAnalyticsOnClickRemoveIconCartItem()
        }
        val cartItemDatas = listOf(data)
        val allCartItemDataList = cartAdapter.allCartItemData

        val dialog = getDisabledItemDialogDeleteConfirmation()

        dialog?.setPrimaryCTAClickListener {
            dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, false, false)
            sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                    dPresenter.generateDeleteCartDataAnalytics(cartItemDatas)
            )
            dialog.dismiss()
        }
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    override fun onTobaccoLiteUrlClicked(url: String) {
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

    override fun onGetCompositeSubscriber(): CompositeSubscription {
        return compositeSubscription
    }

    override fun onDetach() {
        compositeSubscription.unsubscribe()
        super.onDetach()
    }

    override fun showPromoCheckoutStickyButtonInactive() {
        promoCheckoutBtn.state = ButtonPromoCheckoutView.State.INACTIVE
        promoCheckoutBtn.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
        promoCheckoutBtn.title = getString(R.string.promo_checkout_inactive_label)
        promoCheckoutBtn.desc = getString(R.string.promo_checkout_inactive_desc)
        promoCheckoutBtn.setOnClickListener {
            renderPromoCheckoutLoading()
            dPresenter.doValidateUse(generateParamValidateUsePromoRevamp(false, -1, -1, true))
        }
    }

    override fun showPromoCheckoutStickyButtonLoading() {
        renderPromoCheckoutLoading()
    }

    override fun updatePromoCheckoutStickyButton(promoUiModel: PromoUiModel) {
        val lastApplyUiModel = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
        doRenderPromoCheckoutButton(lastApplyUiModel)
        if (promoUiModel.globalSuccess) {
            setLastApplyDataToShopGroup(lastApplyUiModel)
        }
    }

    private fun showToaster(msg: String, isShowOk: Boolean) {
        val toasterInfo = Toaster
        view?.let { v ->
            if (isShowOk) toasterInfo.make(v, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, ACTION_OK)
            else toasterInfo.make(v, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
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
        val params = generateParamValidateUsePromoRevamp(false, -1, -1, true)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        }
    }

    override fun onCartShopNameChecked(isCheckedAll: Boolean) {
        val params = generateParamValidateUsePromoRevamp(isCheckedAll, -1, -1, false)
        if (isNeedHitUpdateCartAndValidateUse(params)) {
            renderPromoCheckoutLoading()
            dPresenter.doUpdateCartAndValidateUse(params)
        } else {
            updatePromoCheckoutManualIfNoSelected(getAllAppliedPromoCodes(params))
        }
    }

    override fun navigateToPromoRecommendation() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
        val promoRequest = generateParamsCouponList()
        val validateUseRequest = generateParamValidateUsePromoRevamp(false, -1, -1, true)
        intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CART)
        intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
        intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequest)

        startActivityForResult(intent, NAVIGATION_PROMO)
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

    override fun sendATCTrackingURL(clickurl: String) {
        var url = "$clickurl&click_source=ATC_direct_click";
        ImpresionTask(userSession).execute(url);
    }
}