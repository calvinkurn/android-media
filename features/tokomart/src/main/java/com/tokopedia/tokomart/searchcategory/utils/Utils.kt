package com.tokopedia.tokomart.searchcategory.utils

import android.os.Parcel
import android.os.Parcelable

internal fun <T: Parcelable> T.copyParcelable(): T? {
    var parcel: Parcel? = null

    return try {
        parcel = Parcel.obtain()
        parcel.writeParcelable(this, 0)
        parcel.setDataPosition(0)
        parcel.readParcelable(this::class.java.classLoader)
    } catch(throwable: Throwable) {
        null
    } finally {
        parcel?.recycle()
    }
}