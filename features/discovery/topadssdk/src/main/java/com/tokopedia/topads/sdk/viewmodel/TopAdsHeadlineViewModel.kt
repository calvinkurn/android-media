package com.tokopedia.topads.sdk.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper


class TopAdsHeadlineViewModel(private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    private val graphqlRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase by lazy {
        GetTopAdsHeadlineUseCase(graphqlRepository)
    }

    fun getTopAdsHeadlineData(
        params: String,
        onSuccess: ((CpmModel) -> Unit)?,
        onError: (() -> Unit)?,
    ) {
        viewModelScope.launchCatchError(
                block = {
                    val addressData = TopAdsAddressHelper(applicationContext).getAddressData()
                    getTopAdsHeadlineUseCase.setParams(params, addressData)
                    val data = getTopAdsHeadlineUseCase.executeOnBackground()
                    if (data.displayAds.data?.isNotEmpty() == true) {
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