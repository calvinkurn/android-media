package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.model.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatus
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateValidateUseCase: AffiliateValidateUserStatus,
        private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase,
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var progressBar = MutableLiveData<Boolean>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var affiliatePerformance = MutableLiveData<AffiliatePerformanceData>()
    private var errorMessage = MutableLiveData<String>()

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = false
            validateUserdata.value = affiliateValidateUseCase.validateUserStatus(userSessionInterface.userId, userSessionInterface.email)
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getAffiliatePerformance() {
        shimmerVisibility.value = true
        launchCatchError(block = {
            shimmerVisibility.value = false
            affiliatePerformance.value = affiliatePerformanceUseCase.affiliatePerformance(userSessionInterface.userId)
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliatePerformanceData(): LiveData<AffiliatePerformanceData> = affiliatePerformance
    fun progressBar(): LiveData<Boolean> = progressBar
}
