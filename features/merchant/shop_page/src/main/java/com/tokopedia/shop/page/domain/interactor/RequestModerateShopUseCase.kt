package com.tokopedia.shop.page.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.data.source.cloud.model.ModerateSubmitInput
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Named

class RequestModerateShopUseCase @Inject constructor(@Named(ShopPageConstant.MODERATE_REQUEST_QUERY) private val moderateQuery:String,
                                                     private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {

    companion object {
        private const val PARAM_INPUT = "input"
        private const val SHOP_ID = "shopIds"
        private const val PARAM_DESC = "responseDesc"
        private const val PARAM_STATUS = "status"
        private const val PARAM_NOTES = "notes"
        private const val STATUS_VALUE = 1
        private const val DESC_VALUE = "Moderate"


        fun createRequestParams(shopIds: Int, moderateNotes :String): RequestParams {
            val requestParams = RequestParams.create()

            requestParams.putInt(SHOP_ID, shopIds)
            requestParams.putInt(PARAM_STATUS, STATUS_VALUE)
            requestParams.putString(PARAM_NOTES, moderateNotes)
            requestParams.putString(PARAM_DESC, DESC_VALUE)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = getModerateSubmitInput(requestParams)

        val graphqlRequest = GraphqlRequest(moderateQuery, ShopModerateData::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ShopModerateData? = it.getData(ShopModerateData::class.java)
            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error[0].message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data.moderateShop.success
        }
    }

    private fun getModerateSubmitInput(requestParams: RequestParams): ModerateSubmitInput {
        return ModerateSubmitInput(
                shopIds = arrayListOf(requestParams.getInt(SHOP_ID,0)),
                status = requestParams.getInt(PARAM_STATUS, 0),
                notes = requestParams.getString(PARAM_NOTES,""),
                responseDesc = requestParams.getString(PARAM_DESC,"")
        )
    }

}