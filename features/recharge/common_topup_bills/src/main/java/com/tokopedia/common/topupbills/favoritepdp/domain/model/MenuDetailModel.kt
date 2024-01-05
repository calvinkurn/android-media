package com.tokopedia.common.topupbills.favoritepdp.domain.model

import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsCatalog
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso

data class MenuDetailModel(
    val catalog: TopupBillsCatalog = TopupBillsCatalog(),
    val userPerso: TopupBillsUserPerso = TopupBillsUserPerso(),
    val tickers: List<TopupBillsTicker> = listOf(),
    val banners: List<TopupBillsBanner> = listOf(),
    val multiCheckoutButtons: List<MultiCheckoutButtons> = listOf(),
)
