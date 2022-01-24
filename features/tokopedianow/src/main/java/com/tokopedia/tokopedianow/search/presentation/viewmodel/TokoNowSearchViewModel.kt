package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.JumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.SearchCategoryJumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokopedianow.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartProductItem
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.NonVariantATCDataView
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
    getRecommendationUseCase: GetRecommendationUseCase,
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
        getRecommendationUseCase,
        setUserPreferenceUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
        userSession,
) {

    private val addToCartBroadMatchTrackingMutableLiveData =
        SingleLiveEvent<Triple<Int, String, BroadMatchItemDataView>>()
    val addToCartBroadMatchTrackingLiveData: LiveData<Triple<Int, String, BroadMatchItemDataView>> =
        addToCartBroadMatchTrackingMutableLiveData

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    private var responseCode = ""
    private var suggestionModel: AceSearchProductModel.Suggestion? = null
    private var searchCategoryJumper: SearchCategoryJumperData? = null
    private var related: AceSearchProductModel.Related? = null

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

        onGetFirstPageSuccess(headerDataView, contentDataView, searchProduct)
    }

    override fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView {
        val titleType = if (query.isEmpty()) AllProductTitle else SearchTitle
        val hasSeeAllCategoryButton = query.isEmpty()

        return TitleDataView(
                titleType = titleType,
                hasSeeAllCategoryButton = hasSeeAllCategoryButton,
                serviceType = chooseAddressData?.service_type.orEmpty(),
                is15mAvailable = chooseAddressData?.warehouses?.find { it.service_type == NOW_15M }?.warehouse_id.orZero() != 0L
        )
    }

    override fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {
        if (!shouldShowSuggestion()) return

        processSuggestionModel { suggestionDataView ->
            val suggestionDataViewIndex = determineSuggestionDataViewIndex(headerList)

            headerList.add(suggestionDataViewIndex, suggestionDataView)
        }
    }

    private fun shouldShowSuggestion() = showSuggestionResponseCodeList.contains(responseCode)

    private fun processSuggestionModel(action: (SuggestionDataView) -> Unit) {
        val suggestionModel = suggestionModel ?: return

        if (suggestionModel.text.isNotEmpty()) {
            val suggestionDataView = createSuggestionDataView(suggestionModel)
            action(suggestionDataView)
        }

        this.suggestionModel = null
    }

    private fun createSuggestionDataView(suggestionModel: AceSearchProductModel.Suggestion) =
        SuggestionDataView(
            text = suggestionModel.text,
            query = suggestionModel.query,
            suggestion = suggestionModel.suggestion,
        )

    private fun determineSuggestionDataViewIndex(headerList: List<Visitable<*>>): Int {
        val quickFilterIndex = headerList.indexOfFirst { it is QuickFilterDataView }

        return quickFilterIndex + 1
    }

    override fun createFooterVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = createBroadMatchVisitableList()

        return broadMatchVisitableList + if (chooseAddressData?.service_type == NOW_15M) {
            listOf(
                createCategoryJumperDataView(),
            )
        } else {
            listOf(
                createCategoryJumperDataView(),
                CTATokopediaNowHomeDataView(),
            )
        }
    }

    private fun createBroadMatchVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = mutableListOf<Visitable<SearchTypeFactory>>()

        if (!isShowBroadMatch()) return broadMatchVisitableList

        processSuggestionModel { suggestionDataView ->
            broadMatchVisitableList.add(suggestionDataView)
        }

        processBroadMatch { broadMatchDataView ->
            broadMatchVisitableList.add(broadMatchDataView)
        }

        return broadMatchVisitableList
    }

    private fun isShowBroadMatch() =
        showBroadMatchResponseCodeList.contains(responseCode)

    private fun processBroadMatch(action: (BroadMatchDataView) -> Unit) {
        related?.otherRelatedList?.forEach { otherRelated ->
            val broadMatchDataView = createBroadMatchDataView(otherRelated)
            action(broadMatchDataView)
        }

        related = null
    }

    private fun createBroadMatchDataView(otherRelated: AceSearchProductModel.OtherRelated) =
        BroadMatchDataView(
            keyword = otherRelated.keyword,
            applink = otherRelated.applink,
            broadMatchItemDataViewList = otherRelated.productList
                .mapIndexed { index, otherRelatedProduct ->
                    BroadMatchItemDataView(
                        id = otherRelatedProduct.id,
                        name = otherRelatedProduct.name,
                        price = otherRelatedProduct.price,
                        imageUrl = otherRelatedProduct.imageUrl,
                        applink = otherRelatedProduct.applink,
                        priceString = otherRelatedProduct.priceString,
                        position = index + 1,
                        alternativeKeyword = otherRelated.keyword,
                        ratingAverage = otherRelatedProduct.ratingAverage,
                        labelGroupDataList = otherRelatedProduct.labelGroupList
                            .map(::mapToLabelGroupDataView),
                        shop = BroadMatchItemDataView.Shop(id = otherRelatedProduct.shop.id),
                        nonVariantATC = NonVariantATCDataView(
                            minQuantity = otherRelatedProduct.minOrder,
                            maxQuantity = otherRelatedProduct.stock,
                            quantity = cartService.getProductQuantity(otherRelatedProduct.id)
                        ),
                    )
                }
        )


    private fun createCategoryJumperDataView(): CategoryJumperDataView {
        val categoryJumperItemList =
                searchCategoryJumper
                        ?.getJumperItemList()
                        ?.map(this::mapToCategoryJumperItem)
                        ?: listOf()

        return CategoryJumperDataView(
                title = searchCategoryJumper?.getTitle() ?: "",
                itemList = categoryJumperItemList,
                serviceType = chooseAddressData?.service_type.orEmpty()
        )
    }

    private fun mapToCategoryJumperItem(jumperData: JumperData) =
            CategoryJumperDataView.Item(
                    title = jumperData.title,
                    applink = jumperData.applink,
            )

    override fun createVisitableListWithEmptyProduct() {
        if (isShowBroadMatch())
            createVisitableListWithEmptyProductBroadmatch()
        else
            super.createVisitableListWithEmptyProduct()
    }

    private fun createVisitableListWithEmptyProductBroadmatch() {
        visitableList.add(chooseAddressDataView)
        visitableList.addAll(createBroadMatchVisitableList())
    }

    override fun getKeywordForGeneralSearchTracking() = query

    override fun getPageSourceForGeneralSearchTracking() =
        "$TOKOPEDIA_NOW.$TOKONOW.$LOCAL_SEARCH.$warehouseId"

    override fun executeLoadMore() {
        getSearchLoadMorePageUseCase.cancelJobs()
        getSearchLoadMorePageUseCase.execute(
                ::onGetSearchLoadMorePageSuccess,
                ::onGetSearchLoadMorePageError,
                createRequestParams(),
        )
    }

    private fun onGetSearchLoadMorePageSuccess(searchModel: SearchModel) {
        val contentDataView = ContentDataView(aceSearchProductData = searchModel.searchProduct.data)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) {

    }

    override fun updateQuantityInVisitable(
        visitable: Visitable<*>,
        index: Int,
        updatedProductIndices: MutableList<Int>,
    ) {
        super.updateQuantityInVisitable(visitable, index, updatedProductIndices)

        if (visitable is BroadMatchDataView)
            updateBroadMatchQuantities(visitable, index, updatedProductIndices)
    }

    private fun updateBroadMatchQuantities(
        broadMatchDataView: BroadMatchDataView,
        index: Int,
        updatedProductIndices: MutableList<Int>,
    ) {
        broadMatchDataView.broadMatchItemDataViewList.forEach { broadMatchItemDataView ->
            updateBroadMatchItemQuantity(broadMatchItemDataView, index, updatedProductIndices)
        }
    }

    private fun updateBroadMatchItemQuantity(
        broadMatchItemDataView: BroadMatchItemDataView,
        index: Int,
        updatedProductIndices: MutableList<Int>,
    ) {
        val nonVariantATC = broadMatchItemDataView.nonVariantATC ?: return
        val quantity = cartService.getProductQuantity(broadMatchItemDataView.id)

        if (nonVariantATC.quantity != quantity) {
            nonVariantATC.quantity = quantity

            if (!updatedProductIndices.contains(index))
                updatedProductIndices.add(index)
        }
    }

    fun onViewATCBroadMatchItem(
        broadMatchItem: BroadMatchItemDataView,
        quantity: Int,
        broadMatchIndex: Int,
    ) {
        val nonVariantATC = broadMatchItem.nonVariantATC ?: return

        val productId = broadMatchItem.id
        val shopId = broadMatchItem.shop.id
        val currentQuantity = nonVariantATC.quantity

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                sendAddToCartBroadMatchItemTracking(quantity, it, broadMatchItem)
                onAddToCartSuccessBroadMatchItem(broadMatchItem, it.data.quantity)
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
            },
            onSuccessUpdateCart = {
                onAddToCartSuccessBroadMatchItem(broadMatchItem, quantity)
            },
            onSuccessDeleteCart = {
                onAddToCartSuccessBroadMatchItem(broadMatchItem, 0)
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(broadMatchIndex)
            },
        )
    }

    private fun sendAddToCartBroadMatchItemTracking(
        quantity: Int,
        addToCartDataModel: AddToCartDataModel,
        broadMatchItem: BroadMatchItemDataView,
    ) {
        addToCartBroadMatchTrackingMutableLiveData.value =
            Triple(quantity, addToCartDataModel.data.cartId, broadMatchItem)
    }

    private fun onAddToCartSuccessBroadMatchItem(
        broadMatchItem: BroadMatchItemDataView,
        updatedQuantity: Int,
    ) {
        updateBroadMatchItemQuantity(broadMatchItem, updatedQuantity)
        refreshMiniCart()
    }

    private fun updateBroadMatchItemQuantity(
        broadMatchItem: BroadMatchItemDataView,
        updatedQuantity: Int,
    ) {
        broadMatchItem.nonVariantATC?.quantity = updatedQuantity
    }

    companion object {
        private val showBroadMatchResponseCodeList = listOf("4", "5")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }

    override fun getRecomKeywords() = listOf(query)
}
