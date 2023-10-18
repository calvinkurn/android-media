package com.tokopedia.topchat.chatroom.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAutoReplyAdapter
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatRoomAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBottomsheetAutoReplyBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TopChatRoomAutoReplyDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TopchatChatroomBottomsheetAutoReplyBinding>()
    private val adapter = TopChatRoomAutoReplyAdapter(isMessageBubble = false)
    private var mainMessage = TopChatRoomAutoReplyItemUiModel("", "", "")
    private var list: List<TopChatRoomAutoReplyItemUiModel> = listOf()

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
        binding?.topchatChatroomTvWelcomeMessageBs?.text = mainMessage.getMessage()
        binding?.topchatChatroomRvAutoReplyBs?.adapter = adapter
        binding?.topchatChatroomRvAutoReplyBs?.layoutManager = LinearLayoutManager(context)
        binding?.topchatChatroomRvAutoReplyBs?.itemAnimator = null
        binding?.topchatChatroomRvAutoReplyBs?.isNestedScrollingEnabled = false
        binding?.topchatChatroomRvAutoReplyBs?.setHasFixedSize(true)
        adapter.updateItem(list)
    }

    fun show(
        fragmentManager: FragmentManager?,
        mainMessage: TopChatRoomAutoReplyItemUiModel,
        list: List<TopChatRoomAutoReplyItemUiModel>
    ) {
        this.mainMessage = mainMessage
        this.list = list
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    companion object {
        private val TAG = TopChatRoomAutoReplyDetailBottomSheet::class.simpleName
    }
}
