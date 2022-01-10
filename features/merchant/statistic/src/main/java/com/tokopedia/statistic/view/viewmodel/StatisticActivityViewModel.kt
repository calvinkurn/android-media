package com.tokopedia.statistic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/02/21
 */

class StatisticActivityViewModel @Inject constructor(
    private val userSession: Lazy<UserSessionInterface>,
    private val checkWhitelistedStatusUseCase: Lazy<CheckWhitelistedStatusUseCase>,
    private val getUserRoleUseCase: Lazy<GetUserRoleUseCase>,
    private val getPMStatusUseCase: Lazy<GetPMStatusUseCase>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val whitelistedStatus: LiveData<Result<Boolean>>
        get() = _whitelistedStatus
    val userRole: LiveData<Result<List<String>>>
        get() = _userRole

    private val _whitelistedStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val _userRole = MutableLiveData<Result<List<String>>>()

    fun checkWhiteListStatus() {
        launchCatchError(block = {
            val whiteListPageSource = Const.WHITE_LIST_KEY_OPERATIONAL_STATISTIC
            val useCase = checkWhitelistedStatusUseCase.get()
            val requestParams = useCase.createParam(whiteListPageSource)
            val result = Success(withContext(dispatchers.io) {
                useCase.execute(requestParams)
            })
            _whitelistedStatus.postValue(result)
        }, onError = {
            _whitelistedStatus.postValue(Fail(it))
        })
    }

    fun getUserRole() {
        launchCatchError(block = {
            val result: Success<List<String>> = Success(withContext(dispatchers.io) {
                getUserRoleUseCase.get().params =
                    GetUserRoleUseCase.createParam(userSession.get().userId)
                return@withContext getUserRoleUseCase.get().executeOnBackground()
            })
            _userRole.postValue(result)
        }, onError = {
            _userRole.postValue(Fail(it))
        })
    }

    fun fetchPMStatus() {
        launchCatchError(block = {
            val useCase = getPMStatusUseCase.get()
            val shopId = userSession.get().shopId
            useCase.params = GetPMStatusUseCase.createParams(shopId)
            val pmStatusData = useCase.executeOnBackground()
            updateUserSession(pmStatusData)
        }, onError = {})
    }

    private fun updateUserSession(pmStatusData: PMStatusUiModel) {
        with(userSession.get()) {
            setIsShopOfficialStore(pmStatusData.isOfficialStore)
            setIsGoldMerchant(pmStatusData.isPowerMerchant())
            setIsPowerMerchantIdle(pmStatusData.isPowerMerchantIdle())
        }
    }
}