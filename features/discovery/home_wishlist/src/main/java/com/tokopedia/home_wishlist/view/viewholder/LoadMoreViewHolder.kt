package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.LoadMoreDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class LoadMoreViewHolder (view: View) : SmartAbstractViewHolder<LoadMoreDataModel>(view){
    override fun bind(element: LoadMoreDataModel, listener: SmartListener) {}

    companion object{
        val LAYOUT = R.layout.layout_loading_more
    }
}