package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.HABIS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
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
    private val componentData = MutableLiveData<ComponentsItem>()

    @JvmField
    @Inject
    var claimCouponClickUseCase: ClaimCouponClickUseCase? = null

    @JvmField
    @Inject
    var userSession: UserSessionInterface? = null

    init {
        componentData.value = components
    }

    fun getComponentData(): LiveData<ComponentsItem> = componentData

    fun getClaimCouponData(): CatalogWithCouponList? {
        val status = getClaimStatus(components.claimCouponList?.getOrElse(0) { CatalogWithCouponList() })
        components.claimCouponList?.get(0)?.status = status
        return components.claimCouponList?.firstOrNull()
    }

    fun getIsDouble(): Boolean {
        return components.properties?.columns?.equals(DOUBLE_COLUMNS) ?: false
    }

    fun getRedeemCouponCode(): LiveData<String> {
        return couponCode
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun redeemCoupon(showToaster: (message: String) -> Unit) {
        launchCatchError(block = {
            if (userSession?.isLoggedIn == true) {
                val data = claimCouponClickUseCase?.redeemCoupon(getQueryMap())
                if (!data?.hachikoRedeem?.coupons?.firstOrNull()?.appLink.isNullOrEmpty()) {
                    components.data?.firstOrNull()?.applinks = data?.hachikoRedeem?.coupons?.firstOrNull()?.appLink
                }
                couponCode.postValue(data?.hachikoRedeem?.coupons?.get(0)?.code ?: "")
            } else {
                couponCode.postValue(NOT_LOGGEDIN)
            }
        }, onError = {
                if (it is MessageErrorException) {
                    if (!it.message.isNullOrEmpty()) {
                        showToaster.invoke(it.message!!)
                    }
                } else {
                    showToaster.invoke(application.applicationContext.resources.getString(R.string.error_message))
                }
                it.printStackTrace()
            })
    }

    private fun getClaimStatus(item: CatalogWithCouponList?): String {
        item?.let {
            return it.buttonStr ?: HABIS
        }
        return HABIS
    }

    fun setClick(context: Context, status: String?) {
        navigate(context, getCouponAppLink() ?: "")
    }

    fun getCouponAppLink(): String? {
        if (components.claimCouponList.isNullOrEmpty()) return ""

        return components.claimCouponList?.get(0)?.appLink
    }

    private fun getQueryMap(): Map<String, Any> {
        return mapOf(
            CATALOG_ID to (
                try {
                    components.claimCouponList?.firstOrNull()?.id ?: 0
                } catch (e: NumberFormatException) {
                    0
                }
                ),
            IS_GIFT to 0,
            GIFT_USER_ID to 0,
            GIFT_EMAIL to "",
            NOTES to ""
        )
    }
}
