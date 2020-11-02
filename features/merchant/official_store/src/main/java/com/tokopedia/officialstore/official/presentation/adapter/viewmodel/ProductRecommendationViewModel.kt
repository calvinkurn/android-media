package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem


data class ProductRecommendationViewModel(
        val productItem: RecommendationItem,
        val listener: RecommendationListener
) : OfficialHomeVisitable {

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = productItem.productId.toString()

    override fun equalsWith(b: Any?): Boolean = b is ProductRecommendationViewModel &&
            b.productItem == productItem

}