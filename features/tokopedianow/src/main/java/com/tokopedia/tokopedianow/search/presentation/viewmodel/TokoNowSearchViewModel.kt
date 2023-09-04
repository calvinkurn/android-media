package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.search.domain.mapper.CategoryJumperMapper.createCategoryJumperDataView
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.addBroadMatchDataView
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.addSuggestionDataView
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.updateSuggestionDataView
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.SearchCategoryJumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokopedianow.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartProductItem
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

class TokoNowSearchViewModel @Inject constructor (
    baseDispatcher: CoroutineDispatchers,
    @Named(SEARCH_QUERY_PARAM_MAP)
    queryParamMap: Map<String, String>,
    @param:Named(SEARCH_FIRST_PAGE_USE_CASE)
    private val getSearchFirstPageUseCase: UseCase<SearchModel>,
    @param:Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
    private val getSearchLoadMorePageUseCase: UseCase<SearchModel>,
    getFilterUseCase: GetFilterUseCase,
    getProductCountUseCase: UseCase<String>,
    getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    cartService: CartService,
    getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    setUserPreferenceUseCase: SetUserPreferenceUseCase,
    chooseAddressWrapper: ChooseAddressWrapper,
    affiliateService: NowAffiliateService,
    userSession: UserSessionInterface
): BaseSearchCategoryViewModel(
    baseDispatcher,
    queryParamMap,
    getFilterUseCase,
    getProductCountUseCase,
    getMiniCartListSimplifiedUseCase,
    cartService,
    getWarehouseUseCase,
    setUserPreferenceUseCase,
    chooseAddressWrapper,
    affiliateService,
    userSession,
) {
    companion object {
        private val showBroadMatchResponseCodeList = listOf("4", "5")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }

    private val addToCartBroadMatchTrackingMutableLiveData: SingleLiveEvent<Triple<Int, String, ProductCardCompactCarouselItemUiModel>> = SingleLiveEvent()
    private var responseCode: String = ""
    private var suggestionModel: AceSearchProductModel.Suggestion? = null
    private var searchCategoryJumper: SearchCategoryJumperData? = null
    private var related: AceSearchProductModel.Related? = null
    private var recommendationCategoryId: String = ""

    val addToCartBroadMatchTrackingLiveData: LiveData<Triple<Int, String, ProductCardCompactCarouselItemUiModel>> = addToCartBroadMatchTrackingMutableLiveData
    val query = queryParamMap[SearchApiConst.Q].orEmpty()

    override val tokonowSource: String
        get() = TOKONOW
    override val tickerPageSource: String
        get() = GetTargetedTickerUseCase.SEARCH_PAGE

    override fun loadFirstPage() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                ::onGetSearchFirstPageSuccess,
                ::onGetFirstPageError,
                createRequestParams()
        )
    }

    override fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView {
        val titleType = if (query.isEmpty()) AllProductTitle else SearchTitle
        val hasSeeAllCategoryButton = query.isEmpty()

        return TitleDataView(
            titleType = titleType,
            hasSeeAllCategoryButton = hasSeeAllCategoryButton,
            chooseAddressData = chooseAddressData
        )
    }

    override fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {
        if (!shouldShowSuggestion()) return

        val suggestionDataViewIndex = determineSuggestionDataViewIndex(headerList)

        headerList.updateSuggestionDataView(suggestionModel, suggestionDataViewIndex)
    }

    override fun createVisitableListWithEmptyProduct(
        violation: AceSearchProductModel.Violation
    ) {
        if (isShowBroadMatch())
            createVisitableListWithEmptyProductBroadmatch()
        else
            super.createVisitableListWithEmptyProduct(violation)
    }

    override fun getKeywordForGeneralSearchTracking() = query

    override fun executeLoadMore() {
        getSearchLoadMorePageUseCase.cancelJobs()
        getSearchLoadMorePageUseCase.execute(
            ::onGetSearchLoadMorePageSuccess,
            ::onGetSearchLoadMorePageError,
            createRequestParams(),
        )
    }

    override fun createFooterVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = createBroadMatchVisitableList()
        return broadMatchVisitableList + if (serviceType == NOW_15M) {
            listOf(
                createCategoryJumperDataView(
                    searchCategoryJumper = searchCategoryJumper,
                    chooseAddressData = chooseAddressData
                )
            )
        } else {
            listOf(
                createCategoryJumperDataView(
                    searchCategoryJumper = searchCategoryJumper,
                    chooseAddressData = chooseAddressData
                ),
                CTATokopediaNowHomeDataView(),
            )
        }
    }

    override fun getRecomKeywords() = listOf(query)

    override fun getRecomCategoryId(pageName: String): List<String> = listOf(recommendationCategoryId)

    override fun createProductAdsParam(): GetProductAdsParam {
        val query = queryParamMutable[SearchApiConst.Q].orEmpty()

        return GetProductAdsParam(
            query = query,
            src = GetProductAdsParam.SRC_SEARCH_TOKONOW,
            userId = userSession.userId,
            addressData = chooseAddressData
        )
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val searchProduct = searchModel.searchProduct
        responseCode = searchModel.getResponseCode()
        suggestionModel = searchModel.getSuggestion()
        searchCategoryJumper = searchModel.searchCategoryJumper
        related = searchModel.getRelated()

        val searchProductHeader = searchProduct.header
        recommendationCategoryId = searchProductHeader.meta.categoryId

        val headerDataView = HeaderDataView(
                title = "",
                aceSearchProductHeader = searchProductHeader,
                categoryFilterDataValue = searchModel.categoryFilter,
                quickFilterDataValue = searchModel.quickFilter,
                bannerChannel = searchModel.bannerChannel,
                targetedTicker = searchModel.targetedTicker
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = searchProduct.data,
                productAds = searchModel.productAds
        )

        val isActive = searchModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive

        onGetFirstPageSuccess(headerDataView, contentDataView, searchProduct,isActive)
    }

    private fun shouldShowSuggestion() = showSuggestionResponseCodeList.contains(responseCode)

    private fun determineSuggestionDataViewIndex(headerList: List<Visitable<*>>): Int {
        val quickFilterIndex = headerList.indexOfFirst { it is QuickFilterDataView }

        return quickFilterIndex + 1
    }

    private fun createBroadMatchVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = mutableListOf<Visitable<SearchTypeFactory>>()

        if (!isShowBroadMatch()) return broadMatchVisitableList

        broadMatchVisitableList.addSuggestionDataView(
            suggestionModel = suggestionModel
        )
        broadMatchVisitableList.addBroadMatchDataView(
            related = related,
            cartService = cartService,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        return broadMatchVisitableList
    }

    private fun isShowBroadMatch() =
        showBroadMatchResponseCodeList.contains(responseCode)

    private fun createVisitableListWithEmptyProductBroadmatch() {
        visitableList.add(chooseAddressDataView)
        visitableList.addAll(createBroadMatchVisitableList())
    }

    private fun onGetSearchLoadMorePageSuccess(searchModel: SearchModel) {
        val contentDataView = ContentDataView(
            aceSearchProductData = searchModel.searchProduct.data,
            productAds = searchModel.productAds
        )
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) { /* nothing to do */ }

    private fun sendAddToCartBroadMatchItemTracking(
        quantity: Int,
        addToCartDataModel: AddToCartDataModel,
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
    ) {
        addToCartBroadMatchTrackingMutableLiveData.value = Triple(quantity, addToCartDataModel.data.cartId, broadMatchItem)
    }

    fun onViewATCBroadMatchItem(
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int
    ) {
        val productId = broadMatchItem.productCardModel.productId
        val shopId = broadMatchItem.shopId
        val currentQuantity = broadMatchItem.productCardModel.orderQuantity

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                sendAddToCartBroadMatchItemTracking(quantity, it, broadMatchItem)
                addToCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                updateToolbarNotification()
                refreshMiniCart()
            },
            onSuccessUpdateCart = {
                updateToolbarNotification()
                refreshMiniCart()
            },
            onSuccessDeleteCart = {
                removeFromCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                updateToolbarNotification()
                refreshMiniCart()
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(broadMatchIndex)
            },
        )
    }
}
