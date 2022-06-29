package com.tokopedia.tokofood.common.minicartwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.databinding.LayoutWidgetPurchaseMiniCartBinding
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@FlowPreview
class TokoFoodMiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), LifecycleObserver {

    private var viewBinding: LayoutWidgetPurchaseMiniCartBinding? = null
    private var viewModel: MultipleFragmentsViewModel? = null

    private var source: String = ""

    private var totalQuantity: Int = Int.ZERO
    set(value) {
        val ctaText =
            if (value <= Int.ZERO) {
                context?.getString(com.tokopedia.tokofood.R.string.minicart_order_empty).orEmpty()
            } else {
                context?.getString(com.tokopedia.tokofood.R.string.minicart_order, value).orEmpty()
            }
        viewBinding?.totalAmountMiniCart?.setCtaText(ctaText)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewModel = null
    }

    // Function to initialize the widget
    fun initialize(sharedViewModel: MultipleFragmentsViewModel,
                   lifecycleScope: LifecycleCoroutineScope,
                   source: String) {
        viewModel = sharedViewModel
        lifecycleScope.launchWhenStarted {
            viewModel?.miniCartFlow?.collect { result ->
                when(result) {
                    is Result.Success -> {
                        renderTotalAmount(result.data)
                    }
                    is Result.Loading -> {
                        renderLoading()
                    }
                    else -> {
                        renderError()
                    }
                }
            }
        }
        viewModel?.collectValues()
        viewModel?.loadInitial(source)
    }

    private fun renderTotalAmount(miniCartUiModel: MiniCartUiModel){
        viewBinding?.totalAmountMiniCart?.run {
            isTotalAmountDisabled = false
            if (isTotalAmountLoading) {
                isTotalAmountLoading = false
            }
            setLabelTitle(miniCartUiModel.shopName)
            val totalPrice =
                miniCartUiModel.totalPriceFmt.takeIf { it.isNotBlank() }
                    ?: context?.getString(com.tokopedia.tokofood.R.string.text_purchase_dash).orEmpty()
            setAmount(totalPrice)
            totalQuantity = miniCartUiModel.totalProductQuantity
            amountCtaView.visible()
            amountCtaView.setOnClickListener {
                viewModel?.clickMiniCart()
            }
        }
    }

    private fun renderLoading() {
        viewBinding?.totalAmountMiniCart?.let { totalAmount ->
            if (!totalAmount.isTotalAmountLoading) {
                totalAmount.isTotalAmountLoading = true
            }
        }
    }

    private fun renderError() {
        viewBinding?.totalAmountMiniCart?.run {
            if (isTotalAmountLoading) {
                isTotalAmountLoading = false
            }
            isTotalAmountDisabled = true
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewBinding = LayoutWidgetPurchaseMiniCartBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewBinding = null
    }

}