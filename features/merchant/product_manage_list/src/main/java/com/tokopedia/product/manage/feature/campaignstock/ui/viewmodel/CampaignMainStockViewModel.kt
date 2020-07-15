package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import javax.inject.Inject

class CampaignMainStockViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val mTotalStockLiveData = MutableLiveData<Int>()
    private val mIsActiveLiveData = MutableLiveData<Boolean>()

}