package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.UpdateQuotaUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditQuotaViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val updateQuotaUseCase: UpdateQuotaUseCase): BaseViewModel(dispatchers.main) {

    private val mQuotaLiveData = MutableLiveData<Pair<Int, Int>>()

    private val mEditQuotaSuccessLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(mQuotaLiveData) {
            launchCatchError(
                    block = {
                        updateQuotaUseCase.params = UpdateQuotaUseCase.createRequestParam(it.first, it.second)
                        value = Success(withContext(dispatchers.io) {
                            updateQuotaUseCase.executeOnBackground()
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val editQuotaSuccessLiveData: LiveData<Result<Boolean>>
        get() = mEditQuotaSuccessLiveData

    fun changeQuotaValue(voucherId: Int,
                         quota: Int) {
        mQuotaLiveData.value = Pair(voucherId, quota)
    }

}