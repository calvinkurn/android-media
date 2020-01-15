package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.domain.usecase.GetSellerHomeLayoutUseCase
import com.tokopedia.sellerhome.view.model.SomeUiModel
import com.tokopedia.sellerhome.view.model.TrendLineViewUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val getSellerHomeLayoutUseCase: GetSellerHomeLayoutUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val layoutResponse = MutableLiveData<Result<List<TrendLineViewUiModel>>>()

    fun getLayout() {

        launchCatchError(block = {
            layoutResponse.value = Success(
                    withContext(Dispatchers.IO) {
                        getSellerHomeLayoutUseCase.executeOnBackground()
                    }
            )
        }, onError = {
            layoutResponse.value = Fail(it)
        })
    }
}