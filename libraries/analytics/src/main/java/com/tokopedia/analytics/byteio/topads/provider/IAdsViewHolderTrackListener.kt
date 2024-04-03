package com.tokopedia.analytics.byteio.topads.provider

import androidx.recyclerview.widget.RecyclerView

interface IAdsViewHolderTrackListener {
    fun onViewAttachedToWindow(recyclerView: RecyclerView?)

    fun onViewDetachedToWindow(recyclerView: RecyclerView?)
}
