package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import rx.schedulers.Schedulers

internal open class ProductListPresenterTestFixtures {

    protected val searchProductCommonResponseJSON = "searchproduct/common-response.json"
    protected val searchProductFirstPageJSON = "searchproduct/loaddata/first-page.json"
    protected val searchProductSecondPageJSON = "searchproduct/loaddata/second-page.json"

    protected val productListView = mockk<ProductListSectionContract.View>(relaxed = true)
    protected val searchProductFirstPageUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val searchProductLoadMoreUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val recommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    protected val getLocalSearchRecommendationUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getInspirationCarouselChipsProductsUseCase = mockk<UseCase<InspirationCarouselChipsProductModel>>(relaxed = true)
    protected val saveLastFilterUseCase = mockk<UseCase<Int>>(relaxed = true)
    protected val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val remoteConfig = mockk<RemoteConfig>()
    protected val searchCoachMarkLocalCache = mockk<CoachMarkLocalCache>(relaxed = true)
    protected val testSchedulersProvider = object : SchedulersProvider {
        override fun io() = Schedulers.immediate()

        override fun ui() = Schedulers.immediate()

        override fun computation() = Schedulers.immediate()
    }
    protected lateinit var productListPresenter: ProductListPresenter

    @Before
    open fun setUp() {
        productListPresenter = ProductListPresenter(
            searchProductFirstPageUseCase,
            searchProductLoadMoreUseCase,
            recommendationUseCase,
            userSession,
            searchCoachMarkLocalCache,
            { getDynamicFilterUseCase },
            { getProductCountUseCase },
            { getLocalSearchRecommendationUseCase },
            { getInspirationCarouselChipsProductsUseCase },
            { saveLastFilterUseCase },
            topAdsUrlHitter,
            testSchedulersProvider,
            { remoteConfig },
        )
        productListPresenter.attachView(productListView)

        verify {
            productListView.isChooseAddressWidgetEnabled
        }
    }

    protected fun `Then verify visitable list with product items`(
            visitableListSlot: CapturingSlot<List<Visitable<*>>>,
            searchProductModel: SearchProductModel,
            topAdsPositionStart: Int = 0,
            organicPositionStart: Int = 0
    ) {
        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemDataView>()

        val topAdsTemplatePosition = getTopAdsProductPositionByTemplate(searchProductModel)

        val organicProductList = searchProductModel.searchProduct.data.productList
        val topAdsProductList = searchProductModel.topAdsModel.data

        var expectedTopAdsProductPosition = topAdsPositionStart + 1
        var expectedOrganicProductPosition = organicPositionStart + 1

        var topAdsProductListIndex = 0
        var organicProductListIndex = 0

        productItemViewModelList.forEachIndexed { index, productItem ->
            if (topAdsTemplatePosition.contains(index)) {
                productItem.assertTopAdsProduct(topAdsProductList[topAdsProductListIndex], expectedTopAdsProductPosition)
                expectedTopAdsProductPosition++
                topAdsProductListIndex++
            }
            else {
                productItem.assertOrganicProduct(organicProductList[organicProductListIndex], expectedOrganicProductPosition)
                expectedOrganicProductPosition++
                organicProductListIndex++
            }
        }
    }

    private fun getTopAdsProductPositionByTemplate(searchProductModel: SearchProductModel): MutableList<Int> {
        val topAdsTemplatePosition = mutableListOf<Int>()
        searchProductModel.topAdsModel.templates.forEachIndexed { index, template ->
            if (template.isIsAd) topAdsTemplatePosition.add(index)
        }
        return topAdsTemplatePosition
    }

    private fun Visitable<*>.assertTopAdsProduct(topAdsProduct: Data, position: Int) {
        val productItem = this as ProductItemDataView

        productItem.isTopAds shouldBe true
        productItem.topadsClickUrl shouldBe topAdsProduct.productClickUrl
        productItem.topadsImpressionUrl shouldBe topAdsProduct.product.image.s_url
        productItem.topadsWishlistUrl shouldBe topAdsProduct.productWishlistUrl
        productItem.topadsTag shouldBe topAdsProduct.tag
        productItem.minOrder shouldBe topAdsProduct.product.productMinimumOrder
        productItem.position shouldBe position
    }

    protected fun Visitable<*>.assertOrganicProduct(
        organicProduct: SearchProductModel.Product,
        position: Int,
        expectedPageTitle: String = "",
    ) {
        val productItem = this as ProductItemDataView

        productItem.isOrganicAds shouldBe organicProduct.isOrganicAds()
        productItem.position shouldBe position

        if (organicProduct.isOrganicAds()) {
            productItem.topadsClickUrl shouldBe organicProduct.ads.productClickUrl
            productItem.topadsImpressionUrl shouldBe organicProduct.ads.productViewUrl
            productItem.topadsWishlistUrl shouldBe organicProduct.ads.productWishlistUrl
            productItem.topadsTag shouldBe organicProduct.ads.tag
        }
        else {
            productItem.topadsClickUrl shouldBe ""
            productItem.topadsImpressionUrl shouldBe ""
            productItem.topadsWishlistUrl shouldBe ""
            productItem.topadsTag shouldBe 0
        }

        productItem.productID shouldBe organicProduct.id
        productItem.productName shouldBe organicProduct.name
        productItem.price shouldBe organicProduct.price
        productItem.minOrder shouldBe organicProduct.minOrder
        productItem.pageTitle shouldBe expectedPageTitle
    }

    @Suppress("UNCHECKED_CAST")
    internal fun RequestParams.getSearchProductParams(): Map<String, Any>
            = parameters[SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS] as Map<String, Any>

    @After
    open fun tearDown() {
        productListPresenter.detachView()
    }
}