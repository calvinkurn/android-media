package com.tokopedia.tokofood.common.minicartwidget.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.databinding.LayoutWidgetPurchaseMiniCartBinding
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class TokoFoodMiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), LifecycleObserver {

    private var viewBinding: LayoutWidgetPurchaseMiniCartBinding? = null
    private var viewModel: MultipleFragmentsViewModel? = null

    private var source: String = ""

    private var job: Job? = null

    private var totalQuantity: Int = Int.ZERO
    set(value) {
        val ctaText =
            try {
                if (value <= Int.ZERO) {
                    context?.getString(com.tokopedia.tokofood.R.string.minicart_order_empty).orEmpty()
                } else {
                    context?.getString(com.tokopedia.tokofood.R.string.minicart_order, value).orEmpty()
                }
            } catch (ex: Resources.NotFoundException) {
                String.EMPTY
            }
        viewBinding?.totalAmountMiniCart?.setCtaText(ctaText)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewModel = null
        job?.cancel()
        job = null
    }

    // Function to initialize the widget
    fun initialize(sharedViewModel: MultipleFragmentsViewModel,
                   lifecycleScope: LifecycleCoroutineScope,
                   source: String) {
        viewModel = sharedViewModel
        lifecycleScope.launchWhenStarted {
            viewModel?.miniCartFlow?.collect { result ->
                renderMiniCart(result, lifecycleScope)
            }
        }
        this.source = source
        viewModel?.loadInitial(source)
    }

    private fun renderMiniCart(result: Result<MiniCartUiModel>,
                               lifecycleScope: LifecycleCoroutineScope) {
        when(result) {
            is Result.Success -> {
                renderTotalAmount(result.data)
            }
            is Result.Loading -> {
                renderLoading(lifecycleScope)
            }
            else -> {
                renderError()
            }
        }
    }

    private fun renderTotalAmount(miniCartUiModel: MiniCartUiModel){
        job?.cancel()
        viewBinding?.totalAmountMiniCart?.run {
            isTotalAmountDisabled = false
            if (isTotalAmountLoading) {
                isTotalAmountLoading = false
            }
            setLabelTitle(miniCartUiModel.shopName)
            val totalPrice =
                miniCartUiModel.totalPriceFmt.takeIf { it.isNotBlank() } ?: getDashString()
            setAmount(totalPrice)
            totalQuantity = miniCartUiModel.totalProductQuantity
            amountCtaView.visible()
            amountCtaView.setOnClickListener {
                viewModel?.clickMiniCart(source)
            }
        }
    }

    private fun renderLoading(lifecycleScope: LifecycleCoroutineScope) {
        job?.cancel()
        job = lifecycleScope.launch {
            delay(MINI_CART_LOADING_MAX_DELAY)
            viewModel?.miniCartFlow?.value?.let { result ->
                renderMiniCart(result, lifecycleScope)
            }
        }
        job?.start()
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

    private fun getDashString(): String {
        return try {
            context?.getString(com.tokopedia.tokofood.R.string.text_purchase_dash).orEmpty()
        } catch (ex: Resources.NotFoundException) {
            String.EMPTY
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

    companion object {
        private const val MINI_CART_LOADING_MAX_DELAY = 5000L
    }

}