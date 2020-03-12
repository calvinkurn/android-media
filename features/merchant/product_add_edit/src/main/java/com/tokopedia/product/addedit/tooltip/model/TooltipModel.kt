package com.tokopedia.product.addedit.tooltip.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.tooltip.adapter.TooltipTypeFactory

abstract class TooltipModel(open var title : String = ""):
        Visitable<TooltipTypeFactory>