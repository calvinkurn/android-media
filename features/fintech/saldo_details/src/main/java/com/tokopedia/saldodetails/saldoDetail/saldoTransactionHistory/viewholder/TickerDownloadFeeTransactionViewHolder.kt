package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SellerSaldoTracker
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TickerDownloadFeeTransactionModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TickerDownloadFeeTransactionViewHolder(
    itemView: View
) : AbstractViewHolder<TickerDownloadFeeTransactionModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.ticker_download_fee_transaction
    }

    override fun bind(element: TickerDownloadFeeTransactionModel) {

        val linkStr = getString(R.string.download_trx_fee_report_ticker_title_link)
        val combinedHtmlDescription =
            getString(R.string.download_trx_fee_report_ticker_title, linkStr)

        itemView.findViewById<Ticker>(R.id.ticker_download_fee).apply {
            setHtmlDescription(combinedHtmlDescription)
            setOnClickListener {
                openCommissionBreakdown(it.context)
            }
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    openCommissionBreakdown(context)
                }

                override fun onDismiss() {
                }
            })
        }
    }

    private fun openCommissionBreakdown(context: Context?) {
        SellerSaldoTracker.sendDownloadCommissionEntryPointClickEvent()
        RouteManager.route(context, ApplinkConstInternalGlobal.COMMISSION_BREAKDOWN)
    }
}
