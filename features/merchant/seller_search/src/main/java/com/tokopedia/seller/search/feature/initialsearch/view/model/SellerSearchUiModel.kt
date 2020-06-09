package com.tokopedia.seller.search.feature.initialsearch.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.model.feature.ItemFeatureSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.history.ItemHistorySearchUiModel

data class SellerSearchUiModel(
        var itemHistorySearchList: List<ItemHistorySearchUiModel> = listOf(),
        var itemFeatureSearchUiModel: List<ItemFeatureSearchUiModel>
)