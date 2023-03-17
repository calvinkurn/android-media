package com.tokopedia.tokopedianow.similarproduct.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.similarproduct.adapter.SimilarProductTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimilarProductUiModel(
    val id: String,
    val shopId: String,
    val shopName: String,
    val name: String,
    val quantity: Int = 0,
    val stock: Int,
    val minOrder: Int = 1,
    val maxOrder: Int = 1,
    val priceFmt: String,
    val weight: String = "",
    val imageUrl: String,
    val slashedPrice: String = "",
    val discountPercentage: String = "",
    val similarProducts: List<SimilarProductUiModel> = emptyList(),
    val categoryId: String = "",
    val categoryName: String = "",
    val position: Int = 0
) : Visitable<SimilarProductTypeFactory>, Parcelable {

    val impressHolder
        get() = ImpressHolder()

    override fun type(typeFactory: SimilarProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}
