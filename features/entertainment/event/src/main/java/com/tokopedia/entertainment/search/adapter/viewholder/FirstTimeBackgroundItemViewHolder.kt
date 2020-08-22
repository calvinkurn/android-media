package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.View
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeModel

class FirstTimeBackgroundItemViewHolder(val view: View) : SearchEventViewHolder<FirstTimeModel>(view) {

    companion object{
        val LAYOUT = R.layout.ent_search_first_time
    }

    override fun bind(element: FirstTimeModel) {

    }
}