package com.tokopedia.salam.umrah.homepage.presentation.usecase

import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.homepage.data.*

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

        val umrahBanner = UmrahHomepageBannerEntity()
        umrahBanner.isLoadFromCloud = isLoadedFromCloud

        val umrahPartnerTravel = UmrahTravelAgentsEntity()
        umrahPartnerTravel.isLoadFromCloud = isLoadedFromCloud

        return listOf(umrahSearchParam,
                umrahBanner,
                umrahDreamFund,
                umrahCategory,
                umrahCategoryFeatured,
                umrahPartnerTravel
        )
    }
}