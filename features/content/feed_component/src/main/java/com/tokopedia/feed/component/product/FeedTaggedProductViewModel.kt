package com.tokopedia.feed.component.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.domain.mapper.ProductMapper
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 19/06/23
 */
class FeedTaggedProductViewModel @Inject constructor(
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _feedTagProductList = MutableLiveData<Result<List<FeedTaggedProductUiModel>>>()
    val feedTagProductList: LiveData<Result<List<FeedTaggedProductUiModel>>>
        get() = _feedTagProductList

    var shopId = ""
    private var cursor = ""
    private var prevActivityId = ""

    fun fetchFeedProduct(activityId: String, products: List<FeedTaggedProductUiModel>, sourceType: FeedTaggedProductUiModel.SourceType) {
        viewModelScope.launch {
            try {
                if (activityId != prevActivityId) cursor = ""

                val currentList: List<FeedTaggedProductUiModel> = when {
                    products.isNotEmpty() -> products
                    _feedTagProductList.value is Success && activityId == prevActivityId -> (_feedTagProductList.value as Success).data
                    else -> emptyList()
                }

                val response = withContext(dispatchers.io) {
                    feedXGetActivityProductsUseCase(
                        feedXGetActivityProductsUseCase.getFeedDetailParam(
                            activityId,
                            cursor
                        )
                    ).data
                }

                cursor = response.nextCursor

                val mappedData = response.products.filter { new ->
                    currentList.firstOrNull { current ->
                        current.id == new.id
                    } == null
                }.map {
                    ProductMapper.transform(it, response.campaign, sourceType)
                }
                _feedTagProductList.value = Success(currentList + mappedData)

                prevActivityId = activityId
                shopId = mappedData.firstOrNull()?.shop?.id ?: ""
            } catch (t: Throwable) {
                _feedTagProductList.value = Fail(t)
            }
        }
    }
}
