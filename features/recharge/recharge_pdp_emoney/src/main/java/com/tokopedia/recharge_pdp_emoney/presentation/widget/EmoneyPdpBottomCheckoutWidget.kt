package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyPdpCheckoutViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 30/04/21
 */
class EmoneyPdpBottomCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
        set(value) {
            field = value
            listener?.run {
                binding.emoneyPdpCheckoutViewButton.setOnClickListener {
                    onClickNextBuyButton()
                }
            }
        }

    private val binding : WidgetEmoneyPdpCheckoutViewBinding
    
    init {
        binding = WidgetEmoneyPdpCheckoutViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.emoneyPdpCheckoutViewButton.isLoading = isLoading
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            binding.emoneyPdpCheckoutViewLayout.show()
        } else {
            binding.emoneyPdpCheckoutViewLayout.hide()
        }
    }

    fun setTotalPrice(price: String) {
        binding.emoneyPdpCheckoutViewTotalPayment.text = price
    }

    interface ActionListener {
        fun onClickNextBuyButton()
    }

}