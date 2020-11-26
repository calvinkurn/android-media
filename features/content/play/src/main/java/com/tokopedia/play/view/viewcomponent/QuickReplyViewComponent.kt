package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.ui.quickreply.adapter.QuickReplyAdapter
import com.tokopedia.play.ui.quickreply.itemdecoration.QuickReplyItemDecoration
import com.tokopedia.play.view.uimodel.QuickReplyUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class QuickReplyViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        listener: Listener
) : ViewComponent(container, idRes) {

    private val rvQuickReply: RecyclerView = rootView as RecyclerView

    private val quickReplyAdapter = QuickReplyAdapter { listener.onQuickReplyClicked(this, it) }

    init {
        rvQuickReply.apply {
            layoutManager = LinearLayoutManager(rvQuickReply.context, RecyclerView.HORIZONTAL, false)
            adapter = quickReplyAdapter
            addItemDecoration(QuickReplyItemDecoration(rvQuickReply.context))
        }
    }

    fun setQuickReply(quickReply: QuickReplyUiModel) {
        quickReplyAdapter.setQuickReply(quickReply.quickReplyList)
    }

    interface Listener {

        fun onQuickReplyClicked(view: QuickReplyViewComponent, replyString: String)
    }
}