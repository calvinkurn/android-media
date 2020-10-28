package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.base.diffutil.OfficialTypeFactory
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class OfficialBannerViewModel(val banner: MutableList<Banner>, val categoryName: String) : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = categoryName

    override fun equalsWith(b: Any?): Boolean = b is OfficialBannerViewModel &&
            banner == b.banner &&
            categoryName == b.categoryName &&
            banner.map { it.bannerId }.containsAll(b.banner.map { it.bannerId })


    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}