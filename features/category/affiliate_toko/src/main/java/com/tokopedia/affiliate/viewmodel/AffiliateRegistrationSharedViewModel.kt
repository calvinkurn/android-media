package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.liveDataExtension.SingleLiveEventData
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

    private var userActionLiveData = SingleLiveEventData<UserAction>()
    private var userLoginPageLiveData = SingleLiveEventData<UserAction>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()
    var affiliatePortfolioData = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    var isFieldError = MutableLiveData<Boolean>()

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = true
            val response = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
            onGetResult(response)
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    var listOfChannels = ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>()
    fun navigateToTermsFragment(arrayListOfChannels: ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>) {
        listOfChannels = arrayListOfChannels
        userActionLiveData.value = UserAction.NaigateToTermsAndFragment
    }

    private fun onGetResult(response: AffiliateValidateUserData) {
        if(response.validateAffiliateUserStatus.data?.isEligible == true && response.validateAffiliateUserStatus.data?.isRegistered == false){
            userActionLiveData.value = UserAction.SignUpAction
        }
        else if(response.validateAffiliateUserStatus.data?.isEligible == false){
            userLoginPageLiveData.value = UserAction.FraudAction
        }
        else if(response.validateAffiliateUserStatus.data?.isEligible == true && response.validateAffiliateUserStatus.data?.isRegistered == true){
            userLoginPageLiveData.value = UserAction.RegisteredAction
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
        userActionLiveData.value = UserAction.NaigateToPortFolio
    }

    fun onRegisterationSuccess() {
        userActionLiveData.value = UserAction.RegistrationSucces
    }

    fun getUserAction(): SingleLiveEventData<UserAction> = userActionLiveData
    fun getLoginScreenAction(): SingleLiveEventData<UserAction> = userLoginPageLiveData
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getProgressBar(): LiveData<Boolean> = progressBar



    sealed class UserAction {
        object RegisteredAction : UserAction()
        object FraudAction : UserAction()
        object SignUpAction : UserAction()
        object NaigateToPortFolio : UserAction()
        object NaigateToTermsAndFragment : UserAction()
        object RegistrationSucces : UserAction()
    }
}