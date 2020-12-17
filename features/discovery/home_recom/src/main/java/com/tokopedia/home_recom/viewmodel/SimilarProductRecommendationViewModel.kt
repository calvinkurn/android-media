package com.tokopedia.home_recom.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_recom.model.entity.FilterSortChip
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.util.getOption
import com.tokopedia.home_recom.util.getSelectedOption
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips.Companion.FULL_FILTER
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips.Companion.QUICK_FILTER
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rx.Subscriber
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * Created by Lukas on 26/08/19
 */
@SuppressLint("SyntheticAccessor")
open class SimilarProductRecommendationViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
        private val getRecommendationFilterChips: GetRecommendationFilterChips,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()){

    private val _recommendationItem = MutableLiveData<Response<List<RecommendationItem>>>()
    val recommendationItem: LiveData<Response<List<RecommendationItem>>> get() = _recommendationItem
    private val _filterSortChip = MutableLiveData<Response<FilterSortChip>>()
    val filterSortChip: LiveData<Response<FilterSortChip>> get() = _filterSortChip

    fun getSimilarProductRecommendation(page: Int = 1, queryParam: String, productId: String, pageName: String){
        launch(dispatcher.getIODispatcher()){
            try{
                if(page == 1 && _recommendationItem.value != null) _recommendationItem.postValue(null)
                if (_recommendationItem.value == null) _recommendationItem.postValue(Response.loading())
                else _recommendationItem.postValue(Response.loadingMore(_recommendationItem.value?.data))
                val userId: Int = userSessionInterface.userId.toIntOrZero()
                var filterAndSort: FilterSortChip? = null
                if(page == 1){
                    getRecommendationFilterChips.setParams(userId = userId, productIDs = productId, queryParam = queryParam, type = QUICK_FILTER, pageName = pageName)
                    val quickFilter = getRecommendationFilterChips.executeOnBackground()

                    getRecommendationFilterChips.setParams(userId = userId, productIDs = productId, queryParam = queryParam, type = FULL_FILTER, pageName = pageName)
                    val fullFilter = getRecommendationFilterChips.executeOnBackground()
                    filterAndSort = FilterSortChip(fullFilter, quickFilter.filterChip)
                    _filterSortChip.postValue(Response.success(filterAndSort))
                }
                val params = singleRecommendationUseCase.getRecomParams(pageNumber = page, productIds = listOf(productId), queryParam = queryParam)

                val recommendationItems = singleRecommendationUseCase.createObservable(params).toBlocking().first()
                if(recommendationItems.isEmpty()){
                    _filterSortChip.postValue(Response.error(Exception()))
                    _recommendationItem.postValue(Response.error(Exception()))
                }else {
                    _recommendationItem.postValue(Response.success(recommendationItems.map {
                        it.copy(position = it.position + (page - 1) * COUNT_PRODUCT)
                    }))
                }

            } catch (e: Exception){
                if(page == 1) _filterSortChip.postValue(Response.error(e))

                if(e is IOException || e is TimeoutException){
                    _recommendationItem.postValue(Response.error(TimeoutException(), _recommendationItem.value?.data))
                } else {
                    _recommendationItem.postValue(Response.error(e, _recommendationItem.value?.data))
                }
            }
        }
    }

    fun getRecommendationFromQuickFilter(title: String, pageName: String, queryParam: String, productId: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {
            val filterAndSort = _filterSortChip.value?.data?.filterAndSort

            // update select / deselect to full filter
            filterAndSort?.filterChip?.getOption()?.find { opt -> opt.name == title }?.let {
                it.isActivated = !it.isActivated
            }

            val oldFilterData = _filterSortChip.value?.data

            val filterString = "&" + _filterSortChip.value?.data?.filterAndSort?.filterChip?.getSelectedOption()?.joinToString(separator = "&") { opt ->
                "${opt.key}=${opt.value}"
            }

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = queryParam + filterString, type = QUICK_FILTER, pageName = pageName)
            val quickFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = queryParam + filterString, type = FULL_FILTER, pageName = pageName)
            val fullFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            _filterSortChip.postValue(Response.loading())
            _recommendationItem.postValue(Response.loading())

            val recommendation = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = queryParam + filterString, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()
            if(recommendation.isNotEmpty()){
                val filterData = FilterSortChip(fullFilterAsync.await(), quickFilterAsync.await().filterChip)
                _filterSortChip.postValue(Response.success(filterData))
                _recommendationItem.postValue(Response.success(recommendation))
            } else {
                _filterSortChip.postValue(Response.success(oldFilterData))
                _recommendationItem.postValue(Response.empty())
            }
        }){
            _filterSortChip.postValue(Response.error(it))
            _recommendationItem.postValue(Response.error(Exception(it.message), _recommendationItem.value?.data))
        }
    }

    fun getRecommendationFromFullFilter(sort: Map<String, String>, filter: Map<String, String>, pageName: String, queryParam: String, productId: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {
            _filterSortChip.value?.data?.let { filterSort ->
                var resultQuery = queryParam

                filterSort.filterAndSort.sortChip.forEach {
                    val isActivated = it.key in sort.keys && it.value == sort[it.key]
                    if (isActivated) resultQuery += "&${it.key}=${it.value}"
                }

                filterSort.filterAndSort.filterChip.getOption().forEach {
                    val isActivated = it.key in filter.keys && filter[it.key] == it.value
                    if (isActivated) resultQuery += "&${it.key}=${it.value}"
                }

                getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = "", queryParam = resultQuery, type = QUICK_FILTER, pageName = pageName)
                val quickFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

                getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = "", queryParam = resultQuery, type = FULL_FILTER, pageName = pageName)
                val fullFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

                _filterSortChip.postValue(Response.loading())
                _recommendationItem.postValue(Response.loading())

                val recommendation = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = resultQuery, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()

                if (recommendation.isNotEmpty()) {
                    _filterSortChip.postValue(Response.success(FilterSortChip(fullFilterAsync.await(), quickFilterAsync.await().filterChip)))
                    _recommendationItem.postValue(Response.success(recommendation))
                } else {
                    _filterSortChip.postValue(Response.success(filterSort))
                    _recommendationItem.postValue(Response.empty())
                }
            }
        }){
            _filterSortChip.postValue(Response.error(it))
            _recommendationItem.postValue(Response.error(Exception(it.message), _recommendationItem.value?.data))
        }
    }

    fun getRecommendationFromEmptyFilter(option: RecommendationFilterChipsEntity.Option, pageName: String, queryParam: String, productId: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {

            // update select / deselect to full filter
            _filterSortChip.value?.data?.filterAndSort?.filterChip?.getOption()?.find { opt -> opt.key == option.key }?.let {
                it.isActivated = !it.isActivated
            }

            val oldFilterData = _filterSortChip.value?.data

            val filterString = "&" + _filterSortChip.value?.data?.filterAndSort?.filterChip?.getSelectedOption()?.joinToString(separator = "&") { opt ->
                "${opt.key}=${opt.value}"
            }

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = queryParam + filterString, type = QUICK_FILTER, pageName = pageName)
            val quickFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = queryParam + filterString, type = FULL_FILTER, pageName = pageName)
            val fullFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            _filterSortChip.postValue(Response.loading())
            _recommendationItem.postValue(Response.loading())

            val recommendation = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = queryParam + filterString, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()
            if(recommendation.isNotEmpty()){
                val filterData = FilterSortChip(fullFilterAsync.await(), quickFilterAsync.await().filterChip)
                _filterSortChip.postValue(Response.success(filterData))
                _recommendationItem.postValue(Response.success(recommendation))
            } else {
                _filterSortChip.postValue(Response.empty(oldFilterData))
                _recommendationItem.postValue(Response.empty())
            }
        }){
            _filterSortChip.postValue(Response.error(it))
            _recommendationItem.postValue(Response.error(Exception(it.message), _recommendationItem.value?.data))
        }
    }

    fun getSelectedSortFilter(): Map<String, String>{
        val selectedSort = _filterSortChip.value?.data?.filterAndSort?.sortChip?.find { it.isSelected && it.value != DEFAULT_VALUE_SORT }
        val selectedFilter = _filterSortChip.value?.data?.filterAndSort?.filterChip?.getSelectedOption()

        val map = mutableMapOf<String, String>()
        map[KEY_SORT] = DEFAULT_VALUE_SORT
        selectedSort?.value?.let { map[KEY_SORT] = selectedSort.value }
        selectedFilter?.forEach { map[it.key] = it.value }

        return map
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

    /**
     * [userId] is the function get user session id
     */
    fun userId() = userSessionInterface.userId

    /**
     * [addWishlist] is the void for handling adding wishlist item
     * @param model the recommendation item product is clicked
     * @param callback the callback for handling [added or removed, throwable] to UI
     */
    fun addWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)){
        if(model.isTopAds){
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    callback.invoke(false, e)
                }

                override fun onNext(wishlistModel: WishlistModel) {
                    if (wishlistModel.data != null) {
                        callback.invoke(true, null)
                    }
                }
            })
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    // do nothing
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    // do nothing
                }
            })
        }
    }

    /**
     * [addWishlist] is the void for handling removing wishlist item
     * @param model the recommendation item product is clicked
     * @param wishlistCallback the callback for handling [added or removed, throwable] to UI
     */
    fun removeWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // do nothing
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // do nothing
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                wishlistCallback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                wishlistCallback.invoke(true, null)
            }
        })
    }

    companion object{
        private const val COUNT_PRODUCT = 20
        const val KEY_SORT = "ob"
        const val DEFAULT_VALUE_SORT = "23"
    }
}