package com.tokopedia.filter.common.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Option(@SerializedName("name")
             @Expose
             var name: String = "",

             @SerializedName("key")
             @Expose
             var key: String = "",

             @SerializedName("value")
             @Expose
             var value: String = "",

             @SerializedName(value = "input_type", alternate = ["inputType"])
             @Expose
             var inputType: String = "",

             @SerializedName(value = "hex_color", alternate = ["hexColor"])
             @Expose
             var hexColor: String = "",

             @SerializedName("metric")
             @Expose
             var metric:String = "",

             @SerializedName(value = "total_data", alternate = ["totalData"])
             @Expose
             private var totalData: String = "",

             @SerializedName(value = "val_min", alternate = ["valMin"])
             @Expose
             var valMin: String = "",

             @SerializedName(value = "val_max", alternate = ["valMax"])
             @Expose
             var valMax: String = "",

             @SerializedName("icon")
             @Expose
             var iconUrl: String = "",

             @SerializedName(value = "description", alternate = ["Description"])
             @Expose
             var description: String = "",

             @SerializedName(value = "is_popular", alternate = ["isPopular"])
             @Expose
             var isPopular: Boolean = false,

             @SerializedName(value = "is_new", alternate = ["isNew"])
             @Expose
             var isNew: Boolean = false,

             @SerializedName("child")
             @Expose
             var levelTwoCategoryList: List<LevelTwoCategory> = listOf(),

             var inputState: String = "") : Parcelable {

    val isAnnotation: Boolean
        get() = KEY_ANNOTATION_ID.equals(key)

    val isCategoryOption: Boolean
        get() = KEY_CATEGORY.equals(key)

    val isOfficialOption: Boolean
        get() = KEY_OFFICIAL.equals(key)

    val uniqueId: String
        get() = key + UID_FIRST_SEPARATOR_SYMBOL + value + UID_SECOND_SEPARATOR_SYMBOL + name

    val isTypeRadio: Boolean
        get() = INPUT_TYPE_RADIO == inputType

    val isTypeTextBox: Boolean
        get() = Option.INPUT_TYPE_TEXTBOX == inputType

    val isPriceRange: Boolean
        get() = key == KEY_PRICE_RANGE_1 || key == KEY_PRICE_RANGE_2 || key == KEY_PRICE_RANGE_3 || key == KEY_PRICE_RANGE_4 || key == KEY_PRICE_RANGE_5

    val isMinPriceOption: Boolean
        get() = key == KEY_PRICE_MIN

    val isMaxPriceOption: Boolean
        get() = key == KEY_PRICE_MAX

    val isMinMaxRangePriceOption: Boolean
        get() = key == KEY_PRICE_MIN_MAX_RANGE

    val isRatingOption: Boolean
        get() = key == KEY_RATING

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other == null || other.javaClass != this.javaClass) return false

        val option = other as Option

        return (this.key == option.key
                && this.value == option.value
                && this.name == option.name)
    }

    companion object {

        const val KEY_PRICE_MIN = "pmin"
        const val KEY_PRICE_MAX = "pmax"
        const val KEY_PRICE_MIN_MAX_RANGE = "pmin-pmax"
        const val KEY_PRICE_WHOLESALE = "wholesale"
        const val KEY_PRICE_RANGE_1 = "price_range_1"
        const val KEY_PRICE_RANGE_2 = "price_range_2"
        const val KEY_PRICE_RANGE_3 = "price_range_3"
        const val KEY_PRICE_RANGE_4 = "price_range_4"
        const val KEY_PRICE_RANGE_5 = "price_range_5"
        const val KEY_CATEGORY = "sc"
        const val KEY_OFFICIAL = "official"
        const val KEY_RATING = "rt"
        const val KEY_ANNOTATION_ID = "annotation_id"

        const val INPUT_TYPE_TEXTBOX = "textbox"
        const val INPUT_TYPE_CHECKBOX = "checkbox"
        const val INPUT_TYPE_RADIO = "radio"
        const val UID_FIRST_SEPARATOR_SYMBOL = "*"
        const val UID_SECOND_SEPARATOR_SYMBOL = "?"
        const val METRIC_INTERNATIONAL = "International"

        const val RATING_ABOVE_FOUR_NAME = "4 Keatas"
        const val RATING_ABOVE_FOUR_VALUE = "4,5"

    }
}
