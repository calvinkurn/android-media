package com.tokopedia.feedplus.presentation.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kenny.hadisaputra on 17/10/23
 */
internal data class RecyclerViewPositionInfo(
    val position: Int,
    val isAdapterPosition: Boolean
)
internal fun RecyclerView.getChildValidPositionInfo(
    child: View,
    state: RecyclerView.State,
): RecyclerViewPositionInfo {
//    val adapterPosition = getChildAdapterPosition(child)
//    return if (adapterPosition == RecyclerView.NO_POSITION) {
//        RecyclerViewPositionInfo(getChildLayoutPosition(child), isAdapterPosition = false)
//    } else {
//        RecyclerViewPositionInfo(adapterPosition, isAdapterPosition = true)
//    }
    return RecyclerViewPositionInfo(getChildLayoutPosition(child), isAdapterPosition = false)
}

internal fun RecyclerView.findViewHolderByPositionInfo(
    info: RecyclerViewPositionInfo
): RecyclerView.ViewHolder? {
    return if (info.isAdapterPosition) {
        findViewHolderForAdapterPosition(info.position)
    } else {
        findViewHolderForLayoutPosition(info.position)
    }
}
