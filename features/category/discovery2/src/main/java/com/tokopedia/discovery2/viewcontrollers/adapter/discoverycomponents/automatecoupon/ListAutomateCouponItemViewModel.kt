package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.data.automatecoupon.ClaimFailure
import com.tokopedia.discovery2.datamapper.AutomateCouponMapper.mapToCtaState
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ListAutomateCouponItemViewModel(
    application: Application,
    component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(component), CoroutineScope {

    private val models = MutableLiveData<AutomateCouponUiModel>()
    private val ctaStateAfterClaim = MutableLiveData<AutomateCouponCtaState>()

    private val _showErrorClaimCoupon: SingleLiveEvent<ClaimFailure> = SingleLiveEvent()
    fun shouldShowErrorClaimCouponToaster(): LiveData<ClaimFailure> = _showErrorClaimCoupon

    @JvmField
    @Inject
    var claimCouponClickUseCase: ClaimCouponClickUseCase? = null

    @JvmField
    @Inject
    var userSession: UserSessionInterface? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()

        component.automateCoupons?.firstOrNull()?.let {
            models.value = it
        }
    }

    fun getCouponModel(): LiveData<AutomateCouponUiModel> = models

    fun getCTAState(): LiveData<AutomateCouponCtaState> {
        return ctaStateAfterClaim
    }

    fun claim(catalogId: Long?) {
        launchCatchError(block = {
            if (userSession?.isLoggedIn == false) {
                _showErrorClaimCoupon.value = ClaimFailure.Unauthorized

                return@launchCatchError
            }

            val response = claimCouponClickUseCase?.redeemCoupon(catalogId.orZero())
            val ctaList = response?.hachikoRedeem?.ctaList
            ctaStateAfterClaim.postValue(ctaList.mapToCtaState())
        }, onError = {
            _showErrorClaimCoupon.value = ClaimFailure.Ineligible(it.message.orEmpty())
            Timber.e(it)
        })
    }
}
