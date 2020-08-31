package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.AddShopEtalaseMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class AddShopEtalaseUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<AddShopEtalaseMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<AddShopEtalaseMutation>(context, AddShopEtalaseMutation::class.java) {
            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_add_shop_etalase)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val name = requestParams.getString(NAME, "")
                variables[NAME] = name
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
        val NAME = "name"

        @JvmStatic
        fun createRequestParams(etalaseName: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(NAME, etalaseName)
            return requestParams
        }
    }
}
