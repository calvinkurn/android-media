package com.tokopedia.createpost.producttag.model

import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.TickerUiModel

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class PagedGlobalSearchProductResponse(
    val pagedData: PagedDataUiModel<ProductUiModel>,
    val suggestion: String,
    val ticker: TickerUiModel,
)