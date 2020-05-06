package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.LoadingDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class LoadingViewHolder(view: View) : SmartAbstractViewHolder<LoadingDataModel>(view){
    override fun bind(element: LoadingDataModel, listener: SmartListener) {}

    companion object{
        val LAYOUT = R.layout.layout_loading_list
    }
}