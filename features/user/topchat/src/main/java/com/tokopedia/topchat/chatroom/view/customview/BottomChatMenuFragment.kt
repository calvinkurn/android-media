package com.tokopedia.topchat.chatroom.view.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ChatMenuAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.ChatMenuFactory
import java.lang.IllegalStateException

class BottomChatMenuFragment : BottomSheetDialogFragment() {

    private lateinit var rvChatMenu: RecyclerView
    private lateinit var chatMenuListener: BaseChatMenuViewHolder.ChatMenuListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is BaseChatMenuViewHolder.ChatMenuListener) {
            chatMenuListener = parentFragment
        } else {
            throw IllegalStateException("The parent fragment must implement the ChatMenuListener interface")
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_menu, container, false).also { view ->
            rvChatMenu = view.findViewById(R.id.rvChatMenu)

            initChatMenu()
        }
    }

    private fun initChatMenu() {
        val menuItems = ChatMenuFactory.createChatMenuItems()
        with(rvChatMenu) {
            setHasFixedSize(true)
            adapter = ChatMenuAdapter(menuItems, chatMenuListener)
        }
    }

    companion object {
        val TAG = "rounded_bottom_chat_menu"
    }
}