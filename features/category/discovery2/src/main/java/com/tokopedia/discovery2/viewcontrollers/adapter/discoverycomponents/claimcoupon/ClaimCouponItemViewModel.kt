package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.CLAIMED
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.OUT_OF_STOCK
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.UNCLAIMED
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val CATALOG_ID = "catalogId"
private const val IS_GIFT = "isGift"
private const val GIFT_USER_ID = "giftUserId"
private const val GIFT_EMAIL = "giftEmail"
private const val NOTES = "notes"

class ClaimCouponItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

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
        val status = checkClaimStatus(components.data?.getOrElse(0) { DataItem() })
        components.data?.get(0)?.status = status
        componentData.value = components.data?.get(0)
        return componentData
    }

    fun getIsDouble(): Boolean {
        return components.properties?.columns?.equals(DOUBLE_COLUMNS) ?: false
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
            if (userSession.isLoggedIn) {
                val data = claimCouponClickUseCase.redeemCoupon(getQueryMap())
                couponCode.postValue(data.hachikoRedeem?.coupons?.get(0)?.code)
            } else {
                couponCode.postValue(NOT_LOGGEDIN)
            }
        }, onError = {
            it.printStackTrace()
        })
    }


    private fun checkClaimStatus(item: DataItem?): String {
        var status = if (item?.couponCode.isNullOrEmpty()) CLAIMED else UNCLAIMED
        if (item?.isDisabled == true || item?.isDisabledBtn == true) {
            status = OUT_OF_STOCK
        }
        return status
    }

    fun setClick(context: Context, status: String?) {
        var applink = ""
        if (status == UNCLAIMED) {
            applink = GenerateUrl.getClaimCoupon(components.data?.get(0)?.couponCode ?: "")
        } else if (status == CLAIMED || status == OUT_OF_STOCK) {
            applink = GenerateUrl.getClaimCoupon(components.data?.get(0)?.slug ?: "")
        }
        navigate(context, applink)
    }

    private fun getQueryMap(): Map<String, Any> {
        return mapOf(CATALOG_ID to (try {
            components.data?.get(0)?.id?.toIntOrZero() ?: 0
        } catch (e: NumberFormatException) {
            0
        }),
                IS_GIFT to 0,
                GIFT_USER_ID to 0,
                GIFT_EMAIL to "",
                NOTES to "")
    }

}