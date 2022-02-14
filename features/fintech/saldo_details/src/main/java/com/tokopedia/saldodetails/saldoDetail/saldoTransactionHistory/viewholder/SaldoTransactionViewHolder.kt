package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil.DATE_PATTERN_FOR_UI
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil.DATE_PATTERN_FROM_SERVER
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.DepositHistoryList
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.date.DateUtil
import kotlin.math.abs

class SaldoTransactionViewHolder(itemView: View, val onClick: (Visitable<*>) -> Unit) :
    AbstractViewHolder<DepositHistoryList>(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tvTransactionHeading)
    private val tvSaldoAmount = itemView.findViewById<Typography>(R.id.tvSaldoAmount)
    private val tvNote = itemView.findViewById<Typography>(R.id.tvSaldoNote)
    private val tvTransactionDate = itemView.findViewById<Typography>(R.id.tvSaldoTransactionDate)
    private val labelTransactionStatus =
        itemView.findViewById<Label>(R.id.labelSaldoTransactionStatus)
    private val iconNext = itemView.findViewById<IconUnify>(R.id.iconNextButton)

    override fun bind(element: DepositHistoryList) {
        val context = itemView.context
        tvTitle.text = element.typeDescription
        tvNote.text = element.note
        if (element.amount > 0) {
            tvSaldoAmount.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            tvSaldoAmount.text = String.format(
                context.resources.getString(R.string.sp_positive_saldo_balance),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(element.amount, false))
        } else {
            tvSaldoAmount.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_R600))
            tvSaldoAmount.text = String.format(
                context.resources.getString(R.string.sp_negative_saldo_balance),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(abs(element.amount), false)
            )
        }

        val transDateStr = DateUtil.formatDate(
            DATE_PATTERN_FROM_SERVER,
            DATE_PATTERN_FOR_UI,
            element.createTime,
        )
        if(!element.withdrawalStatusString.isNullOrEmpty()){
            labelTransactionStatus.visible()
            labelTransactionStatus.text = element.withdrawalStatusString
            labelTransactionStatus.setLabelType(getLocalLabelColor(element.withdrawalStatusColor))
        } else {
            labelTransactionStatus.gone()
        }
        if(element.haveDetail){
            itemView.setOnClickListener { openDetailPage(element) }
            iconNext.visible()
        } else{
            itemView.setOnClickListener {}
            iconNext.gone()
        }

        tvTransactionDate.text = context.resources.getString(R.string.sp_date_time_view, transDateStr)

    }

    private fun openDetailPage(element: DepositHistoryList) {
        onClick(element)
    }

    private fun getLocalLabelColor(serverColorInt: Int): Int {
        return when(serverColorInt){
            1-> Label.GENERAL_LIGHT_GREEN
            2-> Label.GENERAL_LIGHT_ORANGE
            3-> Label.GENERAL_LIGHT_RED
            else -> Label.GENERAL_DARK_GREY
        }
    }

    companion object {
        val LAYOUT = R.layout.item_saldo_transaction
    }

}
