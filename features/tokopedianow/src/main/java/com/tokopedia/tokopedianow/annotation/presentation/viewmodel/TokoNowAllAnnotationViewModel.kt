package com.tokopedia.tokopedianow.annotation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.addAnnotations
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.annotation.domain.mapper.VisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import com.tokopedia.tokopedianow.annotation.presentation.model.LoadMoreDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowAllAnnotationViewModel @Inject constructor(
    private val getAllAnnotationPageUseCase: GetAllAnnotationPageUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {
    private val _headerTitle = MutableLiveData<Result<String>>()
    private val _firstPage = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _loadMore = MutableLiveData<List<Visitable<*>>>()
    private val _isOnScrollNotNeeded = MutableLiveData<Unit>()

    private val layout: MutableList<Visitable<*>> = arrayListOf()
    private var needToLoadMoreData: LoadMoreDataModel = LoadMoreDataModel(isNeededToLoadMore = true)

    val headerTitle: LiveData<Result<String>>
        get() = _headerTitle
    val firstPage: LiveData<Result<List<Visitable<*>>>>
        get() = _firstPage
    val loadMore: LiveData<List<Visitable<*>>>
        get() = _loadMore
    val isOnScrollNotNeeded: LiveData<Unit>
        get() = _isOnScrollNotNeeded

    fun getFirstPage(
        categoryId: String,
        annotationType: String
    ) {
        launchCatchError(block = {
            val response = getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                annotationType = AnnotationType.valueOf(annotationType),
                pageLastId = String.EMPTY
            )

            layout.addAnnotations(response)

            if (response.isNeededToLoadMore()) {
                layout.addLoadMore()
            }

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
        if (isAtTheBottomOfThePage) {
            when {
                layout.last() is LoadingMoreModel -> {
                    launchCatchError(block = {
                        val response = getAllAnnotationPageUseCase.execute(
                            categoryId = categoryId,
                            annotationType = AnnotationType.valueOf(annotationType),
                            pageLastId = needToLoadMoreData.pageLastId
                        )

                        needToLoadMoreData = if (response.annotationList.isNotEmpty()) {
                            layout.removeLoadMore()
                            layout.addAnnotations(response)

                            if (response.isNeededToLoadMore()) {
                                layout.addLoadMore()
                            }

                            _loadMore.postValue(layout)

                            needToLoadMoreData.copy(
                                isNeededToLoadMore = response.isNeededToLoadMore(),
                                pageLastId = response.pagination.pageLastID
                            )
                        } else {
                            layout.removeLoadMore()
                            _loadMore.postValue(layout)

                            needToLoadMoreData.copy(
                                isNeededToLoadMore = false
                            )
                        }
                    }) {
                        layout.removeLoadMore()
                        _loadMore.postValue(layout)
                    }
                }
                else -> {
                    _isOnScrollNotNeeded.value = Unit
                }
            }
        }
    }

}
