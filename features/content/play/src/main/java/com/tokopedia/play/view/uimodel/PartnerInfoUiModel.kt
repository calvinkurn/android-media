package com.tokopedia.play.view.uimodel

import com.tokopedia.play.ui.toolbar.model.PartnerType

/**
 * Created by jegul on 18/12/19
 */
data class PartnerInfoUiModel(
        val id: Long,
        val name: String,
        val type: PartnerType,
        val isFollowed: Boolean
)