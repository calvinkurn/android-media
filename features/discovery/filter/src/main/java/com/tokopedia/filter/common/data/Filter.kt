package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Filter() : Parcelable {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("template_name")
    @Expose
    private var templateName: String = ""

    @SerializedName("search")
    @Expose
    var search: Search = Search()

    @SerializedName("options")
    @Expose
    var filterOptions: List<Option> = ArrayList()

    val isSeparator: Boolean
        get() = TEMPLATE_NAME_SEPARATOR.equals(templateName)

    val isCategoryFilter: Boolean
        get() = TEMPLATE_NAME_CATEGORY.equals(templateName)

    val isColorFilter: Boolean
        get() = TEMPLATE_NAME_COLOR.equals(templateName)

    val isOfferingFilter: Boolean
        get() = TEMPLATE_NAME_OFFERING.equals(templateName)

    val isPriceFilter: Boolean
        get() = TEMPLATE_NAME_PRICE.equals(templateName)

    val isRatingFilter: Boolean
        get() = TEMPLATE_NAME_RATING.equals(templateName)

    val isSizeFilter: Boolean
        get() = TEMPLATE_NAME_SIZE.equals(templateName)

    val isLocationFilter: Boolean
        get() = TEMPLATE_NAME_LOCATION.equals(templateName)

    val isBrandFilter: Boolean
        get() = TEMPLATE_NAME_BRAND.equals(templateName)

    val isOtherFilter: Boolean
        get() = TEMPLATE_NAME_OTHER.equals(templateName)

    val isExpandableFilter: Boolean
        get() = (isCategoryFilter || isColorFilter || isRatingFilter
                || isSizeFilter || isBrandFilter || isLocationFilter
                || isOtherFilter || filterOptions.size > 1)

    fun getOptions(): List<Option> {
        return filterOptions
    }

    fun setOptions(options: List<Option>) {
        this.filterOptions = options
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.templateName)
        dest.writeParcelable(this.search, flags)
        dest.writeTypedList(this.filterOptions)
    }

    protected constructor(`in`: Parcel) : this() {
        this.title = `in`.readString()
        this.templateName = `in`.readString()
        this.search = `in`.readParcelable(Search::class.java.getClassLoader())
        this.filterOptions = `in`.createTypedArrayList(Option.CREATOR)
    }

    companion object {

        const val TEMPLATE_NAME_LOCATION = "template_location"
        const val TEMPLATE_NAME_OTHER = "template_other"
        const val TEMPLATE_NAME_SEPARATOR = "template_separator"
        const val TEMPLATE_NAME_RATING = "template_rating"
        const val TEMPLATE_NAME_SIZE = "template_size"
        const val TEMPLATE_NAME_CATEGORY = "template_category"
        const val TEMPLATE_NAME_COLOR = "template_color"
        const val TEMPLATE_NAME_PRICE = "template_price"
        const val TEMPLATE_NAME_BRAND = "template_brand"
        const val TEMPLATE_NAME_OFFERING = "template_offer"

        @JvmField
        val CREATOR: Parcelable.Creator<Filter> = object : Parcelable.Creator<Filter> {
            override fun createFromParcel(source: Parcel): Filter {
                return Filter(source)
            }

            override fun newArray(size: Int): Array<Filter?> {
                return arrayOfNulls(size)
            }
        }
    }
}
