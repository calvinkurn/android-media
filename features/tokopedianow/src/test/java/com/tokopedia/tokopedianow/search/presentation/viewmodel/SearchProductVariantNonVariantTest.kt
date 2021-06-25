package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchProductVariantNonVariantTest: SearchTestFixtures() {

    private val searchModel =
            "search/first-page-products-variant-and-non-variant.json".jsonToObject<SearchModel>()

    @Test
    fun `test product item with non variant configuration`() {
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val productItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        val productNonVariant = searchModel.searchProduct.data.productList[1]
        val productItemDataViewNonVariant = productItemDataViewList[1]
        `Then verify product item with non variant configuration`(
                productNonVariant,
                productItemDataViewNonVariant
        )
    }

    private fun `Then verify product item with non variant configuration`(
            productNonVariant: AceSearchProductModel.Product,
            productItemDataViewNonVariant: ProductItemDataView,
    ) {
        assertThat(productItemDataViewNonVariant.nonVariantATC, notNullValue())
        assertThat(productItemDataViewNonVariant.variantATC, nullValue())

        val nonVariantATC = productItemDataViewNonVariant.nonVariantATC!!
        assertThat(nonVariantATC.minQuantity, shouldBe(productNonVariant.minOrder))
        assertThat(nonVariantATC.maxQuantity, shouldBe(productNonVariant.stock))
    }

    @Test
    fun `test product item with variant configuration`() {
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val productItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        val productItemDataViewVariant = productItemDataViewList[2]
        `Then verify product item with variant configuration`(productItemDataViewVariant)
    }

    private fun `Then verify product item with variant configuration`(
            productItemDataViewVariant: ProductItemDataView,
    ) {
        assertThat(productItemDataViewVariant.variantATC, notNullValue())
        assertThat(productItemDataViewVariant.nonVariantATC, nullValue())
    }
}