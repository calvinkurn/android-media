package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

interface OfficialHomeVisitable : Visitable<OfficialHomeTypeFactory>{
    fun visitableId(): String?
    fun equalsWith(b: Any?): Boolean
    fun getChangePayloadFrom(b: Any?): Bundle?
}