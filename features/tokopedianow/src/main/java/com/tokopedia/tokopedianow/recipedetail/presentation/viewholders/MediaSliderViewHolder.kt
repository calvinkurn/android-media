package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel

class MediaSliderViewHolder(itemView: View): AbstractViewHolder<MediaSliderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_media_slider
    }

    override fun bind(media: MediaSliderUiModel) {

    }
}