package com.tokopedia.flashsale.management.view.presenter

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.RequestError
import com.tokopedia.flashsale.management.data.ResponseError
import com.tokopedia.flashsale.management.data.Success
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.flashsale.management.domain.SingleGraphqlUseCaseKt
import com.tokopedia.flashsale.management.ekstension.thenOnUI
import com.tokopedia.flashsale.management.util.AppExecutors
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

/* context for dummy data */
class CampaignPresenter @Inject
constructor(private @ApplicationContext val context: Context)  {

    private val job = Job()

    fun getCampaignList(rawQuery: String, all: String, offset: Int, rows: Int,
                                 campaign_type: Int, q: String, status: String,
                        onSuccess: (DataCampaignList)-> Unit, onError: (Throwable)-> Unit) {
        val parameters = mapOf(FlashSaleConstant.PARAM_ALL to all,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_CAMPAIGN_TYPE to campaign_type,
                FlashSaleConstant.PARAM_QUERY to q,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_STATUS to status)

        val graphqlRequest = GraphqlRequest(rawQuery, DataCampaignList::class.java, parameters)
        val useCase = SingleGraphqlUseCaseKt(DataCampaignList::class.java)
        useCase.setRequest(graphqlRequest)

        GlobalScope.launch(AppExecutors.uiContext + job){
            async(AppExecutors.networkContext) {
                useCase.getResponse()
            }.thenOnUI {
                when(it){
                    is ResponseError -> {
                        //onError(MessageErrorException(it.error.getOrNull(0)))
                        onSuccess(Gson()
                                .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign),
                                        DataCampaignList::class.java))
                    }
                    is RequestError -> {
                        //onError(it.error)
                        onSuccess(Gson()
                                .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign),
                                DataCampaignList::class.java))
                    }
                    is Success -> onSuccess(it.response)
                }
            }
        }
    }

    fun getCampaignLabel(rawQuery: String, onSuccess: (DataCampaignLabel) -> Unit, onError: (Throwable) -> Unit) {
        val graphqlRequest = GraphqlRequest(rawQuery, DataCampaignLabel::class.java)
        val useCase = SingleGraphqlUseCaseKt(DataCampaignLabel::class.java)
        useCase.setRequest(graphqlRequest)
        GlobalScope.launch(AppExecutors.uiContext + job) {
            async(AppExecutors.networkContext) {
                useCase.getResponse()
            }.thenOnUI {
                when(it){
                    is ResponseError, is RequestError -> {
                        onSuccess(Gson()
                                .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_label),
                                        DataCampaignLabel::class.java))
                    }
                    is Success -> onSuccess(it.response)
                }
            }
        }
    }

    fun detachView() {
        job.cancel()
    }
}