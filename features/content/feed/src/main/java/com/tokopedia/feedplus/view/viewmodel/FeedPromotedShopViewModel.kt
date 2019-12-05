package com.tokopedia.feedplus.view.viewmodel

import com.tokopedia.topads.sdk.domain.model.Data

/**
 * @author by yoasfs on 2019-12-05
 */
data class FeedPromotedShopViewModel(
        var promotedShopViewModel: Data = Data(),
        var adapterPosition: Int = 0,
        var resultString: String = ""
)