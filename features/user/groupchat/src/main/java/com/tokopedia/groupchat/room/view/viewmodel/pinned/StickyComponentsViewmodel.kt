package com.tokopedia.groupchat.room.view.viewmodel.pinned

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.android.parcel.Parcelize

@Parcelize
class StickyComponentsViewModel(var list :List<StickyComponentViewModel>) : Visitable<Any>, Parcelable {

    override fun type(typeFactory: Any?): Int {
        return 0
    }

    companion object {
        const val TYPE = "sticky_components"
    }
}