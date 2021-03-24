package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class OfficialFeaturedShopDataModel(
        val channel: Channel,
        val categoryName: String,
        val listener: FeaturedShopListener
) : OfficialHomeVisitable{

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = this::class.java.simpleName

    override fun equalsWith(b: Any?): Boolean = b is OfficialFeaturedShopDataModel &&
            channel == b.channel &&
            categoryName == b.categoryName &&
            channel.grids.map { it.id }.containsAll(b.channel.grids.map { it.id })

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}