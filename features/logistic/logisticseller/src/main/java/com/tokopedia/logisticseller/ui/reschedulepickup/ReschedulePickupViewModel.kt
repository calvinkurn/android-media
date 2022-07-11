package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.domain.mapper.ReschedulePickupMapper
import com.tokopedia.logisticseller.data.model.RescheduleDetailModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.data.model.SaveRescheduleModel
import com.tokopedia.logisticseller.domain.usecase.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.domain.usecase.SaveReschedulePickupUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReschedulePickupViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getReschedulePickup: GetReschedulePickupUseCase,
    private val saveReschedulePickup: SaveReschedulePickupUseCase
) : BaseViewModel(dispatcher.main) {
    private val _reschedulePickupDetail = MutableLiveData<Result<RescheduleDetailModel>>()
    val reschedulePickupDetail: LiveData<Result<RescheduleDetailModel>>
        get() = _reschedulePickupDetail
    private val _saveRescheduleDetail = MutableLiveData<Result<SaveRescheduleModel>>()
    val saveRescheduleDetail: LiveData<Result<SaveRescheduleModel>>
        get() = _saveRescheduleDetail

    fun getReschedulePickupDetail(orderId: String) {
        launchCatchError(
            block = {
                val response = getReschedulePickup(ReschedulePickupMapper.mapToGetReschedulePickupParam(listOf(orderId)))
                if (response.mpLogisticGetReschedulePickup.data.isNotEmpty()) {
                    _reschedulePickupDetail.value = Success(ReschedulePickupMapper.mapToRescheduleDetailModel(response.mpLogisticGetReschedulePickup))
                } else {
                    _reschedulePickupDetail.value = Fail(Throwable("Data Reschedule Pickup tidak ditemukan"))
                }
            },
            onError = { _reschedulePickupDetail.value = Fail(it) }
        )
    }

    fun saveReschedule(orderId: String, date: String, time: RescheduleTimeOptionModel, reason: String) {
        launchCatchError(
            block = {
                val response = saveReschedulePickup(ReschedulePickupMapper.mapToSaveReschedulePickupParam(orderId, date, time.time, reason))
                _saveRescheduleDetail.value = Success(ReschedulePickupMapper.mapToSaveRescheduleModel(response, time.etaPickup, orderId))
            },
            onError = {
                _saveRescheduleDetail.value = Fail(it)
            }
        )
    }
}