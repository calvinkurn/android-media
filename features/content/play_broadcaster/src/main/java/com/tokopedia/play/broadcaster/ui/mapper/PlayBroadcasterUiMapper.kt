package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.ui.model.PlayEtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.state.SelectableState
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 02/06/20
 */
object PlayBroadcasterUiMapper {

    fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<PlayEtalaseUiModel> = etalaseList.map {
        PlayEtalaseUiModel(
                id = it.id.toLong(),
                name = it.name,
                productMap = mutableMapOf(),
                totalProduct = it.count,
                stillHasProduct = true,
                type = EtalaseType.getByType(it.type)
        )
    }

    fun mapProductList(
            productsResponse: GetProductsByEtalaseResponse.GetShopProductData,
            isSelectedHandler: (Long) -> Boolean,
            isSelectableHandler: () -> SelectableState
    ) = productsResponse.data.map {
        ProductContentUiModel(
                id = it.productId.toLong(),
                name = it.name,
                imageUrl = it.primaryImage.resize300,
                stock = it.stock,
                isSelectedHandler = isSelectedHandler,
                isSelectable = isSelectableHandler
        )
    }
}