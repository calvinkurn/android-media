package com.tokopedia.topads.dashboard.view.adapter.autoads

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder.AutoAdsItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemViewModel

/**
 * Created by Pika on 2/6/20.
 */

interface AutoAdsItemsAdapterTypeFactory {

    fun type(model: AutoAdsItemsEmptyViewModel): Int

    fun type(model: AutoAdsItemsItemViewModel): Int

    fun holder(type: Int, view: View): AutoAdsItemsViewHolder<*>

}