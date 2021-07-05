package com.tokopedia.topads.dashboard.view.adapter.autoads

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemModel

/**
 * Created by Pika on 2/6/20.
 */

interface AutoAdsItemsAdapterTypeFactory {

    fun type(model: AutoAdsItemsEmptyModel): Int

    fun type(model: AutoAdsItemsItemModel): Int

    fun holder(type: Int, view: View): AutoAdsItemsViewHolder<*>

}