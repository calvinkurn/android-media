package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatListProductBundlingBinding
import com.tokopedia.utils.view.binding.viewBinding

class MultipleBundlingItemViewHolder(
    itemView: View,
    private val listener: ProductBundlingListener?,
    private val productBundling: ProductBundlingUiModel?
): RecyclerView.ViewHolder(itemView) {

    private var binding: ItemTopchatListProductBundlingBinding? by viewBinding()

    fun bind(data: BundleItem) {
        bindBundlingName(data)
        bindImage(data)
        bindListener(data)
    }

    private fun bindBundlingName(data: BundleItem) {
        binding?.tvProductBundling?.text = data.name
    }

    private fun bindImage(data: BundleItem) {
        binding?.ivProductBundlingThumbnail?.loadImage(data.imageUrl)
    }

    private fun bindListener(data: BundleItem) {
        binding?.ivProductBundlingThumbnail?.setOnClickListener {
            productBundling?.let { bundle ->
                listener?.onClickProductBundlingImage(data, bundle)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_list_product_bundling
    }
}