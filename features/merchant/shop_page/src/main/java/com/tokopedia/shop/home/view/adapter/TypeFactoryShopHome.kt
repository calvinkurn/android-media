package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

interface TypeFactoryShopHome {
    fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int
    fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int
    fun type(shopHomeProductEtalaseTitleUiModel: ShopHomeProductEtalaseTitleUiModel): Int
    fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int
    fun type(carouselPlayCardViewModel: CarouselPlayWidgetUiModel): Int
}