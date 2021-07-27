package com.tokopedia.moneyin.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class MoneyInScheduleOptionResponse(
        val data: ResponseData?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable<ResponseData>(ResponseData::class.java.classLoader))

    data class ResponseData(
            @SerializedName("getPickupScheduleOption")
            val getPickupScheduleOption: GetPickupScheduleOption?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readParcelable<GetPickupScheduleOption>(GetPickupScheduleOption::class.java.classLoader))

        data class GetPickupScheduleOption(
                @SerializedName("ScheduleDate")
                val scheduleDate: ArrayList<ScheduleDate>
        ) : Parcelable {
            constructor(parcel: Parcel) : this(parcel.createTypedArrayList(ScheduleDate) as ArrayList<ScheduleDate>)

            data class ScheduleDate(
                    @SerializedName("DateFmt")
                    val dateFmt: String,
                    @SerializedName("ScheduleTime")
                    val scheduleTime: ArrayList<ScheduleTime>
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readString() ?: "",
                        parcel.createTypedArrayList(ScheduleTime) as ArrayList<ScheduleTime>
                )

                data class ScheduleTime(
                        @SerializedName("MaxTimeUnix")
                        val maxTimeUnix: Int,
                        @SerializedName("MinTimeUnix")
                        val minTimeUnix: Int,
                        @SerializedName("TimeFmt")
                        val timeFmt: String
                ) : Parcelable {
                    constructor(parcel: Parcel) : this(
                            parcel.readInt(),
                            parcel.readInt(),
                            parcel.readString() ?: "")

                    override fun writeToParcel(parcel: Parcel, flags: Int) {
                        parcel.writeInt(maxTimeUnix)
                        parcel.writeInt(minTimeUnix)
                        parcel.writeString(timeFmt)
                    }

                    override fun describeContents(): Int {
                        return 0
                    }

                    companion object CREATOR : Parcelable.Creator<ScheduleTime> {
                        override fun createFromParcel(parcel: Parcel): ScheduleTime {
                            return ScheduleTime(parcel)
                        }

                        override fun newArray(size: Int): Array<ScheduleTime?> {
                            return arrayOfNulls(size)
                        }
                    }
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(dateFmt)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<ScheduleDate> {
                    override fun createFromParcel(parcel: Parcel): ScheduleDate {
                        return ScheduleDate(parcel)
                    }

                    override fun newArray(size: Int): Array<ScheduleDate?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {

            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<GetPickupScheduleOption> {
                override fun createFromParcel(parcel: Parcel): GetPickupScheduleOption {
                    return GetPickupScheduleOption(parcel)
                }

                override fun newArray(size: Int): Array<GetPickupScheduleOption?> {
                    return arrayOfNulls(size)
                }
            }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(getPickupScheduleOption, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ResponseData> {
            override fun createFromParcel(parcel: Parcel): ResponseData {
                return ResponseData(parcel)
            }

            override fun newArray(size: Int): Array<ResponseData?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoneyInScheduleOptionResponse> {
        override fun createFromParcel(parcel: Parcel): MoneyInScheduleOptionResponse {
            return MoneyInScheduleOptionResponse(parcel)
        }

        override fun newArray(size: Int): Array<MoneyInScheduleOptionResponse?> {
            return arrayOfNulls(size)
        }
    }
}
