package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.chipfilter.ChipFilterRepository
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import javax.inject.Inject

class CategoryChipFilterRepository @Inject constructor() : BaseRepository(), ChipFilterRepository {

    @Inject
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    override suspend fun getChipFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, position: Int, componentName: String?): List<ComponentsItem> {
        getRecommendationFilterChips.setParams(pageName = "quick_filter", xSource = "discopage")
        return mapChipsToComponents(getRecommendationFilterChips.executeOnBackground(), componentName, position).also {
            getComponent(componentId, pageEndPoint)?.setComponentsItem(it)
        }
    }

    private fun mapChipsToComponents(recommendationFilters: List<RecommendationFilterChipsEntity.RecommendationFilterChip>, componentName: String?, position: Int): ArrayList<ComponentsItem> {
        val filters = arrayListOf<DataItem>()
        for (item in recommendationFilters) {
            filters.add(DataItem(title = item.name, name = ComponentNames.ChipsFilterItem.componentName, id = item.value))
        }
        return DiscoveryDataMapper.mapListToComponentList(filters, ComponentNames.ChipsFilterItem.componentName, componentName, position)
    }
}