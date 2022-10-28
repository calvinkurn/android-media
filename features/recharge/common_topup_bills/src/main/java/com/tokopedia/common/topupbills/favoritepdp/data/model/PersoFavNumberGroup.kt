package com.tokopedia.common.topupbills.favoritepdp.data.model

import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData

data class PersoFavNumberGroup(
    var favoriteNumberChips: TopupBillsPersoFavNumberData = PersoFavNumberChipsData(),
    var favoriteNumberList: TopupBillsPersoFavNumberData = PersoFavNumberListData(),
    var favoriteNumberPrefill: TopupBillsPersoFavNumberData = PersoFavNumberPrefillData()
)