package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.constants.GQL_OVOP2P_TRANSFER_REQUEST
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import java.util.*
import javax.inject.Inject


@GqlQuery("TransferGqlQuery", GQL_OVOP2P_TRANSFER_REQUEST)
class OvoP2pTransferUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<OvoP2pTransferRequestBase>(graphqlRepository) {

    fun transferOvo(
        onSuccess: (OvoP2pTransferRequestBase) -> Unit,
        onError: (Throwable) -> Unit,
        transferReqMap: HashMap<String, Any>
    ) {

        try {
            this.setTypeClass(OvoP2pTransferRequestBase::class.java)
            this.setGraphqlQuery(TransferGqlQuery.GQL_QUERY)
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


