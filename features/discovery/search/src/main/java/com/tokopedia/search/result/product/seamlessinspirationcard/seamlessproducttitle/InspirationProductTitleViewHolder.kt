package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproducttitle

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.view.HomeComponentHeaderListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselSeamlessProductTitleBinding
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselListener
import com.tokopedia.search.utils.SEARCH_PAGE_RESULT_MAX_LINE
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class InspirationProductTitleViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationCarouselListener,
    private val isReimagineProductCard: Boolean,
): AbstractViewHolder<InspirationProductTitleDataView>(itemView) {

    private var binding: SearchInspirationCarouselSeamlessProductTitleBinding? by viewBinding()

    init {
        initPadding()
    }

    private fun initPadding() {
        binding?.root?.run {
            if (!isReimagineProductCard)
                setPadding(
                    paddingStart,
                    NON_REIMAGINE_PADDING_TOP_DP.toPx(),
                    paddingEnd,
                    paddingBottom,
                )
            else
                setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
        }
    }

    override fun bind(element: InspirationProductTitleDataView?) {
        element ?: return
        val option = element.inspirationCarouselDataView.options.firstOrNull() ?: return

        binding?.searchInspirationCarouselSeamlessProductTitle?.bind(
            channelHeader = ChannelHeader(
                name = option.title,
                subtitle = option.subtitle,
                applink = option.applink,
                headerType = ChannelHeader.HeaderType.CHEVRON,
            ),
            listener = object : HomeComponentHeaderListener {
                override fun onSeeAllClick(link: String) {
                    inspirationCarouselListener.onInspirationCarouselSeeAllClicked(option)
                }
            },
            maxLines = SEARCH_PAGE_RESULT_MAX_LINE,
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_carousel_seamless_product_title

        private const val NON_REIMAGINE_PADDING_TOP_DP = 12
    }
}
