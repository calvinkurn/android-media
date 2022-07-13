package com.tokopedia.usercomponents.userconsent.ui

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentStateResult
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.GetConsentCollectionUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class UserConsentViewModel @Inject constructor(
    private val getUserConsentCollection: GetConsentCollectionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _consentCollection = SingleLiveEvent<UserConsentStateResult<UserConsentCollectionDataModel>>()
    val consentCollection: LiveData<UserConsentStateResult<UserConsentCollectionDataModel>>
        get() = _consentCollection

    fun getConsentCollection(consentCollectionParam: ConsentCollectionParam) {
        launchCatchError(coroutineContext, {
            _consentCollection.value = UserConsentStateResult.Loading()

            val response = getUserConsentCollection(consentCollectionParam)

            if (response.data.success && response.data.collectionPoints.isNotEmpty()) {
                _consentCollection.value = UserConsentStateResult.Success(response.data)
            } else {
                val message = if (response.data.errorMessages.isNotEmpty()) {
                    response.data.errorMessages.first()
                } else {
                    GENERAL_ERROR
                }

                _consentCollection.value = UserConsentStateResult.Fail(Throwable(message))
            }
        }, {
            _consentCollection.value = UserConsentStateResult.Fail(it)
        })
    }

    companion object {
        const val GENERAL_ERROR = "Terjadi kesalahan. Silahkan coba lagi."
    }
}