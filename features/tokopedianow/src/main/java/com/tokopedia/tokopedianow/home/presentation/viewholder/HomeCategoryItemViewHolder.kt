package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryItemUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_category.view.*

class HomeCategoryItemViewHolder(
        itemView: View,
        private val listener: HomeCategoryItemListener? = null,
): AbstractViewHolder<HomeCategoryItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_home_category
    }

    override fun bind(data: HomeCategoryItemUiModel) {
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

    interface HomeCategoryItemListener {
        fun onCategoryClicked(position: Int, categoryId: String)
    }
}
