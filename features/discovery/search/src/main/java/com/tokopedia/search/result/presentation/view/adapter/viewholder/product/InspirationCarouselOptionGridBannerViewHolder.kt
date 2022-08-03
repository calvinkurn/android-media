package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.drawable.ColorDrawable
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionGridBannerBinding
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCarouselOptionGridBannerViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid_banner
    }
    private var binding: SearchInspirationCarouselOptionGridBannerBinding? by viewBinding()

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
        binding?.optionGridCardViewBannerImage?.let {
            it.shouldShowWithAction(item.bannerImageUrl.isNotEmpty()) {
                it.loadImage(item.bannerImageUrl) {
                    setPlaceHolder(-1)
                }
            }
        }
    }

    private fun hideDescButton() {
        val binding = binding ?: return
        binding.optionGridBannerDesc.visibility = View.GONE
        binding.optionGridBannerButton.visibility = View.GONE
    }

    private fun renderBackgroundColor() {
        binding?.optionGridCardViewConstraintLayout?.background = ColorDrawable(
                MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

    private fun bindProductDesc(item: InspirationCarouselDataView.Option) {
        binding?.optionGridBannerDesc?.let {
            it.shouldShowWithAction(item.title.isNotEmpty()) {
                it.text = MethodChecker.fromHtml(item.title)
            }
        }
    }

    private fun showGridButton() {
        binding?.optionGridBannerButton?.visibility = View.VISIBLE
    }

    private fun bindOnClickListener(item: InspirationCarouselDataView.Option) {
        binding?.optionGridCardViewBanner?.setOnClickListener { _ ->
            inspirationCarouselListener.onInspirationCarouselGridBannerClicked(item)
        }
    }
}