package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.model.AffiliateProductCommissionData
import com.tokopedia.exploreCategory.model.AffiliateSearchData
import com.tokopedia.exploreCategory.usecase.AffiliateProductCommissionUseCase
import com.tokopedia.exploreCategory.usecase.AffiliateSearchUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromoViewModel  @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateSearchUseCase: AffiliateSearchUseCase,
        private val affiliateProductCommissionUseCase: AffiliateProductCommissionUseCase,
) : BaseViewModel() {
    private var progressBar = MutableLiveData<Boolean>()
    private var affiliateSearchData = MutableLiveData<AffiliateSearchData>()
    private var affiliateProductCommissionData = MutableLiveData<AffiliateProductCommissionData>()
    private var errorMessage = MutableLiveData<String>()

    fun getSearch() {
        progressBar.value =  true
        launchCatchError(block = {
            affiliateSearchData.value =
                    affiliateSearchUseCase.affiliateSearchWithLink(userSessionInterface.userId, arrayListOf())
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getProductCommission() {
        launchCatchError(block = {
            affiliateProductCommissionData.value =
                    affiliateProductCommissionUseCase.affiliateProductCommission(userSessionInterface.userId,
                            arrayListOf())
        }, onError = {
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getAffiliateSearchData(): LiveData<AffiliateSearchData> = affiliateSearchData
    fun getAffiliateProductCommissionData(): LiveData<AffiliateProductCommissionData> = affiliateProductCommissionData
    fun progressBar(): LiveData<Boolean> = progressBar
}
