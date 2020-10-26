package com.tokopedia.sellerorder.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import kotlinx.android.synthetic.main.item_som_filter_date.view.*

class SomFilterDateViewHolder(view: View, private val somFilterListener: SomFilterListener): AbstractViewHolder<SomFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_som_filter_date
    }

    override fun bind(element: SomFilterUiModel?) {
        with(itemView) {
            tvTitleFilterDate.text = element?.nameFilter
            tfSelectDate.apply {
                setOnClickListener {
                    somFilterListener.onDateClicked(adapterPosition)
                }
                getFirstIcon().setOnClickListener {
                    somFilterListener.onDateClicked(adapterPosition)
                }
            }
        }
    }

}