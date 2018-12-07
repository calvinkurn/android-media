package com.tokopedia.chat_common.view.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.R
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */

class TopChatViewState(var view: View) {

    private var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private var replyBox: View = view.findViewById(R.id.reply_box)
    private var actionBox: View? = view.findViewById(R.id.add_comment_area)
    private var mainLoading: ProgressBar? = view.findViewById(R.id.progress)

    private var sendButton: View = view.findViewById(R.id.send_but)
    private var replyEditText: EditText = view.findViewById(R.id.new_comment)
    private var attachButton: ImageView = view.findViewById(R.id.add_url)
    private var pickerButton: View = view.findViewById(R.id.image_picker)
    private var maximize: View = view.findViewById(R.id.maximize)
    private var notifier: View = view.findViewById(R.id.notifier)

    init {
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                scrollDownWhenInBottom()
            }
        }
    }

    private fun scrollDownWhenInBottom() {
        if(checkLastCompletelyVisibleItemIsFirst()) {
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

    fun setDefault(){
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

    fun addMessage(visitable: Visitable<*>) {
        getAdapter().addList(listOf(visitable))
    }
}