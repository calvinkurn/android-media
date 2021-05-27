package com.tokopedia.saldodetails.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class SaldoTransactionViewHolder(itemView: View) :
    AbstractViewHolder<DepositHistoryList>(itemView) {
    private val dateTV: TextView
    private val note: TextView
    private val nominal: TextView
    private val heading: TextView
    private val imageView: ImageUnify
    private val context: Context? = itemView.context

    init {
        dateTV = itemView.findViewById(com.tokopedia.saldodetails.R.id.saldo_transaction_date)
        note = itemView.findViewById(com.tokopedia.saldodetails.R.id.note)
        nominal = itemView.findViewById(com.tokopedia.saldodetails.R.id.nominal)
        heading = itemView.findViewById(com.tokopedia.saldodetails.R.id.transaction_heading)
        imageView = itemView.findViewById(com.tokopedia.saldodetails.R.id.transaction_image_view)
    }


    override fun bind(element: DepositHistoryList) {

        val sourceDate: Date
        var strDate = ""
        val sdfSource = SimpleDateFormat(DATE_PATTERN_FROM_SERVER, Locale.US)
        val sdfView = SimpleDateFormat(DATE_PATTERN_FOR_UI, Locale.US)
        try {
            sourceDate = sdfSource.parse(element.createTime)
            strDate = sdfView.format(sourceDate)
        } catch (e: ParseException) {
        }

        dateTV.text = String.format(
            context!!.resources.getString(com.tokopedia.saldodetails.R.string.sp_date_time_view),
            strDate
        )
        note.text = element.note
        heading.text = element.typeDescription
        imageView.urlSrc = element.imageURL ?: ""
        if (element.amount > 0) {
            if (context != null) {
                nominal.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G700))
                nominal.text = String.format(
                    context.resources.getString(com.tokopedia.saldodetails.R.string.sp_positive_saldo_balance),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(element.amount, false)
                )
            } else {
                nominal.text = element.amount.toString()
            }

        } else {
            if (context != null) {
                nominal.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y600))
                nominal.text = String.format(
                    context.resources.getString(com.tokopedia.saldodetails.R.string.sp_negative_saldo_balance),
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(abs(element.amount), false)
                )
            } else {
                nominal.text = element.amount.toString()
            }
        }
    }

    companion object {

        private val DATE_PATTERN_FROM_SERVER = "yyyy-MM-dd HH:mm:ss"
        private val DATE_PATTERN_FOR_UI = "dd MMM yyyy HH:mm"

        @JvmField
        val LAYOUT = com.tokopedia.saldodetails.R.layout.item_saldo_transaction
    }

}
