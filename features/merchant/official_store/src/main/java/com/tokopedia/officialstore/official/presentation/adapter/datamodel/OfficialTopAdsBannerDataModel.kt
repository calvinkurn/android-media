package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class OfficialTopAdsBannerDataModel(val channelModel: ChannelModel, var tdnBanner: ArrayList<TopAdsImageViewModel>? = null) : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = channelModel.id

    override fun equalsWith(b: Any?): Boolean = b is OfficialTopAdsBannerDataModel && channelModel == b.channelModel

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}
