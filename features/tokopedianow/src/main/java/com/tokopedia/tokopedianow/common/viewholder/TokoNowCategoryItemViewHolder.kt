package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeCategoryBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryItemViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryItemListener? = null,
): AbstractViewHolder<TokoNowCategoryItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_category
    }

    private var binding: ItemTokopedianowHomeCategoryBinding? by viewBinding()

    override fun bind(data: TokoNowCategoryItemUiModel) {
        binding?.apply {
            tpCategory.text = data.title
            iuCategory.loadImage(data.imageUrl) {
                setCacheStrategy(MediaCacheStrategy.RESOURCE)
            }
            cuItemCategory.setOnClickListener {
                listener?.onCategoryClicked(adapterPosition, data.id)
                RouteManager.route(itemView.context, data.appLink)
            }
        }
    }

    interface TokoNowCategoryItemListener {
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}
