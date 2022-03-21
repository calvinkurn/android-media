package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase.GetAdminPermissionListUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminInvitationAcceptedViewModel @Inject constructor(
    private val getAdminPermissionListUseCase: Lazy<GetAdminPermissionListUseCase>,
    private val coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.main) {

    private val _adminPermission = MutableLiveData<Result<List<AdminPermissionUiModel>>>()
    val adminPermission: LiveData<Result<List<AdminPermissionUiModel>>>
        get() = _adminPermission

    fun getAdminPermission(shopId: String) {
        launchCatchError(block = {
            val invitationAcceptedResponse = withContext(coroutineDispatchers.io) {
                getAdminPermissionListUseCase.get().execute(shopId)
            }
            _adminPermission.value = Success(invitationAcceptedResponse)
        }, onError = {
            _adminPermission.value = Fail(it)
        })
    }
}