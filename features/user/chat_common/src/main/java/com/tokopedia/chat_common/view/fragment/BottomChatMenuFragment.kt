package com.tokopedia.chat_common.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.adapter.ChatMenuAdapter
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory
import java.lang.IllegalStateException

class BottomChatMenuFragment : BottomSheetDialogFragment() {

    private lateinit var rvChatMenu: RecyclerView
    private lateinit var chatMenuListener: BaseChatMenuViewHolder.ChatMenuListener
    private lateinit var chatMenuFactory: ChatMenuFactory

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is BaseChatMenuViewHolder.ChatMenuListener) {
            chatMenuListener = parentFragment
        } else {
            val errorMessage = "The parent fragment must implement the ChatMenuListener interface"
            throw IllegalStateException(errorMessage)
        }

        initChatMenuFactory()
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

    private fun initChatMenuFactory() {
        chatMenuFactory = chatMenuListener.createChatMenuFactory()
    }

    private fun initChatMenu() {
        with(rvChatMenu) {
            setHasFixedSize(true)
            adapter = ChatMenuAdapter(chatMenuFactory, chatMenuListener)
        }
    }

    companion object {
        val TAG = "rounded_bottom_chat_menu"
    }


}