package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Option() : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String = ""

    @SerializedName("key")
    @Expose
    var key: String = ""

    @SerializedName("value")
    @Expose
    var value: String = ""

    @SerializedName(value = "input_type", alternate = [ "inputType" ])
    @Expose
    var inputType: String = ""

    @SerializedName(value = "hex_color", alternate = [ "hexColor" ])
    @Expose
    var hexColor: String = ""

    @SerializedName("metric")
    @Expose
    var metric = ""

    @SerializedName(value = "total_data", alternate = [ "totalData" ])
    @Expose
    private var totalData: String = ""

    @SerializedName(value = "val_min", alternate = [ "valMin" ])
    @Expose
    var valMin: String = ""

    @SerializedName(value = "val_max", alternate = [ "valMax" ])
    @Expose
    var valMax: String = ""

    @SerializedName("icon")
    @Expose
    var iconUrl: String = ""

    @SerializedName(value = "description", alternate = [ "Description" ])
    @Expose
    var description: String = ""

    @SerializedName(value = "is_popular", alternate = [ "isPopular" ])
    @Expose
    var isPopular: Boolean = false

    @SerializedName(value = "is_new", alternate = [ "isNew" ])
    @Expose
    var isNew: Boolean = false

    @SerializedName("child")
    @Expose
    var levelTwoCategoryList: List<LevelTwoCategory> = listOf()

    var inputState = ""

    val isAnnotation: Boolean
        get() = KEY_ANNOTATION_ID.equals(key)

    val isCategoryOption: Boolean
        get() = KEY_CATEGORY.equals(key)

    val isOfficialOption: Boolean
        get() = KEY_OFFICIAL.equals(key)

    val uniqueId: String
        get() = key + UID_FIRST_SEPARATOR_SYMBOL + value + UID_SECOND_SEPARATOR_SYMBOL + name

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true

        if (obj == null || obj.javaClass != this.javaClass) return false

        val option = obj as Option

        return (this.key == option.key
                && this.value == option.value
                && this.name == option.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.name)
        dest.writeString(this.key)
        dest.writeString(this.value)
        dest.writeString(this.inputType)
        dest.writeString(this.hexColor)
        dest.writeString(this.metric)
        dest.writeString(this.totalData)
        dest.writeString(this.valMin)
        dest.writeString(this.valMax)
        dest.writeString(this.iconUrl)
        dest.writeString(this.description)
        dest.writeByte(if (this.isPopular) 1.toByte() else 0.toByte())
        dest.writeByte(if (this.isNew) 1.toByte() else 0.toByte())
        dest.writeTypedList(this.levelTwoCategoryList)
        dest.writeString(this.inputState)
    }

    protected constructor(`in`: Parcel) : this() {
        val zeroByte = 0
        this.name = `in`.readString()
        this.key = `in`.readString()
        this.value = `in`.readString()
        this.inputType = `in`.readString()
        this.hexColor = `in`.readString()
        this.metric = `in`.readString()
        this.totalData = `in`.readString()
        this.valMin = `in`.readString()
        this.valMax = `in`.readString()
        this.iconUrl = `in`.readString()
        this.description = `in`.readString()
        this.isPopular = `in`.readByte() !== zeroByte.toByte()
        this.isNew = `in`.readByte() !== zeroByte.toByte()
        this.levelTwoCategoryList = `in`.createTypedArrayList(LevelTwoCategory.CREATOR)
        this.inputState = `in`.readString()
    }

    companion object {

        const val KEY_PRICE_MIN = "pmin"
        const val KEY_PRICE_MAX = "pmax"
        const val KEY_PRICE_MIN_MAX_RANGE = "pmin-pmax"
        const val KEY_PRICE_WHOLESALE = "wholesale"
        const val KEY_PRICE_RANGE_1 = "price_range_1"
        const val KEY_PRICE_RANGE_2 = "price_range_2"
        const val KEY_PRICE_RANGE_3 = "price_range_3"
        const val KEY_CATEGORY = "sc"
        const val KEY_OFFICIAL = "official"
        const val KEY_RATING = "rt"
        const val KEY_ANNOTATION_ID = "annotation_id"

        const val INPUT_TYPE_TEXTBOX = "textbox"
        const val INPUT_TYPE_CHECKBOX = "checkbox"
        const val UID_FIRST_SEPARATOR_SYMBOL = "*"
        const val UID_SECOND_SEPARATOR_SYMBOL = "?"
        const val METRIC_INTERNATIONAL = "International"

        const val RATING_ABOVE_FOUR_NAME = "4 Keatas"
        const val RATING_ABOVE_FOUR_VALUE = "4,5"

        @JvmField
        val CREATOR: Parcelable.Creator<Option> = object : Parcelable.Creator<Option> {

            override fun createFromParcel(source: Parcel): Option {
                return Option(source)
            }

            override fun newArray(size: Int): Array<Option?> {
                return arrayOfNulls(size)
            }
        }
    }
}
