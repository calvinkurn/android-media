package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.topupbills.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import kotlinx.android.synthetic.main.view_topup_bills_checkout.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 17/05/19.
 */
class TopupBillsCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
        set(value) {
            field = value
            listener?.run {
                btn_recharge_checkout_next.setOnClickListener {
                    onClickNextBuyButton()
                }
            }
        }
    var promoListener: TickerPromoStackingCheckoutView.ActionListener? = null
        set(value) {
            field = value
            promoListener?.run {
                recharge_checkout_promo_ticker.actionListener = this
            }
        }

    init {
        View.inflate(context, R.layout.view_topup_bills_checkout, this)
        recharge_checkout_promo_ticker.enableView()
    }

    fun getPromoTicker(): TickerPromoStackingCheckoutView {
        return recharge_checkout_promo_ticker
    }

    fun setTotalPrice(price: String) {
        txt_recharge_checkout_price.text = price
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            buy_layout.show()
        } else {
            buy_layout.hide()
        }
    }

    fun setBuyButtonState(state: Boolean) {
        btn_recharge_checkout_next.isEnabled = state
    }

    fun setBuyButtonLabel(label: String) {
        btn_recharge_checkout_next.text = label
    }

    interface ActionListener {
        fun onClickNextBuyButton()
    }
}