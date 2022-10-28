package com.tokopedia.shop.flashsale.presentation.list.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignEligibilityUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import javax.inject.Inject

class CampaignListContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase,
    private val getSellerCampaignEligibilityUseCase: GetSellerCampaignEligibilityUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ACTIVE_CAMPAIGN_ID = 1
        private const val HISTORY_CAMPAIGN_ID = 2
    }

    private val _isEligible = MutableLiveData<Result<Boolean>>()
    val isEligible: LiveData<Result<Boolean>>
        get() = _isEligible

    private val _tabsMeta = MutableLiveData<Result<TabMetaDataResult>>()
    val tabsMeta: LiveData<Result<TabMetaDataResult>>
        get() = _tabsMeta

    data class TabMetaDataResult(val targetTabPosition: Int, val tabMeta: List<TabMeta>)

    data class PrerequisiteData(
        val isEligible: Boolean,
        val tabsMetadata: List<TabMeta>
    )

    fun getPrerequisiteData(targetTabPosition: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val isEligibleDeferred = async { getSellerCampaignEligibilityUseCase.execute() }
                val tabsDeferred = async { getSellerCampaignListMetaUseCase.execute() }

                val prerequisiteData = PrerequisiteData(
                    isEligibleDeferred.await(),
                    tabsDeferred.await()
                )

                val isEligible = !isIneligibleAccess(prerequisiteData)
                if (isEligible) {
                    val tabsMetaResult = TabMetaDataResult(targetTabPosition, prerequisiteData.tabsMetadata)
                    _tabsMeta.postValue(Success(tabsMetaResult))
                }

                _isEligible.postValue(Success(isEligible))
            },
            onError = { error ->
                _isEligible.postValue(Fail(error))
            }
        )

    }

    fun getTabsMeta(targetTabPosition: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val tabsMetaData = getSellerCampaignListMetaUseCase.execute()
                _tabsMeta.postValue(Success(TabMetaDataResult(targetTabPosition, tabsMetaData)))
            },
            onError = { error ->
                _tabsMeta.postValue(Fail(error))
            }
        )

    }

    private fun isIneligibleAccess(prerequisiteData: PrerequisiteData) : Boolean {
        val isEligible = prerequisiteData.isEligible

        val activeCampaign = prerequisiteData.tabsMetadata.find { tab -> tab.id == ACTIVE_CAMPAIGN_ID }
        val historyCampaign = prerequisiteData.tabsMetadata.find { tab -> tab.id == HISTORY_CAMPAIGN_ID }
        val activeCampaignCount = activeCampaign?.totalCampaign
        val historyCampaignCount = historyCampaign?.totalCampaign

        val hasActiveCampaign = activeCampaignCount.isMoreThanZero()
        val hasHistoryCampaign = historyCampaignCount.isMoreThanZero()
        return !isEligible && !hasActiveCampaign && !hasHistoryCampaign
    }
}