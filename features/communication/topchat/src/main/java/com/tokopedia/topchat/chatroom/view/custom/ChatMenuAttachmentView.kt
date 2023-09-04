package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.adapter.AttachmentMenuAdapter
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.config.GlobalConfig

class ChatMenuAttachmentView : RecyclerView {

    companion object {
        private const val SPAN_COUNT = 4
    }

    private val manager = GridLayoutManager(context, SPAN_COUNT)

    private val adapter = AttachmentMenuAdapter()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    )

    init {
        setHasFixedSize(true)
        layoutManager = manager
        setAdapter(adapter)
    }

    fun setAttachmentMenuListener(listener: AttachmentMenu.AttachmentMenuListener) {
        adapter.attachmentMenuListener = listener
    }

    fun setAttachmentMenuViewHolderListener(listener: AttachmentItemViewHolder.AttachmentViewHolderListener) {
        adapter.viewHolderListener = listener
    }

    fun addVoucherAttachmentMenu() {
        if (!adapter.alreadyHasAttachVoucherMenu() && GlobalConfig.isSellerApp()) {
            adapter.addVoucherAttachmentMenu()
        }
    }
}
