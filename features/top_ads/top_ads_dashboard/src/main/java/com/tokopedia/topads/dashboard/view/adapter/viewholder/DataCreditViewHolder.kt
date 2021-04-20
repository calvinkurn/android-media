package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.DataCredit
import kotlinx.android.synthetic.main.item_top_ads_credit.view.*

class DataCreditViewHolder(val view: View,
                           private val listener: OnSelectedListener) : AbstractViewHolder<DataCredit>(view) {

    companion object {
        val LAYOUT = R.layout.item_top_ads_credit
    }

    override fun bind(element: DataCredit) {
        itemView.radio_button.isChecked = listener.isPositionChecked(adapterPosition)
        itemView.radio_button.text = element.productPrice
        itemView.radio_button.setOnClickListener { listener.select(adapterPosition) }
        itemView.setOnClickListener { listener.select(adapterPosition) }
        if (itemView.radio_button.isChecked) {
            itemView.radio_button.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.topads.common.R.color.topads_common_select_color_checked))
            itemView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.topads_credit_item_bg)
        } else {
            itemView.radio_button.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.topads.common.R.color.black))
            itemView.background = null
        }
    }

    interface OnSelectedListener {
        fun isPositionChecked(pos: Int): Boolean
        fun select(pos: Int)
    }
}