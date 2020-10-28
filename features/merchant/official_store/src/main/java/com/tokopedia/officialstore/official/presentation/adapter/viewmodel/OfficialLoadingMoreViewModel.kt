package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class OfficialLoadingMoreViewModel : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = this::class.java.name

    override fun equalsWith(b: Any?): Boolean = b is OfficialLoadingMoreViewModel

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}