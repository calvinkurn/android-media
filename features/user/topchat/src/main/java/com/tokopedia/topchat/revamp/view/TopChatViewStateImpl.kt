package com.tokopedia.topchat.revamp.view

import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableViewModel
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactory
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.revamp.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.revamp.view.listener.ImagePickerListener
import com.tokopedia.topchat.revamp.view.listener.SendButtonListener
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */

class TopChatViewStateImpl(
        @NonNull override val view: View,
        typingListener: TypingListener,
        private val sendListener: SendButtonListener,
        private val templateListener: ChatRoomContract.View.TemplateChatListener,
        private val imagePickerListener: ImagePickerListener,
        toolbar: Toolbar
) : BaseChatViewStateImpl(view, toolbar, typingListener), TopChatViewState {



    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
    lateinit var templateAdapter: TemplateChatAdapter
    lateinit var templateChatTypeFactory: TemplateChatTypeFactory
    var isUploading: Boolean = false
    var isFirstTime: Boolean = true

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

        maximize.setOnClickListener { maximizeTools() }

        sendButton.setOnClickListener {
            sendListener.onSendClicked(replyEditText.text.toString(),
                    SendableViewModel.generateStartTime())
        }

        templateAdapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(templateListener))
        templateRecyclerView.setHasFixedSize(true)
        templateRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        templateRecyclerView.adapter = templateAdapter
        templateRecyclerView.visibility = View.GONE

        pickerButton.setOnClickListener {
            imagePickerListener.pickImageToUpload()
        }
    }

    fun minimizeTools() {
        maximize.visibility = View.VISIBLE
        pickerButton.visibility = View.GONE
        attachButton.visibility = View.GONE
    }

    private fun maximizeTools() {
        maximize.visibility = View.GONE
        pickerButton.visibility = View.VISIBLE
        attachButton.visibility = View.VISIBLE
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
    }

    fun setActionable(actionable: Boolean) {
        val count = actionBox.childCount
        for (i in 0 until count) {
            actionBox.getChildAt(i).isEnabled = actionable

        }
    }

    fun onSuccessLoadFirstTime(viewModel: ChatroomViewModel) {
        hideLoading()
        scrollToBottom()
        updateHeader(viewModel)
        showReplyBox(viewModel.replyable)
        showActionButtons()
        checkShowQuickReply(viewModel)
    }

    private fun showActionButtons() {
        pickerButton.visibility = View.VISIBLE
        attachProductButton.visibility = View.VISIBLE
        maximizeButton.visibility = View.GONE
    }

    override fun showErrorWebSocket(b: Boolean) {
        notifier.visibility = View.VISIBLE
        val title = notifier.findViewById<TextView>(R.id.title)
        val action = notifier.findViewById<View>(R.id.action)
        if(b) {
            title.setText(R.string.error_no_connection_retrying);
            action.visibility = View.VISIBLE

        } else {
            if(isFirstTime) {
                isFirstTime = false
                notifier.visibility = View.GONE
                return
            }
            title.setText(R.string.connected_websocket);
            action.visibility = View.GONE
            Observable.timer(1500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        notifier.visibility = View.GONE
                    }

        }
    }

    private fun checkShowQuickReply(chatroomViewModel: ChatroomViewModel) {
//        if (chatroomViewModel.listChat.isNotEmpty()
//                && chatroomViewModel.listChat[0] is QuickReplyListViewModel) {
//            showQuickReply(chatroomViewModel.listChat[0] as QuickReplyListViewModel)
//        }
    }

    fun setTemplate(listTemplate: List<Visitable<Any>>?) {
        templateRecyclerView.visibility = View.GONE
        listTemplate?.let {
            templateAdapter.list = listTemplate
            templateRecyclerView.visibility = View.VISIBLE
        }
    }

    fun addTemplateString(message: String) {
        val text = replyEditText.getText().toString()
        val index = replyEditText.getSelectionStart()
        replyEditText.setText(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index)))
        replyEditText.setSelection(message.length + text.substring(0, index).length + 1)
    }

    override fun showRetryUploadImages(it: ImageUploadViewModel, retry: Boolean) {
        getAdapter().showRetryFor(it, retry)
    }
}

