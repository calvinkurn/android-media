package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel

interface OlpAdapterTypeFactory {
    fun type(type: OfferInfoForBuyerUiModel): Int
}
