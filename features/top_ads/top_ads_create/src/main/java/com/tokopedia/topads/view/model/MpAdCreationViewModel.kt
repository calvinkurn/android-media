package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.common.domain.model.TopadsShopInfoV2Model
import com.tokopedia.topads.common.domain.usecase.TopadsGetShopInfoUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class MpAdCreationViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val getShopInfoUseCase: TopadsGetShopInfoUseCase
) : BaseViewModel(dispatchers.io) {

    private val _shopInfoResult:MutableLiveData<Result<TopadsShopInfoV2Model>> = MutableLiveData()
    val shopInfoResult:LiveData<Result<TopadsShopInfoV2Model>> = _shopInfoResult

    fun getShopInfo(shopID:String){
        getShopInfoUseCase.getShopInfo(
            {
                _shopInfoResult.value = Success(it)
            },
            {
                _shopInfoResult.value = Fail(it)
            },
            shopID
        )
    }
}

