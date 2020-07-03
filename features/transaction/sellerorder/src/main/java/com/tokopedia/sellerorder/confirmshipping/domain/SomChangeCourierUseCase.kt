package com.tokopedia.sellerorder.confirmshipping.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomChangeCourierUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomChangeCourier.Data>) {

    suspend fun execute(query: String): Result<SomChangeCourier.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomChangeCourier.Data::class.java)

        return try {
            val changeCourier = useCase.executeOnBackground()
            Success(changeCourier)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}