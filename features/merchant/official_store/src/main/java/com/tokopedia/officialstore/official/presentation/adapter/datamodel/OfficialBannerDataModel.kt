package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class OfficialBannerDataModel(val banner: MutableList<Banner>, val categoryName: String) : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = categoryName

    override fun equalsWith(b: Any?): Boolean = b is OfficialBannerDataModel &&
            banner == b.banner &&
            categoryName == b.categoryName &&
            banner.map { it.bannerId }.containsAll(b.banner.map { it.bannerId })


    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}