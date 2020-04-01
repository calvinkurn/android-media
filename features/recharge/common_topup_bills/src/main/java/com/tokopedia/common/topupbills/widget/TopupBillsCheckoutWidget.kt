package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.common.topupbills.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 17/05/19.
 */
class TopupBillsCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val totalPrice: TextView
    private val nextButton: UnifyButton
    private val buyLayout: RelativeLayout
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, R.layout.view_topup_bills_checkout, this)
        totalPrice = view.findViewById(R.id.total_price)
        nextButton = view.findViewById(R.id.next_btn)
        buyLayout = view.findViewById(R.id.buy_layout)

        nextButton.setOnClickListener {
            listener.onClickNextBuyButton()
        }
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setTotalPrice(price: String) {
        totalPrice.setText(price)
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            buyLayout.visibility = View.VISIBLE
        } else {
            buyLayout.visibility = View.GONE
        }
    }

    fun setBuyButtonState(state: Boolean) {
        nextButton.isEnabled = state
    }

    interface ActionListener {
        fun onClickNextBuyButton()
    }
}