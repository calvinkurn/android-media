package com.tokopedia.cartrevamp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.databinding.FragmentCartRevampBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.CartFragment
import com.tokopedia.cart.view.adapter.cart.CartAdapter
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.compoundview.CartToolbarListener
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartSelectAllHolderData
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.cartrevamp.view.bottomsheet.showSummaryTransactionBottomsheet
import com.tokopedia.cartrevamp.view.decorator.CartItemDecoration
import com.tokopedia.cartrevamp.view.di.DaggerCartRevampComponent
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rx.Subscriber
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
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }

    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var hasLoadRecommendation: Boolean = false

    private var recommendationPage = 1
    private var isCheckUncheckDirectAction = true

    private var delayShowPromoButtonJob: Job? = null
    private var TRANSLATION_LENGTH = 0f
    private var initialPromoButtonPosition = 0f

    companion object {
        private const val SPAN_SIZE_ZERO = 0
        private const val SPAN_SIZE_ONE = 1
        private const val SPAN_SIZE_TWO = 2

        const val DELAY_CHECK_BOX_GLOBAL = 500L
    }

    override fun initInjector() {
        activity?.let {
            val baseMainApplication = it.application as BaseMainApplication
            DaggerCartRevampComponent.builder().baseAppComponent(baseMainApplication.baseAppComponent)
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

    override fun getFragment(): Fragment {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onCartDataDisableToCheckout() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onRecentViewProductClicked(productId: String) {
        TODO("Not yet implemented")
    }

    override fun onRecentViewImpression() {
        TODO("Not yet implemented")
    }

    override fun onRecommendationProductClicked(recommendationItem: RecommendationItem) {
        TODO("Not yet implemented")
    }

    override fun onRecommendationProductImpression(recommendationItem: RecommendationItem) {
        TODO("Not yet implemented")
    }

    override fun onRecommendationImpression(recommendationItem: CartRecommendationItemHolderData) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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

    private fun animatePromoButtonToHiddenPosition(valueY: Float) {
        binding?.llPromoCheckout?.animate()?.y(valueY)?.setDuration(0)?.start()
    }

    private fun animatePromoButtonToStartingPosition() {
        binding?.apply {
            val initialPosition =
                bottomLayout.y - llPromoCheckout.height + CartFragment.PROMO_POSITION_BUFFER.dpToPx(resources.displayMetrics)
            llPromoCheckout.animate().y(initialPosition).setDuration(0).start()
        }
    }

    private fun checkGoToShipment(message: String?) {
//        if (message.isNullOrEmpty()) {
//            val redStatePromoTripleData = getRedStatePromo()
//            val hasRedStatePromo = redStatePromoTripleData.first
//            val redStateGlobalPromo = redStatePromoTripleData.second
//            val clearOrders = redStatePromoTripleData.third
//
//            val clearPromo = ClearPromoRequest(
//                ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
//                orderData = ClearPromoOrderData(redStateGlobalPromo, clearOrders)
//            )
//            if (hasRedStatePromo) {
//                dPresenter.doClearRedPromosBeforeGoToCheckout(clearPromo)
//            } else {
//                goToCheckoutPage()
//            }
//        } else {
//            showToastMessageRed(message)
//            cartPageAnalytics.eventClickCheckoutCartClickCheckoutFailed()
//            cartPageAnalytics.eventViewErrorWhenCheckout(message)
//        }
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

    private fun handleCheckboxGlobalChangeEvent() {
        val isChecked = binding?.topLayout?.checkboxGlobal?.isChecked ?: return
        if (isCheckUncheckDirectAction) {
            cartAdapter.setAllAvailableItemCheck(isChecked)
//            dPresenter.reCalculateSubTotal(cartAdapter.allAvailableShopGroupDataList)
//            dPresenter.saveCheckboxState(cartAdapter.allAvailableCartItemHolderData)
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
                delay(CartFragment.DELAY_SHOW_PROMO_BUTTON_AFTER_SCROLL)
                binding?.apply {
                    val initialPosition =
                        bottomLayout.y - llPromoCheckout.height + CartFragment.PROMO_POSITION_BUFFER.dpToPx(
                            resources.displayMetrics
                        )
                    llPromoCheckout.animate().y(initialPosition)
                        .setDuration(CartFragment.PROMO_ANIMATION_DURATION).start()
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

        val adapterData = cartAdapter.getData()
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
            if (cartAdapter.allAvailableCartItemData.isNotEmpty()) {
                if (binding?.topLayout?.root?.visibility == View.GONE) setTopLayoutVisibility(true)
            }
        } else {
            if (binding?.topLayout?.root?.visibility == View.VISIBLE) setTopLayoutVisibility(false)
        }
    }

    private fun isAtcExternalFlow(): Boolean {
        return getAtcProductId() != 0L
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
                    IconBuilder(IconBuilderFlag(pageSource = CartFragment.CART_PAGE)).addIcon(
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
//        dPresenter.processGetRecommendationData(
//            recommendationPage,
//            cartAdapter.allCartItemProductId
//        )
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
        if (cartAdapter.hasSelectedCartItem()) {
            binding?.topLayout?.textActionDelete?.show()
        } else {
            binding?.topLayout?.textActionDelete?.invisible()
        }
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
            startActivityForResult(intent, CartFragment.NAVIGATION_WISHLIST)
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
                binding?.appBarLayout?.elevation = CartFragment.NO_ELEVATION.toFloat()
            } else {
                binding?.topLayoutShadow?.gone()
            }
        } else {
            if (show) {
                binding?.appBarLayout?.elevation = CartFragment.HAS_ELEVATION.toFloat()
                binding?.topLayoutShadow?.gone()
            } else {
                binding?.appBarLayout?.elevation = CartFragment.NO_ELEVATION.toFloat()
            }
        }
    }

    private fun setTopLayoutVisibility(isShow: Boolean) {
        var isShowToolbarShadow = binding?.topLayoutShadow?.visibility == View.VISIBLE

        if (isShow) {
            binding?.topLayout?.root?.show()
            if (binding?.appBarLayout?.elevation == CartFragment.HAS_ELEVATION.toFloat()) {
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
}
