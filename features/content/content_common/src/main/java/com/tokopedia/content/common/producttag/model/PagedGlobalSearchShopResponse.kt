package com.tokopedia.content.common.producttag.model

import com.tokopedia.content.common.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SearchHeaderUiModel
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
data class PagedGlobalSearchShopResponse(
    val totalShop: Int,
    val pagedData: PagedDataUiModel<ShopUiModel>,
    val header: SearchHeaderUiModel,
)