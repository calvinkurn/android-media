package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.*
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY_TYPE
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.Product
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.topads.sdk.domain.model.Data
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

/**
 * Test Data:
1. Has rating, has review, has integrity label group, has count_sold, has shop rating
2. No rating, no review, has integrity label group, has count_sold, has shop rating
3. No rating, no review, no integrity label group, no count_sold, has shop rating
4. No rating, no review, no integrity label group, has count_sold, has shop rating
5. Has rating, has review, no integrity label group, has count_sold, has shop rating
6. Has rating, has review, no integrity label group, has count_sold, no shop rating
7. No rating, no review, no integrity label group, no count_sold, no shop rating
8. Has rating, has review, no integrity label group, no count_sold, no shop rating
 * */
private const val shopRatingJSON = "searchproduct/shoprating/shop-rating.json"

internal class SearchProductShopRatingTest: ProductListPresenterTestFixtures() {

    class ExpectedData {
        var rating = 0
        var countReview = 0
        var labelIntegrity: ProductLabelGroup? = null
        var shopRating = ""
        var isShopRatingYellow = false
    }

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val searchProductModel = shopRatingJSON.jsonToObject<SearchProductModel>()

    @Test
    fun `Shop Rating Variant A`() {
        `Execute Test Shop Rating With Variant`(AB_TEST_SHOP_RATING_VARIANT_A)

        `Then assert product list for variant A`()
    }

    private fun `Then assert product list for variant A`() {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemViewModel>()
        val organicProductList = searchProductModel.searchProduct.data.productList
        val topAdsDataList = searchProductModel.topAdsModel.data

        // Organic
        productItemViewModelList[0].verifyVariantA_ShowRatingReviewOnly(organicProductList[0])
        productItemViewModelList[1].verifyVariantA_ShowIntegrityOnly(organicProductList[1])
        productItemViewModelList[2].verifyVariantA_ShowShopRatingOnly(organicProductList[2])
        productItemViewModelList[3].verifyVariantA_ShowShopRatingOnly(organicProductList[3])
        productItemViewModelList[4].verifyVariantA_ShowRatingReviewOnly(organicProductList[4])
        productItemViewModelList[5].verifyVariantA_ShowRatingReviewOnly(organicProductList[5])
        productItemViewModelList[6].verifyShowNone()
        productItemViewModelList[7].verifyVariantA_ShowRatingReviewOnly(organicProductList[7])

        // Top Ads
        productItemViewModelList[8].verifyVariantA_ShowRatingReviewOnly(topAdsDataList[0])
        productItemViewModelList[9].verifyVariantA_ShowIntegrityOnly(topAdsDataList[1])
        productItemViewModelList[10].verifyVariantA_ShowShopRatingOnly(topAdsDataList[2])
        productItemViewModelList[11].verifyVariantA_ShowShopRatingOnly(topAdsDataList[3])
        productItemViewModelList[12].verifyVariantA_ShowRatingReviewOnly(topAdsDataList[4])
        productItemViewModelList[13].verifyVariantA_ShowRatingReviewOnly(topAdsDataList[5])
        productItemViewModelList[14].verifyShowNone()
        productItemViewModelList[15].verifyVariantA_ShowRatingReviewOnly(topAdsDataList[7])
    }

    @Test
    fun `Shop Rating Variant B`() {
        `Execute Test Shop Rating With Variant`(AB_TEST_SHOP_RATING_VARIANT_B)

        `Then assert product list for variant B`()
    }

    private fun `Then assert product list for variant B`() {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemViewModel>()
        val productList = searchProductModel.searchProduct.data.productList
        val topAdsDataList = searchProductModel.topAdsModel.data

        // Organic
        productItemViewModelList[0].verifyVariantB_ShowIntegrityAndShopRating(productList[0])
        productItemViewModelList[1].verifyVariantB_ShowIntegrityAndShopRating(productList[1])
        productItemViewModelList[2].verifyVariantB_ShowShopRatingOnly(productList[2])
        productItemViewModelList[3].verifyVariantB_ShowIntegrityAndShopRating(productList[3])
        productItemViewModelList[4].verifyVariantB_ShowIntegrityAndShopRating(productList[4])
        productItemViewModelList[5].verifyVariantB_ShowIntegrityOnly(productList[5])
        productItemViewModelList[6].verifyShowNone()
        productItemViewModelList[7].verifyShowNone()

        // Top Ads
        productItemViewModelList[8].verifyVariantB_ShowIntegrityAndShopRating(topAdsDataList[0])
        productItemViewModelList[9].verifyVariantB_ShowIntegrityAndShopRating(topAdsDataList[1])
        productItemViewModelList[10].verifyVariantB_ShowShopRatingOnly(topAdsDataList[2])
        productItemViewModelList[11].verifyVariantB_ShowIntegrityAndShopRating(topAdsDataList[3])
        productItemViewModelList[12].verifyVariantB_ShowIntegrityAndShopRating(topAdsDataList[4])
        productItemViewModelList[13].verifyVariantB_ShowIntegrityOnly(topAdsDataList[5])
        productItemViewModelList[14].verifyShowNone()
        productItemViewModelList[15].verifyShowNone()
    }

    @Test
    fun `Shop Rating Variant C`() {
        `Execute Test Shop Rating With Variant`(AB_TEST_SHOP_RATING_VARIANT_C)

        `Then assert product list for variant C`()
    }

    private fun `Then assert product list for variant C`() {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemViewModel>()
        val productList = searchProductModel.searchProduct.data.productList
        val topAdsDataList = searchProductModel.topAdsModel.data

        // Organic
        productItemViewModelList[0].verifyVariantC_ShowShopRatingOnly(productList[0])
        productItemViewModelList[1].verifyVariantC_ShowShopRatingOnly(productList[1])
        productItemViewModelList[2].verifyVariantC_ShowShopRatingOnly(productList[2])
        productItemViewModelList[3].verifyVariantC_ShowShopRatingOnly(productList[3])
        productItemViewModelList[4].verifyVariantC_ShowShopRatingOnly(productList[4])
        productItemViewModelList[5].verifyShowNone()
        productItemViewModelList[6].verifyShowNone()
        productItemViewModelList[7].verifyShowNone()

        // Top Ads
        productItemViewModelList[8].verifyVariantC_ShowShopRatingOnly(topAdsDataList[0])
        productItemViewModelList[9].verifyVariantC_ShowShopRatingOnly(topAdsDataList[1])
        productItemViewModelList[10].verifyVariantC_ShowShopRatingOnly(topAdsDataList[2])
        productItemViewModelList[11].verifyVariantC_ShowShopRatingOnly(topAdsDataList[3])
        productItemViewModelList[12].verifyVariantC_ShowShopRatingOnly(topAdsDataList[4])
        productItemViewModelList[13].verifyShowNone()
        productItemViewModelList[14].verifyShowNone()
        productItemViewModelList[15].verifyShowNone()
    }

    @Test
    fun `Shop Rating No Variant`() {
        `Execute Test Shop Rating With Variant`("")

        `Then assert product list for no variant`()
    }

    private fun `Then assert product list for no variant`() {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemViewModel>()
        val productList = searchProductModel.searchProduct.data.productList
        val topAdsDataList = searchProductModel.topAdsModel.data

        productList.forEachIndexed { index, productItem ->
            productItemViewModelList[index].verifyNoVariant_NoChangesOnShopRating(productItem)
        }

        topAdsDataList.forEachIndexed { index, topAdsData ->
            val topAdsIndex = index + productList.size
            productItemViewModelList[topAdsIndex].verifyNoVariant_NoChangesOnShopRating(topAdsData)
        }
    }

    private fun `Execute Test Shop Rating With Variant`(variant: String) {
        `Given Search Product API will return SearchProductModel`()
        `Given AB Test Shop Rating will return Variant`(variant)
        setUp()

        `When Load Data`()

        `Then verify and capture product list`()
    }

    private fun `Given AB Test Shop Rating will return Variant`(variant: String) {
        every { productListView.abTestRemoteConfig.getString(AB_TEST_SHOP_RATING) } returns variant
    }

    private fun `Given Search Product API will return SearchProductModel`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any> = mapOf()) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify and capture product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun ProductItemViewModel.verifyShopRatingDataForProductCard(expectedData: ExpectedData) {
        rating shouldBe expectedData.rating
        countReview shouldBe expectedData.countReview
        labelGroupList.assertLabelGroupListHasLabelIntegrity(expectedData.labelIntegrity)
        shopRating shouldBe expectedData.shopRating

        if (expectedData.shopRating.isNotEmpty())
            isShopRatingYellow shouldBe expectedData.isShopRatingYellow
    }

    private fun List<LabelGroupViewModel>.assertLabelGroupListHasLabelIntegrity(expectedLabelGroupIntegrity: ProductLabelGroup?) {
        val actualLabelGroupViewModelList = filter { it.position == LABEL_INTEGRITY }
        if (actualLabelGroupViewModelList.size > 1) {
            throw AssertionError("Only 1 Label Group $LABEL_INTEGRITY.")
        }

        val actualLabelGroupViewModel = actualLabelGroupViewModelList.getOrNull(0)

        if (expectedLabelGroupIntegrity == null && actualLabelGroupViewModel == null)
            return
        else if (expectedLabelGroupIntegrity == null && actualLabelGroupViewModel != null)
            throw AssertionError("Label Group $LABEL_INTEGRITY should not exists.")
        else if (expectedLabelGroupIntegrity != null && actualLabelGroupViewModel == null)
            throw AssertionError("Label Group $LABEL_INTEGRITY should exists as expected: $expectedLabelGroupIntegrity.")
        else if (expectedLabelGroupIntegrity != null && actualLabelGroupViewModel != null)
            actualLabelGroupViewModel.assertLabelIntegrity(expectedLabelGroupIntegrity)
    }

    private fun LabelGroupViewModel.assertLabelIntegrity(expectedLabelGroupIntegrity: ProductLabelGroup) {
        position shouldBe expectedLabelGroupIntegrity.position
        type shouldBe expectedLabelGroupIntegrity.type
        title shouldBe expectedLabelGroupIntegrity.title
    }

    private fun ProductItemViewModel.verifyVariantA_ShowRatingReviewOnly(productItem: Product) {
        val expectedData = ExpectedData()
        expectedData.rating = productItem.rating
        expectedData.countReview = productItem.countReview

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantA_ShowRatingReviewOnly(topAdsData: Data) {
        val expectedData = ExpectedData()
        expectedData.rating = topAdsData.product.productRating
        expectedData.countReview = topAdsData.product.countReviewFormat.toIntOrZero()

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantA_ShowIntegrityOnly(productItem: Product) {
        val labelIntegrity = productItem.labelGroupList.find { it.position == LABEL_INTEGRITY }!!
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = labelIntegrity

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantA_ShowIntegrityOnly(topAdsData: Data) {
        val labelIntegrity = topAdsData.product.labelGroupList.find { it.position == LABEL_INTEGRITY }!!
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = ProductLabelGroup(
                title = labelIntegrity.title, position = labelIntegrity.position, type = labelIntegrity.type
        )

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantA_ShowShopRatingOnly(productItem: Product) {
        val expectedData = ExpectedData()
        expectedData.shopRating = productItem.shop.ratingAverage

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantA_ShowShopRatingOnly(topAdsData: Data) {
        val expectedData = ExpectedData()
        expectedData.shopRating = topAdsData.shop.shopRatingAvg

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyShowNone() {
        verifyShopRatingDataForProductCard(ExpectedData())
    }

    private fun ProductItemViewModel.verifyVariantB_ShowIntegrityAndShopRating(productItem: Product) {
        val productLabelGroup = createLabelIntegrityFromCountSold(productItem)
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = productLabelGroup
        expectedData.shopRating = productItem.shop.ratingAverage

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun createLabelIntegrityFromCountSold(productItem: Product): ProductLabelGroup {
        return ProductLabelGroup(
                position = LABEL_INTEGRITY,
                title = productItem.countSold,
                type = LABEL_INTEGRITY_TYPE
        )
    }

    private fun ProductItemViewModel.verifyVariantB_ShowIntegrityAndShopRating(topAdsData: Data) {
        val productLabelGroup = createLabelIntegrityFromCountSold(topAdsData.product)
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = productLabelGroup
        expectedData.shopRating = topAdsData.shop.shopRatingAvg

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun createLabelIntegrityFromCountSold(productItem: com.tokopedia.topads.sdk.domain.model.Product): ProductLabelGroup {
        return ProductLabelGroup(
                position = LABEL_INTEGRITY,
                title = productItem.countSold,
                type = LABEL_INTEGRITY_TYPE
        )
    }

    private fun ProductItemViewModel.verifyVariantB_ShowShopRatingOnly(productItem: Product) {
        val expectedData = ExpectedData()
        expectedData.shopRating = productItem.shop.ratingAverage

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantB_ShowShopRatingOnly(topAdsData: Data) {
        val expectedData = ExpectedData()
        expectedData.shopRating = topAdsData.shop.shopRatingAvg

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantB_ShowIntegrityOnly(productItem: Product) {
        val productLabelGroup = createLabelIntegrityFromCountSold(productItem)
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = productLabelGroup

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantB_ShowIntegrityOnly(topAdsData: Data) {
        val productLabelGroup = createLabelIntegrityFromCountSold(topAdsData.product)
        val expectedData = ExpectedData()
        expectedData.labelIntegrity = productLabelGroup

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantC_ShowShopRatingOnly(productItem: Product) {
        val expectedData = ExpectedData()
        expectedData.shopRating = productItem.shop.ratingAverage
        expectedData.isShopRatingYellow = true

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyVariantC_ShowShopRatingOnly(topAdsData: Data) {
        val expectedData = ExpectedData()
        expectedData.shopRating = topAdsData.shop.shopRatingAvg
        expectedData.isShopRatingYellow = true

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyNoVariant_NoChangesOnShopRating(productItem: Product) {
        val expectedData = ExpectedData()
        expectedData.rating = productItem.rating
        expectedData.countReview = productItem.countReview
        expectedData.labelIntegrity = productItem.labelGroupList.find { it.position == LABEL_INTEGRITY }
        expectedData.shopRating = ""

        verifyShopRatingDataForProductCard(expectedData)
    }

    private fun ProductItemViewModel.verifyNoVariant_NoChangesOnShopRating(topAdsData: Data) {
        val labelIntegrity = topAdsData.product.labelGroupList.find { it.position == LABEL_INTEGRITY }
        val expectedData = ExpectedData()
        expectedData.rating = topAdsData.product.productRating
        expectedData.countReview = topAdsData.product.countReviewFormat.toIntOrZero()
        expectedData.labelIntegrity = labelIntegrity?.run { ProductLabelGroup(
                title = title, position = position, type = type
        )}
        expectedData.shopRating = ""

        verifyShopRatingDataForProductCard(expectedData)
    }
}