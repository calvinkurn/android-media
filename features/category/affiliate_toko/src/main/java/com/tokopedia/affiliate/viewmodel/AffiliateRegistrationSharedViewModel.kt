package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.liveDataExtension.SingleLiveEventData
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateRegistrationSharedViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase
) : BaseViewModel() {

    private var userActionLiveData = SingleLiveEventData<UserAction>()
    private var userLoginPageLiveData = SingleLiveEventData<UserAction>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()
    var affiliatePortfolioData = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    var isFieldError = MutableLiveData<Boolean>()
    private var affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementDataV2>()

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
        if (response.validateAffiliateUserStatus.userStatusData?.isSystemDown == true) {
            userLoginPageLiveData.value = UserAction.SystemDown
        } else if (response.validateAffiliateUserStatus.userStatusData?.isEligible == true && response.validateAffiliateUserStatus.userStatusData?.isRegistered == false) {
            userLoginPageLiveData.value = UserAction.SignUpAction
        } else if (response.validateAffiliateUserStatus.userStatusData?.isEligible == false) {
            userLoginPageLiveData.value = UserAction.FraudAction
        } else if (response.validateAffiliateUserStatus.userStatusData?.isEligible == true && response.validateAffiliateUserStatus.userStatusData?.isRegistered == true) {
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

    fun getAnnouncementInformation() {
        launchCatchError(block = {
            affiliateAnnouncement.value =
                affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement()
        }, onError = {
                it.printStackTrace()
            })
    }

    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementDataV2> = affiliateAnnouncement
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
        object SystemDown : UserAction()
    }
}
