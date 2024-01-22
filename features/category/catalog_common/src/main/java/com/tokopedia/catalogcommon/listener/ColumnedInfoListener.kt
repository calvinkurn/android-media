package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel

interface ColumnedInfoListener {
    fun onColumnedInfoSeeMoreClicked(
        sectionTitle: String,
        columnData: List<ColumnedInfoUiModel.ColumnData>
    )

    fun onColumnedInfoImpression(columnedInfoUiModel: ColumnedInfoUiModel)
}
