package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by milhamj on 2019-08-14.
 */
class CommissionDetailItemViewHolder(v: View) : AbstractViewHolder<CommissionDetailItemViewModel>(v) {

    private val collapseBtn: View = v.findViewById(R.id.collapseBtn)
    private val incomeLayout: View = v.findViewById(R.id.incomeLayout)
    private val feeLayout: View = v.findViewById(R.id.feeLayout)

    private val datetime: Typography = v.findViewById(R.id.datetime)
    private val incomeValue: Typography = v.findViewById(R.id.incomeValue)
    private val incomeInvoice: Typography = v.findViewById(R.id.incomeInvoice)
    private val feeValue: Typography = v.findViewById(R.id.feeValue)
    private val feeInvoice: Typography = v.findViewById(R.id.feeInvoice)
    private val totalCommission: Typography = v.findViewById(R.id.totalCommission)

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission
    }

    override fun bind(element: CommissionDetailItemViewModel?) {
        datetime.text = "21 Agustus 2018 pukul 22.08"
        incomeValue.text = "Rp 5.000"
        feeValue.text = "- Rp 2.000"
        totalCommission.text = "Rp 3.000"

        collapseBtn.setOnClickListener {
            if (isDetailVisible()) {
                hideDetail()
            } else {
                showDetail()
            }
        }

        incomeInvoice.setOnClickListener {
            RouteManager.route(itemView.context, "${ApplinkConstInternalGlobal.WEBVIEW}?url=www.tokopedia.com")
        }

        feeInvoice.setOnClickListener {
            RouteManager.route(itemView.context, "${ApplinkConstInternalGlobal.WEBVIEW}?url=www.bukalapak.com")
        }
    }

    private fun isDetailVisible() = incomeLayout.isVisible

    private fun showDetail() {
        incomeLayout.visible()
        feeLayout.visible()
        incomeInvoice.visible()
        feeInvoice.visible()
    }

    private fun hideDetail() {
        incomeLayout.gone()
        feeLayout.gone()
        incomeInvoice.gone()
        feeInvoice.gone()
    }
}