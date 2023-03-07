package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.presentation.viewmodel.CategoryTestFixtures
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Callback
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class CategoryAffiliateTest: CategoryTestFixtures(), Callback {

    private val categoryModelJSON = "category/first-page-products-variant-and-non-variant.json"
    private val categoryModel = categoryModelJSON.jsonToObject<CategoryModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowCategoryViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get category first page use case will be successful`(categoryModel)
    }

    @Test
    fun `when add to cart should call init affiliate cookie`() {
        addToCartTestHelper.`test add to cart success`()

        `assert affiliate cookie called`()
    }

    @Test
    fun `given init affiliate cookie error when add to cart should not nothing`() {
        `given init affiliate cookie error`()

        addToCartTestHelper.`test add to cart success`()
    }

    @Test
    fun `when create affiliate link should call affiliate helper createAffiliateLink`() {
        val url = "tokopedia://now/category/1"

        `when create affiliate link`(url)

        `assert createAffiliateLink called`(url)
    }

    private fun `given init affiliate cookie error`() {
        coEvery { affiliateHelper.initCookie(anyString(), anyString(), any()) } throws NullPointerException()
    }

    private fun `when create affiliate link`(url: String) {
        tokoNowCategoryViewModel.createAffiliateLink(url)
    }

    private fun `assert affiliate cookie called`() {
        coVerify { affiliateHelper.initCookie(anyString(), anyString(), any()) }
    }

    private fun `assert createAffiliateLink called`(url: String) {
        coVerify { affiliateHelper.createAffiliateLink(url, anyString()) }
    }
}
