package com.tokopedia.shop.home.view.adapter.viewholder

import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.viewmodel_slider_banner.view.*
import java.util.ArrayList

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderBannerViewHolder(
        view: View?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view), CarouselUnify.OnActiveIndexChangedListener {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.viewmodel_slider_banner
    }

    class CarouselData(val imageUrl: String)

    private var carouselShopPage: CarouselUnify? = null
    private var bannerData: ShopHomeDisplayWidgetUiModel? = null
    private var carouselData: ArrayList<Any>? = null

    private var heightRatio = 0.0F

    private var itmListener = { view: View, data: Any ->
        val img: ImageUnify = view.findViewById(R.id.imageCarousel)
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

        val indexZero = getIndexRatio(0).toFloat()
        val indexOne = getIndexRatio(1).toFloat()
        heightRatio = (indexOne / indexZero)

        img.setImageUrl(carouselItem.imageUrl, heightRatio = heightRatio)
    }

    init {
        carouselShopPage = view?.findViewById(R.id.carousel_shop_page)
        carouselShopPage?.apply {
            autoplayDuration = 5000L
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true
            onActiveIndexChangedListener = this@ShopHomeSliderBannerViewHolder
            indicatorWrapper.setMargin(0, 8.toPx(), 0, 0)
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
            carouselData?.let {
                if (stage.childCount == 0) {
                    addItems(R.layout.widget_slider_banner_item, it, itmListener)
                    Handler().post {
                        activeIndex = 0
                        autoplay = true
                    }
                }
            }
        }
        itemView.textViewTitle?.apply {
            if (shopHomeDisplayWidgetUiModel.header.title.isEmpty()) {
                hide()
            } else {
                text = shopHomeDisplayWidgetUiModel.header.title
                show()
            }
        }
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

    private fun getIndexRatio(index: Int): Int {
        return bannerData?.header?.ratio?.split(":")?.get(index)?.toInt() ?: 0
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
}