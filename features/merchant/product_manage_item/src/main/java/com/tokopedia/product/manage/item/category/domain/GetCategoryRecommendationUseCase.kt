package com.tokopedia.product.manage.item.category.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.item.common.constant.ProductAddParamConstant
import com.tokopedia.product.manage.item.main.base.data.mapper.CategoryRecommendationMapper
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.Category
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.CategoryRecommendationData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetCategoryRecommendationUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                           @Named(ProductAddParamConstant.RAW_JARVIS_RECOMMENDATION) private val rawQuery: String,
                                                           private val getCategoryRecommendationMapper: CategoryRecommendationMapper
) : UseCase<List<Category>>() {

    override fun createObservable(requestParam: RequestParams?): Observable<List<Category>> {

        val graphqlRequest = GraphqlRequest(rawQuery, CategoryRecommendationData::class.java, requestParam?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParam).map(getCategoryRecommendationMapper)
    }

    companion object {

        private const val PARAM_PRODUCT_NAME = "productName"

        @JvmOverloads
        fun createRequestParams(productName: String = ""):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_PRODUCT_NAME, productName)
            return requestParams
        }

    }
}