package com.tokopedia.home_wishlist.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.model.datamodel.LoadMoreDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.model.entity.WishlistItem

object WishlistDataViewMapper {
    fun mapperToWishlistItemDataModel(list: List<WishlistItem>): List<Visitable<*>>{
        return list.map{ WishlistItemDataModel(it) }
    }

    fun mapperLoadingRecommendationWidget(list: List<Visitable<*>>): List<Visitable<*>>{
        val newMappingList = ArrayList(list)
        val countRecommendationWidget = list.size / 4
        for(i in countRecommendationWidget downTo 1){
            newMappingList.add(i * 4, LoadMoreDataModel())
        }
        return newMappingList
    }
}