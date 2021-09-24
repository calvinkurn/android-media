package com.tokopedia.home_account.linkaccount.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.linkaccount.domain.GetUserProfile
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class LinkAccountViewModel @Inject constructor(
    private val getLinkStatusUseCase: GetLinkStatusUseCase,
    private val getProfileUseCase: GetUserProfile,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io), LifecycleObserver {

    private val _linkStatus = MutableLiveData<Result<LinkStatusResponse>>()
    val linkStatus: LiveData<Result<LinkStatusResponse>> get() = _linkStatus

    fun getLinkStatus(isGetProfile: Boolean = false) {
        launchCatchError(block = {
            val result = getLinkStatusUseCase(GetLinkStatusUseCase.ACCOUNT_LINKING_TYPE)
            if(isGetProfile) {
                val profile = getProfileUseCase(Unit)
                val phone = profile.profileInfo.phone
                if(phone.isNotEmpty()) {
                    userSession.phoneNumber = phone
                    result.response.linkStatus.forEach { it.phoneNo = phone }
                }
            }

            _linkStatus.postValue(Success(result))
        }, onError = {
            _linkStatus.postValue(Fail(it))
        })
    }
}