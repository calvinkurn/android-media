package com.tokopedia.product.addedit.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.variant.data.constant.GetVariantCombinationQueryConstant
import com.tokopedia.product.addedit.variant.data.model.GetVariantCategoryCombinationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetVariantCategoryCombinationUseCase @Inject constructor(
        repository: GraphqlRepository
) : GraphqlUseCase<GetVariantCategoryCombinationResponse>(repository) {

    companion object {
        private const val PARAM_CATEGORY_ID = "categoryID"
        private const val PARAM_PRODUCT_VARIANTS = "productVariants"
        private const val PARAM_TYPE = "type"
        private val query = String.format(
            GetVariantCombinationQueryConstant.BASE_QUERY,
            GetVariantCombinationQueryConstant.OPERATION_PARAM_BY_CATEGORY,
            GetVariantCombinationQueryConstant.QUERY_PARAM_BY_CATEGORY,
            GetVariantCombinationQueryConstant.QUERY_DATA_BY_CATEGORY
        ).trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetVariantCategoryCombinationResponse::class.java)
    }

    fun setParams(categoryId: Int, productVariants: List<String>, type: String) {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_CATEGORY_ID, categoryId)
        if (productVariants.isNotEmpty()) {
            requestParams.putObject(PARAM_PRODUCT_VARIANTS,productVariants.joinToString(separator = ","))
        }
        requestParams.putString(PARAM_TYPE, type)
        setRequestParams(requestParams.parameters)
    }
}