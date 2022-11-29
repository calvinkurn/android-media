package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_BUNDLE
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber

private const val singleBundle = "searchproduct/inspirationbundle/single-bundling.json"
private const val multipleBundle = "searchproduct/inspirationbundle/multiple-bundling.json"
private const val invalidBundle = "searchproduct/inspirationbundle/invalid-bundling.json"

internal class SearchProductInspirationBundleTest: ProductListPresenterTestFixtures() {
    companion object {
        private const val SAVING_AMOUNT_FORMAT = "Hemat <b>%s</b>"
    }

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val keyword = "samsung"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0",
    )
    private val expectedDimension90 = Dimension90Utils.getDimension90(searchParameter)

    @Test
    fun `Show inspiration bundle - single_bundle`() {
        val searchProductModel: SearchProductModel = singleBundle.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Bundle`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct single bundle and product sequence on first page`(searchProductModel)
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Bundle`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct single bundle and product sequence on first page`(
        searchProductModel: SearchProductModel
    ) {
        val visitableList = visitableListSlot.captured

        // 0 -> choose address data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> product
        // 11 -> inspiration bundle
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product
        visitableList.size shouldBe 16

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<ChooseAddressDataView>(
                        "visitable list at index $index should be ChooseAddressDataViewModel"
                    )
                }
                11 -> {
                    visitable.shouldBeInstanceOf<InspirationProductBundleDataView>(
                            "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationProductBundleDataView).layout == LAYOUT_INSPIRATION_CAROUSEL_BUNDLE) {
                        "Inspiration Carousel layout should be $LAYOUT_INSPIRATION_CAROUSEL_BUNDLE"
                    }
                    visitable.assertSingleBundleDataView(
                        searchProductModel.searchInspirationCarousel.data[0],
                    )
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                            "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationProductBundleDataView.assertSingleBundleDataView(
        inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title
        this.trackingOption shouldBe inspirationCarouselData.trackingOption.toInt()

        this.bundleList.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.carouselTitle shouldBe inspirationCarouselData.title
            actual.applink shouldBe expected.applink
            actual.componentId shouldBe expected.componentId
            actual.bundle.assertSingleBundle(
                expected,
                expected.inspirationCarouselProducts,
                expected.title,
            )
            actual.trackingOption shouldBe inspirationCarouselData.trackingOption.toInt()
            actual.dimension90 shouldBe expectedDimension90
            actual.type shouldBe inspirationCarouselData.type
        }
    }

    private fun BundleUiModel.assertSingleBundle(
        option: SearchProductModel.InspirationCarouselOption,
        expectedInspirationCarouselProduct: List<SearchProductModel.InspirationCarouselProduct>,
        optionTitle: String,
    ) {
        bundleName shouldBe optionTitle
        bundleType shouldBe BundleTypes.SINGLE_BUNDLE
        bundleDetails.listShouldBe(expectedInspirationCarouselProduct) { actualBundle, expectedProduct ->
            actualBundle.bundleId shouldBe expectedProduct.bundleId
            actualBundle.minOrderWording shouldBe expectedProduct.label
            actualBundle.originalPrice shouldBe expectedProduct.originalPrice
            actualBundle.displayPrice shouldBe expectedProduct.priceStr
            actualBundle.displayPriceRaw shouldBe expectedProduct.price.toLong()
            actualBundle.discountPercentage shouldBe expectedProduct.discountPercentage
            actualBundle.savingAmountWording shouldBe SAVING_AMOUNT_FORMAT.format(expectedProduct.discount)
            actualBundle.shopInfo?.shopId shouldBe ""
            actualBundle.shopInfo?.shopName shouldBe option.bundle.shop.name
            actualBundle.shopInfo?.shopIconUrl shouldBe option.bundle.shop.url
            actualBundle.productSoldInfo shouldBe option.bundle.countSold
            actualBundle.useProductSoldInfo shouldBe true
            actualBundle.products.size shouldBe 1
            val actualProduct = actualBundle.products[0]
            actualProduct.productId shouldBe expectedProduct.id
            actualProduct.productName shouldBe expectedProduct.name
            actualProduct.productImageUrl shouldBe expectedProduct.imgUrl
            actualProduct.productAppLink shouldBe expectedProduct.applink
            actualProduct.hasVariant shouldBe false
        }
    }

    @Test
    fun `Show inspiration bundle - multiple_bundle`() {
        val searchProductModel: SearchProductModel = multipleBundle.jsonToObject()
        `Given Search Product API will return SearchProductModel with Inspiration Bundle`(searchProductModel)

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has correct multiple bundle and product sequence on first page`(searchProductModel)
    }

    private fun `Then verify visitable list has correct multiple bundle and product sequence on first page`(
        searchProductModel: SearchProductModel
    ) {
        val visitableList = visitableListSlot.captured

        // 0 -> choose address data
        // 1 -> product
        // 2 -> product
        // 3 -> product
        // 4 -> product
        // 5 -> product
        // 6 -> product
        // 7 -> product
        // 8 -> product
        // 9 -> product
        // 10 -> product
        // 11 -> inspiration bundle
        // 12 -> product
        // 13 -> product
        // 14 -> product
        // 15 -> product
        visitableList.size shouldBe 16

        visitableList.forEachIndexed { index, visitable ->
            when (index) {
                0 -> {
                    visitable.shouldBeInstanceOf<ChooseAddressDataView>(
                        "visitable list at index $index should be ChooseAddressDataViewModel"
                    )
                }
                11 -> {
                    visitable.shouldBeInstanceOf<InspirationProductBundleDataView>(
                        "visitable list at index $index should be InspirationCarouselViewModel"
                    )
                    assert((visitable as InspirationProductBundleDataView).layout == LAYOUT_INSPIRATION_CAROUSEL_BUNDLE) {
                        "Inspiration Carousel layout should be $LAYOUT_INSPIRATION_CAROUSEL_BUNDLE"
                    }
                    visitable.assertMultipleBundleDataView(
                        searchProductModel.searchInspirationCarousel.data[0],
                    )
                }
                else -> {
                    visitable.shouldBeInstanceOf<ProductItemDataView>(
                        "visitable list at index $index should be ProductItemViewModel"
                    )
                }
            }
        }
    }

    private fun InspirationProductBundleDataView.assertMultipleBundleDataView(
        inspirationCarouselData: SearchProductModel.InspirationCarouselData,
    ) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title
        this.trackingOption shouldBe inspirationCarouselData.trackingOption.toInt()

        this.bundleList.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.carouselTitle shouldBe inspirationCarouselData.title
            actual.applink shouldBe expected.applink
            actual.componentId shouldBe expected.componentId
            actual.bundle.assertMultipleBundle(
                expected,
                expected.inspirationCarouselProducts,
                expected.title,
            )
            actual.trackingOption shouldBe inspirationCarouselData.trackingOption.toInt()
            actual.dimension90 shouldBe expectedDimension90
            actual.type shouldBe inspirationCarouselData.type
        }
    }

    private fun BundleUiModel.assertMultipleBundle(
        option: SearchProductModel.InspirationCarouselOption,
        expectedInspirationCarouselProduct: List<SearchProductModel.InspirationCarouselProduct>,
        optionTitle: String,
    ) {
        bundleName shouldBe optionTitle
        bundleType shouldBe BundleTypes.MULTIPLE_BUNDLE
        selectedBundleApplink
        val bundleDetail = bundleDetails[0]

        bundleDetail.bundleId shouldBe 0.toString()
        bundleDetail.originalPrice shouldBe option.bundle.originalPrice
        bundleDetail.displayPrice shouldBe CurrencyFormatUtil.convertPriceValueToIdrFormat(option.bundle.price, false)
        bundleDetail.displayPriceRaw shouldBe option.bundle.price
        bundleDetail.discountPercentage shouldBe option.bundle.discountPercentage
        bundleDetail.savingAmountWording shouldBe SAVING_AMOUNT_FORMAT.format(option.bundle.discount)
        bundleDetail.shopInfo?.shopId shouldBe ""
        bundleDetail.shopInfo?.shopName shouldBe option.bundle.shop.name
        bundleDetail.shopInfo?.shopIconUrl shouldBe option.bundle.shop.url
        bundleDetail.productSoldInfo shouldBe option.bundle.countSold
        bundleDetail.useProductSoldInfo shouldBe true
        bundleDetail.products.listShouldBe(expectedInspirationCarouselProduct) { actualProduct, expectedProduct ->
            actualProduct.productId shouldBe expectedProduct.id
            actualProduct.productName shouldBe expectedProduct.name
            actualProduct.productImageUrl shouldBe expectedProduct.imgUrl
            actualProduct.productAppLink shouldBe expectedProduct.applink
            actualProduct.hasVariant shouldBe false
        }
    }

    @Test
    fun `Hide Inspiration Bundle with invalid option count`() {
        `Given Search Product API will return SearchProductModel with Inspiration Bundle`(
            invalidBundle.jsonToObject()
        )

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify inspiration bundle is not displayed`()
    }

    private fun `Then verify inspiration bundle is not displayed`() {
        val visitableList = visitableListSlot.captured

        visitableList.any { it is InspirationProductBundleDataView } shouldBe false
    }

}
