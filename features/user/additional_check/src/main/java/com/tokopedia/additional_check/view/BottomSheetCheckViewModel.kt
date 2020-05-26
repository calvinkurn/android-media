package com.tokopedia.additional_check.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.additional_check.data.BottomSheetModel
import com.tokopedia.additional_check.data.GetObjectPojo
import com.tokopedia.additional_check.domain.usecase.AdditionalCheckUseCase
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class BottomSheetCheckViewModel @Inject constructor (@Named(SessionModule.SESSION_MODULE) private val userSession: UserSessionInterface,
                                                     private val additionalCheckUseCase: AdditionalCheckUseCase,
                                                     dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    private val mutableGetDataResponse = MutableLiveData<Result<GetObjectPojo>>()
    val getDataResponse: LiveData<Result<GetObjectPojo>>
        get() = mutableGetDataResponse

    fun check() {
        additionalCheckUseCase.getMockBottomSheetSuccess(onSuccess = {
            mutableGetDataResponse.value = Success(it)
        }, onError = {
            Fail(it)
        })
    }
}