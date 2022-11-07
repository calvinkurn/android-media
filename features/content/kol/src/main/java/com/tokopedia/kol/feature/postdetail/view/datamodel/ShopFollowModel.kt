package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 03/08/22.
 */
data class ShopFollowModel(
    val rowNumber: Int,
    val action: ShopFollowAction,
    val isFollowedFromRSRestrictionBottomSheet: Boolean = false
)
