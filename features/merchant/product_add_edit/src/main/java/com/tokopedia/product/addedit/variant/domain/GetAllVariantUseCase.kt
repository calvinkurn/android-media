package com.tokopedia.product.addedit.variant.domain

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.addedit.variant.data.constant.GetVariantCombinationQueryConstant
import com.tokopedia.product.addedit.variant.data.model.AllVariant
import com.tokopedia.product.addedit.variant.data.model.GetVariantCategoryCombinationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAllVariantUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<GetVariantCategoryCombinationResponse>(repository) {

    companion object {
        private const val CACHE_EXPIRY_TIME_IN_DAYS = 1
        private const val PARAM_CATEGORY_ID = "categoryID"
        private const val PARAM_ALL_VARIANTS = "allVariants"
        private val query = String.format(
            GetVariantCombinationQueryConstant.BASE_QUERY,
            GetVariantCombinationQueryConstant.OPERATION_PARAM_ALL_VARIANT,
            GetVariantCombinationQueryConstant.QUERY_PARAM_ALL_VARIANT,
            GetVariantCombinationQueryConstant.QUERY_DATA_ALL_VARIANT
        ).trimIndent()
    }

    private var searchKeyword: String = ""

    init {
        setGraphqlQuery(query)
        setTypeClass(GetVariantCategoryCombinationResponse::class.java)
        setCacheStrategy(createCacheStrategy())
    }

    private fun createCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(CACHE_EXPIRY_TIME_IN_DAYS * GraphqlConstant.ExpiryTimes.DAY.`val`())
            .setSessionIncluded(true)
            .build()
    }

    fun setParams(categoryId: Int, searchKeyword: String) {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_CATEGORY_ID, categoryId)
        requestParams.putString(PARAM_ALL_VARIANTS, true.toString())
        setRequestParams(requestParams.parameters)
        this.searchKeyword = searchKeyword
    }

    suspend fun getDataFiltered(): List<AllVariant> {
        return executeOnBackground().getVariantCategoryCombination.data.allVariants.filter {
            it.variantName.startsWith(searchKeyword, ignoreCase = true)
        }
    }
}