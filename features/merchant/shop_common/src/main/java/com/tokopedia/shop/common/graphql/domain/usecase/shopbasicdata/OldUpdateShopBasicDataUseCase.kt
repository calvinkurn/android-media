package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

@Deprecated("Use UpdateShopBasicDataUseCase")
class OldUpdateShopBasicDataUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<ShopBasicDataMutation>
    private val paramKeys = listOf(NAME, DOMAIN, TAGLINE, DESCRIPTION, LOGO_CODE, FILE_PATH, FILE_NAME)

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopBasicDataMutation>(context, ShopBasicDataMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_shop_basic_data)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()

                paramKeys.forEach { key ->
                    val value = requestParams.getString(key, "")
                    when (key) {
                        TAGLINE -> variables[key] = value
                        DESCRIPTION -> variables[key] = value
                        else -> if(value.isNotEmpty()) { variables[key] = value }
                    }
                }
                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLSuccessMapper())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {
        private const val NAME = "name"
        private const val DOMAIN = "domain"
        private const val TAGLINE = "tagline"
        private const val DESCRIPTION = "description"
        private const val LOGO_CODE = "logoCode"
        private const val FILE_PATH = "filePath"
        private const val FILE_NAME = "fileName"

        @JvmStatic
        fun createRequestParams(tagline: String, description: String,
                //optional, either code only, or (filePath & fileName) only
                                logoCode: String?,
                                filePath: String?, fileName: String?): RequestParams {
            val requestParams = createRequestParams(tagline, description)
            requestParams.putString(LOGO_CODE, logoCode)
            requestParams.putString(FILE_PATH, filePath)
            requestParams.putString(FILE_NAME, fileName)
            return requestParams
        }

        @JvmStatic
        fun createRequestParams(tagline: String, description: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(TAGLINE, tagline)
            requestParams.putString(DESCRIPTION, description)
            return requestParams
        }

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
}
