package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse
import com.tokopedia.topads.sdk.repository.TopAdsRepository

class FakeTopAdsRepository : TopAdsRepository() {

    var response = TopAdsmageViewResponse(null, null, null)

    override suspend fun getImageData(queryParams: MutableMap<String, Any>): ArrayList<TopAdsImageViewModel> {
        return mapToListOfTopAdsImageViewModel(response, queryParams)
    }

}