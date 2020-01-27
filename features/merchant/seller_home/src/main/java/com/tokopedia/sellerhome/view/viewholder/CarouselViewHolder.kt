package com.tokopedia.sellerhome.view.viewholder

import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.Banner
import com.tokopedia.banner.Indicator
import com.tokopedia.core2.R2.color.unify_G500
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import kotlinx.android.synthetic.main.sah_carousel_widget.view.*

/**
 * Created By @faisalramd on 2020-01-23
 */

class CarouselViewHolder(itemView: View?) : AbstractViewHolder<CarouselWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sah_carousel_widget
        val BANNER_SEE_ALL_TEXT_SIZE = com.tokopedia.design.R.dimen.sp_12
        const val STATE_LOADING = 0
        const val STATE_NORMAL = 1
        const val STATE_ERROR = 2
        const val STATE_IMPERFECT_WITHOUT_SEE_ALL = 3
        const val STATE_IMPERFECT_WITHOUT_BOTTOM_ITEM = 4
    }

    var isSeeAllVisible = true
    var isBottomItemVisible = true

    override fun bind(element: CarouselWidgetUiModel) {
        val state = 1

        with(itemView) {
            tvBannerTitle.text = element.title
            renderBanners(bannerImages, element.imageUrls)
            setVisibilityState(state, itemView)
        }
    }

    private fun setVisibilityState(state: Int, itemView: View) {
        when (state) {
            STATE_LOADING -> {
                itemView.tvBannerTitle.visibility = View.GONE
                itemView.bannerImages.visibility = View.GONE
                itemView.bannerImagesShimmering.visibility = View.VISIBLE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            STATE_NORMAL -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            STATE_ERROR -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.GONE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.VISIBLE
                isSeeAllVisible = true
                isBottomItemVisible = true
            }
            STATE_IMPERFECT_WITHOUT_SEE_ALL -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = false
                isBottomItemVisible = true
            }
            STATE_IMPERFECT_WITHOUT_BOTTOM_ITEM -> {
                itemView.tvBannerTitle.visibility = View.VISIBLE
                itemView.bannerImages.visibility = View.VISIBLE
                itemView.bannerImagesShimmering.visibility = View.GONE
                itemView.bannerImagesError.visibility = View.GONE
                isSeeAllVisible = false
                isBottomItemVisible = false
            }
            else -> {}
        }
    }

    private fun renderBanners(banner: Banner, data: List<String>) {
        if (data.isNotEmpty()) {
            with (banner) {
                setPromoList(data)
                setOnPromoClickListener { print(it) }
                setOnPromoScrolledListener { print(it)}
                setOnPromoAllClickListener { print("all click") }
                context?.let {
                    setBannerSeeAllTextColor(ContextCompat.getColor(it, unify_G500))
                }
                setBannerIndicator(Indicator.GREEN)
                bannerSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(BANNER_SEE_ALL_TEXT_SIZE))
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