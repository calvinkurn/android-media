package com.tokopedia.affiliate.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateOnBoardingData
import com.tokopedia.affiliate.model.request.OnBoardingRequest
import com.tokopedia.affiliate.usecase.AffiliateOnBoardingUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateTermsAndConditionViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateOnBoardingUseCase: AffiliateOnBoardingUseCase)
    :BaseViewModel() {
    private var onBoardingData = MutableLiveData<AffiliateOnBoardingData.OnBoardAffiliate>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()

    fun affiliateOnBoarding(channels : ArrayList<OnBoardingRequest.Channel>) {
        launchCatchError(block = {
            progressBar.value = true
            onBoardingData.value = affiliateOnBoardingUseCase.affiliateOnBoarding(channels)
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getOnBoardingData() : LiveData<AffiliateOnBoardingData.OnBoardAffiliate> = onBoardingData
}