package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_NO_RESULT
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.domain.model.CategoryTrackerModel
import com.tokopedia.tokopedianow.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokopedianow.category.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.*
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_LIST_DEPTH
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CATEGORY
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryViewModel @Inject constructor (
    baseDispatcher: CoroutineDispatchers,
    @param:Named(TOKONOW_CATEGORY_L1)
        val categoryL1: String,
    @param:Named(TOKONOW_CATEGORY_L2)
        val categoryL2: String,
    @Named(TOKONOW_CATEGORY_QUERY_PARAM_MAP)
        queryParamMap: Map<String, String>,
    @param:Named(CATEGORY_FIRST_PAGE_USE_CASE)
        private val getCategoryFirstPageUseCase: UseCase<CategoryModel>,
    @param:Named(CATEGORY_LOAD_MORE_PAGE_USE_CASE)
        private val getCategoryLoadMorePageUseCase: UseCase<CategoryModel>,
    getFilterUseCase: UseCase<DynamicFilterModel>,
    getProductCountUseCase: UseCase<String>,
    getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    cartService: CartService,
    getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getRecommendationUseCase: GetRecommendationUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    setUserPreferenceUseCase: SetUserPreferenceUseCase,
    chooseAddressWrapper: ChooseAddressWrapper,
    abTestPlatformWrapper: ABTestPlatformWrapper,
    userSession: UserSessionInterface,
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

    protected val openScreenTrackingUrlMutableLiveData = SingleLiveEvent<CategoryTrackerModel>()
    val openScreenTrackingUrlLiveData: LiveData<CategoryTrackerModel> = openScreenTrackingUrlMutableLiveData

    val categoryIdTracking: String

    private var navigation: TokonowCategoryDetail.Navigation? = null

    init {
        updateQueryParamWithCategoryIds()

        categoryIdTracking = getCategoryIdForTracking()
    }

    private fun updateQueryParamWithCategoryIds() {
        if (categoryL1.isNotEmpty()) {
            queryParamMutable[SearchApiConst.SRP_PAGE_ID] = categoryL1
        }

        if (categoryL2.isNotEmpty()) {
            val categoryFilterKeyWithExclude = "${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}"
            queryParamMutable[categoryFilterKeyWithExclude] = categoryL2
        }
    }

    override val tokonowSource: String
        get() = TOKONOW_DIRECTORY

    private fun getCategoryIdForTracking() =
            if (categoryL2.isNotEmpty()) "$categoryL1/$categoryL2"
            else categoryL1

    override fun loadFirstPage() {
        getCategoryFirstPageUseCase.cancelJobs()
        getCategoryFirstPageUseCase.execute(
                this::onGetCategoryFirstPageSuccess,
                this::onGetFirstPageError,
                createRequestParams(),
        )
    }

    override fun createRequestParams(): RequestParams {
        val requestParams = super.createRequestParams()

        requestParams.putString(CATEGORY_ID, categoryL1)
        requestParams.putString(WAREHOUSE_ID, chooseAddressData?.warehouse_id ?: "")

        return requestParams
    }

    private fun onGetCategoryFirstPageSuccess(categoryModel: CategoryModel) {
        navigation = categoryModel.categoryDetail.data.navigation

        val searchProduct = categoryModel.searchProduct
        val headerDataView = HeaderDataView(
                title = categoryModel.categoryDetail.data.name,
                aceSearchProductHeader = searchProduct.header,
                categoryFilterDataValue = categoryModel.categoryFilter,
                quickFilterDataValue = categoryModel.quickFilter,
                bannerChannel = categoryModel.bannerChannel,
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = searchProduct.data,
                repurchaseWidget = categoryModel.tokonowRepurchaseWidget,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView, searchProduct)

        sendOpenScreenTrackingUrl(categoryModel)
    }

    override fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView {
        return TitleDataView(
            titleType = CategoryTitle(headerDataView.title),
            hasSeeAllCategoryButton = true,
            serviceType = chooseAddressData?.service_type.orEmpty()
        )
    }

    override fun createFooterVisitableList(): List<Visitable<*>> {
        val recomData =
            TokoNowRecommendationCarouselUiModel(pageName = TOKONOW_CLP, isBindWithPageName = true)
        recomData.categoryId = getRecomCategoryId(recomData)
        return listOf(
            createAisleDataView(),
            recomData
        )
    }

    private fun createAisleDataView() = CategoryAisleDataView(
        listOf(
            createAisleItem(navigation?.prev),
            createAisleItem(navigation?.next),
        ),
        chooseAddressData?.service_type.orEmpty()
    )

    private fun createAisleItem(navigationItem: NavigationItem?): CategoryAisleItemDataView {
        return CategoryAisleItemDataView(
                id = navigationItem?.id ?: "",
                name = navigationItem?.name ?: "",
                imgUrl = navigationItem?.imageUrl ?: "",
                applink = navigationItem?.applinks ?: "",
        )
    }

    override fun createVisitableListWithEmptyProduct() {
        super.createVisitableListWithEmptyProduct()

        val categoryGridIndex = minOf(visitableList.size, 2)
        val categoryGridUIModel = TokoNowCategoryGridUiModel(
                id = "",
                title = CATEGORY_GRID_TITLE,
                categoryList = null,
                state = TokoNowLayoutState.LOADING,
        )
        visitableList.add(categoryGridIndex, categoryGridUIModel)
    }

    override fun processEmptyState(isEmptyProductList: Boolean) {
        loadCategoryGrid(isEmptyProductList)
    }

    private fun loadCategoryGrid(isEmptyProductList: Boolean) {
        launchCatchError(
                block = { tryLoadCategoryGrid(isEmptyProductList) },
                onError = { catchLoadCategoryGridError() }
        )
    }

    private suspend fun tryLoadCategoryGrid(isEmptyProductList: Boolean) {
        if (!isEmptyProductList) return

        val categoryList = getCategoryList()

        updateCategoryUIModel(
                categoryItemListUIModel = HomeCategoryMapper.mapToCategoryList(categoryList, warehouseId),
                categoryUIModelState = TokoNowLayoutState.SHOW,
        )
    }

    private suspend fun getCategoryList() =
            getCategoryListUseCase.execute(warehouseId, CATEGORY_LIST_DEPTH)?.data

    private suspend fun updateCategoryUIModel(
            categoryItemListUIModel: List<TokoNowCategoryItemUiModel>?,
            categoryUIModelState: Int,
    ) {
        val currentCategoryUIModel = getCategoryGridUIModelInVisitableList() ?: return

        val updatedCategoryUiModel = currentCategoryUIModel.copy(
                categoryList = categoryItemListUIModel,
                state = categoryUIModelState,
        )

        replaceCategoryUIModelInVisitableList(currentCategoryUIModel, updatedCategoryUiModel)

        suspendUpdateVisitableListLiveData()
    }

    private fun getCategoryGridUIModelInVisitableList(): TokoNowCategoryGridUiModel? {
        return visitableList
                .find { it is TokoNowCategoryGridUiModel }
                as? TokoNowCategoryGridUiModel
    }

    private fun replaceCategoryUIModelInVisitableList(
            current: TokoNowCategoryGridUiModel,
            updated: TokoNowCategoryGridUiModel,
    ) {
        val position = visitableList.indexOf(current)

        visitableList.removeAt(position)
        visitableList.add(position, updated)
    }

    private suspend fun catchLoadCategoryGridError() {
        updateCategoryUIModel(
                categoryItemListUIModel = null,
                categoryUIModelState = TokoNowLayoutState.HIDE
        )
    }

    fun onCategoryGridRetry() {
        processEmptyState(true)
    }

    override fun getPageSourceForGeneralSearchTracking() =
        "$TOKOPEDIA_NOW.$TOKONOW_CATEGORY.$LOCAL_SEARCH.$warehouseId"

    private fun sendOpenScreenTrackingUrl(categoryModel: CategoryModel) {
        openScreenTrackingUrlMutableLiveData.value = CategoryTrackerModel(
            id = categoryModel.categoryDetail.data.id,
            name = categoryModel.categoryDetail.data.name,
            url = categoryModel.categoryDetail.data.url
        )
    }

    override fun executeLoadMore() {
        getCategoryLoadMorePageUseCase.execute(
                this::onGetCategoryLoadMorePageSuccess,
                this::onGetCategoryLoadMorePageError,
                createRequestParams(),
        )
    }

    private fun onGetCategoryLoadMorePageSuccess(categoryModel: CategoryModel) {
        val aceSearchProductData = categoryModel.searchProduct.data
        val contentDataView = ContentDataView(aceSearchProductData = aceSearchProductData)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetCategoryLoadMorePageError(throwable: Throwable) {

    }

    override fun getRecomCategoryId(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel
    ): List<String> {
        if (recommendationCarouselDataView.pageName == TOKONOW_NO_RESULT) return listOf()

        val tokonowParam = FilterHelper.createParamsWithoutExcludes(queryParam)
        val categoryFilterId = tokonowParam[SearchApiConst.SC] ?: ""

        return if (categoryFilterId.isNotEmpty()) listOf(categoryFilterId)
        else listOf(tokonowParam[SearchApiConst.SRP_PAGE_ID] ?: "")
    }
}
