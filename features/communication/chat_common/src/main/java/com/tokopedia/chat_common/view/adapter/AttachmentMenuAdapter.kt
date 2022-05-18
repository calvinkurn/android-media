package com.tokopedia.chat_common.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.VoucherMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder

class AttachmentMenuAdapter(
        var viewHolderListener: AttachmentItemViewHolder.AttachmentViewHolderListener? = null
) : RecyclerView.Adapter<AttachmentItemViewHolder>() {

    private val menus = arrayListOf<AttachmentMenu>()
    var attachmentMenuListener: AttachmentMenu.AttachmentMenuListener? = null
        set(value) {
            value?.let {
                field = value
                val menus = value.createAttachmentMenus()
                this.menus.apply {
                    clear()
                    addAll(menus)
                }
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentItemViewHolder {
        return AttachmentItemViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: AttachmentItemViewHolder, position: Int) {
        holder.bind(menus[position], attachmentMenuListener, viewHolderListener)
    }

    fun alreadyHasAttachVoucherMenu(): Boolean {
        for (menu in menus) {
            if (menu is VoucherMenu) return true
        }
        return false
    }

    fun addVoucherAttachmentMenu() {
        menus.add(VoucherMenu())
        notifyItemInserted(menus.size - 1)
    }

}