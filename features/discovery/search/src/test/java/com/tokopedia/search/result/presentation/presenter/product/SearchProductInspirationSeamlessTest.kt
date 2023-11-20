package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselData
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert
import org.junit.Test
import rx.Subscriber
import java.util.TreeMap

private const val inspirationProductWithAllImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product.json"
private const val inspirationProductWithOneKeywordNoImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product_one_item_no_image.json"
private const val inspirationProductWithAllKeywordNoImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product_no_image_all.json"
private const val LAYOUT_INSPIRATION_KEYWORD_SEAMLESS = "carousel_seamless"
private const val TARGET_CLICK = 0

internal class SearchProductInspirationSeamlessTest : ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val keyword = "sepatu"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
        SearchApiConst.START to "0",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to "0"
    )

    @Test
    fun `Show inspiration seamless general cases`() {
        val searchProductModel: SearchProductModel = inspirationProductWithAllImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration seamless keyword product and product sequence`(
            searchProductModel
        )
    }

    @Test
    fun `Show inspiration seamless general no Image at one keyword`() {
        val searchProductModel: SearchProductModel =
            inspirationProductWithOneKeywordNoImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration seamless keyword product and product sequence`(
            searchProductModel
        )
    }

    @Test
    fun `Show inspiration seamless general no Image at all keyword`() {
        val searchProductModel: SearchProductModel =
            inspirationProductWithAllKeywordNoImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify view set product list`()
        `Then verify visitable list has correct inspiration seamless keyword product and product sequence`(
            searchProductModel
        )
    }

    @Test
    fun `Check impression keyword item for first Item`() {
        val searchProductModel: SearchProductModel = inspirationProductWithAllImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        val targetKeyword = findKeywordInspirationOn(position = TARGET_CLICK)
        `When inspiration keyword impression`(targetKeyword)
        `Then verify view interaction for impressed inspiration keyword`(targetKeyword)
    }

    @Test
    fun `Check keyword click`() {
        val searchProductModel: SearchProductModel = inspirationProductWithAllImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        val targetKeyword = findKeywordInspirationOn(position = TARGET_CLICK)
        `When inspiration keyword click`(targetKeyword)
        `Then verify view interaction for click inspiration keyword`(targetKeyword)
    }

    @Test
    fun `Check inspiration product impression`() {
        val searchProductModel: SearchProductModel = inspirationProductWithAllImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)
        val targetProduct = findFirstProductInspiration()
        `When inspiration product impression`(targetProduct)
        `Then verify view interaction for impressed inspiration product`(targetProduct)
    }

    @Test
    fun `Check product click`() {
        val searchProductModel: SearchProductModel = inspirationProductWithAllImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        val targetProduct = findFirstProductInspiration()
        `When inspiration product click`(targetProduct)
        `Then verify view interaction for click inspiration product`(targetProduct)
    }

    private fun `load Data Product Search With Data`(searchModel: SearchProductModel) {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
            searchModel
        )
        `Given Mechanism to save and get product position from cache`()
        `When Load Data`()

        `Then verify view set product list`()
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
        searchProductModel: SearchProductModel
    ) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct inspiration seamless keyword product and product sequence`(
        searchProductModel: SearchProductModel
    ) {
        val visitableList = visitableListSlot.captured
        visitableList.size shouldBe 19

        val inspirationSeamlessCardData =
            searchProductModel.searchInspirationCarousel.getDataInspirationSeamless()
        val dataInspirationKeywordsAndProducts =
            inspirationSeamlessCardData.indexingSeamlessInspirationProductByKeywordPosition()
        val listPositionOfInspirationSeamlessKeywords = dataInspirationKeywordsAndProducts.keys
        val positionOfInspirationSeamlessProduct =
            dataInspirationKeywordsAndProducts.getStartAndEndPositionOfSeamlessInspirationProduct()

        var inspirationCarouselIndex = 0
        var keywordIndex = 0
        var productIndex = 0
        visitableList.forEachIndexed { index, visitable ->
            when {
                index.isOnSeamlessInspirationKeywordPositionsBaseOn(
                    listPositionOfInspirationSeamlessKeywords
                ) -> {
                    visitable.shouldBeInstanceOf<InspirationKeywordCardView>(
                        "visitable list at index $index should be InspirationKeywordCardView"
                    )
                    (visitable as InspirationKeywordCardView)
                        .assertInspirationKeywordDataView(
                            inspirationSeamlessCardData[inspirationCarouselIndex]
                        )
                    inspirationCarouselIndex += 1
                    keywordIndex = index
                    productIndex = 0
                }

                index.isOnSeamlessInspirationProductPositionsBaseOn(
                    positionOfInspirationSeamlessProduct,
                    keywordIndex
                ) -> {
                    visitable.shouldBeInstanceOf<InspirationProductItemDataView>(
                        "visitable list at index $index should be InspirationProductItemDataView"
                    )
                    val product =
                        dataInspirationKeywordsAndProducts.getProductOn(keywordIndex, productIndex)
                    (visitable as InspirationProductItemDataView)
                        .assertInspirationProductItemDataView(product)
                    productIndex++
                }

                else -> {
                    val isInspirationSeamlessCard =
                        visitable is InspirationKeywordCardView || visitable is InspirationProductItemDataView
                    Assert.assertFalse(isInspirationSeamlessCard)
                }
            }
        }
    }

    private fun Int.isOnSeamlessInspirationKeywordPositionsBaseOn(listOfKeywordPositions: MutableSet<Int>) =
        listOfKeywordPositions.contains(this).orFalse()

    private fun Int.isOnSeamlessInspirationProductPositionsBaseOn(
        listOfStarAndEndProductPositions: TreeMap<Int, Int>,
        keywordIndex: Int
    ): Boolean {
        val positionStart = keywordIndex + 1
        val positionEnd = listOfStarAndEndProductPositions[positionStart] ?: return false
        return this in positionStart..positionEnd
    }

    private fun SearchInspirationCarousel.getDataInspirationSeamless(): List<InspirationCarouselData> {
        return this.data.filter {
            it.layout == LAYOUT_INSPIRATION_KEYWORD_SEAMLESS
        }
    }

    private fun List<InspirationCarouselData>.indexingSeamlessInspirationProductByKeywordPosition(): TreeMap<Int, ArrayList<InspirationCarouselProduct>> {
        val inspirationSeamlessProduct: TreeMap<Int, ArrayList<InspirationCarouselProduct>> =
            TreeMap()
        this.forEach {
            val inspirationSeamlessInspirationProduct: ArrayList<InspirationCarouselProduct> =
                arrayListOf()
            it.inspirationCarouselOptions.forEach { products ->
                inspirationSeamlessInspirationProduct.addAll(products.inspirationCarouselProducts)
            }
            inspirationSeamlessProduct[it.position + 1] = inspirationSeamlessInspirationProduct
        }
        return inspirationSeamlessProduct
    }

    private fun TreeMap<Int, ArrayList<InspirationCarouselProduct>>.getStartAndEndPositionOfSeamlessInspirationProduct(): TreeMap<Int, Int> {
        val inspirationSeamlessProduct: TreeMap<Int, Int> = TreeMap()
        this.forEach { (position, inspirationCarouselProducts) ->
            val startPosition = position + 1
            inspirationSeamlessProduct[startPosition] =
                startPosition + inspirationCarouselProducts.size
        }
        return inspirationSeamlessProduct
    }

    private fun TreeMap<Int, ArrayList<InspirationCarouselProduct>>.getProductOn(
        key: Int,
        productIndex: Int
    ) =
        this[key]?.get(productIndex) ?: throw IndexOutOfBoundsException()

    private fun InspirationProductItemDataView.assertInspirationProductItemDataView(
        inspirationCarouselProduct: InspirationCarouselProduct
    ) {
        this.id shouldBe inspirationCarouselProduct.id
        this.name shouldBe inspirationCarouselProduct.name
        this.price shouldBe inspirationCarouselProduct.price
        this.imageUrl shouldBe inspirationCarouselProduct.imgUrl
        this.url shouldBe inspirationCarouselProduct.url
        this.applink shouldBe inspirationCarouselProduct.applink
    }

    private fun InspirationKeywordCardView.assertInspirationKeywordDataView(
        inspirationCarouselData: InspirationCarouselData,
    ) {
        val isNoImage = !inspirationCarouselData.inspirationCarouselOptions.none { it.bannerImageUrl.isEmpty() }
        this.title shouldBe inspirationCarouselData.title
        this.isOneOrMoreIsEmptyImage shouldBe isNoImage
        this.optionsItems.forEachIndexed { index, seamlessInspirationKeyword ->
            seamlessInspirationKeyword.keyword shouldBe inspirationCarouselData.inspirationCarouselOptions[index].title
        }
    }

    private fun findKeywordInspirationOn(position: Int): InspirationKeywordDataView {
        val visitableList = visitableListSlot.captured
        val inspirationKeywords =
            visitableList.find { it is InspirationKeywordCardView } as InspirationKeywordCardView
        return inspirationKeywords.optionsItems[position]
    }

    private fun `When inspiration keyword impression`(inspirationKeyword: InspirationKeywordDataView) {
        productListPresenter.onInspirationKeywordImpressed(inspirationKeyword)
    }

    private fun `When inspiration keyword click`(inspirationKeyword: InspirationKeywordDataView) {
        productListPresenter.onInspirationKeywordItemClick(inspirationKeyword)
    }

    private fun `Then verify view interaction for impressed inspiration keyword`(inspirationKeyword: InspirationKeywordDataView) {
        verify {
            inspirationKeywordSeamlessView.trackEventImpressionInspirationKeyword(inspirationKeyword)
        }
    }

    private fun `Then verify view interaction for click inspiration keyword`(inspirationKeyword: InspirationKeywordDataView) {
        verify {
            inspirationKeywordSeamlessView.trackEventClickItemInspirationKeyword(inspirationKeyword)
            applinkModifier.modifyApplink(inspirationKeyword.applink)
            inspirationKeywordSeamlessView.openLink(any(), inspirationKeyword.url)
        }
    }

    private fun findFirstProductInspiration(): InspirationProductItemDataView {
        val visitableList = visitableListSlot.captured
        return visitableList.find { it is InspirationProductItemDataView } as InspirationProductItemDataView
    }

    private fun `When inspiration product impression`(inspirationProduct: InspirationProductItemDataView) {
        productListPresenter.onInspirationProductItemImpressed(inspirationProduct)
    }

    private fun `Then verify view interaction for impressed inspiration product`(inspirationProduct: InspirationProductItemDataView) {
        verify {
            val seamlessInspirationProductType = inspirationProduct.seamlessInspirationProductType
            inspirationProductSeamlessView.trackInspirationProductSeamlessImpression(
                seamlessInspirationProductType.type,
                seamlessInspirationProductType.inspirationCarouselProduct
            )
        }
    }

    private fun `When inspiration product click`(inspirationProduct: InspirationProductItemDataView) {
        productListPresenter.onInspirationProductItemClick(inspirationProduct)
    }

    private fun `Then verify view interaction for click inspiration product`(inspirationProduct: InspirationProductItemDataView) {
        verify {
            val seamlessInspirationProductType = inspirationProduct.seamlessInspirationProductType
            inspirationProductSeamlessView.trackInspirationProductSeamlessClick(
                seamlessInspirationProductType.type,
                seamlessInspirationProductType.inspirationCarouselProduct
            )
            inspirationProductSeamlessView.openLink(
                inspirationProduct.applink,
                inspirationProduct.url
            )
        }
    }
}
