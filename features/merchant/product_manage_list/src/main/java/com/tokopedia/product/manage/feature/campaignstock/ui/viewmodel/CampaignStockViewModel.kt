package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.StockThumbnailUrlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        private val campaignStockAllocationUseCase: CampaignStockAllocationUseCase,
        private val stockThumbnailUrlUseCase: StockThumbnailUrlUseCase,
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val mStockAllocationParams = MutableLiveData<Pair<List<String>, String>>()

    private val mGetStockAllocationLiveData = MediatorLiveData<Result<Pair<String, GetStockAllocationData>>>().apply {
        addSource(mStockAllocationParams) { paramsPair ->
            val productIds = paramsPair.first
            val shopId = paramsPair.second
            launchCatchError(
                    block = {
                        value = Success(withContext(Dispatchers.IO) {
                            stockThumbnailUrlUseCase.params = StockThumbnailUrlUseCase.createRequestParams(productIds.firstOrNull().orEmpty())
                            campaignStockAllocationUseCase.params = CampaignStockAllocationUseCase.createRequestParam(productIds, shopId)
                            val thumbnailUrl = async { stockThumbnailUrlUseCase.executeOnBackground() }
                            val stockAllocationData = async { campaignStockAllocationUseCase.executeOnBackground() }
                            Pair(thumbnailUrl.await(), stockAllocationData.await())
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val getStockAllocationData: LiveData<Result<Pair<String, GetStockAllocationData>>>
        get() = mGetStockAllocationLiveData

    fun getStockAllocation(productIds: List<String>,
                           shopId: String) {
        mStockAllocationParams.value = Pair(productIds, shopId)
    }
}