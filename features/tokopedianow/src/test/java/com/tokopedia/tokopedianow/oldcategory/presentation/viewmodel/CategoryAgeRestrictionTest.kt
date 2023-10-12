package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import org.junit.Assert
import org.junit.Test

class CategoryAgeRestrictionTest: CategoryTestFixtures() {

    @Test
    fun `when on view created should give query safe model`() {
        val categoryModel = "oldcategory/first-page-8-products.json"
            .jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)

        val localCacheModel = LocalCacheModel(service_type = "2h", shop_id = "12121", warehouse_id = "33333")

        tokoNowCategoryViewModel.mockSuperClassField("chooseAddressData", localCacheModel)

        `When view created`()

        `Then verify the data`(QuerySafeModel(isQuerySafe = true, "33333"))
    }

    private fun `Then verify the data`(data: QuerySafeModel) {
        Assert.assertEquals(tokoNowCategoryViewModel.querySafeLiveData.value, data)
    }
}
