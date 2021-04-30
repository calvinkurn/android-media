package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryMenuUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_category_menu.view.*

class HomeCategoryMenuViewHolder(itemView: View): AbstractViewHolder<HomeCategoryMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_home_category_menu
    }

    override fun bind(data: HomeCategoryMenuUiModel) {
        itemView.apply {
            textCategory.text = data.title
            imageCategory.loadImage(data.iconUrl) {
                setCacheStrategy(MediaCacheStrategy.RESOURCE)
                isCircular(true)
            }
        }
    }
}
