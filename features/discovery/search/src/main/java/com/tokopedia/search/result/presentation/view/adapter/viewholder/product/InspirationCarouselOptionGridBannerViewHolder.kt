package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel_option_grid_banner.view.*

class InspirationCarouselOptionGridBannerViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid_banner
    }

    override fun bind(item: InspirationCarouselViewModel.Option) {
        if (item.bannerImageUrl.isNotEmpty()) {
            bindBannerImage(item)
        } else {
            bindProductDesc(item)
            showGridButton()
        }
        bindOnClickListener(item)
    }

    private fun bindBannerImage(item: InspirationCarouselViewModel.Option) {
        itemView.optionGridCardViewBannerImage?.shouldShowWithAction(item.bannerImageUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, itemView.optionGridCardViewBannerImage, item.bannerImageUrl)
        }
    }

    private fun bindProductDesc(item: InspirationCarouselViewModel.Option) {
        itemView.optionGridBannerDesc?.shouldShowWithAction(item.title.isNotEmpty()) {
            itemView.optionGridBannerDesc?.text = MethodChecker.fromHtml(item.title)
        }
    }

    private fun showGridButton() {
        itemView.optionGridBannerButton?.visibility = View.VISIBLE
    }

    private fun bindOnClickListener(item: InspirationCarouselViewModel.Option) {
        itemView.optionGridCardViewBanner?.setOnClickListener { _ ->
            inspirationCarouselListener.onInspirationCarouselGridBannerClicked(item)
        }
    }
}