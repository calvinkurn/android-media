package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import javax.inject.Inject

class CategoryQuickFilterRepository @Inject constructor() : BaseRepository(), QuickFilterRepository {

    @Inject
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    override suspend fun getQuickFilterData(componentId: String, pageEndPoint: String): ArrayList<Filter>? {
        getRecommendationFilterChips.setParams(pageName = "clp_quick_filter", xSource = "category")
        return mapFilters(getRecommendationFilterChips.executeOnBackground().filterChip)
    }

    private fun mapFilters(recommendationFilters: List<RecommendationFilterChipsEntity.RecommendationFilterChip>): ArrayList<Filter>? {
        val filters = arrayListOf<Filter>()
        recommendationFilters.forEach { filter ->
            val options = arrayListOf<Option>()
            filter.options.forEach { option ->
                options.add(Option(
                        name = option.name,
                        iconUrl = option.icon,
                        key = option.key,
                        value = option.value,
                        isPopular = option.isPopular,
                        inputType = option.inputType))
            }
            filters.add(Filter(
                    title = filter.title,
                    options = options,
                    templateName = filter.templateName
            ))
        }
        return filters
    }
}