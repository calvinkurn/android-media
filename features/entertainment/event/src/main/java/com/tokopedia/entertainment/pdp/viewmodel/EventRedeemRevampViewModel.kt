package com.tokopedia.entertainment.pdp.viewmodel

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.data.redeem.ErrorRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class EventRedeemRevampViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getEventRedeemUseCase: GetEventRedeemUseCase
) : BaseViewModel(dispatcher.main) {

    private val _inputRedeemUrl = MutableSharedFlow<String>(Int.ONE)

    val flowRedeemData: SharedFlow<Result<EventRedeem>> =
        _inputRedeemUrl.flatMapConcat {
            flow {
                emit(getRedeemData(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun setInputRedeemUrl(redeemUrl: String) {
        _inputRedeemUrl.tryEmit(redeemUrl)
    }

    private suspend fun getRedeemData(redeemUrl: String): Result<EventRedeem> {
//        getEventRedeemUseCase.setUrlRedeem(redeemUrl)
//        val response = withContext(dispatcher.io) {
//            getEventRedeemUseCase.executeOnBackground()
//        }
//        val value = response[EventRedeem::class.java]
//
//        return if (value?.code == SUCCESS_CODE && !value.isError) {
//            Success(convertToRedeemResponse(response))
//        } else {
//            val error = convertToErrorResponse(response)
//            Fail(MessageErrorException(error))
//        }
        val dummy = Gson().fromJson(DUMMY, EventRedeem::class.java)
        return Success(dummy)
    }

    private fun convertToRedeemResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeem {
        return typeRestResponseMap[EventRedeem::class.java]?.getData() as EventRedeem
    }

    private fun convertToErrorResponse(typeRestResponseMap: Map<Type, RestResponse?>): String? {
        val errorBody = typeRestResponseMap[EventRedeem::class.java]?.errorBody ?: ""
        val errorRedeem = Gson().fromJson(errorBody, ErrorRedeem::class.java)
        return errorRedeem.messageError.firstOrNull()
    }

    companion object{
        private const val SUCCESS_CODE = 200
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val DUMMY = """
            {
    "data": {
        "product": {
            "id": 378,
            "category_id": 1,
            "provider_id": 3,
            "facility_group_id": 0,
            "provider_product_id": "Testing offline events-1-3",
            "provider_product_code": "0",
            "display_name": "Testing offline events",
            "title": "Testing offline events",
            "url": "testing-offline-event",
            "seo_url": "testing-offline-event",
            "image_web": "https://s3-ap-southeast-1.amazonaws.com/loket-production-sg/images/banner/20180228074058.png",
            "thumbnail_web": "https://ecs7.tokopedia.net/img/banner/2018/3/8/5253715/5253715_59dd6dd4-fe55-4d7c-a9dd-6ea93a9ff951.jpg",
            "image_app": "https://s3-ap-southeast-1.amazonaws.com/loket-production-sg/images/banner/20180228074058.png",
            "thumbnail_app": "https://ecs7.tokopedia.net/img/banner/2018/3/8/5253715/5253715_59dd6dd4-fe55-4d7c-a9dd-6ea93a9ff951.jpg",
            "tnc": "~~ Before purchasing your ticket, please make sure that purchasing date is not less than 7 days before the event date.\n~ Tickets cannot be exchanged or refunded.\n~ Please ensure that you enter a valid email address and mobile phone number.\n~ We will send you a confirmation email after payment completed.\n~ Please note that this email is not a ticket and will not give you access. This email need to be redeemed for a ticket at the venue.\n~ Make sure you bring either a printout of this voucher to the venue to guarantee entry\n~ Internet handling fee will be charged per ticket. Please check your total amount before payment.\n~ We recommend that you arrive at the venue at least 20 minutes before the event start to redeemed your physical tickets.\n~ The amount paid is only for the ticket entry, not include any accommodation (Transportation, hotel etc).",
            "mrp": 0,
            "sales_price": 0,
            "is_free": 0,
            "min_start_date": 1577880000,
            "max_end_date": 1735732800,
            "created_at": "2022-11-07T11:44:48+07:00",
            "updated_at": "2022-11-07T11:44:48+07:00"
        },
        "schedule": {
            "show_data": "01 Jan 2020 19:00",
            "name": "Testing Free 1",
            "description": "Tiket berlaku untuk 1 orang. Harap membaca syarat dan ketentuan."
        },
        "user": {
            "name": "Alif"
        },
        "action": [
            {
                "label": "Voucher already redeemed",
                "ui_control": "text",
                "button_type": "text",
                "url_params": {},
                "weight": 0,
                "key": "message",
                "value": "Voucher already redeemed",
                "color": "red",
                "text_color": "red",
                "border_color": ""
            }
        ],
        "redemptions": [
            {
                "id": 1,
                "day": 1,
                "redemption_time": 0,
                "participant_details": [
                    {
                        "label": "Nama",
                        "value": "Alif"
                    }
                ]
            },
            {
                "id": 2,
                "day": 2,
                "redemption_time": 1667796268,
                "participant_details": [
                    {
                        "label": "Nama",
                        "value": "Fila"
                    }
                ]
            },
            {
                "id": 3,
                "day": 3,
                "redemption_time": 1667796268,
                "participant_details": [
                    {
                        "label": "Nama",
                        "value": "Fila"
                    }
                ]
            },
            {
                "id": 4,
                "day": 3,
                "redemption_time": 1667796268,
                "participant_details": [
                    {
                        "label": "Nama",
                        "value": "Fila"
                    },
                    {
                        "label": "NIK",
                        "value": "330303251296003439443"
                    },
                    {
                        "label": "NIK",
                        "value": "330303251296003439443"
                    },
                    {
                        "label": "NIK",
                        "value": "330303251296003439443"
                    }
                ]
            }
        ],
        "quantity": 2
    },
    "status": "OK",
    "config": null,
    "server_process_time": "270.807875ms"
}
        """
    }
}
