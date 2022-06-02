package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetMembershipDetailUseCase
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDetailUiModel
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

class MembershipDetailViewModel @Inject constructor(
    private val getShopScoreUpdatePeriod: Lazy<GetMembershipDetailUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val membershipBasicInfo: LiveData<Result<MembershipDetailUiModel>>
        get() = _membershipBasicInfo

    private val _membershipBasicInfo = MutableLiveData<Result<MembershipDetailUiModel>>()

    fun getMembershipBasicInfo() {
        launchCatchError(block = {
            val shopId = userSession.get().shopId
            getShopScoreUpdatePeriod.get().setParams(shopId.toLongOrZero())
            val result = withContext(dispatchers.io) {
                getShopScoreUpdatePeriod.get().executeOnBackground()
            }
            _membershipBasicInfo.value = Success(result)
        }, onError = {
            _membershipBasicInfo.value = Fail(it)
        })
    }
}