package com.tokopedia.saldodetails.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.saldodetails.di.DispatcherModule
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus
import com.tokopedia.saldodetails.response.model.LiveDataResult
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class MerchantSaldoPriorityViewModel @Inject constructor(
        val setMerchantSaldoStatusUseCase: SetMerchantSaldoStatus,
        @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
        @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher
): BaseViewModel(uiDispatcher) {

    val gqlUpdateSaldoStatusLiveData: MutableLiveData<LiveDataResult<GqlSetMerchantSaldoStatus>> = MutableLiveData()

    fun updateSellerSaldoStatus(value: Boolean) {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = setMerchantSaldoStatusUseCase.updateStatus(value)
                response.merchantSaldoStatus?.value = value
                gqlUpdateSaldoStatusLiveData.postValue(LiveDataResult.success(response))
            }
        }, onError = {
                gqlUpdateSaldoStatusLiveData.postValue(LiveDataResult.error(it))
        })
    }
}
