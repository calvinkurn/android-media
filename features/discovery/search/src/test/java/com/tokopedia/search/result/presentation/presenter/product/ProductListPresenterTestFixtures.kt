package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.ProductListType.FIXED_GRID
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.discovery.common.reimagine.Search3ProductCard
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.iris.Iris
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.ads.AdsLowOrganic
import com.tokopedia.search.result.product.banned.BannedProductsPresenterDelegate
import com.tokopedia.search.result.product.banned.BannedProductsView
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchView
import com.tokopedia.search.result.product.byteio.ByteIOTrackingDataFactoryImpl
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressView
import com.tokopedia.search.result.product.deduplication.Deduplication
import com.tokopedia.search.result.product.deduplication.DeduplicationView
import com.tokopedia.search.result.product.dialog.BottomSheetInappropriateView
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterPresenterDelegate
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterView
import com.tokopedia.search.result.product.filter.dynamicfilter.MutableDynamicFilterModelProviderDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDynamicProductView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenterDelegate
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetPresenterDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenterDelegate
import com.tokopedia.search.result.product.pagination.PaginationImpl
import com.tokopedia.search.result.product.productfilterindicator.ProductFilterIndicator
import com.tokopedia.search.result.product.recommendation.RecommendationPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.LastClickedProductIdProviderImpl
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.responsecode.ResponseCodeImpl
import com.tokopedia.search.result.product.safesearch.MutableSafeSearchPreference
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenterDelegate
import com.tokopedia.search.result.product.safesearch.SafeSearchView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPreference
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPresenterDelegate
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordPresenterDelegate
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductPresenterDelegate
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductView
import com.tokopedia.search.result.product.similarsearch.SimilarSearchOnBoardingPresenterDelegate
import com.tokopedia.search.result.product.similarsearch.SimilarSearchOnBoardingView
import com.tokopedia.search.result.product.suggestion.SuggestionPresenter
import com.tokopedia.search.result.product.tdn.TopAdsImageViewPresenterDelegate
import com.tokopedia.search.result.product.ticker.TickerPresenterDelegate
import com.tokopedia.search.result.product.visitable.VisitableFactory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import rx.schedulers.Schedulers

internal open class ProductListPresenterTestFixtures {

    protected val searchProductCommonResponseJSON = "searchproduct/common-response.json"
    protected val searchProductFirstPageJSON = "searchproduct/loaddata/first-page.json"
    protected val searchProductSecondPageJSON = "searchproduct/loaddata/second-page.json"

    protected val searchProductCommonResponseReimagineJSON = "searchproduct/common-response-reimagine.json"
    protected val searchProductFirstPageReimagineJSON = "searchproduct/loaddata/reimagine/first-page.json"
    protected val searchProductSecondPageReimagineJSON = "searchproduct/loaddata/reimagine/second-page.json"

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
    protected val getPostATCCarouselUseCase = mockk<UseCase<SearchInspirationCarousel>>(relaxed = true)
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
    protected val couponUseCase = mockk<com.tokopedia.usecase.coroutines.UseCase<SearchCouponModel>>(relaxed = true)
    protected val redeemCouponUseCase = mockk<com.tokopedia.usecase.coroutines.UseCase<SearchRedeemCouponModel>>(relaxed = true)
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
    protected val bottomSheetInappropriateView = mockk<BottomSheetInappropriateView>(relaxed = true)
    protected val safeSearchView = mockk<SafeSearchView>(relaxed = true)
    protected val inspirationCarouselView = mockk<InspirationCarouselView>(relaxed = true)
    protected val bottomSheetFilterView = mockk<BottomSheetFilterView>(relaxed = true)
    protected val abTestRemoteConfig = mockk<RemoteConfig>(relaxed = true)
    protected val similarSearchCoachMarkLocalCache = mockk<SimilarSearchCoachMarkLocalCache>(relaxed = true)
    protected val similarSearchOnBoardingView = mockk<SimilarSearchOnBoardingView>(relaxed = true)
    protected val inspirationKeywordSeamlessView = mockk<InspirationKeywordView>(relaxed = true)
    protected val inspirationProductSeamlessView =
        mockk<InspirationProductView>(relaxed = true)
    protected val reimagineRollence = mockk<ReimagineRollence>(relaxed = true)
    protected val deduplicationView = mockk<DeduplicationView>(relaxed = true)
    private val dynamicFilterModel = MutableDynamicFilterModelProviderDelegate()
    private val pagination = PaginationImpl()
    private val chooseAddressPresenterDelegate = ChooseAddressPresenterDelegate(chooseAddressView)
    private val lastClickedProductIdProvider = LastClickedProductIdProviderImpl()
    private val deduplication = Deduplication(deduplicationView)
    private val requestParamsGenerator = RequestParamsGenerator(
        userSession,
        pagination,
        lastClickedProductIdProvider,
        deduplication
    )
    protected val bottomSheetFilterPresenter = BottomSheetFilterPresenterDelegate(
        bottomSheetFilterView,
        queryKeyProvider,
        requestParamsGenerator,
        chooseAddressPresenterDelegate,
        { getProductCountUseCase },
        { getDynamicFilterUseCase },
        dynamicFilterModel
    )
    val iris = mockk<Iris>()
    private val byteIOTrackingDataFactoryImpl = mockk<ByteIOTrackingDataFactoryImpl>(relaxed = true)

    protected lateinit var productListPresenter: ProductListPresenter

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    open fun setUp() {
        val responseCodeImpl = ResponseCodeImpl()
        val sameSessionRecommendationPresenterDelegate = SameSessionRecommendationPresenterDelegate(
            viewUpdater,
            requestParamsGenerator,
            sameSessionRecommendationUseCase,
            sameSessionRecommendationPreference,
            queryKeyProvider,
            productFilterIndicator
        )
        val tickerPresenter = TickerPresenterDelegate()
        val safeSearchPresenter = SafeSearchPresenterDelegate(
            safeSearchPreference,
            safeSearchView,
            bottomSheetInappropriateView
        )

        val inspirationListAtcPresenterDelegate = InspirationListAtcPresenterDelegate(
            addToCartUseCase,
            getPostATCCarouselUseCase,
            requestParamsGenerator,
            userSession,
            inspirationListAtcView,
            viewUpdater,
            searchParameterProvider,
            byteIOTrackingDataFactoryImpl
        )
        val suggestionPresenter = SuggestionPresenter()
        val bannerPresenterDelegate = BannerPresenterDelegate(pagination)
        val broadMatchPresenterDelegate = BroadMatchPresenterDelegate(
            broadMatchView,
            inspirationCarouselDynamicProductView,
            topAdsUrlHitter,
            classNameProvider,
            applinkModifier,
            pagination,
            suggestionPresenter
        )
        val inspirationCarouselPresenterDelegate = InspirationCarouselPresenterDelegate(
            inspirationCarouselView,
            inspirationListAtcPresenterDelegate,
            topAdsUrlHitter,
            classNameProvider,
            requestParamsGenerator,
            { getInspirationCarouselChipsProductsUseCase },
            chooseAddressPresenterDelegate,
            viewUpdater,
            deduplication,
            couponUseCase,
            redeemCouponUseCase,
            iris,
            byteIOTrackingDataFactoryImpl
        )

        val adsLowOrganic = AdsLowOrganic(
            searchProductTopAdsUseCase,
            { abTestRemoteConfig },
            viewUpdater,
            requestParamsGenerator,
            chooseAddressPresenterDelegate,
            responseCodeImpl,
            byteIOTrackingDataFactoryImpl
        )

        val visitableFactory = VisitableFactory(
            suggestionPresenter = suggestionPresenter,
            performanceMonitoringProvider = { performanceMonitoring },
            topAdsHeadlineHelper = topAdsHeadlineHelper,
            inspirationCarouselPresenter = inspirationCarouselPresenterDelegate,
            inspirationWidgetPresenter = InspirationWidgetPresenterDelegate(),
            bannerDelegate = bannerPresenterDelegate,
            broadMatchDelegate = broadMatchPresenterDelegate,
            topAdsImageViewPresenterDelegate = TopAdsImageViewPresenterDelegate(),
            pagination = pagination,
            byteIOTrackingDataFactory = byteIOTrackingDataFactoryImpl
        )

        val similarSearchOnBoardingPresenterDelegate = SimilarSearchOnBoardingPresenterDelegate(
            similarSearchLocalCache = similarSearchCoachMarkLocalCache,
            { abTestRemoteConfig },
            similarSearchOnBoardingView
        )

        val inspirationKeywordPresenterDelegate = InspirationKeywordPresenterDelegate(
            inspirationKeywordSeamlessView,
            applinkModifier
        )

        val inspirationProductPresenterDelegate = InspirationProductPresenterDelegate(
            inspirationProductSeamlessView,
            topAdsUrlHitter,
            classNameProvider,
            lastClickedProductIdProvider
        )

        productListPresenter = ProductListPresenter(
            searchFirstPageUseCase,
            searchLoadMoreUseCase,
            userSession,
            searchCoachMarkLocalCache,
            { getDynamicFilterUseCase },
            { getProductCountUseCase },
            { getLocalSearchRecommendationUseCase },
            { getInspirationCarouselChipsProductsUseCase },
            { saveLastFilterUseCase },
            addToCartUseCase,
            { getPostATCCarouselUseCase },
            topAdsUrlHitter,
            testSchedulersProvider,
            topAdsHeadlineHelper,
            { performanceMonitoring },
            chooseAddressPresenterDelegate,
            bannerPresenterDelegate,
            requestParamsGenerator,
            pagination,
            LastFilterPresenterDelegate(
                requestParamsGenerator,
                chooseAddressPresenterDelegate
            ) { saveLastFilterUseCase },
            sameSessionRecommendationPresenterDelegate,
            BannedProductsPresenterDelegate(bannedProductsView, viewUpdater),
            inspirationListAtcPresenterDelegate,
            broadMatchPresenterDelegate,
            suggestionPresenter,
            tickerPresenter,
            safeSearchPresenter,
            WishlistPresenterDelegate(wishlistView),
            dynamicFilterModel,
            bottomSheetFilterPresenter,
            visitableFactory,
            inspirationCarouselPresenterDelegate,
            RecommendationPresenterDelegate(viewUpdater, recommendationUseCase),
            adsLowOrganic,
            abTestRemoteConfig,
            responseCodeImpl,
            similarSearchOnBoardingPresenterDelegate,
            inspirationKeywordPresenterDelegate,
            inspirationProductPresenterDelegate,
            reimagineRollence,
            lastClickedProductIdProvider,
            deduplication,
            byteIOTrackingDataFactoryImpl,
        )
        productListPresenter.attachView(productListView)

        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    protected fun `Then verify visitable list with product items`(
        visitableListSlot: CapturingSlot<List<Visitable<*>>>,
        searchProductModel: SearchProductModel,
        topAdsPositionStart: Int = 0,
        organicPositionStart: Int = 0,
    ) {
        val expectedProductListType = searchProductModel.searchProduct.header.meta.productListType
        val expectedShowButtonATC = searchProductModel.searchProduct.header.meta.showButtonAtc

        `Then verify visitable list with product items`(
            searchProductModel,
            visitableListSlot,
            topAdsPositionStart,
            organicPositionStart,
            expectedProductListType,
            expectedShowButtonATC,
        )
    }

    protected fun `Then verify visitable list with product items for reimagine`(
        visitableListSlot: CapturingSlot<List<Visitable<*>>>,
        searchProductModel: SearchProductModel,
        topAdsPositionStart: Int = 0,
        organicPositionStart: Int = 0,
        expectedBlurred: Boolean = true
    ) {
        val expectedShowButtonATC = searchProductModel.searchProductV5.header.meta.showButtonAtc

        `Then verify visitable list with product items for reimagine`(
            searchProductModel,
            visitableListSlot,
            topAdsPositionStart,
            organicPositionStart,
            FIXED_GRID,
            expectedShowButtonATC,
            expectedBlurred
        )
    }

    private fun `Then verify visitable list with product items`(
        searchProductModel: SearchProductModel,
        visitableListSlot: CapturingSlot<List<Visitable<*>>>,
        topAdsPositionStart: Int,
        organicPositionStart: Int,
        expectedProductListType: String,
        expectedShowButtonATC: Boolean,
    ) {
        val organicProductList = searchProductModel.searchProduct.data.productList
        val topAdsProductList = searchProductModel.topAdsModel.data

        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemDataView>()

        val topAdsTemplatePosition = getTopAdsProductPositionByTemplate(searchProductModel)

        var expectedTopAdsProductPosition = topAdsPositionStart + 1
        var expectedOrganicProductPosition = organicPositionStart + 1

        var topAdsProductListIndex = 0
        var organicProductListIndex = 0

        productItemViewModelList.forEachIndexed { index, productItem ->
            if (topAdsTemplatePosition.contains(index)) {
                productItem.assertTopAdsProduct(
                    topAdsProductList[topAdsProductListIndex],
                    expectedTopAdsProductPosition,
                    expectedProductListType,
                    expectedShowButtonATC
                )
                expectedTopAdsProductPosition++
                topAdsProductListIndex++
            } else {
                productItem.assertOrganicProduct(
                    organicProductList[organicProductListIndex],
                    expectedOrganicProductPosition,
                    "",
                    expectedProductListType,
                    expectedShowButtonATC
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

    protected fun Visitable<*>.assertTopAdsProduct(
        topAdsProduct: Data,
        position: Int,
        productListType: String,
        isShowButtonAtc: Boolean = false
    ) {
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
        productItem.productListType shouldBe productListType
        productItem.showButtonAtc shouldBe isShowButtonAtc
        productItem.labelGroupList?.forEachIndexed { index, labelGroupDataView ->
            val expectedLabelGroup = topAdsProduct.product.labelGroupList[index]

            labelGroupDataView.position shouldBe expectedLabelGroup.position
            labelGroupDataView.title shouldBe expectedLabelGroup.title
            labelGroupDataView.type shouldBe expectedLabelGroup.type
            labelGroupDataView.imageUrl shouldBe expectedLabelGroup.imageUrl

            labelGroupDataView.styleList.forEachIndexed { styleIndex, styleDataView ->
                val expectedStyle = expectedLabelGroup.styleList[styleIndex]
                styleDataView.key shouldBe expectedStyle.key
                styleDataView.value shouldBe expectedStyle.value
            }
        }
    }

    protected fun Visitable<*>.assertOrganicProduct(
        organicProduct: SearchProductModel.Product,
        position: Int,
        expectedPageTitle: String = "",
        productListType: String = "",
        isShowButtonAtc: Boolean = false
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

    private fun `Then verify visitable list with product items for reimagine`(
        searchProductModel: SearchProductModel,
        visitableListSlot: CapturingSlot<List<Visitable<*>>>,
        topAdsPositionStart: Int,
        organicPositionStart: Int,
        expectedProductListType: String,
        expectedShowButtonATC: Boolean,
        expectedBlurred: Boolean = true
    ) {
        val organicProductList = searchProductModel.searchProductV5.data.productList
        val topAdsProductList = searchProductModel.topAdsModel.data

        val visitableList = visitableListSlot.captured
        val productItemViewModelList = visitableList.filterIsInstance<ProductItemDataView>()

        val topAdsTemplatePosition = getTopAdsProductPositionByTemplate(searchProductModel)

        var expectedTopAdsProductPosition = topAdsPositionStart + 1
        var expectedOrganicProductPosition = organicPositionStart + 1

        var topAdsProductListIndex = 0
        var organicProductListIndex = 0

        productItemViewModelList.forEachIndexed { index, productItem ->
            if (topAdsTemplatePosition.contains(index)) {
                productItem.assertTopAdsProduct(
                    topAdsProductList[topAdsProductListIndex],
                    expectedTopAdsProductPosition,
                    expectedProductListType,
                    expectedShowButtonATC
                )
                expectedTopAdsProductPosition++
                topAdsProductListIndex++
            } else {
                productItem.assertOrganicProductReimagine(
                    organicProductList[organicProductListIndex],
                    expectedOrganicProductPosition,
                    "",
                    expectedProductListType,
                    expectedBlurred
                )
                expectedOrganicProductPosition++
                organicProductListIndex++
            }
        }
    }

    protected fun Visitable<*>.assertOrganicProductReimagine(
        organicProduct: SearchProductV5.Data.Product,
        position: Int,
        expectedPageTitle: String = "",
        productListType: String = "",
        expectedBlurred: Boolean = true
    ) {
        val productItem = this as ProductItemDataView

        productItem.isOrganicAds shouldBe organicProduct.isOrganicAds()
        productItem.position shouldBe position

        if (organicProduct.isOrganicAds()) {
            productItem.topadsClickUrl shouldBe organicProduct.ads.productClickURL
            productItem.topadsImpressionUrl shouldBe organicProduct.ads.productViewURL
            productItem.topadsWishlistUrl shouldBe organicProduct.ads.productWishlistURL
            productItem.topadsTag shouldBe organicProduct.ads.tag
        } else {
            productItem.topadsClickUrl shouldBe ""
            productItem.topadsImpressionUrl shouldBe ""
            productItem.topadsWishlistUrl shouldBe ""
            productItem.topadsTag shouldBe 0
        }

        productItem.productID shouldBe organicProduct.id
        productItem.productName shouldBe organicProduct.name
        productItem.price shouldBe organicProduct.price.text
        productItem.pageTitle shouldBe expectedPageTitle
        productItem.productListType shouldBe productListType
        productItem.parentId shouldBe organicProduct.meta.parentID
        productItem.priceRange shouldBe organicProduct.price.range
        productItem.priceInt shouldBe organicProduct.price.number
        productItem.originalPrice shouldBe organicProduct.price.original
        productItem.discountPercentage shouldBe organicProduct.price.discountPercentage.toInt()
        productItem.ratingString shouldBe organicProduct.rating
        productItem.imageUrl shouldBe organicProduct.mediaURL.image
        productItem.imageUrl300 shouldBe organicProduct.mediaURL.image300
        productItem.imageUrl700 shouldBe organicProduct.mediaURL.image700
        productItem.warehouseID shouldBe organicProduct.meta.warehouseID
        productItem.shopID shouldBe organicProduct.shop.id
        productItem.shopName shouldBe organicProduct.shop.name
        productItem.shopCity shouldBe organicProduct.shop.city
        productItem.shopUrl shouldBe organicProduct.shop.url
        productItem.isWishlisted shouldBe organicProduct.wishlist

        productItem.badgesList?.size shouldBe 1
        productItem.badgesList!!.first().run {
            this@run.imageUrl shouldBe organicProduct.badge.url
            this@run.title shouldBe organicProduct.badge.title
            this@run.isShown shouldBe true
        }

        productItem.categoryID shouldBe organicProduct.category.id
        productItem.categoryName shouldBe organicProduct.category.name
        productItem.categoryBreadcrumb shouldBe organicProduct.category.breadcrumb

        productItem.labelGroupList?.size shouldBe organicProduct.labelGroupList.size
        productItem.labelGroupList!!.forEachIndexed { labelGroupIndex, labelGroupDataView ->
            val expectedLabelGroup = organicProduct.labelGroupList[labelGroupIndex]

            labelGroupDataView.position shouldBe expectedLabelGroup.position
            labelGroupDataView.title shouldBe expectedLabelGroup.title
            labelGroupDataView.type shouldBe expectedLabelGroup.type
            labelGroupDataView.imageUrl shouldBe expectedLabelGroup.url

            labelGroupDataView.styleList.forEachIndexed { styleIndex, styleDataView ->
                val expectedStyle = expectedLabelGroup.styleList[styleIndex]
                styleDataView.key shouldBe expectedStyle.key
                styleDataView.value shouldBe expectedStyle.value
            }
        }

        productItem.labelGroupVariantList.size shouldBe organicProduct.labelGroupVariantList.size
        productItem.labelGroupVariantList.forEachIndexed { index, labelGroupVariantDataView ->
            val labelGroupVariantModel = organicProduct.labelGroupVariantList[index]
            labelGroupVariantDataView.title shouldBe labelGroupVariantModel.title
            labelGroupVariantDataView.type shouldBe labelGroupVariantModel.type
            labelGroupVariantDataView.typeVariant shouldBe labelGroupVariantModel.typeVariant
            labelGroupVariantDataView.hexColor shouldBe labelGroupVariantModel.hexColor
        }

        productItem.freeOngkirDataView.imageUrl shouldBe organicProduct.freeShipping.url
        productItem.freeOngkirDataView.isActive shouldBe true

        productItem.productUrl shouldBe organicProduct.url
        productItem.applink shouldBe organicProduct.applink
        productItem.customVideoURL shouldBe organicProduct.mediaURL.videoCustom
        productItem.isPortrait shouldBe organicProduct.meta.isPortrait
        productItem.isImageBlurred shouldBe (organicProduct.meta.isImageBlurred && expectedBlurred)

        productItem.boosterList shouldBe ""
        productItem.sourceEngine shouldBe ""
        productItem.showButtonAtc shouldBe false
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
        page: Int = 1
    ) {
        val headlineAdsCount = if (page <= 1) 2 else 1

        every { topAdsHeadlineHelper.processHeadlineAds(any(), any(), any()) } answers {
            searchProductModel.cpmModel.data.take(headlineAdsCount).forEachIndexed { index, data ->
                val isUseSeparator = index > 0 || page > 1

                thirdArg<(Int, ArrayList<CpmData>, Boolean) -> Unit>().invoke(
                    index,
                    arrayListOf(data),
                    isUseSeparator
                )
            }
        }
    }

    protected fun `Given search reimagine rollence product card will return non control variant`() {
        every { reimagineRollence.search3ProductCard() } returns Search3ProductCard.PRODUCT_CARD_SRE_2024
    }

    protected fun getListDeduplication(): String {
        return deduplication.getProductIdList()
    }

    @After
    open fun tearDown() {
        productListPresenter.detachView()
    }
}
