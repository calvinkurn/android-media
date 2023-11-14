package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel

const val OPPOSITE_MARGIN = 4

interface AdapterListener {
    fun isOpposite(adapterPosition: Int, isSender: Boolean): Boolean
    fun getCarouselViewPool(): RecyclerView.RecycledViewPool
    fun changeToFallbackUiModel(element: ReviewUiModel, lastKnownPosition: Int)
}

fun getOppositeMargin(context: Context): Int {
    return OPPOSITE_MARGIN.dpToPx(context.resources.displayMetrics)
}
