package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 18/11/20.
 */
class GetOrderDetailUseCase @Inject constructor(private val useCase: GraphqlUseCase<DetailsData>) {

    /*suspend fun execute(query: String, requestCancelParam: BuyerRequestCancelParam): Result<DetailsData> {
        useCase.setGraphqlQuery(query)
        // useCase.setTypeClass(DetailsData::class.java)
        // useCase.setRequestParams(generateParam(requestCancelParam))

        return try {
            val cancellationReason = useCase.executeOnBackground()
            Success(cancellationReason)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun getVariables(postId: Int): Map<String, Any>? {
        val variables: MutableMap<String, Any> = HashMap()
        variables[com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase.PARAM_ID] = postId
        variables[com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase.PARAM_CURSOR] = com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase.FIRST_CURSOR
        variables[com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase.PARAM_LIMIT] = com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase.DEFAULT_LIMIT
        return variables
    }*/
}