package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    fun getStockAllocation(shopId: String,
                           productIds: Array<String>) {

    }

}