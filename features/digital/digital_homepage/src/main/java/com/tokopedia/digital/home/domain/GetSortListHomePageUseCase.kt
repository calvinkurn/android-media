package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageTransactionModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel

class GetSortListHomePageUseCase {

    fun getSortEmptyList(loadFromCloud: Boolean): List<DigitalHomePageItemModel>{

        val homeBanner = DigitalHomePageBannerModel()
        homeBanner.isLoadFromCloud = loadFromCloud

        val transaction = DigitalHomePageTransactionModel()
        transaction.isLoadFromCloud = loadFromCloud
        transaction.isLoaded = true

        val category = DigitalHomePageCategoryModel()
        category.isLoadFromCloud = loadFromCloud

        return listOf(homeBanner,
                transaction,
                category
        )
    }
}