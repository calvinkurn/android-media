package com.tokopedia.product.addedit.tooltip.model

import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory

/**
 * Created by faisalramd on 2020-03-09.
 */

data class NumericTooltipModel(
        override var id: Int,
        override var title: String,
        var number : String = "") : TooltipModel(id, title) {
    override fun type(typeFactory: TooltipTypeFactory): Int = typeFactory.type(this)
}