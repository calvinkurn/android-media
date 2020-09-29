package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.usecase.RequestParams
import java.util.HashMap
import javax.inject.Inject

class UpdateShopBasicDataUseCase @Inject constructor(
        @ApplicationContext context: Context,
        repository: GraphqlRepository
): GraphqlUseCase<ShopBasicDataMutation>(repository) {
    companion object {
        private const val NAME = "name"
        private const val DOMAIN = "domain"
        private const val TAGLINE = "tagline"
        private const val DESCRIPTION = "description"
        private const val LOGO_CODE = "logoCode"
        private const val FILE_PATH = "filePath"
        private const val FILE_NAME = "fileName"

        private val paramKeys = listOf(NAME, DOMAIN, TAGLINE, DESCRIPTION, LOGO_CODE, FILE_PATH, FILE_NAME)

        @JvmStatic
        fun createRequestParam(
                name: String?,
                domain: String?,
                tagLine: String?,
                description: String?,
                logoCode: String?
        ): RequestParams {
            val requestParams = RequestParams()

            requestParams.putString(NAME, name)
            requestParams.putString(DOMAIN, domain)
            requestParams.putString(TAGLINE, tagLine)
            requestParams.putString(DESCRIPTION, description)

            logoCode?.let { requestParams.putString(LOGO_CODE, logoCode) }
            return requestParams
        }
    }

    init {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_shop_basic_data)
        setGraphqlQuery(query)
        setTypeClass(ShopBasicDataMutation::class.java)
    }

    fun setParams(requestParams: RequestParams) {
        val variables = HashMap<String, Any>()
        paramKeys.forEach { key ->
            val value = requestParams.getString(key, "")
            when (key) {
                TAGLINE -> variables[key] = value
                DESCRIPTION -> variables[key] = value
                else -> if(value.isNotEmpty()) { variables[key] = value }
            }
        }
        setRequestParams(variables)
    }
}