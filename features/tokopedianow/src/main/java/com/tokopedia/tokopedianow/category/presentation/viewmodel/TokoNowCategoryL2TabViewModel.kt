package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterNotLoadedLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToCategoryTabLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToQuickFilter
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryParamMapper
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowCategoryL2TabViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    private val categoryParamMapper: CategoryParamMapper,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseTokoNowViewModel(
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()

    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    
    private val visitableList = mutableListOf<Visitable<*>>()

    fun onViewCreated(components: List<Component>) {
        launchCatchError(block = {
            visitableList.clear()

            visitableList.mapToCategoryTabLayout(components)
            
            visitableList.filterNotLoadedLayout().forEach {
                when(it) {
                    is CategoryQuickFilterUiModel -> getQuickFilterAsync(it).await()
                }
            }
        }) {
            
        }
    }

    private fun getQuickFilterAsync(item: CategoryQuickFilterUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val params = createQuickFilterRequestParams()
            val response = getSortFilterUseCase.execute(params)
            visitableList.mapToQuickFilter(item, response)
            updateVisitableListLiveData()
        }) {

        }
    }

    private fun createQuickFilterRequestParams(): Map<String?, Any?> {
        return categoryParamMapper.createRequestParams().also {
            it[SearchApiConst.NAVSOURCE] = QUICK_FILTER_TOKONOW_DIRECTORY
            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW_DIRECTORY
        }
    }

    private fun updateVisitableListLiveData() {
        _visitableListLiveData.postValue(visitableList)
    }
}
