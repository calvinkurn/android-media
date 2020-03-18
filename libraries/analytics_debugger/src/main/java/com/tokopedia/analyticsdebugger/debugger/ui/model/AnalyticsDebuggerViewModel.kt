package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.AnalyticsDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author okasurya on 5/16/18.
 */
@Parcelize
data class AnalyticsDebuggerViewModel(var id: Long = 0, var name: String? = null,
                                 var category: String? = null, var data: String? = null,
                                 var dataExcerpt: String? = null,
                                 var timestamp: String? = null) : Visitable<AnalyticsDebuggerTypeFactory>, Parcelable {

    override fun type(typeFactory: AnalyticsDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }
}
