package com.tokopedia.content.product.picker.ugc.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedDataUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.SearchHeaderUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
data class PagedGlobalSearchShopResponse(
    @SerializedName("totalShop")
    val totalShop: Int,
    @SerializedName("pagedData")
    val pagedData: PagedDataUiModel<ShopUiModel>,
    @SerializedName("header")
    val header: SearchHeaderUiModel,
)
