package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.constant.GQL_FEATURED_PRODUCT
import com.tokopedia.product.manage.feature.list.data.model.FeaturedProductResponseModel
import javax.inject.Inject
import javax.inject.Named

class SetFeaturedProductUseCase @Inject constructor(
    @Named(GQL_FEATURED_PRODUCT) private val gqlMutation: String,
    repository: GraphqlRepository
) : GraphqlUseCase<FeaturedProductResponseModel>(repository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"
        private const val PARAM_STATUS = "status"
    }

    init {
        setGraphqlQuery(gqlMutation)
        setTypeClass(FeaturedProductResponseModel::class.java)
    }

    fun setParams(productId: Long, status: Int) {
        val params = HashMap<String, Any>()
        params[PARAM_PRODUCT_ID] = productId
        params[PARAM_STATUS] = status
        setRequestParams(params)
    }
}