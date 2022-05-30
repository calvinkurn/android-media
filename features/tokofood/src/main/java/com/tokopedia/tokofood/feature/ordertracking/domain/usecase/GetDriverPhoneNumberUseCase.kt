package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.DriverPhoneNumberMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.query.DriverPhoneNumberQuery
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import javax.inject.Inject

class GetDriverPhoneNumberUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<DriverPhoneNumberResponse>,
    private val driverPhoneNumberMapper: DriverPhoneNumberMapper
) {
    init {
        useCase.setGraphqlQuery(DriverPhoneNumberQuery)
        useCase.setTypeClass(DriverPhoneNumberResponse::class.java)
    }

    suspend fun execute(orderId: String): DriverPhoneNumberUiModel {
        useCase.setRequestParams(DriverPhoneNumberQuery.createRequestParams(orderId))
        return driverPhoneNumberMapper.mapToDriverPhoneNumberUiModel(useCase.executeOnBackground().tokofoodDriverPhoneNumber)
    }
}