package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.LoadMoreDataModel

/**
 * Created by yfsx on 16/09/21.
 */
class LoadMoreViewHolder(view: View) : AbstractViewHolder<LoadMoreDataModel>(view) {
    override fun bind(element: LoadMoreDataModel) {}

    companion object {
        val LAYOUT = R.layout.layout_recom_loading_more
    }
}