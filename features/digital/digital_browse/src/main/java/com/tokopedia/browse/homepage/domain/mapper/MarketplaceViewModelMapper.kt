package com.tokopedia.browse.homepage.domain.mapper

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryRowEntity
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.browse.homepage.data.entity.DigitalBrowsePopularBrand
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 05/09/18.
 */

class MarketplaceViewModelMapper @Inject
constructor() {

    fun transform(data: DigitalBrowseMarketplaceData): DigitalBrowseMarketplaceViewModel {
        return DigitalBrowseMarketplaceViewModel(transformPopular(data.popularBrandDatas),
                transformRow(data.categoryGroups!!.dynamicHomeCategoryGroupEntities!![0].categoryRow))
    }

    private fun transformRow(categoryRowEntityList: List<DigitalBrowseCategoryRowEntity>?): List<DigitalBrowseRowViewModel> {
        val returnDataList = ArrayList<DigitalBrowseRowViewModel>()

        categoryRowEntityList!!.map {
            returnDataList.add(DigitalBrowseRowViewModel(it.id, it.name, it.url, it.imageUrl,
                    it.type, it.categoryId, it.appLinks, it.categoryLabel))
        }

        return returnDataList
    }

    private fun transformPopular(popularBrandsEntityList: List<DigitalBrowsePopularBrand>?): List<DigitalBrowsePopularBrandsViewModel> {
        val returnDataList = ArrayList<DigitalBrowsePopularBrandsViewModel>()

        popularBrandsEntityList!!.map {
            returnDataList.add(DigitalBrowsePopularBrandsViewModel(it.id.toLong(), it.name, it.isNew,
                    it.logoUrl, it.url))
        }

        return returnDataList
    }
}
