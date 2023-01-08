package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.MediaSliderAnalytics
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeMediaSliderBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MediaSliderViewHolder(
    itemView: View,
    private val analytics: MediaSliderAnalytics? = null
): AbstractViewHolder<MediaSliderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_media_slider
    }

    private var binding: ItemTokopedianowRecipeMediaSliderBinding? by viewBinding()

    override fun bind(media: MediaSliderUiModel) {
        binding?.mediaSlider?.apply {
            setAnalytics(analytics)
            init(media.items)
        }
    }
}