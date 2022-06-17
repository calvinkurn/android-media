package com.tokopedia.logisticseller.reschedulepickup.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.reschedulepickup.data.mapper.ReschedulePickupMapper
import com.tokopedia.logisticseller.reschedulepickup.data.model.RescheduleDetailModel
import com.tokopedia.logisticseller.reschedulepickup.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.reschedulepickup.data.model.SaveRescheduleModel
import com.tokopedia.logisticseller.reschedulepickup.domain.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.reschedulepickup.domain.SaveReschedulePickupUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReschedulePickupViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getReschedulePickupUseCase: GetReschedulePickupUseCase,
    private val saveReschedulePickupUseCase: SaveReschedulePickupUseCase
) : BaseViewModel(dispatcher.io) {
    private val _reschedulePickupDetail = MutableLiveData<Result<RescheduleDetailModel>>()
    val reschedulePickupDetail: LiveData<Result<RescheduleDetailModel>>
        get() = _reschedulePickupDetail
    private val _saveRescheduleDetail = MutableLiveData<Result<SaveRescheduleModel>>()
    val saveRescheduleDetail: LiveData<Result<SaveRescheduleModel>>
        get() = _saveRescheduleDetail

    fun getReschedulePickupDetail(orderId: String) {
        launchCatchError(
            block = {
                val response = getReschedulePickupUseCase(ReschedulePickupMapper.mapToGetReschedulePickupParam(listOf(orderId)))
                if (response.mpLogisticGetReschedulePickup.data.isNotEmpty()) {
                    _reschedulePickupDetail.postValue(Success(ReschedulePickupMapper.mapToRescheduleDetailModel(response.mpLogisticGetReschedulePickup)))
                } else {
                    _reschedulePickupDetail.postValue(Fail(Throwable("Data Reschedule Pickup tidak ditemukan")))
                }
            },
            onError = { _reschedulePickupDetail.postValue(Fail(it)) }
        )
    }

    fun saveReschedule(orderId: String, date: String, time: RescheduleTimeOptionModel, reason: String) {
        launchCatchError(
            block = {
                val response = saveReschedulePickupUseCase.execute(ReschedulePickupMapper.mapToSaveReschedulePickupParam(orderId, date, time.time, reason))
                _saveRescheduleDetail.postValue(Success(ReschedulePickupMapper.mapToSaveRescheduleModel(response, time.etaPickup, orderId)))
            },
            onError = {
                _saveRescheduleDetail.postValue(Fail(it))
            }
        )
    }
}