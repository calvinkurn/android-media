package com.tokopedia.buy_more_get_more.minicart.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.bmsm.BmsmMiniCartDeepLinkMapper
import com.tokopedia.applink.internal.ApplinkConstBmsm
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.common.OfferType
import com.tokopedia.buy_more_get_more.databinding.ViewBmgmMiniCartWidgetBinding
import com.tokopedia.buy_more_get_more.minicart.analytics.BmgmMiniCartTracker
import com.tokopedia.buy_more_get_more.minicart.common.di.DaggerBmgmComponent
import com.tokopedia.buy_more_get_more.minicart.common.utils.MiniCartUtils
import com.tokopedia.buy_more_get_more.minicart.common.utils.logger.NonFatalIssueLogger
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.diffutil.MiniCartDiffUtilCallback
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.buy_more_get_more.minicart.presentation.model.getProductList
import com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.v2.MiniCartV2Widget
import com.tokopedia.minicart.v2.MiniCartV2WidgetListener
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartView : ConstraintLayout, BmgmMiniCartAdapter.Listener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var offerType: OfferType = OfferType.PROGRESSIVE_DISCOUNT

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    private var param = MiniCartParam()
    private var shopIds = listOf<Long>()
    private var latestOfferMessage = ""

    private var binding: ViewBmgmMiniCartWidgetBinding? = null
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter(this) }
    private val viewModel: BmgmMiniCartViewModel by lazy {
        val owner = context as AppCompatActivity
        return@lazy ViewModelProvider(
            owner,
            viewModelFactory
        )[BmgmMiniCartViewModel::class.java]
    }
    private var offerEndDate = ""
    private var onOfferEndedCallback: ((isOfferEnded: Boolean) -> Unit)? = null
    private var miniCartV2WidgetListener: MiniCartV2WidgetListener? = null

    init {
        binding = ViewBmgmMiniCartWidgetBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        initInjector()
        setupRecyclerView()
        initSummaryView(false)
    }

    override fun setOnItemClickedListener() {
        val isOfferEnded = MiniCartUtils.checkIsOfferEnded(offerEndDate)
        if (isOfferEnded) {
            onOfferEndedCallback?.invoke(true)
            return
        }

        sendClickUpSellingEvent()
        when (offerType) {
            OfferType.PROGRESSIVE_DISCOUNT -> {
                saveCartDataToLocalStorage()
                RouteManager.route(context, ApplinkConstBmsm.BMGM_MINI_CART_DETAIL)
            }

            OfferType.GIFT_WITH_PURCHASE -> {
                val intent = RouteManager.getIntent(context, ApplinkConstBmsm.BMGM_MINI_CART_EDITOR)
                intent.putExtra(BmsmMiniCartDeepLinkMapper.EXTRA_PARAM, param)
                intent.putExtra(BmsmMiniCartDeepLinkMapper.OFFER_END_DATE, offerEndDate)
                context.startActivity(intent)
            }
        }
    }

    fun init(lifecycleOwner: LifecycleOwner) {
        observeCartData(lifecycleOwner)
        observeSetCarChecklistStatus(lifecycleOwner)
        doOnDisposed(lifecycleOwner)
    }

    fun setOnCheckCartClickListener(
        offerEndDate: String,
        callback: (isOfferEnded: Boolean) -> Unit
    ) {
        this.offerEndDate = offerEndDate
        this.onOfferEndedCallback = callback
    }

    fun fetchData(
        shopIds: List<Long>,
        offerIds: List<Long>,
        offerJsonData: String,
        warehouseIds: List<Long>,
        cartId: String
    ) {
        this.param = MiniCartParam(
            shopIds = shopIds,
            offerIds = offerIds,
            offerJsonData = offerJsonData,
            warehouseIds = warehouseIds,
            cartId = cartId
        )
        this.shopIds = shopIds
        viewModel.getMiniCartData(param = param, showLoadingState = false)
    }

    fun refreshAfterAtC() {
        viewModel.getMiniCartData(param = param, showLoadingState = false)
    }

    private fun doOnDisposed(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.DESTROYED) {
                viewModel.clearCartDataLocalCache()
                binding = null
                miniCartV2WidgetListener = null
            }
        }
    }

    private fun createMiniCartV2Listener(): MiniCartV2WidgetListener {
        val listener = object : MiniCartV2WidgetListener() {
            override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
            }

            override fun onPrimaryButtonClickListener() {
                setCartListCheckboxState()
                sendClickCekKeranjangButton()
            }
        }
        miniCartV2WidgetListener = listener
        return listener
    }

    private fun refreshData() {
        viewModel.getMiniCartData(param = param, showLoadingState = true)
    }

    private fun observeCartData(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.cartData.collect {
                when (it) {
                    is BmgmState.Loading -> showMiniCartLoadingState()
                    is BmgmState.Success -> setOnSuccessGetCartData(it.data)
                    is BmgmState.Error -> {
                        sendLogger(it.t)
                        showErrorState()
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun observeSetCarChecklistStatus(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.setCheckListState.collectLatest {
                when (it) {
                    is BmgmState.Loading -> showLoadingButton()
                    is BmgmState.Success -> openCartPage()
                    is BmgmState.Error -> {
                        sendLogger(it.t)
                        openCartPage()
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun sendLogger(t: Throwable) {
        NonFatalIssueLogger.logToCrashlytics(t, this::class.java.canonicalName)
    }

    private fun showMiniCartLoadingState() {
        dismissErrorState()
        binding?.run {
            loadingStateGroup.visible()
            bmsmMiniCartView.gone()
            rvBmgmMiniCart.gone()
            stickyGiftView.gone()
            tvBmgmCartDiscount.gone()
        }
    }

    private fun dismissMiniCartLoadingState() {
        binding?.loadingStateGroup?.gone()
    }

    private fun showErrorState() {
        dismissMiniCartLoadingState()
        binding?.run {
            errorStateGroup.visible()
            stickyGiftView.gone()
            rvBmgmMiniCart.gone()
            tvBmgmCartDiscount.gone()
            bmsmMiniCartView.gone()
            icBmgmReload.setOnClickListener {
                refreshData()
            }
        }
    }

    private fun dismissErrorState() {
        binding?.errorStateGroup?.gone()
    }

    private fun openCartPage() {
        val isOfferEnded = MiniCartUtils.checkIsOfferEnded(offerEndDate)
        if (isOfferEnded) {
            onOfferEndedCallback?.invoke(true)
        } else {
            dismissLoadingButton()
            RouteManager.route(context, ApplinkConst.CART)
        }
    }

    private fun showLoadingButton() {
        binding?.bmsmMiniCartView?.showLoading()
    }

    private fun dismissLoadingButton() {
        binding?.bmsmMiniCartView?.dismissLoading()
    }

    private fun setOnSuccessGetCartData(data: BmgmMiniCartDataUiModel) {
        this.offerType = data.offerType

        dismissMiniCartLoadingState()
        dismissErrorState()
        setupTiersApplied(data)
        showPriceSummary(data)
        saveCartDataToLocalStorage()
    }

    private fun setupTiersApplied(data: BmgmMiniCartDataUiModel) {
        binding?.rvBmgmMiniCart?.run {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmMiniCartItemDecoration(offerType))
            }

            if (data.tiers.isNotEmpty()) {
                setupMessageWithAnimation(data.offerMessage)
                visible()
            } else {
                binding?.tvBmgmCartDiscount?.gone()
                gone()
            }
        }

        binding?.stickyGiftView?.run {
            when (offerType) {
                OfferType.PROGRESSIVE_DISCOUNT -> {
                    updateItemList(data.getProductList())
                    gone()
                }
                OfferType.GIFT_WITH_PURCHASE -> {
                    val allProducts = data.getProductList()
                    val atcProducts = allProducts.filter {
                        it !is BmgmMiniCartVisitable.GwpGiftWidgetUiModel
                    }
                    updateItemList(atcProducts)

                    val gifts =
                        allProducts.filterIsInstance<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>()
                    submitList(gifts)
                    setOnItemClickedListener(::setOnItemClickedListener)
                    isVisible = gifts.isNotEmpty()
                }
            }
        }
    }

    private fun updateItemList(items: List<BmgmMiniCartVisitable>) {
        val oldList = miniCartAdapter.data.toList()
        val diffCallback = MiniCartDiffUtilCallback(oldList, items)
        val diffUtil = DiffUtil.calculateDiff(diffCallback)
        miniCartAdapter.data.clear()
        miniCartAdapter.data.addAll(items)
        diffUtil.dispatchUpdatesTo(miniCartAdapter)
    }

    private fun setupMessageWithAnimation(messages: List<String>) {
        binding?.tvBmgmCartDiscount?.setMessages(messages)

        sendMiniCartTrackingOnLastMessageChanged(messages.lastOrNull().orEmpty())
    }

    private fun saveCartDataToLocalStorage() {
        val shopId = shopIds.firstOrNull().orZero()
        val warehouseId = param.warehouseIds.firstOrNull().orZero()
        viewModel.saveCartDataToLocalStorage(shopId, warehouseId, offerEndDate)
    }

    private fun showPriceSummary(data: BmgmMiniCartDataUiModel) {
        binding?.bmsmMiniCartView?.run {
            val shouldShowSlashPrice =
                data.priceBeforeBenefit != data.finalPrice && offerType == OfferType.PROGRESSIVE_DISCOUNT
            initSummaryView(shouldShowSlashPrice)
            visible()
            refresh(
                MiniCartSimplifiedData(
                    isShowMiniCartWidget = data.tiers.isNotEmpty(),
                    miniCartWidgetData = MiniCartWidgetData(
                        totalProductOriginalPrice = data.priceBeforeBenefit,
                        totalProductPrice = data.finalPrice
                    )
                )
            )
        }
    }

    private fun initSummaryView(shouldShowSlashPrice: Boolean) {
        val listener = miniCartV2WidgetListener ?: createMiniCartV2Listener()
        binding?.bmsmMiniCartView?.initialize(
            MiniCartV2Widget.MiniCartV2WidgetConfig(
                showTopShadow = false,
                showChevron = false,
                showOriginalTotalPrice = shouldShowSlashPrice,
                overridePrimaryButtonAction = true,
                overridePrimaryButtonWording = context.getString(R.string.bmsm_check_cart),
                page = MiniCartAnalytics.Page.BMSM_OLP_PAGE
            ),
            listener
        )
    }

    private fun setCartListCheckboxState() {
        val isOfferEnded = MiniCartUtils.checkIsOfferEnded(offerEndDate)
        if (isOfferEnded) {
            onOfferEndedCallback?.invoke(true)
            return
        }

        val state = viewModel.cartData.value
        if (state is BmgmState.Success<BmgmMiniCartDataUiModel>) {
            viewModel.setCartListCheckboxState(getCartIds(state.data.tiers))
        }
    }

    private fun getCartIds(tiersApplied: List<BmgmMiniCartVisitable>): List<String> {
        return tiersApplied.map {
            return@map when (it) {
                is BmgmMiniCartVisitable.ProductUiModel -> listOf(it.cartId)
                is BmgmMiniCartVisitable.TierUiModel -> it.products.map { p -> p.cartId }
                else -> emptyList()
            }
        }.flatten()
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = miniCartAdapter
        }
    }

    private fun sendClickCekKeranjangButton() {
        val offerId = param.offerIds.firstOrNull().orZero().toString()
        val warehouseId = param.warehouseIds.firstOrNull().orZero().toString()
        val shopId = shopIds.firstOrNull().orZero().toString()
        val userId = userSession.get().userId

        BmgmMiniCartTracker.sendClickCekKeranjangEvent(
            offerId = offerId,
            warehouseId = warehouseId,
            shopId = shopId,
            userId = userId
        )
    }

    private fun sendMiniCartTrackingOnLastMessageChanged(currentLastMessage: String) {
        val shouldSendTracker =
            currentLastMessage.isNotBlank() && currentLastMessage != latestOfferMessage
        if (shouldSendTracker) {
            latestOfferMessage = currentLastMessage

            val offerId = param.offerIds.firstOrNull().orZero().toString()
            val warehouseId = param.warehouseIds.firstOrNull().orZero().toString()
            val shopId = shopIds.firstOrNull().orZero().toString()
            val userId = userSession.get().userId
            BmgmMiniCartTracker.sendImpressionUpsellingEvent(
                offerId = offerId,
                warehouseId = warehouseId,
                lastOfferMessage = latestOfferMessage,
                shopId = shopId,
                userId = userId
            )
        }
    }

    private fun sendClickUpSellingEvent() {
        val offerId = param.offerIds.firstOrNull().orZero().toString()
        val warehouseId = param.warehouseIds.firstOrNull().orZero().toString()
        val shopId = shopIds.firstOrNull().orZero().toString()
        val userId = userSession.get().userId

        BmgmMiniCartTracker.sendClickUpSellingEvent(
            offerId = offerId,
            warehouseId = warehouseId,
            lastOfferMessage = latestOfferMessage,
            shopId = shopId,
            userId = userId
        )
    }

    private fun initInjector() {
        DaggerBmgmComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }
}
