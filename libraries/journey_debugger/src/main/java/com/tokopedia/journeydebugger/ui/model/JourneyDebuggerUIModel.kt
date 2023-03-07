package com.tokopedia.journeydebugger.ui.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.journeydebugger.ui.adapter.JourneyDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JourneyDebuggerUIModel(var id: Long = 0, var journey: String? = null,
                                  var previewJourney: String? = null,
                                  var timestamp: String? = null) : Visitable<JourneyDebuggerTypeFactory>, Parcelable {

    override fun type(typeFactory: JourneyDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }

}
