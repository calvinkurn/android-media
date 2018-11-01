package com.tokopedia.flashsale.management.view.presenter

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.ekstension.toListCampaignInfoViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CampaignDetailInfoPresenter @Inject
constructor(private @ApplicationContext val context: Context,
            private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) {

    private val useCases = mutableListOf<UseCase<Any>>()

    fun detachView() {
        useCases.map { it.unsubscribe() }
    }

    fun getCampaignInfo(rawQuery: String, campaignUrl: String, onSuccess: (List<CampaignInfoViewModel>)->Unit,
                        onError: (Throwable)->Unit) {


        val parameters = mapOf(FlashSaleConstant.PARAM_CAMPAIGN_URL to campaignUrl)
        val useCase = GraphqlUseCase(multiRequestGraphqlUseCase, Campaign::class.java)
        useCase.setGraphqlQuery(rawQuery)
        useCase.setRequestParams(parameters)
        useCase.execute({onSuccess(it.toListCampaignInfoViewModel())}){
            onSuccess(Gson().fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign_info),
                    Campaign::class.java).toListCampaignInfoViewModel())
        }
        useCases += useCase
    }
}