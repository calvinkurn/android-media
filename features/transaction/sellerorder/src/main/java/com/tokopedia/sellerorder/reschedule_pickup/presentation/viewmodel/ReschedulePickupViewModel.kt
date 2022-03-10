package com.tokopedia.sellerorder.reschedule_pickup.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.reschedule_pickup.data.mapper.ReschedulePickupMapper
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDetailModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveRescheduleModel
import com.tokopedia.sellerorder.reschedule_pickup.domain.GetReschedulePickupUseCase
import com.tokopedia.sellerorder.reschedule_pickup.domain.SaveReschedulePickupUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReschedulePickupViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getReschedulePickupUseCase: GetReschedulePickupUseCase,
    private val saveReschedulePickupUseCase: SaveReschedulePickupUseCase
) : BaseViewModel(dispatcher.io) {
    private val _reschedulePickupDetail =
        MutableLiveData<Result<RescheduleDetailModel>>()
    val reschedulePickupDetail: LiveData<Result<RescheduleDetailModel>>
        get() = _reschedulePickupDetail
    private val _saveRescheduleDetail =
        MutableLiveData<Result<SaveRescheduleModel>>()
    val saveRescheduleDetail: LiveData<Result<SaveRescheduleModel>>
        get() = _saveRescheduleDetail

    fun getReschedulePickupDetail(orderId: String) {
        launchCatchError(
            block = {
                _reschedulePickupDetail.postValue(
                    Success(
                        ReschedulePickupMapper.mapToRescheduleDetailModel(
                            getReschedulePickupUseCase.execute(
                                ReschedulePickupMapper.mapToGetReschedulePickupParam(
                                    listOf(orderId)
                                )
                            ).mpLogisticGetReschedulePickup.data.first()
                        )
                    )
                )
            },
            onError = {
                _reschedulePickupDetail.postValue(Fail(it))
            }
        )
    }

    fun saveReschedule(orderId: String, date: String, time: String, reason: String) {
        launchCatchError(
            block = {
                _saveRescheduleDetail.postValue(
                    Success(
                        ReschedulePickupMapper.mapToSaveRescheduleModel(
                            saveReschedulePickupUseCase.execute(
                                ReschedulePickupMapper.mapToSaveReschedulePickupParam(
                                    orderId, date, time, reason
                                )
                            )
                        )
                    )
                )
            },
            onError = {
                _saveRescheduleDetail.postValue(Fail(it))
            }
        )
    }
}