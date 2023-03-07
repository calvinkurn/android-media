package com.tokopedia.tokopedianow.search.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView

object CategoryJumperMapper {
    fun createCategoryJumperDataView(searchCategoryJumper: SearchCategoryJumperModel.SearchCategoryJumperData?, chooseAddressData: LocalCacheModel?): CategoryJumperDataView {
        val categoryJumperItemList =
            searchCategoryJumper
                ?.getJumperItemList()
                ?.map(this::mapToCategoryJumperItem)
                ?: listOf()

        return CategoryJumperDataView(
            title = searchCategoryJumper?.getTitle().orEmpty(),
            itemList = categoryJumperItemList,
            serviceType = chooseAddressData?.service_type.orEmpty()
        )
    }

    private fun mapToCategoryJumperItem(jumperData: SearchCategoryJumperModel.JumperData) =
        CategoryJumperDataView.Item(
            title = jumperData.title,
            applink = jumperData.applink,
        )

}
