package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PeriodValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PeriodValidation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SetVoucherPeriodViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val periodValidationUseCase: PeriodValidationUseCase
) : BaseViewModel(dispatcher) {

    private val mDateStartLiveData = MutableLiveData<String>()
    private val mDateEndLiveData = MutableLiveData<String>()
    private val mHourStartLiveData = MutableLiveData<String>()
    private val mHourEndLiveData = MutableLiveData<String>()

    private val mPeriodValidationLiveData = MutableLiveData<Result<PeriodValidation>>()
    val periodValidationLiveData: LiveData<Result<PeriodValidation>>
        get() = mPeriodValidationLiveData

    fun setStartPeriod(dateStart: String,
                       hourStart: String) {
        mDateStartLiveData.value = dateStart
        mHourStartLiveData.value = hourStart
    }

    fun setEndPeriod(dateEnd: String,
                     hourEnd: String) {
        mDateEndLiveData.value = dateEnd
        mHourEndLiveData.value = hourEnd
    }

    fun validateVoucherPeriod() {
        mDateStartLiveData.value?.let { dateStart ->
            mDateEndLiveData.value?.let { dateEnd ->
                mHourStartLiveData.value?.let { hourStart ->
                    mHourEndLiveData.value?.let { hourEnd ->
                        launchCatchError(
                                block = {
                                    mPeriodValidationLiveData.value = Success(withContext(Dispatchers.IO) {
                                        periodValidationUseCase.params = PeriodValidationUseCase.createRequestParam(dateStart, dateEnd, hourStart, hourEnd)
                                        periodValidationUseCase.executeOnBackground()
                                    })
                                },
                                onError = {
                                    mPeriodValidationLiveData.value = Fail(it)
                                }
                        )
                    }
                }
            }
        }
    }

}