package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_AUTO_WD_TNC
import javax.inject.Inject

class AutoWDTNCUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDTNCResponse>(graphqlRepository) {

    fun getAutoWDTNC(onSuccess: (GetTNCAutoWD) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDTNCResponse::class.java)
            this.setGraphqlQuery(GQL_AUTO_WD_TNC)
            this.execute(
                    { result ->
                        onSuccess(result.getTNCAutoWD)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

}