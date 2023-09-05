package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import com.tokopedia.buy_more_get_more.olp.domain.entity.EmptyStateUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel

interface OlpAdapterTypeFactory {
    fun type(type: OfferInfoForBuyerUiModel): Int

    fun type(type: OfferProductSortingUiModel): Int

    fun type(type: OfferProductListUiModel.Product): Int

    fun type(type: EmptyStateUiModel): Int
}
