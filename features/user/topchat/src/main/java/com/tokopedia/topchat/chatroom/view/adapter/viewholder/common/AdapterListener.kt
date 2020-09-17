package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import android.content.Context

interface AdapterListener {
    fun isNextItemSender(adapterPosition: Int, isSender: Boolean): Boolean
}

fun getOppositeMargin(context: Context?): Float {
    return context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
            ?: 0f
}