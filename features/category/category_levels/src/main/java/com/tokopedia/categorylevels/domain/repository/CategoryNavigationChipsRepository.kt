package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.data.kategorymodel.Data
import com.tokopedia.common_category.data.raw.GQL_CATEGORY_LIST
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.childcategory.ChildCategoryRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.RequestParams

private const val KEY_CATEGORY_ID = "categoryID"
private const val KEY_IS_TRENDING = "isTrending"

class CategoryNavigationChipsRepository : BaseRepository(), ChildCategoryRepository {

    override suspend fun getChildCategory(componentId: String, pageEndPoint: String): List<ComponentsItem> {
        return getComponent(componentId, pageEndPoint)?.let {
            mapToDiscoveryData(getGQLData(GQL_CATEGORY_LIST, Data::class.java, createRequestParams(pageEndPoint.toIntOrZero()), cacheType = CacheType.CACHE_FIRST))
        } ?: arrayListOf()
    }

    private fun createRequestParams(id: Int): Map<String, Any> {
        val requestParams = RequestParams.create()
        requestParams.putInt(KEY_CATEGORY_ID, id)
        requestParams.putBoolean(KEY_IS_TRENDING, true)
        return requestParams.parameters
    }

    private fun mapToDiscoveryData(data: Data): List<ComponentsItem> {
        val dataItems = arrayListOf<DataItem>()
        data.categoryAllList.categories?.firstOrNull()?.child?.let {
            it.forEachIndexed { index, item ->
                dataItems.add(DataItem(title = item?.name, id = item?.id, applinks = item?.applinks, positionForParentItem = index))
            }
        }
        return DiscoveryDataMapper().mapListToComponentList(dataItems, ComponentNames.NavigationChipsItem.componentName, properties = null)
    }

}