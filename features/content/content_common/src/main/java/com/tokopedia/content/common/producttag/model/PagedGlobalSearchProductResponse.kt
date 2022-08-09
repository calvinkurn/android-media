package com.tokopedia.content.common.producttag.model

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.*

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class PagedGlobalSearchProductResponse(
    val pagedData: PagedDataUiModel<ProductUiModel>,
    val header: SearchHeaderUiModel,
    val suggestion: SuggestionUiModel,
    val ticker: TickerUiModel,
)