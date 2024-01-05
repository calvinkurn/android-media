package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst
import com.tokopedia.common.topupbills.data.constant.showMultiCheckoutButton
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyPdpCheckoutViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 30/04/21
 */
class EmoneyPdpBottomCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
        set(value) {
            field = value
        }

    private val binding : WidgetEmoneyPdpCheckoutViewBinding
    private val coachMark2 = CoachMark2(context)
    
    init {
        binding = WidgetEmoneyPdpCheckoutViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.emoneyPdpCheckoutViewButton.isLoading = isLoading
    }

    fun onBuyButtonMultiCheckoutLoading(isLoading: Boolean) {
        binding.emoneyPdpLeftButton.isLoading = isLoading
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            binding.emoneyPdpCheckoutViewLayout.show()
            showCoachMark()
        } else {
            binding.emoneyPdpCheckoutViewLayout.hide()
            hideCoachMark()
        }
    }

    fun showCoachMark() {
        coachMark2.container?.show()
        coachMark2.container?.setPadding(Int.ZERO, MultiCheckoutConst.PADDING_TOP.toPx(), Int.ZERO, Int.ZERO)
    }

    fun hideCoachMark() {
        coachMark2.container?.hide()
    }

    fun setTotalPrice(price: String) {
        binding.emoneyPdpCheckoutViewTotalPayment.text = price
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        showMultiCheckoutButton(multiCheckoutButtons, context, binding.emoneyPdpCheckoutViewButton, binding.emoneyPdpLeftButton,
            coachMark2,
            {
                listener?.onClickNextBuyButton()
            } , {
                listener?.onClickMultiCheckoutButton()
            },  {
                listener?.onCloseCoachMark()
            }
        )
    }

    interface ActionListener {
        fun onClickNextBuyButton()

        fun onClickMultiCheckoutButton()

        fun onCloseCoachMark()
    }

}
