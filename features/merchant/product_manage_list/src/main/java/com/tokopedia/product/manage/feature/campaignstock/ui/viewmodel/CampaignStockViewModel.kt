package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val mGetStockAllocationLiveData = MutableLiveData<Result<GetStockAllocationData>>()
    val getStockAllocationData: LiveData<Result<GetStockAllocationData>>
        get() = mGetStockAllocationLiveData

    fun getStockAllocation(shopId: String,
                           productIds: Array<String>) {
        mGetStockAllocationLiveData.value = Success(GetStockAllocationData())
    }

}