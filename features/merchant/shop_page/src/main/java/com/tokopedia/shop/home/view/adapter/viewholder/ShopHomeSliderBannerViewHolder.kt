package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.viewmodel_slider_banner.view.*
import java.util.ArrayList

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderBannerViewHolder(
        view: View?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.viewmodel_slider_banner
    }

    class CarouselData(val imageUrl: String)

    private var carouselShopPage: CarouselUnify? = null
    private var bannerData: ShopHomeDisplayWidgetUiModel? = null
    private var carouselData: ArrayList<Any>? = null
    private var itmListener = { view: View, data: Any ->
        val img: ImageUnify = view.findViewById(R.id.imageCarousel)
        val carouselItem = data as CarouselData
        val index = carouselData?.indexOf(carouselItem) ?: 0
        bannerData?.let { bannerData ->
            bannerData.data?.let { bannerItemData ->
                img.setOnClickListener {
                    onClickBannerItem(
                            bannerData,
                            bannerItemData[index],
                            adapterPosition,
                            index
                    )
                }
                img.addOnImpressionListener(bannerItemData[index]) {
                    listener.onDisplayItemImpression(
                            bannerData,
                            bannerItemData[index],
                            adapterPosition,
                            index
                    )
                }
            }
        }
        img.setImage(carouselItem.imageUrl, 0F)
    }

    init {
        carouselShopPage = view?.findViewById(R.id.carousel_shop_page)
    }

    override fun bind(shopHomeDisplayWidgetUiModel: ShopHomeDisplayWidgetUiModel) {
        bannerData = shopHomeDisplayWidgetUiModel
        carouselData = dataWidgetToCarouselData(shopHomeDisplayWidgetUiModel)
        carouselShopPage?.apply {
            autoplayDuration = 5000L
            autoplay = true
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true
            carouselData?.let {
                addItems(R.layout.widget_slider_banner_item, it, itmListener)
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