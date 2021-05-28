package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_category.view.*

class HomeCategoryItemViewHolder(itemView: View): AbstractViewHolder<HomeCategoryItemUiModel>(itemView) {

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
                RouteManager.route(context, data.appLink)
            }
        }
    }
}
