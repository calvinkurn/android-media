package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.domain.model.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.domain.model.VariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        private val campaignStockAllocationUseCase: CampaignStockAllocationUseCase,
        private val otherCampaignStockDataUseCase: OtherCampaignStockDataUseCase,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val mIsStockVariant = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val mStockAllocationParams = MutableLiveData<Pair<List<String>, String>>()

    private val mGetStockAllocationLiveData = MediatorLiveData<Result<StockAllocationResult>>().apply {
        addSource(mStockAllocationParams) { paramsPair ->
            val productIds = paramsPair.first
            val shopId = paramsPair.second
            launchCatchError(
                    block = {
                        value = Success(withContext(Dispatchers.IO) {
                            campaignStockAllocationUseCase.params = CampaignStockAllocationUseCase.createRequestParam(productIds, shopId)
                            val stockAllocationData = campaignStockAllocationUseCase.executeOnBackground()
                            stockAllocationData.summary.isVariant.let { isVariant ->
                                mIsStockVariant.postValue(isVariant)
                                if (isVariant) {
                                    getVariantResult(productIds, stockAllocationData)
                                } else {
                                    getNonVariantResult(productIds, stockAllocationData)
                                }
                            }
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val getStockAllocationData: LiveData<Result<StockAllocationResult>>
        get() = mGetStockAllocationLiveData

    fun getStockAllocation(productIds: List<String>,
                           shopId: String) {
        mStockAllocationParams.value = Pair(productIds, shopId)
    }

    private suspend fun getNonVariantResult(productIds: List<String>,
                                            stockAllocationData: GetStockAllocationData): NonVariantStockAllocationResult {
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productIds.firstOrNull().orEmpty())

        val otherCampaignStockData = otherCampaignStockDataUseCase.executeOnBackground()

        return NonVariantStockAllocationResult(
                stockAllocationData,
                otherCampaignStockData
        )
    }

    private suspend fun getVariantResult(productIds: List<String>,
                                         stockAllocationData: GetStockAllocationData): VariantStockAllocationResult {
        val getProductVariantUseCaseRequestParams = GetProductVariantUseCase.createRequestParams(productIds.firstOrNull().orEmpty())
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productIds.firstOrNull().orEmpty())

        val getProductVariantData = async { getProductVariantUseCase.execute(getProductVariantUseCaseRequestParams) }
        val otherCampaignStockData = async { otherCampaignStockDataUseCase.executeOnBackground() }
        return VariantStockAllocationResult(
                ProductManageVariantMapper.mapToVariantsResult(getProductVariantData.await().getProductV3),
                stockAllocationData,
                otherCampaignStockData.await())
    }
}