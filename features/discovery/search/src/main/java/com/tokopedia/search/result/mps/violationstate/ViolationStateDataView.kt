package com.tokopedia.search.result.mps.violationstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory

class ViolationStateDataView(val typeViolation: TypeViolation= TypeViolation.BLACKLISTED): Visitable<MPSTypeFactory> {


    override fun type(typeFactory: MPSTypeFactory?): Int =
        typeFactory?.type(this) ?: 0
}

enum class TypeViolation {
    BANNED,
    BLACKLISTED
}
