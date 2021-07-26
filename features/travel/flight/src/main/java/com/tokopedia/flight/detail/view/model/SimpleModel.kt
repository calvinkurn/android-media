package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by alvarisi on 11/21/17.
 */
@Parcelize
data class SimpleModel(
        var label: String = "",
        var description: String = ""
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return other is SimpleModel && other.label.equals(label, ignoreCase = true)
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

}