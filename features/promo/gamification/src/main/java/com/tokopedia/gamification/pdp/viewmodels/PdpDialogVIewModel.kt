package com.tokopedia.gamification.pdp.viewmodels

import androidx.lifecycle.ViewModel
import com.tokopedia.gamification.pdp.usecase.GamingRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import kotlinx.coroutines.CoroutineDispatcher

class PdpDialogVIewModel(val getRecommendationUseCase: GetRecommendationUseCase,
                         val gamingRecommendationUseCase: GamingRecommendationUseCase,
                         val uiDispatcher: CoroutineDispatcher,
                         val workerDispatcher: CoroutineDispatcher) : ViewModel() {


    fun getDat(){

    }

}