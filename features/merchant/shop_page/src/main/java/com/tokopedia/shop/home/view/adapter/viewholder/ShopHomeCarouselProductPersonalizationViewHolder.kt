package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC

/**
 * author by Rafli Syam on 17/02/2021
 */
class ShopHomeCarouselProductPersonalizationViewHolder (
        itemView: View,
        val shopHomeCarouselProductListener: ShopHomeCarouselProductListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_recommendation_carousel
    }

    private var tvCarouselTitle : TextView? = null
    private var recyclerView : CarouselProductCardView? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeCarousellProductUiModel) {
        tvCarouselTitle?.text = element.header.title

        // product list
        val carouselProductList = element.productList.map {
            ShopPageHomeMapper.mapToProductCardPersonalizationModel(
                    shopHomeProductViewModel = it,
                    isHasATC = isHasATC(element),
                    isHasOCCButton = element.name != RECENT_ACTIVITY,
                    occButtonText = if(isAtcOcc(element.name)) {
                        itemView.context.getString(
                                R.string.occ_text
                        )
                    } else ""
            )
        }

        // listeners
        val productAddToCartListener = object : CarouselProductCardListener.OnItemAddToCartListener {
            override fun onItemAddToCart(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition) ?: return
                shopHomeCarouselProductListener.onCarouselPersonalizationProductItemClickAddToCart(
                        adapterPosition,
                        carouselProductCardPosition,
                        element,
                        productItem,
                        isOcc = isAtcOcc(element.name)
                )
            }
        }

        val productClickListener = object : CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition) ?: return
                shopHomeCarouselProductListener.onPersonalizationCarouselProductItemClicked(
                        adapterPosition,
                        carouselProductCardPosition,
                        element,
                        productItem
                )
            }
        }

        val productImpressionListener = object : CarouselProductCardListener.OnItemImpressedListener {
            override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition) ?: return
                shopHomeCarouselProductListener.onCarouselProductPersonalizationItemImpression(
                        adapterPosition,
                        carouselProductCardPosition,
                        element,
                        productItem
                )

            }

            override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                return element.productList.getOrNull(carouselProductCardPosition)
            }
        }

        when (element.name) {

            RECENT_ACTIVITY -> {
                recyclerView?.bindCarouselProductCardViewGrid(
                        productCardModelList = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                )
            }

            BUY_AGAIN -> {
                recyclerView?.bindCarouselProductCardViewList(
                        productCardModelList = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                )
            }

        }
    }

    private fun initView() {
        tvCarouselTitle = itemView.findViewById(R.id.tv_title)
        recyclerView = itemView.findViewById(R.id.rvCarouselRecommendation)
    }

    private fun isAtcOcc(
            widgetName: String
    ) : Boolean = widgetName == BUY_AGAIN || widgetName == REMINDER

    private fun isHasATC(
       element : ShopHomeCarousellProductUiModel?
    ) : Boolean = (element?.header?.isATC == IS_ATC)
}