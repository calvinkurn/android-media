package com.tokopedia.favorite.view

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCaseWithCoroutine
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUseCaseWithCoroutine
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUseCaseWithCoroutine
import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.view.viewmodel.DataFavoriteMapper
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FavoriteViewModel
@Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val getInitialDataPageUseCase: GetInitialDataPageUseCaseWithCoroutine,
        private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
        private val getAllDataFavoriteUseCase: GetAllDataFavoriteUseCaseWithCoroutine,
        private val getFavoriteShopUseCaseWithCoroutine: GetFavoriteShopUseCaseWithCoroutine,
        private val pagingHandler: PagingHandler
): BaseViewModel(dispatcherProvider.main) {

    /**
     * Refresh and loading
     */

    private val _refresh by lazy {
        MutableLiveData<Boolean>()
    }
    val refresh: LiveData<Boolean>
        get() = _refresh

    private val _isLoadingFavoriteShop by lazy {
        MutableLiveData<Boolean>()
    }
    val isLoadingFavoriteShop: LiveData<Boolean>
        get() = _isLoadingFavoriteShop

    //==============================

    /**
     * Error attributes
     */

    private val _isErrorLoad by lazy {
        MutableLiveData<Boolean>()
    }
    val isErrorLoad: LiveData<Boolean>
        get() = _isErrorLoad

    private val _isErrorLoadMore by lazy {
        MutableLiveData<Boolean>()
    }
    val isErrorLoadMore: LiveData<Boolean>
        get() = _isErrorLoadMore

    private val _isTopAdsShopNetworkFailed by lazy {
        MutableLiveData<Boolean>()
    }

    private val _isFavoriteShopNetworkFailed by lazy {
        MutableLiveData<Boolean>()
    }

    private val _isNetworkFailed by lazy {
        val result = MediatorLiveData<Boolean>()
        result.addSource(_isFavoriteShopNetworkFailed) {
            result.value = combineLatestNetworkFailedData()
        }
        result.addSource(_isTopAdsShopNetworkFailed) {
            result.value = combineLatestNetworkFailedData()
        }
        result
    }
    val isNetworkFailed: LiveData<Boolean>
        get() = _isNetworkFailed

    private val _isErrorAddFavoriteShop by lazy {
        MutableLiveData<Boolean>()
    }

    val isErrorAddFavoriteShop: LiveData<Boolean>
        get() = _isErrorAddFavoriteShop

    //==============================

    /**
     * Data attributes
     */

    private val _initialData by lazy {
        MutableLiveData<List<Visitable<*>>>()
    }
    val initialData: LiveData<List<Visitable<*>>>
        get() = _initialData

    private val _moreDataFavoriteShop by lazy {
        MutableLiveData<List<Visitable<*>>>()
    }
    val moreDataFavoriteShop: LiveData<List<Visitable<*>>>
        get() = _moreDataFavoriteShop

    private val _refreshData by lazy {
        MutableLiveData<List<Visitable<*>>>()
    }
    val refreshData: LiveData<List<Visitable<*>>>
        get() = _refreshData

    private val _addedFavoriteShop by lazy {
        MutableLiveData<FavoriteShopUiModel>()
    }
    val addedFavoriteShop: LiveData<FavoriteShopUiModel>
        get() = _addedFavoriteShop

    private val _favoriteShopImpression by lazy {
        MutableLiveData<String>()
    }
    val favoriteShopImpression: LiveData<String>
        get() = _favoriteShopImpression

    //==============================

    fun loadInitialData() {
        launchCatchError(block = {
            _refresh.value = true
            val dataFavorite = withContext(dispatcherProvider.io) {
                getInitialDataPageUseCase.executeOnBackground()
            }
            _refresh.value = false
            _initialData.value = getDataFavoriteUiModel(dataFavorite)
        }, onError = {
            Timber.e(it, "onError: ")
            _refresh.value = false
            _isTopAdsShopNetworkFailed.value = true
            _isErrorLoad.value = true
        })
    }

    fun addFavoriteShop(view: View, shopItem: TopAdsShopItem) {
        val params = ToggleFavouriteShopUseCase.createRequestParam(shopItem.shopId);
        launchCatchError(block = {
            val isValid = withContext(dispatcherProvider.io) {
                toggleFavouriteShopUseCase.createObservable(params).toBlocking().single()
            }
            view.clearAnimation()
            if (isValid != null && isValid) {
                val favoriteShopUiModel = FavoriteShopUiModel()
                favoriteShopUiModel.shopId = shopItem.shopId
                favoriteShopUiModel.shopName = shopItem.shopName
                favoriteShopUiModel.shopAvatarImageUrl = shopItem.shopImageUrl
                favoriteShopUiModel.shopLocation = shopItem.shopLocation
                favoriteShopUiModel.isFavoriteShop = shopItem.isFav
                _addedFavoriteShop.value = favoriteShopUiModel
                _favoriteShopImpression.value = shopItem.shopClickUrl
            }
        }, onError = { e ->
            Timber.e("onError: %s", e.toString())
            _isErrorAddFavoriteShop.value = true
            view.clearAnimation()
        })
    }

    fun loadMoreFavoriteShop() {
        if (!pagingHandler.CheckNextPage()) return

        pagingHandler.nextPage()
        _isLoadingFavoriteShop.value = true

        val params = GetFavoriteShopUsecase.defaultParams
        val currentPage = pagingHandler.page.toString()
        params.putString(GetFavoriteShopUsecase.KEY_PAGE, currentPage)
        getFavoriteShopUseCaseWithCoroutine.requestParams = params

        launchCatchError(block =  {
            val favoriteShop = withContext(dispatcherProvider.io) {
                getFavoriteShopUseCaseWithCoroutine.executeOnBackground()
            }

            if (favoriteShop.isDataValid) {
                setNextPaging(favoriteShop.pagingModel)
                val elements = DataFavoriteMapper.prepareListFavoriteShop(favoriteShop)
                _moreDataFavoriteShop.value = elements
            } else {
                setNextPaging(favoriteShop.pagingModel)
            }
        }, onError = { e ->
            returnPagingHandlerToPreviousPage()
            _isLoadingFavoriteShop.value = false
            _isErrorLoadMore.value = true
        })
    }

    fun refreshAllDataFavoritePage() {
        _refresh.value = true
        launchCatchError(block = {
            val dataFavorite = withContext(dispatcherProvider.io) {
                getAllDataFavoriteUseCase.executeOnBackground()
            }
            val elements = ArrayList<Visitable<*>>(emptyList())
            addTopAdsShop(dataFavorite, elements)
            addFavoriteShop(dataFavorite, elements)

            _refreshData.value = elements
            _refresh.value = false

            pagingHandler.resetPage()
        }, onError = { e ->
            Timber.e("onError: %s", e.toString())
            _refresh.value = false
        })
    }

    private fun getDataFavoriteUiModel(dataFavorite: DataFavorite): List<Visitable<*>> {
        val visitables = ArrayList<Visitable<*>>()
        addTopAdsShop(dataFavorite, visitables)
        addFavoriteShop(dataFavorite, visitables)
        return visitables
    }

    private fun addTopAdsShop(
            dataFavorite: DataFavorite,
            dataFavoriteItemList: MutableList<Visitable<*>>
    ) {
        validateNetworkTopAdsShop(dataFavorite.topAdsShop)
        val topAdsShop = dataFavorite.topAdsShop
        val topAdsShopItemList = topAdsShop?.topAdsShopItemList
        if (
                topAdsShop != null &&
                topAdsShopItemList != null &&
                topAdsShopItemList.isNotEmpty()
        ) {
            dataFavoriteItemList.add(DataFavoriteMapper.prepareDataTopAdsShop(topAdsShop))
        }
    }

    private fun combineLatestNetworkFailedData(): Boolean {
        return (_isTopAdsShopNetworkFailed.value ?: false) ||
                (_isFavoriteShopNetworkFailed.value ?: false)
    }

    private fun addFavoriteShop(
            dataFavorite: DataFavorite?, dataFavoriteItemList: MutableList<Visitable<*>>
    ) {
        val favoriteShop: FavoriteShop? = dataFavorite?.favoriteShop
        if (favoriteShop != null) {
            validateFavoriteShopErrorNetwork(dataFavorite)
            val favoriteShopData = favoriteShop.data
            if (favoriteShopData != null) {
                setNextPaging(favoriteShop.pagingModel)
                if (favoriteShopData.isNotEmpty()) {
                    for (favoriteShopItem in favoriteShopData) {
                        favoriteShopItem.isFav = (true)
                        dataFavoriteItemList.add(DataFavoriteMapper.prepareDataFavoriteShop(favoriteShopItem))
                    }
                }
            }
        }
    }

    private fun validateNetworkTopAdsShop(topAdsShop: TopAdsShop?) {
        if (topAdsShop != null) {
            _isTopAdsShopNetworkFailed.value = topAdsShop.isNetworkError
        }
    }

    private fun validateFavoriteShopErrorNetwork(dataFavorite: DataFavorite) {
        _isFavoriteShopNetworkFailed.value = (dataFavorite.favoriteShop?.isNetworkError) ?: false
    }

    private fun setNextPaging(pagingModel: PagingHandlerModel?) {
        if (pagingModel != null) {
            pagingHandler.setHasNext(PagingHandler.CheckHasNext(pagingModel))
        } else {
            pagingHandler.setHasNext(false)
            _isLoadingFavoriteShop.value = false
        }
    }

    private fun returnPagingHandlerToPreviousPage() {
        val firstPage = 1
        val currentPage = pagingHandler.page
        if (currentPage <= firstPage) {
            pagingHandler.resetPage()
        } else {
            pagingHandler.page = currentPage - 1
        }
    }

    fun hasNextPage(): Boolean {
        return pagingHandler.CheckNextPage()
    }

}
