package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.SaldoBalanceDataModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetSaldoBalanceUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, SaldoBalanceDataModel>(dispatcher) {

    override fun graphqlQuery(): String = """
        query saldo_widget_balance {
            midasGetSaldoWidgetBalance {
                id
                icon
                title
                subtitle
                subtitle_color
                applink
                weblink
                is_active
            }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): SaldoBalanceDataModel {
        return repository.request(graphqlQuery(), params)
    }
}
