package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.model.AffiliateValidateUserData
import com.tokopedia.exploreCategory.usecase.AffiliateValidateUserStatus
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val useCase: AffiliateValidateUserStatus,
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var productCards = MutableLiveData<ArrayList<Int>>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var errorMessage = MutableLiveData<String>()
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getProductCards(): LiveData<ArrayList<Int>> = productCards
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun validateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata

    fun getAffiliateValidateUser() {
        shimmerVisibility.value = true
        launchCatchError(block = {
            validateUserdata.value = useCase.validateUserStatus(userSessionInterface.userId, userSessionInterface.email)
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
}
