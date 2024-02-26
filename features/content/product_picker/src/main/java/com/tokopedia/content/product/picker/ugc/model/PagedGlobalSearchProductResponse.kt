package com.tokopedia.content.product.picker.ugc.model

import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedDataUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.SearchHeaderUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.SuggestionUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.TickerUiModel

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class PagedGlobalSearchProductResponse(
    val pagedData: PagedDataUiModel<ProductUiModel>,
    val header: SearchHeaderUiModel,
    val suggestion: SuggestionUiModel,
    val ticker: TickerUiModel,
)
