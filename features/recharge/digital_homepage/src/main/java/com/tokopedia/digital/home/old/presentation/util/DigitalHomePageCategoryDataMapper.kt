package com.tokopedia.digital.home.old.presentation.util

import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.model.DigitalHomePageItemModel

class DigitalHomePageCategoryDataMapper {

    companion object {
        fun mapCategoryData(data: DigitalHomePageItemModel): List<DigitalHomePageCategoryModel.Submenu>? {
            if (data is DigitalHomePageCategoryModel) {
                val categoryList = mutableListOf<DigitalHomePageCategoryModel.Submenu>()
                for (subtitle in data.listSubtitle) {
                    for (submenu in subtitle.submenu) {
                        categoryList.add(submenu)
                    }
                }
                return categoryList
            }
            return null
        }
    }

}