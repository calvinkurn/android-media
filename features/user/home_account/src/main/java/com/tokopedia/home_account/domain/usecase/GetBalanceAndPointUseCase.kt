package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@GqlQuery("GetBalanceAndPointQuery", GetBalanceAndPointUseCase.query)
class GetBalanceAndPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, BalanceAndPointDataModel>(dispatcher) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: String): BalanceAndPointDataModel {
        val mapParams = mapOf(
            PARAM_PARTNER_CODE to params
        )
        return repository.request(GetBalanceAndPointQuery(), mapParams)
    }

    companion object {
        private const val PARAM_PARTNER_CODE = "partnerCode"

        private const val partnerCode = "\$partnerCode"

        const val query: String = """
            query walletappGetAccountBalance($partnerCode: String!){
                walletappGetAccountBalance(partnerCode:$partnerCode){
                    id
                    icon
                    title
                    subtitle
                    subtitle_color
                    applink
                    weblink
                    is_active
                    is_show
                }
            }
        """
    }
}