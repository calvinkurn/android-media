package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.data.model.credit_history.CreditHistory
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.extention.formatedDate
import kotlinx.android.synthetic.main.item_credit_history.view.*

class CreditHistoryViewHolder(val view: View): AbstractViewHolder<CreditHistory>(view) {

    override fun bind(element: CreditHistory) {
        with(itemView.credit_history_labelview){
            setContent("${if (element.isReduction) "-" else "+"} ${element.amountFmt}")
            setTitle(element.description)
            setSubTitle(element.formatedDate)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_credit_history
    }
}