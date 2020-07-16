package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.TopAdsDebuggerTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopAdsDebuggerViewModel(var id: Long = 0,
                                   var url: String? = null,
                                   var previewUrl: String? = null,
                                   var eventType: String? = null,
                                   var sourceName: String? = null,
                                   var productId: String? = null,
                                   var productName: String? = null,
                                   var imageUrl: String? = null,
                                   var eventStatus: String? = null,
                                   var timestamp: String? = null,
                                   var fullResponse: String? = "") : Visitable<TopAdsDebuggerTypeFactory>, Parcelable {

    override fun type(typeFactory: TopAdsDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }
}
