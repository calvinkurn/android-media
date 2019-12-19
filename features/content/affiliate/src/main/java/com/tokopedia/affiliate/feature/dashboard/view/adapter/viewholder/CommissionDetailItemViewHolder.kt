package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import java.net.URLEncoder

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

    override fun bind(element: CommissionDetailItemViewModel) {
        datetime.text = element.txTimeFmt
        incomeValue.text = element.affCommissionFmt
        feeValue.text = element.tkpdCommissionFmt
        totalCommission.text = element.netCommissionFmt

        collapseBtn.setOnClickListener {
            if (isDetailVisible()) {
                hideDetail()
            } else {
                showDetail()
            }
        }

        incomeInvoice.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkConstInternalGlobal.WEBVIEW.replace("{url}", URLEncoder.encode(element.affInvoiceUrl, "UTF-8")))
        }

        feeInvoice.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkConstInternalGlobal.WEBVIEW.replace("{url}", URLEncoder.encode(element.tkpdInvoiceUrl, "UTF-8")))
        }
    }

    private fun isDetailVisible() = incomeLayout.isVisible

    private fun showDetail() {
        collapseBtn.setAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.aff_rotate_backward))
        incomeLayout.visible()
        feeLayout.visible()
        incomeInvoice.visible()
        feeInvoice.visible()
    }

    private fun hideDetail() {
        collapseBtn.setAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.aff_rotate_forward))
        incomeLayout.gone()
        feeLayout.gone()
        incomeInvoice.gone()
        feeInvoice.gone()
    }
}