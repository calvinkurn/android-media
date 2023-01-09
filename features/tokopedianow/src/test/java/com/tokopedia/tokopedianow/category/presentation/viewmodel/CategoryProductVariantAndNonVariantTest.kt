package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
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

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val productItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        val productNonVariant = categoryModel.searchProduct.data.productList[1]
        val productItemDataViewNonVariant = productItemDataViewList[1]

        `Then verify product item with non variant configuration`(
            productNonVariant,
            productItemDataViewNonVariant
        )
    }

    private fun `Then verify product item with non variant configuration`(
        product: AceSearchProductModel.Product,
        productItemDataView: ProductItemDataView,
    ) {
        assertThat(productItemDataView.productCardModel.productId, shouldBe(product.id))
        assertThat(productItemDataView.productCardModel.minOrder, shouldBe(product.minOrder))
        assertThat(productItemDataView.productCardModel.maxOrder, shouldBe(product.maxOrder))
    }
}
