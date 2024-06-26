package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Companion.AddToCartTestObject.addToCartQty
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SearchAffiliateTest : SearchTestFixtures(), Callback {

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
            tokoNowSearchViewModel,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartListSimplifiedUseCase,
            userSession,
            this
        )
    }

    override fun `Given first page API will be successful`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
    }

    @Test
    fun `when add to cart should call check atc affiliate cookie`() {
        addToCartTestHelper.`test add to cart success`()

        val productItemList =
            tokoNowSearchViewModel.visitableListLiveData.value!!.getProductItemList()
        val productItemDataViewToATC = productItemList[0]
        val productCardModel = productItemDataViewToATC.productCardModel

        val productId = productCardModel.productId
        val shopId = productItemDataViewToATC.shop.id
        val stock = productCardModel.availableStock
        val isVariant = productCardModel.isVariant

        val expectedAffiliateData = NowAffiliateAtcData(
            productId = productId,
            shopId = shopId,
            stock = stock,
            isVariant = isVariant,
            newQuantity = addToCartQty,
            currentQuantity = 0
        )

        `assert check atc affiliate cookie called`(expectedAffiliateData)
    }

    @Test
    fun `when check atc affiliate cookie error should do nothing`() {
        `given check atc affiliate cookie error`()

        addToCartTestHelper.`test add to cart success`()
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

    private fun List<Visitable<*>>.getProductItemList() = filterIsInstance<ProductItemDataView>()

    private fun `given init affiliate cookie error`() {
        coEvery {
            affiliateService.initAffiliateCookie(
                anyString(),
                anyString()
            )
        } throws NullPointerException()
    }

    private fun `given check atc affiliate cookie error`() {
        coEvery { affiliateService.checkAtcAffiliateCookie(any()) } throws NullPointerException()
    }

    private fun `when create affiliate link`(url: String) {
        tokoNowSearchViewModel.createAffiliateLink(url)
    }

    private fun `assert check atc affiliate cookie called`(expectedData: NowAffiliateAtcData) {
        coVerify { affiliateService.checkAtcAffiliateCookie(expectedData) }
    }

    private fun `assert createAffiliateLink called`(url: String) {
        coVerify { affiliateService.createAffiliateLink(url) }
    }
}
