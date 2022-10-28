package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by yovi.putra on 03/09/22"
 * Project name: android-tokopedia-core
 **/

@Parcelize
data class ProductDetailInfoContent(
    val icon: String = "",
    val title: String = "",
    var subtitle: String = "",
    val applink: String = "",
    val infoLink: String = "",
    val showAtFront: Boolean = false,
    val isAnnotation: Boolean = false,
    val showAtBottomSheet: Boolean = true
) : Parcelable