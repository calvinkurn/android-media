package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget


class ProductRecommendationViewModel(val product: RecommendationWidget) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getProductRecommendationList() {
        Log.d("products: ", product.toString())
    }

}