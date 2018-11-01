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
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/* context for dummy data */
class CampaignPresenter @Inject
constructor(private @ApplicationContext val context: Context,
            private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase)  {

    private val useCases = mutableListOf<UseCase<Any>>()

    fun getCampaignList(rawQuery: String, all: String, offset: Int, rows: Int,
                                 campaign_type: Int, q: String, status: String,
                        onSuccess: (DataCampaignList)-> Unit, onError: (Throwable)-> Unit) {

        val useCase = GraphqlUseCase(multiRequestGraphqlUseCase, DataCampaignList::class.java)

        val parameters = mapOf(FlashSaleConstant.PARAM_ALL to all,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_CAMPAIGN_TYPE to campaign_type,
                FlashSaleConstant.PARAM_QUERY to q,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_STATUS to status)

        useCase.setGraphqlQuery(rawQuery)
        useCase.setRequestParams(parameters)
        useCase.execute(onSuccess){
            onSuccess(Gson()
                    .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign),
                            DataCampaignList::class.java))
        }
        useCases.add(useCase)
    }

    fun getCampaignLabel(rawQuery: String, onSuccess: (DataCampaignLabel) -> Unit, onError: (Throwable) -> Unit) {
        val useCase = GraphqlUseCase(multiRequestGraphqlUseCase, DataCampaignLabel::class.java)
        useCase.setGraphqlQuery(rawQuery)
        useCase.execute(onSuccess){
            onSuccess(Gson()
                    .fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_label),
                            DataCampaignLabel::class.java))
        }
        useCases.add(useCase)
    }

    fun detachView() {
        useCases.map { it.unsubscribe() }
    }
}