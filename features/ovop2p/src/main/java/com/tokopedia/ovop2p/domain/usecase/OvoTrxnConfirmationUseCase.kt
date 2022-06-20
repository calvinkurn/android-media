package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.constants.GQL_OVOP2P_TRANSACTION_CONFIRM
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferConfirmBase
import javax.inject.Inject


@GqlQuery("TransactionConfirmation", GQL_OVOP2P_TRANSACTION_CONFIRM)
class OvoTrxnConfirmationUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<OvoP2pTransferConfirmBase>(graphqlRepository) {

    fun getConfirmTransactionData(
        onSuccess: (OvoP2pTransferConfirmBase) -> Unit,
        onError: (Throwable) -> Unit, transferReqMap: HashMap<String, Any>
    ) {
        try {
            this.setTypeClass(OvoP2pTransferConfirmBase::class.java)
            this.setGraphqlQuery(TransactionConfirmation.GQL_QUERY)
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