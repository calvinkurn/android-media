package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase
) : BaseViewModel() {

    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = true
            validateUserdata.value = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
            progressBar.value = false
        }, onError = {
            it.printStackTrace()
            progressBar.value = false
            errorMessage.value = it
        })
    }

    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar

}
