package com.tokopedia.discovery.common.microinteraction

import android.os.Parcelable
import android.view.View
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchBarMicroInteractionAttributes(
    val x: Float = 0f,
    val width: Float = 0f,
): Parcelable {

    companion object {

        fun create(searchBarView: View?): SearchBarMicroInteractionAttributes {
            return SearchBarMicroInteractionAttributes(
                x = searchBarView?.x ?: 0f,
                width = searchBarView?.width?.toFloat() ?: 0f
            )
        }
    }
}
