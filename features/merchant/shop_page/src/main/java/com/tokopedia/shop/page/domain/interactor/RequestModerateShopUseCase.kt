package com.tokopedia.shop.page.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.ModerateSubmitInput
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.HashMap
import javax.inject.Inject

class RequestModerateShopUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                     private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {


    companion object {
        private const val PARAM_INPUT = "input"
        private const val SHOP_ID = "shopIDs"
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
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.mutation_moderate_shop
        )

        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = getContentSubmitInput(requestParams)

        val graphqlRequest = GraphqlRequest(query, ShopModerateData::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ShopModerateData? = it.getData(ShopModerateData::class.java)
            val error: MutableList<GraphqlError>? = it.getError(GraphqlError::class.java)

            if (data == null) {
                throw RuntimeException()
            } else if (error!=null && !error[0].message.isEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data.moderateShop.success
        }
    }

    fun getContentSubmitInput(requestParams: RequestParams): ModerateSubmitInput {
        return ModerateSubmitInput(
                shopIDs = arrayListOf(requestParams.getInt(SHOP_ID,0)),
                status = requestParams.getInt(PARAM_STATUS, 0),
                notes = requestParams.getString(PARAM_NOTES,""),
                responseDesc = requestParams.getString(PARAM_DESC,"")
        )
    }

}