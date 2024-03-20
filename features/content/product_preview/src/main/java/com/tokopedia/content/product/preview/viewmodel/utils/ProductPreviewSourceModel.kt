package com.tokopedia.content.product.preview.viewmodel.utils

import android.os.Parcelable
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by astidhiyaa on 06/12/23
 */
@Parcelize
data class ProductPreviewSourceModel(
    val productId: String,
    val sourceName: ProductPreviewSourceName,
    val source: ProductPreviewSource
) : Parcelable {

    sealed interface ProductPreviewSource : Parcelable

    @Parcelize
    data class ProductSourceData(
        val productSourceList: List<ProductMediaUiModel> = emptyList(),
        val hasReviewMedia: Boolean
    ) : ProductPreviewSource

    @Parcelize
    data class ReviewSourceData(
        val reviewSourceId: String = "",
        val attachmentSourceId: String = ""
    ) : ProductPreviewSource

    @Parcelize
    object UnknownSource : ProductPreviewSource

    enum class ProductPreviewSourceName(val value: String) {
        SHARE("share"),
        PRODUCT("product"),
        REVIEW("review"),
        UNKNOWN("unknown");

        companion object {
            private val values = ProductPreviewSourceName.values()

            fun getByValue(value: String): ProductPreviewSourceName {
                values.forEach {
                    if (it.value == value) return it
                }
                return UNKNOWN
            }
        }
    }

    companion object {
        val Empty = ProductPreviewSourceModel(
            productId = "",
            sourceName = ProductPreviewSourceName.UNKNOWN,
            source = UnknownSource
        )
    }
}
