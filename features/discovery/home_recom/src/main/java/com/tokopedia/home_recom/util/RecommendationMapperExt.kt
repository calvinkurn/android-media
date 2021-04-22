package com.tokopedia.home_recom.util

import com.tokopedia.filter.common.data.*
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel.Companion.DEFAULT_VALUE_SORT
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_CUSTOM_HORIZONTAL
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import timber.log.Timber

/**
 * Created by Lukas on 08/10/20.
 */

/**
 * Function [mapDataModel]
 * It handling mapper pojo into dataModel
 * @return list of dataModel
 */
fun List<RecommendationWidget>.mapDataModel(): List<HomeRecommendationDataModel>{
    val list = ArrayList<HomeRecommendationDataModel>()
    filter { it.recommendationItemList.isNotEmpty() }.map{ recommendationWidget ->
        when(recommendationWidget.layoutType){
            TYPE_CAROUSEL, TYPE_CUSTOM_HORIZONTAL -> list.add(
                    RecommendationCarouselDataModel(
                            recommendationWidget.title,
                            recommendationWidget.seeMoreAppLink,
                            recommendationWidget.recommendationItemList.asSequence().map { RecommendationCarouselItemDataModel(it, list.size) }.toList()
                    )
            )
            else -> {
                list.add(TitleDataModel(recommendationWidget.title, recommendationWidget.pageName, recommendationWidget.seeMoreAppLink))
                recommendationWidget.recommendationItemList.forEach {
                    list.add(RecommendationItemDataModel(it))
                }
            }
        }
    }
    return list
}

fun ProductInfoDataModel.mapToRecommendationTracking(): RecommendationItem{
    return RecommendationItem(
            productId = productDetailData?.id ?: -1,
            position = 0,
            name = productDetailData?.name ?: "",
            appUrl = productDetailData?.appUrl ?: "",
            clickUrl = productDetailData?.clickUrl ?: "",
            categoryBreadcrumbs = productDetailData?.categoryBreadcrumbs ?: "",
            countReview = productDetailData?.countReview ?: -1,
            departmentId = productDetailData?.departmentId ?: -1,
            imageUrl = productDetailData?.imageUrl ?: "",
            isTopAds = productDetailData?.isTopads ?: false,
            isWishlist = productDetailData?.isWishlist ?: false,
            price = productDetailData?.price ?: "",
            priceInt = productDetailData?.priceInt ?: -1,
            rating = productDetailData?.rating ?: -1,
            recommendationType = productDetailData?.recommendationType ?: "",
            stock = productDetailData?.stock ?: -1,
            trackerImageUrl = productDetailData?.trackerImageUrl ?: "",
            url = productDetailData?.url ?: "",
            wishlistUrl = productDetailData?.wishlistUrl ?: "",
            slashedPrice = productDetailData?.slashedPrice ?: "",
            discountPercentageInt = productDetailData?.discountPercentage ?: -1,
            slashedPriceInt = productDetailData?.slashedPriceInt ?: -1,
            cartId = "",
            shopId = productDetailData?.shop?.id ?: -1,
            shopName = productDetailData?.shop?.name ?: "",
            shopType = if (productDetailData?.shop?.isGold == true) "gold_merchant" else "reguler",
            quantity = productDetailData?.minOrder ?: -1,
            header = "",
            pageName = "",
            minOrder = productDetailData?.minOrder ?: -1,
            location = "",
            badgesUrl = listOf(),
            type = "",
            isFreeOngkirActive = false,
            freeOngkirImageUrl = "",
            discountPercentage = "",
            isGold = false
    )
}

fun List<RecommendationFilterChipsEntity.RecommendationFilterChip>.mapToUnifyFilterModel(chipClick: (item: SortFilterItem, recom: RecommendationFilterChipsEntity.RecommendationFilterChip) -> Unit): List<SortFilterItem>{
    return map {
        SortFilterItem(
            title = it.options.firstOrNull()?.name ?: it.title,
            type = if(it.options.firstOrNull()?.isActivated == true) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        ).apply{
            listener = {
                chipClick(this, it)
            }
            typeUpdated = false
        }
    }
}
fun RecommendationFilterChipsEntity.FilterAndSort.mapToFullFilterModel(): DynamicFilterModel{
    val data = DataValue(this.filterChip.toFilter(), this.sortChip.toSort())
    return DynamicFilterModel(
            data = data,
            defaultSortValue = this.sortChip.find { it.isSelected && it.value != DEFAULT_VALUE_SORT }?.value ?: DEFAULT_VALUE_SORT
    )
}

fun List<RecommendationFilterChipsEntity.RecommendationFilterChip>.toFilter(): List<Filter>{
    return map {
        Filter(
                title = it.title,
                templateName = it.templateName,
                options = it.options.map { option ->
                    Option(
                            name = option.name,
                            iconUrl = option.icon,
                            inputType = option.inputType,
                            key = option.key,
                            value = option.value,
                            isPopular = option.isPopular,
                            hexColor = option.hexColor,
                            isNew = option.isNew,
                            valMax = option.valMax,
                            valMin = option.valMin,
                            description = option.description,
                            inputState = option.isActivated.toString()
                    )
                }
        )
    }
}

fun List<RecommendationFilterChipsEntity.RecommendationSortChip>.toSort(): List<Sort>{
    return map {
        Sort(
                name = it.name,
                value = it.value,
                inputType = it.inputType,
                key = it.key
        )
    }
}

fun List<Option>.getCountSelected(): Int{
    var selectedCount = 0
    forEach { opt ->
        selectedCount += if(opt.inputState == "true") 1 else 0
    }
    return selectedCount
}

fun List<Filter>.getOptions(): List<Option>{
    val list = mutableListOf<Option>()
    forEach { list.addAll(it.options) }
    return list
}

fun List<RecommendationFilterChipsEntity.RecommendationFilterChip>.getSelectedOption(): List<RecommendationFilterChipsEntity.Option>{
    val listOption = mutableListOf<RecommendationFilterChipsEntity.Option>()
    forEach {
        it.options.forEach { opt ->
            if(opt.isActivated || opt.inputType == "true") listOption.add(opt)
        }
    }
    return listOption
}

fun List<RecommendationFilterChipsEntity.RecommendationFilterChip>.getOption(): List<RecommendationFilterChipsEntity.Option>{
    val listOption = mutableListOf<RecommendationFilterChipsEntity.Option>()
    forEach {
        it.options.forEach { opt ->
            listOption.add(opt)
        }
    }
    return listOption
}

fun Map<String, String>.isActivated(key: String, value: String): Boolean{
    if(!this.containsKey(key)) return false
    // check if separator # (241,242,243#10#11) for case city_ids
    return if(this[key]?.contains("#") == true){
        val values = mutableListOf<String>()
        // saved to values [(241,242,243), 10, 11]
        values.addAll(this[key]?.split("#") ?: listOf())
        // check is inside values
        values.any { it == value }
    } else {
        this[key] == value
    }
}