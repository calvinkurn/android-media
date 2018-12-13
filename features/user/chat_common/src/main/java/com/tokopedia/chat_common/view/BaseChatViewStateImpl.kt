package com.tokopedia.chat_common.view

import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.EventsWatcher
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */
open class BaseChatViewStateImpl(@NonNull open val view: View) : BaseChatViewState {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var mainLoading: ProgressBar
    protected lateinit var replyEditText: EditText
    protected lateinit var replyBox: RelativeLayout
    protected lateinit var actionBox: LinearLayout
    protected lateinit var sendButton: View
    protected lateinit var notifier: View

    protected lateinit var replyWatcher: Observable<String>
    protected lateinit var replyIsTyping: Observable<Boolean>

    override fun initView() {
        recyclerView = view.findViewById(R.id.recycler_view)
        mainLoading = view.findViewById(R.id.progress)
        replyEditText = view.findViewById(R.id.new_comment)
        replyBox = view.findViewById(R.id.reply_box)
        actionBox = view.findViewById(R.id.add_comment_area)
        sendButton = view.findViewById(R.id.send_but)
        notifier = view.findViewById(R.id.notifier)

        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }
        replyWatcher = EventsWatcher.text(replyEditText)

        replyIsTyping = replyWatcher.map { t -> t.isNotEmpty() }
    }

    private fun scrollDownWhenInBottom() {
        if (checkLastCompletelyVisibleItemIsFirst()) {
            scrollToBottom()
        }
    }

    protected fun scrollToBottom() {
        Observable.timer(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recyclerView.scrollToPosition(0)
                }
    }

    private fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
    }

    fun showLoading() {
        mainLoading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        mainLoading.visibility = View.GONE
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

    override fun onShowStartTyping() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowStopTyping() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun clearEditText() {
        replyEditText.setText("")
    }

    protected fun showReplyBox() {
        //TODO SHOW REPLY BOX
        replyBox.visibility = View.VISIBLE
    }

}