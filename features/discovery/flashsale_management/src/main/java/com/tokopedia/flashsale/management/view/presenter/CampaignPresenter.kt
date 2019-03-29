package com.tokopedia.flashsale.management.view.presenter

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import javax.inject.Inject
import javax.inject.Named


class CampaignPresenter @Inject
constructor(private val userSession: UserSessionInterface,
            @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LABEL)
            private val getCampaignLabelUseCase: GraphqlUseCase<DataCampaignLabel.Response>,
            @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LIST)
            private val getCampaignListUseCase: GraphqlUseCase<DataCampaignList.Response>)  {

    private val useCases = mutableListOf<UseCase<Any>>()

    fun getCampaignList(rawQuery: String, all: Boolean, offset: Int, rows: Int,
                                 campaign_type: Int, q: String = "", status: String = "",
                        onSuccess: (DataCampaignList.ResponseData)-> Unit, onError: (Throwable)-> Unit) {

        val parameters = mapOf(FlashSaleConstant.PARAM_ALL to all,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_CAMPAIGN_TYPE to campaign_type,
                FlashSaleConstant.PARAM_Q to q,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_STATUS to status,
                FlashSaleConstant.PARAM_SHOP_ID to userSession.shopId.toInt())

        getCampaignListUseCase.setGraphqlQuery(rawQuery)
        getCampaignListUseCase.setRequestParams(parameters)
        getCampaignListUseCase.execute({onSuccess(it.result)}, onError)
        useCases.add(getCampaignListUseCase)
    }

    fun getCampaignLabel(rawQuery: String, onSuccess: (DataCampaignLabel) -> Unit, onError: (Throwable) -> Unit) {
        getCampaignLabelUseCase.setGraphqlQuery(rawQuery)
        getCampaignLabelUseCase.execute({onSuccess(it.result)}, onError)
        useCases.add(getCampaignLabelUseCase)
    }

    fun detachView() {
        useCases.map { it.cancelJobs() }
    }
}