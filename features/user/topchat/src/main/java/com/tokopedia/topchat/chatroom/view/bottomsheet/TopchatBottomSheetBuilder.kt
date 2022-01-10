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
        menus: List<Int>,
        onClick: (TopchatItemMenu, BaseChatUiModel) -> Unit
    ): BottomSheetUnify {
        val longClickMenu = LongClickMenu()
        val title = ctx?.getString(R.string.title_topchat_bubble_long_click_menu) ?: ""
        return longClickMenu.apply {
            setTitle(title)
            setItemMenuList(createBubbleLongClickMenu(ctx, menus))
            setOnItemMenuClickListener { itemMenus, _ ->
                onClick(itemMenus, msg)
                dismiss()
            }
        }
    }

    private fun createBubbleLongClickMenu(
        ctx: Context?,
        menus: List<Int>
    ): MutableList<TopchatItemMenu> {
        val topchatMenus = arrayListOf<TopchatItemMenu>()
        menus.forEach { menuId ->
            val menu = when (menuId) {
                MENU_ID_REPLY -> TopchatItemMenu(
                    title = ctx?.getString(R.string.title_topchat_reply) ?: "",
                    icon = R.drawable.ic_topchat_reply_reference,
                    id = MENU_ID_REPLY
                )
                MENU_ID_COPY_TO_CLIPBOARD -> TopchatItemMenu(
                    title = ctx?.getString(R.string.title_topchat_copy) ?: "",
                    icon = com.tokopedia.iconunify.R.drawable.iconunify_copy,
                    id = MENU_ID_COPY_TO_CLIPBOARD
                )
                MENU_ID_DELETE_BUBBLE -> TopchatItemMenu(
                    title = ctx?.getString(R.string.title_topchat_delete_msg) ?: "",
                    icon = com.tokopedia.iconunify.R.drawable.iconunify_delete,
                    id = MENU_ID_DELETE_BUBBLE
                )
                else -> null
            }
            menu?.let {
                topchatMenus.add(it)
            }
        }
        return topchatMenus
    }

    const val MENU_ID_REPLY = 1
    const val MENU_ID_COPY_TO_CLIPBOARD = 2
    const val MENU_ID_DELETE_BUBBLE = 3
}