package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder

class ProductCarouselRecyclerView : RecyclerView {

    private val lm = ProductListLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setHasFixedSize(true)
        layoutManager = lm
        PagerSnapHelper().attachToRecyclerView(this)
    }

    fun saveProductCarouselState(
            position: Int,
            listener: ProductCarouselListAttachmentViewHolder.Listener
    ) {
        listener.saveProductCarouselState(position, lm.onSaveInstanceState())
    }

    fun restoreSavedCarouselState(
            adapterPosition: Int,
            listener: ProductCarouselListAttachmentViewHolder.Listener
    ) {
        listener.getSavedCarouselState(adapterPosition)?.let {
            lm.onRestoreInstanceState(it)
        }
    }
}