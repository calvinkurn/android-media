package com.tokopedia.topads.credit.history.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.credit.history.data.extensions.formatedDate
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.item_credit_history.view.*

class CreditHistoryViewHolder(val view: View) : AbstractViewHolder<CreditHistory>(view) {

    override fun bind(element: CreditHistory) {
        with(itemView) {
            txt_date.text = element.formatedDate
            txt_amount.text = ("${if (element.isReduction) "-" else "+"} ${element.amountFmt}")
            if (!element.isReduction)
                txt_amount.setTextColor(ContextCompat.getColor(context, com.tokopedia.topads.common.R.color.topads_common_green_tab))
            else
                txt_amount.setTextColor(ContextCompat.getColor(context, com.tokopedia.topads.common.R.color.topads_heading_color))
            txt_title.text = element.description
        }
    }

    companion object {
        val LAYOUT = R.layout.item_credit_history
    }
}