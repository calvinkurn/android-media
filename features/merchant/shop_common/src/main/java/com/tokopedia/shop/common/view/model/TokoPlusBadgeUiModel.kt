package com.tokopedia.shop.common.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 04/07/22.
 */

data class TokoPlusBadgeUiModel(
    val freeShipping: BadgeUiModel = BadgeUiModel(),
    val tokoPlus: BadgeUiModel = BadgeUiModel()
)
data class BadgeUiModel(
    val status: Boolean = false,
    val badgeUrl: String = String.EMPTY
)