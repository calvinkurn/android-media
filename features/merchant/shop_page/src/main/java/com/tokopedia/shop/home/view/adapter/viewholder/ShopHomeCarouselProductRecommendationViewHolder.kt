package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductRecommendationCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductRecommendationCarouselUiModel.Companion.IS_ATC

/**
 * author by Rafli Syam on 17/02/2021
 */
class ShopHomeCarouselProductRecommendationViewHolder (
        itemView: View
) : AbstractViewHolder<ShopHomeProductRecommendationCarouselUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_recommendation_carousel
    }

    private var tvCarouselTitle : TextView? = null
    private var recyclerView : CarouselProductCardView? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeProductRecommendationCarouselUiModel) {
        tvCarouselTitle?.text = element.header.title
        recyclerView?.bindCarouselProductCardViewGrid(
                productCardModelList = element.productList.map {
                    ShopPageHomeMapper.mapToProductCardModel(
                            isHasAddToCartButton = isHasATC(element),
                            hasThreeDots = false,
                            shopHomeProductViewModel = it,
                    )
                }
        )
    }

    private fun initView() {
        tvCarouselTitle = itemView.findViewById(R.id.tv_title)
        recyclerView = itemView.findViewById(R.id.rvCarouselRecommendation)
    }

    private fun isHasATC(
       element : ShopHomeProductRecommendationCarouselUiModel?
    ) : Boolean = (element?.header?.isATC == IS_ATC)
}