package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

class DataCreditViewHolder(
    view: View,
    private val listener: OnSelectedListener,
) : AbstractViewHolder<DataCredit>(view) {

    private val radioButton: RadioButtonUnify = view.findViewById(R.id.radio_button)

    companion object {
        val LAYOUT = R.layout.item_top_ads_credit
    }

    override fun bind(element: DataCredit) {
        radioButton.isChecked = listener.isPositionChecked(adapterPosition)
        radioButton.text = element.productPrice
        radioButton.setOnClickListener { listener.select(adapterPosition) }
        itemView.setOnClickListener { listener.select(adapterPosition) }
        if (radioButton.isChecked) {
            radioButton.setTextColor(ContextCompat.getColor(itemView.context,
                com.tokopedia.topads.common.R.color.Unify_GN500))
            itemView.background =
                AppCompatResources.getDrawable(itemView.context, R.drawable.topads_credit_item_bg)
        } else {
            radioButton.setTextColor(ContextCompat.getColor(itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN1000))
            itemView.background = null
        }
    }

    interface OnSelectedListener {
        fun isPositionChecked(pos: Int): Boolean
        fun select(pos: Int)
    }
}