package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.ui.model.PlayEtalaseUiModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 02/06/20
 */
object PlayBroadcasterUiMapper {

    fun mapEtalaseList(etalaseList: List<ShopEtalaseModel>): List<PlayEtalaseUiModel> = etalaseList.map {
        PlayEtalaseUiModel(
                id = it.id.toLong(),
                name = it.name,
                productList = emptyList(),
                totalProduct = it.count
        )
    }
}