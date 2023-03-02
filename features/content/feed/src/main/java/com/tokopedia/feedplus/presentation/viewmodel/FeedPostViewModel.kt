package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.domain.mapper.MapperFeedHome
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    var source: String = ""

    private val _feedHome = MutableLiveData<Result<FeedModel>>()
    val feedHome: LiveData<Result<FeedModel>>
        get() = _feedHome

    fun fetchFeedPosts() {
        launchCatchError(dispatchers.main, block = {
            val response = withContext(dispatchers.io) {
                val cursor = feedHome.value?.let {
                    if (it is Success) {
                        it.data.pagination.cursor
                    } else {
                        ""
                    }
                } ?: ""

                feedXHomeUseCase.createParams(
                    source,
                    cursor
                )
                val result = feedXHomeUseCase.executeOnBackground()
                MapperFeedHome.transform(result.feedXHome)
            }
            _feedHome.value = Success(response)
        }) {
            _feedHome.value = Fail(it)
        }
    }
}
