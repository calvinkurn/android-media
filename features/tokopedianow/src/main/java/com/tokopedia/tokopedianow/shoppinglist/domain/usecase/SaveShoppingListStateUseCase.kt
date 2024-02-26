package com.tokopedia.tokopedianow.shoppinglist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.query.SaveShoppingListStateQuery
import com.tokopedia.tokopedianow.shoppinglist.domain.query.SaveShoppingListStateQuery.PARAM_ACTION_LIST
import com.tokopedia.tokopedianow.shoppinglist.domain.query.SaveShoppingListStateQuery.PARAM_SOURCE
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Save Shopping List State Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/2491777736/Mutation+TokonowSaveShoppingListState
 */

class SaveShoppingListStateUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) {
    companion object {
        private const val SOURCE = "shopping-list"
    }

    private val graphql by lazy { GraphqlUseCase<SaveShoppingListStateDataResponse>(gqlRepository) }

    init {
        graphql.setGraphqlQuery(SaveShoppingListStateQuery)
        graphql.setTypeClass(SaveShoppingListStateDataResponse::class.java)
    }

    suspend fun execute(
        actionList: List<SaveShoppingListStateActionParam>,
        source: String = SOURCE
    ) {
        graphql.setRequestParams(RequestParams.create().apply {
            putString(PARAM_SOURCE, source)
            putObject(PARAM_ACTION_LIST, actionList)
        }.parameters)

        val response = graphql.executeOnBackground().tokonowSaveShoppingListState

        if (response.header.errorCode.isNotBlank()) {
            MessageErrorException(response.header.messages.joinToString(separator = ", "))
        }
    }
}

