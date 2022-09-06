package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailInfoSeeMoreData
import kotlinx.parcelize.Parcelize

/**
 * Created by yovi.putra on 03/09/22"
 * Project name: android-tokopedia-core
 **/

@Parcelize
data class ProductDetailInfoSeeMore(
    val actionTitle: String = "",
    val param: String = ""
) : Parcelable

fun ProductDetailInfoSeeMoreData.asModel() = ProductDetailInfoSeeMore(
    actionTitle = actionTitle,
    param = param
)