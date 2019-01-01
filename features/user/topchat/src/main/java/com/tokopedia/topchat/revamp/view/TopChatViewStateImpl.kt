package com.tokopedia.topchat.revamp.view

import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.BaseChatViewStateImpl
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.revamp.presenter.TopChatRoomPresenter
import rx.functions.Action1
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */

class TopChatViewStateImpl(
        @NonNull override val view: View,
        presenter: TopChatRoomPresenter,
        private val typingListener: TypingListener,
        toolbar: Toolbar
) : BaseChatViewStateImpl(view, toolbar), TopChatViewState{


    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var templateRecyclerView: RecyclerView = view.findViewById(R.id.list_template)
//    lateinit var templateAdapter: TemplateChatAdapter
//    lateinit var templateChatTypeFactory: TemplateChatTypeFactory

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
//        replyWatcher = EventsWatcher.text(replyEditText)
//
//        replyIsTyping = replyWatcher.map(Func1 { t -> t.length > 0 })

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
        //TODO ADD MESSAGE ID & OPPONENT ID
        sendButton.setOnClickListener {
//            presenter.sendMessage("",
//                    replyEditText.text.toString(),
//                    SendableViewModel.generateStartTime(),
//                    "")
        }
//
//        templateAdapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(this))
//        templateRecyclerView.setHasFixedSize(true)
//        templateRecyclerView.layoutManager = LinearLayoutManager(contractView.context, LinearLayoutManager.HORIZONTAL, false)
//        templateRecyclerView.adapter = templateAdapter
//        templateRecyclerView.visibility = View.GONE
//
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

    fun recipientTyping() {
        getAdapter().showTyping()
        scrollDownWhenInBottom()
    }

    fun recipientStopTyping() {
        getAdapter().removeTyping()
    }

    fun removeDummy(visitable: Visitable<*>) {
        getAdapter().removeDummy(visitable)
    }

    fun addMessage(visitable: Visitable<*>) {
        getAdapter().addElement(visitable)
        scrollDownWhenInBottom()
    }

    fun setActionable(actionable: Boolean) {
        val count = actionBox.childCount
        for (i in 0 until count) {
            actionBox.getChildAt(i).isEnabled = actionable

        }
    }

}