package com.tokopedia.topchat.chatroom.view.custom.voucher

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomVoucherCarouselAdapter
import com.tokopedia.topchat.chatroom.view.adapter.decoration.TopChatRoomHorizontalSpacingItemDecoration
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomVoucherUiModel

class TopChatRoomVoucherCarouselRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val horizontalSpacingItemDecoration = TopChatRoomHorizontalSpacingItemDecoration(
        8.dpToPx(context.resources.displayMetrics)
    )
    private var voucherAdapter: TopChatRoomVoucherCarouselAdapter? = null

    init {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }

    fun initData(newList: List<TopChatRoomVoucherUiModel>) {
        voucherAdapter?.updateData(newList)
        scrollToPosition(0)
    }

    fun setVoucherListener(listener: TopChatRoomVoucherListener) {
        voucherAdapter = TopChatRoomVoucherCarouselAdapter(listener)
        adapter = voucherAdapter
    }
}
