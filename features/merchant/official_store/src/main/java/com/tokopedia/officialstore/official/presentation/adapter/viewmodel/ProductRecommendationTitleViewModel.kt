package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory

class ProductRecommendationTitleViewModel (val title: String): OfficialHomeVisitable{
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String? = title

    override fun equalsWith(b: Any?): Boolean = b is ProductRecommendationTitleViewModel &&
            title == b.title

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)

}