package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import kotlinx.android.synthetic.main.item_tokopedianow_home_category.view.*

class TokoNowCategoryItemViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryItemListener? = null,
): AbstractViewHolder<TokoNowCategoryItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_category
    }

    override fun bind(data: TokoNowCategoryItemUiModel) {
        itemView.apply {
            textCategory.text = data.title
            imageCategory.loadImage(data.imageUrl) {
                setCacheStrategy(MediaCacheStrategy.RESOURCE)
            }
            setOnClickListener {
                listener?.onCategoryClicked(adapterPosition, data.id)
                RouteManager.route(context, data.appLink)
            }
        }
    }

    interface TokoNowCategoryItemListener {
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}
