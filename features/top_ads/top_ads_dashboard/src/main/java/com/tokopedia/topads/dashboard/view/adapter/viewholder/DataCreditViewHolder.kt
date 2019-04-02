package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.item_top_ads_credit.view.*

class DataCreditViewHolder(val view: View,
                           private val listener: OnSelectedListener): AbstractViewHolder<DataCredit>(view) {

    companion object {
        val LAYOUT = R.layout.item_top_ads_credit
    }

    override fun bind(element: DataCredit) {
        itemView.radio_button.isChecked = listener.isPositionChecked(adapterPosition)
        itemView.radio_button.setText(element.productPrice)
        itemView.radio_button.setOnClickListener { listener.select(adapterPosition) }
        itemView.setOnClickListener { listener.select(adapterPosition)}

    }

    interface OnSelectedListener {
        fun isPositionChecked(pos: Int): Boolean
        fun select(pos: Int)
    }
}