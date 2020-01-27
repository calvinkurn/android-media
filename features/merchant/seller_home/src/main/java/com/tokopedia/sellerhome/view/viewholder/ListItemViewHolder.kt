package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ListItemUiModel
import kotlinx.android.synthetic.main.sah_item_list.view.*

class ListItemViewHolder(view: View?) : AbstractViewHolder<ListItemUiModel>(view) {
    companion object {
        val RES_LAYOUT = R.layout.sah_item_list
    }

    override fun bind(element: ListItemUiModel) {
        with(element) {
            itemView.tv_info_seller_title.text = title
            itemView.tv_info_seller_type.text = type
            itemView.tv_info_seller_date.text = desc
            loadImage(imageUrl)
        }
    }

    private fun loadImage(imageUrl: String) {
        ImageHandler.loadImageRounded(itemView.context, itemView.iv_info_seller, imageUrl, 20f)
    }
}