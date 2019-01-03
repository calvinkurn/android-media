package com.tokopedia.topchat.revamp.view

import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableViewModel
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactory
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.revamp.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.revamp.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.revamp.view.adapter.TopChatTypeFactoryImpl
import com.tokopedia.topchat.revamp.view.listener.SendButtonListener
import rx.functions.Action1
import java.util.concurrent.TimeUnit
/**
 * @author : Steven 29/11/18
 */

class TopChatViewStateImpl(
        @NonNull override val view: View,
        presenter: TopChatRoomPresenter,
        private val imageAnnouncementListener: ImageAnnouncementListener,
        private val chatLinkHandlerListener: ChatLinkHandlerListener,
        private val imageUploadListener: ImageUploadListener,
        private val productAttachmentListener: ProductAttachmentListener,
        private val typingListener: TypingListener,
        private val sendListener: SendButtonListener,
        private val templateListener: ChatRoomContract.View.TemplateChatListener,
        toolbar: Toolbar
) : BaseChatViewStateImpl(view, toolbar), TopChatViewState {


    private lateinit var adapter: TopChatRoomAdapter
    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
    lateinit var templateAdapter: TemplateChatAdapter
    lateinit var templateChatTypeFactory: TemplateChatTypeFactory

    init {
        initView()
    }

    override fun initView() {
        super.initView()
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }
        replyIsTyping.subscribe(Action1 {
            if (it) {
                minimizeTools()
                typingListener.onStartTyping()
            }
        })

        replyIsTyping.debounce(2, TimeUnit.SECONDS)
                .subscribe {
                    typingListener.onStopTyping()
                }

        maximize.setOnClickListener { maximizeTools() }

        sendButton.setOnClickListener {
            sendListener.onSendClicked(replyEditText.text.toString(),
                    SendableViewModel.generateStartTime())
        }

        adapter = TopChatRoomAdapter(TopChatTypeFactoryImpl(imageAnnouncementListener
                , chatLinkHandlerListener, imageUploadListener, productAttachmentListener), ArrayList())
        setAdapter(adapter)

        templateAdapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(templateListener))
        templateRecyclerView.setHasFixedSize(true)
        templateRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        templateRecyclerView.adapter = templateAdapter
        templateRecyclerView.visibility = View.GONE

//        pickerButton.setOnClickListener {
//            contractView.pickImageToUpload()
//        }
    }

    private fun minimizeTools() {
        maximize.visibility = View.VISIBLE
        pickerButton.visibility = View.GONE
        attachButton.visibility = View.GONE
    }

    private fun maximizeTools() {
        maximize.visibility = View.GONE
        pickerButton.visibility = View.VISIBLE
        attachButton.visibility = View.VISIBLE
    }

    private fun scrollDownWhenInBottom() {
        if (checkLastCompletelyVisibleItemIsFirst()) {
            scrollToBottom()
        }
    }

    private fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
    }

    fun developmentView() {
        actionBox?.visibility = View.VISIBLE
    }

    fun setDefault() {
        sendButton.requestFocus()
    }

    fun setNonReplyable() {
        actionBox?.visibility = View.GONE

    }

    fun setReplyable() {
        actionBox?.visibility = View.VISIBLE
    }

    override fun getAdapter(): TopChatRoomAdapter {
        return super.getAdapter() as TopChatRoomAdapter
    }

    fun removeDummy(visitable: Visitable<*>) {
        getAdapter().removeDummy(visitable)
    }

    fun addMessage(visitable: Visitable<*>) {
        getAdapter().addNewMessage(visitable)
        scrollDownWhenInBottom()
    }

    fun setActionable(actionable: Boolean) {
        val count = actionBox.childCount
        for (i in 0 until count) {
            actionBox.getChildAt(i).isEnabled = actionable

        }
    }

    fun onSuccessLoadFirstTime(viewModel: ChatroomViewModel) {
        hideLoading()
        getAdapter().addList(viewModel.listChat)
        scrollToBottom()
        updateHeader(viewModel)
        showReplyBox()
        showActionButtons()
        checkShowQuickReply(viewModel)
    }

    private fun showActionButtons() {
        pickerButton.visibility = View.VISIBLE
        attachProductButton.visibility = View.VISIBLE
        maximizeButton.visibility = View.GONE
    }


    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
//        if (chatroomViewModel.listChat.isNotEmpty()
//                && chatroomViewModel.listChat[0] is QuickReplyListViewModel) {
//            showQuickReply(chatroomViewModel.listChat[0] as QuickReplyListViewModel)
//        }
    }

    fun setTemplate(listTemplate: List<Visitable<Any>>?) {
        if(listTemplate == null){
            templateRecyclerView.visibility = View.GONE
        }
        templateAdapter.list = listTemplate
        templateRecyclerView.visibility = View.VISIBLE
    }

    fun addTemplateString(message: String) {
        val text = replyEditText.getText().toString()
        val index = replyEditText.getSelectionStart()
        replyEditText.setText(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index)))
        replyEditText.setSelection(message.length + text.substring(0, index).length + 1)
    }
}

