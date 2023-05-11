package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L2
import com.tokopedia.tokopedianow.category.di.module.CategoryUseCaseModule.Companion.MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.category.domain.usecase.GetMainCategoryFirstPageUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryMainViewModel @Inject constructor(
    @Named(NOW_CATEGORY_L1)
    val categoryIdL1: String,
    @Named(NOW_CATEGORY_L2)
    val categoryIdL2: String,
    @Named(MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME)
    val getFirstPageUseCase: GetMainCategoryFirstPageUseCase,
    val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    val userSession: UserSessionInterface,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    addressData: TokoNowLocalAddress,
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

    private val _categoryFirstPage = MutableLiveData<List<Visitable<*>>>()

    val categoryFirstPage: LiveData<List<Visitable<*>>> = _categoryFirstPage

    fun getFirstPage(navToolbarHeight: Int) {
        launchCatchError(block = {
            getFirstPageUseCase.setParams(
                categoryId = categoryIdL1,
                warehouseId = warehouseId
            )
            val categoryModel = getFirstPageUseCase.executeOnBackground()

            layout.addHeaderSpace(navToolbarHeight, categoryModel)
            layout.addChooseAddress(categoryModel)
            layout.addCategoryTitle(categoryModel)
            layout.addCategoryMenu(categoryModel, categoryIdL1)

            _categoryFirstPage.postValue(layout)
        }, onError = {

        })
    }
}
