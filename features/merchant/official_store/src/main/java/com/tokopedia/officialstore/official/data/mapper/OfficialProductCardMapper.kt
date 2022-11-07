package com.tokopedia.officialstore.official.data.mapper

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView

class OfficialProductCardMapper (
        private val context: Context,
        private val dispatchers: CoroutineDispatchers
){
    fun mappingProductCards(grids: List<Grid>): List<ProductCardModel> {
        return grids.map {grid ->
            ProductCardModel(
                    slashedPrice = grid.slashedPrice,
                    productName = grid.name,
                    formattedPrice = grid.price,
                    productImageUrl = grid.imageUrl,
                    discountPercentage = grid.discount,
                    freeOngkir = ProductCardModel.FreeOngkir(grid.freeOngkir?.isActive
                            ?: false, grid.freeOngkir?.imageUrl ?: ""),
                    labelGroupList = grid.labelGroup.map { label ->
                        ProductCardModel.LabelGroup(
                                position = label.position,
                                title = label.title,
                                type = label.type,
                                imageUrl = label.imageUrl
                        )
                    },
                    hasThreeDots = false
            )

        }
    }

    suspend fun getMaxHeightProductCards(productCardModels: List<ProductCardModel>): Int{
        return productCardModels.getMaxHeightForGridView(
                context = context,
                coroutineDispatcher = dispatchers.io,
                productImageWidth = context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
        )
    }
}
