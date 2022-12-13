package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.usecase.UpdateCouponFacadeUseCase
import com.tokopedia.mvc.util.DateTimeUtils.DASH_DATE_FORMAT
import com.tokopedia.mvc.util.DateTimeUtils.HOUR_FORMAT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class VoucherEditPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val updateVoucherPeriodUseCase: UpdateCouponFacadeUseCase,
    private val mapper: UpdateVoucherMapper
) : BaseViewModel(dispatchers.main) {

    private val _dateStartLiveData = MutableLiveData<String>()
    private val _dateEndLiveData = MutableLiveData<String>()
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

    private val _updateVoucherPeriodStateLiveData =
        MutableLiveData<com.tokopedia.usecase.coroutines.Result<Boolean>>()
    val updateVoucherPeriodStateLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<Boolean>>
        get() = _updateVoucherPeriodStateLiveData

    fun setStartDateTime(startDate: Calendar?) {
        _startDateCalendarLiveData.value = startDate
        _dateStartLiveData.value = startDate?.time?.toFormattedString(DASH_DATE_FORMAT)
        _hourStartLiveData.value = startDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateTime(endDate: Calendar?) {
        _endDateCalendarLiveData.value = endDate
        _dateEndLiveData.value = endDate?.time?.toFormattedString(DASH_DATE_FORMAT)
        _hourEndLiveData.value = endDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun validateAndUpdateDateTime(voucher: Voucher?) {
        val dateStart = _dateStartLiveData.value ?: return
        val dateEnd = _dateEndLiveData.value ?: return
        val hourStart = _hourStartLiveData.value ?: return
        val hourEnd = _hourEndLiveData.value ?: return
        voucher?.let {
            launchCatchError(
                block = {
                    val result = withContext(dispatchers.io) {
                        updateVoucherPeriodUseCase.execute(
                            mapper.toUpdateVoucher(it, dateStart, dateEnd),
                            emptyList(),
                            dateStart,
                            hourStart,
                            dateEnd,
                            hourEnd
                        )
                    }
                    _updateVoucherPeriodStateLiveData.value = Success(result)
                },
                onError = {
                    _updateVoucherPeriodStateLiveData.value = Fail(it)
                }
            )
        }
    }
}
