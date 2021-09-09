package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.SimpleTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdViewModel(var id: String = "") : Visitable<SimpleTypeFactory>, Parcelable {

    override fun type(typeFactory: SimpleTypeFactory): Int {
        return typeFactory.type(this)
    }

}
