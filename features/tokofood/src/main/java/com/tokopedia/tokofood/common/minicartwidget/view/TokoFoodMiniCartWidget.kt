package com.tokopedia.tokofood.common.minicartwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.databinding.LayoutWidgetPurchaseMiniCartBinding
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@FlowPreview
class TokoFoodMiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var viewBinding: LayoutWidgetPurchaseMiniCartBinding? = null
    private var viewModel: MultipleFragmentsViewModel? = null

    private var onButtonClickAction: () -> Unit = {}

    private var source: String = ""

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
        viewModel?.loadCartList(source)
    }

    fun setOnATCClickListener(action: () -> Unit) {
        onButtonClickAction = action
    }

    private fun renderTotalAmount(miniCartUiModel: MiniCartUiModel){
        viewBinding?.totalAmountMiniCart?.apply {
            isTotalAmountDisabled = false
            if (isTotalAmountLoading) {
                isTotalAmountLoading = false
            }
            setLabelTitle(miniCartUiModel.shopName)
            setAmount(miniCartUiModel.totalPriceFmt)
            setCtaText("Pesan ${miniCartUiModel.totalProductQuantity}")
            amountCtaView.setOnClickListener {
                onButtonClickAction()
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
        viewBinding?.totalAmountMiniCart?.isTotalAmountDisabled = true
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