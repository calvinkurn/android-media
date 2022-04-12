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

    override fun bind(element: CreditHistory) {
        with(itemView) {
            txtDate?.text = element.formatedDate
            txtAmount?.text = ("${if (element.isReduction) "-" else "+"} ${element.amountFmt}")
            if (!element.isReduction)
                txtAmount?.setTextColor(ContextCompat.getColor(context,
                    com.tokopedia.topads.common.R.color.topads_common_green_tab))
            else
                txtAmount?.setTextColor(ContextCompat.getColor(context,
                    com.tokopedia.topads.common.R.color.topads_heading_color))
            txtTitle?.text = element.description
        }
    }

    companion object {
        val LAYOUT = R.layout.item_credit_history
    }
}