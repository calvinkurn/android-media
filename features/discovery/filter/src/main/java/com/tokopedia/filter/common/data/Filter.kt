package com.tokopedia.filter.common.data

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
class Filter(@SerializedName("title")
             @Expose
             var title: String = "",

             @SerializedName("subTitle", alternate = ["subtitle"])
             @Expose
             var subTitle: String = "",

             @SerializedName("template_name", alternate = ["templateName"])
             @Expose
             var templateName: String = "",

             @SerializedName("search")
             @Expose
             var search: Search = Search(),

             @SerializedName("isNew")
             @Expose
             var isNew: Boolean = false,

             @SerializedName("filter_attribute_detail")
             @Expose
             var filterAttributeDetail: String = "",

             @SerializedName("chip_name")
             @Expose
             var chipName: String = "",

             @SerializedName("options")
             @Expose
             var options: List<Option> = ArrayList()) : Parcelable {

    fun clone(
            title: String? = null,
            subTitle: String? = null,
            templateName: String? = null,
            search: Search? = null,
            isNew: Boolean? = null,
            filterAttributeDetail: String? = null,
            options: List<Option>? = null,
    ): Filter {
        return Filter(
                title = title ?: this.title,
                subTitle = subTitle ?: this.subTitle,
                templateName = templateName ?: this.templateName,
                search = search ?: this.search,
                isNew = isNew ?: this.isNew,
                filterAttributeDetail = filterAttributeDetail ?: this.filterAttributeDetail,
                options = options ?: this.options,
        )
    }

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
                || isOtherFilter || options.size > 1)

    val isKeywordFilter: Boolean
        get() = TEMPLATE_NEGATIVE_KEYWORD == templateName

    val isPriceRangeCheckboxFilter: Boolean
        get() = TEMPLATE_PRICING_FOOD.equals(templateName)

    fun getFlattenedOptions() : List<Option> {
        return options.flatMap { option ->
            option.levelTwoCategoryList.flatMap { levelTwoCategory ->
                levelTwoCategory.levelThreeCategoryList.map { levelThreeCategory ->
                    levelThreeCategory.asOption()
                } + levelTwoCategory.asOption()
            } + option
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
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
        const val TEMPLATE_NEGATIVE_KEYWORD = "template_negative_keyword"
        const val TEMPLATE_PRICING_FOOD = "template_pricing_food"
    }
}
