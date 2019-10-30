package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.LoadMoreDataModel

class LoadMoreViewHolder (view: View) : SmartAbstractViewHolder<LoadMoreDataModel>(view){
    override fun bind(element: LoadMoreDataModel, listener: SmartListener) {}

    companion object{
        val LAYOUT = R.layout.layout_loading_more
    }
}