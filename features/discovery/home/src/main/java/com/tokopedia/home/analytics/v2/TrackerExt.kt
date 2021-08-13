package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.LabelGroup

/**
 * Created by Lukas on 2/18/21.
 */

var LABEL_FULFILLMENT: String = "fulfillment"

fun List<LabelGroup>.hasLabelGroupFulfillment(): Boolean{
    return this.any { it.position == LABEL_FULFILLMENT }
}