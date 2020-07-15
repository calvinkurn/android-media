package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.QUERY_GET_INFO_AUTO_WD
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusData
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusResponse
import javax.inject.Inject
import javax.inject.Named

class AutoWDInfoUseCase @Inject constructor(
        @Named(QUERY_GET_INFO_AUTO_WD) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDStatusResponse>(graphqlRepository) {

    fun getAutoWDInfo(onSuccess: (AutoWDStatusData) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDStatusResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.autoWDStatusData)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

}