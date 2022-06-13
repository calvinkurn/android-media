package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class AddToCartOccMultiUseCaseStub @Inject constructor(
    graphqlRepositoryStub: GraphqlRepository,
    addToCartDataMapper: AddToCartDataMapper,
    chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper
) : AddToCartOccMultiUseCase(
    graphqlRepositoryStub,
    addToCartDataMapper,
    chosenAddressAddToCartRequestHelper
) {

    var isError = false
    var errorMessage = listOf<String>()

    override suspend fun executeOnBackground(): AddToCartOccMultiDataModel {
        if(isError) {
            throw MessageErrorException("Oops!")
        } else {
            return if (errorMessage.isNotEmpty()) {
                AddToCartOccMultiDataModel(
                    errorMessage = errorMessage,
                    status = "ERROR",
                    data =  AddToCartOccMultiData(success = 0)
                )
            } else {
                AddToCartOccMultiDataModel(
                    status = "OK",
                    data =  AddToCartOccMultiData(success = 1)
                )
            }
        }
    }
}