package com.tokopedia.promotionstarget.presenter

import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.subscriber.GratificationData
import com.tokopedia.promotionstarget.usecase.GetPopGratificationUseCase
import javax.inject.Inject

class DialogManagerPresenter @Inject constructor(val getPopGratificationUseCase: GetPopGratificationUseCase) {

    suspend fun getGratificationAndShowDialog(gratificationData: GratificationData): GetPopGratificationResponse {
        val data = getPopGratificationUseCase.let {
            it.getResponse(it.getQueryParams(gratificationData.campaignSlug, gratificationData.page))
        }
        return data
    }

}