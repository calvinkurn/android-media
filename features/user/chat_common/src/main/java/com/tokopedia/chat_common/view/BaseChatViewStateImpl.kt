package com.tokopedia.chat_common.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */

open class BaseChatViewStateImpl(open var view: View) : BaseChatViewState {

    protected var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    protected var replyBox: View = view.findViewById(R.id.reply_box)
    protected var actionBox: View? = view.findViewById(R.id.add_comment_area)
    protected var mainLoading: ProgressBar? = view.findViewById(R.id.progress)

    protected var sendButton: View = view.findViewById(R.id.send_but)
    protected var replyEditText: EditText = view.findViewById(R.id.new_comment)
    protected var attachButton: ImageView = view.findViewById(R.id.add_url)
    protected var pickerButton: View = view.findViewById(R.id.image_picker)
    protected var maximize: View = view.findViewById(R.id.maximize)
    protected var notifier: View = view.findViewById(R.id.notifier)

    init {
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }
    }

    override fun onSetupViewFirstTime() {
        //hide send box
        showLoading()
    }

    override fun onShowStartTyping() {
        getAdapter().showTyping()
        scrollDownWhenInBottom()
    }

    override fun onShowStopTyping() {
        getAdapter().removeTyping()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        getAdapter().addList(listOf(visitable))
    }

    protected fun scrollDownWhenInBottom() {
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

    protected fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
    }

    fun developmentView() {
        actionBox?.visibility = View.VISIBLE
    }

    protected fun setDefault() {
        sendButton.requestFocus()
    }

    protected fun setNonReplyable() {
        actionBox?.visibility = View.GONE

    }

    protected fun setReplyable() {
        actionBox?.visibility = View.VISIBLE
    }

    protected fun showLoading() {
        mainLoading?.visibility = View.VISIBLE
    }

    protected fun hideLoading() {
        mainLoading?.visibility = View.GONE
    }

    protected fun setAdapter(adapter: BaseChatAdapter) {
        recyclerView.adapter = adapter
    }

    protected fun getAdapter(): BaseChatAdapter {
        return recyclerView.adapter as BaseChatAdapter
    }

    protected fun addList(listChat: ArrayList<Visitable<*>>) {
        getAdapter().addList(listChat)
    }

    protected fun getList(): List<Visitable<*>> {
        return (recyclerView.adapter as BaseChatAdapter).getList()
    }

    protected fun hideExpandButton() {
        maximize.visibility = View.GONE
    }

    protected fun hidePickerButton() {
        pickerButton.visibility = View.GONE
    }

    protected fun hideAttachButton() {
        attachButton.visibility = View.GONE
    }

    protected fun showReplyBox(){
        //TODO SHOW REPLY BOX

    }

}