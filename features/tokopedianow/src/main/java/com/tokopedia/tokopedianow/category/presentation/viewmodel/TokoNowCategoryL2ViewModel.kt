package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.mapToCategoryUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryLayoutUseCase
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2ShimmerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowCategoryL2ViewModel @Inject constructor(
    private val getCategoryLayoutUseCase: GetCategoryLayoutUseCase,
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val addressData: TokoNowLocalAddress,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseCategoryViewModel(
    getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
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

    override val tickerPage: String
        get() = GetTargetedTickerUseCase.CATEGORY_L2

    val categoryTabLiveData: LiveData<CategoryL2TabUiModel>
        get() = _categoryTabLiveData
    val onTabSelectedLiveData: LiveData<CategoryL2TabUiModel>
        get() = _onTabSelectedLiveData

    private val _categoryTabLiveData = MutableLiveData<CategoryL2TabUiModel>()
    private val _onTabSelectedLiveData = MutableLiveData<CategoryL2TabUiModel>()

    private var categoryTab = CategoryL2TabUiModel()

    override suspend fun loadFirstPage(tickerData: GetTickerData) {
        val tickerList = tickerData.tickerList
        val warehouses = addressData.getWarehousesData()
        val localCacheModel = addressData.getAddressData()
        val getCategoryLayoutResponse = getCategoryLayoutUseCase.execute(categoryIdL2)
        val getCategoryDetailResponse = getCategoryDetailUseCase.execute(warehouses, categoryIdL1)

        visitableList.clear()
        visitableList.addChooseAddress(localCacheModel)
        visitableList.mapToCategoryUiModel(
            getCategoryLayoutResponse,
            getCategoryDetailResponse,
            tickerList
        )

        categoryTab = CategoryL2Mapper.mapToCategoryTab(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = tickerData,
            getCategoryLayoutResponse = getCategoryLayoutResponse,
            categoryDetailResponse = getCategoryDetailResponse,
            queryParamMap = queryParamMap,
            deepLink = deepLink
        )

        hidePageLoading()
        updateCategoryTab(categoryTab)
        updateVisitableListLiveData()
        trackOpenScreen(getCategoryDetailResponse)
    }

    fun showPageLoading() {
        val localCacheModel = addressData.getAddressData()

        visitableList.clear()
        visitableList.addChooseAddress(localCacheModel)
        visitableList.add(CategoryL2ShimmerUiModel)

        updateVisitableListLiveData()
    }

    fun removeChooseAddressWidget() {
        visitableList.removeItem<TokoNowChooseAddressWidgetUiModel>()
        updateVisitableListLiveData()
    }

    fun getWarehouseIds(): String {
        val localCacheModel = addressData.getAddressData()
        return AddressMapper.mapToWarehouseIds(localCacheModel)
    }

    fun onTabSelected(position: Int) {
        val categoryL2TabData = categoryTab.tabList[position]
        categoryIdL2 = categoryL2TabData.categoryIdL2
        categoryTab.selectedTabPosition = position
        _onTabSelectedLiveData.postValue(categoryTab)
    }

    private fun trackOpenScreen(getCategoryDetailResponse: CategoryDetailResponse) {
        sendOpenScreenTracker(
            id = getCategoryDetailResponse.categoryDetail.data.id,
            name = getCategoryDetailResponse.categoryDetail.data.name,
            url = getCategoryDetailResponse.categoryDetail.data.url
        )
    }

    private fun updateCategoryTab(categoryTab: CategoryL2TabUiModel) {
        _categoryTabLiveData.postValue(categoryTab)
    }
}
