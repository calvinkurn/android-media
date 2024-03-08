package com.tokopedia.bmsm_widget.domain.mapper

import com.tokopedia.bmsm_widget.domain.model.GwpGiftResponse
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 06/12/23.
 */

class GwpGiftListMapper @Inject constructor() {

    fun mapToUiModel(response: GwpGiftResponse): List<ProductGiftUiModel> {
        return emptyList()
    }
}