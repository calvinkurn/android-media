package com.tokopedia.deals.pdp.domain

import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.domain.query.EventContentByIdQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsPDPEventContentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DealsProductEventContent>(graphqlRepository) {

    init {
        setGraphqlQuery(EventContentByIdQuery())
        setTypeClass(DealsProductEventContent::class.java)
    }

    suspend fun execute(typeId: String, typeValue: String): DealsProductEventContent {
        setRequestParams(createRequestParams(typeId, typeValue))
        return executeOnBackground()
    }

    private fun createRequestParams(typeId: String, typeValue: String) = HashMap<String, Any>().apply {
        put(TYPE_ID, typeId)
        put(TYPE_VALUE, typeValue)
    }

    companion object {
        private const val TYPE_ID = "typeID"
        private const val TYPE_VALUE = "typeValue"
    }
}
