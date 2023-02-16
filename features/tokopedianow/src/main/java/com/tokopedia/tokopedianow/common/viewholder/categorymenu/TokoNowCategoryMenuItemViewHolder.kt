package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil
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
            ivCategoryImage.setBackgroundColor(
                ViewUtil.safeParseColor(
                    color = data.color,
                    defaultColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )
            root.setOnClickListener {
                setLayoutClicked(data)
            }
            root.addOnImpressionListener(data) {
                listener?.onCategoryItemImpressed(data, layoutPosition)
            }
        }
    }

    private fun setLayoutClicked(data: TokoNowCategoryMenuItemUiModel) {
        RouteManager.route(itemView.context, data.appLink)
        listener?.onCategoryItemClicked(data, layoutPosition)
    }

    interface TokoNowCategoryMenuItemListener {
        fun onCategoryItemClicked(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int)
        fun onCategoryItemImpressed(data: TokoNowCategoryMenuItemUiModel, itemPosition: Int)
    }
}
