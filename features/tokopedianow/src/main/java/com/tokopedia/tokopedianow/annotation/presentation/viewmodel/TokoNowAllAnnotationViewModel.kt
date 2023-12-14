package com.tokopedia.tokopedianow.annotation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.addAnnotations
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import com.tokopedia.tokopedianow.annotation.presentation.model.LoadMoreDataModel
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehouseIds
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowAllAnnotationViewModel @Inject constructor(
    private val getAllAnnotationPageUseCase: GetAllAnnotationPageUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {
    companion object {
        private const val FIRST_PAGE_LAST_ID = "0"
    }

    private val _headerTitle = MutableLiveData<Result<String>>()
    private val _firstPage = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _loadMore = MutableLiveData<List<Visitable<*>>>()

    private val layout: MutableList<Visitable<*>> = arrayListOf()
    private var needToLoadMoreData: LoadMoreDataModel = LoadMoreDataModel(true)

    val headerTitle: LiveData<Result<String>>
        get() = _headerTitle
    val firstPage: LiveData<Result<List<Visitable<*>>>>
        get() = _firstPage
    val loadMore: LiveData<List<Visitable<*>>>
        get() = _loadMore

    fun getFirstPage(
        categoryId: String,
        annotationType: String
    ) {
        launchCatchError(block = {
            val warehouseIds = mapToWarehouseIds(addressData.getAddressData())
            val response = getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                warehouseIds = warehouseIds,
                annotationType = annotationType,
                pageLastId = FIRST_PAGE_LAST_ID
            )

            layout.addAnnotations(response)
            _firstPage.postValue(Success(layout))
            _headerTitle.postValue(Success(response.annotationHeader.title))

            needToLoadMoreData = needToLoadMoreData.copy(
                isNeededToLoadMore = response.isNeededToLoadMore(),
                pageLastId = response.pagination.pageLastID
            )
        }) {
            _headerTitle.postValue(Fail(it))
            _firstPage.postValue(Fail(it))
        }
    }

    fun loadMore(
        categoryId: String,
        annotationType: String,
        isAtTheBottomOfThePage: Boolean
    ) {
        if (isAtTheBottomOfThePage && needToLoadMoreData.isNeededToLoadMore) {
            if (layout.last() is LoadingMoreModel) {
                launchCatchError(block = {
                    val warehouseIds = mapToWarehouseIds(addressData.getAddressData())
                    val response = getAllAnnotationPageUseCase.execute(
                        categoryId = categoryId,
                        warehouseIds = warehouseIds,
                        annotationType = annotationType,
                        pageLastId = needToLoadMoreData.pageLastId
                    )

                    if (response.annotationList.isNotEmpty()) {
                        layout.removeLoadMore()
                        layout.addAnnotations(response)
                        _loadMore.postValue(layout)
                    } else {
                        _loadMore.postValue(emptyList())
                    }

                    needToLoadMoreData = needToLoadMoreData.copy(
                        isNeededToLoadMore = response.isNeededToLoadMore(),
                        pageLastId = response.pagination.pageLastID
                    )
                }) {
                    layout.removeLoadMore()
                    _loadMore.postValue(layout)
                }
            } else {
                layout.addLoadMore()
                _loadMore.value = layout
            }
        }
    }

}
