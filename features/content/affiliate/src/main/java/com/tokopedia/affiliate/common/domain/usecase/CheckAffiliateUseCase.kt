package com.tokopedia.affiliate.common.domain.usecase

import com.tokopedia.affiliatecommon.data.pojo.checkaffiliate.AffiliateCheckData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by milhamj on 10/17/18.
 */
class CheckAffiliateUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {

    private val query = """
        query affiliateCheck() {
          affiliateCheck{
            isAffiliate
            status
          }
        }
    """

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val graphqlRequest = GraphqlRequest(
                query,
                AffiliateCheckData::class.java, false
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(mapIsAffiliate())
    }

    private fun mapIsAffiliate(): Func1<GraphqlResponse, Boolean> {
        return Func1 { graphqlResponse: GraphqlResponse ->
            val data: AffiliateCheckData = graphqlResponse.getData(AffiliateCheckData::class.java)
            if (data == null || data.affiliateCheck == null) {
                throw RuntimeException()
            }
            data.affiliateCheck.isIsAffiliate
        }
    }

}