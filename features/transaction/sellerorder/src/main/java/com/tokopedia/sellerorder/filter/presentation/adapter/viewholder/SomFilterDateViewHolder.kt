package com.tokopedia.sellerorder.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter.Companion.PAYLOAD_DATE_FILTER
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import kotlinx.android.synthetic.main.item_som_filter_date.view.*

class SomFilterDateViewHolder(view: View, private val somFilterListener: SomFilterListener): AbstractViewHolder<SomFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_som_filter_date
    }

    override fun bind(element: SomFilterUiModel?) {
        with(itemView) {
            tvTitleFilterDate?.text = element?.nameFilter
            selectDateFilter.setOnClickListener {
                somFilterListener.onDateClicked(adapterPosition)
            }
        }
    }

    override fun bind(element: SomFilterUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if(element == null || payloads.isEmpty()) {
            return
        }

        when(payloads[0] as Int) {
            PAYLOAD_DATE_FILTER -> {
                if(element.date.isNotBlank()) {
                    with(itemView) {
                        selectDateFilter.setDateLabel(element.date)
                    }
                }
            }
        }
    }
}