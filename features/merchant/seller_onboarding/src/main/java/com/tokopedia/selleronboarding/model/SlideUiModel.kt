package com.tokopedia.selleronboarding.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 11/04/20
 */

data class SlideUiModel(
        val headerText: String,
        val drawableUrl: String,
        val impressHolder: ImpressHolder = ImpressHolder()
)