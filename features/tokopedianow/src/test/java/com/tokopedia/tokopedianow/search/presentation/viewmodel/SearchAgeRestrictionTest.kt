package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import org.junit.Assert
import org.junit.Test

class SearchAgeRestrictionTest: SearchTestFixtures() {

    @Test
    fun `when on view created should give query safe model`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given search view model`(mapOf())

        `Given get search first page use case will be successful`(searchModel)

        val localCacheModel = LocalCacheModel(service_type = "2h", shop_id = "12121", warehouse_id = "33333")

        tokoNowSearchViewModel.mockSuperClassField("chooseAddressData", localCacheModel)

        `When view created`()

        `Then verify the data`(QuerySafeModel(isQuerySafe = true, "33333"))
    }

    private fun `Then verify the data`(data: QuerySafeModel) {
        Assert.assertEquals(tokoNowSearchViewModel.querySafeLiveData.value, data)
    }
}