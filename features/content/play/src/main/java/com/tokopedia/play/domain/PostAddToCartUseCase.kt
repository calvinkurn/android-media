package com.tokopedia.play.domain

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.play.util.exception.DefaultErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-06.
 */
class PostAddToCartUseCase @Inject constructor(
        private val addToCartUseCase: AddToCartUseCase
) : UseCase<AddToCartDataModel>() {

    var parameters: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): AddToCartDataModel {
        require(parameters.paramsAllValueInString.isEmpty()) { "Please provide add to cart params" }
        return try {
             addToCartUseCase.createObservable(parameters)
                    .toBlocking()
                    .single()
        } catch (exception: Exception) {
            throw DefaultErrorException(exception)
        }
    }

}