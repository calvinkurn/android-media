package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.filter.common.data.*
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import javax.inject.Inject

class CategoryFullFilterRepository @Inject constructor() : BaseRepository(), FilterRepository{

    @Inject
    lateinit var getRecommendationFilterChips: GetRecommendationFilterChips

    override suspend fun getFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): DynamicFilterModel? {
        getRecommendationFilterChips.setParams(pageName = "clp_full_filter", xSource = "category")
        return mapFilters(getRecommendationFilterChips.executeOnBackground())
    }

    private fun mapFilters(recommendationChips: RecommendationFilterChipsEntity.FilterAndSort): DynamicFilterModel {
        val filters = arrayListOf<Filter>()
        val sort = arrayListOf<Sort>()
        recommendationChips.filterChip.forEach { filter ->
            if (!filter.options.isNullOrEmpty()) {
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
                        templateName = filter.templateName,
                        search = Search(1, filter.search.placeholder)
                ))
            }
        }
        recommendationChips.sortChip.forEach {
            sort.add(Sort(it.name,it.key,it.value, it.inputType))
        }
        return DynamicFilterModel(data = DataValue(filter = filters as List<Filter>, sort = sort))
    }
}