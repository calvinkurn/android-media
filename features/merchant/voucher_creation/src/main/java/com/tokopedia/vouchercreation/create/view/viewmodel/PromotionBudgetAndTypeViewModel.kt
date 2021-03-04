package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.common.domain.usecase.BasicShopInfoUseCase
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromotionBudgetAndTypeViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val basicShopInfoUseCase: BasicShopInfoUseCase,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main){

    private val mBasicShopInfoLiveData = MutableLiveData<Result<ShopInfo>>()
    val basicShopInfoLiveData : LiveData<Result<ShopInfo>>
        get() = mBasicShopInfoLiveData

    fun getBasicShopInfo() {
        launchCatchError(
                block = {
                    mBasicShopInfoLiveData.value = Success(withContext(dispatchers.io) {
                        val userId = userSession.userId.toInt()
                        basicShopInfoUseCase.params = BasicShopInfoUseCase.createRequestParams(userId)
                        basicShopInfoUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mBasicShopInfoLiveData.value = Fail(it)
                }
        )
    }

}