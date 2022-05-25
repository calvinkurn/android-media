package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetMembershipBasicInfoUseCase
import com.tokopedia.power_merchant.subscribe.view.model.MembershipBasicInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 24/05/22.
 */

class MembershipActivityViewModel @Inject constructor(
    private val getMembershipBasicInfo: Lazy<GetMembershipBasicInfoUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val membershipBasicInfo: LiveData<Result<MembershipBasicInfoUiModel>>
        get() = _membershipBasicInfo

    private val _membershipBasicInfo = MutableLiveData<Result<MembershipBasicInfoUiModel>>()

    fun getMembershipBasicInfo() {
        launchCatchError(block = {
            val shopId = userSession.get().shopId
            val result = withContext(dispatchers.io) {
                getMembershipBasicInfo.get().execute(shopId)
            }
            _membershipBasicInfo.value = Success(result)
        }, onError = {
            _membershipBasicInfo.value = Fail(it)
        })
    }
}