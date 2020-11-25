package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.adapter.BottomSheetAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 27/05/20
 */

data class BottomSheetListItemUiModel(
        val title: String, val
        description: String
) : BaseBottomSheetUiModel {

    override fun type(typeFactory: BottomSheetAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}