package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatus
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusResponse
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_GET_AUTO_WD_STATUS
import javax.inject.Inject

class AutoWDStatusUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDStatusResponse>(graphqlRepository) {

    fun getAutoWDStatus(onSuccess: (AutoWDStatus) -> Unit,
                        onError: (Throwable) -> Unit) {
        this.setTypeClass(AutoWDStatusResponse::class.java)
        this.setGraphqlQuery(GQL_GET_AUTO_WD_STATUS)
        this.execute(
                { result ->
                    onSuccess(result.autoWDStatus)
                }, { error ->
            onError(error)
        })
    }

}