package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.ACTION_MULTIPLE
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_LEFT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_RIGHT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.PREFERENCE_MULTICHECKOUT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.common.topupbills.data.constant.showMultiCheckoutButton
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsCheckoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
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
    private val coachMark2 = CoachMark2(context)
    var listener: ActionListener? = null
        set(value) {
            field = value
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
            coachMark2.container?.show()
        } else {
            binding.buyLayout.hide()
            coachMark2.container?.hide()
        }
    }

    fun setBuyButtonLabel(label: String) {
        binding.btnRechargeCheckoutNext.text = label
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.btnRechargeCheckoutNext.isLoading = isLoading
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        showMultiCheckoutButton(multiCheckoutButtons, context, binding.btnRechargeCheckoutNext, binding.btnRechargeMultiCheckout, coachMark2, {
            listener?.onClickNextBuyButton()
        }, {
            listener?.onClickMultiCheckout()
        }, {
            listener?.onCloseCoachMark()
        })
    }

    interface ActionListener {
        fun onClickNextBuyButton()

        fun onClickMultiCheckout()

        fun onCloseCoachMark()
    }
}
