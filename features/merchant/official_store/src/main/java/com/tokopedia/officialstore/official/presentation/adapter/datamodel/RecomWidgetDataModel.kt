package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class RecomWidgetDataModel(val recomWidgetList: RecommendationWidget, val categoryName: String) :
    OfficialHomeVisitable {
    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun visitableId(): String = categoryName

    override fun equalsWith(b: Any?): Boolean = b is RecomWidgetDataModel

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}
