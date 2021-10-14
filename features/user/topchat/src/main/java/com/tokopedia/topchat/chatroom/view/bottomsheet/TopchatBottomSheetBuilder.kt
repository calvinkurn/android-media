package com.tokopedia.topchat.chatroom.view.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.chat_common.data.BaseChatUiModel
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
        msg: BaseChatUiModel,
        onClick: (Int, BaseChatUiModel) -> Unit
    ): BottomSheetUnify {
        val longClickMenu = LongClickMenu()
        val title = ctx?.getString(R.string.title_topchat_bubble_long_click_menu) ?: ""
        return longClickMenu.apply {
            setTitle(title)
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
        val copyMenu = TopchatItemMenu(
            _title = ctx?.getString(R.string.title_topchat_copy) ?: "",
            _icon = com.tokopedia.iconunify.R.drawable.iconunify_copy,
            _id = MENU_ID_COPY_TO_CLIPBOARD
        )
        return menus.apply {
            add(replyMenu)
            add(copyMenu)
        }
    }

    const val MENU_ID_REPLY = 1
    const val MENU_ID_COPY_TO_CLIPBOARD = 2
}