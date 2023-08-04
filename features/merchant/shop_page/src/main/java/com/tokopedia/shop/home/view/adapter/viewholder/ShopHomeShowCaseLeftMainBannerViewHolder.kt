package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseLeftMainBannerBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseLeftMainBannerViewHolder(itemView: View) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_left_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
    }

    private val viewBinding: ItemShopHomeShowcaseLeftMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title
        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD


        val firstShowcase = showcases.getOrNull(0)
        val secondShowcase = showcases.getOrNull(1)
        val thirdShowcase = showcases.getOrNull(2)
        val fourthShowcase = showcases.getOrNull(3)
        val fifthShowcase = showcases.getOrNull(4)


        firstShowcase?.let {
            viewBinding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            viewBinding?.tpgFirstBannerTitle?.text = firstShowcase.name
            viewBinding?.imgFirstBanner?.visible()
            viewBinding?.tpgFirstBannerTitle?.visible()
        }

        secondShowcase?.let {
            viewBinding?.imgSecondBanner?.loadImage(secondShowcase.imageUrl)
            viewBinding?.tpgSecondBannerTitle?.text = secondShowcase.name
            viewBinding?.imgSecondBanner?.visible()
            viewBinding?.tpgSecondBannerTitle?.visible()
        }
        thirdShowcase?.let {
            viewBinding?.imgThirdBanner?.loadImage(thirdShowcase.imageUrl)
            viewBinding?.tpgThirdBannerTitle?.text = thirdShowcase.name
            viewBinding?.imgThirdBanner?.visible()
            viewBinding?.tpgThirdBannerTitle?.visible()
        }
        fourthShowcase?.let {
            viewBinding?.imgFourthBanner?.loadImage(fourthShowcase.imageUrl)
            viewBinding?.tpgFourthBannerTitle?.text = fourthShowcase.name
            viewBinding?.imgFourthBanner?.visible()
            viewBinding?.tpgFourthBannerTitle?.visible()
        }
        fifthShowcase?.let {
            viewBinding?.imgFifthBanner?.loadImage(fifthShowcase.imageUrl)
            viewBinding?.tpgFifthBannerTitle?.text = fifthShowcase.name
            viewBinding?.imgFifthBanner?.visible()
            viewBinding?.tpgFifthBannerTitle?.visible()
        }
    }

}
