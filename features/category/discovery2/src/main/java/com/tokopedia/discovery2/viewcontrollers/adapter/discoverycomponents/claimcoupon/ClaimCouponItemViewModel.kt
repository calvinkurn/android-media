package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.HABIS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
    private val componentData = MutableLiveData<DataItem>()

    @Inject
    lateinit var claimCouponClickUseCase: ClaimCouponClickUseCase

    @Inject
    lateinit var userSession: UserSessionInterface



    fun getComponentData(): LiveData<DataItem> {
        val status = getClaimStatus(components.data?.getOrElse(0) { DataItem() })
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


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun redeemCoupon(showToaster: (message : String) -> Unit) {
        launchCatchError(block = {
            if (userSession.isLoggedIn) {
                val data = claimCouponClickUseCase.redeemCoupon(getQueryMap())
                couponCode.postValue(data.hachikoRedeem?.coupons?.get(0)?.code)
            } else {
                couponCode.postValue(NOT_LOGGEDIN)
            }
        }, onError = {
            if(it is MessageErrorException){
                if(!it.message.isNullOrEmpty()){
                    showToaster.invoke(it.message!!)
                }
            }else{
                showToaster.invoke(application.applicationContext.resources.getString(R.string.error_message))
            }
            it.printStackTrace()
        })
    }


    private fun getClaimStatus(item: DataItem?): String {
        item?.let {
            return it.claimButtonStr ?: HABIS
        }
        return HABIS
    }

    fun setClick(context: Context, status: String?) {
        val applink = GenerateUrl.getClaimCouponApplink(components.data?.get(0)?.slug ?: "")
        navigate(context, applink)
    }

    fun getCouponSlug(): String? {
        if (components.data.isNullOrEmpty()) return ""

        return components.data?.get(0)?.slug
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