package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferRequestBase
import javax.inject.Inject

class OvoP2pTransferUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<OvoP2pTransferRequestBase>(graphqlRepository) {

    fun transferOvo(onSuccess: (OvoP2pTransferRequestBase) -> Unit, onError: (Throwable) -> Unit) {

    }

}