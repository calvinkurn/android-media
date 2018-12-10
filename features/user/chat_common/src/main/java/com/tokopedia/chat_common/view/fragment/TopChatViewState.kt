package com.tokopedia.chat_common.view.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.EventsWatcher
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */

class TopChatViewState(var view: View, presenter: BaseChatPresenter) {

    private var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private var replyBox: RelativeLayout = view.findViewById(R.id.reply_box)
    private var actionBox: LinearLayout = view.findViewById(R.id.add_comment_area)
    private var mainLoading: ProgressBar = view.findViewById(R.id.progress)

    private var sendButton: View = view.findViewById(R.id.send_but)
    private var replyEditText: EditText = view.findViewById(R.id.new_comment)
    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var pickerButton: View = view.findViewById(R.id.image_picker)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var notifier: View = view.findViewById(R.id.notifier)

    private var replyWatcher: Observable<String>
    private var replyIsTyping: Observable<Boolean>

    init {
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }
        replyWatcher = EventsWatcher.text(replyEditText)

        replyIsTyping = replyWatcher.map(Func1 { t -> t.length > 0 })

        replyIsTyping.subscribe(Action1 {
            if (it) {
                minimizeTools()
            }
        })

        maximize.setOnClickListener { maximizeTools() }
        sendButton.setOnClickListener { presenter.sendMessage(replyEditText.text.toString()) }
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

    private fun scrollToBottom() {
        Observable.timer(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recyclerView.scrollToPosition(0)
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

    fun showLoading() {
        mainLoading?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        mainLoading?.visibility = View.GONE
    }

    fun setAdapter(adapter: BaseChatAdapter) {
        recyclerView.adapter = adapter
    }

    fun getAdapter(): BaseChatAdapter {
        return recyclerView.adapter as BaseChatAdapter
    }

    fun addList(listChat: ArrayList<Visitable<*>>) {
        getAdapter().addList(listChat)
    }

    fun getList(): List<Visitable<*>> {
        return (recyclerView.adapter as BaseChatAdapter).getList()
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
        getAdapter().addNewMessage(visitable)
        scrollDownWhenInBottom()
    }

    fun setActionable(actionable: Boolean) {
        val count = actionBox.childCount
        for (i in 0 until count) {
            actionBox.getChildAt(i).isEnabled = actionable

        }
    }

    fun clearEditText() {
        replyEditText.setText("")
    }
}