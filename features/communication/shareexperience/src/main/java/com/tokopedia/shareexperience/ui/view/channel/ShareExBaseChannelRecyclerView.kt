package com.tokopedia.shareexperience.ui.view.channel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shareexperience.ui.adapter.channel.ShareExBaseChannelAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExHorizontalSpacingItemDecoration

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
    protected abstract fun getChannelAdapter(): ShareExBaseChannelAdapter

    protected open fun init() {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        adapter = getChannelAdapter()
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }
}
