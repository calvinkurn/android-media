package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatMultipleProductBundlingAttachmentBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundlingCarouselViewHolder constructor(
    itemView: View?,
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener
) : BaseChatViewHolder<MultipleProductBundlingUiModel>(itemView) {

    private val binding: ItemTopchatMultipleProductBundlingAttachmentBinding? by viewBinding()
    private val multipleProductBundlingAdapter =
        MultipleProductBundlingAdapter(listener, adapterListener)

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.rvProductBundle?.apply {
            setHasFixedSize(true)
            setRecycledViewPool(adapterListener.getProductCarouselViewPool())
            adapter = multipleProductBundlingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        saveProductCarouselState(viewHolder.adapterPosition, listener)
                    }
                }
            })
        }
    }

    override fun bind(viewModel: MultipleProductBundlingUiModel?) {
        super.bind(viewModel)
        multipleProductBundlingAdapter.carousel = viewModel
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_multiple_product_bundling_attachment
    }
}