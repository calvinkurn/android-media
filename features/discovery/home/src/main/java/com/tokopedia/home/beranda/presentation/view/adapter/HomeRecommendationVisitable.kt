package com.tokopedia.home.beranda.presentation.view.adapter

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

interface HomeRecommendationVisitable : SmartVisitable<HomeRecommendationTypeFactory> {
    fun getChangePayloadFrom(b: Any?): Bundle?
}