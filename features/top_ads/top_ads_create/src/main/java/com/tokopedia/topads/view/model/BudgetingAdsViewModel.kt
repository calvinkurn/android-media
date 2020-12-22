package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class BudgetingAdsViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val bidInfoUseCase: BidInfoUseCase) : BaseViewModel(dispatcher) {


    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit, onEmpty: (() -> Unit)) {

        launch(block = {
            bidInfoUseCase.setParams(suggestions, KEYWORD, SOURCE_VALUE)
            bidInfoUseCase.executeQuerySafeMode({
                if (it.topadsBidInfo.data.isEmpty()) {
                    onEmpty()
                } else {
                    onSuccess(it.topadsBidInfo.data)
                }
            }, {
                it.printStackTrace()
            })
        })
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        launch(block = {
            bidInfoUseCase.setParams(suggestions, PRODUCT, SOURCE_VALUE)
            bidInfoUseCase.executeQuerySafeMode({
                onSuccess(it.topadsBidInfo.data)
            }, {
                it.printStackTrace()
            })
        })
    }
}