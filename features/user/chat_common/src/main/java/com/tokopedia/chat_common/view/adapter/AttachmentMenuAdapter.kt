package com.tokopedia.chat_common.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ImageMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.ProductMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder

class AttachmentMenuAdapter : RecyclerView.Adapter<AttachmentItemViewHolder>() {

    private val menus = createMenus()

    private fun createMenus(): List<AttachmentMenu> {
        return arrayListOf(
                ProductMenu(),
                ImageMenu()
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentItemViewHolder {
        return AttachmentItemViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: AttachmentItemViewHolder, position: Int) {
        holder.bind(menus[position])
    }

}