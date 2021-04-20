package com.tokopedia.product.manage.common.feature.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.common.feature.variant.data.model.param.UpdateVariantParam
import com.tokopedia.product.manage.common.feature.variant.data.query.ProductUpdateV3
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class EditProductVariantUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductUpdateV3Response>(graphqlRepository) {

    companion object {
        private const val PARAM_INPUT = "input"

        fun createRequestParams(variant: UpdateVariantParam): RequestParams {
            return RequestParams().apply { putObject(PARAM_INPUT, variant) }
        }
    }

    init {
        setGraphqlQuery(ProductUpdateV3.QUERY)
        setTypeClass(ProductUpdateV3Response::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ProductUpdateV3Response {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}