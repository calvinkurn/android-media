package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsCheckoutBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 17/05/19.
 */
class TopupBillsCheckoutWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTopupBillsCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var listener: ActionListener? = null
        set(value) {
            field = value
            listener?.run {
                binding.btnRechargeCheckoutNext.setOnClickListener {
                    onClickNextBuyButton()
                }
            }
        }
    var promoListener: TickerPromoStackingCheckoutView.ActionListener? = null
        set(value) {
            field = value
            promoListener?.run {
                binding.rechargeCheckoutPromoTicker.actionListener = this
            }
        }

    init {
        binding.rechargeCheckoutPromoTicker.enableView()
    }

    fun getPromoTicker(): TickerPromoStackingCheckoutView {
        return binding.rechargeCheckoutPromoTicker
    }

    fun getCheckoutButton(): UnifyButton {
        return binding.btnRechargeCheckoutNext
    }

    fun setTotalPrice(price: String) {
        binding.txtRechargeCheckoutPrice.text = price
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            binding.buyLayout.show()
        } else {
            binding.buyLayout.hide()
        }
    }

    fun setBuyButtonLabel(label: String) {
        binding.btnRechargeCheckoutNext.text = label
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.btnRechargeCheckoutNext.isLoading = isLoading
    }

    interface ActionListener {
        fun onClickNextBuyButton()
    }
}
