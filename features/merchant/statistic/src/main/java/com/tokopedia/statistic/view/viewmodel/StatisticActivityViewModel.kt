package com.tokopedia.statistic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.usecase.GetElementBenefitByKeyBulkUseCase
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/02/21
 */

class StatisticActivityViewModel @Inject constructor(
    private val userSession: Lazy<UserSessionInterface>,
    private val checkWhitelistedStatusUseCase: Lazy<CheckWhitelistedStatusUseCase>,
    private val getUserRoleUseCase: Lazy<GetUserRoleUseCase>,
    private val getElementBenefitByKeyBulkUseCase: Lazy<GetElementBenefitByKeyBulkUseCase>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel() {

    val whitelistedStatus: LiveData<Result<Boolean>>
        get() = _whitelistedStatus
    val userRole: LiveData<Result<List<String>>>
        get() = _userRole

    private val _whitelistedStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private val _userRole = MutableLiveData<Result<List<String>>>()

    private val _paywallAccessState = MutableStateFlow(false)
    val paywallAccess: StateFlow<Boolean> = _paywallAccessState

    fun checkWhiteListStatus() {
        viewModelScope.launch(dispatchers.io) {
            runCatching {
                val whiteListPageSource = Const.WHITE_LIST_KEY_TRAFFIC_INSIGHT
                val useCase = checkWhitelistedStatusUseCase.get()
                val requestParams = useCase.createParam(whiteListPageSource)
                val result = Success(withContext(dispatchers.io) {
                    useCase.execute(requestParams)
                })
                _whitelistedStatus.postValue(result)
            }.onFailure {
                _whitelistedStatus.postValue(Fail(it))
            }
        }
    }

    fun getUserRole() {
        viewModelScope.launch(dispatchers.io) {
            runCatching {
                val result: Success<List<String>> = Success(withContext(dispatchers.io) {
                    getUserRoleUseCase.get().params =
                        GetUserRoleUseCase.createParam(userSession.get().userId)
                    return@withContext getUserRoleUseCase.get().executeOnBackground()
                })
                _userRole.postValue(result)
            }.onFailure {
                _userRole.postValue(Fail(it))
            }
        }
    }

    fun fetchPaywallAccessState() {
        viewModelScope.launch(dispatchers.main) {
            runCatching {
                val shopId = userSession.get().shopId
                val elementKey =
                    GetElementBenefitByKeyBulkUseCase.Companion.Keys.STATISTIC_PAYWALL_ACCESS
                val source = GetElementBenefitByKeyBulkUseCase.Companion.Sources.STATISTIC
                val data = withContext(dispatchers.io) {
                    getElementBenefitByKeyBulkUseCase.get().execute(
                        shopId = shopId,
                        elementKeys = listOf(elementKey),
                        source = source,
                        useCache = true
                    )
                }
                val isGranted = data.isGrantedByElementKey(elementKey)
                _paywallAccessState.value = isGranted
            }.onFailure {
                _paywallAccessState.value = false
            }
        }
    }
}
