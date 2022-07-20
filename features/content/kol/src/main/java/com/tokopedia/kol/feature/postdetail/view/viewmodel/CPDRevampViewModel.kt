package com.tokopedia.kol.feature.postdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendation
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationResponse
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetRecommendationPostUseCase
import com.tokopedia.kol.feature.postdetail.view.datamodel.CDPRevampDataUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CPDRevampViewModel@Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val getRecommendationPostUseCase: GetRecommendationPostUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
): BaseViewModel(baseDispatcher.main){

    var currentCursor = ""
    private val _getCDPPostRecomData = MutableLiveData<Result<FeedXPostRecommendation>>()
    private val _getCDPPostFirstPostData = MutableLiveData<Result<CDPRevampDataUiModel>>()

    val cDPPostRecomData : LiveData<Result<FeedXPostRecommendation>>
       get() = _getCDPPostRecomData

    val getCDPPostFirstPostData :LiveData<Result<CDPRevampDataUiModel>>
        get() = _getCDPPostFirstPostData


    fun getCDPPostDetailFirstData(id: String){
        launchCatchError( block = {
            val results = withContext(baseDispatcher.io) {
                getFeedDataResult(id)
            }

            _getCDPPostFirstPostData.value = Success(results)

        }) {
            _getCDPPostFirstPostData.value = Fail(it)

        }
    }

    fun getCDPRecomData(id: String){
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getCDPRecomDataResult(id)
            }
            currentCursor = results.feedXPostRecommendation.nextCursor

            _getCDPPostRecomData.value = Success(results.feedXPostRecommendation)

        }) {
            _getCDPPostRecomData.value = Fail(it)
        }

    }
    private suspend fun getFeedDataResult(detailId: String): CDPRevampDataUiModel {
        try {
            return getPostDetailUseCase.executeForCDPRevamp(cursor = "", detailId = detailId)
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    private suspend fun getCDPRecomDataResult(activityId: String): FeedXPostRecommendationData {
        try {
            return getRecommendationPostUseCase.execute(cursor = currentCursor, activityId = activityId )
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

}