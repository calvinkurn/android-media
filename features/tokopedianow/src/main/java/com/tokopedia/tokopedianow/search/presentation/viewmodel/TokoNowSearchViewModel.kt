package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
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
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.ABTestPlatformWrapper
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
    getFilterUseCase: UseCase<DynamicFilterModel>,
    getProductCountUseCase: UseCase<String>,
    getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    cartService: CartService,
    getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    setUserPreferenceUseCase: SetUserPreferenceUseCase,
    chooseAddressWrapper: ChooseAddressWrapper,
    abTestPlatformWrapper: ABTestPlatformWrapper,
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
        abTestPlatformWrapper,
        userSession,
) {
    companion object {
        private val showBroadMatchResponseCodeList = listOf("4", "5")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }

    private val addToCartBroadMatchTrackingMutableLiveData: SingleLiveEvent<Triple<Int, String, TokoNowProductCardCarouselItemUiModel>> = SingleLiveEvent()
    private var responseCode: String = ""
    private var suggestionModel: AceSearchProductModel.Suggestion? = null
    private var searchCategoryJumper: SearchCategoryJumperData? = null
    private var related: AceSearchProductModel.Related? = null

    val addToCartBroadMatchTrackingLiveData: LiveData<Triple<Int, String, TokoNowProductCardCarouselItemUiModel>> = addToCartBroadMatchTrackingMutableLiveData
    val query = queryParamMap[SearchApiConst.Q].orEmpty()

    override val tokonowSource: String
        get() = TOKONOW

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

    override fun createVisitableListWithEmptyProduct() {
        if (isShowBroadMatch())
            createVisitableListWithEmptyProductBroadmatch()
        else
            super.createVisitableListWithEmptyProduct()
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

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val searchProduct = searchModel.searchProduct
        responseCode = searchModel.getResponseCode()
        suggestionModel = searchModel.getSuggestion()
        searchCategoryJumper = searchModel.searchCategoryJumper
        related = searchModel.getRelated()

        val searchProductHeader = searchProduct.header

        val headerDataView = HeaderDataView(
                title = "",
                aceSearchProductHeader = searchProductHeader,
                categoryFilterDataValue = searchModel.categoryFilter,
                quickFilterDataValue = searchModel.quickFilter,
                bannerChannel = searchModel.bannerChannel,
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = searchProduct.data,
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
            cartService = cartService
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
        val contentDataView = ContentDataView(aceSearchProductData = searchModel.searchProduct.data)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) { /* nothing to do */ }

    private fun sendAddToCartBroadMatchItemTracking(
        quantity: Int,
        addToCartDataModel: AddToCartDataModel,
        broadMatchItem: TokoNowProductCardCarouselItemUiModel,
    ) {
        addToCartBroadMatchTrackingMutableLiveData.value = Triple(quantity, addToCartDataModel.data.cartId, broadMatchItem)
    }

    fun onViewATCBroadMatchItem(
        broadMatchItem: TokoNowProductCardCarouselItemUiModel,
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
