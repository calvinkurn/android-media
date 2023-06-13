package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
data class LikeContentModel(
    val rowNumber: Int,
    val action: ContentLikeAction,
)