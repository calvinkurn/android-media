package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class AddToCartUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository,
    addToCartDataMapper: AddToCartDataMapper,
    chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper
): AddToCartUseCase(graphqlRepository, addToCartDataMapper, chosenAddressAddToCartRequestHelper) {

    var isError = false
    var errorMessage = arrayListOf<String>()

    override suspend fun executeOnBackground(): AddToCartDataModel {
        if(isError) {
            throw MessageErrorException("Oops!")
        } else {
            return if (errorMessage.isNotEmpty()) {
                AddToCartDataModel(
                    errorMessage = errorMessage,
                    status = "ERROR",
                    data =  DataModel()
                )
            } else {
                AddToCartDataModel(
                    status = "OK",
                    data =  DataModel(success = 1)
                )
            }
        }
    }
}