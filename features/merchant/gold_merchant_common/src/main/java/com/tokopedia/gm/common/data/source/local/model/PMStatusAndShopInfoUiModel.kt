package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 25/03/21
 */

data class PMStatusAndShopInfoUiModel(
        val pmStatus: PMStatusUiModel,
        val shopInfo: PMShopInfoUiModel,
        val isFreeShippingEnabled: Boolean = false
)