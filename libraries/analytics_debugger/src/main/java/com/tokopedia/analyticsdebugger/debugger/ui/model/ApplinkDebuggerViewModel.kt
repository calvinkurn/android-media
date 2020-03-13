package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.ApplinkDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
class ApplinkDebuggerViewModel : Visitable<ApplinkDebuggerTypeFactory>, Parcelable {
    var id: Long = 0
    var applink: String? = null
    var trace: String? = null
    var previewTrace: String? = null
    var timestamp: String? = null

    override fun type(typeFactory: ApplinkDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }

}
