package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.datamapper.AutomateCouponMapper.mapToCtaState
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.usecase.automatecoupon.GetAutomateCouponUseCase
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

class SingleAutomateCouponViewModel(
    val application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()
    private val ctaStateAfterClaim = MutableLiveData<AutomateCouponCtaState>()

    private val _showErrorClaimCoupon: SingleLiveEvent<String> = SingleLiveEvent()
    fun shouldShowErrorClaimCouponToaster(): LiveData<String> = _showErrorClaimCoupon

    @JvmField
    @Inject
    var useCase: GetAutomateCouponUseCase? = null

    @JvmField
    @Inject
    var userSession: UserSessionInterface? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    @JvmField
    @Inject
    var claimCouponClickUseCase: ClaimCouponClickUseCase? = null

    fun getComponentList(): LiveData<ArrayList<ComponentsItem>> {
        return componentList
    }

    fun getCTAState(): LiveData<AutomateCouponCtaState> {
        return ctaStateAfterClaim
    }

    fun claim(catalogId: Long?) {
        launchCatchError(block = {
            if (userSession?.isLoggedIn == false) {
                _showErrorClaimCoupon.value = NEED_LOGIN_MESSAGE

                return@launchCatchError
            }

            val response = claimCouponClickUseCase?.redeemCoupon(catalogId.orZero())
            val ctaList = response?.hachikoRedeem?.ctaList
            ctaStateAfterClaim.postValue(ctaList.mapToCtaState())
        }, onError = {
                _showErrorClaimCoupon.value = it.message.orEmpty()
                Timber.e(it)
            })
    }

    fun fetch(isDarkMode: Boolean) {
        launchCatchError(block = {
            val state = useCase?.execute(component.id, component.pageEndPoint, isDarkMode)
            if (state == GetAutomateCouponUseCase.State.LOADED) {
                if (component.getComponentsItem()?.isNotEmpty() == true) {
                    componentList.postValue(component.getComponentsItem() as ArrayList<ComponentsItem>)
                }
            }
        }, onError = {
                Timber.e(it)
            })
    }

    companion object {
        private const val NEED_LOGIN_MESSAGE = "Silakan login terlebih dahulu"
    }
}
