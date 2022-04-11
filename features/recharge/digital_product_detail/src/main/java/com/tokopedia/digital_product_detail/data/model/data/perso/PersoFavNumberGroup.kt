package com.tokopedia.digital_product_detail.data.model.data.perso

import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData

data class PersoFavNumberGroup(
    var favoriteNumberChips: TopupBillsPersoFavNumberData = PersoFavNumberChipsData(),
    var favoriteNumberList: TopupBillsPersoFavNumberData = PersoFavNumberListData(),
    var favoriteNumberPrefill: TopupBillsPersoFavNumberData = PersoFavNumberPrefillData()
)