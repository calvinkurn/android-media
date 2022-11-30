package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.banned.BannedProductsPresenterDelegate
import com.tokopedia.search.result.product.banned.BannedProductsView
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchView
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressView
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterPresenterDelegate
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterView
import com.tokopedia.search.result.product.filter.dynamicfilter.MutableDynamicFilterModelProviderDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDynamicProductView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenterDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetPresenterDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenterDelegate
import com.tokopedia.search.result.product.pagination.PaginationImpl
import com.tokopedia.search.result.product.productfilterindicator.ProductFilterIndicator
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.safesearch.MutableSafeSearchPreference
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenterDelegate
import com.tokopedia.search.result.product.safesearch.SafeSearchView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPreference
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPresenterDelegate
import com.tokopedia.search.result.product.suggestion.SuggestionPresenter
import com.tokopedia.search.result.product.tdn.TopAdsImageViewPresenterDelegate
import com.tokopedia.search.result.product.ticker.TickerPresenterDelegate
import com.tokopedia.search.result.product.wishlist.WishlistPresenterDelegate
import com.tokopedia.search.result.product.wishlist.WishlistView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import rx.schedulers.Schedulers

internal open class ProductListPresenterTestFixtures {

    protected val searchProductCommonResponseJSON = "searchproduct/common-response.json"
    protected val searchProductFirstPageJSON = "searchproduct/loaddata/first-page.json"
    protected val searchProductSecondPageJSON = "searchproduct/loaddata/second-page.json"
    protected val className = "SearchClassName"

    protected val productListView = mockk<ProductListSectionContract.View>(relaxed = true)
    protected val searchProductFirstPageUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected open val searchFirstPageUseCase: UseCase<SearchProductModel>
        get() = searchProductFirstPageUseCase
    protected val searchProductLoadMoreUseCase = mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected open val searchLoadMoreUseCase: UseCase<SearchProductModel>
        get() = searchProductLoadMoreUseCase
    protected val searchProductTopAdsUseCase = mockk<UseCase<TopAdsModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val recommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    protected val getLocalSearchRecommendationUseCase =
        mockk<UseCase<SearchProductModel>>(relaxed = true)
    protected val getInspirationCarouselChipsProductsUseCase =
        mockk<UseCase<InspirationCarouselChipsProductModel>>(relaxed = true)
    protected val saveLastFilterUseCase = mockk<UseCase<Int>>(relaxed = true)
    protected val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val searchCoachMarkLocalCache = mockk<CoachMarkLocalCache>(relaxed = true)
    protected val topAdsHeadlineHelper = mockk<TopAdsHeadlineHelper>(relaxed = true)
    protected val performanceMonitoring = mockk<PageLoadTimePerformanceInterface>(relaxed = true)
    protected val chooseAddressView = mockk<ChooseAddressView>(relaxed = true)
    protected val bannedProductsView = mockk<BannedProductsView>(relaxed = true)
    protected val broadMatchView = mockk<BroadMatchView>(relaxed = true)
    protected val wishlistView = mockk<WishlistView>(relaxed = true)
    protected val inspirationCarouselDynamicProductView =
        mockk<InspirationCarouselDynamicProductView>(relaxed = true)
    protected val testSchedulersProvider = object : SchedulersProvider {
        override fun io() = Schedulers.immediate()

        override fun ui() = Schedulers.immediate()

        override fun computation() = Schedulers.immediate()
    }
    protected val viewUpdater = mockk<ViewUpdater>(relaxed = true)
    protected val sameSessionRecommendationUseCase =
        mockk<UseCase<SearchSameSessionRecommendationModel>>(relaxed = true)
    protected val sameSessionRecommendationPreference =
        mockk<SameSessionRecommendationPreference>(relaxed = true)
    protected val queryKeyProvider = mockk<QueryKeyProvider>(relaxed = true)
    protected val productFilterIndicator = mockk<ProductFilterIndicator>(relaxed = true)
    protected val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    protected val searchParameterProvider = mockk<SearchParameterProvider>(relaxed = true)
    protected val inspirationListAtcView = mockk<InspirationListAtcView>(relaxed = true)
    protected val classNameProvider = mockk<ClassNameProvider> {
        every { className } returns this@ProductListPresenterTestFixtures.className
    }
    protected val applinkModifier = mockk<ApplinkModifier>(relaxed = true)
    protected val safeSearchPreference = mockk<MutableSafeSearchPreference>(relaxed = true)
    protected val safeSearchView = mockk<SafeSearchView>(relaxed = true)
    protected val dynamicFilterModel = MutableDynamicFilterModelProviderDelegate()
    protected val bottomSheetFilterView = mockk<BottomSheetFilterView>(relaxed = true)
    private val pagination = PaginationImpl()
    private val chooseAddressPresenterDelegate = ChooseAddressPresenterDelegate(chooseAddressView)
    private val requestParamsGenerator = RequestParamsGenerator(userSession, pagination)
    protected val bottomSheetFilterPresenter = BottomSheetFilterPresenterDelegate(
        bottomSheetFilterView,
        queryKeyProvider,
        requestParamsGenerator,
        chooseAddressPresenterDelegate,
        { getProductCountUseCase },
        { getDynamicFilterUseCase },
        dynamicFilterModel,
    )
    protected val inspirationCarouselView = mockk<InspirationCarouselView>(relaxed = true)

    protected lateinit var productListPresenter: ProductListPresenter

    @Before
    open fun setUp() {
        val sameSessionRecommendationPresenterDelegate = SameSessionRecommendationPresenterDelegate(
            viewUpdater,
            requestParamsGenerator,
            sameSessionRecommendationUseCase,
            sameSessionRecommendationPreference,
            queryKeyProvider,
            productFilterIndicator,
        )
        val suggestionPresenter = SuggestionPresenter()

        val inspirationListAtcPresenterDelegate = InspirationListAtcPresenterDelegate(
            addToCartUseCase,
            userSession,
            inspirationListAtcView,
            searchParameterProvider,
        )
        val tickerPresenter = TickerPresenterDelegate()
        val safeSearchPresenter = SafeSearchPresenterDelegate(
            safeSearchPreference,
            safeSearchView,
        )
        val topAdsImageViewPresenter = TopAdsImageViewPresenterDelegate()

        productListPresenter = ProductListPresenter(
            searchFirstPageUseCase,
            searchLoadMoreUseCase,
            recommendationUseCase,
            userSession,
            searchCoachMarkLocalCache,
            { getDynamicFilterUseCase },
            { getProductCountUseCase },
            { getLocalSearchRecommendationUseCase },
            { getInspirationCarouselChipsProductsUseCase },
            { saveLastFilterUseCase },
            addToCartUseCase,
            topAdsUrlHitter,
            testSchedulersProvider,
            topAdsHeadlineHelper,
            { performanceMonitoring },
            chooseAddressPresenterDelegate,
            BannerPresenterDelegate(pagination),
            requestParamsGenerator,
            pagination,
            LastFilterPresenterDelegate(
                requestParamsGenerator,
                chooseAddressPresenterDelegate
            ) { saveLastFilterUseCase },
            sameSessionRecommendationPresenterDelegate,
            BannedProductsPresenterDelegate(bannedProductsView, viewUpdater),
            inspirationListAtcPresenterDelegate,
            BroadMatchPresenterDelegate(
                broadMatchView,
                inspirationCarouselDynamicProductView,
                viewUpdater,
                topAdsUrlHitter,
                classNameProvider,
                applinkModifier,
                pagination,
                suggestionPresenter,
            ),
            suggestionPresenter,
            tickerPresenter,
            safeSearchPresenter,
            topAdsImageViewPresenter,
            WishlistPresenterDelegate(wishlistView),
            InspirationWidgetPresenterDelegate(),
            InspirationCarouselPresenterDelegate(
                inspirationCarouselView,
                inspirationListAtcPresenterDelegate,
            ),
            dynamicFilterModel,
            bottomSheetFilterPresenter,
        )
        productListPresenter.attachView(productListView)
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
                productItem.assertTopAdsProduct(
                    topAdsProductList[topAdsProductListIndex],
                    expectedTopAdsProductPosition
                )
                expectedTopAdsProductPosition++
                topAdsProductListIndex++
            } else {
                productItem.assertOrganicProduct(
                    organicProductList[organicProductListIndex],
                    expectedOrganicProductPosition,
                    "",
                    searchProductModel.getProductListType(),
                    searchProductModel.isShowButtonAtc,
                )
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

    protected fun Visitable<*>.assertTopAdsProduct(topAdsProduct: Data, position: Int) {
        val productItem = this as ProductItemDataView

        productItem.isTopAds shouldBe true
        productItem.topadsClickUrl shouldBe topAdsProduct.productClickUrl
        productItem.topadsImpressionUrl shouldBe topAdsProduct.product.image.s_url
        productItem.topadsWishlistUrl shouldBe topAdsProduct.productWishlistUrl
        productItem.topadsTag shouldBe topAdsProduct.tag
        productItem.minOrder shouldBe topAdsProduct.product.productMinimumOrder
        productItem.position shouldBe position
        productItem.productName shouldBe topAdsProduct.product.name
        productItem.applink shouldBe topAdsProduct.applinks
        productItem.customVideoURL shouldBe topAdsProduct.product.customVideoUrl
        productItem.priceRange shouldBe topAdsProduct.product.priceRange
    }

    protected fun Visitable<*>.assertOrganicProduct(
        organicProduct: SearchProductModel.Product,
        position: Int,
        expectedPageTitle: String = "",
        productListType: String = "",
        isShowButtonAtc: Boolean = false,
    ) {
        val productItem = this as ProductItemDataView

        productItem.isOrganicAds shouldBe organicProduct.isOrganicAds()
        productItem.position shouldBe position

        if (organicProduct.isOrganicAds()) {
            productItem.topadsClickUrl shouldBe organicProduct.ads.productClickUrl
            productItem.topadsImpressionUrl shouldBe organicProduct.ads.productViewUrl
            productItem.topadsWishlistUrl shouldBe organicProduct.ads.productWishlistUrl
            productItem.topadsTag shouldBe organicProduct.ads.tag
        } else {
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
        productItem.productListType shouldBe productListType
        productItem.showButtonAtc shouldBe isShowButtonAtc
        productItem.parentId shouldBe organicProduct.parentId
        productItem.priceRange shouldBe organicProduct.priceRange
    }

    @Suppress("UNCHECKED_CAST")
    internal fun RequestParams.getSearchProductParams(): Map<String, Any> =
        parameters[SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS] as Map<String, Any>

    /**
     * Mock behavior for TopAdsHeadlineHelper.processHeadlineAds:
     * 1. Page 1 will take 2 Headline Ads
     * 2. Page 2 and above will take 1 Headline Ads
     * 3. isUseSeparator is only FALSE for the FIRST Headline Ads of the FIRST page
     */
    protected fun `Given top ads headline helper will process headline ads`(
        searchProductModel: SearchProductModel,
        page: Int = 1,
    ) {
        val headlineAdsCount = if (page <= 1) 2 else 1

        every { topAdsHeadlineHelper.processHeadlineAds(any(), any(), any()) } answers {
            searchProductModel.cpmModel.data.take(headlineAdsCount).forEachIndexed { index, data ->
                val isUseSeparator = index > 0 || page > 1

                thirdArg<(Int, ArrayList<CpmData>, Boolean) -> Unit>().invoke(
                    index,
                    arrayListOf(data),
                    isUseSeparator,
                )
            }
        }
    }

    @After
    open fun tearDown() {
        productListPresenter.detachView()
    }
}
