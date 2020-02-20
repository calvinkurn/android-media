package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageCategoryFeaturedEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageMyUmrahEntity
import com.tokopedia.salam.umrah.homepage.data.UmrahHomepageModel

/**
 * @author by firman on 28/10/19
 */
class UmrahHomepageEmptyDataUseCase {

    fun requestEmptyViewModels(isLoadedFromCloud: Boolean): List<UmrahHomepageModel> {
        val umrahSearchParam = UmrahSearchParameterEntity()
        umrahSearchParam.isLoadFromCloud = isLoadedFromCloud

        val umrahDreamFund = UmrahHomepageMyUmrahEntity()
        umrahDreamFund.isLoadFromCloud = isLoadedFromCloud

        val umrahCategory = UmrahHomepageCategoryEntity()
        umrahCategory.isLoadFromCloud = isLoadedFromCloud

        val umrahCategoryFeatured = UmrahHomepageCategoryFeaturedEntity()
        umrahCategoryFeatured.isLoadFromCloud = isLoadedFromCloud

        return listOf(umrahSearchParam,
                umrahDreamFund,
                umrahCategory,
                umrahCategoryFeatured
        )
    }
}