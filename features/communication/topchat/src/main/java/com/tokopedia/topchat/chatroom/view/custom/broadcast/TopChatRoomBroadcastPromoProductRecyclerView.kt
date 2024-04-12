package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomBroadcastProductCarouselAdapter
import com.tokopedia.topchat.chatroom.view.adapter.decoration.TopChatRoomHorizontalSpacingItemDecoration
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomBroadcastPromoTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel

class TopChatRoomBroadcastPromoProductRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val horizontalSpacingItemDecoration = TopChatRoomHorizontalSpacingItemDecoration(
        4.dpToPx(context.resources.displayMetrics)
    )
    private var promoProductAdapter: TopChatRoomBroadcastProductCarouselAdapter? = null

    init {
        setHasFixedSize(true)
        layoutManager = linearLayoutManager
        isNestedScrollingEnabled = false
        itemAnimator = null
        addItemDecoration(horizontalSpacingItemDecoration)
    }

    fun updateData(newList: List<Visitable<*>>) {
        promoProductAdapter?.updateData(newList)
    }

    fun setListener(
        productListener: TopChatRoomBroadcastProductListener,
        broadcastUiModel: TopChatRoomBroadcastUiModel
    ) {
        promoProductAdapter = TopChatRoomBroadcastProductCarouselAdapter(
            TopChatRoomBroadcastPromoTypeFactoryImpl(broadcastUiModel, productListener)
        )
        adapter = promoProductAdapter
    }
}
