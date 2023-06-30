package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.DriverPhoneNumberMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.stub.common.graphql.interactor.GraphqlUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.DriverPhoneNumberMapperStub

class GetDriverPhoneNumberUseCaseStub(
    private val useCase: GraphqlUseCaseStub<DriverPhoneNumberResponse>,
    private val driverPhoneNumberMapper: DriverPhoneNumberMapper
) : GetDriverPhoneNumberUseCase(useCase, driverPhoneNumberMapper) {

    var responseStub: DriverPhoneNumberResponse = DriverPhoneNumberResponse()
        set(value) {
            useCase.createMapResult(DriverPhoneNumberResponse::class.java, value)
            field = value
        }

    override suspend fun execute(orderId: String): DriverPhoneNumberUiModel {
        useCase.setRequestParams(createRequestParams(orderId))
        val response = useCase.executeOnBackground().tokofoodDriverPhoneNumber
        return driverPhoneNumberMapper.mapToDriverPhoneNumberUiModel(response)
    }
}
