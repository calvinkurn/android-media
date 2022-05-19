package com.tokopedia.play.view.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
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

    private val path = Path()
    private var maskHeight: Float? = null

    private var maskAnimator: ValueAnimator? = null

    init {
        val view = View.inflate(context, R.layout.view_chat_list, this)

        rvChatList = view.findViewById(R.id.rv_chat_list)
        csDownView = view.findViewById(R.id.csdown_view)

        adapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                csDownView.showIndicatorRed(rvChatList.canScrollDown)
                if (!csDownView.isVisible || chatAdapter.getItem(chatAdapter.lastIndex).isSelfMessage) {
                    scrollToLatest()
                }
            }
        }

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollDown && recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                    else csDownView.hide()
                } else if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_SETTLING) {
                    csDownView.apply { showIndicatorRed(false) }.hide()
                } else if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if (!recyclerView.canScrollDown) csDownView.hide()
                    else {
                        if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                        else csDownView.hide()
                    }
                }
            }
        }

        setupView(view)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            chatAdapter.registerAdapterDataObserver(adapterObserver)
        } catch (ignored: java.lang.IllegalStateException) {}
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            chatAdapter.unregisterAdapterDataObserver(adapterObserver)
        } catch (ignored: IllegalStateException) {}
    }

    fun showNewChat(chat: PlayChatUiModel) {
        chatAdapter.addChat(chat)
    }

    fun setChatList(chatList: List<PlayChatUiModel>) {
        chatAdapter.setChatList(chatList)
    }

    fun setTopMask(height: Float, animate: Boolean) {
        maskAnimator?.cancel()

        val valueAnimator = ValueAnimator.ofFloat(maskHeight.orZero(), height)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            maskHeight = value
            postInvalidateOnAnimation()
        }
        valueAnimator.duration = if (animate) MASK_DURATION_IN_MS else 0
        valueAnimator.start()

        maskAnimator = valueAnimator
    }

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        val maskHeight = this.maskHeight
        if (maskHeight != null) {
            path.apply {
                reset()
                addRect(MASK_START_POS, height.toFloat(), width.toFloat(), maskHeight, Path.Direction.CW)
            }
            canvas.clipPath(path)
        }
        return super.drawChild(canvas, child, drawingTime)
    }

    private fun setupView(view: View) {
        rvChatList.apply {
            val layoutMan = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutMan.stackFromEnd = true
            layoutManager = layoutMan
            itemAnimator = null

            adapter = chatAdapter
            addOnScrollListener(scrollListener)
            addItemDecoration(ChatListItemDecoration(context))
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
}