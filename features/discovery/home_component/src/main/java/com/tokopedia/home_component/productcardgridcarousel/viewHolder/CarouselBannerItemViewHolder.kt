package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemBannerBinding
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselBannerCardDataModel
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CarouselBannerItemViewHolder(
    view: View,
    private val cardInteraction: Boolean = false,
) : AbstractViewHolder<CarouselBannerCardDataModel>(view) {
    private var binding: HomeBannerItemBannerBinding? by viewBinding()

    override fun bind(element: CarouselBannerCardDataModel) {
        bindView(element)
    }

    private fun bindView(element: CarouselBannerCardDataModel) {
        binding?.cardBanner?.apply {
            animateOnPress =
                if (cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
        }
        binding?.containerBanner?.setGradientBackground(element.gradientColor)
        binding?.imageBanner?.setImageUrl(element.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.home_banner_item_banner
    }
}
