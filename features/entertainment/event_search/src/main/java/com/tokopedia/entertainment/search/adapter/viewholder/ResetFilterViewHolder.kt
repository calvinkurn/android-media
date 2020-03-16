package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.View
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.DetailEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.ResetFilterViewModel
import kotlinx.android.synthetic.main.ent_search_emptystate.view.*

class ResetFilterViewHolder(val view:View, val onClicked : ((Boolean) -> Unit)?) : DetailEventViewHolder<ResetFilterViewModel>(view) {

    init {
        with(itemView){
            resetFilter.setOnClickListener {
                onClicked?.invoke(true)
            }
        }
    }

    override fun bind(element: ResetFilterViewModel) {
        //EMPTY
    }

    companion object{
        val LAYOUT = R.layout.ent_search_emptystate
    }
}