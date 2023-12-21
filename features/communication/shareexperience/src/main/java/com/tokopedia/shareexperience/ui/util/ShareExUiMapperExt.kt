package com.tokopedia.shareexperience.ui.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel

fun ShareExBottomSheetModel.map(): List<Visitable<in ShareExTypeFactory>> {
    val result = arrayListOf<Visitable<in ShareExTypeFactory>>()

    // Subtitle UI
    val subtitleUiModel = ShareExSubtitleUiModel(this.subtitle)
    result.add(subtitleUiModel)

    // Chip UI

    //
    return result
}
