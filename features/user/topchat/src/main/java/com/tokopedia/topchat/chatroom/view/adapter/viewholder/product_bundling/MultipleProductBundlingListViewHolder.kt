package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.databinding.ItemTopchatListProductBundlingBinding
import com.tokopedia.utils.view.binding.viewBinding

class MultipleProductBundlingListViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private var binding: ItemTopchatListProductBundlingBinding? by viewBinding()

    fun bind(data: MultipleProductBundlingUiModel.Bundling) {
        bindBundlingName(data)
        bindImage(data)
    }

    private fun bindBundlingName(data: MultipleProductBundlingUiModel.Bundling) {
        binding?.tvProductBundling?.text = data.bundlingName
    }

    private fun bindImage(data: MultipleProductBundlingUiModel.Bundling) {
        binding?.ivProductBundlingThumbnail?.loadImage(data.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_list_product_bundling
    }
}