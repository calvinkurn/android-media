package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.CloseShopScheduleMutation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class UpdateShopScheduleUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<CloseShopScheduleMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<CloseShopScheduleMutation>(context, CloseShopScheduleMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_close_shop_schedule)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val action = requestParams.getInt(ACTION, 0)
                variables[ACTION] = action

                // close end, to shop to start open
                val closeEnd = requestParams.getString(CLOSE_END, "")
                if (!TextUtils.isEmpty(closeEnd)) {
                    variables[CLOSE_END] = (closeEnd.toLong() / 1000L).toString()
                }

                val closeNow = requestParams.getBoolean(CLOSE_NOW, false)
                variables[CLOSE_NOW] = closeNow

                if (closeNow) {
                    // closeNow true must have end date; the business currently does not allow empty end date
                    if (TextUtils.isEmpty(closeEnd)) {
                        val closeStart = requestParams.getString(CLOSE_START, "")
                        if (!closeStart.isNullOrEmpty()) {
                            variables[CLOSE_END] = closeStart
                        }
                    }
                } else { // open with schedule start
                    val closeStart = requestParams.getString(CLOSE_START, "")
                    if (!TextUtils.isEmpty(closeStart)) {
                        variables[CLOSE_START] = (closeStart.toLong() / 1000L).toString()
                    }
                }

                val closeNote = requestParams.getString(CLOSE_NOTE, "")
                variables[CLOSE_NOTE] = closeNote
                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLSuccessMapper())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {
        val ACTION = "action"
        val CLOSE_START = "closeStart"
        val CLOSE_END = "closeEnd"
        val CLOSE_NOW = "closeNow"
        val CLOSE_NOTE = "closeNote"

        /**
         * update shop schedule
         *
         * @param action     1:to open; 0: to close
         * @param closeNow   true means close start = today
         * @param closeStart schedule for shop to close
         * @param closeEnd   schedule for shop to open
         * @param closeNote  close note for open
         */
        @JvmStatic
        fun createRequestParams(@ShopScheduleActionDef action: Int,
                                closeNow: Boolean,
                                closeStart: String?,
                                closeEnd: String?,
                                closeNote: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(ACTION, action)
            requestParams.putBoolean(CLOSE_NOW, closeNow)
            requestParams.putString(CLOSE_START, closeStart)
            requestParams.putString(CLOSE_END, closeEnd)
            requestParams.putString(CLOSE_NOTE, closeNote)
            return requestParams
        }
    }
}
