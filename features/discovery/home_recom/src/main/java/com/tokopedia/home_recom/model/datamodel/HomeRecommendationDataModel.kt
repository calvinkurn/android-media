package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

interface HomeRecommendationDataModel : Visitable<HomeRecommendationTypeFactory> {
    fun name(): String
    fun equalsWith(newData: HomeRecommendationDataModel): Boolean
    fun newInstance(): HomeRecommendationDataModel
    fun getChangePayload(newData: HomeRecommendationDataModel): Bundle?
}