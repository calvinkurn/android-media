package com.tokopedia.digital.digital_recommendation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationViewModel @Inject constructor(
        private val digitalRecommendationUseCase: DigitalRecommendationUseCase,
        private val userSessionInterface: UserSessionInterface,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _digitalRecommendationItems = MutableLiveData<Result<List<DigitalRecommendationModel>>>()
    val digitalRecommendationItems: LiveData<Result<List<DigitalRecommendationModel>>>
        get() = _digitalRecommendationItems

    fun fetchDigitalRecommendation() {
        launch {
            _digitalRecommendationItems.postValue(digitalRecommendationUseCase.execute())
        }
    }

    fun getUserId(): String =
            if (userSessionInterface.userId.isNotEmpty()) userSessionInterface.userId
            else "0"

}