package com.tokopedia.flashsale.management.view.presenter

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

/* context for dummy data */
class CampaignPresenter @Inject
constructor(private @ApplicationContext val context: Context,
            @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LABEL)
            private val getCampaignLabelUseCase: GraphqlUseCase<DataCampaignLabel>,
            @Named(FlashSaleConstant.NAMED_REQUEST_CAMPAIGN_LIST)
            private val getCampaignListUseCase: GraphqlUseCase<DataCampaignList>)  {

    private val useCases = mutableListOf<UseCase<Any>>()

    fun getCampaignList(rawQuery: String, all: String, offset: Int, rows: Int,
                                 campaign_type: Int, q: String, status: String,
                        onSuccess: (DataCampaignList)-> Unit, onError: (Throwable)-> Unit) {

        val parameters = mapOf(FlashSaleConstant.PARAM_ALL to all,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_CAMPAIGN_TYPE to campaign_type,
                FlashSaleConstant.PARAM_QUERY to q,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_STATUS to status)

        getCampaignListUseCase.setGraphqlQuery(rawQuery)
        getCampaignListUseCase.setRequestParams(parameters)
        getCampaignListUseCase.execute(onSuccess){
            onSuccess(Gson()
                    .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign),
                            DataCampaignList::class.java))
        }
        useCases.add(getCampaignListUseCase)
    }

    fun getCampaignLabel(rawQuery: String, onSuccess: (DataCampaignLabel) -> Unit, onError: (Throwable) -> Unit) {

        getCampaignLabelUseCase.setGraphqlQuery(rawQuery)
        getCampaignLabelUseCase.execute(onSuccess){
            onSuccess(Gson()
                    .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_label),
                            DataCampaignLabel::class.java))
        }
        useCases.add(getCampaignLabelUseCase)
    }

    fun detachView() {
        useCases.map { it.unsubscribe() }
    }
}