package com.tokopedia.saldodetails.transactionDetailPages.penjualan

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.SalesTransactionDetail
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.date.DateUtil
import kotlin.math.abs

class SalesSaldoTransactionViewHolder (itemView: View, val onClick: (Visitable<*>) -> Unit) :
    AbstractViewHolder<SalesTransactionDetail>(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tvTransactionHeading)
    private val tvSaldoAmount = itemView.findViewById<Typography>(R.id.tvSaldoAmount)
    private val tvNote = itemView.findViewById<Typography>(R.id.tvSaldoNote)
    private val tvTransactionDate = itemView.findViewById<Typography>(R.id.tvSaldoTransactionDate)


    override fun bind(element: SalesTransactionDetail) {
        val context = itemView.context
        tvTitle.text = element.description
        tvNote.text = element.invoice
        if (element.amount > 0) {
            tvSaldoAmount.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            tvSaldoAmount.text = String.format(
                context.resources.getString(R.string.sp_positive_saldo_balance),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(element.amount, false))
        } else {
            tvSaldoAmount.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500))
            tvSaldoAmount.text = String.format(
                context.resources.getString(R.string.sp_negative_saldo_balance),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(abs(element.amount), false)
            )
        }
        val transDateStr = DateUtil.formatDate(
            SaldoDateUtil.DATE_PATTERN_FROM_SERVER,
            SaldoDateUtil.DATE_PATTERN_FOR_UI,
            element.createTime,
        )
        itemView.setOnClickListener { openDetailPage(element) }
        tvTransactionDate.text = context.resources.getString(R.string.sp_date_time_view, transDateStr)

    }

    private fun openDetailPage(element: SalesTransactionDetail) {
        onClick(element)
    }

    companion object {
        val LAYOUT = R.layout.saldo_item_sales_transaction
    }

}