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
        val categoryViewModelList = ArrayList<DigitalBrowseServiceCategoryViewModel>()
        val titleMap = HashMap<String, IndexPositionModel>()

        data.categoryGroups!!.dynamicHomeCategoryGroupEntities!!.map {
            titleMap.put(it.title, IndexPositionModel(titleIndex, categoryViewModelList.size))
            categoryViewModelList.add(DigitalBrowseServiceCategoryViewModel(id = it.id, name = it.title, isTitle = true))
            categoryViewModelList.addAll(transform(it.categoryRow))

            titleIndex++
        }

        return DigitalBrowseServiceViewModel(categoryViewModelList, titleMap)
    }

    private fun transform(categoryRow: List<DigitalBrowseCategoryRowEntity>?): List<DigitalBrowseServiceCategoryViewModel> {
        val returnData = ArrayList<DigitalBrowseServiceCategoryViewModel>()

        categoryRow!!.map {
            returnData.add(DigitalBrowseServiceCategoryViewModel(it.id, it.name, it.url, it.imageUrl,
                    it.type, it.categoryId, it.appLinks, it.categoryLabel, false))
        }

        return returnData
    }
}
