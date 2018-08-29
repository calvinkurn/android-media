package com.tokopedia.product.edit.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory

class TitleVideoRecommendationViewModel : Visitable<ProductAddVideoRecommendationAdapterTypeFactory>, ProductAddVideoRecommendationBaseViewModel{

    override fun type(typeFactory: ProductAddVideoRecommendationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}