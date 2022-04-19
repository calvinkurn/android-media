package com.tokopedia.play.domain

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-06.
 */
class PostAddToCartUseCase @Inject constructor(
        private val addToCartUseCase: AddToCartUseCase
) : UseCase<AddToCartDataModel>() {

    lateinit var parameters: AddToCartRequestParams

    override suspend fun executeOnBackground(): AddToCartDataModel {
        if(!this::parameters.isInitialized) throw IllegalArgumentException("Please provide add to cart params")

        return try {
            addToCartUseCase.setParams(parameters)
            addToCartUseCase.executeOnBackground()
        } catch (exception: Exception) {
            if (exception is ResponseErrorException)
                throw MessageErrorException(exception.message)
            else throw exception
        }
    }
}