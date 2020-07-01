package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.iid.FirebaseInstanceId
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.domain.UpdateTokenUseCase
import com.tokopedia.troubleshooter.notification.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.entity.UpdateFcmTokenResponse
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TroubleshootContract {
    fun getNewToken(token: (String) -> Unit)
    fun updateToken(newToken: String)
    fun troubleshoot()
}

class TroubleshootViewModel @Inject constructor(
        private val troubleshootUseCase: TroubleshootStatusUseCase,
        private val updateTokenUseCase: UpdateTokenUseCase,
        private val sharedPreferences: SharedPreferences,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), TroubleshootContract {

    private val _troubleshoot = MutableLiveData<NotificationSendTroubleshoot>()
    val troubleshoot: LiveData<NotificationSendTroubleshoot> get() = _troubleshoot

    private val _updateToken = MutableLiveData<UpdateFcmTokenResponse>()
    val updateToken: LiveData<UpdateFcmTokenResponse> get() = _updateToken

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    override fun troubleshoot() {
        launchCatchError(block = {
            val result = troubleshootUseCase(RequestParams.EMPTY)
            withContext(dispatcher.main()) {
                _troubleshoot.value = result.notificationSendTroubleshoot
            }
        }, onError = {
            _error.value = it
        })
    }

    override fun updateToken(newToken: String) {
        val currentToken = sharedPreferences.getString(FCM_TOKEN, "") ?: return
        val param = updateTokenUseCase.params(currentToken, newToken)

        launchCatchError(block = {
            val result = updateTokenUseCase(param)
            withContext(dispatcher.main()) {
                _updateToken.value = result
            }
        }, onError = {
            _error.value = it
        })
    }

    override fun getNewToken(token: (String) -> Unit) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener
            if (task.result?.token == null) return@addOnCompleteListener
            task.result?.token?.let { newToken ->
                token(newToken)
            }
        }
    }

    companion object {
        private const val FCM_TOKEN = "pref_fcm_token"
    }

}