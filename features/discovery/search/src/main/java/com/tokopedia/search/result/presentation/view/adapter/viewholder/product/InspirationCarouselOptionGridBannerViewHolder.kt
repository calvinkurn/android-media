package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.drawable.ColorDrawable
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
            hideDescButton()
        } else {
            renderBackgroundColor()
            bindProductDesc(item)
            showGridButton()
        }
        bindOnClickListener(item)
    }

    private fun bindBannerImage(item: InspirationCarouselViewModel.Option) {
        itemView.optionGridCardViewBannerImage?.shouldShowWithAction(item.bannerImageUrl.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(itemView.optionGridCardViewBannerImage, item.bannerImageUrl)
        }
    }

    private fun hideDescButton() {
        itemView.optionGridBannerDesc?.visibility = View.GONE
        itemView.optionGridBannerButton?.visibility = View.GONE
    }

    private fun renderBackgroundColor() {
        itemView.optionGridCardViewConstraintLayout?.background = ColorDrawable(
                MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_P500)
        )
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