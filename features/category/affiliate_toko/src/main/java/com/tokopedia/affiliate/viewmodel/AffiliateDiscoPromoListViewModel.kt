package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.usecase.AffiliateDiscoveryCampaignUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class AffiliateDiscoPromoListViewModel @Inject constructor(
    private val affiliateDiscoveryCampaignUseCase: AffiliateDiscoveryCampaignUseCase
) :
    BaseViewModel() {
    companion object {
        private const val PAGE_SIZE = 20
    }

    private var discoBanners = MutableLiveData<AffiliateDiscoveryCampaignResponse>()
    private var progressBar = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<Throwable>()
    private val noMoreDataAvailable = MutableLiveData(false)

    fun getDiscoBanners(
        page: Int,
        limit: Int = PAGE_SIZE
    ) {
        progressBar.value = true
        launchCatchError(
            block = {
                discoBanners.value =
                    affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                        page,
                        limit
                    )
                progressBar.value = false
                discoBanners.value?.recommendedAffiliateDiscoveryCampaign?.data?.items.let {
                    noMoreDataAvailable.value = it.isNullOrEmpty() || it.size < PAGE_SIZE
                }
            },
            onError = {
                progressBar.value = false
                noMoreDataAvailable.value = true
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getDiscoCampaignBanners(): LiveData<AffiliateDiscoveryCampaignResponse> = discoBanners
    fun noMoreDataAvailable(): LiveData<Boolean> = noMoreDataAvailable
}
