package com.tokopedia.topads.credit.history.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsGetFreeDepositUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.domain.usecase.TopadsCreditHistoryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class TopAdsCreditHistoryViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val creditHistoryUseCase: TopadsCreditHistoryUseCase,
    @Named("Main") val dispatcher: CoroutineDispatcher,
    private val pendingRewardUseCase: TopadsGetFreeDepositUseCase,
) : BaseViewModel(dispatcher) {

    private val _creditsHistory = MutableLiveData<Result<TopAdsCreditHistory>>()
    val creditsHistory : LiveData<Result<TopAdsCreditHistory>> = _creditsHistory
    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val creditAmount = MutableLiveData<String>()

    private val _expiryDateHiddenTrial: MutableLiveData<String?> = MutableLiveData()
    val expiryDateHiddenTrial: LiveData<String?> = _expiryDateHiddenTrial

    fun loadPendingReward() {
        pendingRewardUseCase.execute {
            _expiryDateHiddenTrial.postValue(it.pendingRebateCredit)
        }
    }

    fun getCreditHistory(startDate: Date? = null, endDate: Date? = null) {
        launchCatchError(block = {
            creditHistoryUseCase.executeQuery(startDate, endDate).collect {
                _creditsHistory.postValue(it)
            }
        }, onError = {
            _creditsHistory.postValue(Fail(it))
        })
    }

    fun getAutoTopUpStatus() {
        autoTopUpUSeCase.setQuery()
        autoTopUpUSeCase.setParams()
        autoTopUpUSeCase.execute({ data ->
            when {
                data.response == null -> getAutoTopUpStatus.value = Fail(Exception("Gagal mengambil status"))
                data.response.errors.isEmpty() -> getAutoTopUpStatus.value = Success(data.response.data)
                else -> getAutoTopUpStatus.value = Fail(ResponseErrorException(data.response.errors))
            }
        }, {
            getAutoTopUpStatus.value = Fail(it)
        })
    }

    fun getShopDeposit() {
        topAdsGetShopDepositUseCase.execute({
            creditAmount.value = it.topadsDashboardDeposits.data.amountFmt
        }
                , {
            Timber.e(it, "P1#TOPADS_CREDIT_HISTORY_VIEW_MODEL_DEPOSIT#%s", it.localizedMessage)

        })
    }

    companion object {
        private const val PARAM_USER_ID = "userId"
    }
}