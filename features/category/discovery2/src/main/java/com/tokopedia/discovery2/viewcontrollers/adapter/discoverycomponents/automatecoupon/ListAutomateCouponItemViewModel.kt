package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ListAutomateCouponItemViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val models = MutableLiveData<AutomateCouponUiModel>()
    private val redirectionAfterClaim = MutableLiveData<String>()

    private val _showErrorClaimCoupon: SingleLiveEvent<String> = SingleLiveEvent()
    fun shouldShowErrorClaimCouponToaster(): LiveData<String> = _showErrorClaimCoupon

    @JvmField
    @Inject
    var userSession: UserSessionInterface? = null

    @JvmField
    @Inject
    var claimCouponClickUseCase: ClaimCouponClickUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()

        component.automateCoupons?.firstOrNull()?.let {
            models.value = it
        }
    }

    fun getCouponModel(): LiveData<AutomateCouponUiModel> = models

    fun claim() {
        launchCatchError(block = {
            if (userSession?.isLoggedIn == true) {
                val catalogId = component.data?.firstOrNull()?.catalogIds?.firstOrNull()?.toLongOrZero()
                val response = claimCouponClickUseCase?.redeemCoupon(catalogId.orZero())
                val ctaList = response?.hachikoRedeem?.ctaList

                if (ctaList?.isNotEmpty() == true) {
                    ctaList.first().metadata?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            val appLink = jsonObject.getString("app_link")
                            if (appLink.isNotEmpty()) {
                                redirectionAfterClaim.postValue(appLink)
                            }
                        } catch (e: JSONException) {
                            Timber.e(e)
                        }
                    }
                }
            }
        }, onError = {
            _showErrorClaimCoupon.value = it.message.orEmpty()
            Timber.e(it)
        })
    }
}
