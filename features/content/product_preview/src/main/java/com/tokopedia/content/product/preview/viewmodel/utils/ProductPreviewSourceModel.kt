package com.tokopedia.content.product.preview.viewmodel.utils

import android.os.Parcelable
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by astidhiyaa on 06/12/23
 */
@Parcelize
data class ProductPreviewSourceModel(
    val productId: String,
    val productPreviewSource: ProductPreviewSource
) : Parcelable {

    sealed interface ProductPreviewSource : Parcelable

    @Parcelize
    data class ProductSourceData(
        val productList: List<ProductContentUiModel> = emptyList()
    ) : ProductPreviewSource

    @Parcelize
    data class ReviewSourceData(
        val reviewId: String = "",
        val reviewMediaPosition: Int = -1
    ) : ProductPreviewSource

    @Parcelize
    object UnknownSource : ProductPreviewSource

    companion object {
        val Empty = ProductPreviewSourceModel(
            productId = "",
            productPreviewSource = UnknownSource
        )
    }
}
