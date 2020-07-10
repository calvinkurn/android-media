package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.View
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeViewModel

class FirstTimeBackgroundItemViewHolder(val view: View) : SearchEventViewHolder<FirstTimeViewModel>(view) {

    companion object{
        val LAYOUT = R.layout.ent_search_first_time
    }

    override fun bind(element: FirstTimeViewModel) {

    }
}