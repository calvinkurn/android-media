package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomEditAwbResponse
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomEditRefNumUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomEditAwbResponse.Data>) {

    suspend fun execute(query: String): Result<SomEditAwbResponse.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomEditAwbResponse.Data::class.java)

        return try {
            val editAwb = useCase.executeOnBackground()
            Success(editAwb)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}