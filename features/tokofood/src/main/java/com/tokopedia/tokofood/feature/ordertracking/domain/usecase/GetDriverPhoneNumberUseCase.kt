package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.DriverPhoneNumberMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.DRIVER_PHONE_NUMBER_QUERY
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("DriverPhoneNumberQuery", DRIVER_PHONE_NUMBER_QUERY)
open class GetDriverPhoneNumberUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<DriverPhoneNumberResponse>,
    private val driverPhoneNumberMapper: DriverPhoneNumberMapper
) {
    init {
        useCase.setGraphqlQuery(DriverPhoneNumberQuery())
        useCase.setTypeClass(DriverPhoneNumberResponse::class.java)
    }

    open suspend fun execute(orderId: String): DriverPhoneNumberUiModel {
        useCase.setRequestParams(createRequestParams(orderId))
        return driverPhoneNumberMapper.mapToDriverPhoneNumberUiModel(useCase.executeOnBackground().tokofoodDriverPhoneNumber)
    }

    fun createRequestParams(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        private const val ORDER_ID_KEY = "orderID"
    }
}
