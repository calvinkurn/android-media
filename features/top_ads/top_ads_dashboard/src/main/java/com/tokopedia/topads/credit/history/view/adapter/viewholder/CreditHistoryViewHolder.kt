package com.tokopedia.topads.credit.history.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.credit.history.data.extensions.formatedDate
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifyprinciples.Typography

class CreditHistoryViewHolder(val view: View) : AbstractViewHolder<CreditHistory>(view) {

    private val txtAmount: Typography? = view.findViewById(R.id.txt_amount)
    private val txtTitle: Typography? = view.findViewById(R.id.txt_title)
    private val txtDate: Typography? = view.findViewById(R.id.txt_date)
    private val txtStatus: Typography? = view.findViewById(R.id.txt_status)

    override fun bind(element: CreditHistory) {
        with(itemView) {
            txtDate?.text = element.formatedDate
            txtTitle?.text = element.description

            txtAmount?.apply {
                text = ("${if (element.isReduction) "-" else "+"} ${element.amountFmt}")
                setTextColor(ContextCompat.getColor(
                    context,
                    if (!element.isReduction) {
                        com.tokopedia.topads.common.R.color.topads_common_green_tab
                    } else {
                        com.tokopedia.topads.common.R.color.topads_heading_color
                    }
                ))
            }

            txtStatus?.apply {
                setTextColor(ContextCompat.getColor(
                    context,
                    when (element.status) {
                        getString(R.string.topads_credit_status_berhasil) -> com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        getString(R.string.topads_credit_status_digunakan) -> com.tokopedia.unifyprinciples.R.color.Unify_NN500
                        getString(R.string.topads_credit_status_menunggu) -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
                        getString(R.string.topads_credit_status_kedaluwarsa) -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        else -> com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    }
                ))
                text = if (element.isReduction) {
                    setTextColor(ContextCompat.getColor(context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500))
                    getString(R.string.topads_credit_status_digunakan)
                } else {
                    element.status
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_credit_history
    }
}