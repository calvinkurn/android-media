package com.tokopedia.minicart.bmgm.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartWidgetBinding
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartView : ConstraintLayout, BmgmMiniCartAdapter.Listener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    companion object {
        private const val CROSSED_TEXT_FORMAT = "<del>%s</del>"
        private const val MESSAGE_SWITCH_INITIAL_DELAY = 3L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var param = BmgmParamModel()
    private var shopIds = listOf<Long>()
    private var messageIndex = Int.ZERO
    private var offerCount: Int = Int.ZERO

    private var binding: ViewBmgmMiniCartWidgetBinding? = null
    private var footerBinding: ViewBmgmMiniCartSubTotalBinding? = null
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter(this) }
    private val viewModel: BmgmMiniCartViewModel by lazy {
        val owner = context as AppCompatActivity
        return@lazy ViewModelProvider(
            owner, viewModelFactory
        )[BmgmMiniCartViewModel::class.java]
    }
    private var hasVisited = false

    init {
        binding = ViewBmgmMiniCartWidgetBinding.inflate(
            LayoutInflater.from(context), this, true
        ).apply {
            footerBinding = ViewBmgmMiniCartSubTotalBinding.bind(root)
        }
        initInjector()
        setupView()
        setupRecyclerView()
    }

    private fun setupView() {
        binding?.run {
            tvBmgmCartDiscount.setFactory {
                return@setFactory Typography(root.context).apply {
                    setType(Typography.DISPLAY_3)
                    setTextColor(context.getResColor(unifyprinciplesR.color.Unify_NN950))
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        viewModel.clearCartDataLocalCache()
        binding = null
        super.onDetachedFromWindow()
    }

    override fun setOnItemClickedListener() {
        viewModel.storeCartDataToLocalCache()
        RouteManager.route(context, ApplinkConstInternalGlobal.BMGM_MINI_CART)
    }

    fun init(lifecycleOwner: LifecycleOwner) {
        observeCartData(lifecycleOwner)
        observeSetCarChecklistStatus(lifecycleOwner)
    }

    fun fetchData(
        shopIds: List<Long>,
        offerIds: List<Long>,
        offerJsonData: String,
        warehouseIds: List<String>,
        offerCount: Int
    ) {
        this.param = BmgmParamModel(
            offerIds = offerIds, offerJsonData = offerJsonData, warehouseIds = warehouseIds
        )
        this.shopIds = shopIds
        this.offerCount = offerCount
        viewModel.getMiniCartData(shopIds, param, false)
    }

    fun refreshAfterAtC() {
        viewModel.getMiniCartData(shopIds, param, false)
    }

    private fun refreshData() {
        viewModel.getMiniCartData(shopIds = shopIds, param = param, showLoadingState = true)
    }

    private fun observeCartData(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cartData.collect {
                when (it) {
                    is BmgmState.Loading -> showMiniCartLoadingState()
                    is BmgmState.Success -> setOnSuccessGetCartData(it.data)
                    is BmgmState.Error -> showErrorState()
                    else -> {
                        /* no-op */
                    }
                }
            }
        }
    }

    private fun observeSetCarChecklistStatus(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.setCheckListState.collectLatest {
                when (it) {
                    is BmgmState.Loading -> showLoadingButton()
                    is BmgmState.Success, is BmgmState.Error -> openCartPage()
                    else -> {
                        /* no-op */
                    }
                }
            }
        }
    }

    private fun showMiniCartLoadingState() {
        dismissErrorState()
        binding?.run {
            loadingStateGroup.visible()
            containerSubTotal.gone()
            rvBmgmMiniCart.gone()
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
            rvBmgmMiniCart.gone()
            tvBmgmCartDiscount.gone()
            containerSubTotal.gone()
            icBmgmReload.setOnClickListener {
                refreshData()
            }
        }
    }

    private fun dismissErrorState() {
        binding?.errorStateGroup?.gone()
    }

    private fun openCartPage() {
        dismissLoadingButton()
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun showLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = true
    }

    private fun dismissLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = false
    }

    private fun setOnSuccessGetCartData(data: BmgmMiniCartDataUiModel) {
        dismissMiniCartLoadingState()
        dismissErrorState()
        setupTiersApplied(data)
        setupFooterView(data)
    }

    private fun setupTiersApplied(data: BmgmMiniCartDataUiModel) {
        binding?.run {
            rvBmgmMiniCart.visible()

            if (data.tiersApplied.isNotEmpty()) {
                setupMessageWithAnimation(data.offerMessage)
                rvBmgmMiniCart.visible()
            } else {
                tvBmgmCartDiscount.gone()
                rvBmgmMiniCart.gone()
            }

            miniCartAdapter.clearAllElements()
            miniCartAdapter.addElement(getProductList(data))
        }
    }

    private fun setupMessageWithAnimation(messages: List<String>) {
        if (messages.isEmpty()) return
        binding?.run {
            if (messages.size > Int.ONE) {
                showMultipleMessage(messages)
            } else {
                val message = messages.firstOrNull().orEmpty()
                showSingleMessageWithNoAnimation(message)
            }
        }
    }

    private fun showMultipleMessage(messages: List<String>) {
        binding?.run {
            val firstMessage = messages.firstOrNull().orEmpty()
            if (!hasVisited) {
                hasVisited = true
                showSingleMessageWithNoAnimation(firstMessage)
            } else {
                if (firstMessage.isBlank()) {
                    tvBmgmCartDiscount.gone()
                } else {
                    tvBmgmCartDiscount.visible()
                    tvBmgmCartDiscount.setText(firstMessage.parseAsHtml())
                }
            }
            flipTextWithAnimation(messages)
        }
    }

    private fun showSingleMessageWithNoAnimation(message: String) {
        binding?.run {
            if (message.isBlank()) {
                tvBmgmCartDiscount.gone()
            } else {
                tvBmgmCartDiscount.visible()
                tvBmgmCartDiscount.setCurrentText(message.parseAsHtml())
            }
        }
    }

    private fun flipTextWithAnimation(messages: List<String>) {
        binding?.tvBmgmCartDiscount?.run {
            postDelayed({
                if (messageIndex == messages.size.minus(Int.ONE)) {
                    messageIndex = Int.ZERO
                } else {
                    val message = messages.getOrNull(++messageIndex).orEmpty()
                    if (message.isBlank()) {
                        gone()
                    } else {
                        visible()
                        setText(message.parseAsHtml())
                    }
                    flipTextWithAnimation(messages)
                }
            }, TimeUnit.SECONDS.toMillis(MESSAGE_SWITCH_INITIAL_DELAY))
        }
    }

    private fun setupFooterView(data: BmgmMiniCartDataUiModel) {
        binding?.containerSubTotal?.visible()
        footerBinding?.run {
            if (data.tiersApplied.isEmpty()) {
                tvBmgmFinalPrice.text =
                    context.getString(R.string.mini_cart_widget_label_total_price_unavailable_default)
                tvBmgmPriceBeforeDiscount.gone()
                btnBmgmOpenCart.isEnabled = false
            } else {
                btnBmgmOpenCart.isEnabled = true
                tvBmgmFinalPrice.text = data.getPriceAfterDiscountStr()
                setupCrossedPrice(data)
                btnBmgmOpenCart.setOnClickListener {
                    viewModel.setCartListCheckboxState(getCartIds(data.tiersApplied))
                }
            }
        }
    }

    private fun setupCrossedPrice(data: BmgmMiniCartDataUiModel) {
        footerBinding?.run {
            val showCrossedPrice = data.finalPrice != data.priceBeforeBenefit
            if (showCrossedPrice) {
                tvBmgmPriceBeforeDiscount.visible()
                val priceStr = String.format(CROSSED_TEXT_FORMAT, data.getPriceBeforeDiscountStr())
                tvBmgmPriceBeforeDiscount.text = priceStr.parseAsHtml()
            } else {
                tvBmgmPriceBeforeDiscount.gone()
            }
        }
    }

    private fun getCartIds(tiersApplied: List<BmgmMiniCartVisitable.TierUiModel>): List<String> {
        return tiersApplied.map { it.products }.flatten().map { it.cartId }
    }

    private fun getProductList(data: BmgmMiniCartDataUiModel): List<BmgmMiniCartVisitable> {
        val productList = mutableListOf<BmgmMiniCartVisitable>()
        data.tiersApplied.forEach { t ->
            if (t.isDiscountTier()) {
                productList.add(t)
            } else {
                t.products.forEach { p ->
                    productList.add(p)
                }
            }
        }
        val numOfDiscountTier = data.tiersApplied.count { it.isDiscountTier() }
        val hasReachMaxDiscount = numOfDiscountTier == offerCount
        if (!hasReachMaxDiscount) {
            productList.add(BmgmMiniCartVisitable.PlaceholderUiModel)
        }
        return productList
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmMiniCartItemDecoration())
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = miniCartAdapter
        }
    }

    private fun initInjector() {
        DaggerBmgmComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }
}