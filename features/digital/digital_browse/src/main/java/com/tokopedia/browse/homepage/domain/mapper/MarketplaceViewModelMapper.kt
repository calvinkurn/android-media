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
        val returnData = DigitalBrowseMarketplaceViewModel()

        returnData.rowViewModelList = transformRow(data.categoryGroups!!
                .dynamicHomeCategoryGroupEntities!![0].categoryRow)
        returnData.popularBrandsList = transformPopular(data.popularBrandDatas)

        return returnData
    }

    fun transformRow(categoryRowEntityList: List<DigitalBrowseCategoryRowEntity>?): List<DigitalBrowseRowViewModel> {
        val returnDataList = ArrayList<DigitalBrowseRowViewModel>()

        for (row in categoryRowEntityList!!) {
            val returnRow = DigitalBrowseRowViewModel()

            returnRow.appLinks = row.appLinks
            returnRow.categoryId = row.categoryId
            returnRow.categoryLabel = row.categoryLabel
            returnRow.id = row.id
            returnRow.imageUrl = row.imageUrl
            returnRow.name = row.name
            returnRow.type = row.type
            returnRow.url = row.url

            returnDataList.add(returnRow)
        }

        return returnDataList
    }

    fun transformPopular(popularBrandsEntityList: List<DigitalBrowsePopularBrand>?): List<DigitalBrowsePopularBrandsViewModel> {
        val returnDataList = ArrayList<DigitalBrowsePopularBrandsViewModel>()

        for (row in popularBrandsEntityList!!) {
            val returnRow = DigitalBrowsePopularBrandsViewModel()

            returnRow.id = row.id.toLong()
            returnRow.logoUrl = row.logoUrl
            returnRow.name = row.name
            returnRow.isNew = row.isNew
            returnRow.url = row.url

            returnDataList.add(returnRow)
        }

        return returnDataList
    }
}
