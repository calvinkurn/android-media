package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.CoBrandCCBalanceDataModel
import javax.inject.Inject

open class GetCoBrandCCBalanceAndPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, CoBrandCCBalanceDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query cobrand_get_states_for_accounts {
            cc_cobrand_getstatesforaccounts {
                id
                icon
                title
                subtitle
                subtitle_color
                applink
                weblink
                is_active
                type
                status_name
            }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): CoBrandCCBalanceDataModel {
        return repository.request(graphqlQuery(), params)
    }
}
