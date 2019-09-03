package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageTransactionModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageItemModel

class GetSortListHomePageUseCase {

    fun getSortEmptyList() : List<DigitalHomePageItemModel>{
        return listOf(
                DigitalHomePageBannerModel(),
                DigitalHomePageTransactionModel(),
                DigitalHomePageCategoryModel()
        )
    }
}