package com.tokopedia.usercomponents.userconsent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.ConsentStateResult
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.GetConsentCollectionUseCase
import javax.inject.Inject

class UserConsentViewModel @Inject constructor(
    private val getUserConsentCollectionUseCase: GetConsentCollectionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _consentCollection = MutableLiveData<ConsentStateResult<ConsentCollectionDataModel>>()
    val consentCollection: LiveData<ConsentStateResult<ConsentCollectionDataModel>>
        get() = _consentCollection

    fun getConsentCollection(consentCollectionParam: ConsentCollectionParam) {
        launchCatchError(coroutineContext, {
            _consentCollection.value = ConsentStateResult.Loading()

            val response = getUserConsentCollectionUseCase(consentCollectionParam)

            if (response.data.success && response.data.collectionPoints.isNotEmpty()) {
                _consentCollection.value = ConsentStateResult.Success(response.data)
            } else {
                val message = if (response.data.errorMessages.isNotEmpty()) {
                    response.data.errorMessages[0]
                } else {
                    "Terjadi kesalahan pada sistem. Silahkan coba lagi."
                }

                _consentCollection.value = ConsentStateResult.Fail(Throwable(message))
            }
        }, {
            _consentCollection.value = ConsentStateResult.Fail(it)
        })
    }
}