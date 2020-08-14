package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import android.text.TextUtils

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

class UpdateShopBasicDataUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<ShopBasicDataMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopBasicDataMutation>(context, ShopBasicDataMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_shop_basic_data)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[TAGLINE] = requestParams.getString(TAGLINE, "")
                variables[DESCRIPTION] = requestParams.getString(DESCRIPTION, "")
                val logoCode = requestParams.getString(LOGO_CODE, "")
                if (!TextUtils.isEmpty(logoCode)) {
                    variables[LOGO_CODE] = logoCode
                }
                val filePath = requestParams.getString(FILE_PATH, "")
                if (!TextUtils.isEmpty(filePath)) {
                    variables[FILE_PATH] = filePath
                }
                val fileName = requestParams.getString(FILE_NAME, "")
                if (!TextUtils.isEmpty(fileName)) {
                    variables[FILE_NAME] = fileName
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
        private const val LOGO = "logo"
        private const val CODE = "code"
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

            name?.let { requestParams.putString(NAME, it) }
            domain?.let { requestParams.putString(DOMAIN, it) }
            tagLine?.let { requestParams.putString(TAGLINE, it) }
            description?.let { requestParams.putString(DESCRIPTION, it) }

            logoCode?.let {
                val logoRequestParam = RequestParams()
                val logoCodeParam = mapOf(CODE to logoCode)
                logoRequestParam.putObject(LOGO, logoCodeParam)
            }

            return requestParams
        }
    }
}
