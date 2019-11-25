package com.tokopedia.filter.newdynamicfilter.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

class DynamicFilterDetailOfferingAdapter(filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailAdapter(filterDetailView) {

    override val layout: Int = R.layout.filter_detail_offering

    override fun getViewHolder(view: View): DynamicFilterDetailViewHolder {
        return OfferingItemViewHolder(view, filterDetailView)
    }

    private class OfferingItemViewHolder(itemView: View, filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailViewHolder(itemView, filterDetailView) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val iconNew: View = itemView.findViewById(R.id.icon_new)

        override fun bind(option: Option) {
            super.bind(option)
            title.text = option.name
            description.text = option.description
            description.visibility = if (!TextUtils.isEmpty(option.description)) View.VISIBLE else View.GONE
            iconNew.visibility = if (option.isNew) View.VISIBLE else View.GONE
        }
    }
}
