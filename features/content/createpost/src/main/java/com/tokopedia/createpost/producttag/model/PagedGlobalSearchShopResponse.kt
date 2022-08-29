package com.tokopedia.createpost.producttag.model

import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.SearchHeaderUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
data class PagedGlobalSearchShopResponse(
    val totalShop: Int,
    val pagedData: PagedDataUiModel<ShopUiModel>,
    val header: SearchHeaderUiModel,
)