package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SeePerformanceTopAdsViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase,
) : BaseViewModel(dispatchers.io) {

    private val _topAdsDeposits: MutableLiveData<Result<Deposit>> = MutableLiveData()
    val topAdsDeposits: LiveData<Result<Deposit>> = _topAdsDeposits

    fun getTopAdsDeposit() {
        topAdsGetDepositUseCase.execute({
            _topAdsDeposits.value = Success(it)
        }, {
            _topAdsDeposits.value = Fail(it)
            it.printStackTrace()
        })
    }

}
