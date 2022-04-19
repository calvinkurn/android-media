package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.presentation.util.AdminPermissionMapper
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminRoleAuthorizeViewModel @Inject constructor(
        private val authorizeAccessUseCase: AuthorizeAccessUseCase,
        private val userSession: UserSessionInterface,
        private val mapper: AdminPermissionMapper,
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val accessIdsLiveData = MutableLiveData<Int>()

    private val _isRoleAuthorizedLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(accessIdsLiveData) { accessId ->
            launchCatchError(
                    block = {
                        value = authorizeAccess(accessId)
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val isRoleAuthorizedLiveData: LiveData<Result<Boolean>>
        get() = _isRoleAuthorizedLiveData

    private val _isLoadingLiveData = MediatorLiveData<Boolean>().apply {
        addSource(_isRoleAuthorizedLiveData) {
            value = false
        }
    }
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData

    fun checkAccess(@AdminFeature adminFeature: String) {
        accessIdsLiveData.value = mapper.mapFeatureToAccessId(adminFeature)
    }

    private suspend fun authorizeAccess(accessId: Int): Result<Boolean>{
        _isLoadingLiveData.value = true
        return Success(
                withContext(dispatchers.io) {
                    if (userSession.isShopOwner) {
                        true
                    } else {
                        AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), accessId).let { requestParams ->
                            authorizeAccessUseCase.execute(requestParams)
                        }
                    }
                }
        )
    }

}