package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.View
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEmptyStateModel

class SearchEmptyStateViewHolder(val view:View) : SearchEventViewHolder<SearchEmptyStateModel>(view){

    override fun bind(element: SearchEmptyStateModel) {
        //EMPTY
    }

    companion object{
        val LAYOUT = R.layout.ent_search_emptystate
    }
}