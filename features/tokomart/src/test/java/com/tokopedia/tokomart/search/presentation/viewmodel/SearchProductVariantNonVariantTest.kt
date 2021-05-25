package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchProductVariantNonVariantTest: SearchTestFixtures() {

    @Test
    fun `test product item with non variant configuration`() {
        val searchModel = "search/first-page-products-variant-and-non-variant.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!
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
        val searchModel = "search/first-page-products-variant-and-non-variant.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!
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