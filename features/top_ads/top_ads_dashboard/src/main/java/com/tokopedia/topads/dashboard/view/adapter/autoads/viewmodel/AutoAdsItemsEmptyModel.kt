package com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsAdapterTypeFactory

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsEmptyModel : AutoAdsItemsModel() {
    override fun type(typesFactory: AutoAdsItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}