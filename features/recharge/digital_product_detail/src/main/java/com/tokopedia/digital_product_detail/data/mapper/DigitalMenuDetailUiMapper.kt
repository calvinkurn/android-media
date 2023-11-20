package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import javax.inject.Inject

@DigitalPDPScope
class DigitalMenuDetailUiMapper @Inject constructor() {

    fun mapMenuDetailModel(data: TopupBillsMenuDetail): MenuDetailModel {
        return MenuDetailModel(
            catalog = data.catalog,
            userPerso = data.userPerso,
            tickers = data.tickers,
            banners = data.banners,
            multiCheckoutButtons = data.multiCheckoutButtons
        )
    }
}
