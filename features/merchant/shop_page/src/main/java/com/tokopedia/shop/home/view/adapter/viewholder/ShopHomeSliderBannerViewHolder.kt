package com.tokopedia.shop.home.view.adapter.viewholder

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE
import com.tokopedia.shop.home.ShopCarouselBannerImageUnify
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.viewmodel_slider_banner.view.*
import java.util.*

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderBannerViewHolder(
        view: View?,
        private val previousViewHolder: AbstractViewHolder<*>?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view), CarouselUnify.OnActiveIndexChangedListener {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.viewmodel_slider_banner
        const val DURATION_SLIDER_BANNER = 5000L
    }

    private var carouselShopPage: CarouselUnify? = null
    private var bannerData: ShopHomeDisplayWidgetUiModel? = null
    private var carouselData: ArrayList<Any>? = null

    private var itmListener = { view: View, data: Any ->
        val img: ShopCarouselBannerImageUnify = view.findViewById(R.id.imageCarousel)
        val carouselItem = data as CarouselData
        val index = carouselData?.indexOf(carouselItem) ?: 0
        bannerData?.let { bannerData ->
            bannerData.data?.let {
                img.setOnClickListener {
                    onClickBannerItem(
                            bannerData,
                            bannerData.data.get(index),
                            adapterPosition,
                            index
                    )
                }
            }
        }
        val performanceMonitoring = PerformanceMonitoring.start(SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE)
        //avoid crash in ImageUnify when image url is returned as base64
        try {
            if(img.context.isValidGlideContext()) {
                val ratio = bannerData?.let { getHeightRatio(it) } ?: 0f
                img.heightRatio = ratio
                img.setImageUrl(carouselItem.imageUrl, ratio)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        img.onUrlLoaded = {
            performanceMonitoring.stopTrace()
        }
    }

    init {
        carouselShopPage = view?.findViewById(R.id.carousel_shop_page)
        carouselShopPage?.apply {
            autoplayDuration = DURATION_SLIDER_BANNER
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true
            onActiveIndexChangedListener = this@ShopHomeSliderBannerViewHolder
            indicatorWrapper.setMargin(0, 4.toPx(), 0, 0)
        }
    }

    override fun onActiveIndexChanged(prev: Int, current: Int) {
        itemView.isVisibleOnTheScreen({
            bannerData?.let { shopHomeDisplayWidgetUiModel ->
                shopHomeDisplayWidgetUiModel.data?.let { listDisplayWidget ->
                    if (current >= 0 && current < shopHomeDisplayWidgetUiModel.data.size) {
                        val item = listDisplayWidget[current]
                        if (!item.isInvoke) {
                            listener.onDisplayItemImpression(
                                    shopHomeDisplayWidgetUiModel,
                                    listDisplayWidget[current],
                                    adapterPosition,
                                    current
                            )
                            item.invoke()
                        }
                    }
                }
            }
        }, {})
    }

    override fun bind(shopHomeDisplayWidgetUiModel: ShopHomeDisplayWidgetUiModel) {
        bannerData = shopHomeDisplayWidgetUiModel
        carouselData = dataWidgetToCarouselData(shopHomeDisplayWidgetUiModel)
        carouselShopPage?.apply {
            stage.removeAllViews()
            carouselData?.let {
                if (stage.childCount == 0) {
                    addItems(R.layout.widget_slider_banner_item, it, itmListener)
                    Handler().post {
                        activeIndex = 0
                    }
                }
            }
        }
        itemView.textViewTitle?.apply {
            if (shopHomeDisplayWidgetUiModel.header.title.isEmpty()) {
                hide()
                if (previousViewHolder is ShopHomeSliderSquareViewHolder || previousViewHolder is ShopHomeCarousellProductViewHolder) {
                    (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                        setMargins(leftMargin, 16.toPx(), rightMargin, bottomMargin)
                    }
                }
            } else {
                text = shopHomeDisplayWidgetUiModel.header.title
                show()
            }
        }
        bannerData?.let{
            val widthRatio = getIndexRatio(it, 0).toString()
            val heightRatio = getIndexRatio(it, 1).toString()
            carouselShopPage?.apply {
                (layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = "$widthRatio:$heightRatio"
                post {
                    (carouselShopPage?.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ""
                }
            }
        }
    }

    fun pauseTimer() {
        carouselShopPage?.autoplay = false
    }

    fun resumeTimer() {
        carouselShopPage?.timer = Timer()
        carouselShopPage?.autoplay = true
    }

    private fun onClickBannerItem(
            element: ShopHomeDisplayWidgetUiModel?,
            displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem?,
            adapterPosition: Int,
            index: Int
    ) {
        displayWidgetItem?.let {
            listener.onDisplayItemClicked(
                    element,
                    it,
                    adapterPosition,
                    index
            )
            it.invoke()
        }
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(data: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(data, 0).toFloat()
        val indexOne = getIndexRatio(data, 1).toFloat()
        return (indexOne / indexZero)
    }

    private fun dataWidgetToCarouselData(element: ShopHomeDisplayWidgetUiModel): ArrayList<Any> {
        val mutableString: ArrayList<Any> = ArrayList()
        element.data?.map {
            it.imageUrl.let { img ->
                mutableString.add(CarouselData(img))
            }
        }
        return mutableString
    }

    class CarouselData(val imageUrl: String)
}