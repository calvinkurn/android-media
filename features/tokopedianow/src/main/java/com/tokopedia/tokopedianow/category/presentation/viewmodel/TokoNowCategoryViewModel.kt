package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokopedianow.category.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokopedianow.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CLP
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
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
        addToCartUseCase: AddToCartUseCase,
        updateCartUseCase: UpdateCartUseCase,
        deleteCartUseCase: DeleteCartUseCase,
        getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
        getRecommendationUseCase: GetRecommendationUseCase,
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
        userSession: UserSessionInterface,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        addToCartUseCase,
        updateCartUseCase,
        deleteCartUseCase,
        getWarehouseUseCase,
        getRecommendationUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
        userSession,
) {

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

        val headerDataView = HeaderDataView(
                title = categoryModel.categoryDetail.data.name,
                hasSeeAllCategoryButton = true,
                aceSearchProductHeader = categoryModel.searchProduct.header,
                categoryFilterDataValue = categoryModel.categoryFilter,
                quickFilterDataValue = categoryModel.quickFilter,
                bannerChannel = categoryModel.bannerChannel,
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = categoryModel.searchProduct.data,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    override fun createFooterVisitableList() = listOf(
            createAisleDataView(),
            RecommendationCarouselDataView(),
    )

    private fun createAisleDataView() = CategoryAisleDataView(
            listOf(
                    createAisleItem(navigation?.prev),
                    createAisleItem(navigation?.next),
            )
    )

    private fun createAisleItem(navigationItem: NavigationItem?): CategoryAisleItemDataView {
        return CategoryAisleItemDataView(
                id = navigationItem?.id ?: "",
                name = navigationItem?.name ?: "",
                imgUrl = navigationItem?.imageUrl ?: "",
                applink = navigationItem?.applinks ?: "",
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

    fun onBindRecommendationCarousel(element: RecommendationCarouselDataView, adapterPosition: Int) {
        launchCatchError(
                block = { getRecommendationCarousel(element, adapterPosition) },
                onError = { getRecommendationCarouselError(element, adapterPosition) },
        )
    }

    private suspend fun getRecommendationCarousel(
            element: RecommendationCarouselDataView,
            adapterPosition: Int,
    ) {
        if (element.carouselData.state == STATE_READY) return

        val tokonowParam = FilterHelper.createParamsWithoutExcludes(queryParam)
        val recomCategoryId = getRecomCategoryId(tokonowParam)
        val getRecommendationRequestParam = GetRecommendationRequestParam(
                pageName = TOKONOW_CLP,
                categoryIds = listOf(recomCategoryId),
                xSource = RECOM_WIDGET,
                isTokonow = true,
                pageNumber = PAGE_NUMBER_RECOM_WIDGET,
        )
        val recommendationList = getRecommendationUseCase.getData(getRecommendationRequestParam)

        element.carouselData = RecommendationCarouselData(
                state = STATE_READY,
                recommendationData = recommendationList.firstOrNull() ?: RecommendationWidget()
        )

        updatedVisitableIndicesMutableLiveData.value = listOf(adapterPosition)
    }

    private fun getRecomCategoryId(tokonowParam: Map<String, String>): String {
        val categoryFilterId = tokonowParam[SearchApiConst.SC] ?: ""

        return if (categoryFilterId.isNotEmpty()) categoryFilterId
        else (tokonowParam[SearchApiConst.SRP_PAGE_ID] ?: "")
    }

    private fun getRecommendationCarouselError(
            element: RecommendationCarouselDataView,
            adapterPosition: Int,
    ) {
        element.carouselData = RecommendationCarouselData(state = STATE_FAILED)

        updatedVisitableIndicesMutableLiveData.value = listOf(adapterPosition)
    }
}
