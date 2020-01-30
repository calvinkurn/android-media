package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.CarouselState
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import kotlinx.android.synthetic.main.sah_carousel_widget.view.*
import timber.log.Timber

/**
 * Created By @faisalramd on 2020-01-23
 */

class CarouselViewHolder(itemView: View?) : AbstractViewHolder<CarouselWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sah_carousel_widget
    }

    private var isSeeAllVisible = true
    private var isBottomItemVisible = true

    override fun bind(element: CarouselWidgetUiModel) {
        val elementDataUi = element.data?.data
        var imageList: List<String> = emptyList()

        elementDataUi?.let { dataUi ->
            imageList = dataUi.map { it.featuredMediaURL }
        }

        with(itemView) {
            tvBannerTitle.text = element.title
            renderBanners(bannerImages, imageList)
            element.data?.let {
                setVisibilityState(it.state, itemView)
            }
        }
    }

    private fun setVisibilityState(state: CarouselState, itemView: View) {
        when (state) {
            CarouselState.LOADING -> {
                itemView.tvBannerTitle.visibility = View.GONE
                itemView.bannerImages.visibility = View.GONE
                itemView.bannerImagesShimmering.visibility = View.VISIBLE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            CarouselState.NORMAL -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            CarouselState.ERROR -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.GONE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.VISIBLE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            CarouselState.IMPERFECT_WITHOUT_SEE_ALL -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = false
                isBottomItemVisible = true
            }
            CarouselState.IMPERFECT_WITHOUT_BOTTOM_ITEM -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = false
                isBottomItemVisible = false
            }
        }
    }

    private fun renderBanners(banner: BannerCarousel, imageList: List<String>) {

        if (imageList.isNotEmpty()) {
            with(banner) {
                setPromoList(imageList)
                setOnPromoClickListener { Timber.e(it.toString()) }
                setOnPromoScrolledListener { Timber.e(it.toString()) }
                setOnPromoAllClickListener { Timber.e("Lihat Semua") }
                buildView()

                if (!isSeeAllVisible) bannerSeeAll.visibility = View.GONE
                if (!isBottomItemVisible) {
                    bannerSeeAll.visibility = View.GONE
                    bannerIndicator.visibility = View.GONE
                }
            }
        }
    }
}