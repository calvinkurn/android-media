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
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
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
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher
import com.tokopedia.merchantvoucher.voucherlistbottomsheet.MerchantVoucherListBottomSheetFragment
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.promocheckout.common.analytics.*
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.*
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.uimodel.TrackingDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel
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
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShopItems
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.common.feature.promo_clashing.ClashBottomSheetFragment
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.TickerData
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupWithErrorData
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartAdapter
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartItemAdapter
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveView
import com.tokopedia.purchase_platform.features.cart.view.compoundview.ToolbarRemoveWithBackView
import com.tokopedia.purchase_platform.features.cart.view.di.DaggerNewCartComponent
import com.tokopedia.purchase_platform.features.cart.view.mapper.PromoMapper
import com.tokopedia.purchase_platform.features.cart.view.mapper.RecentViewMapper
import com.tokopedia.purchase_platform.features.cart.view.mapper.WishlistMapper
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartSectionHeaderHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentActivity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.listener.WishListActionListener

import java.util.ArrayList
import java.util.Collections

import javax.inject.Inject

import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartFragment : BaseCheckoutFragment(), ICartListView, ActionListener, CartItemAdapter.ActionListener, PromoActionListener, RefreshHandler.OnRefreshHandlerListener, ICartListAnalyticsListener, ToolbarRemoveView.ToolbarCartListener, MerchantVoucherListBottomSheetFragment.ActionListener, ClashBottomSheetFragment.ActionListener, InsuranceItemActionListener, TickerAnnouncementActionListener {

    private var FLAG_BEGIN_SHIPMENT_PROCESS = false
    private var FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
    private var FLAG_IS_CART_EMPTY = false

    private var appBarLayout: AppBarLayout? = null
    private var cartRecyclerView: RecyclerView? = null
    private var btnToShipment: TextView? = null
    private var tvTotalPrice: TextView? = null
    private var rlContent: RelativeLayout? = null
    private var cbSelectAll: CheckBox? = null
    private var llHeader: LinearLayout? = null
    private var btnRemove: Typography? = null
    private var cardHeader: CardView? = null
    private var bottomLayout: LinearLayout? = null
    private var bottomLayoutShadow: View? = null
    private var llNetworkErrorView: LinearLayout? = null
    private var llCartContainer: LinearLayout? = null

    private var progressDialog: ProgressDialog? = null

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

    private var isToolbarWithBackButton = true

    lateinit var cartPerformanceMonitoring: PerformanceMonitoring
    private var isTraceCartStopped: Boolean = false

    lateinit var cartAllPerformanceMonitoring: PerformanceMonitoring
    private var isTraceCartAllStopped: Boolean = false

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var hasLoadRecommendation: Boolean = false

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager
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

    private val selectedCartDataList: List<CartItemData>?
        get() = cartAdapter.selectedCartItemData

    private val dialogDeleteConfirmation: DialogUnify
        get() {
            val dialogUnify = DialogUnify(activity!!, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_item))
            dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_item))
            dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
            dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            return dialogUnify
        }

    private val disabledItemDialogDeleteConfirmation: DialogUnify
        get() {
            val dialogUnify = DialogUnify(activity!!, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialogUnify.setTitle(getString(R.string.label_dialog_title_delete_disabled_item))
            dialogUnify.setDescription(getString(R.string.label_dialog_message_remove_cart_item))
            dialogUnify.setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
            dialogUnify.setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            return dialogUnify
        }

    private val insuranceDialogDeleteConfirmation: DialogUnify
        get() {
            val dialog = DialogUnify(activity!!, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.label_dialog_title_delete_item_macro_insurance))
            dialog.setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item_with_insurance))
            dialog.setPrimaryCTAText(getString(R.string.label_dialog_action_delete_and_add_to_wishlist_macro_insurance))
            dialog.setSecondaryCTAText(getString(R.string.label_dialog_action_delete_macro_insurance))
            return dialog
        }

    private fun getMultipleItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.apply {
            return DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_dialog_title_delete_multiple_item, count))
                setDescription(getString(R.string.label_dialog_message_remove_cart_multiple_item))
                setPrimaryCTAText(getString(R.string.label_dialog_action_delete))
                setSecondaryCTAText(getString(R.string.label_dialog_action_cancel))
            }
        }

        return null
    }

    private fun getMultipleDisabledItemsDialogDeleteConfirmation(count: Int): DialogUnify? {
        activity?.apply {
            return DialogUnify(activity!!, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
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

        if (activity != null) {
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            saveInstanceCacheManager = SaveInstanceCacheManager(activity!!, savedInstanceState)
        }

        if (savedInstanceState != null && saveInstanceCacheManager != null) {
            loadCachedData()
        } else {
            cartPerformanceMonitoring = PerformanceMonitoring.start(CART_TRACE)
            cartAllPerformanceMonitoring = PerformanceMonitoring.start(CART_ALL_TRACE)
        }

        initRemoteConfig()

        dPresenter!!.attachView(this)
    }

    private fun initRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        isInsuranceEnabled = remoteConfig.getBoolean(APP_ENABLE_INSURANCE_RECOMMENDATION, false)
    }

    private fun loadCachedData() {
        cartListData = saveInstanceCacheManager!!.get<CartListData>(CartListData::class.java.simpleName, CartListData::class.java)
        wishLists = saveInstanceCacheManager!!.get<List<CartWishlistItemHolderData>>(CartWishlistItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartWishlistItemHolderData>>() {

                }.type, null)
        recentViewList = saveInstanceCacheManager!!.get<List<CartRecentViewItemHolderData>>(CartRecentViewItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecentViewItemHolderData>>() {

                }.type, null)
        recommendationList = saveInstanceCacheManager!!.get<List<CartRecommendationItemHolderData>>(CartRecommendationItemHolderData::class.java.simpleName,
                object : TypeToken<ArrayList<CartRecommendationItemHolderData>>() {

                }.type, null)
        recommendationSectionHeader = saveInstanceCacheManager!!.get<CartSectionHeaderHolderData>(CartSectionHeaderHolderData::class.java.simpleName,
                object : TypeToken<CartSectionHeaderHolderData>() {

                }.type, null)
    }

    override fun onStop() {
        updateCartAfterDetached()

        if (FLAG_SHOULD_CLEAR_RECYCLERVIEW) {
            clearRecyclerView()
        }

        super.onStop()
    }

    private fun updateCartAfterDetached() {
        val hasChanges = dPresenter!!.dataHasChanged()
        try {
            val activity = activity
            val cartItemDataList = selectedCartDataList
            if (hasChanges && activity != null && cartItemDataList != null
                    && cartItemDataList.size > 0 && !FLAG_BEGIN_SHIPMENT_PROCESS) {
                val service = Intent(getActivity(), UpdateCartIntentService::class.java)
                service.putParcelableArrayListExtra(
                        UpdateCartIntentService.EXTRA_CART_ITEM_DATA_LIST, ArrayList(selectedCartDataList!!)
                )
                getActivity()!!.startService(service)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        cartAdapter!!.unsubscribeSubscription()
        dPresenter!!.detachView()
        super.onDestroy()
    }

    override fun initInjector() {
        if (activity != null) {
            val baseMainApplication = activity!!.application as BaseMainApplication
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (available) {
                appBarLayout!!.elevation = NO_ELEVATION.toFloat()
            } else {
                appBarLayout!!.elevation = HAS_ELEVATION.toFloat()
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
        progressDialog!!.setMessage(getString(R.string.title_loading))
        progressDialog!!.setCancelable(false)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // Remove default cardview margin on Kitkat or lower
            val pixel = Utils.convertDpToPixel(-6f, context!!)
            (cardHeader!!.layoutParams as ViewGroup.MarginLayoutParams).setMargins(pixel, pixel, pixel, pixel)
        }

        initViewListener()
        setupRecyclerView()
    }

    private fun initViewListener() {
        btnToShipment!!.setOnClickListener { view -> checkGoToShipment("") }
        cbSelectAll!!.setOnClickListener { view -> onSelectAllClicked() }
        llHeader!!.setOnClickListener { view -> onSelectAllClicked() }
        btnRemove!!.setOnClickListener { v ->
            if (btnRemove!!.visibility == View.VISIBLE) {
                onToolbarRemoveAllCart()
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        cartRecyclerView!!.layoutManager = layoutManager
        cartRecyclerView!!.adapter = cartAdapter
        cartRecyclerView!!.addItemDecoration(cartItemDecoration!!)
        cartRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        (cartRecyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position != RecyclerView.NO_POSITION) {
                    if (position < cartAdapter!!.itemCount && cartAdapter!!.getItemViewType(position) == CartRecommendationViewHolder.LAYOUT) {
                        1
                    } else 2
                } else 0
            }
        }

        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (hasLoadRecommendation) {
                    dPresenter!!.processGetRecommendationData(endlessRecyclerViewScrollListener!!.currentPage, cartAdapter!!.allCartItemProductId)
                }
            }
        }
        cartRecyclerView!!.addOnScrollListener(endlessRecyclerViewScrollListener!!)
    }

    private fun setupToolbar(view: View) {
        if (arguments != null) {
            val args = arguments!!.getString(CartFragment::class.java.simpleName)
            if (args != null && !args.isEmpty()) {
                isToolbarWithBackButton = false
            }
        }

        val appbar = view.findViewById<Toolbar>(R.id.toolbar)
        val statusBarBackground = view.findViewById<View>(R.id.status_bar_bg)
        statusBarBackground.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(activity!!)

        val toolbar: View
        if (isToolbarWithBackButton) {
            toolbar = toolbarRemoveWithBackView()
            statusBarBackground.visibility = View.GONE
        } else {
            toolbar = toolbarRemoveView()
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarBackground.visibility = View.INVISIBLE
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarBackground.visibility = View.VISIBLE
            } else {
                statusBarBackground.visibility = View.GONE
            }
        }
        appbar.addView(toolbar)
        (activity as AppCompatActivity).setSupportActionBar(appbar)
    }

    private fun toolbarRemoveWithBackView(): ToolbarRemoveWithBackView {
        val toolbar = ToolbarRemoveWithBackView(activity!!)
        toolbar.navigateUp(activity)
        toolbar.setOnClickGoToChuck(this)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun toolbarRemoveView(): ToolbarRemoveView {
        val toolbar = ToolbarRemoveView(activity!!)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun getAppliedPromoCodeList(toBeDeletedCartItemDataList: List<CartItemData>?): ArrayList<String> {
        val appliedPromoList = ArrayList<String>()
        val cartShopHolderDataList = cartAdapter!!.allShopGroupDataList
        for (cartShopHolderData in cartShopHolderDataList) {
            if (cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData != null &&
                    !TextUtils.isEmpty(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData!!.code) &&
                    !appliedPromoList.contains(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData!!.code)) {
                for ((originData) in toBeDeletedCartItemDataList!!) {
                    if (cartShopHolderData.shopGroupAvailableData.cartString == originData!!.cartString) {
                        appliedPromoList.add(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData!!.code)
                        break
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
                Unit
            }
            dialog?.setSecondaryCTAClickListener {
                dialog.dismiss()
                Unit
            }
            dialog?.show()
        } else {
            showToastMessageRed(getString(R.string.message_delete_empty_selection))
        }
    }

    override fun onGoToChuck() {
        startActivity(Chuck.getLaunchIntent(activity))
    }

    private fun checkGoToShipment(message: String?) {
        if (message == null || message == "") {
            val insuranceCartShopsArrayList = cartAdapter!!.isInsuranceCartProductUnSelected

            if (!insuranceCartShopsArrayList.isEmpty()) {
                deleteMacroInsurance(insuranceCartShopsArrayList, false)
            } else if (cartAdapter!!.isInsuranceSelected) {
                cartPageAnalytics!!.sendEventPurchaseInsurance(userSession!!.userId,
                        cartAdapter!!.selectedInsuranceProductId,
                        cartAdapter!!.selectedInsuranceProductTitle)
            }
            dPresenter!!.processToUpdateCartData(selectedCartDataList)
        } else {
            showToastMessageRed(message)
            sendAnalyticsOnButtonCheckoutClickedFailed()
            sendAnalyticsOnGoToShipmentFailed(message)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            setHasOptionsMenu(true)
            activity!!.title = activity!!.getString(R.string.title_activity_cart)
            if (savedInstanceState == null) {
                refreshHandler!!.startRefresh()
            } else {
                if (cartListData != null) {
                    dPresenter!!.cartListData = cartListData
                    renderLoadGetCartDataFinish()
                    renderInitialGetCartListDataSuccess(cartListData)
                    stopCartPerformanceTrace()
                } else {
                    refreshHandler!!.startRefresh()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveInstanceCacheManager != null) {
            saveInstanceCacheManager!!.onSave(outState)
            saveInstanceCacheManager!!.put(CartListData::class.java.simpleName, cartListData)
            if (wishLists != null) {
                saveInstanceCacheManager!!.put(CartWishlistItemHolderData::class.java.simpleName, ArrayList(wishLists!!))
            }
            if (recentViewList != null) {
                saveInstanceCacheManager!!.put(CartRecentViewItemHolderData::class.java.simpleName, ArrayList(recentViewList!!))
            }
            if (recommendationList != null) {
                saveInstanceCacheManager!!.put(CartRecommendationItemHolderData::class.java.simpleName, ArrayList(recommendationList!!))
            }
            if (recommendationSectionHeader != null) {
                saveInstanceCacheManager!!.put(CartSectionHeaderHolderData::class.java.simpleName, recommendationSectionHeader)
            }
        }
    }

    override fun onCartItemDeleteButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickRemoveIconCartItem()
        val appliedPromoCodes = ArrayList<String>()
        val cartShopHolderData = cartAdapter!!.getCartShopHolderDataByIndex(parentPosition)
        if (cartShopHolderData!!.shopGroupAvailableData.voucherOrdersItemData != null && !TextUtils.isEmpty(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData!!.code)) {
            appliedPromoCodes.add(cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData!!.code)
        }
        val cartItemDatas = ArrayList(listOf(cartItemHolderData.cartItemData))
        val allCartItemDataList = cartAdapter!!.allCartItemData

        val dialog: DialogUnify

        val macroInsurancePresent = !cartAdapter!!.insuranceCartShops.isEmpty()
        val removeAllItem = allCartItemDataList.size == cartItemDatas.size
        val removeMacroInsurance = macroInsurancePresent && removeAllItem

        if (removeMacroInsurance) {
            dialog = insuranceDialogDeleteConfirmation
            dialog.setPrimaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter!!.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, true, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(
                            dPresenter!!.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    )
                }
                dialog.dismiss()
                Unit
            }
            dialog.setSecondaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter!!.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter!!.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    )
                }
                dialog.dismiss()
                Unit
            }

        } else {
            dialog = dialogDeleteConfirmation
            dialog.setPrimaryCTAClickListener {
                if (cartItemDatas.size > 0) {
                    dPresenter!!.processDeleteCartItem(allCartItemDataList, cartItemDatas, appliedPromoCodes, false, removeMacroInsurance)
                    sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                            dPresenter!!.generateCartDataAnalytics(
                                    cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                            )
                    )
                }
                dialog.dismiss()
                Unit
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
                Unit
            }
        }

        dialog.show()
    }

    override fun onCartItemQuantityPlusButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickButtonPlusCartItem()
        cartAdapter!!.increaseQuantity(position, parentPosition)
    }

    override fun onCartItemQuantityMinusButtonClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickButtonMinusCartItem()
        cartAdapter!!.decreaseQuantity(position, parentPosition)
    }

    override fun onCartItemQuantityReseted(position: Int, parentPosition: Int, needRefreshItemView: Boolean) {
        cartAdapter!!.resetQuantity(position, parentPosition)
    }

    override fun onCartItemProductClicked(cartItemHolderData: CartItemHolderData, position: Int, parentPosition: Int) {
        sendAnalyticsOnClickProductNameCartItem(cartItemHolderData.cartItemData.originData!!.productName)
        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, cartItemHolderData.cartItemData.originData!!.productId), NAVIGATION_PDP)
    }

    override fun onClickShopNow() {
        cartPageAnalytics!!.eventClickAtcCartClickBelanjaSekarangOnEmptyCart()
        RouteManager.route(activity, ApplinkConst.HOME)
    }

    override fun onShowAllItem(appLink: String) {
        RouteManager.route(activity, appLink)
    }

    private fun onErrorAddWishList(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter!!.notifyByProductId(productId, false)
        cartAdapter!!.notifyWishlist(productId, false)
        cartAdapter!!.notifyRecentView(productId, false)
        cartAdapter!!.notifyRecommendation(productId, false)
    }

    private fun onSuccessAddWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_add_wishlist_success))
        cartAdapter!!.notifyByProductId(productId, true)
        cartAdapter!!.notifyWishlist(productId, true)
        cartAdapter!!.notifyRecentView(productId, true)
        cartAdapter!!.notifyRecommendation(productId, true)
    }

    private fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        showToastMessageRed(errorMessage)
        cartAdapter!!.notifyByProductId(productId, true)
        cartAdapter!!.notifyWishlist(productId, true)
        cartAdapter!!.notifyRecentView(productId, true)
        cartAdapter!!.notifyRecommendation(productId, true)
    }

    private fun onSuccessRemoveWishlist(productId: String) {
        showToastMessageGreen(getString(R.string.toast_message_remove_wishlist_success))
        cartAdapter!!.notifyByProductId(productId, false)
        cartAdapter!!.notifyWishlist(productId, false)
        cartAdapter!!.notifyRecentView(productId, false)
        cartAdapter!!.notifyRecommendation(productId, false)
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
                        cartPageAnalytics!!.eventClickAddWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics!!.eventClickAddWishlistOnProductRecommendation()
                    }
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    if (FLAG_IS_CART_EMPTY) {
                        cartPageAnalytics!!.eventClickRemoveWishlistOnProductRecommendationEmptyCart()
                    } else {
                        cartPageAnalytics!!.eventClickRemoveWishlistOnProductRecommendation()
                    }
                }
            }
        }
        return recommendationWishlistActionListener
    }

    private fun getCartAvailableWishlistActionListener(): WishListActionListener {
        if (cartAvailableWishlistActionListener == null) {
            cartAvailableWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics!!.eventAddWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics!!.eventRemoveWishlistAvailableSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return cartAvailableWishlistActionListener
    }

    private fun getCartUnavailableWishlistActionListener(): WishListActionListener {
        if (cartUnavailableWishlistActionListener == null) {
            cartUnavailableWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics!!.eventAddWishlistUnavailableSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics!!.eventRemoveWishlistUnvailableSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return cartUnavailableWishlistActionListener
    }

    private fun getLastSeenWishlistActionListener(): WishListActionListener {
        if (lastSeenWishlistActionListener == null) {
            lastSeenWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics!!.eventAddWishlistLastSeenSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics!!.eventRemoveWishlistLastSeenSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return lastSeenWishlistActionListener
    }

    private fun getWishlistsWishlistActionListener(): WishListActionListener {
        if (wishlistsWishlistActionListener == null) {
            wishlistsWishlistActionListener = object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorAddWishList(errorMessage, productId)
                }

                override fun onSuccessAddWishlist(productId: String) {
                    this@CartFragment.onSuccessAddWishlist(productId)
                    cartPageAnalytics!!.eventAddWishlistWishlistsSection(FLAG_IS_CART_EMPTY, productId)
                }

                override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
                    this@CartFragment.onErrorRemoveWishlist(errorMessage, productId)
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    this@CartFragment.onSuccessRemoveWishlist(productId)
                    cartPageAnalytics!!.eventRemoveWishlistWishlistsSection(FLAG_IS_CART_EMPTY, productId)
                }
            }
        }
        return wishlistsWishlistActionListener
    }

    override fun onAddDisabledItemToWishlist(productId: String) {
        dPresenter!!.processAddToWishlist(productId, userSession!!.userId, getCartUnavailableWishlistActionListener())
    }

    override fun onAddLastSeenToWishlist(productId: String) {
        dPresenter!!.processAddToWishlist(productId, userSession!!.userId, getLastSeenWishlistActionListener())
    }

    override fun onAddWishlistToWishlist(productId: String) {
        dPresenter!!.processAddToWishlist(productId, userSession!!.userId, getWishlistsWishlistActionListener())
    }

    override fun onAddRecommendationToWishlist(productId: String) {
        dPresenter!!.processAddToWishlist(productId, userSession!!.userId, getRecommendationWishlistActionListener())
    }

    override fun onRemoveDisabledItemFromWishlist(productId: String) {
        dPresenter!!.processRemoveFromWishlist(productId, userSession!!.userId, getCartUnavailableWishlistActionListener())
    }

    override fun onRemoveLastSeenFromWishlist(productId: String) {
        dPresenter!!.processRemoveFromWishlist(productId, userSession!!.userId, getLastSeenWishlistActionListener())
    }

    override fun onRemoveWishlistFromWishlist(productId: String) {
        dPresenter!!.processRemoveFromWishlist(productId, userSession!!.userId, getWishlistsWishlistActionListener())
    }

    override fun onRemoveRecommendationFromWishlist(productId: String) {
        dPresenter!!.processRemoveFromWishlist(productId, userSession!!.userId, getRecommendationWishlistActionListener())
    }

    private fun onProductClicked(productId: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.PRODUCT_INFO, productId)
        startActivityForResult(intent, NAVIGATION_PDP)
    }

    override fun onWishlistProductClicked(productId: String) {
        var position = 0

        for (wishlist in wishLists!!) {
            if (wishlist.id.equals(productId, ignoreCase = true)) {
                if (FLAG_IS_CART_EMPTY) {
                    sendAnalyticsOnClickProductWishlistOnEmptyCart(position.toString(),
                            dPresenter!!.generateWishlistProductClickEmptyCartDataLayer(wishlist, position))
                } else {
                    sendAnalyticsOnClickProductWishlistOnCartList(position.toString(),
                            dPresenter!!.generateWishlistProductClickDataLayer(wishlist, position))
                }
            }
            position++
        }

        onProductClicked(productId)
    }

    override fun onRecentViewProductClicked(productId: String) {
        var position = 0

        for (recentView in recentViewList!!) {
            if (recentView.id.equals(productId, ignoreCase = true)) {
                if (FLAG_IS_CART_EMPTY) {
                    sendAnalyticsOnClickProductRecentViewOnEmptyCart(position.toString(),
                            dPresenter!!.generateRecentViewProductClickEmptyCartDataLayer(recentView, position))
                } else {
                    sendAnalyticsOnClickProductRecentViewOnCartList(position.toString(),
                            dPresenter!!.generateRecentViewProductClickDataLayer(recentView, position))
                }
            }
            position++
        }

        onProductClicked(productId)
    }

    override fun onRecommendationProductClicked(productId: String) {
        var index = 1
        var recommendationItemClick: RecommendationItem? = null
        for ((recommendationItem) in recommendationList!!) {
            if (recommendationItem.productId.toString().equals(productId, ignoreCase = true)) {
                recommendationItemClick = recommendationItem
                break
            }
            index++
        }

        if (recommendationItemClick != null) {
            sendAnalyticsOnClickProductRecommendation(index.toString(),
                    dPresenter!!.generateRecommendationDataOnClickAnalytics(recommendationItemClick, FLAG_IS_CART_EMPTY, index))
        }

        onProductClicked(productId)
    }

    override fun onButtonAddToCartClicked(productModel: Any) {
        dPresenter!!.processAddToCart(productModel)
    }

    override fun onShowTickerOutOfStock(productId: String) {
        cartPageAnalytics!!.eventViewTickerOutOfStock(productId)
    }

    override fun onSimilarProductUrlClicked(similarProductUrl: String) {
        RouteManager.route(context, similarProductUrl)
        cartPageAnalytics!!.eventClickMoreLikeThis()
    }

    override fun onSelectAllClicked() {
        val checked = !dPresenter!!.cartListData.isAllSelected
        if (checked) {
            sendAnalyticsOnButtonSelectAllChecked()
        } else {
            sendAnalyticsOnButtonSelectAllUnchecked()
        }
        dPresenter!!.cartListData.isAllSelected = checked
        cbSelectAll!!.isChecked = checked
        cartAdapter!!.setAllShopSelected(checked)
        dPresenter!!.setAllInsuranceProductsChecked(cartAdapter!!.insuranceCartShops, checked)
        cartAdapter!!.notifyDataSetChanged()
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
    }

    override fun onSeeErrorProductsClicked() {
        if (cartRecyclerView!!.layoutManager != null) {
            val linearSmoothScroller = object : LinearSmoothScroller(cartRecyclerView!!.context) {
                override fun getVerticalSnapPreference(): Int {
                    return LinearSmoothScroller.SNAP_TO_START
                }
            }
            linearSmoothScroller.targetPosition = cartAdapter!!.disabledItemHeaderPosition
            cartRecyclerView!!.layoutManager!!.startSmoothScroll(linearSmoothScroller)
        }
    }

    override fun onShowCartTicker(tickerId: String) {
        cartPageAnalytics!!.eventViewInformationAndWarningTickerInCart(tickerId)
    }

    override fun getDefaultCartErrorMessage(): String {
        return if (isAdded) {
            getString(R.string.cart_error_message_no_count)
        } else {
            ""
        }
    }

    override fun onCartShopNameClicked(cartShopHolderData: CartShopHolderData) {
        sendAnalyticsOnClickShopCartItem(cartShopHolderData.shopGroupAvailableData.shopId, cartShopHolderData.shopGroupAvailableData.shopName)

        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP, cartShopHolderData.shopGroupAvailableData.shopId)
            activity!!.startActivity(intent)
        }
    }

    override fun onShopItemCheckChanged(itemPosition: Int, checked: Boolean) {
        dPresenter!!.setHasPerformChecklistChange()
        cartAdapter!!.setShopSelected(itemPosition, checked)
        cartAdapter!!.notifyDataSetChanged()
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
        cartAdapter!!.checkForShipmentForm()
    }

    override fun onCartPromoSuggestionButtonCloseClicked(data: CartPromoSuggestionHolderData, position: Int) {
        data.isVisible = false
        cartAdapter!!.notifyItemChanged(position)
        cartAdapter!!.checkForShipmentForm()
    }

    override fun onCartPromoUseVoucherGlobalPromoClicked(cartPromoGlobal: PromoStackingData, position: Int) {
        val cartItemData = selectedCartDataList
        if (cartItemData != null && cartItemData.size > 0) {
            trackingPromoCheckoutUtil!!.cartClickUseTickerPromoOrCoupon()
            dPresenter!!.processUpdateCartDataPromoStacking(cartItemData, cartPromoGlobal, GO_TO_LIST)
        } else {
            showToastMessageRed(getString(R.string.checkout_module_label_promo_no_item_checked))
        }
    }

    override fun onVoucherMerchantPromoClicked(`object`: Any) {
        if (`object` is ShopGroupAvailableData) {
            cartPageAnalytics!!.eventClickPilihMerchantVoucher()
            dPresenter!!.processUpdateCartDataPromoMerchant(selectedCartDataList, `object`)
        }
    }

    override fun onCartPromoCancelVoucherPromoGlobalClicked(cartPromoGlobal: PromoStackingData, position: Int) {
        val promoCodes = ArrayList<String>()
        promoCodes.add(cartPromoGlobal.promoCode)
        dPresenter!!.processCancelAutoApplyPromoStack(SHOP_INDEX_PROMO_GLOBAL, promoCodes, false)
    }

    override fun onCancelVoucherMerchantClicked(promoMerchantCode: String, shopIndex: Int, ignoreAPIResponse: Boolean) {
        cartPageAnalytics!!.eventClickHapusPromoXOnTicker(promoMerchantCode)
        val promoMerchantCodes = ArrayList<String>()
        promoMerchantCodes.add(promoMerchantCode)
        dPresenter!!.processCancelAutoApplyPromoStack(shopIndex, promoMerchantCodes, ignoreAPIResponse)
    }

    override fun onPromoGlobalTrackingImpression(cartPromoGlobal: PromoStackingData) {
        trackingPromoCheckoutUtil!!.cartImpressionTicker(cartPromoGlobal.getPromoCodeSafe())
    }

    override fun onPromoGlobalTrackingCancelled(cartPromoGlobal: PromoStackingData, position: Int) {
        sendAnalyticsOnClickCancelPromoCodeAndCouponBanner()
    }

    override fun onClickDetailPromoGlobal(dataGlobal: PromoStackingData, position: Int) {
        val cartItemData = selectedCartDataList
        if (cartItemData != null && cartItemData.size > 0) {
            trackingPromoCheckoutUtil!!.cartClickUseTickerPromoOrCoupon()
            dPresenter!!.processUpdateCartDataPromoStacking(cartItemData, dataGlobal, GO_TO_DETAIL)
        }
    }

    override fun onCartDataEnableToCheckout() {
        if (isAdded && btnToShipment != null) {
            btnToShipment!!.setOnClickListener { view -> checkGoToShipment("") }
        }
    }

    override fun onCartDataDisableToCheckout() {
        if (isAdded && btnToShipment != null) {
            btnToShipment!!.setOnClickListener { view -> checkGoToShipment(getString(R.string.message_checkout_empty_selection)) }
        }
    }

    override fun onCartItemAfterErrorChecked() {
        cartAdapter!!.checkForShipmentForm()
    }

    override fun onCartItemQuantityInputFormClicked(qty: String) {
        sendAnalyticsOnClickQuantityCartItemInput(qty)
    }

    override fun onCartItemLabelInputRemarkClicked() {
        sendAnalyticsOnClickCreateNoteCartItem()
    }

    override fun onCartItemCheckChanged(position: Int, parentPosition: Int, checked: Boolean): Boolean {
        dPresenter!!.setHasPerformChecklistChange()
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
        cartAdapter!!.checkForShipmentForm()
        return cartAdapter!!.setItemSelected(position, parentPosition, checked)
    }

    override fun onWishlistCheckChanged(productId: String, isChecked: Boolean) {
        if (activity != null) {
            if (isChecked) {
                dPresenter!!.processAddToWishlist(productId, userSession!!.userId, getCartAvailableWishlistActionListener())
            } else {
                dPresenter!!.processRemoveFromWishlist(productId, userSession!!.userId, getCartAvailableWishlistActionListener())
            }
        }
    }

    override fun onNeedToRefreshSingleShop(parentPosition: Int) {
        cartAdapter!!.notifyItemChanged(parentPosition)
    }

    override fun onNeedToRefreshMultipleShop() {
        cartAdapter!!.notifyDataSetChanged()
    }

    override fun onNeedToRecalculate() {
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
    }

    override fun onCartItemShowTickerPriceDecrease(productId: String) {
        cartPageAnalytics!!.eventViewTickerPriceDecrease(productId)
    }

    override fun onCartItemShowTickerStockDecreaseAndAlreadyAtcByOtherUser(productId: String) {
        cartPageAnalytics!!.eventViewTickerStockDecreaseAndAlreadyAtcByOtherUser(productId)
    }

    override fun onCartItemShowTickerOutOfStock(productId: String) {
        cartPageAnalytics!!.eventViewTickerOutOfStock(productId)
    }

    override fun onCartItemSimilarProductUrlClicked(similarProductUrl: String) {
        RouteManager.route(context, similarProductUrl)
        cartPageAnalytics!!.eventClickMoreLikeThis()
    }

    override fun showProgressLoading() {
        if (progressDialog != null && !progressDialog!!.isShowing) progressDialog!!.show()
    }

    override fun hideProgressLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
        if (refreshHandler!!.isRefreshing) {
            refreshHandler!!.finishRefresh()
        }
    }

    private fun showToastMessage(message: String) {
        val view = view
        if (view != null)
            NetworkErrorHelper.showSnackbar(activity, message)
        else
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun renderInitialGetCartListDataSuccess(cartListData: CartListData?) {
        if (cartListData != null) {
            endlessRecyclerViewScrollListener!!.resetState()

            sendAnalyticsScreenName(screenName)

            if (refreshHandler != null) {
                refreshHandler!!.finishRefresh()
            }

            this.cartListData = cartListData
            cartAdapter!!.resetData()

            renderTickerAnnouncement(cartListData)

            if (cartListData.shopGroupAvailableDataList.isEmpty() && cartListData.shopGroupWithErrorDataList.isEmpty()) {
                renderCartEmpty(cartListData)
            } else {
                renderCartNotEmpty(cartListData)
            }

            if (recentViewList == null) {
                dPresenter!!.processGetRecentViewData()
            } else {
                renderRecentView(null)
            }

            if (wishLists == null) {
                dPresenter!!.processGetWishlistData()
            } else {
                renderWishlist(null)
            }

            if (recommendationList == null) {
                dPresenter!!.processGetRecommendationData(endlessRecyclerViewScrollListener!!.currentPage, cartAdapter!!.allCartItemProductId)
            } else {
                renderRecommendation(null)
            }
        }
    }

    private fun renderCartNotEmpty(cartListData: CartListData) {
        FLAG_IS_CART_EMPTY = false
        cartAdapter!!.removeCartEmptyData()

        val promoStackingData = getPromoGlobalData(cartListData)
        renderPromoGlobal(promoStackingData)
        renderTickerError(cartListData)
        renderCartAvailable(cartListData)
        renderCartNotAvailable(cartListData)
        loadMacroInsurance(cartListData)

        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
        if (cbSelectAll != null) {
            cbSelectAll!!.isChecked = cartListData.isAllSelected
        }

        cartAdapter!!.checkForShipmentForm()
        if (cartRecyclerView!!.itemDecorationCount == 0) {
            cartRecyclerView!!.addItemDecoration(cartItemDecoration!!)
        }
        if (activity != null) {
            llCartContainer!!.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.checkout_module_color_background))
        }

        cartPageAnalytics!!.eventViewCartListFinishRender()
        val cartItemDataList = cartAdapter!!.allCartItemData
        cartPageAnalytics!!.enhancedECommerceCartLoadedStep0(
                dPresenter!!.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_0)
        )

        cartAdapter!!.notifyDataSetChanged()
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

        if (cartRecyclerView!!.itemDecorationCount > 0) {
            cartRecyclerView!!.removeItemDecoration(cartItemDecoration!!)
        }
        if (activity != null) {
            llCartContainer!!.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.white))
        }

        cartAdapter!!.notifyDataSetChanged()
    }

    private fun renderTickerAnnouncement(cartListData: CartListData) {
        val tickerData = cartListData.tickerData
        if (tickerData != null && tickerData.isValid(CART_PAGE)) {
            cartAdapter!!.addCartTicker(TickerAnnouncementHolderData(tickerData.id.toString(), tickerData.message))
        }
    }

    private fun renderPromoGlobal(promoStackingData: PromoStackingData) {
        cartAdapter!!.addPromoStackingVoucherData(promoStackingData)
        if (promoStackingData.state !== TickerPromoStackingCheckoutView.State.FAILED) {
            onPromoGlobalTrackingImpression(promoStackingData)
        }
    }

    private fun renderTickerError(cartListData: CartListData) {
        if (cartListData.isError) {
            val cartItemTickerErrorHolderData = CartItemTickerErrorHolderData()
            cartItemTickerErrorHolderData.cartTickerErrorData = cartListData.cartTickerErrorData
            cartAdapter!!.addCartTickerError(cartItemTickerErrorHolderData)
        }
    }

    private fun renderCartAvailable(cartListData: CartListData) {
        cartAdapter!!.addAvailableDataList(cartListData.shopGroupAvailableDataList)
    }

    private fun renderCartNotAvailable(cartListData: CartListData) {
        if (cartListData.shopGroupWithErrorDataList.size > 0) {
            cartAdapter!!.addNotAvailableHeader(viewHolderDataMapper!!.mapDisabledItemHeaderHolderData(
                    cartListData.cartTickerErrorData!!.errorCount))
            for (shopGroupWithErrorData in cartListData.shopGroupWithErrorDataList) {
                val cartItemHolderDataList = shopGroupWithErrorData.cartItemHolderDataList
                if (cartItemHolderDataList.size > 0) {
                    cartAdapter!!.addNotAvailableShop(viewHolderDataMapper!!.mapDisabledShopHolderData(shopGroupWithErrorData))
                    for ((index, value) in cartItemHolderDataList.withIndex()) {
                        cartAdapter!!.addNotAvailableProduct(viewHolderDataMapper!!.mapDisabledItemHolderData(value, index != cartItemHolderDataList.size - 1))
                    }
                }
            }
        }
    }

    private fun renderEmptyCartPlaceholder() {
        cartAdapter!!.addCartEmptyData()
    }

    private fun getPromoGlobalData(cartListData: CartListData): PromoStackingData {
        var flagAutoApplyStack = false
        val builderGlobal = PromoStackingData.Builder()
        if (cartListData.autoApplyStackData != null && cartListData.autoApplyStackData!!.isSuccess
                && !TextUtils.isEmpty(cartListData.autoApplyStackData!!.code)) {
            val autoApplyStackData = cartListData.autoApplyStackData
            if (autoApplyStackData != null) {
                if (autoApplyStackData.messageSuccess != null && autoApplyStackData.code != null
                        && autoApplyStackData.state != null && autoApplyStackData.titleDescription != null) {
                    builderGlobal.typePromo(if (autoApplyStackData.isCoupon == PromoStackingData.VALUE_COUPON)
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
            if (cartListData.globalCouponAttrData != null) {
                if (cartListData.globalCouponAttrData!!.description != null) {
                    if (!cartListData.globalCouponAttrData!!.description.isEmpty()) {
                        builderGlobal.title(cartListData.globalCouponAttrData!!.description)
                        builderGlobal.titleDefault(cartListData.globalCouponAttrData!!.description)
                    }
                }

                if (cartListData.globalCouponAttrData!!.quantityLabel != null) {
                    if (!cartListData.globalCouponAttrData!!.quantityLabel.isEmpty()) {
                        builderGlobal.counterLabel(cartListData.globalCouponAttrData!!.quantityLabel)
                        builderGlobal.counterLabelDefault(cartListData.globalCouponAttrData!!.quantityLabel)
                    }
                }
            }
        }

        return builderGlobal.build()
    }

    private fun loadMacroInsurance(cartListData: CartListData) {
        if (!cartListData.shopGroupAvailableDataList.isEmpty() && isInsuranceEnabled) {
            dPresenter!!.getInsuranceTechCart()
        }
    }

    override fun stopCartPerformanceTrace() {
        if (cartPerformanceMonitoring != null && !isTraceCartStopped) {
            cartPerformanceMonitoring!!.stopTrace()
            isTraceCartStopped = true
        }
    }

    override fun stopAllCartPerformanceTrace() {
        if (cartAllPerformanceMonitoring != null && !isTraceCartAllStopped && hasTriedToLoadRecentViewList && hasTriedToLoadWishList && hasTriedToLoadRecommendation) {
            cartAllPerformanceMonitoring!!.stopTrace()
            isTraceCartAllStopped = true
        }
    }

    private fun showErrorLayout(message: String) {
        if (activity != null) {
            enableSwipeRefresh()
            activity!!.invalidateOptionsMenu()
            refreshHandler!!.finishRefresh()
            showErrorContainer()
            onContentAvailabilityChanged(false)
            NetworkErrorHelper.showEmptyState(activity, llNetworkErrorView, message
            ) {
                llNetworkErrorView!!.visibility = View.GONE
                rlContent!!.visibility = View.VISIBLE
                refreshHandler!!.isRefreshing = true
                cartAdapter!!.resetData()
                dPresenter!!.processInitialGetCartData(cartId, dPresenter!!.cartListData == null, false)
            }
        }
    }

    private fun showMainContainerLoadingInitData() {
        llNetworkErrorView!!.visibility = View.GONE
        rlContent!!.visibility = View.VISIBLE
        bottomLayout!!.visibility = View.GONE
        bottomLayoutShadow!!.visibility = View.GONE
        cardHeader!!.visibility = View.GONE
    }

    private fun showMainContainer() {
        llNetworkErrorView!!.visibility = View.GONE
        rlContent!!.visibility = View.VISIBLE
        bottomLayout!!.visibility = View.VISIBLE
        bottomLayoutShadow!!.visibility = View.VISIBLE
        cardHeader!!.visibility = View.VISIBLE
    }

    private fun showErrorContainer() {
        rlContent!!.visibility = View.GONE
        llNetworkErrorView!!.visibility = View.VISIBLE
        bottomLayout!!.visibility = View.GONE
        bottomLayoutShadow!!.visibility = View.GONE
        cardHeader!!.visibility = View.GONE
    }

    private fun showEmptyCartContainer() {
        llNetworkErrorView!!.visibility = View.GONE
        bottomLayout!!.visibility = View.GONE
        bottomLayoutShadow!!.visibility = View.GONE
        cardHeader!!.visibility = View.GONE
        onContentAvailabilityChanged(false)
    }

    private fun showSnackbarRetry(message: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, message) { dPresenter!!.processInitialGetCartData(cartId, dPresenter!!.cartListData == null, false) }
                .showRetrySnackbar()
    }

    override fun renderErrorInitialGetCartListData(message: String) {
        if (cartAdapter!!.itemCount > 0) {
            showSnackbarRetry(message)
        } else {
            showErrorLayout(message)
        }
    }

    override fun renderToShipmentFormSuccess(eeCheckoutData: Map<String, Any>,
                                             cartItemDataList: List<CartItemData>,
                                             checkoutProductEligibleForCashOnDelivery: Boolean,
                                             checklistCondition: Int) {
        when (checklistCondition) {
            CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES -> cartPageAnalytics!!.enhancedECommerceGoToCheckoutStep1SuccessDefault(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES -> cartPageAnalytics!!.enhancedECommerceGoToCheckoutStep1SuccessCheckAll(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP -> cartPageAnalytics!!.enhancedECommerceGoToCheckoutStep1SuccessPartialShop(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM -> cartPageAnalytics!!.enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
            CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM -> cartPageAnalytics!!.enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(eeCheckoutData, checkoutProductEligibleForCashOnDelivery)
        }
        navigateToShipmentPage()
    }

    private fun navigateToShipmentPage() {
        FLAG_BEGIN_SHIPMENT_PROCESS = true
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = true
        val isAutoApplyPromoStackCodeApplied = dPresenter!!.cartListData != null &&
                dPresenter!!.cartListData.autoApplyStackData != null &&
                dPresenter!!.cartListData.autoApplyStackData!!.isSuccess
        val intent = ShipmentActivity.createInstance(activity, cartAdapter!!.promoStackingGlobalData,
                cartListData!!.cartPromoSuggestionHolderData, cartListData!!.defaultPromoDialogTab,
                isAutoApplyPromoStackCodeApplied
        )
        startActivityForResult(intent, ShipmentActivity.REQUEST_CODE)
    }

    private fun clearRecyclerView() {
        cartAdapter!!.unsubscribeSubscription()
        cartRecyclerView!!.adapter = null
        cartAdapter = CartAdapter(null, null, null, null, null)
        cartRecyclerView!!.removeAllViews()
        cartRecyclerView!!.recycledViewPool.clear()
    }

    override fun renderErrorToShipmentForm(message: String) {
        sendAnalyticsOnButtonCheckoutClickedFailed()
        sendAnalyticsOnGoToShipmentFailed(message)
        showToastMessageRed(message)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            refreshHandler!!.isRefreshing = true
            if (dPresenter!!.cartListData == null) {
                dPresenter!!.processInitialGetCartData(cartId, true, false)
            } else {
                if (dPresenter!!.dataHasChanged()) {
                    dPresenter!!.processToUpdateAndReloadCartData()
                } else {
                    dPresenter!!.processInitialGetCartData(cartId, false, true)
                }
            }
        }
    }

    private fun disableSwipeRefresh() {
        refreshHandler!!.setPullEnabled(false)
    }

    private fun enableSwipeRefresh() {
        refreshHandler!!.setPullEnabled(true)
    }

    override fun getAllCartDataList(): List<CartItemData> {
        return cartAdapter!!.allCartItemData
    }

    override fun getAllAvailableCartDataList(): List<CartItemData> {
        return cartAdapter!!.allAvailableCartItemData
    }

    override fun getAllShopDataList(): List<CartShopHolderData> {
        return cartAdapter!!.allShopGroupDataList
    }

    override fun renderDetailInfoSubTotal(qty: String, subtotalPrice: String, selectAllCartItem: Boolean, unselectAllItem: Boolean, hasAvailableItems: Boolean) {
        if (dPresenter!!.cartListData != null) {
            dPresenter!!.cartListData.isAllSelected = selectAllCartItem
        }
        if (cbSelectAll!!.isChecked != selectAllCartItem) {
            cbSelectAll!!.isChecked = selectAllCartItem
        }
        btnRemove!!.visibility = if (unselectAllItem) View.INVISIBLE else View.VISIBLE
        cardHeader!!.visibility = if (hasAvailableItems) View.GONE else View.VISIBLE
        tvTotalPrice!!.text = subtotalPrice
        btnToShipment!!.text = String.format(getString(R.string.cart_item_button_checkout_count_format), qty)
    }

    override fun updateCashback(cashback: Double) {
        cartAdapter!!.updateShipmentSellerCashback(cashback)
    }

    override fun goToCouponList() {
        val promo = dPresenter!!.generateCheckPromoFirstStepParam(cartAdapter!!.promoStackingGlobalData)
        val intent = getIntentToPromoList(promo)
        startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun getIntentToPromoList(promo: Promo): Intent {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_MARKETPLACE)
        val bundle = Bundle()
        bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, cartListData!!.isPromoCouponActive)
        bundle.putString(PROMO_CODE, "")
        bundle.putBoolean(ONE_CLICK_SHIPMENT, false)
        bundle.putInt(PAGE_TRACKING, FROM_CART)
        bundle.putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, promo)
        intent.putExtras(bundle)
        return intent
    }

    private fun getIntentToPromoDetail(promo: Promo, promoStackingData: PromoStackingData): Intent {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE)
        intent.putExtra(EXTRA_KUPON_CODE, promoStackingData.getPromoCodeSafe())
        intent.putExtra(EXTRA_IS_USE, true)
        intent.putExtra(ONE_CLICK_SHIPMENT, false)
        intent.putExtra(PAGE_TRACKING, FROM_CART)
        intent.putExtra(CHECK_PROMO_CODE_FIRST_STEP_PARAM, promo)
        return intent
    }

    override fun goToDetailPromoStacking(promoStackingData: PromoStackingData) {
        val promo = dPresenter!!.generateCheckPromoFirstStepParam(cartAdapter!!.promoStackingGlobalData)

        if (promoStackingData.typePromo == PromoStackingData.TYPE_COUPON) {
            val intent = getIntentToPromoDetail(promo, promoStackingData)
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
        } else {
            val intent = getIntentToPromoList(promo)
            startActivityForResult(intent, IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun showToastMessageRed(message: String) {
        var message = message
        if (TextUtils.isEmpty(message)) {
            message = "Terjadi kesalahan. Ulangi beberapa saat lagi"
        }
        val view = view
        if (view != null) {
            NetworkErrorHelper.showRedCloseSnackbar(view, message)
        } else if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showToastMessageGreen(message: String) {
        val view = view
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
        if (refreshHandler!!.isRefreshing) {
            refreshHandler!!.isRefreshing = false
        }
        showMainContainer()
        onContentAvailabilityChanged(true)
    }

    override fun onDeleteCartDataSuccess(deletedCartIds: List<Int>) {
        cartAdapter!!.removeCartItemById(deletedCartIds, context)
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
        notifyBottomCartParent()
    }

    override fun onRefresh(view: View) {
        if (dPresenter!!.dataHasChanged()) {
            showMainContainer()
            dPresenter!!.processToUpdateAndReloadCartData()
        } else {
            if (dPresenter!!.cartListData != null && dPresenter!!.cartListData.shopGroupAvailableDataList.size > 0) {
                showMainContainer()
            }
            dPresenter!!.processInitialGetCartData(cartId, cartListData == null, true)
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
                refreshHandler!!.isRefreshing = true
                dPresenter!!.processInitialGetCartData(cartId, cartListData == null, true)
            }
        }
    }

    private fun onResultFromRequestCodeCartShipment(resultCode: Int, data: Intent?) {
        if (cartRecyclerView!!.adapter == null) {
            cartAdapter = CartAdapter(this, this, this, this, this)
            cartRecyclerView!!.adapter = cartAdapter
        }
        FLAG_SHOULD_CLEAR_RECYCLERVIEW = false
        if (resultCode == PaymentConstant.PAYMENT_CANCELLED) {
            showToastMessageRed(getString(R.string.alert_payment_canceled_or_failed_transaction_module))
            dPresenter!!.processInitialGetCartData(cartId, false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_SUCCESS) {
            showToastMessageGreen(getString(R.string.message_payment_success))
            refreshHandler!!.isRefreshing = true
            dPresenter!!.processInitialGetCartData(cartId, false, false)
        } else if (resultCode == PaymentConstant.PAYMENT_FAILED) {
            showToastMessageRed(getString(R.string.default_request_error_unknown))
            sendAnalyticsScreenName(screenName)
            refreshHandler!!.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter!!.processInitialGetCartData(cartId, false, false)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            sendAnalyticsScreenName(screenName)
            refreshHandler!!.isRefreshing = true
            if (cartListData != null) {
                renderInitialGetCartListDataSuccess(cartListData)
            } else {
                dPresenter!!.processInitialGetCartData(cartId, false, false)
            }
        } else if (resultCode == ShipmentActivity.RESULT_CODE_COUPON_STATE_CHANGED) {
            refreshHandler!!.isRefreshing = true
            dPresenter!!.processInitialGetCartData(cartId, false, false)
        }
    }

    private fun onResultFromRequestCodeLoyalty(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            sendAnalyticsScreenName(screenName)
            val bundle = data!!.extras
            if (bundle != null) {
                val promoStackingData = bundle.getParcelable<PromoStackingData>(EXTRA_PROMO_DATA)

                if (cartListData != null && promoStackingData != null) {
                    if (cartListData!!.globalCouponAttrData != null) {
                        if (cartListData!!.globalCouponAttrData!!.description != null) {
                            if (!cartListData!!.globalCouponAttrData!!.description.isEmpty()) {
                                promoStackingData.titleDefault = cartListData!!.globalCouponAttrData!!.description
                            }
                        }

                        if (cartListData!!.globalCouponAttrData!!.quantityLabel != null) {
                            if (!cartListData!!.globalCouponAttrData!!.quantityLabel.isEmpty()) {
                                promoStackingData.counterLabelDefault = cartListData!!.globalCouponAttrData!!.quantityLabel
                            }
                        }
                    }
                }

                for ((originData) in cartAdapter!!.allCartItemData) {
                    if (promoStackingData != null && promoStackingData.trackingDetailUiModels.size > 0) {
                        for ((productId, promoCodesTracking, promoDetailsTracking) in promoStackingData.trackingDetailUiModels) {
                            if (productId.toString().equals(originData!!.productId!!, ignoreCase = true)) {
                                originData.promoCodes = promoCodesTracking
                                originData.promoDetails = promoDetailsTracking
                            }
                        }
                    }
                }

                if (promoStackingData != null) {
                    cartAdapter!!.updateItemPromoStackVoucher(promoStackingData)
                    if (promoStackingData.typePromo == PromoStackingData.TYPE_VOUCHER) {
                        sendAnalyticsOnViewPromoManualApply("voucher")
                    } else {
                        sendAnalyticsOnViewPromoManualApply("coupon")
                    }
                }
            }
        } else if (resultCode == RESULT_CLASHING) {
            val bundle = data!!.extras
            if (bundle != null) {
                val clashingInfoDetailUiModel = bundle.getParcelable<ClashingInfoDetailUiModel>(EXTRA_CLASHING_DATA)
                if (clashingInfoDetailUiModel != null) {
                    var type = bundle.getString(EXTRA_TYPE)
                    if (type == null) type = ""
                    onClashCheckPromo(clashingInfoDetailUiModel, type)
                }
            }
        }
    }

    override fun sendAnalyticsOnClickBackArrow() {
        cartPageAnalytics!!.eventClickAtcCartClickArrowBack()
    }

    override fun sendAnalyticsOnClickRemoveButtonHeader() {
        cartPageAnalytics!!.eventClickAtcCartClickHapusOnTopRightCorner()
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartSelectedWithAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromTrashBin(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedECommerceRemoveFromCartClickHapusFromTrashBin(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickRemoveIconCartItem() {
        cartPageAnalytics!!.eventClickAtcCartClickTrashBin()
    }

    override fun sendAnalyticsOnClickButtonPlusCartItem() {
        cartPageAnalytics!!.eventClickAtcCartClickButtonPlus()
    }

    override fun sendAnalyticsOnClickButtonMinusCartItem() {
        cartPageAnalytics!!.eventClickAtcCartClickButtonMinus()
    }

    override fun sendAnalyticsOnClickProductNameCartItem(productName: String?) {
        cartPageAnalytics!!.eventClickAtcCartClickProductName(productName)
    }

    override fun sendAnalyticsOnClickShopCartItem(shopId: String?, shopName: String?) {
        cartPageAnalytics!!.eventClickAtcCartClickShop(shopId, shopName)
    }

    override fun sendAnalyticsOnClickCancelPromoCodeAndCouponBanner() {
        cartPageAnalytics!!.eventClickAtcCartClickXOnBannerPromoCode()
    }

    override fun sendAnalyticsOnClickRemoveCartConstrainedProduct(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductWithAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickConfirmationRemoveCartConstrainedProductNoAddToWishList(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickQuantityCartItemInput(quantity: String) {
        cartPageAnalytics!!.eventClickAtcCartClickInputQuantity(quantity)
    }

    override fun sendAnalyticsOnClickCreateNoteCartItem() {
        cartPageAnalytics!!.eventClickAtcCartClickTulisCatatan()
    }

    override fun sendAnalyticsOnDataCartIsEmpty() {
        cartPageAnalytics!!.eventViewAtcCartImpressionCartEmpty()
    }

    override fun sendAnalyticsScreenName(screenName: String) {
        cartPageAnalytics!!.sendScreenName(activity, screenName)
    }

    override fun getScreenName(): String {
        return ConstantTransactionAnalytics.ScreenName.CART
    }

    override fun sendAnalyticsOnButtonCheckoutClickedFailed() {
        cartPageAnalytics!!.eventClickCheckoutCartClickCheckoutFailed()
    }

    override fun sendAnalyticsOnGoToShipmentFailed(errorMessage: String) {
        cartPageAnalytics!!.eventViewErrorWhenCheckout(errorMessage)
    }

    override fun sendAnalyticsOnButtonSelectAllChecked() {
        cartPageAnalytics!!.eventClickCheckoutCartClickPilihSemuaProdukChecklist()
    }

    override fun sendAnalyticsOnButtonSelectAllUnchecked() {
        cartPageAnalytics!!.eventClickCheckoutCartClickPilihSemuaProdukUnChecklist()
    }

    override fun sendAnalyticsOnViewPromoManualApply(type: String) {
        cartPageAnalytics!!.eventViewPromoManualApply(type)
    }

    override fun sendAnalyticsOnViewProductRecommendation(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceViewRecommendationOnCart(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecommendation(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceClickProductRecommendationOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewProductWishlist(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceProductViewWishList(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductWishlistOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceClickProductWishListOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductWishlistOnCartList(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceClickProductWishListOnCartList(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewProductRecentView(eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceProductViewLastSeen(eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecentViewOnEmptyCart(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceClickProductLastSeenOnEmptyCart(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnClickProductRecentViewOnCartList(position: String, eeDataLayerCart: Map<String, Any>) {
        cartPageAnalytics!!.enhancedEcommerceClickProductLastSeenOnCartList(position, eeDataLayerCart)
    }

    override fun sendAnalyticsOnViewPromoAutoApply() {
        cartPageAnalytics!!.eventViewPromoAutoApply()
    }

    override fun notifyBottomCartParent() {
        if (activity is CartNotifyListener) {
            (activity as CartNotifyListener).onNotifyCart()
        }
    }

    override fun showMerchantVoucherListBottomsheet(shopGroupAvailableData: ShopGroupAvailableData) {
        val promo = dPresenter!!.generateCheckPromoFirstStepParam(cartAdapter!!.promoStackingGlobalData)
        if (fragmentManager != null) {
            var shopId = 0
            try {
                shopId = Integer.parseInt(shopGroupAvailableData.shopId!!)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            val cartItemDataVoucherArrayList = ArrayList<CartItemDataVoucher>()
            for ((cartItemData, _, _, _, _, isSelected) in shopGroupAvailableData.cartItemDataList!!) {
                if (isSelected) {
                    try {
                        val cartItemDataVoucher = CartItemDataVoucher()
                        cartItemDataVoucher.productId = Integer.parseInt(cartItemData.originData!!.productId!!)
                        cartItemDataVoucher.productName = cartItemData.originData!!.productName
                        cartItemDataVoucherArrayList.add(cartItemDataVoucher)
                    } catch (e: NumberFormatException) {
                        if (GlobalConfig.isAllowDebuggingTools()!!) {
                            e.printStackTrace()
                        }
                    }

                }
            }

            val merchantVoucherListBottomSheetFragment = MerchantVoucherListBottomSheetFragment.newInstance(shopId, shopGroupAvailableData.cartString!!, promo, "cart", cartItemDataVoucherArrayList)
            merchantVoucherListBottomSheetFragment.actionListener = this
            merchantVoucherListBottomSheetFragment.show(fragmentManager, "")
        }
    }

    override fun onClashCheckPromo(clashingInfoDetailUiModel: ClashingInfoDetailUiModel,
                                   type: String) {
        val clashBottomSheetFragment = ClashBottomSheetFragment.newInstance()
        clashBottomSheetFragment.setData(clashingInfoDetailUiModel)
        clashBottomSheetFragment.setActionListener(this)
        clashBottomSheetFragment.setAnalyticsCart(cartPageAnalytics!!)
        clashBottomSheetFragment.setSource("cart")
        clashBottomSheetFragment.setType(type)
        clashBottomSheetFragment.show(fragmentManager!!, "")
    }

    override fun getInsuranceCartShopData(): ArrayList<InsuranceCartDigitalProduct>? {
        try {
            val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
            for (insuranceCartShops in cartAdapter!!.insuranceCartShops) {
                if (insuranceCartShops != null && !insuranceCartShops.shopItemsList.isEmpty()) {
                    for ((_, digitalProductList) in insuranceCartShops.shopItemsList) {
                        if (digitalProductList != null && !digitalProductList.isEmpty()) {
                            for (insuranceCartDigitalProduct in digitalProductList) {
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
        if (cartAdapter != null) {
            cartAdapter!!.removeInsuranceDataItem(productId)
        }
    }

    override fun renderInsuranceCartData(insuranceCartResponse: InsuranceCartResponse?, isRecommendation: Boolean) {

        /*
         * render insurance cart data on ui, both micro and macro, if is_product_level == true,
         * then insurance product is of type micro insurance and should be tagged at product level,
         * for micro insurance product add insurance data in shopGroup list*/

        if (insuranceCartResponse != null && insuranceCartResponse.cartShopsList != null &&
                !insuranceCartResponse.cartShopsList.isEmpty()) {
            for (insuranceCartShops in insuranceCartResponse.cartShopsList) {
                val shopId = insuranceCartShops.shopId
                for ((productId, digitalProductList) in insuranceCartShops.shopItemsList) {
                    for (insuranceCartDigitalProduct in digitalProductList) {
                        insuranceCartDigitalProduct.shopId = shopId.toString()
                        insuranceCartDigitalProduct.productId = productId.toString()
                        if (!insuranceCartDigitalProduct.isProductLevel) {
                            cartAdapter!!.addInsuranceDataList(insuranceCartShops, isRecommendation)
                        }
                    }
                }
            }
            cartAdapter!!.notifyDataSetChanged()
        }

    }

    override fun onSuccessCheckPromoMerchantFirstStep(promoData: ResponseGetPromoStackUiModel, promoCode: String) {
        onSuccessCheckPromoFirstStep(promoData)
    }

    override fun onSuccessCheckPromoFirstStep(responseGetPromoStackUiModel: ResponseGetPromoStackUiModel) {
        // Update global promo state
        if (responseGetPromoStackUiModel.data.codes.size > 0) {
            val promoStackingGlobalData = cartAdapter!!.promoStackingGlobalData
            promoMapper!!.convertPromoGlobalModel(responseGetPromoStackUiModel, promoStackingGlobalData!!)
        }

        // Update merchant voucher state
        val cartShopHolderDataList = cartAdapter!!.allShopGroupDataList
        if (cartShopHolderDataList != null) {
            for (cartShopHolderData in cartShopHolderDataList) {
                for (voucherOrdersItemUiModel in responseGetPromoStackUiModel.data.voucherOrders) {
                    if (voucherOrdersItemUiModel.uniqueId == cartShopHolderData.shopGroupAvailableData.cartString) {
                        var voucherOrdersItemData = cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData
                        if (voucherOrdersItemData == null) {
                            voucherOrdersItemData = VoucherOrdersItemData()
                        }
                        promoMapper!!.convertPromoMerchantModel(voucherOrdersItemUiModel, voucherOrdersItemData)
                        cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData = voucherOrdersItemData
                        break
                    }
                }
                if (responseGetPromoStackUiModel.data.trackingDetailUiModel.size > 0) {
                    for (trackingDetailUiModel in responseGetPromoStackUiModel.data.trackingDetailUiModel) {
                        if (cartShopHolderData.shopGroupAvailableData.cartItemDataList != null) {
                            for ((cartItemData) in cartShopHolderData.shopGroupAvailableData.cartItemDataList!!) {
                                if (trackingDetailUiModel.productId.toString().equals(cartItemData.originData!!.productId!!, ignoreCase = true)) {
                                    cartItemData.originData!!.promoCodes = trackingDetailUiModel.promoCodesTracking
                                    cartItemData.originData!!.promoDetails = trackingDetailUiModel.promoDetailsTracking
                                }
                            }
                        }
                    }
                }

            }
        }

        cartAdapter!!.notifyDataSetChanged()
    }

    override fun onSuccessClearPromoStack(shopIndex: Int) {
        if (shopIndex == SHOP_INDEX_PROMO_GLOBAL) {
            if (cartListData!!.shopGroupAvailableDataList.isEmpty()) {
                cartAdapter!!.removePromoStackingVoucherData()
            } else {
                val promoStackingData = cartAdapter!!.promoStackingGlobalData
                resetPromoGlobal(promoStackingData!!)
                cartAdapter!!.updateItemPromoStackVoucher(promoStackingData)
            }
        } else {
            val cartShopHolderData = cartAdapter!!.getCartShopHolderDataByIndex(shopIndex)
            if (cartShopHolderData != null) {
                cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData = null
                cartAdapter!!.notifyItemChanged(shopIndex)
            }
        }
    }

    override fun onSuccessClearPromoStackAfterClash() {
        // Reset global promo
        val promoStackingData = cartAdapter!!.promoStackingGlobalData
        resetPromoGlobal(promoStackingData!!)

        // Reset merchant promo
        val cartShopHolderDataList = cartAdapter!!.allCartShopHolderData
        for (cartShopHolderData in cartShopHolderDataList) {
            if (cartShopHolderData != null) {
                cartShopHolderData.shopGroupAvailableData.voucherOrdersItemData = null
            }
        }

        cartAdapter!!.notifyDataSetChanged()
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
        dPresenter!!.processCancelAutoApplyPromoStackAfterClash(cartAdapter!!.promoStackingGlobalData, oldPromoList, newPromoList, type)
    }

    // get newly added cart id if open cart after ATC on PDP
    override fun getCartId(): String? {
        return if (arguments != null && !TextUtils.isEmpty(arguments!!.getString(CartActivity.EXTRA_CART_ID))) {
            arguments!!.getString(CartActivity.EXTRA_CART_ID)
        } else "0"
    }

    override fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops,
                                            updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>) {
        dPresenter!!.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList)
    }

    override fun sendEventDeleteInsurance(insuranceTitle: String) {
        cartPageAnalytics!!.sendEventDeleteInsurance(insuranceTitle)
    }

    override fun sendEventInsuranceImpression(title: String) {
        cartPageAnalytics!!.sendEventInsuranceImpression(title)
    }

    override fun sendEventInsuranceImpressionForShipment(title: String) {

    }

    override fun sendEventChangeInsuranceState(isChecked: Boolean, insuranceTitle: String) {
        cartPageAnalytics!!.sendEventChangeInsuranceState(isChecked, insuranceTitle)
    }

    override fun deleteMacroInsurance(insuranceCartDigitalProductArrayList: ArrayList<InsuranceCartDigitalProduct>, showConfirmationDialog: Boolean) {
        if (showConfirmationDialog) {
            val view = layoutInflater.inflate(R.layout.remove_insurance_product, null, false)
            val alertDialog = AlertDialog.Builder(context!!)
                    .setView(view)
                    .setCancelable(true)
                    .show()

            view.findViewById<View>(R.id.button_positive).setOnClickListener { v ->
                dPresenter!!.processDeleteCartInsurance(insuranceCartDigitalProductArrayList, showConfirmationDialog)
                alertDialog.dismiss()
            }

            view.findViewById<View>(R.id.button_negative).setOnClickListener { v -> alertDialog.dismiss() }
        } else {
            dPresenter!!.processDeleteCartInsurance(insuranceCartDigitalProductArrayList, showConfirmationDialog)
        }
    }

    override fun onInsuranceSelectStateChanges() {
        dPresenter!!.reCalculateSubTotal(cartAdapter!!.allShopGroupDataList, cartAdapter!!.insuranceCartShops)
    }

    override fun renderRecentView(recentViewList: List<RecentView>?) {
        var cartRecentViewItemHolderDataList: MutableList<CartRecentViewItemHolderData> = ArrayList()
        if (this.recentViewList != null) {
            cartRecentViewItemHolderDataList.addAll(this.recentViewList!!)
        } else if (recentViewList != null) {
            cartRecentViewItemHolderDataList = recentViewMapper!!.convertToViewHolderModelList(recentViewList)
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recent_view)

        val cartRecentViewHolderData = CartRecentViewHolderData()
        cartRecentViewHolderData.recentViewList = cartRecentViewItemHolderDataList
        cartAdapter!!.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.recentViewList = cartRecentViewItemHolderDataList

        sendAnalyticsOnViewProductRecentView(
                dPresenter!!.generateRecentViewDataImpressionAnalytics(cartRecentViewItemHolderDataList, FLAG_IS_CART_EMPTY)
        )
    }

    override fun renderWishlist(wishlists: List<Wishlist>?) {
        var cartWishlistItemHolderDataList: MutableList<CartWishlistItemHolderData> = ArrayList()
        if (this.wishLists != null) {
            cartWishlistItemHolderDataList.addAll(this.wishLists!!)
        } else if (wishlists != null) {
            cartWishlistItemHolderDataList = wishlistMapper!!.convertToViewHolderModelList(wishlists)
        }
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_wishlist)
        cartSectionHeaderHolderData.showAllAppLink = ApplinkConst.WISHLIST

        val cartRecentViewHolderData = CartWishlistHolderData()
        cartRecentViewHolderData.wishList = cartWishlistItemHolderDataList
        cartAdapter!!.addCartWishlistData(cartSectionHeaderHolderData, cartRecentViewHolderData)
        this.wishLists = cartWishlistItemHolderDataList

        sendAnalyticsOnViewProductWishlist(
                dPresenter!!.generateWishlistDataImpressionAnalytics(cartWishlistItemHolderDataList, FLAG_IS_CART_EMPTY)
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
            if (recommendationList != null && recommendationList!!.size != 0) {
                cartRecommendationItemHolderDataList.addAll(this.recommendationList!!)
            }
        }

        var cartSectionHeaderHolderData: CartSectionHeaderHolderData? = null
        if (endlessRecyclerViewScrollListener!!.currentPage == 0 && recommendationWidget == null || recommendationWidget != null && endlessRecyclerViewScrollListener!!.currentPage == 1 && recommendationSectionHeader == null) {
            if (recommendationSectionHeader != null) {
                cartSectionHeaderHolderData = recommendationSectionHeader
            } else {
                cartSectionHeaderHolderData = CartSectionHeaderHolderData()
                if (recommendationWidget != null && !TextUtils.isEmpty(recommendationWidget.title)) {
                    cartSectionHeaderHolderData.title = recommendationWidget.title
                } else {
                    cartSectionHeaderHolderData.title = getString(R.string.checkout_module_title_recommendation)
                }
                recommendationSectionHeader = cartSectionHeaderHolderData
            }
        }

        if (cartRecommendationItemHolderDataList.size > 0) {
            cartAdapter!!.addCartRecommendationData(cartSectionHeaderHolderData, cartRecommendationItemHolderDataList)
            recommendationList = cartRecommendationItemHolderDataList

            sendAnalyticsOnViewProductRecommendation(
                    dPresenter!!.generateRecommendationDataAnalytics(recommendationList, FLAG_IS_CART_EMPTY)
            )
        }
    }

    override fun showItemLoading() {
        cartAdapter!!.addCartLoadingData()
    }

    override fun hideItemLoading() {
        cartAdapter!!.removeCartLoadingData()
        endlessRecyclerViewScrollListener!!.updateStateAfterGetData()
        hasLoadRecommendation = true
    }

    override fun triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataResponseModel: AddToCartDataModel, productModel: Any) {
        var stringObjectMap: Map<String, Any>? = null
        var eventCategory = ""
        var eventAction = ""
        var eventLabel = ""
        if (productModel is CartWishlistItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_WISHLIST
            eventLabel = ""
            stringObjectMap = dPresenter!!.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
        } else if (productModel is CartRecentViewItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_BELI_ON_RECENT_VIEW_PAGE
            eventLabel = ""
            stringObjectMap = dPresenter!!.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
        } else if (productModel is CartRecommendationItemHolderData) {
            eventCategory = ConstantTransactionAnalytics.EventCategory.CART
            eventAction = ConstantTransactionAnalytics.EventAction.CLICK_ADD_TO_CART
            eventLabel = ""
            stringObjectMap = dPresenter!!.generateAddToCartEnhanceEcommerceDataLayer(productModel, addToCartDataResponseModel, FLAG_IS_CART_EMPTY)
        }

        if (stringObjectMap != null) {
            cartPageAnalytics!!.sendEnhancedECommerceAddToCart(stringObjectMap, eventCategory, eventAction, eventLabel)
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
            Unit
        }
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            Unit
        }
        dialog?.show()
    }

    override fun onDeleteDisabledItem(cartItemData: CartItemData) {
        if (cartItemData.nicotineLiteMessageData != null) {
            cartPageAnalytics!!.eventClickTrashIconButtonOnProductContainTobacco()
        } else {
            sendAnalyticsOnClickRemoveIconCartItem()
        }
        val cartItemDatas = listOf(cartItemData)
        val allCartItemDataList = cartAdapter!!.allCartItemData

        val dialog = disabledItemDialogDeleteConfirmation

        dialog.setPrimaryCTAClickListener {
            dPresenter!!.processDeleteCartItem(allCartItemDataList, cartItemDatas, null, false, false)
            sendAnalyticsOnClickConfirmationRemoveCartSelectedNoAddToWishList(
                    dPresenter!!.generateCartDataAnalytics(
                            cartItemDatas, EnhancedECommerceCartMapData.REMOVE_ACTION
                    )
            )
            dialog.dismiss()
            Unit
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            Unit
        }
        dialog.show()
    }

    override fun onTobaccoLiteUrlClicked(url: String) {
        cartPageAnalytics!!.eventClickBrowseButtonOnTickerProductContainTobacco()
        dPresenter!!.redirectToLite(url)
    }

    override fun onShowTickerTobacco() {
        cartPageAnalytics!!.eventViewTickerProductContainTobacco()
    }

    override fun goToLite(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {

        private val SHOP_INDEX_PROMO_GLOBAL = -1

        private val HAS_ELEVATION = 8
        private val NO_ELEVATION = 0
        private val CART_TRACE = "mp_cart"
        private val CART_ALL_TRACE = "mp_cart_all"
        private val CART_PAGE = "cart"
        private val NAVIGATION_PDP = 64728
        private val GO_TO_DETAIL = 2
        internal val GO_TO_LIST = 1

        fun newInstance(bundle: Bundle?, args: String): CartFragment {
            var bundle = bundle
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle.putString(CartFragment::class.java.simpleName, args)
            val fragment = CartFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
