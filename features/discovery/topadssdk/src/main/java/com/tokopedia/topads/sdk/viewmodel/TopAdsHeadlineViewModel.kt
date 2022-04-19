package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase


class TopAdsHeadlineViewModel : ViewModel() {

    private val graphqlRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase by lazy {
        GetTopAdsHeadlineUseCase(graphqlRepository)
    }

    fun getTopAdsHeadlineData(params: String, onSuccess: ((CpmModel) -> Unit)?, onError: (() -> Unit)?) {
        viewModelScope.launchCatchError(
                block = {
                    getTopAdsHeadlineUseCase.setParams(params)
                    val data = getTopAdsHeadlineUseCase.executeOnBackground()
                    if (data.displayAds.data.isNotEmpty()) {
                        onSuccess?.invoke(data.displayAds)
                    } else {
                        onError?.invoke()
                    }
                },
                onError = {
                    it.printStackTrace()
                    onError?.invoke()
                }
        )
    }
}