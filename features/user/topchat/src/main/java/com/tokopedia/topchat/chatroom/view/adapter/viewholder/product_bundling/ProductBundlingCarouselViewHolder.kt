package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatMultipleProductBundlingAttachmentBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundlingCarouselViewHolder constructor(
    itemView: View?,
    productBundlingListener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val productBundlingCarouselListener: Listener,
    searchListener: SearchListener,
    commonListener: CommonViewHolderListener,
    private val deferredAttachment: DeferredViewHolderAttachment
) : BaseChatViewHolder<MultipleProductBundlingUiModel>(itemView) {

    interface Listener {
        fun saveProductBundlingCarouselState(position: Int, state: Parcelable?)
        fun getProductBundlingCarouselState(position: Int): Parcelable?
    }

    private val binding: ItemTopchatMultipleProductBundlingAttachmentBinding? by viewBinding()
    private val multipleProductBundlingAdapter = MultipleProductBundlingAdapter(
            productBundlingListener, adapterListener,
            searchListener, commonListener, deferredAttachment
        )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.rvProductBundleCard?.apply {
            setRecycledViewPool(adapterListener.getCarouselViewPool())
            adapter = multipleProductBundlingAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(adapterPosition, productBundlingCarouselListener)
                    }
                }
            })
        }
    }

    override fun bind(carouselBundling: MultipleProductBundlingUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> {
                bind(carouselBundling)
            }
            else -> return
        }
    }

    override fun bind(carouselBundling: MultipleProductBundlingUiModel) {
        super.bind(carouselBundling)
        syncCarouselProductBundling(carouselBundling)
        multipleProductBundlingAdapter.carousel = carouselBundling
        binding?.rvProductBundleCard?.restoreSavedCarouselState(adapterPosition, productBundlingCarouselListener)
    }

    /**
     * Update the element manually
     * When this view has not been rendered but the adapter has been updated
     */
    private fun syncCarouselProductBundling(element: MultipleProductBundlingUiModel) {
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