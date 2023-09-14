package com.tokopedia.productcard.compact.similarproduct.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.similarproduct.presentation.adapter.ProductCardCompactSimilarProductTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCardCompactSimilarProductUiModel(
    val id: String,
    val shopId: String,
    val shopName: String,
    val name: String,
    val quantity: Int = 0,
    val stock: Int,
    val isVariant: Boolean,
    val minOrder: Int = 1,
    val maxOrder: Int = 1,
    val priceFmt: String,
    val weight: String = "",
    val imageUrl: String,
    val slashedPrice: String = "",
    val discountPercentage: String = "",
    val similarProducts: List<ProductCardCompactSimilarProductUiModel> = emptyList(),
    val categoryId: String = "",
    val categoryName: String = "",
    val position: Int = 0,
    val warehouseIds: String = ""
) : Visitable<ProductCardCompactSimilarProductTypeFactory>, Parcelable {

    val impressHolder
        get() = ImpressHolder()

    override fun type(typeFactory: ProductCardCompactSimilarProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}
