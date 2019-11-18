package com.tokopedia.product.manage.item.main.base.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.Category
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.CategoryRecommendationData
import rx.functions.Func1
import javax.inject.Inject

class CategoryRecommendationMapper @Inject constructor() : Func1<GraphqlResponse, List<Category>?>{

    override fun call(t: GraphqlResponse?): List<Category>? {
        val dataModel = t?.getData<CategoryRecommendationData?>(CategoryRecommendationData::class.java)
        return mapModel(dataModel!!)
    }

    /**
     * Map GraphQl Response to return only its recommendation categories
     */
    private fun mapModel(dataModel: CategoryRecommendationData): List<Category> = dataModel.getJarvisRecommendation.categories
}