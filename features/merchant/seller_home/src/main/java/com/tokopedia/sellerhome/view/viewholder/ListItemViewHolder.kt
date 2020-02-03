package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.view.model.ListItemUiModel
import kotlinx.android.synthetic.main.sah_item_list.view.*

class ListItemViewHolder(view: View?) : AbstractViewHolder<ListItemUiModel>(view) {
    companion object {
        val RES_LAYOUT = R.layout.sah_item_list
    }

    override fun bind(element: ListItemUiModel) {
        with(element) {
            itemView.tv_list_item_title.text = title
            itemView.tv_list_item_desc.text = subtitle.parseAsHtml()
            loadImage(featuredMediaURL)
        }
    }

    private fun loadImage(featuredMediaURL: String) {
        ImageHandler.loadImageRounded(itemView.context, itemView.iv_list_item, featuredMediaURL, 20f)
    }
}