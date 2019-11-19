@file:JvmName("Helper")
package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

internal inline fun <reified T : Parcelable> createParcel(
    crossinline parcelCreator: (Parcel) -> T?
): Parcelable.Creator<T> = object : Parcelable.Creator<T> {
    override fun createFromParcel(source: Parcel): T? = parcelCreator(source)
    override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
}
