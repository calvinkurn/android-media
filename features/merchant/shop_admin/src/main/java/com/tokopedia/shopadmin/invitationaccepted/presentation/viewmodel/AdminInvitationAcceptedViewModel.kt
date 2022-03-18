package com.tokopedia.shopadmin.invitationaccepted.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.invitationaccepted.domain.usecase.GetAdminManagementInfoListUseCase
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.InvitationAcceptedUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminInvitationAcceptedViewModel @Inject constructor(
    private val getAdminManagementInfoListUseCase: GetAdminManagementInfoListUseCase,
    private val coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.main) {

    private val _adminInvitationAccepted = MutableLiveData<Result<InvitationAcceptedUiModel>>()
    val adminInvitationAccepted: LiveData<Result<InvitationAcceptedUiModel>>
        get() = _adminInvitationAccepted

    fun getAdminWelcome(shopId: String) {
        launchCatchError(block = {
            val invitationAcceptedResponse = withContext(coroutineDispatchers.io) {
                getAdminManagementInfoListUseCase.execute(shopId)
            }
            _adminInvitationAccepted.value = Success(invitationAcceptedResponse)
        }, onError = {
            _adminInvitationAccepted.value = Fail(it)
        })
    }
}