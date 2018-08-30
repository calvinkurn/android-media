package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class UpdateShopBasicDataUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<ShopBasicDataMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopBasicDataMutation>(context, ShopBasicDataMutation::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_mutation_shop_basic_data

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val tagline = requestParams.getString(TAGLINE, "")
                if (!TextUtils.isEmpty(tagline)) {
                    variables[TAGLINE] = tagline
                }
                val description = requestParams.getString(DESCRIPTION, "")
                if (!TextUtils.isEmpty(description)) {
                    variables[DESCRIPTION] = description
                }
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
        val TAGLINE = "tagline"
        val DESCRIPTION = "description"
        val LOGO_CODE = "logoCode"
        val FILE_PATH = "filePath"
        val FILE_NAME = "fileName"

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
    }
}
