package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWD
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWDResponse
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_GET_INFO_AUTO_WITHDRAWAL
import javax.inject.Inject

class AutoWDInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GetInfoAutoWDResponse>(graphqlRepository) {

    fun getAutoWDInfo(onSuccess: (GetInfoAutoWD) -> Unit,
                      onError: (Throwable) -> Unit) {
        this.setTypeClass(GetInfoAutoWDResponse::class.java)
        this.setGraphqlQuery(GQL_GET_INFO_AUTO_WITHDRAWAL)
        this.execute(
                { result -> onSuccess(result.getInfoAutoWD) },
                { error -> onError(error) }
        )
    }
}