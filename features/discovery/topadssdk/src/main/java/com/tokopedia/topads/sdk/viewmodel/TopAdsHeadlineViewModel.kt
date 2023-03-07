package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class TopAdsHeadlineViewModel @Inject constructor(
    private val topAdsAddressHelper: TopAdsAddressHelper,
    private val getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase
) : BaseViewModel(Dispatchers.Main) {

    fun getTopAdsHeadlineData(
        params: String,
        onSuccess: ((CpmModel) -> Unit)?,
        onError: (() -> Unit)?,
    ) {
        viewModelScope.launchCatchError(
                block = {
                    val addressData = topAdsAddressHelper.getAddressData()
                    getTopAdsHeadlineUseCase.setParams(params, addressData)
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