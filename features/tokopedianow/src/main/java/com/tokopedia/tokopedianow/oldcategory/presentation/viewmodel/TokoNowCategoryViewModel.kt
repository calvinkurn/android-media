package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_NO_RESULT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.PREFIX_ALL
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategorySharingModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryTrackerModel
import com.tokopedia.tokopedianow.oldcategory.domain.model.TokonowCategoryDetail
import com.tokopedia.tokopedianow.oldcategory.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokopedianow.oldcategory.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.oldcategory.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment.Companion.DEFAULT_CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.oldcategory.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L1
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_SERVICE_TYPE
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryMenuMapper
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse.CategoryResponse
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_LIST_DEPTH
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryViewModel @Inject constructor(
    baseDispatcher: CoroutineDispatchers,
    @param:Named(TOKONOW_CATEGORY_L1)
    val categoryL1: String,
    @param:Named(TOKONOW_CATEGORY_L2)
    val categoryL2: String,
    @param:Named(TOKONOW_CATEGORY_SERVICE_TYPE)
    val externalServiceType: String,
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
    private val getCategoryListUseCase: GetCategoryListUseCase,
    setUserPreferenceUseCase: SetUserPreferenceUseCase,
    chooseAddressWrapper: ChooseAddressWrapper,
    affiliateService: NowAffiliateService,
    userSession: UserSessionInterface
) : BaseSearchCategoryViewModel(
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
    userSession
) {

    private val openScreenTrackingUrlMutableLiveData = SingleLiveEvent<CategoryTrackerModel>()
    val openScreenTrackingUrlLiveData: LiveData<CategoryTrackerModel> = openScreenTrackingUrlMutableLiveData

    private val shareMutableLiveData = SingleLiveEvent<CategorySharingModel>()
    val shareLiveData: LiveData<CategorySharingModel> = shareMutableLiveData

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
    override val tickerPageSource: String
        get() = GetTargetedTickerUseCase.CATEGORY_PAGE

    private fun getCategoryIdForTracking() =
        if (categoryL2.isNotEmpty()) {
            "$categoryL1/$categoryL2"
        } else {
            categoryL1
        }

    override fun loadFirstPage() {
        getCategoryFirstPageUseCase.cancelJobs()
        getCategoryFirstPageUseCase.execute(
            this::onGetCategoryFirstPageSuccess,
            this::onGetFirstPageError,
            createRequestParams()
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
            targetedTicker = categoryModel.targetedTicker
        )

        val contentDataView = ContentDataView(
            aceSearchProductData = searchProduct.data,
            repurchaseWidget = categoryModel.tokonowRepurchaseWidget
        )

        val isActive = categoryModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive

        onGetFirstPageSuccess(headerDataView, contentDataView, searchProduct, isActive)

        sendOpenScreenTrackingUrl(categoryModel)
        setSharingModel(categoryModel)
    }

    override fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView {
        return TitleDataView(
            titleType = CategoryTitle(headerDataView.title),
            hasSeeAllCategoryButton = true,
            chooseAddressData = chooseAddressData
        )
    }

    override fun createFooterVisitableList(): List<Visitable<*>> {
        val recomData = TokoNowProductRecommendationUiModel(
            requestParam = createProductRecommendationRequestParam(
                pageName = TOKONOW_CLP
            )
        )
        return listOf(
            createAisleDataView(),
            recomData
        )
    }

    private fun createAisleDataView() = CategoryAisleDataView(
        listOf(
            createAisleItem(navigation?.prev),
            createAisleItem(navigation?.next)
        ),
        chooseAddressData?.service_type.orEmpty()
    )

    private fun createAisleItem(navigationItem: NavigationItem?): CategoryAisleItemDataView {
        return CategoryAisleItemDataView(
            id = navigationItem?.id ?: "",
            name = navigationItem?.name ?: "",
            imgUrl = navigationItem?.imageUrl ?: "",
            applink = navigationItem?.applinks ?: ""
        )
    }

    override fun createVisitableListWithEmptyProduct(
        violation: AceSearchProductModel.Violation
    ) {
        super.createVisitableListWithEmptyProduct(violation)
        val categoryMenuIndex = minOf(visitableList.size, 2)
        val categoryMenuUIModel = TokoNowCategoryMenuUiModel(
            state = TokoNowLayoutState.LOADING
        )
        visitableList.add(categoryMenuIndex, categoryMenuUIModel)
    }

    override fun processEmptyState(isEmptyProductList: Boolean) {
        loadCategoryMenu(isEmptyProductList)
    }

    override fun onViewCreated(source: MiniCartSource?) {
        val currentServiceType = chooseAddressData?.getServiceType()

        if (externalServiceType != currentServiceType && externalServiceType.isNotBlank()) {
            setUserPreference(externalServiceType)
        } else {
            super.onViewCreated(source)
        }
    }

    private fun loadCategoryMenu(isEmptyProductList: Boolean) {
        launchCatchError(
            block = { tryLoadCategoryMenu(isEmptyProductList) },
            onError = { catchLoadCategoryMenuError() }
        )
    }

    private suspend fun tryLoadCategoryMenu(isEmptyProductList: Boolean) {
        if (!isEmptyProductList) return

        val categoryList = getCategoryList()

        val seeAllAppLink = ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY

        updateCategoryUIModel(
            categoryItemListUIModel = CategoryMenuMapper.mapToCategoryList(
                response = categoryList,
                seeAllAppLink = seeAllAppLink
            ),
            categoryUIModelState = TokoNowLayoutState.SHOW,
            seeAllAppLink = seeAllAppLink
        )
    }

    private suspend fun getCategoryList(): List<CategoryResponse> {
        val warehouses = AddressMapper.mapToWarehousesData(chooseAddressData)
        return getCategoryListUseCase.execute(warehouses, CATEGORY_LIST_DEPTH).data
    }

    private suspend fun updateCategoryUIModel(
        categoryItemListUIModel: List<Visitable<*>>?,
        categoryUIModelState: Int,
        seeAllAppLink: String = ""
    ) {
        val currentCategoryUIModel = getCategoryMenuUIModelInVisitableList() ?: return

        val updatedCategoryUiModel = currentCategoryUIModel.copy(
            categoryListUiModel = categoryItemListUIModel,
            state = categoryUIModelState,
            seeAllAppLink = seeAllAppLink
        )

        replaceCategoryUIModelInVisitableList(currentCategoryUIModel, updatedCategoryUiModel)

        suspendUpdateVisitableListLiveData()
    }

    private fun getCategoryMenuUIModelInVisitableList(): TokoNowCategoryMenuUiModel? {
        return visitableList
            .find { it is TokoNowCategoryMenuUiModel }
            as? TokoNowCategoryMenuUiModel
    }

    private fun replaceCategoryUIModelInVisitableList(
        current: TokoNowCategoryMenuUiModel,
        updated: TokoNowCategoryMenuUiModel
    ) {
        val position = visitableList.indexOf(current)

        visitableList.removeAt(position)
        visitableList.add(position, updated)
    }

    private suspend fun catchLoadCategoryMenuError() {
        updateCategoryUIModel(
            categoryItemListUIModel = null,
            categoryUIModelState = TokoNowLayoutState.HIDE
        )
    }

    fun onCategoryMenuRetry() {
        processEmptyState(true)
    }

    fun getCurrentCategoryId(categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String): String {
        return when {
            categoryIdLvl3.isNotBlank() && categoryIdLvl3 != DEFAULT_CATEGORY_ID -> {
                categoryIdLvl3
            }
            categoryIdLvl2.isNotBlank() && categoryIdLvl2 != DEFAULT_CATEGORY_ID -> {
                categoryIdLvl2
            }
            else -> {
                categoryIdLvl1
            }
        }
    }

    private fun sendOpenScreenTrackingUrl(categoryModel: CategoryModel) {
        openScreenTrackingUrlMutableLiveData.value = CategoryTrackerModel(
            id = categoryModel.categoryDetail.data.id,
            name = categoryModel.categoryDetail.data.name,
            url = categoryModel.categoryDetail.data.url
        )
    }

    private fun setSharingModel(categoryModel: CategoryModel) {
        var categoryIdLvl2 = DEFAULT_CATEGORY_ID
        var categoryIdLvl3 = DEFAULT_CATEGORY_ID

        queryParam.forEach {
            when (it.key) {
                "${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}" -> categoryIdLvl2 = it.value
                SearchApiConst.SC -> categoryIdLvl3 = it.value
            }
        }

        val title = getTitleCategory(categoryIdLvl2, categoryModel)
        val constructedLink = getConstructedLink(categoryModel.categoryDetail.data.url, categoryIdLvl2, categoryIdLvl3)
        val utmCampaignList = getUtmCampaignList(categoryIdLvl2, categoryIdLvl3)

        shareMutableLiveData.value = CategorySharingModel(
            categoryIdLvl2 = categoryIdLvl2,
            categoryIdLvl3 = categoryIdLvl3,
            title = title,
            deeplinkParam = constructedLink.first,
            url = constructedLink.second,
            utmCampaignList = utmCampaignList
        )
    }

    private fun getTitleCategory(categoryIdLvl2: String, categoryModel: CategoryModel): String {
        return if (categoryIdLvl2.isNotBlank() && categoryIdLvl2 != DEFAULT_CATEGORY_ID) {
            categoryModel.quickFilter.filter.first().title.removePrefix(PREFIX_ALL).trim()
        } else {
            categoryModel.categoryDetail.data.name
        }
    }

    private fun getConstructedLink(categoryUrl: String, categoryIdLvl2: String, categoryIdLvl3: String): Pair<String, String> {
        var deeplinkParam = "${TokoNowCategoryFragment.DEFAULT_DEEPLINK_PARAM}/$categoryL1"
        var url = categoryUrl
        if (categoryIdLvl2.isNotBlank() && categoryIdLvl2 != DEFAULT_CATEGORY_ID) {
            deeplinkParam += "/$categoryIdLvl2"
            url += String.format(TokoNowCategoryFragment.URL_PARAM_LVL_2, categoryIdLvl2)

            if (categoryIdLvl3.isNotBlank() && categoryIdLvl3 != DEFAULT_CATEGORY_ID) {
                deeplinkParam += String.format(TokoNowCategoryFragment.DEEPLINK_PARAM_LVL_3, categoryIdLvl3)
                url += String.format(TokoNowCategoryFragment.URL_PARAM_LVL_3, categoryIdLvl3)
            }
        }
        return Pair(deeplinkParam, url)
    }

    private fun getUtmCampaignList(categoryIdLvl2: String, categoryIdLvl3: String): List<String> {
        val categoryId: String
        val categoryLvl: Int
        when {
            categoryIdLvl3.isNotBlank() && categoryIdLvl3 != DEFAULT_CATEGORY_ID -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_3
                categoryId = categoryIdLvl3
            }
            categoryIdLvl2.isNotBlank() && categoryIdLvl2 != DEFAULT_CATEGORY_ID -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_2
                categoryId = categoryIdLvl2
            }
            else -> {
                categoryLvl = TokoNowCategoryFragment.CATEGORY_LVL_1
                categoryId = categoryL1
            }
        }
        return listOf(String.format(TokoNowCategoryFragment.PAGE_TYPE_CATEGORY, categoryLvl), categoryId)
    }

    override fun executeLoadMore() {
        getCategoryLoadMorePageUseCase.execute(
            this::onGetCategoryLoadMorePageSuccess,
            this::onGetCategoryLoadMorePageError,
            createRequestParams()
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
        pageName: String
    ): List<String> {
        if (pageName == TOKONOW_NO_RESULT) return listOf()

        val tokonowParam = FilterHelper.createParamsWithoutExcludes(queryParam)
        val categoryFilterId = tokonowParam[SearchApiConst.SC] ?: ""

        return if (categoryFilterId.isNotEmpty()) {
            listOf(categoryFilterId)
        } else {
            listOf(tokonowParam[SearchApiConst.SRP_PAGE_ID] ?: "")
        }
    }
}
