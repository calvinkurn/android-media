package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryMenuItemViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryMenuItemListener? = null,
): AbstractViewHolder<TokoNowCategoryMenuItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_menu_item
    }

    private var binding: ItemTokopedianowCategoryMenuItemBinding? by viewBinding()

    override fun bind(data: TokoNowCategoryMenuItemUiModel) {
        binding?.apply {
            tpCategoryTitle.text = data.title
            ivCategoryImage.loadImage(data.imageUrl)
            root.setOnClickListener {
                setLayoutClicked(data)
            }
        }
    }

    private fun setLayoutClicked(data: TokoNowCategoryMenuItemUiModel) {
        RouteManager.route(itemView.context, data.appLink)
        listener?.onCategoryClicked(
            position = layoutPosition,
            categoryId = data.id,
            headerName = data.headerName,
            categoryName = data.title
        )
    }

    interface TokoNowCategoryMenuItemListener {
        fun onCategoryClicked(
            position: Int,
            categoryId: String,
            headerName: String,
            categoryName: String
        )
    }
}
