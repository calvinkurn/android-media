package com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup.domain.GetOvoTopUpUrlUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OvoTopUpWebViewViewModel @Inject constructor(private val getOvoTopUpUrlUseCase: GetOvoTopUpUrlUseCase,
                                                   private val userSession: UserSessionInterface) : ViewModel() {

    private val _ovoTopUpUrl: OccMutableLiveData<OccState<String>> = OccMutableLiveData(OccState.Loading)
    val ovoTopUpUrl: LiveData<OccState<String>>
        get() = _ovoTopUpUrl

    fun getOvoTopUpUrl(redirectUrl: String) {
        getOvoTopUpUrlUseCase.execute(userSession.name, userSession.email, userSession.phoneNumber, redirectUrl,
                { url -> _ovoTopUpUrl.value = OccState.Success(url) },
                { throwable -> _ovoTopUpUrl.value = OccState.Failed(Failure(throwable)) })
    }
}