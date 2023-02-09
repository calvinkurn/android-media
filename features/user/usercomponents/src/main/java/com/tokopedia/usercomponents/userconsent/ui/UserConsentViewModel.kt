package com.tokopedia.usercomponents.userconsent.ui

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.collection.GetCollectionPointWithConsentUseCase
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.SubmitConsentUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class UserConsentViewModel @Inject constructor(
    private val getUserConsentCollection: GetConsentCollectionUseCase,
    private val getUserConsentCollectionWithConsent: GetCollectionPointWithConsentUseCase,
    private val submitConsentUseCase: SubmitConsentUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _consentCollection = SingleLiveEvent<UserComponentsStateResult<UserConsentCollectionDataModel>>()
    val consentCollection: LiveData<UserComponentsStateResult<UserConsentCollectionDataModel>>
        get() = _consentCollection

    fun getConsentCollection(consentCollectionParam: ConsentCollectionParam, hideWhenAlreadyHaveConsent: Boolean) {
        launchCatchError(coroutineContext, {
            _consentCollection.value = UserComponentsStateResult.Loading()

            val response = if (userSession.isLoggedIn && hideWhenAlreadyHaveConsent) {
                getUserConsentCollectionWithConsent(consentCollectionParam)
            } else {
                getUserConsentCollection(consentCollectionParam)
            }

            _consentCollection.value = if (response.data.collectionPoints.isNotEmpty()) {
                UserComponentsStateResult.Success(response.data)
            } else {
                val message = if (response.data.errorMessages.isNotEmpty()) {
                    response.data.errorMessages.first()
                } else {
                    GENERAL_ERROR
                }

                UserComponentsStateResult.Fail(Throwable(message))
            }
        }, {
            _consentCollection.value = UserComponentsStateResult.Fail(it)
        })
    }

    fun submitConsent(param: ConsentSubmissionParam) {
        launchCatchError(block =  {
            submitConsentUseCase(param)
        }, onError = {})
    }

    companion object {
        const val GENERAL_ERROR = "Terjadi kesalahan. Silahkan coba lagi."
    }
}
