package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatMultipleProductBundlingAttachmentBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundlingCarouselViewHolder constructor(
    itemView: View?,
    productBundlingListener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val productBundlingCarouselListener: Listener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    searchListener: SearchListener,
    commonListener: CommonViewHolderListener,
) : BaseChatViewHolder<MultipleProductBundlingUiModel>(itemView) {

    interface Listener {
        fun saveProductBundlingCarouselState(position: Int, state: Parcelable?)
        fun getProductBundlingCarouselState(position: Int): Parcelable?
    }

    private val binding: ItemTopchatMultipleProductBundlingAttachmentBinding? by viewBinding()
    private val multipleProductBundlingAdapter = MultipleProductBundlingAdapter(
            productBundlingListener, adapterListener,
            deferredAttachment, searchListener, commonListener
        )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.rvProductBundleCard?.apply {
            setRecycledViewPool(adapterListener.getProductCarouselViewPool())
            adapter = multipleProductBundlingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(adapterPosition, productBundlingCarouselListener)
                    }
                }
            })
        }
    }

    override fun bind(viewModel: MultipleProductBundlingUiModel) {
        super.bind(viewModel)
        bindSyncProductBundlingDeferred(viewModel)
        multipleProductBundlingAdapter.carousel = viewModel
        binding?.rvProductBundleCard?.restoreSavedCarouselState(adapterPosition, productBundlingCarouselListener)
    }


    private fun bindSyncProductBundlingDeferred(element: MultipleProductBundlingUiModel) {
        if (!element.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[element.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            element.syncError()
        } else {
            element.updateData(attachment.parsedAttributes)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_multiple_product_bundling_attachment
    }
}