package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L2
import com.tokopedia.tokopedianow.category.di.module.CategoryUseCaseModule.Companion.MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME
import com.tokopedia.tokopedianow.category.di.module.CategoryUseCaseModule.Companion.MAIN_CATEGORY_HEADER_USE_CASE_NAME
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryHeaderUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryFirstPageUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryMainViewModel @Inject constructor(
    @Named(NOW_CATEGORY_L1)
    val categoryIdL1: String,
    @Named(NOW_CATEGORY_L2)
    val categoryIdL2: String,
    @Named(MAIN_CATEGORY_HEADER_USE_CASE_NAME)
    val getCategoryHeaderUseCase: GetCategoryHeaderUseCase,
    @Named(MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME)
    val getFirstPageUseCase: GetCategoryFirstPageUseCase,
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
    private val layout: MutableList<Visitable<*>> = mutableListOf()

    private val _categoryHeader = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _categoryPage = MutableLiveData<Result<List<Visitable<*>>>>()

    val categoryHeader: LiveData<Result<List<Visitable<*>>>> = _categoryHeader
    val categoryPage: LiveData<Result<List<Visitable<*>>>> = _categoryPage

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
                    headerResponse = headerResponse,
                    categoryIdL1 = categoryIdL1
                )

                _categoryHeader.postValue(Success(layout))
            },
            onError = {

            }
        )
    }

    fun getCategoryFirstPage() {
        launchCatchError(
            block = {
                getFirstPageUseCase.setParams(
                    chooseAddressData = addressData.getAddressData(),
                    categoryIdL1 = categoryIdL1,
                    uniqueId = getUniqueId()
                )

                val categoryPage = getFirstPageUseCase.executeOnBackground()
                layout.addCategoryShowcase(categoryPage)

                _categoryPage.postValue(Success(layout))
            },
            onError = {

            }
        )
    }
}
