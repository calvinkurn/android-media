package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.usecase.automatecoupon.GetAutomateCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SingleAutomateCouponViewModel(
    val application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentList = MutableLiveData<ArrayList<ComponentsItem>>()
    private val redirectionAfterClaim = MutableLiveData<String>()

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

    fun getRedirectLink(): LiveData<String> {
        return redirectionAfterClaim
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetch()
    }

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
                Timber.e(it)
            })
    }

    private fun fetch() {
        launchCatchError(block = {
            val state = useCase?.execute(component.id, component.pageEndPoint)
            if (state == GetAutomateCouponUseCase.State.LOADED) {
                componentList.postValue(component.getComponentsItem() as ArrayList<ComponentsItem>)
            }
        }, onError = {
                Timber.e(it)
            })
    }
}
