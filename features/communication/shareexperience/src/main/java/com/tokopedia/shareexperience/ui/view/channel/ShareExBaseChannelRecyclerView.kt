package com.tokopedia.shareexperience.ui.view.channel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shareexperience.ui.adapter.channel.ShareExBaseChannelAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExHorizontalSpacingItemDecoration
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener

abstract class ShareExBaseChannelRecyclerView: RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val horizontalSpacingItemDecoration = ShareExHorizontalSpacingItemDecoration(
        8.dpToPx(context.resources.displayMetrics)
    )
    protected var channelAdapter: ShareExBaseChannelAdapter? = null
    protected abstract fun getChannelAdapter(listener: ShareExChannelListener): ShareExBaseChannelAdapter
    fun setChannelListener(listener: ShareExChannelListener) {
        channelAdapter = getChannelAdapter(listener)
        adapter = channelAdapter
    }

    protected open fun init() {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }
}
