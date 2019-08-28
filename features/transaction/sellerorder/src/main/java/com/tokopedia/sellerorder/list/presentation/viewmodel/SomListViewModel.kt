package com.tokopedia.sellerorder.list.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.usecase.GetSomListTickerUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class SomListViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                           private val somListTickerUseCase: GetSomListTickerUseCase) : BaseViewModel(dispatcher) {

    val tickerListResult = MutableLiveData<Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>>>()

    fun getTickerList(rawQuery: String, requestBy: String, client: String, fromCloud: Boolean = true) {
        launch {
            tickerListResult.value = somListTickerUseCase.execute(rawQuery, requestBy, client, fromCloud)
        }
    }
}