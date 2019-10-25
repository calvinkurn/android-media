package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.LoadingDataModel

class LoadingViewHolder(view: View) : AbstractViewHolder<LoadingDataModel>(view){
    override fun bind(element: LoadingDataModel?) {}

    companion object{
        val LAYOUT = R.layout.layout_loading_list
    }
}