package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.ApplinkDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApplinkDebuggerViewModel(var id: Long = 0, var applink: String? = null,
                               var trace: String? = null, var previewTrace: String? = null,
                               var timestamp: String? = null) : Visitable<ApplinkDebuggerTypeFactory>, Parcelable {

    override fun type(typeFactory: ApplinkDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }

}
