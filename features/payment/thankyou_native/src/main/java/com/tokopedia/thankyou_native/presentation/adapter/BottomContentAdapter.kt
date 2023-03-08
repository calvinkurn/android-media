package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.thankyou_native.presentation.adapter.factory.GyroRecommendationFactory

class BottomContentAdapter(private val visitableList: ArrayList<Visitable<*>>,
                           private val typeFactory: GyroRecommendationFactory
) : BaseAdapter<GyroRecommendationFactory>(typeFactory, visitableList) {

    var widgetOrder: String = ""

    fun addItemWithOrder(data: Visitable<*>) {

    }
}
