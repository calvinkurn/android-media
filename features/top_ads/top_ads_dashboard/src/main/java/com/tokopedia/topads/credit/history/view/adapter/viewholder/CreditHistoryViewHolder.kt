package com.tokopedia.topads.credit.history.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.credit.history.data.extensions.formatedDate
import kotlinx.android.synthetic.main.item_credit_history.view.*

class CreditHistoryViewHolder(val view: View): AbstractViewHolder<CreditHistory>(view) {

    override fun bind(element: CreditHistory) {
        with(itemView){
            txt_date.text = element.formatedDate
            txt_amount.text = ("${if (element.isReduction) "-" else "+"} ${element.amountFmt}")
            txt_title.text = element.description
        }
    }

    companion object {
        val LAYOUT = R.layout.item_credit_history
    }
}