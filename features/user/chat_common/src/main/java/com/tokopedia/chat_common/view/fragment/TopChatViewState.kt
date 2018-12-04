package com.tokopedia.chat_common.view.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.tokopedia.chat_common.R

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

    fun setDefault(){
        sendButton.requestFocus()
    }

    fun setNonReplyable() {
        actionBox!!.visibility = View.GONE
    }

    fun setReplyable() {
        actionBox!!.visibility = View.VISIBLE
    }

    fun showLoading() {
        mainLoading!!.visibility = View.VISIBLE
    }

    fun hideLoading() {
        mainLoading!!.visibility = View.GONE
    }


}