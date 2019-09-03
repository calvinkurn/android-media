package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.affiliate

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel

/**
 * Created by jegul on 2019-09-03.
 */
class AffiliateDashboardHeaderViewHolder(
        itemView: View,
        val mainView: AffiliateDashboardContract.View
) : AbstractViewHolder<DashboardHeaderViewModel>(itemView) {

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_af_affiliate_dashboard_header
    }

    private val tvTotalSaldo by lazy { itemView.findViewById<TextView>(R.id.tv_total_saldo) }
    private val tvAffiliateIncome by lazy { itemView.findViewById<TextView>(R.id.tv_affiliate_income) }
    private val tvTotalViewed by lazy { itemView.findViewById<TextView>(R.id.tv_total_viewed) }
    private val tvTotalClicked by lazy { itemView.findViewById<TextView>(R.id.tv_total_clicked) }

    override fun bind(element: DashboardHeaderViewModel) {
        tvTotalSaldo.text = MethodChecker.fromHtml(element.saldoString)
        tvAffiliateIncome.text = MethodChecker.fromHtml(element.saldoString)
        tvTotalViewed.text = MethodChecker.fromHtml(element.seenCount)
        tvTotalClicked.text = MethodChecker.fromHtml(element.clickCount)
    }
}