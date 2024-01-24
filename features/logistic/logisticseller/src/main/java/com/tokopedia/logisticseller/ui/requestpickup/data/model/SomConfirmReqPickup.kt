package com.tokopedia.logisticseller.ui.requestpickup.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2019-11-12.
 */
data class SomConfirmReqPickup(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("mpLogisticPreShipInfo")
        @Expose
        val mpLogisticPreShipInfo: MpLogisticPreShipInfo = MpLogisticPreShipInfo()
    ) {

        @Parcelize
        data class MpLogisticPreShipInfo(
            @SerializedName("status")
            @Expose
            val status: String = "",

            @SerializedName("message_error")
            @Expose
            val listErrorMsg: List<String> = listOf(),

            @SerializedName("data")
            @Expose
            val dataSuccess: DataSuccess = DataSuccess()
        ) : Parcelable {

            @Parcelize
            data class DataSuccess(
                @SerializedName("pickup_location")
                @Expose
                val pickupLocation: PickupLocation = PickupLocation(),

                @SerializedName("drop_off_location")
                @Expose
                val dropOffLocation: DropOffLocation = DropOffLocation(),

                @SerializedName("detail")
                @Expose
                val detail: Detail = Detail(),

                @SerializedName("notes")
                @Expose
                val notes: Notes = Notes(),

                @SerializedName("schedule_time_day")
                @Expose
                val schedule_time: ScheduleTime = ScheduleTime(),

                @SerializedName("ticker")
                @Expose
                val ticker: Ticker = Ticker(),

                @SerializedName("ticker_unification_params")
                val tickerUnificationParams: TickerUnificationParams = TickerUnificationParams()

            ) : Parcelable {

                @Parcelize
                data class TickerUnificationParams(
                    @SerializedName("page")
                    val page: String = "",

                    @SerializedName("target")
                    val target: List<TickerUnificationTargets> = listOf(),

                    @SerializedName("template")
                    val template: Template = Template()

                ) : Parcelable {

                    @Parcelize
                    data class Template(
                        @SerializedName("contents")
                        val contents: List<Content> = listOf()
                    ) : Parcelable {

                        @Parcelize
                        data class Content(
                            @SerializedName("key")
                            val key: String = "",

                            @SerializedName("value")
                            val value: String = ""
                        ) : Parcelable
                    }
                }

                @Parcelize
                data class PickupLocation(
                    @SerializedName("title")
                    @Expose
                    val title: String = "",

                    @SerializedName("address")
                    @Expose
                    val address: String = "",

                    @SerializedName("phone")
                    @Expose
                    val phone: String = ""
                ) : Parcelable

                @Parcelize
                data class DropOffLocation(

                    @SerializedName("drop_off_notes")
                    val dropOffNotes: DropOffNotes = DropOffNotes()

                ) : Parcelable {

                    @Parcelize
                    data class DropOffNotes(
                        @SerializedName("title")
                        val title: String = "",

                        @SerializedName("text")
                        val text: String = "",

                        @SerializedName("url_text")
                        val urlText: String = "",

                        @SerializedName("url_detail")
                        val urlDetail: String = ""
                    ) : Parcelable
                }

                @Parcelize
                data class Detail(
                    @SerializedName("title")
                    val title: String = "",

                    @SerializedName("invoice")
                    val invoice: String = "",

                    @SerializedName("shippers")
                    val listShippers: List<Shipper> = listOf(),

                    @SerializedName("orchestra_partner")
                    val orchestraPartner: String = ""
                ) : Parcelable {

                    @Parcelize
                    data class Shipper(
                        @SerializedName("name")
                        val name: String = "",

                        @SerializedName("service")
                        val service: String = "",

                        @SerializedName("note")
                        val note: String = "",

                        @SerializedName("courier_image")
                        val courierImg: String = "",

                        @SerializedName("count_text")
                        val countText: String = "",

                        @SerializedName("count")
                        val count: String = ""
                    ) : Parcelable
                }

                @Parcelize
                data class Notes(
                    @SerializedName("title")
                    val title: String = "",

                    @SerializedName("list")
                    val listNotes: List<String> = listOf()
                ) : Parcelable

                @Parcelize
                data class ScheduleTime(
                    @SerializedName("today")
                    val today: List<ScheduleResponse> = listOf(),
                    @SerializedName("tomorrow")
                    val tomorrow: List<ScheduleResponse> = listOf()
                ) : Parcelable {

                    @Parcelize
                    data class ScheduleResponse(
                        @SerializedName("key")
                        val key: String = "",
                        @SerializedName("start")
                        val start: String = "",
                        @SerializedName("end")
                        val end: String = ""
                    ) : Parcelable
                }

                @Parcelize
                data class Ticker(
                    @SerializedName("text")
                    val text: String = "",
                    @SerializedName("url_text")
                    val urlText: String = "",
                    @SerializedName("url_detail")
                    val urlDetail: String = "",
                    @SerializedName("action_key")
                    val actionKey: String = "",
                    @SerializedName("type")
                    val type: String = ""
                ) : Parcelable

                @Parcelize
                data class TickerUnificationTargets(
                    @SerializedName("type")
                    val type: String = "",

                    @SerializedName("values")
                    val values: List<String> = listOf()
                ) : Parcelable
            }
        }
    }
}
