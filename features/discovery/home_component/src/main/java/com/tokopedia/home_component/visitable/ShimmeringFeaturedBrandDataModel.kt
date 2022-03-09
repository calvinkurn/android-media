package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class ShimmeringFeaturedBrandDataModel(
    val name : String = "shimmering_featured_brand"
): HomeComponentVisitable {
    override fun visitableId(): String? {
        return name
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is ShimmeringFeaturedBrandDataModel
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}