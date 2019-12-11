package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem


class ProductRecommendationViewModel(
        val productItem: RecommendationItem,
        val listener: RecommendationListener
) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}