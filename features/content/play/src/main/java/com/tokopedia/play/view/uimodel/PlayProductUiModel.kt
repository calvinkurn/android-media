package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.view.type.*

/**
 * Created by jegul on 03/03/20
 */
sealed class PlayProductUiModel {

    data class Product(
        val id: String,
        val shopId: String,
        val imageUrl: String,
        val title: String,
        val stock: ProductStock,
        val isVariantAvailable: Boolean,
        val price: ProductPrice,
        val minQty: Int,
        val isFreeShipping: Boolean,
        val applink: String?,
        val isTokoNow: Boolean,
        val isPinned: Boolean,
        val isRilisanSpesial: Boolean,
        val buttons: List<ProductButtonUiModel>,
    ) : PlayProductUiModel() {

        val impressHolder = ImpressHolder()

        companion object {
            val Empty: Product
                get() = Product(
                    id = "",
                    shopId = "",
                    imageUrl = "",
                    title = "",
                    stock = OutOfStock,
                    isVariantAvailable = false,
                    price = OriginalPrice("", 0.0),
                    minQty = 0,
                    isFreeShipping = false,
                    applink = null,
                    isTokoNow = false,
                    isPinned = false,
                    isRilisanSpesial = false,
                    buttons = emptyList(),
                )
        }
    }

    object Placeholder : PlayProductUiModel()
}
