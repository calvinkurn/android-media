package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.databinding.ItemTopchatListProductBundlingBinding
import com.tokopedia.utils.view.binding.viewBinding

class MultipleBundlingItemViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private var binding: ItemTopchatListProductBundlingBinding? by viewBinding()

    fun bind(data: BundleItem) {
        bindBundlingName(data)
        bindImage(data)
    }

    private fun bindBundlingName(data: BundleItem) {
        binding?.tvProductBundling?.text = data.name
    }

    private fun bindImage(data: BundleItem) {
        binding?.ivProductBundlingThumbnail?.loadImage(data.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_list_product_bundling
    }
}