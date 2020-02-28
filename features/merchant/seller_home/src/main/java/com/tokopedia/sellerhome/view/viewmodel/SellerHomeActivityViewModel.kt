package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.usecase.GetStatusShopUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

class SellerHomeActivityViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getShopStatusUseCase: GetStatusShopUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : CustomBaseViewModel(dispatcher) {

    private val _shopStatus = MutableLiveData<Result<GetShopStatusResponse>>()
    val shopStatus: LiveData<Result<GetShopStatusResponse>>
        get() = _shopStatus

    fun getShopStatus() = executeCall(_shopStatus) {
        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams(userSession.shopId)
        getShopStatusUseCase.executeOnBackground()
    }

}