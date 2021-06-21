package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.drawable.ColorDrawable
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel_option_grid_banner.view.*

class InspirationCarouselOptionGridBannerViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid_banner
    }

    override fun bind(item: InspirationCarouselDataView.Option) {
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

    private fun bindBannerImage(item: InspirationCarouselDataView.Option) {
        itemView.optionGridCardViewBannerImage?.shouldShowWithAction(item.bannerImageUrl.isNotEmpty()) {
            itemView.optionGridCardViewBannerImage.loadImage(item.bannerImageUrl) {
                setPlaceHolder(-1)
            }
        }
    }

    private fun hideDescButton() {
        itemView.optionGridBannerDesc?.visibility = View.GONE
        itemView.optionGridBannerButton?.visibility = View.GONE
    }

    private fun renderBackgroundColor() {
        itemView.optionGridCardViewConstraintLayout?.background = ColorDrawable(
                MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

    private fun bindProductDesc(item: InspirationCarouselDataView.Option) {
        itemView.optionGridBannerDesc?.shouldShowWithAction(item.title.isNotEmpty()) {
            itemView.optionGridBannerDesc?.text = MethodChecker.fromHtml(item.title)
        }
    }

    private fun showGridButton() {
        itemView.optionGridBannerButton?.visibility = View.VISIBLE
    }

    private fun bindOnClickListener(item: InspirationCarouselDataView.Option) {
        itemView.optionGridCardViewBanner?.setOnClickListener { _ ->
            inspirationCarouselListener.onInspirationCarouselGridBannerClicked(item)
        }
    }
}