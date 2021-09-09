package com.tokopedia.developer_options.sharedpref

import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdViewModel(var id: String = "") : Visitable<SimpleTypeFactory> {

    override fun type(typeFactory: SimpleTypeFactory): Int {
        return typeFactory.type(this)
    }

}
