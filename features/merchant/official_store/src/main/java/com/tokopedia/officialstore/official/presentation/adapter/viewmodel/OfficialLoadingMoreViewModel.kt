package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.base.diffutil.OfficialTypeFactory
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class OfficialLoadingMoreViewModel : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = this::class.java.name

    override fun equalsWith(b: Any?): Boolean = b is OfficialLoadingMoreViewModel

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}