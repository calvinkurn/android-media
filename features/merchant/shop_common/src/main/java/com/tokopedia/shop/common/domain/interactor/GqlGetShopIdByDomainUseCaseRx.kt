package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopinfobydomain.ShopInfoByDomainResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 02/07/21.
 */
class GqlGetShopIdByDomainUseCaseRx @Inject constructor(
        private val gqlUseCase: GraphqlUseCase
) : UseCase<String>() {

    companion object {
        private const val KEY_DOMAIN = "domain"
        private val query = """
            query GetShopInfoByDomain(${'$'}domain:String){
              shopInfoByID(input:{shopIDs:[0], fields:[""], domain:${'$'}domain, source:"check-shopid"}) {
                result {
                  shopCore {
                    shopID
                  }
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(
                domain: String
        ): RequestParams = RequestParams.create().apply {
            putObject(KEY_DOMAIN, domain)
        }
    }

    override fun createObservable(params: RequestParams): Observable<String> {
        val request = GraphqlRequest(query,
                ShopInfoByDomainResponse::class.java,
                params.parameters)

        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        return gqlUseCase.createObservable(params).map { it ->
            val response = it.getData<ShopInfoByDomainResponse>(ShopInfoByDomainResponse::class.java)
            val error = it.getError(ShopInfoByDomainResponse::class.java) ?: listOf()
            if (response == null || response.shopInfoByID.result.isEmpty()) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
            }
            response.shopInfoByID.result.first().shopCore.shopID
        }
    }
}