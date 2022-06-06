package com.tokopedia.createpost.producttag.model

import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.SearchHeaderUiModel
import com.tokopedia.createpost.producttag.view.uimodel.TickerUiModel

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class PagedGlobalSearchProductResponse(
    val pagedData: PagedDataUiModel<ProductUiModel>,
    val header: SearchHeaderUiModel,
    val suggestion: String,
    val ticker: TickerUiModel,
)