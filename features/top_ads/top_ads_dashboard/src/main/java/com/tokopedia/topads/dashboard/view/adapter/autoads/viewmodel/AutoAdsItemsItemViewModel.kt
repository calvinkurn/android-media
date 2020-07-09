package com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel

import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsAdapterTypeFactory

/**
 * Created by Pika on 2/6/20.
 */
class AutoAdsItemsItemViewModel(var data: WithoutGroupDataItem) : AutoAdsItemsViewModel() {
    override fun type(typesFactory: AutoAdsItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}