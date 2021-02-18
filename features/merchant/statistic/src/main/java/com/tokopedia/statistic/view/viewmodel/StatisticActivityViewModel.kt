package com.tokopedia.statistic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/02/21
 */

class StatisticActivityViewModel @Inject constructor(
        private val checkWhitelistedStatusUseCase: CheckWhitelistedStatusUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val whitelistedStatus: LiveData<Result<Boolean>>
        get() = _whitelistedStatus

    private val _whitelistedStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun checkWhiteListStatus() {
        launchCatchError(block = {
            val whiteListPageSource = Const.WHITE_LIST_KEY_BUYER_INSIGHT
            checkWhitelistedStatusUseCase.params = CheckWhitelistedStatusUseCase.createParam(whiteListPageSource)
            val result = withContext(dispatchers.io) {
                checkWhitelistedStatusUseCase.executeOnBackground()
            }
            _whitelistedStatus.postValue(Success(result))
        }, onError = {
            _whitelistedStatus.postValue(Fail(it))
        })
    }
}