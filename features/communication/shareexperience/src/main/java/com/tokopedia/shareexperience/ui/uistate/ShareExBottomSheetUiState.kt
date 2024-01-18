package com.tokopedia.shareexperience.ui.uistate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

data class ShareExBottomSheetUiState(
    val title: String = "",
    val uiModelList: List<Visitable<in ShareExTypeFactory>>? = null
)
