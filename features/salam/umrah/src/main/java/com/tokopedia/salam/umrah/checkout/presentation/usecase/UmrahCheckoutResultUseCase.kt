package com.tokopedia.salam.umrah.checkout.presentation.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutResultUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<UmrahCheckoutResultEntity>(graphqlRepository) {

    suspend fun execute(rawQuery: String, umrahCheckoutResultParam: UmrahCheckoutResultParams): Result<UmrahCheckoutResultEntity> {

        try {
            val params = mapOf(PARAM_CHECKOUT_PROPERTY to umrahCheckoutResultParam)
            this.setGraphqlQuery(rawQuery)
            this.setTypeClass(UmrahCheckoutResultEntity::class.java)
            this.setRequestParams(params)
            val checkoutResult = this.executeOnBackground()
            return Success(checkoutResult)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object{
        const val PARAM_CHECKOUT_PROPERTY = "params"

    }
}