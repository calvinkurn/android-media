package com.tokopedia.product.addedit.tooltip.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory

open class TooltipModel(open var id : Int = 0,
                        open var title : String = "")
    : Visitable<TooltipTypeFactory>{
    override fun type(typeFactory: TooltipTypeFactory): Int = typeFactory.type(this)
}