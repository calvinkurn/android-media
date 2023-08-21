package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.mapToCategoryUiModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.CategoryGetDetailModular
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryLayoutUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowCategoryL2ViewModel @Inject constructor(
    private val getCategoryLayout: GetCategoryLayoutUseCase,
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val addressData: TokoNowLocalAddress,
    getCategoryProductUseCase: GetCategoryProductUseCase,
    getProductAdsUseCase: GetProductAdsUseCase,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    aceSearchParamMapper: AceSearchParamMapper,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseCategoryViewModel(
    getCategoryProductUseCase = getCategoryProductUseCase,
    getProductAdsUseCase = getProductAdsUseCase,
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    aceSearchParamMapper = aceSearchParamMapper,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    private val _categoryTab = MutableLiveData<CategoryL2TabUiModel>()
    private val _loadMore = MutableLiveData<Unit>()

    val categoryTab: LiveData<CategoryL2TabUiModel> = _categoryTab
    val loadMore: LiveData<Unit> = _loadMore

    override suspend fun loadFirstPage(tickerList: List<TickerData>) {
        val addressData = addressData.getAddressData()
        val getCategoryLayoutResponse = getCategoryLayoutAsync().await()
        val getCategoryDetailResponse = getCategoryDetailAsync().await()

        visitableList.clear()
        visitableList.addChooseAddress(addressData)
        visitableList.mapToCategoryUiModel(
            getCategoryLayoutResponse,
            getCategoryDetailResponse
        )

        val categoryTab = CategoryL2Mapper.mapToCategoryTab(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            getCategoryLayoutResponse = getCategoryLayoutResponse,
            categoryDetailResponse = getCategoryDetailResponse
        )

        hidePageLoading()
        updateCategoryTab(categoryTab)
        updateVisitableListLiveData()
        sendOpenScreenTracker(id = "", name = "", url = "")
    }

    override suspend fun loadNextPage() {
        _loadMore.postValue(Unit)
    }

    private suspend fun getCategoryLayoutAsync(): Deferred<CategoryGetDetailModular?> {
        return asyncCatchError(block = {
            getCategoryLayout.execute(categoryIdL1)
        }) {
            null
        }
    }

    private fun getCategoryDetailAsync(): Deferred<CategoryDetailResponse?> {
        return asyncCatchError(block = {
            val warehouses = addressData.getWarehousesData()
            getCategoryDetailUseCase.execute(warehouses, categoryIdL1)
        }) {
            null
        }
    }

    private fun updateCategoryTab(categoryTab: CategoryL2TabUiModel) {
        _categoryTab.postValue(categoryTab)
    }
}
