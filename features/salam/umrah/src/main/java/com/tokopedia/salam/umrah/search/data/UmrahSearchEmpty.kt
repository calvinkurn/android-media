package com.tokopedia.salam.umrah.search.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapterTypeFactory

class UmrahSearchEmpty : Visitable<UmrahSearchAdapterTypeFactory> {
    override fun type(typeFactory: UmrahSearchAdapterTypeFactory): Int =
            typeFactory.type(this)
}