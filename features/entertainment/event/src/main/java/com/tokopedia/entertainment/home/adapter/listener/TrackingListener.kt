package com.tokopedia.entertainment.home.adapter.listener

import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse

interface TrackingListener {
    fun impressionBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int)
    fun clickBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int)
    fun clickCategoryIcon(item: CategoryEventViewHolder.CategoryItemModel, position: Int)
    fun clickSeeAllCuratedEventProduct(title: String, position: Int)
    fun clickSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int)
    fun impressionSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int)
    fun clickTopEventProduct(item: EventItemModel,
                             listItems: List<String>,
                             position: Int)

    fun impressionTopEventProduct(item: EventItemModel,
                                  listItems: List<String>,
                                  position: Int)

    fun clickSeeAllTopEventProduct()
    fun impressionLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int)
    fun clickLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int)
}