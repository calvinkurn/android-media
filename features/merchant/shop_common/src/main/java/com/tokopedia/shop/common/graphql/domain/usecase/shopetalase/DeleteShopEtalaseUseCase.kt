package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.DeleteShopEtalaseMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class DeleteShopEtalaseUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<DeleteShopEtalaseMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<DeleteShopEtalaseMutation>(context, DeleteShopEtalaseMutation::class.java) {
            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_delete_shop_etalase)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val name = requestParams.getString(ID, "")
                variables[ID] = name
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
        val ID = "id"

        @JvmStatic
        fun createRequestParams(etalaseId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID, etalaseId)
            return requestParams
        }
    }
}
