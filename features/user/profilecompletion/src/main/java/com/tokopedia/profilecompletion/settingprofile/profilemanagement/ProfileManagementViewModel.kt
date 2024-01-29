package com.tokopedia.profilecompletion.settingprofile.profilemanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.domain.GetGotoCookieResult
import com.tokopedia.profilecompletion.domain.GetGotoCookieUseCase
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

class ProfileManagementViewModel @Inject constructor(
    private val getGotoCookieUseCase: GetGotoCookieUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val url: String = "${TokopediaUrl.getInstance().GOTO_ACCOUNTS}/profile/web/"

    private val _getUrlProfileManagement = MutableLiveData<GetGotoCookieResult>()
    val getUrlProfileManagement: LiveData<GetGotoCookieResult> get() = _getUrlProfileManagement

    fun getProfileManagementData() {
        _getUrlProfileManagement.value = GetGotoCookieResult.Loading
        launchCatchError(
            block = {
                val response = getGotoCookieUseCase.invoke(url)
                _getUrlProfileManagement.value = response
            }, onError = {
                _getUrlProfileManagement.value = GetGotoCookieResult.Failed(it)
            }
        )
    }

}
