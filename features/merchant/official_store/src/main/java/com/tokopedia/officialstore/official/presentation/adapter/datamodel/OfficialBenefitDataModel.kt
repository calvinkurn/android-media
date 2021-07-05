package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.data.model.Benefit
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class OfficialBenefitDataModel(val benefit: MutableList<Benefit>) : OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)

    override fun visitableId(): String = this::class.java.simpleName

    override fun equalsWith(b: Any?): Boolean = b is OfficialBenefitDataModel &&
            benefit == b.benefit &&
            benefit.map { it.id }.containsAll(b.benefit.map { it.id })
}