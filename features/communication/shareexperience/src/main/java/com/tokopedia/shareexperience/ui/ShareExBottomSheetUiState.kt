package com.tokopedia.shareexperience.ui

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

data class ShareExBottomSheetUiState(
    val uiModelList: List<Visitable<in ShareExTypeFactory>>? = null
)
