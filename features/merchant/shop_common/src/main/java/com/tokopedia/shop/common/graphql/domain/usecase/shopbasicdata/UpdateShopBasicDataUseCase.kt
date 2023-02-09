package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.usecase.RequestParams
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
        private const val IMG_ID = "imgId"

        private val paramKeys = listOf(NAME, DOMAIN, TAGLINE, DESCRIPTION, IMG_ID)

        @JvmStatic
        fun createRequestParam(
                name: String?,
                domain: String?,
                tagLine: String?,
                description: String?,
                imgId: String?
        ): RequestParams {
            val requestParams = RequestParams()

            requestParams.putString(NAME, name)
            requestParams.putString(DOMAIN, domain)
            requestParams.putString(TAGLINE, tagLine)
            requestParams.putString(DESCRIPTION, description)

            imgId?.let { requestParams.putString(IMG_ID, imgId) }
            return requestParams
        }
    }

    init {
        val query = """
            mutation updateShopInfo(${'$'}name :String, ${'$'}domain :String, ${'$'}tagline :String, ${'$'}description :String, ${'$'}imgId :String){
              updateShopInfo(input: {
                  name: ${'$'}name,
                  domain: ${'$'}domain,
                  tagline: ${'$'}tagline,
                  description: ${'$'}description,
                  logo: {
                    imgID: ${'$'}imgId
                  }
                }) {
                  success
                  message
                }
            }
        """.trimIndent()
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
