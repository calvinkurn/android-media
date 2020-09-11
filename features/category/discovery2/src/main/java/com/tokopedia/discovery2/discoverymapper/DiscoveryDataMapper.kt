package com.tokopedia.discovery2.discoverymapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.categorynavigationresponse.ChildItem
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort

private const val CHIPS = "Chips"
private const val TABS_ITEM = "tabs_item"

class DiscoveryDataMapper {

    companion object {

        val discoveryDataMapper: DiscoveryDataMapper by lazy { DiscoveryDataMapper() }

        fun mapListToComponentList(itemList: List<DataItem>, subComponentName: String = "", parentComponentName: String?, position: Int, design: String = ""): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            itemList.forEachIndexed { index, it ->
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                val id = "${CHIPS}_$index"
                componentsItem.name = subComponentName
                componentsItem.id = id
                componentsItem.design = design
                it.parentComponentName = parentComponentName
                it.positionForParentItem = position
                val dataItem = mutableListOf<DataItem>()
                dataItem.add(it)
                componentsItem.data = dataItem
                list.add(componentsItem)
            }
            return list
        }

        fun mapTabsListToComponentList(component: ComponentsItem, subComponentName: String = "", position: Int, pinnedTabID: String?): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            var isSelectedFound = false
            component.data?.forEachIndexed { index, it ->
                val id = "${TABS_ITEM}_$index"
                if (!pinnedTabID.isNullOrEmpty()) {
                    var pinnedActiveIndex = pinnedTabID.toIntOrZero()
                    if (pinnedActiveIndex.isMoreThanZero()) {
                        pinnedActiveIndex -= 1
                        if (index == pinnedActiveIndex) {
                            it.isSelected = true
                            isSelectedFound = true
                        }
                    }
                } else if (it.isSelected) {
                    isSelectedFound = true
                }
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                componentsItem.name = subComponentName
                componentsItem.pageEndPoint = component.pageEndPoint
                it.positionForParentItem = position
                val dataItem = mutableListOf<DataItem>()
                dataItem.add(it)
                componentsItem.data = dataItem
                componentsItem.id = id
                list.add(componentsItem)
            }

            if (!isSelectedFound) {
                list.getOrNull(0)?.data?.getOrNull(0)?.isSelected = true
            }
            return list
        }

        fun mapBannerComponentData(bannerComponent: ComponentsItem): ComponentsItem {
            return bannerComponent.apply {
                this.data?.forEach {
                    it.id = this.id
                }
            }
        }
    }

    fun mapDynamicCategoryListToComponentList(itemList: List<DataItem>, subComponentName: String = "", categoryHeaderName: String,
                                              categoryHeaderPosition: Int): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            val dataItem = mutableListOf<DataItem>()
            it.title = categoryHeaderName
            it.positionForParentItem = categoryHeaderPosition
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }

    fun mapListToComponentList(itemList: List<DataItem>?, subComponentName: String = "", properties: Properties?, typeProductCard: String = ""): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            val dataItem = mutableListOf<DataItem>()
            it.typeProductCard = subComponentName
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }


    fun mapListToComponentList(child: List<ChildItem?>?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        child?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = ComponentNames.HorizontalCategoryNavigationIem.componentName
            val dataItemlist = mutableListOf<DataItem>()
            val dataItem = DataItem()
            dataItem.imageUrlMobile = it?.thumbnailImage
            dataItem.name = it?.name
            dataItem.id = it?.id
            dataItem.applinks = it?.applinks
            dataItemlist.add(dataItem)
            componentsItem.data = dataItemlist
            list.add(componentsItem)
        }
        return list
    }

    fun mapFiltersToDynamicFilterModel(dataItem: DataItem?): DynamicFilterModel? {
        val filter = dataItem?.filter
        filter?.forEach {
            if(it.options.isNullOrEmpty())
                filter.remove(it)
        }
        return DynamicFilterModel(data = DataValue(filter = filter as List<Filter>, sort = dataItem.sort as List<Sort>))
    }
}