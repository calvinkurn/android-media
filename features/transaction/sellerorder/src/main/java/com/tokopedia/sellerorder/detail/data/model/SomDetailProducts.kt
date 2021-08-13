package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.sellerorder.detail.presentation.model.BaseProductUiModel

/**
 * Created by fwidjaja on 2019-10-04.
 */
data class SomDetailProducts (
        val listProducts: List<BaseProductUiModel> = listOf(),
        val isTopAds: Boolean = false,
        val isBroadcastChat: Boolean = false
)