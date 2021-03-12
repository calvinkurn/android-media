package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.model.SomEditRefNumRequestParam
import com.tokopedia.sellerorder.common.domain.model.SomEditRefNumResponse
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 11/05/20.
 */
class SomEditRefNumUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomEditRefNumResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomEditRefNumResponse.Data::class.java)
    }

    suspend fun execute(): SomEditRefNumResponse.Data = useCase.executeOnBackground()

    fun setParams(params: SomEditRefNumRequestParam) {
        useCase.setRequestParams(RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters)
    }

    companion object {
        private val QUERY = """
            mutation MpLogisticEditRefNum(${'$'}input: MPLogisticEditRefNumInputs!){
              mpLogisticEditRefNum(input: ${'$'}input) {
                message
              }
            }
        """.trimIndent()
    }
}