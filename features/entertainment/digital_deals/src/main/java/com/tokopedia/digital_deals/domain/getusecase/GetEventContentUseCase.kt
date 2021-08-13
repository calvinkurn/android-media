package com.tokopedia.digital_deals.domain.getusecase

import android.content.Context
import com.tokopedia.digital_deals.di.scope.DealsScope
import com.tokopedia.digital_deals.domain.util.DealsQuery
import com.tokopedia.digital_deals.view.model.response.EventContentData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class GetEventContentUseCase @Inject constructor(
        @DealsScope private val context: Context,
        private val gqlUseCase: GraphqlUseCase<EventContentData>
){
    private val paramTypeId = "typeID"
    private val paramTypeValue = "typeValue"

    fun getEventContent(
            onSuccess: (EventContentData) -> Unit,
            onError: (Throwable) -> Unit,
            typeId: String,
            typeValue: String
    ){
        val params = generateParams(typeId, typeValue)

        gqlUseCase.apply {
            setTypeClass(EventContentData::class.java)
            setRequestParams(params)
            setGraphqlQuery(DealsQuery.eventContentById())
            execute({
                onSuccess(it)
            }, {
                onError(it)
            })
        }
    }

    private fun generateParams(typeId: String, typeValue: String): Map<String, Any> {
        return mapOf(
                paramTypeId to typeId,
                paramTypeValue to typeValue
        )
    }
}