package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.FpmDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
class FpmDebuggerViewModel : Visitable<FpmDebuggerTypeFactory>, Parcelable {
    var id: Long = 0
    var name: String? = null
    var duration: Long = 0
    var metrics: String? = null
    var attributes: String? = null
    var previewMetrics: String? = null
    var previewAttributes: String? = null
    var timestamp: String? = null

    override fun type(typeFactory: FpmDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }
}
