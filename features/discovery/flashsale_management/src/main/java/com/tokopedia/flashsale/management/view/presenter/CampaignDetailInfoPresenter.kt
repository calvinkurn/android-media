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
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.domain.SingleGraphqlUseCaseKt
import com.tokopedia.flashsale.management.ekstension.thenOnUI
import com.tokopedia.flashsale.management.ekstension.toListCampaignInfoViewModel
import com.tokopedia.flashsale.management.util.AppExecutors
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class CampaignDetailInfoPresenter @Inject constructor(private @ApplicationContext val context: Context) {

    private val job = Job()

    fun detachView() {
        job.cancel()
    }

    fun getCampaignInfo(rawQuery: String, campaignUrl: String, onSuccess: (List<CampaignInfoViewModel>)->Unit,
                        onError: (Throwable)->Unit) {
        val parameters = mapOf(FlashSaleConstant.PARAM_CAMPAIGN_URL to campaignUrl)

        val graphqlRequest = GraphqlRequest(rawQuery, Campaign::class.java, parameters)
        val useCase = SingleGraphqlUseCaseKt(Campaign::class.java)
        useCase.setRequest(graphqlRequest)
        GlobalScope.launch(AppExecutors.uiContext+job) {
            async(AppExecutors.networkContext){
                useCase.getResponse()
            }.thenOnUI {
                when(it){
                    is ResponseError -> {
                        //onError(MessageErrorException(it.error.getOrNull(0)))
                        onSuccess(Gson().fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign_info),
                                Campaign::class.java).toListCampaignInfoViewModel())
                    }
                    is RequestError -> {
                        //onError(it.error)
                        onSuccess(Gson().fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign_info),
                                Campaign::class.java).toListCampaignInfoViewModel())
                    }
                    is Success -> onSuccess(it.response.toListCampaignInfoViewModel())
                }
            }
        }
    }
}