package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import kotlinx.android.synthetic.main.sah_carousel_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_carousel_widget_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*

/**
 * Created By @faisalramd on 2020-01-23
 */

class CarouselViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<CarouselWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sah_carousel_widget
    }

    override fun bind(element: CarouselWidgetUiModel) {
        listener.getCarouselData()
        observeState(element)

        itemView.tvBannerTitle.text = element.title
    }

    private fun observeState(element: CarouselWidgetUiModel) {
        val data = element.data
        when {
            null == data -> showLoadingState()
            data.error.isNotBlank() -> showErrorState()
            else -> renderBanners(element)
        }
    }

    private fun showLoadingState() = with(itemView) {
        tvBannerTitle.gone()
        bannerImages.gone()
        bannerImagesShimmering.visible()
        commonWidgetErrorState.gone()
    }

    private fun showErrorState() = with(itemView) {
        tvBannerTitle.visible()
        commonWidgetErrorState.visible()
        bannerImages.gone()
        bannerImagesShimmering.gone()
        ImageHandler.loadImageWithId(itemView.imgWidgetOnError, R.drawable.unify_globalerrors_connection)
    }

    private fun setupDetails(element: CarouselWidgetUiModel) {
        val banner = itemView.bannerImages
        if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
            banner.bannerSeeAll.text = element.ctaText
            banner.bannerSeeAll.visible()
        } else {
            banner.bannerSeeAll.gone()
        }
    }

    private fun renderBanners(element: CarouselWidgetUiModel) {
        val data = element.data?.data
        val imageList = data?.map { it.featuredMediaURL }.orEmpty()

        if (imageList.isNotEmpty()) {
            with(itemView) {
                tvBannerTitle.visible()
                commonWidgetErrorState.gone()
                bannerImagesShimmering.gone()

                setupDetails(element)

                bannerImages.visible()
                bannerImages.setPromoList(imageList)
                bannerImages.setOnPromoClickListener {
                    RouteManager.route(context, data?.get(it)?.appLink)
                }
                bannerImages.setOnPromoAllClickListener {
                    seeAll()
                }
                bannerImages.buildView()
            }
        } else {
            listener.removeWidget(adapterPosition, element)
        }
    }

    private fun seeAll() {

    }

    interface Listener : BaseViewHolderListener {
        fun getCarouselData()
    }
}