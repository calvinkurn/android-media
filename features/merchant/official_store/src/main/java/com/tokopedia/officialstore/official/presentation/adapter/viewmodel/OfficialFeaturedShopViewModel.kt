package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.HeaderShop
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class OfficialFeaturedShopViewModel(
        val featuredShop: List<Shop>,
        val headerShop: HeaderShop?,
        val categoryName: String,
        val listener: FeaturedShopListener
) : OfficialHomeVisitable{

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = this::class.java.simpleName

    override fun equalsWith(b: Any?): Boolean = b is OfficialFeaturedShopViewModel &&
            featuredShop == b.featuredShop &&
            headerShop == b.headerShop &&
            categoryName == b.categoryName &&
            featuredShop.map { it.shopId }.containsAll(b.featuredShop.map { it.shopId })

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}