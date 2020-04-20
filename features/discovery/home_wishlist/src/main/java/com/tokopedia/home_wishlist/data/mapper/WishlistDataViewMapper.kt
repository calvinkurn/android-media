package com.tokopedia.home_wishlist.data.mapper

import com.tokopedia.home_wishlist.model.datamodel.LoadMoreDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.smart_recycler_helper.SmartVisitable

object WishlistDataViewMapper {
    fun mapperToWishlistItemDataModel(list: List<WishlistItem>): List<SmartVisitable<*>>{
        return list.map{ WishlistItemDataModel(it) }
    }

    fun mapperLoadingRecommendationWidget(list: List<SmartVisitable<*>>): List<SmartVisitable<*>>{
        val newMappingList = ArrayList(list)
        val countRecommendationWidget = list.size / 4
        for(i in countRecommendationWidget downTo 1){
            newMappingList.add(i * 4, LoadMoreDataModel())
        }
        return newMappingList
    }
}