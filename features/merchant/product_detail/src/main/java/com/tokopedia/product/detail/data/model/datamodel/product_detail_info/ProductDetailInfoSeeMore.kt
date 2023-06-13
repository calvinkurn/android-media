package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailInfoSeeMoreData
import kotlinx.parcelize.Parcelize

/**
 * Created by yovi.putra on 03/09/22"
 * Project name: android-tokopedia-core
 **/

@Parcelize
data class ProductDetailInfoSeeMore(
    val actionTitle: String = String.EMPTY,
    val param: String = String.EMPTY,
    val bottomSheetTitle: String = String.EMPTY
) : Parcelable

fun ProductDetailInfoSeeMoreData.asUiData() = ProductDetailInfoSeeMore(
    actionTitle = actionTitle,
    param = param,
    bottomSheetTitle = bottomSheetTitle
)