package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderBannerUiModel
import com.tokopedia.unifyprinciples.Typography

class HomeSliderBannerViewHolder (
        itemView: View
): AbstractViewHolder<HomeSliderBannerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_slider_banner
    }

    private var carousel: CarouselUnify? = null
    private var tpTitle: Typography? = null
    private var tpDesc: Typography? = null
    private var tpSmallDesc: Typography? = null

    init {
        initCarousel()
    }

    private var itmListener = { view: View, data: Any ->
        tpTitle = view.findViewById(R.id.tp_title)
        tpDesc = view.findViewById(R.id.tp_desc)
        tpSmallDesc = view.findViewById(R.id.tp_small_desc)
        val dataList = data as String
        if (dataList.contains("dia")) {
            tpSmallDesc?.hide()
        }
    }

    override fun bind(element: HomeSliderBannerUiModel?) {
        carousel?.apply {
            stage.removeAllViews()
            if (stage.childCount == 0) {
                addItems(R.layout.partial_item_tokomart_slider_banner, arrayListOf("ana","dia"), itmListener)
            }
        }
    }

    private fun initCarousel() {
        carousel = itemView.findViewById(R.id.carousel_slider)
        carousel?.apply {
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true
        }
    }
}