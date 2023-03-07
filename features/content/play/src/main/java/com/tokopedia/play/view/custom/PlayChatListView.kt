package com.tokopedia.play.view.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.chatlist.adapter.ChatAdapter
import com.tokopedia.play.ui.chatlist.itemdecoration.ChatListItemDecoration
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by jegul on 10/08/21
 */
class PlayChatListView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val rvChatList: RecyclerView
    private val csDownView: ChatScrollDownView

    private val adapterObserver : RecyclerView.AdapterDataObserver
    private val scrollListener : RecyclerView.OnScrollListener

    private val RecyclerView.canScrollDown: Boolean
        get() = canScrollVertically(1)

    private val chatAdapter = ChatAdapter()

    private val chatListOnLayoutChangeListener: View.OnLayoutChangeListener

    private val itemDecoration = ChatListItemDecoration(context)

    init {
        val view = View.inflate(context, R.layout.view_chat_list, this)

        rvChatList = view.findViewById(R.id.rv_chat_list)
        csDownView = view.findViewById(R.id.csdown_view)

        chatListOnLayoutChangeListener =
            OnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
                if ((bottom - top) != (oldBottom - oldTop)) scrollToLatest()
            }

        adapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                csDownView.showIndicatorRed(rvChatList.canScrollDown)
                if (!csDownView.isVisible || chatAdapter.currentList.lastOrNull()?.isSelfMessage == true) {
                    scrollToLatest()
                }
            }
        }

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when(recyclerView.scrollState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                        else csDownView.hide()
                    }
                    else -> {
                        if (!recyclerView.canScrollDown) csDownView.hide()
                        else {
                            if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                            else csDownView.hide()
                        }
                    }
                }

                if (!recyclerView.canScrollDown) csDownView.showIndicatorRed(false)
            }
        }

        setupView(view)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            chatAdapter.registerAdapterDataObserver(adapterObserver)

            rvChatList.apply {
                addOnScrollListener(scrollListener)
                addItemDecoration(itemDecoration)
                addOnLayoutChangeListener(chatListOnLayoutChangeListener)
            }
        } catch (ignored: java.lang.IllegalStateException) {}
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            chatAdapter.unregisterAdapterDataObserver(adapterObserver)

            rvChatList.apply {
                removeOnScrollListener(scrollListener)
                removeItemDecoration(itemDecoration)
                removeOnLayoutChangeListener(chatListOnLayoutChangeListener)
            }
        } catch (ignored: IllegalStateException) {}
    }

    fun setMaxHeight(height: Float) {
        if (rvChatList is MaximumHeightRecyclerView) rvChatList.setMaxHeight(height)
    }

    fun setChatList(chatList: List<PlayChatUiModel>) {
        chatAdapter.submitList(chatList)
    }

    private fun setupView(view: View) {
        rvChatList.apply {
            val layoutMan = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(PlayChatItemDecoration(context))
            layoutMan.stackFromEnd = true
            layoutManager = layoutMan
            itemAnimator = null

            adapter = chatAdapter
        }

        csDownView.setOnClickListener {
            if (rvChatList.canScrollDown) scrollToLatest()
        }
    }

    private fun scrollToLatest() {
        try {
            rvChatList.post {
                rvChatList.scrollBy(0, Integer.MAX_VALUE)
            }
        } catch (ignored: Throwable) {}
    }

    private fun isChatPositionBeyondOffset(recyclerView: RecyclerView): Boolean {
        val offset = recyclerView.computeVerticalScrollOffset()
        val range = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
        return offset + SCROLL_OFFSET_INDICATOR < range - 1
    }

    companion object {
        private const val SCROLL_OFFSET_INDICATOR = 90

        private const val MASK_START_POS = 0f
        private const val MASK_DURATION_IN_MS = 300L
    }

    private class PlayChatItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

        private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.dp_6)

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = dividerHeight
        }
    }
}