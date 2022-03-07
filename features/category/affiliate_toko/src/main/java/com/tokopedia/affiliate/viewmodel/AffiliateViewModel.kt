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

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            validateUserdata.value = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
        }, onError = {
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getErrorMessage(): LiveData<Throwable> = errorMessage

}
