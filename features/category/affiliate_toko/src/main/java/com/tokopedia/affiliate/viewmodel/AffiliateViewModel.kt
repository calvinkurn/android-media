package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AffiliateViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
    private val affiliateUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase
) : BaseViewModel() {

    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var progressBar = MutableLiveData<Boolean>()
    private val _unreadNotificationCount = MutableLiveData(Int.ZERO)

    fun getAffiliateValidateUser() {
        launchCatchError(
            block = {
                progressBar.value = true
                affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email).let {
                    validateUserdata.value = it
                }
                progressBar.value = false
            },
            onError = {
                Timber.e(it)
                progressBar.value = false
                errorMessage.value = it
            }
        )
    }

    fun fetchUnreadNotificationCount() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e -> Timber.e(e) }
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            _unreadNotificationCount.value =
                affiliateUnreadNotificationUseCase.getUnreadNotifications()
        }
    }

    fun resetNotificationCount() {
        _unreadNotificationCount.value = Int.ZERO
    }

    fun getUnreadNotificationCount(): LiveData<Int> = _unreadNotificationCount

    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
}
