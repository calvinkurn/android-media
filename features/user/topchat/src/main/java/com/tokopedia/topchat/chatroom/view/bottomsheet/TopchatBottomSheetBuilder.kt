package com.tokopedia.topchat.chatroom.view.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.widget.LongClickMenu
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

object TopchatBottomSheetBuilder {

    fun getErrorUploadImageBs(
            context: Context,
            onRetryClicked: () -> Unit,
            onDeleteClicked: () -> Unit
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(
                context, R.layout.bs_topchat_upload_image_error_action, null
        )
        val retryBtn = view.findViewById<Typography>(R.id.tp_topchat_retry_send_img)
        val deleteBtn = view.findViewById<Typography>(R.id.tp_topchat_delete_image)
        retryBtn?.setOnClickListener {
            onRetryClicked()
            bottomSheetUnify.dismiss()
        }
        deleteBtn?.setOnClickListener {
            onDeleteClicked()
            bottomSheetUnify.dismiss()
        }
        bottomSheetUnify.apply {
            showCloseIcon = false
            showHeader = false
            setChild(view)
        }
        return bottomSheetUnify
    }

    fun getLongClickBubbleMenuBs(
        ctx: Context?,
        msg: BaseChatViewModel,
        onClick: (Int, BaseChatViewModel) -> Unit
    ): BottomSheetUnify {
        val longClickMenu = LongClickMenu()
        return longClickMenu.apply {
            setItemMenuList(createBubbleLongClickMenu(ctx))
            setOnItemMenuClickListener { itemMenus, _ ->
                onClick(itemMenus.id, msg)
                dismiss()
            }
        }
    }

    private fun createBubbleLongClickMenu(
        ctx: Context?
    ): MutableList<TopchatItemMenu> {
        val menus = arrayListOf<TopchatItemMenu>()
        val replyMenu = TopchatItemMenu(
            _title = ctx?.getString(R.string.title_topchat_reply) ?: "",
            _icon = R.drawable.ic_topchat_reply_reference,
            _id = MENU_ID_REPLY
        )
        return menus.apply {
            add(replyMenu)
        }
    }

    const val MENU_ID_REPLY = 1
}