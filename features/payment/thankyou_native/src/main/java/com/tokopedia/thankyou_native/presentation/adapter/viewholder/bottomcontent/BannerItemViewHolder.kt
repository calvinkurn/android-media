package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankBottomContentBannerBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.BannerWidgetModel
import com.tokopedia.thankyou_native.presentation.views.listener.BannerListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class BannerItemViewHolder(
    view: View?,
    val bannerListener: BannerListener
    ): AbstractViewHolder<BannerWidgetModel>(view) {

    companion object {
        val LAYOUT_ID = R.layout.thank_bottom_content_banner
        private val IMAGE_MARGIN = 16.toPx()
        private val IMAGE_GAP = 8.toPx()
        private const val SLIDE_TO_SHOW_1_ITEM = 1f
        private const val SLIDE_TO_SHOW_MULTIPLE_ITEM = 1.1f
    }

    private var binding: ThankBottomContentBannerBinding? by viewBinding()

    override fun bind(data: BannerWidgetModel?) {
        if (data == null) return

        bindTitle(data)
        bindCarousel(data)
    }

    private fun bindTitle(data: BannerWidgetModel) {
        binding?.tvBannerTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.tvBannerTitle?.text = data.title
        }
    }

    private fun bindCarousel(data: BannerWidgetModel) {
        binding?.carouselBanner?.apply {
            stage.removeAllViews()
            data.items.forEachIndexed { index, bannerItem ->
                val imageView = ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                    setMargin(
                        if (index == Int.ZERO) IMAGE_MARGIN else IMAGE_GAP,
                        0,
                        if (index == data.items.size - 1) IMAGE_MARGIN else IMAGE_GAP,
                        0,
                    )
                    adjustViewBounds = true
                    loadImageWithoutPlaceholder(bannerItem.assetUrl)
                    setOnClickListener {
                        bannerListener.onBannerClick(bannerItem, index)
                    }
                }

                addItem(imageView)
                indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
                slideToShow =
                    if (data.items.size > 1) SLIDE_TO_SHOW_MULTIPLE_ITEM else SLIDE_TO_SHOW_1_ITEM

                addOnImpressionListener(bannerItem) {
                    bannerListener.onBannerImpressed(bannerItem, index)
                }
            }
        }
    }
}
