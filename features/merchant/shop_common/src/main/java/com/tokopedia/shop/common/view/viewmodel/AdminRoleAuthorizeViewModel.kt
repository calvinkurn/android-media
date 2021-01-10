package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.common.constant.AccessId
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
        private val dispatchers: CoroutineDispatchersProvider): BaseViewModel(dispatchers.main) {

    private val accessIdLiveData = MutableLiveData<Int>()

    private val _isRoleAuthorizedLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(accessIdLiveData) { id ->
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

    fun checkAccess(@AccessId accessId: Int) {
        accessIdLiveData.value = accessId
    }

    private suspend fun authorizeAccess(accessId: Int): Result<Boolean>{
        return Success(
                withContext(dispatchers.io) {
                    if (userSession.isShopOwner) {
                        true
                    } else {
                        authorizeAccessUseCase.run {
                            val requestParams = AuthorizeAccessUseCase.createRequestParams(
                                    userSession.shopId.toIntOrNull() ?: 0, accessId)
                            authorizeAccessUseCase.execute(requestParams)
                        }
                    }
                }
        )
    }

}