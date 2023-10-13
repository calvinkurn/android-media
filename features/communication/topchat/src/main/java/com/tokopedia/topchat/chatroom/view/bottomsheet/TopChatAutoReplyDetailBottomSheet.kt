package com.tokopedia.topchat.chatroom.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.TopChatChatRoomAutoReplyAdapter
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBottomsheetAutoReplyBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TopChatAutoReplyDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TopchatChatroomBottomsheetAutoReplyBinding>()
    private val adapter = TopChatChatRoomAutoReplyAdapter(isMessageBubble = false)
    private var welcomeMessage = TopChatAutoReplyItemUiModel("", "", "")
    private var list: List<TopChatAutoReplyItemUiModel> = listOf()

    init {
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topchat_chatroom_bottomsheet_auto_reply,
            container,
            false
        )
        binding = TopchatChatroomBottomsheetAutoReplyBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        this.setTitle(getString(R.string.topchat_message_auto_reply_bs_title))
        binding?.topchatChatroomTvWelcomeMessageBs?.text = welcomeMessage.getMessage()
        binding?.topchatChatroomRvAutoReplyBs?.adapter = adapter
        binding?.topchatChatroomRvAutoReplyBs?.layoutManager = LinearLayoutManager(context)
        binding?.topchatChatroomRvAutoReplyBs?.itemAnimator = null
        adapter.updateItem(list)
    }

    fun show(
        fragmentManager: FragmentManager?,
        welcomeMessage: TopChatAutoReplyItemUiModel,
        list: List<TopChatAutoReplyItemUiModel>
    ) {
        this.welcomeMessage = welcomeMessage
        this.list = list
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    companion object {
        private val TAG = TopChatAutoReplyDetailBottomSheet::class.simpleName
    }
}
