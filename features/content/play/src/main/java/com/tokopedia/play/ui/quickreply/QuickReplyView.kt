package com.tokopedia.play.ui.quickreply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.data.QuickReply
import com.tokopedia.play.ui.quickreply.adapter.QuickReplyAdapter
import com.tokopedia.play.ui.quickreply.itemdecoration.QuickReplyItemDecoration

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_quick_reply, container, true)
                    .findViewById(R.id.rv_quick_reply)

    private val rvQuickReply: RecyclerView = view as RecyclerView

    private val quickReplyAdapter = QuickReplyAdapter { listener.onQuickReplyClicked(this, it) }

    init {
        rvQuickReply.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            adapter = quickReplyAdapter
            addItemDecoration(QuickReplyItemDecoration(view.context))
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setQuickReply(quickReply: QuickReply) {
        quickReplyAdapter.setQuickReply(quickReply)
    }

    interface Listener {

        fun onQuickReplyClicked(view: QuickReplyView, replyString: String)
    }
}