package com.tokopedia.search.result.mps.chooseaddress

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory

object ChooseAddressDataView: Visitable<MPSTypeFactory> {

    override fun type(typeFactory: MPSTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
