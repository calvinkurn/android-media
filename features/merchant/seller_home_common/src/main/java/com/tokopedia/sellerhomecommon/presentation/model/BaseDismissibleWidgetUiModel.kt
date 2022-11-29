package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.common.DismissibleState

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseDismissibleWidgetUiModel<T : BaseDataUiModel> : BaseWidgetUiModel<T> {
    val isDismissible: Boolean
    var dismissibleState: DismissibleState
    val dismissToken: String
    var shouldShowDismissalTimer: Boolean
}
