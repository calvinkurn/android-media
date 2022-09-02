package com.tokopedia.topchat.chatroom.view.custom.product_bundling

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder

class ProductBundlingRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    init {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
    }

    fun saveProductCarouselState(
        position: Int,
        listener: ProductBundlingCarouselViewHolder.Listener
    ) {
        listener.saveProductBundlingCarouselState(position, linearLayoutManager.onSaveInstanceState())
    }

    fun restoreSavedCarouselState(
        adapterPosition: Int,
        listener: ProductBundlingCarouselViewHolder.Listener
    ) {
        listener.getProductBundlingCarouselState(adapterPosition)?.let {
            linearLayoutManager.onRestoreInstanceState(it)
        }
    }
}