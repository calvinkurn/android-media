package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class OfficialTopAdsBannerDataModel(var title: String?, var tdnBanner: ArrayList<TopAdsImageViewModel>? = null) : OfficialHomeVisitable {
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = null

    override fun equalsWith(b: Any?): Boolean = b is OfficialTopAdsBannerDataModel && tdnBanner == b.tdnBanner

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}
