package com.tokopedia.product.addedit.tooltip.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory

data class TooltipModel(var id : Int = 0,
                        var title : String = "",
                        var subtitle : String,
                        var image : String = "")
    : Visitable<TooltipTypeFactory>{
    override fun type(typeFactory: TooltipTypeFactory): Int = typeFactory.type(this)
}