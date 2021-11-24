package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.type.ProductPrice
import com.tokopedia.play.broadcaster.type.ProductStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.SelectableState

/**
 * Created by jegul on 26/05/20
 */
sealed class ProductUiModel

data class ProductContentUiModel(
        val id: String,
        val name: String,
        val imageUrl: String,
        val originalImageUrl: String,
        val stock: ProductStock,
        val price: ProductPrice,
        val isSelectedHandler: (String) -> Boolean,
        val isSelectable: (Boolean) -> SelectableState,
        val transitionName: String? = null
) : ProductUiModel() {

    val hasStock: Boolean
        get() = stock is StockAvailable

    fun extractData() = ProductData(
            id = id,
            name = name,
            imageUrl = imageUrl,
            originalImageUrl = originalImageUrl,
            stock = stock,
            price = price
    )

    companion object {

        fun createFromData(
                data: ProductData,
                isSelectedHandler: (String) -> Boolean = { false },
                isSelectable: (Boolean) -> SelectableState = { NotSelectable(Throwable()) },
                transitionName: String? = null): ProductContentUiModel {

            return ProductContentUiModel(
                    id = data.id,
                    name = data.name,
                    imageUrl = data.imageUrl,
                    originalImageUrl = data.originalImageUrl,
                    stock = data.stock,
                    isSelectedHandler = isSelectedHandler,
                    isSelectable = isSelectable,
                    transitionName = transitionName,
                    price = data.price
            )
        }
    }
}

object ProductLoadingUiModel : ProductUiModel()