package com.tokopedia.flashsale.management.view.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.ekstension.toListCampaignInfoViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class CampaignDetailInfoPresenter @Inject
constructor(private val userSession: UserSession,
            @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN)
            private val useCase: GraphqlUseCase<Campaign.Response>) {

    private val useCases = mutableListOf<UseCase<Any>>()

    fun detachView() {
        useCases.map { it.unsubscribe() }
    }

    fun getCampaignInfo(rawQuery: String, campaignUrl: String, onSuccess: (List<CampaignInfoViewModel>)->Unit,
                        onError: (Throwable)->Unit) {


        val parameters = mapOf(FlashSaleConstant.PARAM_SLUG to campaignUrl,
                FlashSaleConstant.PARAM_SHOP_ID to userSession.shopId.toInt())

        useCase.setGraphqlQuery(rawQuery)
        useCase.setRequestParams(parameters)
        useCase.execute({onSuccess(it.result.campaign.toListCampaignInfoViewModel())}, onError)
        useCases += useCase
    }
}