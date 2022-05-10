package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingPojo
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatMultipleProductBundlingAttachmentBinding
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.Exception

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
        bindDeferredAttachment(carouselBundling)
        multipleProductBundlingAdapter.carousel = carouselBundling
        binding?.rvProductBundleCard?.restoreSavedCarouselState(adapterPosition, productBundlingCarouselListener)
    }

    private fun bindDeferredAttachment(carouselBundling: MultipleProductBundlingUiModel) {
        if (!carouselBundling.isLoading()) return
        val attachments = deferredAttachment.getLoadedChatAttachments()
        for (i in 0 until carouselBundling.listBundling.size) {
            val productBundlingUi = carouselBundling.listBundling[i]
            val attachment = attachments[productBundlingUi.id] ?: return
            if (attachment is ErrorAttachment) {
                productBundlingUi.syncError()
            } else if (attachment.parsedAttributes is ProductBundlingPojo) {
                val productBundlingData = attachment.parsedAttributes as? ProductBundlingPojo
                if (i < productBundlingData?.listProductBundling?.size?: -1) {
                    productBundlingUi.updateData(
                        productBundlingData?.listProductBundling?.get(i)
                    )
                }
            }
        }

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_multiple_product_bundling_attachment
    }
}