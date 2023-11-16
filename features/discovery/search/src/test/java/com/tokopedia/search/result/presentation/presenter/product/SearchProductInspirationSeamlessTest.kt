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
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS_PRODUCT
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT
import com.tokopedia.search.result.product.productitem.ProductItemVisitable
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine.LayoutType
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproducttitle.InspirationProductTitleDataView
import com.tokopedia.search.result.product.separator.VerticalSeparatorDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Subscriber
import java.util.TreeMap

private const val inspirationProductWithAllImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product.json"
private const val inspirationProductWithOneKeywordNoImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product_one_item_no_image.json"
private const val inspirationProductWithAllKeywordNoImage =
    "searchproduct/seamlessinspiration/seamless-inspiration-product_no_image_all.json"
private const val inspirationKeywordGridIconDrifting =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-icon-drifting.json"
private const val inspirationKeywordGridIconFunneling =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-icon-funneling.json"
private const val inspirationKeywordGridIconSeamlessFunneling =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-icon-seamless-funneling.json"
private const val inspirationKeywordGridIconSeamlessDrifting =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-icon-seamless-drifting.json"
private const val inspirationKeywordGridImageDrifting =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-image-drifting.json"
private const val inspirationKeywordGridImageFunneling =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-image-funneling.json"
private const val inspirationKeywordGridImageSeamlessFunneling =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-image-seamless-funneling.json"
private const val inspirationKeywordGridImageSeamlessDrifting =
    "searchproduct/seamlessinspiration/seamless-inspiration-keyword-image-seamless-drifting.json"
private const val inspirationCarouselSeamlessWithOtherCarousel =
    "searchproduct/seamlessinspiration/inspiration-carousel-seamless-with-other-carousel.json"
private const val TARGET_CLICK = 0

internal class SearchProductInspirationSeamlessTest : ProductListPresenterTestFixtures() {
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

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

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.DEFAULT_SEAMLESS,
            keyword
        )
    }

    @Test
    fun `Show inspiration seamless general no Image at one keyword`() {
        val searchProductModel: SearchProductModel =
            inspirationProductWithOneKeywordNoImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.DEFAULT_SEAMLESS,
            keyword
        )
    }

    @Test
    fun `Show inspiration seamless general no Image at all keyword`() {
        val searchProductModel: SearchProductModel =
            inspirationProductWithAllKeywordNoImage.jsonToObject()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.DEFAULT_SEAMLESS,
            keyword
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

    @Test
    fun `Show inspiration keyword grid icon keyword drifting`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridIconDrifting.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.ICON_DRIFTING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid icon keyword funneling`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridIconFunneling.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.ICON_FUNNELING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid icon seamless keyword funneling`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridIconSeamlessFunneling.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.ICON_FUNNELING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid icon seamless keyword drifting`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridIconSeamlessDrifting.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.ICON_DRIFTING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid image keyword drifting`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridImageDrifting.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.IMAGE_DRIFTING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid image keyword funneling`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridImageFunneling.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.IMAGE_FUNNELING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid image keyword seamless drifting`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridImageSeamlessDrifting.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.IMAGE_DRIFTING,
            keyword
        )
    }

    @Test
    fun `Show inspiration keyword grid image keyword seamless funneling`() {
        val searchProductModel: SearchProductModel =
            inspirationKeywordGridImageSeamlessFunneling.jsonToObject()
        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then verify visitable list has correct inspiration keyword product and product sequence`(
            searchProductModel,
            LayoutType.IMAGE_FUNNELING,
            keyword
        )
    }

    private fun `load Data Product Search With Data`(
        searchModel: SearchProductModel,
        searchParams: Map<String, Any> = searchParameter,
    ) {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
            searchModel
        )
        `Given Mechanism to save and get product position from cache`()
        `Given keyword from view`()

        `When Load Data`(searchParams)

        `Then verify view set product list`()
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(
        searchProductModel: SearchProductModel
    ) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given keyword from view`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `When Load Data`(searchParams: Map<String, Any>) {
        productListPresenter.loadData(searchParams)
    }

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has correct inspiration keyword product and product sequence`(
        searchProductModel: SearchProductModel,
        layoutType : LayoutType,
        searchKeyword: String,
    ) {
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
                            inspirationSeamlessCardData[inspirationCarouselIndex],
                            layoutType,
                            searchKeyword
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
            it.layout == LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS
        }
    }

    private fun List<InspirationCarouselData>.indexingSeamlessInspirationProductByKeywordPosition()
    : TreeMap<Int, ArrayList<InspirationCarouselProduct>> {
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

    private fun TreeMap<Int, ArrayList<InspirationCarouselProduct>>
        .getStartAndEndPositionOfSeamlessInspirationProduct(): TreeMap<Int, Int> {
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
        inspirationCarouselProduct: InspirationCarouselProduct,
    ) {
        this.id shouldBe inspirationCarouselProduct.id
        this.name shouldBe inspirationCarouselProduct.name
        this.price shouldBe inspirationCarouselProduct.price
        this.imageUrl shouldBe inspirationCarouselProduct.imgUrl
        this.url shouldBe inspirationCarouselProduct.url
        this.applink shouldBe inspirationCarouselProduct.applink
        this.topAdsViewUrl shouldBe inspirationCarouselProduct.ads.productViewUrl
        this.topAdsClickUrl shouldBe inspirationCarouselProduct.ads.productClickUrl
        this.isOrganicAds shouldBe inspirationCarouselProduct.isOrganicAds()
    }

    private fun InspirationKeywordCardView.assertInspirationKeywordDataView(
        inspirationCarouselData: InspirationCarouselData,
        layoutType: LayoutType,
        searchTerm: String,
    ) {
        val isNoImage = !inspirationCarouselData.inspirationCarouselOptions.none { it.bannerImageUrl.isEmpty() }
        this.title shouldBe inspirationCarouselData.title
        this.isOneOrMoreIsEmptyImage shouldBe isNoImage
        this.layoutType shouldBe layoutType
        this.searchTerm shouldBe searchTerm
        this.optionsItems.forEachIndexed { index, seamlessInspirationKeyword ->
            val title = inspirationCarouselData.inspirationCarouselOptions[index].title
            seamlessInspirationKeyword.keyword shouldBe title
        }
    }

    private fun findKeywordInspirationOn(position: Int): InspirationKeywordDataView {
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

    private fun `Then verify view interaction for impressed inspiration keyword`(
        inspirationKeyword: InspirationKeywordDataView
    ) {
        verify {
            inspirationKeywordSeamlessView.trackEventImpressionInspirationKeyword(inspirationKeyword)
        }
    }

    private fun `Then verify view interaction for click inspiration keyword`(
        inspirationKeyword: InspirationKeywordDataView
    ) {
        verify {
            inspirationKeywordSeamlessView.trackEventClickItemInspirationKeyword(inspirationKeyword)
            applinkModifier.modifyApplink(inspirationKeyword.applink)
            inspirationKeywordSeamlessView.openLink(any(), inspirationKeyword.url)
        }
    }

    private fun findFirstProductInspiration(): InspirationProductItemDataView {
        return visitableList.find { it is InspirationProductItemDataView } as InspirationProductItemDataView
    }

    private fun `When inspiration product impression`(inspirationProduct: InspirationProductItemDataView) {
        productListPresenter.onInspirationProductItemImpressed(inspirationProduct)
    }

    private fun `Then verify view interaction for impressed inspiration product`(
        inspirationProduct: InspirationProductItemDataView
    ) {
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

    private fun `Then verify view interaction for click inspiration product`(
        inspirationProduct: InspirationProductItemDataView
    ) {
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

    @Test
    fun `Show other carousel between carousel_seamless carousel`() {
        val searchProductModel: SearchProductModel =
            inspirationCarouselSeamlessWithOtherCarousel.jsonToObject()

        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then assert carousel product_list is between carousel_seamless`(searchProductModel)
    }

    private fun `Then assert carousel product_list is between carousel_seamless`(
        searchProductModel: SearchProductModel
    ) {
        val productCountBeforeCarousel = searchProductModel
            .searchInspirationCarousel
            .data
            .find { it.layout == LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT }
            ?.position

        val carouselIndex = visitableList.indexOfFirst { it is SuggestionDataView }
        val productItemListBeforeCarousel = visitableList
            .subList(0, carouselIndex)
            .filterIsInstance<ProductItemVisitable>()

        assertEquals(productItemListBeforeCarousel.size, productCountBeforeCarousel)
    }

    @Test
    fun `inspiration carousel seamless_product`() {
        val searchProductModel = "searchproduct/seamlessinspiration/seamless-product-diversification.json"
            .jsonToObject<SearchProductModel>()

        `Given search reimagine rollence product card will return non control variant`()
        `load Data Product Search With Data`(searchProductModel)

        `Then assert inspiration carousel seamless product`(searchProductModel)
    }

    private fun `Then assert inspiration carousel seamless product`(searchProductModel: SearchProductModel) {
        val seamlessProductData = searchProductModel.searchInspirationCarousel.data.find {
            it.layout == LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS_PRODUCT
        }!!

        val seamlessProductItemList = seamlessProductData
            .inspirationCarouselOptions
            .flatMap { it.inspirationCarouselProducts }

        val seamlessProductTitleIndex =
            visitableList.indexOfFirst { it is ProductItemDataView } + seamlessProductData.position

        val seamlessProductItemFirstIndex = seamlessProductTitleIndex + 1

        assertThat(
            visitableList[seamlessProductTitleIndex],
            instanceOf(InspirationProductTitleDataView::class.java)
        )

        seamlessProductItemList.forEachIndexed { index, inspirationCarouselProduct ->
            val inspirationProductItemDataView =
                visitableList[index + seamlessProductItemFirstIndex] as InspirationProductItemDataView

            inspirationProductItemDataView
                .assertInspirationProductItemDataView(inspirationCarouselProduct)
            inspirationProductItemDataView.isShowAdsLabel shouldBe false
        }

        assertThat(
            visitableList[seamlessProductItemFirstIndex + seamlessProductItemList.size],
            instanceOf(VerticalSeparatorDataView::class.java)
        )
    }
}
