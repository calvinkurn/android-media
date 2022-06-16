package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.constants.GQL_OVO_THNAKYOU_PAGE
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferThankyouBase
import javax.inject.Inject

@GqlQuery("ThankYouPageGql", GQL_OVO_THNAKYOU_PAGE)
class OvoTrnxThankyouPageUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<OvoP2pTransferThankyouBase>(graphqlRepository) {


    fun getThankyouPageData(
        onSuccess: (OvoP2pTransferThankyouBase) -> Unit,
        onError: (Throwable) -> Unit, transferReqMap: HashMap<String, Any>
    ) {
        try {
            this.setTypeClass(OvoP2pTransferThankyouBase::class.java)
            this.setGraphqlQuery(ThankYouPageGql.GQL_QUERY)
            this.setRequestParams(transferReqMap)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }


}