package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class UniversalInboxTopadsHeadlineUiModel(
    var cpmModel: CpmModel? = null,
    var topadsHeadLinePage: Int = Int.ZERO,
    var index: Int = Int.ZERO
) {
    val impressHolder: ImpressHolder = ImpressHolder()
}
