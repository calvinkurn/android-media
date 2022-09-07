package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_HEADER = "header"
private const val KEY_STATUS = "status"
private const val KEY_DATA = "data"
private const val KEY_TEMPLATE = "template"
private const val KEY_ERROR = "errors"

data class TopAdsModel(
    @SerializedName(KEY_ERROR)
    @Expose
    var error: Error? = Error(),

    @SerializedName(KEY_STATUS)
    @Expose
    var status: Status? = Status(),

    @SerializedName(KEY_HEADER)
    @Expose
    var header: Header? = Header(),

    @SerializedName(KEY_DATA)
    @Expose
    var data: MutableList<Data>? = ArrayList(),

    @SerializedName(KEY_TEMPLATE)
    var templates: MutableList<Template>? = ArrayList(),

    @Expose(deserialize = false, serialize = false)
    var adsPosition: Int = 0
) : Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_ERROR)) {
            error = Error(jSONObject.getJSONArray(KEY_ERROR).getJSONObject(0))
        }
        if (!jSONObject.isNull(KEY_HEADER)) {
            header = Header(jSONObject.getJSONObject(KEY_HEADER))
        }
        if (!jSONObject.isNull(KEY_STATUS)) {
            status = Status(jSONObject.getJSONObject(KEY_STATUS))
        }
        if (!jSONObject.isNull(KEY_DATA)) {
            val dataArray = jSONObject.getJSONArray(KEY_DATA)
            for (i in 0 until dataArray.length()) {
                data?.add(Data(dataArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_TEMPLATE)) {
            val dataArray = jSONObject.getJSONArray(KEY_TEMPLATE)
            for (i in 0 until dataArray.length()) {
                templates?.add(Template(dataArray.getJSONObject(i)))
            }
        }
    }

    constructor(parcel: Parcel) : this() {
        error = parcel.readParcelable(Error::class.java.classLoader)
        status = parcel.readParcelable(Status::class.java.classLoader)
        header = parcel.readParcelable(Header::class.java.classLoader)
        data = parcel.createTypedArrayList(Data.CREATOR)
        templates = parcel.createTypedArrayList(Template.CREATOR)
        adsPosition = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(error, flags)
        dest.writeParcelable(status, flags)
        dest.writeParcelable(header, flags)
        dest.writeTypedList(data)
        dest.writeTypedList(templates)
        dest.writeInt(adsPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<TopAdsModel> = object : Parcelable.Creator<TopAdsModel> {
            override fun createFromParcel(parcel: Parcel): TopAdsModel {
                return TopAdsModel(parcel)
            }

            override fun newArray(size: Int): Array<TopAdsModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
