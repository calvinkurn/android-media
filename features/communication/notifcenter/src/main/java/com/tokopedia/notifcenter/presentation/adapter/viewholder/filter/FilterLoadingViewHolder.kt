package com.tokopedia.notifcenter.presentation.adapter.viewholder.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.filter.FilterLoadingUiModel

class FilterLoadingViewHolder(
        itemView: View?
) : AbstractViewHolder<FilterLoadingUiModel>(itemView) {

    override fun bind(element: FilterLoadingUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_notification_filter_loading
    }
}