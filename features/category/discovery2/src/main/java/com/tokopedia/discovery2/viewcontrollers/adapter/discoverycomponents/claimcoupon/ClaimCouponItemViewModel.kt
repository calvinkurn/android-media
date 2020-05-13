package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ClaimCouponConstant
import com.tokopedia.discovery2.ClaimCouponConstant.Companion.DOUBLE_COLUMNS
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ClaimCouponItemViewModel(val application: Application, private val components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    private val claimStatus = MutableLiveData<String>()
    private val couponCode = MutableLiveData<String>()
    private val componentData = MutableLiveData<DataItem>()

    @Inject
    lateinit var claimCouponClickUseCase: ClaimCouponClickUseCase
    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        initDaggerInject()
    }

    fun getComponentData(): LiveData<DataItem> {
        componentData.value = components.data?.get(0)
        return componentData
    }

    fun getIsDouble(): Boolean {
        return components.properties?.columns?.equals(DOUBLE_COLUMNS) ?: false
    }

    fun getClaimStatus(): LiveData<String> {
        val status = checkClaimStatus(components.data?.getOrElse(0) { DataItem() })
        claimStatus.value = status
        return claimStatus
    }

    fun getRedeemCouponCode(): LiveData<String> {
        return couponCode
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun redeemCoupon() {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                if (userSession.isLoggedIn) {
                    val data = claimCouponClickUseCase.redeemCoupon(getQueryMap())
                    couponCode.postValue(data.hachikoRedeem?.coupons?.get(0)?.code)
                } else {
                    couponCode.postValue(ClaimCouponConstant.NOT_LOGEDIN)
                }
            }


        }, onError = {
            it.printStackTrace()
        })
    }


    private fun checkClaimStatus(item: DataItem?): String {
        var status = if (item?.couponCode.isNullOrEmpty()) ClaimCouponConstant.CLAIMED else ClaimCouponConstant.UNCLAIMED
        if (item?.isDisabled == true || item?.isDisabledBtn == true) {
            status = ClaimCouponConstant.OUT_OF_STOCK
        }
        return status
    }

    fun setClick(context: Context, status: String?) {
        var applink = ""
        if (status == ClaimCouponConstant.UNCLAIMED) {
            applink = GenerateUrl.getClaimCoupon(components.data?.get(0)?.couponCode ?: "")
        } else if (status == ClaimCouponConstant.CLAIMED || status == ClaimCouponConstant.OUT_OF_STOCK) {
            applink = GenerateUrl.getClaimCoupon(components.data?.get(0)?.slug ?: "")
        }
        navigate(context, applink)
    }

    fun navigate(context: Context, applink: String) {
        if (applink.isNotEmpty()) {
            RouteManager.route(context, applink)
        }
    }

    private fun getQueryMap(): Map<String, Any> {
        return mapOf("catalogId" to (try {
            components.data?.get(0)?.claimCouponid?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            0
        }),
                "isGift" to 0,
                "giftUserId" to 0,
                "giftEmail" to "",
                "notes" to "")
    }

}