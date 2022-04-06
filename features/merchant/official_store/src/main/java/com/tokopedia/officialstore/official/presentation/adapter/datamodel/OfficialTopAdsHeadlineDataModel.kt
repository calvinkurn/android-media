package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse


data class OfficialTopAdsHeadlineDataModel(
    val topAdsHeadlineResponse: TopAdsHeadlineResponse = TopAdsHeadlineResponse(),
) : OfficialHomeVisitable {

    override fun visitableId(): String = this::class.java.name

    override fun equalsWith(b: Any?): Boolean = b is OfficialLoadingMoreDataModel

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)

}
