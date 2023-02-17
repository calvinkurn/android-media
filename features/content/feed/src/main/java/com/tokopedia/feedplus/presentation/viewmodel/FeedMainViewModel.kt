package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedTabsUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val feedTabsUseCase: FeedTabsUseCase,
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _feedTabs = MutableLiveData<Result<FeedTabsModel>>()
    val feedTabs: LiveData<Result<FeedTabsModel>>
        get() = _feedTabs

    fun fetchFeedTabs() {
        launchCatchError(dispatchers.io, block = {
            feedXHeaderUseCase.setRequestParams(
                FeedXHeaderUseCase.createParam()
            )
            val response = feedXHeaderUseCase.executeOnBackground()
            _feedTabs.postValue(Success(MapperFeedTabs.transform(response.feedXHeaderData)))
            handleCreationData(
                    MapperFeedTabs.getCreationBottomSheetData(
                        response.feedXHeaderData
                    )
            )
        }) {
            _feedTabs.postValue(Fail(it))
        }
    }

}
