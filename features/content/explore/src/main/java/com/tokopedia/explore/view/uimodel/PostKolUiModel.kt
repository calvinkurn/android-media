package com.tokopedia.explore.view.uimodel

import com.tokopedia.explore.domain.entity.PostKol
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by jegul on 2019-08-29.
 */
data class PostKolUiModel(
        val postKol: PostKol,
        val impressHolder: ImpressHolder = ImpressHolder()
)