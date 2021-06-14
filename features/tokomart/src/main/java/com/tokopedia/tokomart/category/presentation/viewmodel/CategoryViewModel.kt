package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokomart.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_L1
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokomart.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class CategoryViewModel @Inject constructor (
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
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        addToCartUseCase,
        updateCartUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
) {

    private var navigation: TokonowCategoryDetail.Navigation? = null

    init {
        if (categoryL2.isNotEmpty()) queryParamMutable["${OptionHelper.EXCLUDE_PREFIX}_${SearchApiConst.SC}"] = categoryL2
    }

    override fun onViewCreated() {
        getCategoryFirstPageUseCase.cancelJobs()
        getCategoryFirstPageUseCase.execute(
                this::onGetCategoryFirstPageSuccess,
                this::onGetCategoryFirstPageError,
                createRequestParams(),
        )
    }

    override fun createRequestParams(): RequestParams {
        val requestParams = super.createRequestParams()

        requestParams.putString(CATEGORY_ID, categoryL1)
        requestParams.putString(WAREHOUSE_ID, chooseAddressData?.warehouse_id ?: "")

        return requestParams
    }

    override fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        super.appendMandatoryParams(tokonowQueryParam)

        tokonowQueryParam[SearchApiConst.NAVSOURCE] = TOKONOW_DIRECTORY
        tokonowQueryParam[SearchApiConst.SOURCE] = TOKONOW_DIRECTORY
        tokonowQueryParam[SearchApiConst.SRP_PAGE_ID] = categoryL1
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

    private fun onGetCategoryFirstPageError(throwable: Throwable) {

    }

    override fun createFooterVisitableList() = listOf(createAisleDataView())

    private fun createAisleDataView() = CategoryAisleDataView(
            listOf(
                    createAisleItem(navigation?.prev),
                    createAisleItem(navigation?.next),
            )
    )

    private fun createAisleItem(navigationItem: NavigationItem?): CategoryAisleItemDataView {
        return CategoryAisleItemDataView(
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
}
