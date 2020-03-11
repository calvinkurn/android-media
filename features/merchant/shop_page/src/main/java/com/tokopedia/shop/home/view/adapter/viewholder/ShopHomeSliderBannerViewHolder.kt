package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.setImage
import java.util.ArrayList

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderBannerViewHolder(
        view: View?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view)
//        BannerView.OnPromoClickListener, BannerView.OnPromoAllClickListener,
//        BannerView.OnPromoDragListener, BannerView.OnPromoScrolledListener,
//        BannerView.OnPromoLoadedListener
{

    private var carouselShopPage: CarouselUnify? = null
    private var bannerData: ShopHomeDisplayWidgetUiModel? = null

    init {
        carouselShopPage = view?.findViewById(R.id.carousel_shop_page)
    }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        bannerData = element
        val carouselData = dataWidgetToCarouselData(element)

        val itemCarousel = { view: View, data: Any ->
            val img: ImageUnify = view.findViewById(R.id.imageCarousel)

            img.setImage((data as CarouselData).imageUrl, 0F)
        }

        carouselShopPage?.apply {
            autoplay = true
            autoplayDuration = 5000L
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true

            addItems(R.layout.widget_slider_banner_item, carouselData, itemCarousel)
        }
    }

//    override fun onPromoLoaded() {
//        this.banner?.bannerIndicator?.visible()
//    }
//
//    override fun onPromoClick(position: Int) {
//        bannerData?.data?.let {
//            val widgetItem = it[position]
//            listener.onItemClicked(bannerData, widgetItem, adapterPosition, position)
//        }
//    }
//
//    override fun onPromoAllClick() {}
//
//    override fun onPromoDragEnd() {}
//
//    override fun onPromoDragStart() {}
//
//    override fun onPromoScrolled(position: Int) {
//        bannerData?.data?.let {
//            val widgetItem = it[position]
//            if(!widgetItem.isInvoke){
//                listener.onItemImpression(bannerData, widgetItem, adapterPosition, position)
//                widgetItem.invoke()
//            }
//        }
//    }

    private fun dataWidgetToCarouselData(element: ShopHomeDisplayWidgetUiModel): ArrayList<Any> {
        val mutableString: ArrayList<Any> = ArrayList()
        element.data?.map {
            it.imageUrl.let { img ->
                mutableString.add(CarouselData(img))
            }
        }
        return mutableString
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.viewmodel_slider_banner
    }


    class CarouselData(val imageUrl: String)
}