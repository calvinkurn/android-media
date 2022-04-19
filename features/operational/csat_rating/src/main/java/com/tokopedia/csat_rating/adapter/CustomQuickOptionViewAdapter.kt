package com.tokopedia.csat_rating.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import com.tokopedia.csat_rating.R
import com.tokopedia.csat_rating.quickfilter.ItemFilterViewHolder
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterAdapter
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterListener

class CustomQuickOptionViewAdapter(actionListener: QuickSingleFilterListener) : QuickSingleFilterAdapter(actionListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.csat_layout_option_item, parent, false)
        return OptionItemViewHolder(view, actionListener)
    }
}
