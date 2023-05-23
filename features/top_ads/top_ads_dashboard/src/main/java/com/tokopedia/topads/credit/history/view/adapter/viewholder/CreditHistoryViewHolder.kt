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
                        com.tokopedia.topads.common.R.color.Unify_GN500
                    } else {
                        com.tokopedia.topads.common.R.color.Unify_N700_96
                    }
                ))
            }

            txtStatus?.apply {
                val textNColor = getStatusTextAndColor(element)
                setTextColor(ContextCompat.getColor(context, textNColor.second))
                text = textNColor.first
            }
        }
    }

    private fun getStatusTextAndColor(element: CreditHistory): Pair<String, Int> = when {
        element.status == Status.PENDING -> getString(R.string.topads_credit_status_menunggu) to com.tokopedia.unifyprinciples.R.color.Unify_YN500
        element.status == Status.EXPIRED -> getString(R.string.topads_credit_status_kedaluwarsa) to com.tokopedia.unifyprinciples.R.color.Unify_RN500
        element.isReduction -> getString(R.string.topads_credit_status_digunakan) to com.tokopedia.unifyprinciples.R.color.Unify_NN500
        element.status == Status.CLAIMED || (!element.isReduction && element.status != Status.PENDING) ->
            getString(R.string.topads_credit_status_berhasil) to com.tokopedia.unifyprinciples.R.color.Unify_GN500
        else -> "" to com.tokopedia.unifyprinciples.R.color.Unify_NN500
    }

    companion object {
        private object Status {
            const val PENDING = "Pending"
            const val EXPIRED = "Expired"
            const val CLAIMED = "Claimed"
        }
        val LAYOUT = R.layout.item_credit_history
    }
}