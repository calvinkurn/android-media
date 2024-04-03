package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeItem
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job

open class BaseCategoryViewModel(
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
) : BaseTokoNowViewModel(
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

    protected val visitableList = mutableListOf<Visitable<*>>()

    private val _openScreenTracker: MutableLiveData<CategoryOpenScreenTrackerModel> = MutableLiveData()
    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val _updateToolbarNotification: MutableLiveData<Boolean> = MutableLiveData()
    private val _refreshState = MutableLiveData<Unit>()
    private val _outOfCoverageState = MutableLiveData<Unit>()
    private val _isPageLoading = MutableLiveData<Boolean>()
    private val _onPageError = MutableLiveData<Throwable>()

    val openScreenTracker: LiveData<CategoryOpenScreenTrackerModel> = _openScreenTracker
    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    val updateToolbarNotification = _updateToolbarNotification
    val refreshState: LiveData<Unit> = _refreshState
    val outOfCoverageState: LiveData<Unit> = _outOfCoverageState
    val isPageLoading: LiveData<Boolean> = _isPageLoading
    val onPageError: LiveData<Throwable> = _onPageError

    private var loadMoreJob: Job? = null

    var categoryIdL1 = String.EMPTY
    var categoryIdL2 = String.EMPTY
    var currentCategoryId = String.EMPTY
    var deepLink: String = String.EMPTY
    var queryParamMap: HashMap<String, String> = hashMapOf()

    protected open val tickerPage: String = ""

    protected open suspend fun loadFirstPage(
        tickerData: GetTickerData
    ) {
    }

    protected open suspend fun loadNextPage() {
    }

    protected fun sendOpenScreenTracker(id: String, name: String, url: String) {
        _openScreenTracker.postValue(CategoryOpenScreenTrackerModel(id, name, url))
    }

    protected fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    protected fun updateVisitableListLiveData() {
        val visitableList = visitableList.toMutableList()
        _visitableListLiveData.postValue(visitableList)
    }

    protected fun removeVisitableItem(visitableId: String) {
        visitableList.removeItem(visitableId)
    }

    protected fun hidePageLoading() {
        _isPageLoading.postValue(false)
    }

    private fun showPageLoading() {
        _isPageLoading.postValue(true)
    }

    fun onViewCreated() {
        showPageLoading()
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
        val isJobCompleted = loadMoreJob?.isCompleted == true
        if (isAtTheBottomOfThePage && (loadMoreJob == null || isJobCompleted)) {
            loadMoreJob = launchCatchError(block = {
                loadNextPage()
            }) {
            }
        }
    }

    fun refreshLayout() {
        getMiniCart()
        updateAddressData()
        loadMoreJob = null
        _refreshState.postValue(Unit)
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
                clearVisitableList()
                showOutOfCoverageState()
            }
        }) {
            _onPageError.postValue(it)
        }
    }

    private fun clearVisitableList() {
        visitableList.clear()
        updateVisitableListLiveData()
    }

    private fun showOutOfCoverageState() {
        _outOfCoverageState.postValue(Unit)
    }

    private fun getShopIdBeforeLoadData() {
        launchCatchError(block = {
            val response = getShopAndWarehouseUseCase(SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
            onGetShopAndWarehouseSuccess(response)
        }) {
            // no op
        }
    }

    private fun onGetShopAndWarehouseSuccess(state: GetStateChosenAddressResponse) {
        addressData.updateAddressData(state)
        processLoadDataWithShopId()
        getMiniCart()
    }

    private suspend fun getTickerData(): GetTickerData {
        val tickerData = getTickerDataAsync(
            warehouseId = getWarehouseId(),
            page = tickerPage
        ).await()

        return if (tickerData != null) {
            hasBlockedAddToCart = tickerData.blockAddToCart
            tickerData
        } else {
            GetTickerData()
        }
    }

    private fun Long.isValidId() = this != INVALID_ID
}
