package com.tokopedia.sellerorder.requestpickup.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2019-11-12.
 */
data class SomConfirmReqPickup (
    @SerializedName("data")
    @Expose
    val data: Data = Data()) {
        data class Data (
                @SerializedName("mpLogisticPreShipInfo")
                @Expose
                val mpLogisticPreShipInfo: MpLogisticPreShipInfo = MpLogisticPreShipInfo()) {

                @Parcelize
                data class MpLogisticPreShipInfo (
                    @SerializedName("status")
                    @Expose
                    val status: String = "",

                    @SerializedName("message_error")
                    @Expose
                    val listErrorMsg: List<String> = listOf(),

                    @SerializedName("data")
                    @Expose
                    val dataSuccess: DataSuccess = DataSuccess()) : Parcelable {

                    @Parcelize
                    data class DataSuccess(
                            @SerializedName("pickup_location")
                            @Expose
                            val pickupLocation: PickupLocation = PickupLocation(),

                            @SerializedName("detail")
                            @Expose
                            val detail: Detail = Detail(),

                            @SerializedName("notes")
                            @Expose
                            val notes: Notes = Notes(),

                            @SerializedName("schedule_time_day")
                            @Expose
                            val schedule_time: ScheduleTime = ScheduleTime()) : Parcelable {

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
                        data class Detail(
                                    @SerializedName("title")
                                    @Expose
                                    val title: String = "",

                                    @SerializedName("shippers")
                                    @Expose
                                    val listShippers: List<Shipper> = listOf(),

                                    @SerializedName("orchestra_partner")
                                    @Expose
                                    val orchestraPartner: String = "") : Parcelable {

                                @Parcelize
                                data class Shipper(
                                        @SerializedName("name")
                                        @Expose
                                        val name: String = "",

                                        @SerializedName("service")
                                        @Expose
                                        val service: String = "",

                                        @SerializedName("note")
                                        @Expose
                                        val note: String = "",

                                        @SerializedName("courier_image")
                                        @Expose
                                        val courierImg: String = "",

                                        @SerializedName("count_text")
                                        @Expose
                                        val countText: String = "",

                                        @SerializedName("count")
                                        @Expose
                                        val count: String = "") : Parcelable
                        }

                            @Parcelize
                            data class Notes(
                                    @SerializedName("title")
                                    @Expose
                                    val title: String = "",

                                    @SerializedName("list")
                                    @Expose
                                    val listNotes: List<String> = listOf()) : Parcelable

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
                                        val end : String = ""
                                ): Parcelable

                            }
                    }
                }
        }
    }