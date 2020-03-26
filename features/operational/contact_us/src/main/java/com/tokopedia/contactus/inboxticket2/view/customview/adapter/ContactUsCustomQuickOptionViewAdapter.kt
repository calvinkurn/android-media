package com.tokopedia.contactus.inboxticket2.view.customview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.contactus.R
import com.tokopedia.design.quickfilter.ItemFilterViewHolder
import com.tokopedia.design.quickfilter.QuickSingleFilterAdapter
import com.tokopedia.design.quickfilter.QuickSingleFilterListener

class ContactUsCustomQuickOptionViewAdapter(actionListener: QuickSingleFilterListener?) : QuickSingleFilterAdapter(actionListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_option_item, parent, false)
        return ContactUsOptionItemViewHolder(view, actionListener)
    }
}