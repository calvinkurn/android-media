package com.tokopedia.topads.dashboard.view.adapter.autoads

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemViewModel

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsAdapterTypeFactoryImpl : AutoAdsItemsAdapterTypeFactory {

    override fun type(model: AutoAdsItemsEmptyViewModel): Int = AutoAdsItemsEmptyViewHolder.LAYOUT
    override fun type(model: AutoAdsItemsItemViewModel): Int = AutoAdsItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): AutoAdsItemsViewHolder<*> {
        return when (type) {
            AutoAdsItemsItemViewHolder.LAYOUT -> AutoAdsItemsItemViewHolder(view)
            AutoAdsItemsEmptyViewHolder.LAYOUT -> AutoAdsItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}