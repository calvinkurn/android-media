package com.tokopedia.browse.homepage.domain.mapper

import com.tokopedia.browse.homepage.data.entity.DigitalBrowseCategoryRowEntity
import com.tokopedia.browse.homepage.data.entity.DigitalBrowseMarketplaceData
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 07/09/18.
 */

class ServiceViewModelMapper @Inject
constructor() {

    fun transform(data: DigitalBrowseMarketplaceData): DigitalBrowseServiceViewModel {
        var titleIndex = 0
        val returnData = DigitalBrowseServiceViewModel()

        val categoryViewModelList = ArrayList<DigitalBrowseServiceCategoryViewModel>()
        val titleMap = HashMap<String, IndexPositionModel>()

        for (row in data.categoryGroups!!.dynamicHomeCategoryGroupEntities!!) {
            val indexPositionModel = IndexPositionModel()
            indexPositionModel.indexPositionInTab = titleIndex
            indexPositionModel.indexPositionInList = categoryViewModelList.size
            titleMap.put(row.title, indexPositionModel)

            val returnRow = DigitalBrowseServiceCategoryViewModel()

            returnRow.id = row.id
            returnRow.name = row.title
            returnRow.isTitle = true

            categoryViewModelList.add(returnRow)
            categoryViewModelList.addAll(transform(row.categoryRow))

            titleIndex++
        }

        returnData.categoryViewModelList = categoryViewModelList
        returnData.titleMap = titleMap
        return returnData
    }

    private fun transform(categoryRow: List<DigitalBrowseCategoryRowEntity>?): List<DigitalBrowseServiceCategoryViewModel> {
        val returnData = ArrayList<DigitalBrowseServiceCategoryViewModel>()

        for (row in categoryRow!!) {
            val data = DigitalBrowseServiceCategoryViewModel()
            data.id = row.id
            data.name = row.name
            data.appLinks = row.appLinks
            data.categoryId = row.categoryId
            data.categoryLabel = row.categoryLabel
            data.imageUrl = row.imageUrl
            data.type = row.type
            data.url = row.url
            data.isTitle = false

            returnData.add(data)
        }

        return returnData
    }
}
