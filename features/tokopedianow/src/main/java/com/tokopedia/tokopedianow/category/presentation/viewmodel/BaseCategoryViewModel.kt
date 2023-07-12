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
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam.Companion.SRC_DIRECTORY_TOKONOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

abstract class BaseCategoryViewModel(
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    private val addressData: TokoNowLocalAddress,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
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

    companion object {
        private const val INVALID_ID = 0L
    }


    private val _openScreenTracker: MutableLiveData<CategoryOpenScreenTrackerModel> = MutableLiveData()
    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val _updateToolbarNotification: MutableLiveData<Boolean> = MutableLiveData()
    private val _refreshState = MutableLiveData<Unit>()
    private val _outOfCoverageState = MutableLiveData<Unit>()

    val openScreenTracker: LiveData<CategoryOpenScreenTrackerModel> = _openScreenTracker
    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    val updateToolbarNotification = _updateToolbarNotification
    val refreshState: LiveData<Unit> = _refreshState
    val outOfCoverageState: LiveData<Unit> = _outOfCoverageState

    protected val visitableList = mutableListOf<Visitable<*>>()

    private var moreShowcaseJob: Job? = null

    var navToolbarHeight = 0

    var categoryIdL1 = String.EMPTY
    var categoryIdL2 = String.EMPTY
    var currentCategoryId = String.EMPTY
    var queryParamMap: HashMap<String, String>? = hashMapOf()

    protected abstract fun loadFirstPage(tickerList: List<TickerData>)

    protected abstract suspend fun loadNextPage()

    protected abstract fun onSuccessGetCategoryProduct(
        response: AceSearchProductModel,
        categoryL2Model: CategoryL2Model
    )

    protected abstract fun onErrorGetCategoryProduct(
        error: Throwable,
        categoryL2Model: CategoryL2Model
    )

    protected suspend fun getCategoryProductAsync(
        categoryL2Model: CategoryL2Model,
    ): Deferred<Unit?> = asyncCatchError(block = {
        val response = getCategoryProductUseCase.execute(
            chooseAddressData = getAddressData(),
            categoryIdL2 = categoryL2Model.id,
            uniqueId = getUniqueId()
        )
        onSuccessGetCategoryProduct(response, categoryL2Model)
    }) {
        onErrorGetCategoryProduct(it, categoryL2Model)
    }

    protected fun getProductAds(categoryId: String) {
        launchCatchError(block = {
            val params = GetProductAdsParam(
                categoryId = categoryId,
                warehouseIds = addressData.getWarehouseIds(),
                src = SRC_DIRECTORY_TOKONOW,
                userId = getUserId()
            )
            val response = getProductAdsUseCase.execute(params)

            if (response.productList.isNotEmpty()) {
                visitableList.mapProductAdsCarousel(
                    response = response,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            } else {
                removeVisitableItem(PRODUCT_ADS_CAROUSEL)
            }

            updateVisitableListLiveData()
        }) {
            removeVisitableItem(PRODUCT_ADS_CAROUSEL)
            updateVisitableListLiveData()
        }
    }

    protected fun sendOpenScreenTracker(id: String, name: String, url: String) {
        _openScreenTracker.postValue(CategoryOpenScreenTrackerModel(id, name, url))
    }

    protected fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    protected fun updateVisitableListLiveData() {
        _visitableListLiveData.postValue(visitableList)
    }

    protected fun removeVisitableItem(visitableId: String) {
        visitableList.removeItem(visitableId)
    }

    fun onViewCreated() {
        initAffiliateCookie()
        processLoadDataPage()
    }

    fun onViewResume() {
        if (addressData.isChoosenAddressUpdated()) {
            refreshLayout()
        } else {
            getMiniCart()
        }
        updateToolbarNotification()
    }

    fun onScroll(isAtTheBottomOfThePage: Boolean) {
        val isJobCompleted = moreShowcaseJob?.isCompleted == true
        if (isAtTheBottomOfThePage && (moreShowcaseJob == null || isJobCompleted)) {
            moreShowcaseJob = launchCatchError(block = {
                loadNextPage()
            }) {

            }
        }
    }

    fun refreshLayout() {
        getMiniCart()
        updateAddressData()
        moreShowcaseJob = null
        _refreshState.value = Unit
    }

    private fun processLoadDataPage() {
        val shopId = addressData.getShopId()

        if (shopId.isValidId()) {
            processLoadDataWithShopId()
        } else {
            getShopIdBeforeLoadData()
        }
    }

    private fun processLoadDataWithShopId() {
        launchCatchError(block = {
            val warehouseId = addressData.getWarehouseId()

            if (warehouseId.isValidId()) {
                val tickerList = getTickerData()
                loadFirstPage(tickerList)
            } else {
                _outOfCoverageState.value = Unit
            }
        }) {

        }
    }

    private fun getShopIdBeforeLoadData() {
        getShopAndWarehouseUseCase.getStateChosenAddress(
            ::onGetShopAndWarehouseSuccess,
            onFail = {},
            SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH
        )
    }

    private fun onGetShopAndWarehouseSuccess(state: GetStateChosenAddressResponse) {
        addressData.updateAddressData(state)
        processLoadDataWithShopId()
        getMiniCart()
    }

    private suspend fun getTickerData(): List<TickerData> {
        val tickerData = getTickerDataAsync(
            warehouseId = getWarehouseId(),
            page = GetTargetedTickerUseCase.CATEGORY_PAGE
        ).await()

        return if(tickerData != null) {
            hasBlockedAddToCart = tickerData.first
            tickerData.second
        } else {
            emptyList()
        }
    }

    private fun getUniqueId() = if (isLoggedIn()) {
        AuthHelper.getMD5Hash(getUserId())
    } else {
        AuthHelper.getMD5Hash(getDeviceId())
    }

    private fun Long.isValidId() = this != INVALID_ID
}
