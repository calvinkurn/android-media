package com.tokopedia.shop.page.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.ModerateSubmitInput
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoModerate
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.HashMap
import javax.inject.Inject

class ModerateShopUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                              private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {


    companion object {
        private const val PARAM_INPUT = "input"
        private const val SHOP_ID = "shopIDs"
        private const val REASON_ID = "reasonID"
        private const val PARAM_STATUS = "status"
        private const val PARAM_NOTES = "notes"
        private const val PARAM_START_DATE = "startDate"
        private const val STATUS_VALUE = 1
        fun createRequestParams(shopIds: Int,notes:String): RequestParams {
            val requestParams = RequestParams.create()

            requestParams.putInt(SHOP_ID, shopIds)
            requestParams.putInt(PARAM_STATUS, STATUS_VALUE)
            requestParams.putString(PARAM_NOTES, notes)
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

        val graphqlRequest = GraphqlRequest(query, ShopInfoModerate::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ShopInfoModerate? = it.getData(ShopInfoModerate::class.java)
            if (data == null) {
                throw RuntimeException()
            } else if (TextUtils.isEmpty(data.data.moderateShop.message).not()) {
                throw MessageErrorException(data.errors[0].message)
            }
            data.data.moderateShop.success
        }

    }

    fun getContentSubmitInput(requestParams: RequestParams): ModerateSubmitInput {
        return ModerateSubmitInput(
                arrayListOf(requestParams.getInt(SHOP_ID,0)),
                requestParams.getInt(PARAM_STATUS, 0),
                requestParams.getString(PARAM_NOTES,"")
        )
    }




}