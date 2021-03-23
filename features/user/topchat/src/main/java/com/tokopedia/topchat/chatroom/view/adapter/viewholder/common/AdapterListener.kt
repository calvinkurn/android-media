package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel

interface AdapterListener {
    fun isOpposite(adapterPosition: Int, isSender: Boolean): Boolean
    fun getProductCarouselViewPool(): RecyclerView.RecycledViewPool
    fun changeToFallbackUiModel(element: ReviewUiModel, lastKnownPosition: Int)
}

fun getOppositeMargin(context: Context?): Float {
    return context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
            ?: 0f
}