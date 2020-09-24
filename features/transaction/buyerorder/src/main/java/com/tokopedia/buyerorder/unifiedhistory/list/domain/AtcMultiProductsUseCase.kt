package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.google.gson.JsonArray
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.AtcMultiData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/08/20.
 */
class AtcMultiProductsUseCase @Inject constructor(private val useCase: GraphqlUseCase<AtcMultiData>) {

    suspend fun execute(query: String, paramJsonArray: JsonArray): Result<AtcMultiData> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(AtcMultiData::class.java)
        useCase.setRequestParams(generateParam(paramJsonArray))

        return try {
            val atc = useCase.executeOnBackground()
            Success(atc)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(jsonArray: JsonArray): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM to jsonArray)
    }
}