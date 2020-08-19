package com.tokopedia.favorite.view

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCase
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUsecase
import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.view.viewmodel.DataFavoriteMapper
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

class FavoriteViewModel
@Inject constructor(
        private val dispatcherProvider: DispatcherProvider,
        private val getInitialDataPageUsecase: GetInitialDataPageUsecase,
        private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
        private val getAllDataFavoriteUseCase: GetAllDataFavoriteUseCase,
        private val getFavoriteShopUseCase: GetFavoriteShopUsecase,
        private val favoriteMapper: DataFavoriteMapper
): BaseViewModel(dispatcherProvider.ui()) {

    private val pagingHandler = PagingHandler()

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
        MutableLiveData<FavoriteShopViewModel>()
    }
    val addedFavoriteShop: LiveData<FavoriteShopViewModel>
        get() = _addedFavoriteShop

    private val _favoriteShopImpression by lazy {
        MutableLiveData<String>()
    }
    val favoriteShopImpression: LiveData<String>
        get() = _favoriteShopImpression

    //==============================

    fun loadInitialData() {
        getInitialDataPageUsecase.execute(RequestParams.EMPTY, InitialDataSubscriber())
    }

    fun addFavoriteShop(view: View, shopItem: TopAdsShopItem) {
        toggleFavouriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopItem.shopId),
                AddFavoriteShopSubscriber(view, shopItem)
        )
    }

    fun loadMoreFavoriteShop() {
        if (pagingHandler.CheckNextPage()) {
            pagingHandler.nextPage()
            _isLoadingFavoriteShop.value = true
            val params = GetFavoriteShopUsecase.defaultParams
            val currentPage = pagingHandler.page.toString()
            params.putString(GetFavoriteShopUsecase.KEY_PAGE, currentPage)
            getFavoriteShopUseCase.execute(params, LoadMoreSubscriber())
        }
    }

    fun refreshAllDataFavoritePage() {
        getAllDataFavoriteUseCase.execute(RequestParams.EMPTY, RefreshFavoriteSubscriber())
    }

    private fun addTopAdsShop(
            dataFavorite: DataFavorite?,
            dataFavoriteItemList: MutableList<Visitable<*>>
    ) {
        if (dataFavorite != null) {
            validateNetworkTopAdsShop(dataFavorite.topAdsShop)

            val topAdsShop = dataFavorite.topAdsShop
            val topAdsShopItemList = topAdsShop?.topAdsShopItemList
            if (
                    topAdsShop != null &&
                    topAdsShopItemList != null &&
                    topAdsShopItemList.isNotEmpty()
            ) {
                dataFavoriteItemList.add(favoriteMapper.prepareDataTopAdsShop(topAdsShop)
                )
            }
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
                if (favoriteShop.pagingModel != null) {
                    setNextPaging(favoriteShop.pagingModel)
                }
                if (favoriteShopData.isNotEmpty()) {
                    for (favoriteShopItem in favoriteShopData) {
                        favoriteShopItem.isFav = (true)
                        dataFavoriteItemList.add(
                                favoriteMapper.prepareDataFavoriteShop(favoriteShopItem))
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

    /**
     * Inner classes
     */

    inner class InitialDataSubscriber: Subscriber<DataFavorite>() {
        override fun onStart() {
            super.onStart()
            _refresh.value = true
        }

        override fun onNext(dataFavorite: DataFavorite) {
            _refresh.value = false
            _initialData.value = getDataFavoriteViewModel(dataFavorite)
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable?) {
            Timber.e(e, "onError: ")
            _refresh.value = false
            _isTopAdsShopNetworkFailed.value = true
            _isErrorLoad.value = true
        }

        private fun getDataFavoriteViewModel(dataFavorite: DataFavorite): List<Visitable<*>> {
            val elements = ArrayList<Visitable<*>>(emptyList())
            addTopAdsShop(dataFavorite, elements)
            addFavoriteShop(dataFavorite, elements)
            return elements
        }
    }

    inner class AddFavoriteShopSubscriber(private val view: View, private val shopItem: TopAdsShopItem): Subscriber<Boolean>() {
        override fun onStart() {
            view.isEnabled = false
        }

        override fun onNext(isValid: Boolean?) {
            view.clearAnimation()
            if (isValid != null && isValid) {
                val favoriteShopViewModel = FavoriteShopViewModel()
                favoriteShopViewModel.shopId = shopItem.shopId
                favoriteShopViewModel.shopName = shopItem.shopName
                favoriteShopViewModel.shopAvatarImageUrl = shopItem.shopImageUrl
                favoriteShopViewModel.shopLocation = shopItem.shopLocation
                favoriteShopViewModel.isFavoriteShop = shopItem.isFav
                _addedFavoriteShop.value = favoriteShopViewModel
                _favoriteShopImpression.value = shopItem.shopClickUrl
            }
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable?) {
            Timber.e("onError: %s", e.toString())
            _isErrorAddFavoriteShop.value = true
        }
    }

    inner class LoadMoreSubscriber: Subscriber<FavoriteShop>() {
        override fun onNext(favoriteShop: FavoriteShop) {
            if (favoriteShop.isDataValid) {
                setNextPaging(favoriteShop.pagingModel)
                val elements  =  favoriteMapper.prepareListFavoriteShop(favoriteShop)
                _moreDataFavoriteShop.value = elements
            } else {
                setNextPaging(favoriteShop.pagingModel)
            }
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable?) {
            e?.printStackTrace()
            returnPagingHandlerToPreviousPage()
            _isErrorLoadMore.value = true
        }
    }

    inner class RefreshFavoriteSubscriber: Subscriber<DataFavorite>() {
        override fun onStart() {
            super.onStart()
            _refresh.value = true
        }

        override fun onNext(dataFavorite: DataFavorite) {
            val elements = ArrayList<Visitable<*>>(emptyList())
            addTopAdsShop(dataFavorite, elements)
            addFavoriteShop(dataFavorite, elements)
            _refreshData.value = elements
            _refresh.value = false
            pagingHandler.resetPage()
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable?) {
            Timber.e("onError: %s", e.toString())
        }

    }
}
