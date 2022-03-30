package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectParam
import com.tokopedia.sellerorder.detail.domain.query.SomReasonRejectQuery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomReasonRejectUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomReasonRejectData.Data>) {

    suspend fun execute(reasonRejectParam: SomReasonRejectParam): Result<SomReasonRejectData.Data> {
        useCase.setGraphqlQuery(SomReasonRejectQuery)
        useCase.setTypeClass(SomReasonRejectData.Data::class.java)
        useCase.setRequestParams(generateParam(reasonRejectParam))

        return try {
            val reasonReject = useCase.executeOnBackground()
            Success(reasonReject)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(reasonRejectParam: SomReasonRejectParam): Map<String, SomReasonRejectParam> {
        return mapOf(PARAM_INPUT to reasonRejectParam)
    }
}