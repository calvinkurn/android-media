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
        val number: String,
        val isNumerationShown: Boolean,
        val rating: String,
        val label: Label,
        val soldQuantity: String,

    ) : PlayProductUiModel() {

        val impressHolder = ImpressHolder()
        data class Label (
            val rankColors: List<String>,
            val rankFmt: String,
            val rankType: String,
        ) {
            companion object {
                const val RIBBON_TYPE_DEFAULT = "no ribbon"
            }
        }

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
                    number = "",
                    isNumerationShown = false,
                    rating = "",
                    soldQuantity = "",
                    label = Label(
                        rankColors = emptyList(),
                        rankFmt = "",
                        rankType = "",
                    )
                )
        }
    }

    object Placeholder : PlayProductUiModel()
}

val PlayProductUiModel.Product.isShowRating: Boolean
    get() = this.rating.isNotBlank()

val PlayProductUiModel.Product.isShowSoldQuantity: Boolean
    get() = this.soldQuantity.isNotBlank()
