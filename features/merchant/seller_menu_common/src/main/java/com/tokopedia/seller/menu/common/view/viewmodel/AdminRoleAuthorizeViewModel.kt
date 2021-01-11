package com.tokopedia.seller.menu.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.domain.usecase.AdminPermissionUseCase
import com.tokopedia.seller.menu.common.view.mapper.AdminPermissionMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminRoleAuthorizeViewModel @Inject constructor(
        private val adminPermissionUseCase: AdminPermissionUseCase,
        private val userSession: UserSessionInterface,
        private val mapper: AdminPermissionMapper,
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val permissionIdsLiveData = MutableLiveData<List<String>>()

    private val _isRoleAuthorizedLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(permissionIdsLiveData) { id ->
            launchCatchError(
                    block = {
                        value = authorizeAccess(id)
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val isRoleAuthorizedLiveData: LiveData<Result<Boolean>>
        get() = _isRoleAuthorizedLiveData

    fun checkAccess(@AdminFeature adminFeature: String) {
        permissionIdsLiveData.value = mapper.mapFeatureToPermissionList(adminFeature)
    }

    private suspend fun authorizeAccess(permissionList: List<String>): Result<Boolean>{
        return Success(
                withContext(dispatchers.io) {
                    if (userSession.isShopOwner) {
                        true
                    } else {
                        adminPermissionUseCase.run {
                            permissionIds = permissionList
                            execute()
                        }
                    }
                }
        )
    }

}