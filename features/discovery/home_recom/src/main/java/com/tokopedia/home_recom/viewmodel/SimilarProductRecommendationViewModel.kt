package com.tokopedia.home_recom.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_recom.model.entity.FilterSortChip
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.util.getOption
import com.tokopedia.home_recom.util.getSelectedOption
import com.tokopedia.home_recom.util.isActivated
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips.Companion.FULL_FILTER
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips.Companion.QUICK_FILTER
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.toRecommendationWidget
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.async
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

    private val _recommendationItem = MutableLiveData<Response<Pair<List<RecommendationItem>, Boolean>>>()
    val recommendationItem: LiveData<Response<Pair<List<RecommendationItem>, Boolean>>> get() = _recommendationItem
    private val _filterSortChip = MutableLiveData<Response<FilterSortChip>>()
    val filterSortChip: LiveData<Response<FilterSortChip>> get() = _filterSortChip

    fun getSimilarProductRecommendation(page: Int = 1, queryParam: String, productId: String, pageName: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {
            if(page == 1 && _recommendationItem.value != null) _recommendationItem.postValue(null)
            if (_recommendationItem.value == null) _recommendationItem.postValue(Response.loading())
            else _recommendationItem.postValue(Response.loadingMore(_recommendationItem.value?.data))
            val userId: Int = userSessionInterface.userId.toIntOrZero()
            val filterAndSort: FilterSortChip?
            if(page == 1){
                getRecommendationFilterChips.setParams(userId = userId, productIDs = productId, queryParam = queryParam, type = QUICK_FILTER, pageName = pageName)
                val quickFilter = getRecommendationFilterChips.executeOnBackground()

                getRecommendationFilterChips.setParams(userId = userId, productIDs = productId, queryParam = queryParam, type = FULL_FILTER, pageName = pageName)
                val fullFilter = getRecommendationFilterChips.executeOnBackground()
                filterAndSort = FilterSortChip(fullFilter, quickFilter.filterChip)
                _filterSortChip.postValue(Response.success(filterAndSort))
            }
            val params = singleRecommendationUseCase.getRecomParams(pageNumber = page, productIds = listOf(productId), queryParam = queryParam)

            val recommendationWidget = singleRecommendationUseCase.createObservable(params).toBlocking().first()
            val recommendationItems = recommendationWidget.toRecommendationWidget().recommendationItemList
            if(recommendationItems.isEmpty() && page == 1){
                _filterSortChip.postValue(Response.error(Exception()))
                _recommendationItem.postValue(Response.error(Exception()))
            } else {
                _recommendationItem.postValue(Response.success(Pair(recommendationItems.map {
                    it.copy(position = it.position + (page - 1) * COUNT_PRODUCT)
                }, recommendationWidget.pagination.hasNext)))
            }
        }){ throwable ->
            if(page == 1) _filterSortChip.postValue(Response.error(throwable))

            if(throwable is IOException || throwable is TimeoutException){
                _recommendationItem.postValue(Response.error(TimeoutException(), _recommendationItem.value?.data))
            } else {
                _recommendationItem.postValue(Response.error(throwable, _recommendationItem.value?.data))
            }
        }
    }

    fun getRecommendationFromQuickFilter(title: String, queryParam: String, productId: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {
            _filterSortChip.value?.data?.let { filterSortChip ->

                _filterSortChip.postValue(Response.loading())
                _recommendationItem.postValue(Response.loading())

                // update select / deselect to full filter
                filterSortChip.filterAndSort.filterChip.getOption().find { opt -> opt.name == title }?.let{
                    it.isActivated = !it.isActivated
                }

                val selectedQuickFilter = _filterSortChip.value?.data?.quickFilterList?.getOption()?.find { opt -> opt.name == title}
                selectedQuickFilter?.let {
                    it.isActivated = !it.isActivated
                }

                val filterString = _filterSortChip.value?.data?.filterAndSort?.filterChip?.getSelectedOption()?.joinToString(separator = "&") { opt ->
                    "${opt.key}=${opt.value}"
                }
                val sortString = _filterSortChip.value?.data?.filterAndSort?.sortChip?.filter { it.isSelected }?.joinToString (separator = "&"){ it.key + "=" + it.value }
                val param = "$queryParam&$filterString&$sortString"

                val recommendationWidget = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = param, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()

                val filterData = filterSortChip.copy(
                        filterAndSort = filterSortChip.filterAndSort.copy(
                                filterChip = filterSortChip.filterAndSort.filterChip.map {
                                    it.copy(
                                            options = it.options.map { option ->
                                                option.copy(
                                                        isActivated = filterSortChip.filterAndSort.filterChip.getOption().find { option.key == it.key && option.value == it.value && it.isActivated } != null
                                                )
                                            }
                                    )
                                },
                                sortChip = filterSortChip.filterAndSort.sortChip.map { sortChip ->
                                    sortChip.copy(
                                            isSelected = filterSortChip.filterAndSort.sortChip.find { sortChip.key == it.key && sortChip.value == it.value && it.isSelected } != null
                                    )
                                }
                        ),
                        quickFilterList = filterSortChip.quickFilterList.map { recomFilterChip ->
                            recomFilterChip.copy(
                                    options = recomFilterChip.options.map {
                                        it.copy(
                                                isActivated = it.name == title && selectedQuickFilter?.isActivated ?: false
                                        )
                                    }
                            )

                        }
                )
                _filterSortChip.postValue(Response.success(filterData))
                if (recommendationWidget.recommendation.isNotEmpty()) {
                    val recommendationItems = recommendationWidget.toRecommendationWidget().recommendationItemList
                    val dimension61 = filterString + if(sortString?.isNotEmpty() == true)"&" else "" + sortString
                    _recommendationItem.postValue(Response.success(Pair(recommendationItems.map { it.copy(dimension61 = dimension61) }, recommendationWidget.pagination.hasNext)))
                } else {
                    _recommendationItem.postValue(Response.empty())
                }
            }
        }){
            _filterSortChip.postValue(Response.error(it))
            _recommendationItem.postValue(Response.error(Exception(it.message), _recommendationItem.value?.data))
        }
    }

    fun getRecommendationFromFullFilter(selectedSortFromFullFilter: Map<String, String>, selectedFilterFromFullFilter: Map<String, String>, queryParam: String, productId: String){
        launchCatchError(dispatcher.getIODispatcher(), block = {
            _filterSortChip.value?.data?.let { filterSort ->
                _filterSortChip.postValue(Response.loading())
                _recommendationItem.postValue(Response.loading())
                var resultQuery = queryParam
                var dimension61 = ""
                filterSort.filterAndSort.sortChip.forEach { sortChip ->
                    val isActivated = selectedSortFromFullFilter.isActivated(sortChip.key, sortChip.value)
                    if (isActivated) dimension61 += "&${sortChip.key}=${sortChip.value}"
                    sortChip.isSelected = isActivated
                    val quickFilter = filterSort.quickFilterList.getOption().find { sortChip.key == it.key && sortChip.name == it.name }
                    quickFilter?.let { it.isActivated = isActivated }
                }

                val mapFilter = mutableMapOf<String, String>()

                filterSort.filterAndSort.filterChip.getOption().forEach { filterChip ->
                    val isActivated = selectedFilterFromFullFilter.isActivated(filterChip.key, filterChip.value)
                    filterChip.isActivated = isActivated
                    if (isActivated) {
                        if(mapFilter.containsKey(filterChip.key)) mapFilter[filterChip.key] = mapFilter[filterChip.key] + "&" + filterChip.value
                        else mapFilter[filterChip.key] = filterChip.value
                    }
                    val quickFilter = filterSort.quickFilterList.getOption().find { filterChip.key == it.key && filterChip.name == it.name }
                    quickFilter?.let { it.isActivated = isActivated }
                }
                val selectedFilterString = mapFilter.map { it.key + "=" + it.value }.joinToString(",")
                if(dimension61.isNotEmpty()) dimension61 += "&$selectedFilterString"
                else dimension61 = selectedFilterString

                resultQuery += "&$dimension61"

                _filterSortChip.postValue(Response.loading())
                _recommendationItem.postValue(Response.loading())

                val recommendationWidget = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = resultQuery, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()
                val recomChip = filterSort.copy(
                        filterAndSort = filterSort.filterAndSort.copy(
                                filterChip = filterSort.filterAndSort.filterChip.map {
                                    it.copy(
                                            options = it.options.map { option ->
                                                option.copy(
                                                        isActivated = option.isActivated
                                                )
                                            }
                                    )
                                },
                                sortChip = filterSort.filterAndSort.sortChip.map { sortChip ->
                                    sortChip.copy(
                                            isSelected = sortChip.key in selectedSortFromFullFilter.keys && sortChip.value == selectedSortFromFullFilter[sortChip.key]
                                    )
                                }
                        ),
                        quickFilterList = filterSort.quickFilterList.map { recomFilterChip ->
                            recomFilterChip.copy(
                                    options = recomFilterChip.options.map {
                                        it.copy(
                                                isActivated = it.key in selectedFilterFromFullFilter.keys && selectedFilterFromFullFilter[it.key] == it.value
                                                        || it.key in selectedSortFromFullFilter.keys && it.value == selectedSortFromFullFilter[it.key]
                                        )
                                    }
                            )
                        }
                )

                _filterSortChip.postValue(Response.success(recomChip))

                if (recommendationWidget.recommendation.isNotEmpty()) {
                    val recommendationItems = recommendationWidget.toRecommendationWidget().recommendationItemList
                    _recommendationItem.postValue(Response.success(Pair(recommendationItems.map { it.copy(dimension61 = dimension61) }, recommendationWidget.pagination.hasNext)))
                } else {
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
            _filterSortChip.postValue(Response.loading())
            _recommendationItem.postValue(Response.loading())

            // update select / deselect to full filter
            _filterSortChip.value?.data?.filterAndSort?.filterChip?.getOption()?.find { opt -> opt.key == option.key }?.let {
                it.isActivated = !it.isActivated
            }

            val oldFilterData = _filterSortChip.value?.data
            val sortString = _filterSortChip.value?.data?.filterAndSort?.sortChip?.filter { it.isSelected }?.joinToString (separator = "&"){ it.key + "=" + it.value }
            val filterString = _filterSortChip.value?.data?.filterAndSort?.filterChip?.getSelectedOption()?.joinToString(separator = "&") { opt ->
                "${opt.key}=${opt.value}"
            }
            val dimension61 = "$sortString&$filterString"
            val query = "$queryParam&$sortString&$filterString"

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = query, type = QUICK_FILTER, pageName = pageName)

            val quickFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            getRecommendationFilterChips.setParams(userId = userSessionInterface.userId.toIntOrZero(), productIDs = productId, queryParam = query, type = FULL_FILTER, pageName = pageName)
            val fullFilterAsync = async { getRecommendationFilterChips.executeOnBackground() }

            _filterSortChip.postValue(Response.loading())
            _recommendationItem.postValue(Response.loading())

            val recommendationWidget = singleRecommendationUseCase.createObservable(singleRecommendationUseCase.getRecomParams(queryParam = query, productIds = listOf(productId), pageNumber = 1)).toBlocking().first()

            if (recommendationWidget.recommendation.isNotEmpty()) {
                val recommendationItems = recommendationWidget.toRecommendationWidget().recommendationItemList
                val filterData = FilterSortChip(fullFilterAsync.await(), quickFilterAsync.await().filterChip)
                _filterSortChip.postValue(Response.success(filterData))
                _recommendationItem.postValue(Response.success(Pair(recommendationItems.map { it.copy(dimension61 = dimension61) }, recommendationWidget.pagination.hasNext)))
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
        val map = mutableMapOf<String, String>()
        map[KEY_SORT] = DEFAULT_VALUE_SORT
        _filterSortChip.value?.data?.let {
            val selectedSort = it.filterAndSort.sortChip.find { it.isSelected && it.value != DEFAULT_VALUE_SORT }
            val selectedFilter = it.filterAndSort.filterChip.getSelectedOption()

            selectedSort?.value?.let { map[KEY_SORT] = selectedSort.value }
            selectedFilter.forEach {
                if(map.containsKey(it.key)){
                    map[it.key] = map[it.key] + "#" + it.value
                } else {
                    map[it.key] = it.value
                }
            }
        }


        return map
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

    /**
     * [userId] is the function get user session id
     */
    fun userId(): String = userSessionInterface.userId

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