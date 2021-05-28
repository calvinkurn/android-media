package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokomart.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_ID
import com.tokopedia.tokomart.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokomart.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class CategoryViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        @param:Named(TOKONOW_CATEGORY_ID)
        val categoryId: Int,
        @Named(TOKONOW_CATEGORY_QUERY_PARAM_MAP)
        queryParamMap: Map<String, String>,
        @param:Named(CATEGORY_FIRST_PAGE_USE_CASE)
        private val getCategoryFirstPageUseCase: UseCase<CategoryModel>,
        @param:Named(CATEGORY_LOAD_MORE_PAGE_USE_CASE)
        private val getCategoryLoadMorePageUseCase: UseCase<CategoryModel>,
        getFilterUseCase: UseCase<DynamicFilterModel>,
        getProductCountUseCase: UseCase<String>,
        getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
) {

    private var navigation: TokonowCategoryDetail.Navigation? = null

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

        requestParams.putString(CATEGORY_ID, categoryId.toString())
        requestParams.putString(WAREHOUSE_ID, HARDCODED_WAREHOUSE_ID_PLEASE_DELETE)

        return requestParams
    }

    override fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        super.appendMandatoryParams(tokonowQueryParam)

        tokonowQueryParam[SearchApiConst.NAVSOURCE] = TOKONOW_DIRECTORY
        tokonowQueryParam[SearchApiConst.SOURCE] = TOKONOW_DIRECTORY
        tokonowQueryParam[SearchApiConst.SRP_PAGE_ID] = categoryId
    }

    private fun onGetCategoryFirstPageSuccess(categoryModel: CategoryModel) {
        navigation = categoryModel.categoryDetail.data.navigation

        val headerDataView = HeaderDataView(
                title = categoryModel.categoryDetail.data.name,
                hasSeeAllCategoryButton = true,
                aceSearchProductHeader = categoryModel.searchProduct.header,
                categoryFilterDataValue = categoryModel.categoryFilter,
                quickFilterDataValue = categoryModel.quickFilter,
        )

        val contentDataView = ContentDataView(
                productList = categoryModel.searchProduct.data.productList,
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
        val contentDataView = ContentDataView(productList = categoryModel.searchProduct.data.productList)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetCategoryLoadMorePageError(throwable: Throwable) {

    }
}
