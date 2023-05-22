package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.di.module.CategoryUseCaseModule.Companion.MAIN_CATEGORY_HEADER_USE_CASE_NAME
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryMenuMapper.mapToCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addRecipeProgressBar
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.mapCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeRecipeProgressBar
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateProductQuantity
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryHeaderUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryMainViewModel @Inject constructor(
    @Named(NOW_CATEGORY_L1)
    val categoryIdL1: String,
    @Named(MAIN_CATEGORY_HEADER_USE_CASE_NAME)
    val getCategoryHeaderUseCase: GetCategoryHeaderUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    val userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
): TokoNowCategoryBaseViewModel(
    userSession = userSession,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    addressData = addressData,
    dispatchers = dispatchers
) {
    private companion object {
        const val BATCH_SHOWCASE_TOTAL = 3
    }

    private val layout: MutableList<Visitable<*>> = mutableListOf()
    private val categoryL2Models: MutableList<CategoryL2Model> = mutableListOf()
    private var categoryMenu: TokoNowCategoryMenuUiModel? = null

    private val _categoryHeader = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _categoryPage = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _isOnScrollNotNeeded = MutableLiveData<Boolean>()

    val categoryHeader: LiveData<Result<List<Visitable<*>>>> = _categoryHeader
    val categoryPage: LiveData<Result<List<Visitable<*>>>> = _categoryPage
    val isOnScrollNotNeeded: LiveData<Boolean> = _isOnScrollNotNeeded

    override fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: CategoryLayoutType
    ) {
        miniCartData?.apply {
            layout.updateProductQuantity(
                productId = productId,
                quantity = quantity,
                layoutType = layoutType
            )
        }
    }

    override fun onSuccessGetMiniCartData(
        miniCartData: MiniCartSimplifiedData
    ) {
        super.onSuccessGetMiniCartData(miniCartData)
        layout.updateProductQuantity(
            miniCartData = miniCartData,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE
        )
    }

    private suspend fun getCategoryShowcaseAsync(
        categoryL2Model: CategoryL2Model,
        hasAdded: Boolean
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val categoryPage = getCategoryProductUseCase.execute(
                chooseAddressData = addressData.getAddressData(),
                categoryIdL2 = categoryL2Model.id,
                uniqueId = getUniqueId()
            )

            categoryL2Models.remove(categoryL2Model)

            if (hasAdded) {
                layout.mapCategoryShowcase(
                    model = categoryPage,
                    categoryIdL2 = categoryL2Model.id,
                    title = categoryL2Model.title,
                    seeAllAppLink = categoryL2Model.appLink,
                    miniCartData = miniCartData
                )
            } else {
                layout.addCategoryShowcase(
                    model = categoryPage,
                    categoryIdL2 = categoryL2Model.id,
                    title = categoryL2Model.title,
                    state = TokoNowLayoutState.SHOW,
                    seeAllAppLink = categoryL2Model.appLink,
                    miniCartData = miniCartData
                )
            }

        }) {
            categoryL2Models.remove(categoryL2Model)

            layout.removeItem(
                id = categoryL2Model.id
            )
        }
    }

    private fun addCategoryShowcases(
        categoryNavigationUiModel: CategoryNavigationUiModel
    ) {
        categoryL2Models.clear()

        categoryL2Models.addAll(
            categoryNavigationUiModel.categoryListUiModel.map {
                CategoryL2Model(
                    id = it.id,
                    title = it.title,
                    appLink = it.appLink
                )
            }
        )

        categoryL2Models.take(BATCH_SHOWCASE_TOTAL).forEach { categoryL2Model ->
            layout.addCategoryShowcase(
                categoryIdL2 = categoryL2Model.id,
                state = TokoNowLayoutState.LOADING,
                miniCartData = miniCartData
            )
        }
    }

    private fun getMoreShowcases() {
        launch {
            layout.addRecipeProgressBar()

            categoryL2Models.take(BATCH_SHOWCASE_TOTAL).map { categoryL2Model ->
                getCategoryShowcaseAsync(categoryL2Model, false)
            }.awaitAll()

            layout.removeRecipeProgressBar()

            _categoryPage.postValue(Success(layout))
        }
    }

    fun getCategoryHeader(
        navToolbarHeight: Int
    ) {
        launchCatchError(
            block = {
                getCategoryHeaderUseCase.setParams(
                    categoryId = categoryIdL1,
                    warehouseId = getWarehouseId()
                )

                val headerResponse = getCategoryHeaderUseCase.executeOnBackground()
                val categoryNavigationUiModel = headerResponse.categoryNavigation.mapToCategoryNavigation(categoryIdL1)
                categoryMenu = headerResponse.categoryNavigation.mapToCategoryMenu()

                layout.addHeaderSpace(
                    space = navToolbarHeight,
                    headerResponse = headerResponse
                )
                layout.addChooseAddress(
                    headerResponse = headerResponse
                )
                layout.addCategoryTitle(
                    headerResponse = headerResponse
                )
                layout.addCategoryNavigation(
                    categoryNavigationUiModel = categoryNavigationUiModel
                )
                layout.addProductRecommendation(
                    categoryId = listOf(categoryIdL1)
                )

                addCategoryShowcases(
                    categoryNavigationUiModel = categoryNavigationUiModel
                )

                _categoryHeader.postValue(Success(layout))
            },
            onError = {
                /* error state on the page */
            }
        )
    }

    fun getFirstPage() {
        launch {
            categoryL2Models.take(BATCH_SHOWCASE_TOTAL).map { categoryL2Model ->
                getCategoryShowcaseAsync(categoryL2Model, true)
            }.awaitAll()

            _categoryPage.postValue(Success(layout))
        }
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        if (categoryL2Models.isEmpty()) {
            _isOnScrollNotNeeded.value = true

            categoryMenu?.let {
                layout.addCategoryMenu(it)
                _categoryPage.value = Success(layout)
            }
        } else if (isAtTheBottomOfThePage) {
            getMoreShowcases()
        }
    }

    fun refreshLayout(
        navToolbarHeight: Int
    ) {
        layout.clear()
        getCategoryHeader(navToolbarHeight)
        _isOnScrollNotNeeded.value = false
    }
}
