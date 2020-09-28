package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsTabsListener
import com.tokopedia.deals.common.ui.dataview.DealsTabsDataView
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_tabs.view.*

class DealsTabsViewHolder (itemView: View, private val tabsListener: DealsTabsListener)
    : BaseViewHolder(itemView) {

    fun bind(tabs: DealsTabsDataView) {
        with(itemView) {
            if (tabs.title.isNullOrEmpty()) {
                shimmering.show()
            } else {

            }
        }
     }


    companion object {
        val LAYOUT = R.layout.item_deals_tabs
    }
}