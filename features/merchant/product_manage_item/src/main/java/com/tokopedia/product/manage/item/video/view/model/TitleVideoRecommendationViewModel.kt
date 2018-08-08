package com.tokopedia.product.manage.item.video.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory

class TitleVideoRecommendationViewModel : Visitable<ProductAddVideoRecommendationAdapterTypeFactory>, ProductAddVideoRecommendationBaseViewModel {

    override fun type(typeFactory: ProductAddVideoRecommendationAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}