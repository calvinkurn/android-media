package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by Rafli Syam on 2020-03-09
 */

interface BaseShowcaseProduct : Parcelable

@Parcelize
data class ShowcaseProduct(
        val productId: String,
        val productName: String,
        val productPrice: Int,
        val ratingStarAvg: Float,
        val totalReview: Int,
        val productImageUrl: String,
        val isPo: Boolean = false,
        val isWholesale: Boolean = false,
        val isWishlist: Boolean = false,
        val isSold: Boolean = false,
        var ishighlighted: Boolean = false,
        var isCloseable: Boolean = false,
        var isNewAppended: Boolean = false
): BaseShowcaseProduct, Parcelable

@Parcelize
class LoadingShowcaseProduct: BaseShowcaseProduct