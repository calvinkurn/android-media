package com.tokopedia.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class FollowShopUseCase @Inject constructor(@Named(MUTATION_NAME) private val mutationString: String,
                                            private val graphqlUseCase: GraphqlUseCase,
                                            private val schedulers: ExecutorSchedulers) : UseCase<DataFollowShop>() {

    companion object {
        const val MUTATION_NAME = "followShop"

        const val PARAM_INPUT = "input"
        const val PARAM_SHOP_ID = "shopID"
    }

    fun buildRequestParams(shopId: String): RequestParams {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = mapOf(PARAM_SHOP_ID to shopId)

        return RequestParams.create().apply {
            putAll(variables)
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DataFollowShop> {
        val graphqlRequest = GraphqlRequest(mutationString, DataFollowShop::class.java, requestParams?.parameters
                ?: emptyMap())
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val followShopResponse = it.getData<DataFollowShop>(DataFollowShop::class.java)
                            ?: throw ResponseErrorException()
                    if (!followShopResponse.followShop.isSuccess) {
                        throw ResponseErrorException(followShopResponse.followShop.message)
                    }
                    followShopResponse
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}