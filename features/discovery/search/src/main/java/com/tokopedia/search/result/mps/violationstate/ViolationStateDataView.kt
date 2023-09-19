package com.tokopedia.search.result.mps.violationstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory

class ViolationStateDataView(val violationType: ViolationType= ViolationType.BLACKLISTED): Visitable<MPSTypeFactory> {


    override fun type(typeFactory: MPSTypeFactory?): Int =
        typeFactory?.type(this) ?: 0
}
