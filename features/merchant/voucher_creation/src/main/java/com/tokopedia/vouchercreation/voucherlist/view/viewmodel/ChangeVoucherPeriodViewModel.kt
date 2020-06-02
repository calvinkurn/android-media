package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ChangeVoucherPeriodUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetTokenUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ChangeVoucherPeriodViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                       private val changeVoucherPeriodUseCase: ChangeVoucherPeriodUseCase,
                                                       private val getTokenUseCase: GetTokenUseCase) : BaseViewModel(dispatcher) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
    }

    private val mVoucherUiModel = MutableLiveData<VoucherUiModel>()

    private val mDateStartLiveData = MutableLiveData<String>()
    private val mDateEndLiveData = MutableLiveData<String>()
    private val mHourStartLiveData = MutableLiveData<String>()
    private val mHourEndLiveData = MutableLiveData<String>()

    private val mStartDateCalendarLiveData = MutableLiveData<Calendar>()
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = mStartDateCalendarLiveData
    private val mEndDateCalendarLiveData = MutableLiveData<Calendar>()
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = mEndDateCalendarLiveData

    private val mUpdateVoucherSuccessLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(mVoucherUiModel) { uiModel ->
            mDateStartLiveData.value?.let { dateStart ->
                mDateEndLiveData.value?.let { dateEnd ->
                    mHourStartLiveData.value?.let { hourStart ->
                        mHourEndLiveData.value?.let { hourEnd ->
                            launchCatchError(
                                    block = {
                                        val token = getTokenUseCase.executeOnBackground()
                                        value = Success(withContext(Dispatchers.IO) {
                                            changeVoucherPeriodUseCase.params =
                                                    ChangeVoucherPeriodUseCase.createRequestParam(
                                                            uiModel,
                                                            token,
                                                            dateStart,
                                                            hourStart,
                                                            dateEnd,
                                                            hourEnd)
                                            changeVoucherPeriodUseCase.executeOnBackground()
                                        })
                                    },
                                    onError = {
                                        value = Fail(it)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
    val updateVoucherSuccessLiveData: LiveData<Result<Boolean>>
        get() = mUpdateVoucherSuccessLiveData

    fun setStartDateCalendar(startDate: Calendar) {
        mStartDateCalendarLiveData.value = startDate
        mDateStartLiveData.value = startDate.time.toFormattedString(DATE_FORMAT)
        mHourStartLiveData.value = startDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateCalendar(endDate: Calendar) {
        mEndDateCalendarLiveData.value = endDate
        mDateEndLiveData.value = endDate.time.toFormattedString(DATE_FORMAT)
        mHourEndLiveData.value = endDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun validateVoucherPeriod(uiModel: VoucherUiModel) {
        mVoucherUiModel.value = uiModel
    }

}