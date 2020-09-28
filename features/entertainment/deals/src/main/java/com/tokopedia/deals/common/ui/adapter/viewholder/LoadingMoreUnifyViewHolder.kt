package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.model.LoadingMoreUnifyModel

class LoadingMoreUnifyViewHolder(view: View) : BaseViewHolder(view){

    fun bind(element: LoadingMoreUnifyModel) {
        itemView.setLayoutParams(AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }


    companion object{
        val LAYOUT = R.layout.item_deals_loading_more_unify
    }
}