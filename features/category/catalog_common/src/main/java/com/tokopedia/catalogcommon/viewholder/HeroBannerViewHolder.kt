package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HeroBannerViewHolder(itemView: View) : AbstractViewHolder<HeroBannerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_banner_hero
    }

    private val binding by viewBinding<WidgetItemBannerHeroBinding>()


    override fun bind(element: HeroBannerUiModel) {
        binding?.content?.text = element.content
    }
}
