package com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.recommendationforyou

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.domain.model.Product
import com.tokopedia.dilayanitokopedia.ui.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class HomeRecommendationItemDataModel(
    val product: Product,
    val pageName: String = "",
    val position: Int = -1,
    val tabName: String = ""
) : HomeRecommendationVisitable, ImpressHolder() {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return product.id
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationItemDataModel

        if (product != other.product) return false

        return true
    }

    override fun hashCode(): Int {
        return product.hashCode()
    }
}
