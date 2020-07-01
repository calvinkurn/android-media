package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import com.tokopedia.vouchercreation.create.domain.usecase.BasicShopInfoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromotionBudgetAndTypeViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val basicShopInfoUseCase: BasicShopInfoUseCase,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatcher){

    private val mBasicShopInfoLiveData = MutableLiveData<Result<ShopInfo>>()
    val basicShopInfoLiveData : LiveData<Result<ShopInfo>>
        get() = mBasicShopInfoLiveData

    fun getBasicShopInfo() {
        launchCatchError(
                block = {
                    mBasicShopInfoLiveData.value = Success(withContext(Dispatchers.IO) {
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