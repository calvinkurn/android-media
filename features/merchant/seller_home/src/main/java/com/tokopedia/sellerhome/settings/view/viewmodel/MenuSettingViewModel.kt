package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuSettingViewModel @Inject constructor(
        private val authorizeAccessUseCase: AuthorizeAccessUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main), LifecycleObserver {

    private val mShopSettingAccessLiveData = MutableLiveData<Result<Boolean>>()
    val shopSettingAccessLiveData: LiveData<Result<Boolean>>
        get() = mShopSettingAccessLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        checkShopSettingAccess()
    }

    private fun checkShopSettingAccess() {
        launchCatchError(
                block = {
                    userSession.shopId.toIntOrZero().let { shopId ->
                        AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING).let { requestParams ->
                            mShopSettingAccessLiveData.postValue(Success(withContext(dispatchers.io) {
                                authorizeAccessUseCase.execute(requestParams)
                            }))
                        }
                    }
                },
                onError = {
                    mShopSettingAccessLiveData.value = Fail(it)
                }
        )
    }

}