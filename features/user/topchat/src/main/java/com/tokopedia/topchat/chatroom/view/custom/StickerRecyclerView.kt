package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.adapter.StickerAdapter

class StickerRecyclerView : RecyclerView {

    private val manager = GridLayoutManager(context, 4)
    private val adapter = StickerAdapter()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setHasFixedSize(true)
        layoutManager = manager
        setAdapter(adapter)
    }

    fun updateSticker(sticker: List<Sticker>) {
        adapter.stickers = sticker
    }

}