package com.tokopedia.tokofood.common.minicartwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
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

    // Function to initialize the widget
    fun initialize(sharedViewModel: MultipleFragmentsViewModel, lifecycleScope: LifecycleCoroutineScope) {
        viewModel = sharedViewModel
        lifecycleScope.launchWhenResumed {
            viewModel?.miniCartFlow?.collect {
                renderTotalAmount(it)
            }
        }
        viewModel?.loadCartList()
    }

    private fun renderTotalAmount(miniCartUiModel: MiniCartUiModel){
        viewBinding?.totalAmountMiniCart?.apply {
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartUiModel.totalPrice, false).removeDecimalSuffix())
            setCtaText("Pesan ${miniCartUiModel.totalProductQuantity}")
            setLabelTitle(miniCartUiModel.shopName)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewBinding = LayoutWidgetPurchaseMiniCartBinding.inflate(LayoutInflater.from(context))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewBinding = null
    }

}