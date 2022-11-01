package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ImpressHolder : Parcelable {
    @Expose(serialize = false, deserialize = false)
    var isInvoke = false
        private set


}
