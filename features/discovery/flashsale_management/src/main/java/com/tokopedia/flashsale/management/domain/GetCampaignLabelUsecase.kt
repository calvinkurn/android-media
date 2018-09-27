package com.tokopedia.flashsale.management.domain

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaign_label.DataCampaignLabel
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetCampaignLabelUsecase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<DataCampaignLabel>() {

    override fun createObservable(requestParams: RequestParams): Observable<DataCampaignLabel> {

        return Observable.just(Gson().fromJson(GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_label), DataCampaignLabel::class.java))
    }

    override fun unsubscribe() {
        graphqlUseCase.unsubscribe()
        super.unsubscribe()
    }
}