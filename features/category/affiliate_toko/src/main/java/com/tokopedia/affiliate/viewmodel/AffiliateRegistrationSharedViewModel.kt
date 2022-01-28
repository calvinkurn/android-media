package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.liveDataUtil.SingleEvent
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateRegistrationSharedViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase
) : BaseViewModel() {

    private var userActionLiveData = MutableLiveData<SingleEvent<UserAction>>()
    private var errorMessage = MutableLiveData<Throwable>()

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            val response = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
            onGetResult(response)
        }, onError = {
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    var listOfChannels = ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()
    fun navigateToTermsFragment(arrayListOfChannels: ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>) {
        listOfChannels = arrayListOfChannels
        userActionLiveData.value = SingleEvent(UserAction.NaigateToTermsAndFragment)
    }

    private fun onGetResult(response: AffiliateValidateUserData) {
        if(response.validateAffiliateUserStatus.data?.isEligible == true && response.validateAffiliateUserStatus.data?.isRegistered == false){
            userActionLiveData.value = SingleEvent(UserAction.SignUpAction)
        }
        else if(response.validateAffiliateUserStatus.data?.isEligible == false){
            userActionLiveData.value = SingleEvent(UserAction.FraudAction)
        }
        else if(response.validateAffiliateUserStatus.data?.isEligible == true && response.validateAffiliateUserStatus.data?.isRegistered == true){
            userActionLiveData.value = SingleEvent(UserAction.RegisteredAction)
        }

    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserEmail(): String {
        return userSessionInterface.email
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }

    fun navigateToPortFolio() {
        userActionLiveData.value = SingleEvent(UserAction.NaigateToPortFolio)
    }

    fun onRegisterationSuccess() {
        userActionLiveData.value = SingleEvent(UserAction.RegistrationSucces)
    }

    fun getUserAction(): LiveData<SingleEvent<UserAction>> = userActionLiveData
    fun getErrorMessage(): LiveData<Throwable> = errorMessage



    sealed class UserAction {
        object RegisteredAction : UserAction()
        object FraudAction : UserAction()
        object SignUpAction : UserAction()
        object NaigateToPortFolio : UserAction()
        object NaigateToTermsAndFragment : UserAction()
        object RegistrationSucces : UserAction()
    }
}