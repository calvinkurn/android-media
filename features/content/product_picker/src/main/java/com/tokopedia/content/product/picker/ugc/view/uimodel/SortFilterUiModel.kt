package com.tokopedia.content.product.picker.ugc.view.uimodel

import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on May 17, 2022
 */
data class SortFilterUiModel(
    val data: DynamicFilterModel,
    val state: PagedState
) {

    companion object {
        val Empty: SortFilterUiModel
            get() = SortFilterUiModel(
                data = DynamicFilterModel(),
                state = PagedState.Unknown,
            )
    }
}
