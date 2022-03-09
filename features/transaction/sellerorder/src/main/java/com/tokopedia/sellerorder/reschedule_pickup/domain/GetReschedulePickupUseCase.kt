package com.tokopedia.sellerorder.reschedule_pickup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import javax.inject.Inject

class GetReschedulePickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<GetReschedulePickupResponse.Data>) {

    init {
        useCase.setTypeClass(GetReschedulePickupResponse.Data::class.java)
    }

    suspend fun execute(param: GetReschedulePickupParam): GetReschedulePickupResponse.Data {
//        useCase.setGraphqlQuery(GetReschedulePickupQuery)
//        useCase.setRequestParams(generateParam(param))
//        return useCase.executeOnBackground()
        return GetReschedulePickupResponse.Data(
            mpLogisticGetReschedulePickup = GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup(
                data = listOf(
                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem(
                        orderData = listOf(
                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData(
                                chooseDay = listOf(
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption(
                                        day = "Kamis, 9 Desember 2021",
                                        chooseTime = listOf(
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "08:00 WIB"
                                            ),
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "09:00 WIB"
                                            ),
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "10:00 WIB"
                                            )
                                        )
                                    ),
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption(
                                        day = "Jumat, 10 Desember 2021",
                                        chooseTime = listOf(
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "11:00 WIB"
                                            ),
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "12:00 WIB"
                                            ),
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "13:00 WIB"
                                            )
                                        )
                                    ),
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption(
                                        day = "Senin, 13 Desember 2021",
                                        chooseTime = listOf(
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "14:00 WIB"
                                            ),
                                            GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption(
                                                time = "15:00 WIB"
                                            ),
                                        )
                                    )
                                ),
                                chooseReason = listOf(
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption(
                                        reason = "Toko Tutup"
                                    ),
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption(
                                        reason = "Pembeli Tidak Ditempat"
                                    ),
                                    GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption(
                                        reason = "Lainnya (Isi Sendiri)"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    private fun generateParam(param: GetReschedulePickupParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }
}