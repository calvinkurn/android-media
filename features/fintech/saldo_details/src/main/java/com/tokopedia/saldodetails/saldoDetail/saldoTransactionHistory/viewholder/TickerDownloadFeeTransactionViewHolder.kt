package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.content.Intent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.saldoDetail.CommissionBreakdownActivity
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TickerDownloadFeeTransactionModel
import com.tokopedia.unifycomponents.ticker.Ticker

class TickerDownloadFeeTransactionViewHolder(itemView: View) :
    AbstractViewHolder<TickerDownloadFeeTransactionModel>(itemView) {
    override fun bind(element: TickerDownloadFeeTransactionModel) {

        val linkStr = getString(R.string.download_trx_fee_report_ticker_title_link)
        val combinedHtmlDescription = getString(R.string.download_trx_fee_report_ticker_title, linkStr)

        itemView.findViewById<Ticker>(R.id.ticker_download_fee).apply {
            setHtmlDescription(combinedHtmlDescription)
        }

        itemView.setOnClickListener {
            it.context?.startActivity(Intent(it.context, CommissionBreakdownActivity::class.java))
        }

    }

    companion object {
        val LAYOUT = R.layout.ticker_download_fee_transaction
    }

}
