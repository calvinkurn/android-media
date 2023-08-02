package com.tokopedia.profilecompletion.profilemanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementResult
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementUseCase
import javax.inject.Inject

class ProfileManagementViewModel @Inject constructor(
    private val getUrlProfileManagementUseCase: GetUrlProfileManagementUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    var url: String = ""
        private set

    private val _getAuth = MutableLiveData<GetUrlProfileManagementResult>()
    val getAuth: LiveData<GetUrlProfileManagementResult> get() = _getAuth

    fun getProfileManagementData() {
        _getAuth.value = GetUrlProfileManagementResult.Loading
        launchCatchError(
            block = {
                val response = getUrlProfileManagementUseCase.invoke(Unit)

                if (response is GetUrlProfileManagementResult.Success) {
                    url = response.url
                }

                _getAuth.value = response
            }, onError = {
                _getAuth.value = GetUrlProfileManagementResult.Failed
            }
        )
    }

}
