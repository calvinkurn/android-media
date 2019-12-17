package com.tokopedia.purchase_platform.features.cart.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.reflect.TypeToken
import com.readystatesoftware.chuck.Chuck
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.*
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartResponse
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.common.feature.promo_clashing.ClashBottomSheetFragment
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartAdapter
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartItemAdapter
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveView
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveWithBackView
import com.tokopedia.purchase_platform.features.cart.view.di.DaggerNewCartComponent
import com.tokopedia.purchase_platform.features.cart.view.mapper.PromoMapper
import com.tokopedia.purchase_platform.features.cart.view.mapper.RecentViewMapper
import com.tokopedia.purchase_platform.features.cart.view.mapper.WishlistMapper
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.*
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentActivity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.listener.WishListActionListener
import java.util.*
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartFragment : BaseCheckoutFragment(), ICartListView, ActionListener, CartItemAdapter.ActionListener, PromoActionListener, RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, ToolbarRemoveView.ToolbarCartListener, MerchantVoucherListBottomSheetFragment.ActionListener, ClashBottomSheetFragment.ActionListener, InsuranceItemActionListener, TickerAnnouncementActionListener {

    lateinit var appBarLayout: AppBarLayout
    lateinit var cartRecyclerView: RecyclerView
    lateinit var btnToShipment: TextView
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

    @Inject
    lateinit var dPresenter: ICartListPresenter
    @Inject
    lateinit var cartItemDecoration: RecyclerView.ItemDecoration
    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart
    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil
    @Inject
    lateinit var viewHolderDataMapper: ViewHolderDataMapper
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var wishlistMapper: WishlistMapper
    @Inject
    lateinit var recentViewMapper: RecentViewMapper
    @Inject
    lateinit var promoMapper: PromoMapper

    lateinit var cartAdapter: CartAdapter
    lateinit var refreshHandler: RefreshHandler
    lateinit var progressDialog: ProgressDialog

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
    private var hasTriedToLoadRecommendation: Boolean = false
    private var isInsuranceEnabled = false
    private var isToolbarWithBackButton = true

    companion object {

        private var FLAG_BEGIN_SHIPMENT_PROCESS = false
        private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        private var FLAG_IS_CART_EMPTY = false

        private val SHOP_INDEX_PROMO_GLOBAL = -1

        private val HAS_ELEVATION = 8
        private val NO_ELEVATION = 0
        private val CART_TRACE = "mp_cart"
        private val CART_ALL_TRACE = "mp_cart_all"
        private val CART_PAGE = "cart"
        private val NAVIGATION_PDP = 64728
        private val GO_TO_DETAIL = 2
        val GO_TO_LIST = 1

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

    override fun getActivityObject(): FragmentActivity? {
        return activity
    }

    private val selectedCartDataList: List<CartItemData>?
        get() = cartAdapter.selectedCartItemData

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

    private fun updateCartAfterDetached() {
        val hasChanges = dPresenter.dataHasChanged()
        try {
            val cartItemDataList = selectedCartDataList
            activity?.let {
                if (hasChanges && cartItemDataList?.isNotEmpty() == true && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                    val service = Intent(it, UpdateCartIntentService::class.java)
                    service.putParcelableArrayListExtra(
                            UpdateCartIntentService.EXTRA_CART_ITEM_DATA_LIST, ArrayList(cartAdapter.selectedCartItemData)
                    )
                    it.startService(service)
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
            DaggerNewCartComponent.builder()
                    .baseAppComponent(baseMainApplication.baseAppComponent)
                    .build()
                    .inject(this)
        }
        cartAdapter = CartAdapter(this, this, this, this, this)
    }

    override fun getOptionsMenuEnable(): Boolean {
        return true
    }

    private fun onContentAvailabilityChanged(available: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (available) {
                appBarLayout.elevation = NO_ELEVATION.toFloat()
            } else {
                appBarLayout.elevation = HAS_ELEVATION.toFloat()
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

        refreshHandler = RefreshHandler(activity, view, this)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
        progressDialog.setCancelable(false)

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
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        cartRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = cartAdapter
            addItemDecoration(cartItemDecoration)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(-1)) {
                        disableSwipeRefresh()
                    } else {
                        enableSwipeRefresh()
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
                setOnClickGoToChuck(this@CartFragment)
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

    private fun getAppliedPromoCodeList(toBeDeletedCartItemDataList: List<CartItemData>?): ArrayList<String> {
        val appliedPromoList = ArrayList<String>()
        val cartShopHolderDataList = cartAdapter.allShopGroupDataList
        for (cartShopHolderData in cartShopHolderDataList) {
            if (!TextUtils.isEmpty(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData?.code) &&
                    !appliedPromoList.contains(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData?.code)) {
                toBeDeletedCartItemDataList?.let {
                    for (cartItemData in toBeDeletedCartItemDataList) {
                        if (cartShopHolderData.shopGroupAvailableData.cartString == cartItemData.originData?.cartString) {
                            appliedPromoList.add(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData?.code
                                    ?: "")
                            break
                        }
                    }
                }
            }
        }

        return appliedPromoList
    }

    override fun onToolbarRemoveAllCart() {
        sendAnalyticsOnClickRemoveButtonHeader()
        val toBeDeletedCartItemDataList = selectedCartDataList
        val allCartItemDataList = cartAdapter.allCartItemData
        if (toBeDeletedCartItemDataList?.isNotEmpty() == true) {
            val dialog = getMultipleItemsDialogDeleteConfirmation(toBeDeletedCartItemDataList.size)
            dialog?.setPrimaryCTAClickListener {
                if (toBeDeletedCartItemDataList.isNotEmpty()) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, toBeDeletedCartItemDataList, getAppliedPromoCodeList(toBeDeletedCartItemDataList), false, true)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    toBeDeletedCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
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

    override fun onGoToChuck() {
        activity?.let {
            startActivity(Chuck.getLaunchIntent(it))
        }
    }

    private fun checkGoToShipment(message: String?) {
        if (message.isNullOrEmpty()) {
            val insuranceCartShopsArrayList = cartAdapter.isInsuranceCartProductUnSelected

            if (insuranceCartShopsArrayList.isNotEmpty()) {
                deleteMacroInsurance(insuranceCartShopsArrayList, false)
            } else if (cartAdapter.isInsuranceSelected) {
                cartPageAnalytics.sendEventPurchaseInsurance(userSession.userId,
                        cartAdapter.selectedInsuranceProductId,
                        cartAdapter.selectedInsuranceProductTitle)
            }
            if (selectedCartDataList != null) {
                dPresenter.processToUpdateCartData(selectedCartDataList!!)
            }
        } else {
            showToastMessageRed(message)
            sendAnalyticsOnButtonCheckoutClickedFailed()
            sendAnalyticsOnGoToShipmentFailed(message)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            setHasOptionsMenu(true)
            it.title = it.getString(R.string.title_activity_cart)
            if (savedInstanceState == null) {
                refreshHandler.startRefresh()
            } else {
                if (cartListData != null) {
                    dPresenter.setCartListData(cartListData!!)
                    renderLoadGetCartDataFinish()
                    renderInitialGetCartListDataSuccess(cartListData)
                    stopCartPerformanceTrace()
                } else {
                    refreshHandler.startRefresh()
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
        val appliedPromoCodes = ArrayList<String>()
        val cartShopHolderData = cartAdapter.getCartShopHolderDataByIndex(parentPosition)
        if (!TextUtils.isEmpty(cartShopHolderData?.shopGroupAvailableData?.voucherOrdersItemData?.code)) {
            appliedPromoCodes.add(cartShopHolderData?.shopGroupAvailableData?.voucherOrdersItemData?.code
                    ?: "")
        }
        val cartItemDatas = ArrayList(listOf(cartItemHolderData.cartItemData))
        val allCartItemDataList = cartAdapter.allCartItemData

        val dialog: DialogUnify?

        val macroInsurancePresent = cartAdapter.insuranceCartShops.isNotEmpty()
        val removeAllItem = allCartItemDataList.size == cartItemDatas.size
        val removeMacroInsurance = macroInsurancePresent && removeAllItem

        if (removeMacroInsurance) {
            dialog = getInsuranceDialogDeleteConfirmation()
            dialog?.setPrimaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, true, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    )
                }
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    )
                }
                dialog.dismiss()
            }

        } else {
            dialog = getDialogDeleteConfirmation()
            dialog?.setPrimaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
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
        sendAnalyticsOnClickProductNameCartItem(cartItemHolderData.cartItemData.originData?.productName
                ?: "")
        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, cartItemHolderData.cartItemData.originData?.productId), NAVIGATION_PDP)
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

    override fun onRecommendationProductClicked(productId: String) {
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

        onProductClicked(productId)
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
        dPresenter.setHasPerformChecklistChange()
        cartAdapter.setShopSelected(itemPosition, checked)
        cartAdapter.notifyDataSetChanged()
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        cartAdapter.checkForShipmentForm()
    }

    override fun onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestionHolderData: CartPromoSuggestionHolderData, position: Int) {
        cartPromoSuggestionHolderData.isVisible = false
        cartAdapter.notifyItemChanged(position)
        cartAdapter.checkForShipmentForm()
    }

    override fun onCartPromoUseVoucherGlobalPromoClicked(cartPromoGlobal: PromoStackingData, position: Int) {
        val cartItemData = selectedCartDataList
        if (cartItemData?.isNotEmpty() == true) {
            trackingPromoCheckoutUtil.cartClickUseTickerPromoOrCoupon()
            dPresenter.processUpdateCartDataPromoStacking(cartItemData, cartPromoGlobal, GO_TO_LIST)
        } else {
            showToastMessageRed(getString(R.string.checkout_module_label_promo_no_item_checked))
        }
    }

    override fun onVoucherMerchantPromoClicked(`object`: Any) {
        if (`object` is ShopGroupAvailableData) {
            selectedCartDataList?.let {
                cartPageAnalytics.eventClickPilihMerchantVoucher()
                dPresenter.processUpdateCartDataPromoMerchant(it, `object`)
            }
        }
    }

    override fun onCartPromoCancelVoucherPromoGlobalClicked(cartPromoGlobal: PromoStackingData, position: Int) {
        val promoCodes = ArrayList<String>()
        promoCodes.add(cartPromoGlobal.promoCode)
        dPresenter.processCancelAutoApplyPromoStack(SHOP_INDEX_PROMO_GLOBAL, promoCodes, false)
    }

    override fun onCancelVoucherMerchantClicked(promoMerchantCode: String, position: Int, ignoreAPIResponse: Boolean) {
        cartPageAnalytics.eventClickHapusPromoXOnTicker(promoMerchantCode)
        val promoMerchantCodes = ArrayList<String>()
        promoMerchantCodes.add(promoMerchantCode)
        dPresenter.processCancelAutoApplyPromoStack(position, promoMerchantCodes, ignoreAPIResponse)
    }

    override fun onPromoGlobalTrackingImpression(cartPromoGlobal: PromoStackingData) {
        trackingPromoCheckoutUtil.cartImpressionTicker(cartPromoGlobal.getPromoCodeSafe())
    }

    override fun onPromoGlobalTrackingCancelled(cartPromoGlobal: PromoStackingData, position: Int) {
        sendAnalyticsOnClickCancelPromoCodeAndCouponBanner()
    }

    override fun onClickDetailPromoGlobal(dataGlobal: PromoStackingData, position: Int) {
        val cartItemData = selectedCartDataList
        if (cartItemData?.isNotEmpty() == true) {
            trackingPromoCheckoutUtil.cartClickUseTickerPromoOrCoupon()
            dPresenter.processUpdateCartDataPromoStacking(cartItemData, dataGlobal, GO_TO_DETAIL)
        }
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
        dPresenter.setHasPerformChecklistChange()
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        cartAdapter.checkForShipmentForm()
        return cartAdapter.setItemSelected(position, parentPosition, checked)
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
        if (!progressDialog.isShowing) progressDialog.show()
    }

    override fun hideProgressLoading() {
        if (progressDialog.isShowing) progressDialog.dismiss()
        if (refreshHandler.isRefreshing) {
            refreshHandler.finishRefresh()
        }
    }

    override fun renderInitialGetCartListDataSuccess(cartListData: CartListData?) {
        cartListData?.let {
            sendAnalyticsScreenName(screenName)

            endlessRecyclerViewScrollListener.resetState()
            refreshHandler.finishRefresh()
            this.cartListData = it
            cartAdapter.resetData()

            renderTickerAnnouncement(it)

            if (it.shopGroupAvailableDataList.isEmpty() && it.shopGroupWithErrorDataList.isEmpty()) {
                renderCartEmpty(it)
            } else {
                renderCartNotEmpty(it)
            }

            if (recentViewList == null) {
                dPresenter.processGetRecentViewData()
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
        }
    }

    private fun renderCartNotEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = false
        cartAdapter.removeCartEmptyData()

        val promoStackingData = getPromoGlobalData(cartListData)
        renderPromoGlobal(promoStackingData)
        renderTickerError(cartListData)
        renderCartAvailable(cartListData)
        renderCartNotAvailable(cartListData)
        loadMacroInsurance(cartListData)

        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        cbSelectAll.isChecked = cartListData.isAllSelected

        cartAdapter.checkForShipmentForm()
        if (cartRecyclerView.itemDecorationCount == 0) {
            cartRecyclerView.addItemDecoration(cartItemDecoration)
        }

        activity?.let {
            llCartContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.checkout_module_color_background))
        }

        cartPageAnalytics.eventViewCartListFinishRender()
        val cartItemDataList = cartAdapter.allCartItemData
        cartPageAnalytics.enhancedECommerceCartLoadedStep0(
                dPresenter.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_0)
        )

        cartAdapter.notifyDataSetChanged()
    }

    private fun renderCartEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = true

        val promoStackingData = getPromoGlobalData(cartListData)
        if (promoStackingData.state !== TickerPromoStackingCheckoutView.State.EMPTY) {
            renderPromoGlobal(promoStackingData)
        }

        renderEmptyCartPlaceholder()
        enableSwipeRefresh()
        sendAnalyticsOnDataCartIsEmpty()
        showEmptyCartContainer()
        notifyBottomCartParent()

        if (cartRecyclerView.itemDecorationCount > 0) {
            cartRecyclerView.removeItemDecoration(cartItemDecoration)
        }

        activity?.let {
            llCartContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
        }

        cartAdapter.notifyDataSetChanged()
    }

    private fun renderTickerAnnouncement(cartListData: CartListData) {
        val tickerData = cartListData.tickerData
        if (tickerData?.isValid(CART_PAGE) == true) {
            cartAdapter.addCartTicker(TickerAnnouncementHolderData(tickerData.id.toString(), tickerData.message))
        }
    }

    private fun renderPromoGlobal(promoStackingData: PromoStackingData) {
        cartAdapter.addPromoStackingVoucherData(promoStackingData)
        if (promoStackingData.state !== TickerPromoStackingCheckoutView.State.FAILED) {
            onPromoGlobalTrackingImpression(promoStackingData)
        }
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

    private fun renderEmptyCartPlaceholder() {
        cartAdapter.addCartEmptyData()
    }

    private fun getPromoGlobalData(cartListData: CartListData): PromoStackingData {
        var flagAutoApplyStack = false
        val builderGlobal = PromoStackingData.Builder()
        if (cartListData.autoApplyStackData?.isSuccess == true && !TextUtils.isEmpty(cartListData.autoApplyStackData?.code)) {
            val autoApplyStackData = cartListData.autoApplyStackData
            if (autoApplyStackData != null) {
                if (autoApplyStackData.messageSuccess != null && autoApplyStackData.code != null
                        && autoApplyStackData.state != null && autoApplyStackData.titleDescription != null) {
                    builderGlobal.typePromo(
                            if (autoApplyStackData.isCoupon == PromoStackingData.VALUE_COUPON)
                                PromoStackingData.TYPE_COUPON
                            else
                                PromoStackingData.TYPE_VOUCHER)
                            .description(autoApplyStackData.messageSuccess)
                            .amount(autoApplyStackData.discountAmount)
                            .promoCode(autoApplyStackData.code)
                            .state(autoApplyStackData.state.mapToStatePromoStackingCheckout())
                            .title(autoApplyStackData.titleDescription)
                            .build()
                    sendAnalyticsOnViewPromoAutoApply()
                    flagAutoApplyStack = true
                }
            } else {
                builderGlobal.state(TickerPromoStackingCheckoutView.State.EMPTY)
            }
        } else {
            builderGlobal.state(TickerPromoStackingCheckoutView.State.EMPTY)
        }

        if (!flagAutoApplyStack) {
            if (cartListData.globalCouponAttrData?.description?.isNotEmpty() == true) {
                builderGlobal.title(cartListData.globalCouponAttrData?.description ?: "")
                builderGlobal.titleDefault(cartListData.globalCouponAttrData?.description ?: "")
            }

            if (cartListData.globalCouponAttrData?.quantityLabel?.isNotEmpty() == true) {
                builderGlobal.counterLabel(cartListData.globalCouponAttrData?.quantityLabel ?: "")
                builderGlobal.counterLabelDefault(cartListData.globalCouponAttrData?.quantityLabel
                        ?: "")
            }
        }

        return builderGlobal.build()
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
            refreshHandler.finishRefresh()
            showErrorContainer()
            onContentAvailabilityChanged(false)
            NetworkErrorHelper.showEmptyState(it, llNetworkErrorView, message) {
                llNetworkErrorView.gone()
                rlContent.show()
                refreshHandler.isRefreshing = true
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
    }

    private fun showEmptyCartContainer() {
        llNetworkErrorView.gone()
        bottomLayout.gone()
        bottomLayoutShadow.gone()
        cardHeader.gone()
        onContentAvailabilityChanged(false)
    }

    private fun showSnackbarRetry(message: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, message) { dPresenter.processInitialGetCartData(getCartId(), dPresenter.getCartListData() == null, false) }
                .showRetrySnackbar()
    }

    override fun renderErrorInitialGetCartListData(message: String) {
        if (cartAdapter.itemCount > 0) {
            showSnackbarRetry(message)
        } else {
            showErrorLayout(message)
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
        val isAutoApplyPromoStackCodeApplied = dPresenter.getCartListData()?.autoApplyStackData?.isSuccess
                ?: false
        activity?.let {
            val intent = ShipmentActivity.createInstance(it, cartAdapter.promoStackingGlobalData,
                    cartListData?.cartPromoSuggestionHolderData, cartListData?.defaultPromoDialogTab,
                    isAutoApplyPromoStackCodeApplied
            )
            startActivityForResult(intent, ShipmentActivity.REQUEST_CODE)
        }
    }

    private fun clearRecyclerView() {
        cartAdapter.unsubscribeSubscription()
        cartRecyclerView.adapter = null
        cartAdapter = CartAdapter(null, null, null, null, null)
        cartRecyclerView.removeAllViews()
        cartRecyclerView.recycledViewPool.clear()
    }

    override fun renderErrorToShipmentForm(message: String) {
        sendAnalyticsOnButtonCheckoutClickedFailed()
        sendAnalyticsOnGoToShipmentFailed(message)
        showToastMessageRed(message)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            refreshHandler.isRefreshing = true
            if (dPresenter.getCartListData() == null) {
                dPresenter.processInitialGetCartData(getCartId(), true, false)
            } else {
                if (dPresenter.dataHasChanged()) {
                    dPresenter.processToUpdateAndReloadCartData()
                } else {
                    dPresenter.processInitialGetCartData(getCartId(), false, true)
                }
            }
        }
    }

    private fun disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false)
    }

    private fun enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true)
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

    override fun renderDetailInfoSubTotal(qty: String, subtotalPrice: String, selectAllItem: Boolean, unselectAllItem: Boolean, hasAvailableItems: Boolean) {
        dPresenter.getCartListData()?.isAllSelected = selectAllItem
        if (cbSelectAll.isChecked != selectAllItem) {
            cbSelectAll.isChecked = selectAllItem
        }
        btnRemove.visibility = if (unselectAllItem) View.INVISIBLE else View.VISIBLE
        cardHeader.visibility = if (hasAvailableItems) View.GONE else View.VISIBLE
        tvTotalPrice.text = subtotalPrice
        btnToShipment.text = String.format(getString(R.string.cart_item_button_checkout_count_format), qty)
    }

    override fun updateCashback(cashback: Double) {
        cartAdapter.updateShipmentSellerCashback(cashback)
    }

    override fun goToCouponList() {
        activity?.let {
            cartAdapter.promoStackingGlobalData?.apply {
                val promo = dPresenter.generateCheckPromoFirstStepParam(this)
                val intent = getIntentToPromoList(promo, it)
                startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    override fun goToDetailPromoStacking(promoStackingData: PromoStackingData) {
        activity?.let {
            cartAdapter.promoStackingGlobalData?.apply {
                val promo = dPresenter.generateCheckPromoFirstStepParam(this)
                if (promoStackingData.typePromo == PromoStackingData.TYPE_COUPON) {
                    val intent = getIntentToPromoDetail(promo, promoStackingData, it)
                    startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
                } else {
                    val intent = getIntentToPromoList(promo, it)
                    startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
                }
            }
        }
    }

    private fun getIntentToPromoList(promo: Promo, activity: Activity): Intent {
        return RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_MARKETPLACE).apply {
            val bundle = Bundle().apply {
                putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, cartListData?.isPromoCouponActive
                        ?: false)
                putString(PROMO_CODE, "")
                putBoolean(ONE_CLICK_SHIPMENT, false)
                putInt(PAGE_TRACKING, FROM_CART)
                putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, promo)
            }
            putExtras(bundle)
        }
    }

    private fun getIntentToPromoDetail(promo: Promo, promoStackingData: PromoStackingData, activity: Activity): Intent {
        return RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            putExtra(EXTRA_KUPON_CODE, promoStackingData.getPromoCodeSafe())
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
            putExtra(CHECK_PROMO_CODE_FIRST_STEP_PARAM, promo)
        }
    }

    override fun showToastMessageRed(message: String) {
        var tmpMessage = message
        if (TextUtils.isEmpty(tmpMessage)) {
            tmpMessage = getString(R.string.default_request_error_unknown)
        }

        if (view != null) {
            NetworkErrorHelper.showRedCloseSnackbar(view, tmpMessage)
        } else if (activity != null) {
            Toast.makeText(activity, tmpMessage, Toast.LENGTH_LONG).show()
        }
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
        onContentAvailabilityChanged(false)
    }

    override fun renderLoadGetCartDataFinish() {
        if (refreshHandler.isRefreshing) {
            refreshHandler.isRefreshing = false
        }
        showMainContainer()
        onContentAvailabilityChanged(true)
    }

    override fun onDeleteCartDataSuccess(deletedCartIds: List<Int>) {
        cartAdapter.removeCartItemById(deletedCartIds, context)
        dPresenter.reCalculateSubTotal(cartAdapter.allShopGroupDataList, cartAdapter.insuranceCartShops)
        notifyBottomCartParent()
    }

    override fun onRefresh(view: View?) {
        if (dPresenter.dataHasChanged()) {
            showMainContainer()
            dPresenter.processToUpdateAndReloadCartData()
        } else {
            if (dPresenter.getCartListData()?.shopGroupAvailableDataList?.isNotEmpty() == true) {
                showMainContainer()
            }
            dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
            //            String promo = PersistentCacheManager.instance.getString("KEY_CACHE_PROMO_CODE", "");
            //            if (!TextUtils.isEmpty(promo)) {
            //                dPresenter.processCheckPromoCodeFromSuggestedPromo(promo, true);
            //            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE -> onResultFromRequestCodeLoyalty(resultCode, data)
            ShipmentActivity.REQUEST_CODE -> onResultFromRequestCodeCartShipment(resultCode, data)
            NAVIGATION_PDP -> {
                refreshHandler.isRefreshing = true
                dPresenter.processInitialGetCartData(getCartId(), cartListData == null, true)
            }
        }
    }

    private fun onResultFromRequestCodeCartShipment(resultCode: Int, data: Intent?) {
        if (cartRecyclerView.adapter == null) {
            cartAdapter = CartAdapter(this, this, this, this, this)
            cartRecyclerView.adapter = cartAdapter
        }
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false

        if (resultCode == PaymentConstant.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module))
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success))
            refreshHandler.isRefreshing = true
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_FAILED) {
            showToastMessageRed(getString(R.string.default_request_error_unknown))
            sendAnalyticsScreenName(screenName)
            refreshHandler.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            sendAnalyticsScreenName(screenName)
            refreshHandler.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter.processInitialGetCartData(getCartId(), false, false)
            }
        } else if (resultCode == ShipmentActivity.RESULT_CODE_COUPON_STATE_CHANGED) {
            refreshHandler.isRefreshing = true
            dPresenter.processInitialGetCartData(getCartId(), false, false)
        }
    }

    private fun onResultFromRequestCodeLoyalty(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            sendAnalyticsScreenName(screenName)
            val bundle = data?.extras
            bundle?.let {
                val promoStackingData = bundle.getParcelable<PromoStackingData>(EXTRA_PROMO_DATA)

                if (cartListData?.globalCouponAttrData?.description?.isNotEmpty() == true) {
                    promoStackingData?.titleDefault = cartListData?.globalCouponAttrData?.description
                            ?: ""
                }

                if (cartListData?.globalCouponAttrData?.quantityLabel?.isNotEmpty() == true) {
                    promoStackingData?.counterLabelDefault = cartListData?.globalCouponAttrData?.quantityLabel
                            ?: ""
                }

                for (cartItemData in cartAdapter.allCartItemData) {
                    if (promoStackingData?.trackingDetailUiModels?.isNotEmpty() == true) {
                        val trackingDetailsUiModels = promoStackingData.trackingDetailUiModels
                        for (trackingDetailUiModel in trackingDetailsUiModels) {
                            if (trackingDetailUiModel.productId.toString().equals(cartItemData.originData?.productId, ignoreCase = true)) {
                                cartItemData.originData?.promoCodes = trackingDetailUiModel.promoCodesTracking
                                cartItemData.originData?.promoDetails = trackingDetailUiModel.promoDetailsTracking
                            }
                        }
                    }
                }

                promoStackingData?.let {
                    cartAdapter.updateItemPromoStackVoucher(it)
                    if (it.typePromo == PromoStackingData.TYPE_VOUCHER) {
                        sendAnalyticsOnViewPromoManualApply("voucher")
                    } else {
                        sendAnalyticsOnViewPromoManualApply("coupon")
                    }
                }
            }
        } else if (resultCode == RESULT_CLASHING) {
            data?.extras?.let {
                val clashingInfoDetailUiModel = it.getParcelable<ClashingInfoDetailUiModel>(EXTRA_CLASHING_DATA)
                if (clashingInfoDetailUiModel != null) {
                    var type = it.getString(EXTRA_TYPE)
                    if (type == null) type = ""
                    onClashCheckPromo(clashingInfoDetailUiModel, type)
                }
            }
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

    override fun showMerchantVoucherListBottomsheet(shopGroupAvailableData: ShopGroupAvailableData) {
        if (fragmentManager != null) {
            var shopId = 0
            try {
                shopId = Integer.parseInt(shopGroupAvailableData.shopId ?: "0")
            } catch (e: NumberFormatException) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    e.printStackTrace()
                }
            }

            val cartItemDataVoucherArrayList = ArrayList<CartItemDataVoucher>()
            val cartItemDataList = shopGroupAvailableData.cartItemDataList
            cartItemDataList?.let {
                for (cartItemHolderData in cartItemDataList) {
                    if (cartItemHolderData.isSelected) {
                        try {
                            val cartItemDataVoucher = CartItemDataVoucher().apply {
                                productId = Integer.parseInt(cartItemHolderData.cartItemData.originData?.productId
                                        ?: "0")
                                productName = cartItemHolderData.cartItemData.originData?.productName
                                        ?: ""
                            }
                            cartItemDataVoucherArrayList.add(cartItemDataVoucher)
                        } catch (e: NumberFormatException) {
                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e.printStackTrace()
                            }
                        }
                    }
                }

                cartAdapter.promoStackingGlobalData?.let {
                    val promo = dPresenter.generateCheckPromoFirstStepParam(it)
                    val merchantVoucherListBottomSheetFragment = MerchantVoucherListBottomSheetFragment.newInstance(
                            shopId, shopGroupAvailableData.cartString ?: "",
                            promo, "cart", cartItemDataVoucherArrayList
                    )
                    merchantVoucherListBottomSheetFragment.actionListener = this@CartFragment
                    merchantVoucherListBottomSheetFragment.show(fragmentManager, "")
                }
            }
        }
    }

    override fun onClashCheckPromo(clashingInfoDetailUiModel: ClashingInfoDetailUiModel,
                                   type: String) {
        fragmentManager?.let {
            ClashBottomSheetFragment.newInstance().apply {
                setData(clashingInfoDetailUiModel)
                setActionListener(this@CartFragment)
                setAnalyticsCart(cartPageAnalytics)
                setSource("cart")
                setType(type)
                show(fragmentManager, "")
            }
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

    override fun onSuccessCheckPromoMerchantFirstStep(promoData: ResponseGetPromoStackUiModel, promoCode: String) {
        onSuccessCheckPromoFirstStep(promoData)
    }

    override fun onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel: ResponseGetPromoStackUiModel) {
        // Update global promo state
        if (responseGetPromoStackUiModel.data.codes.isNotEmpty()) {
            cartAdapter.promoStackingGlobalData?.let {
                promoMapper.convertPromoGlobalModel(responseGetPromoStackUiModel, it)
            }
        }

        // Update merchant voucher state
        cartAdapter.allShopGroupDataList?.let {
            for (cartShopHolderData in it) {
                for (voucherOrdersItemUiModel in responseGetPromoStackUiModel.data.voucherOrders) {
                    if (voucherOrdersItemUiModel.uniqueId == cartShopHolderData.shopGroupAvailableData.cartString) {
                        var voucherOrdersItemData = cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData
                        if (voucherOrdersItemData == null) {
                            voucherOrdersItemData = VoucherOrdersItemData()
                        }
                        promoMapper.convertPromoMerchantModel(voucherOrdersItemUiModel, voucherOrdersItemData)
                        cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData = voucherOrdersItemData
                        break
                    }
                }
                if (responseGetPromoStackUiModel.data.trackingDetailUiModel.isNotEmpty()) {
                    for (trackingDetailUiModel in responseGetPromoStackUiModel.data.trackingDetailUiModel) {
                        val cartItemHolderDataList = cartShopHolderData.shopGroupAvailableData.cartItemDataList
                        cartItemHolderDataList?.let {
                            for (cartItemHolderData in cartItemHolderDataList) {
                                if (trackingDetailUiModel.productId.toString().equals(cartItemHolderData.cartItemData.originData?.productId, ignoreCase = true)) {
                                    cartItemHolderData.cartItemData.originData?.promoCodes = trackingDetailUiModel.promoCodesTracking
                                    cartItemHolderData.cartItemData.originData?.promoDetails = trackingDetailUiModel.promoDetailsTracking
                                }
                            }
                        }
                    }
                }
            }
        }

        cartAdapter.notifyDataSetChanged()
    }

    override fun onSuccessClearPromoStack(shopIndex: Int) {
        if (shopIndex == SHOP_INDEX_PROMO_GLOBAL) {
            if (cartListData?.shopGroupAvailableDataList?.isEmpty() == true) {
                cartAdapter.removePromoStackingVoucherData()
            } else {
                cartAdapter.promoStackingGlobalData?.let {
                    resetPromoGlobal(it)
                    cartAdapter.updateItemPromoStackVoucher(it)
                }
            }
        } else {
            cartAdapter.getCartShopHolderDataByIndex(shopIndex)?.let {
                it.shopGroupAvailableData.voucherOrdersItemData = null
                cartAdapter.notifyItemChanged(shopIndex)
            }
        }
    }

    override fun onSuccessClearPromoStackAfterClash() {
        // Reset global promo
        cartAdapter.promoStackingGlobalData?.let {
            resetPromoGlobal(it)
        }

        // Reset merchant promo
        val cartShopHolderDataList = cartAdapter.allCartShopHolderData
        for (cartShopHolderData in cartShopHolderDataList) {
            cartShopHolderData.let {
                cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData = null
            }
        }

        cartAdapter.notifyDataSetChanged()
    }

    private fun resetPromoGlobal(promoStackingData: PromoStackingData) {
        promoStackingData.state = TickerPromoStackingCheckoutView.State.EMPTY
        promoStackingData.amount = 0
        promoStackingData.promoCode = ""
        promoStackingData.description = ""
        promoStackingData.title = promoStackingData.titleDefault
        promoStackingData.counterLabel = promoStackingData.counterLabelDefault
    }

    override fun onFailedClearPromoStack(ignoreAPIResponse: Boolean) {
        if (!ignoreAPIResponse) {
            showToastMessageRed("")
        }
    }

    override fun onSubmitNewPromoAfterClash(oldPromoList: ArrayList<String>,
                                            newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                            type: String) {
        cartAdapter.promoStackingGlobalData?.let {
            dPresenter.processCancelAutoApplyPromoStackAfterClash(it, oldPromoList, newPromoList, type)
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
        if (this.recentViewList != null) {
            cartRecentViewItemHolderDataList.addAll(this.recentViewList!!)
        } else if (recentViewList != null) {
            cartRecentViewItemHolderDataList = recentViewMapper.convertToViewHolderModelList(recentViewList)
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recent_view)

        val cartRecentViewHolderData = CartRecentViewHolderData()
        cartRecentViewHolderData.recentViewList = cartRecentViewItemHolderDataList
        cartAdapter.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.recentViewList = cartRecentViewItemHolderDataList

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

            sendAnalyticsOnViewProductRecommendation(
                    dPresenter.generateRecommendationDataAnalytics(recommendationList, FLAG_IS_CART_EMPTY)
            )
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
        sendAnalyticsOnClickRemoveCartConstrainedProduct(dPresenter.generateCartDataAnalytics(
                allDisabledCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
        ))

        dialog?.setPrimaryCTAClickListener {
            if (allDisabledCartItemDataList.size > 0) {
                dPresenter.processDeleteCartItem(allCartItemDataList, allDisabledCartItemDataList, null, false, false)
                sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(
                        dPresenter.generateCartDataAnalytics(
                                allDisabledCartItemDataList, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
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
            dPresenter.processDeleteCartItem(allCartItemDataList, cartItemDatas, null, false, false)
            sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                    dPresenter.generateCartDataAnalytics(cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION)
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

    override fun goToLite(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}