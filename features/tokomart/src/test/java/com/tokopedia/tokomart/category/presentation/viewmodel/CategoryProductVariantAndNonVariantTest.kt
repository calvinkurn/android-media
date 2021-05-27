package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryProductVariantAndNonVariantTest: CategoryTestFixtures() {

    private val categoryModel =
            "category/first-page-products-variant-and-non-variant.json".jsonToObject<CategoryModel>()

    @Test
    fun `test product item with non variant configuration`() {
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!
        val productItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        val productNonVariant = categoryModel.searchProduct.data.productList[1]
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
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!
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