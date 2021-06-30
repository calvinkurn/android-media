package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener

/**
 * Created by yfsx on 5/3/21.
 */
data class RecomCarouselSeeMoreDataModel(
        val applink: String = "",
        val backgroundImage: String = "",
        val componentName: String = "",
        val listener: RecomCommonProductCardListener? = null
): Visitable<CommonRecomCarouselCardTypeFactory> {
    override fun type(typeFactory: CommonRecomCarouselCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}