package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

class LevelTwoCategory() : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String = ""

    @SerializedName("key")
    @Expose
    var key: String = ""

    @SerializedName("value")
    @Expose
    var value: String = ""

    @SerializedName("isPopular")
    @Expose
    var isPopular: Boolean = false

    @SerializedName(value = "input_type", alternate = [ "inputType" ])
    @Expose
    private var inputType: String = ""

    @SerializedName(value = "total_data", alternate = [ "totalData" ])
    @Expose
    private var totalData: String = ""

    @SerializedName("child")
    @Expose
    var levelThreeCategoryList: List<LevelThreeCategory> = listOf()

    fun asOption(): Option {
        val uniqueId = OptionHelper.constructUniqueId(this.key, this.value, this.name)
        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)

        option.isPopular = this.isPopular

        return option
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.name)
        dest.writeString(this.key)
        dest.writeString(this.value)
        dest.writeString(this.inputType)
        dest.writeString(this.totalData)
        dest.writeTypedList(this.levelThreeCategoryList)
    }

    protected constructor(`in`: Parcel) : this() {
        this.name = `in`.readString()
        this.key = `in`.readString()
        this.value = `in`.readString()
        this.inputType = `in`.readString()
        this.totalData = `in`.readString()
        this.levelThreeCategoryList = `in`.createTypedArrayList(LevelThreeCategory.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LevelTwoCategory> = object : Parcelable.Creator<LevelTwoCategory> {
            override fun createFromParcel(source: Parcel): LevelTwoCategory {
                return LevelTwoCategory(source)
            }

            override fun newArray(size: Int): Array<LevelTwoCategory?> {
                return arrayOfNulls(size)
            }
        }
    }
}
