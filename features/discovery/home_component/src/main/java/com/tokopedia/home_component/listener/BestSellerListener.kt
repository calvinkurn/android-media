package com.tokopedia.home_component.listener

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel

interface BestSellerListener {
    fun onBestSellerClick(
        bestSellerDataModel: BestSellerDataModel,
        bestSellerProductDataModel: BestSellerProductDataModel,
        widgetPosition: Int
    )
    fun onBestSellerImpress(
        bestSellerDataModel: BestSellerDataModel,
        bestSellerProductDataModel: BestSellerProductDataModel,
        widgetPosition: Int
    )
    fun onBestSellerFilterImpress(
        bestSellerChipProductDataModel: BestSellerChipDataModel,
        bestSellerDataModel: BestSellerDataModel,
    )
    fun onBestSellerFilterClick(
        selectedChipProduct: BestSellerChipProductDataModel,
        bestSellerDataModel: BestSellerDataModel,
    )
    fun onBestSellerFilterScrolled(
        selectedChipProduct: BestSellerChipProductDataModel,
        bestSellerDataModel: BestSellerDataModel,
        widgetPosition: Int,
        chipsPosition: Int,
        scrollDirection: CarouselPagingGroupChangeDirection,
    )
    fun onBestSellerSeeMoreTextClick(bestSellerDataModel: BestSellerDataModel, appLink: String)
}
