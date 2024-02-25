package com.tokopedia.tokopedianow.shoppinglist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.query.GetShoppingListQuery
import com.tokopedia.tokopedianow.shoppinglist.domain.query.GetShoppingListQuery.PARAM_SOURCE
import com.tokopedia.tokopedianow.shoppinglist.domain.query.GetShoppingListQuery.PARAM_WAREHOUSES
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Shopping List Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/2488795997/WIP+GQL+TokonowGetShoppingList
 */
class GetShoppingListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) {
    companion object {
        private const val SOURCE = "shopping-list"
    }

    private val graphql by lazy { GraphqlUseCase<GetShoppingListDataResponse>(gqlRepository) }

    init {
        graphql.setGraphqlQuery(GetShoppingListQuery)
        graphql.setTypeClass(GetShoppingListDataResponse::class.java)
    }

    suspend fun execute(
        warehouses: List<WarehouseData>,
        source: String = SOURCE
    ): GetShoppingListDataResponse.Data {
        graphql.setRequestParams(RequestParams.create().apply {
            putObject(PARAM_WAREHOUSES, warehouses)
            putString(PARAM_SOURCE, source)
        }.parameters)

        val response = graphql.executeOnBackground().tokonowGetShoppingList

        return if (response.header.errorCode.isBlank()) {
            response.data
        } else {
            throw MessageErrorException(response.header.messages.joinToString(separator = ", "))
        }
    }
}
