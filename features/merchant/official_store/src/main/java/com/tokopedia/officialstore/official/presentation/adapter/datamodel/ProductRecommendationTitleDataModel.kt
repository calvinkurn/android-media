package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class ProductRecommendationTitleDataModel (val title: String): OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = title

    override fun equalsWith(b: Any?): Boolean = b is ProductRecommendationTitleDataModel &&
            title == b.title

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)

}