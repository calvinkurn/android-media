package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.usecase.ChangeVoucherPeriodUseCase
import com.tokopedia.mvc.domain.usecase.GetTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class VoucherEditPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getTokenUseCase: GetTokenUseCase,
    private val changeVoucherPeriodUseCase: ChangeVoucherPeriodUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
    }


    private val _dateStartLiveData = MutableLiveData<String>()
    val dateStartLiveData: LiveData<String>
        get() = _dateStartLiveData
    private val _dateEndLiveData = MutableLiveData<String>()
    val dateEndLiveData: LiveData<String>
        get() = _dateEndLiveData
    private val _hourStartLiveData = MutableLiveData<String>()
    val hourStartLiveData: LiveData<String>
        get() = _hourStartLiveData
    private val _hourEndLiveData = MutableLiveData<String>()
    val hourEndLiveData: LiveData<String>
        get() = _hourEndLiveData

    private val _startDateCalendarLiveData = MutableLiveData<Calendar>()
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = _startDateCalendarLiveData
    private val _endDateCalendarLiveData = MutableLiveData<Calendar>()
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = _endDateCalendarLiveData

    private val _updateVoucherPeriodStateLiveData = MutableLiveData<com.tokopedia.usecase.coroutines.Result<UpdateVoucherResult>>()
    val updateVoucherPeriodStateLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<UpdateVoucherResult>>
        get() = _updateVoucherPeriodStateLiveData

    fun setStartDateTime(startDate: Calendar?) {
        _startDateCalendarLiveData.value = startDate
        _dateStartLiveData.value = startDate?.time?.toFormattedString(DATE_FORMAT)
        _hourStartLiveData.value = startDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateTime(endDate: Calendar?) {
        _endDateCalendarLiveData.value = endDate
        _dateEndLiveData.value = endDate?.time?.toFormattedString(DATE_FORMAT)
        _hourEndLiveData.value = endDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun validateAndUpdateDateTime(voucher: Voucher?) {
        _dateStartLiveData.value?.let { dateStart ->
            _dateEndLiveData.value?.let { dateEnd ->
                _hourStartLiveData.value?.let { hourStart ->
                    _hourEndLiveData.value?.let { hourEnd ->
                        voucher?.let {
                            launchCatchError(dispatchers.io, {
                                val token = getTokenUseCase.executeOnBackground()
                                val response = changeVoucherPeriodUseCase.execute(
                                    voucher,
                                    token,
                                    dateStart,
                                    hourStart,
                                    dateEnd,
                                    hourEnd
                                )
                                _updateVoucherPeriodStateLiveData.value = Success(response)
                            }, onError =  {
                                _updateVoucherPeriodStateLiveData.value = Fail(it)
                            })
                        }
                    }
                }
            }
        }
    }
}
