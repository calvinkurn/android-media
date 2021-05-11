package com.tokopedia.tokomart.searchcategory.presentation.model.util

import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView

object DummyDataViewGenerator {

    fun generateBannerDataView(): BannerDataView {
        return BannerDataView(
                applink = "tokopedia://digital/form?category_id=6&menu_id=4&template=voucher",
                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_333f5898-15e2-453c-afaa-2aadffbfba69.png"
        )
    }
}