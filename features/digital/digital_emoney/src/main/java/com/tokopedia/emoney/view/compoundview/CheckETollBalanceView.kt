package com.tokopedia.emoney.view.compoundview

import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.R
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 15/05/18.
 */
class CheckETollBalanceView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                      defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val textLabelNote: TextView
    private val buttonFeature: AppCompatButton

    private val viewRemainingBalance: LinearLayout
    private val textCardNumber: TextView
    private val textRemainingBalance: TextView
    private val textDate: TextView

    private lateinit var listener: OnCheckBalanceClickListener

    init {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_holder_check_emoney_balance_view, this, true)

        textLabelNote = view.findViewById(R.id.text_label_note)
        buttonFeature = view.findViewById(R.id.button_feature)

        viewRemainingBalance = view.findViewById(R.id.view_remaining_balance)
        textCardNumber = view.findViewById(R.id.text_card_number)
        textRemainingBalance = view.findViewById(R.id.text_remaining_balance)
        textDate = view.findViewById(R.id.text_date)

        buttonFeature.setOnClickListener { listener.onClick() }
    }

    interface OnCheckBalanceClickListener {
        fun onClick()
    }

    fun setListener(listener: OnCheckBalanceClickListener) {
        this.listener = listener
    }

    fun showCheckBalance(note: String, buttonText: String) {
        viewRemainingBalance.visibility = View.GONE
        textLabelNote.visibility = View.VISIBLE
        textLabelNote.text = note
        buttonFeature.text = buttonText
    }

    fun showRemainingBalance(cardNumber: String, remainingBalance: String, date: String) {
        textLabelNote.visibility = View.GONE
        viewRemainingBalance.visibility = View.VISIBLE
        textCardNumber.text = "No. Kartu " + NFCUtils.formatCardUIDWithSpace(cardNumber)
        textRemainingBalance.text = remainingBalance
        textDate.text = date
    }

}
