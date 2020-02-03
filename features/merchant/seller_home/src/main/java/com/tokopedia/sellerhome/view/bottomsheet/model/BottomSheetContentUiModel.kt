package com.tokopedia.sellerhome.view.bottomsheet.model

import com.tokopedia.sellerhome.view.bottomsheet.adapter.BottomSheetAdapterTypeFactory

data class BottomSheetContentUiModel(val content: String) : BaseBottomSheetUiModel {
    override fun type(typeFactory: BottomSheetAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}