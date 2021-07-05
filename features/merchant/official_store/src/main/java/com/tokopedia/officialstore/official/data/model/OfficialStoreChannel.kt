package com.tokopedia.officialstore.official.data.model

import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.productcard.ProductCardModel

/**
 * Created by Lukas on 04/11/20.
 */

data class OfficialStoreChannel (
        val channel: Channel,
        val productCardModels: List<ProductCardModel> = listOf(),
        val height: Int = 0
)