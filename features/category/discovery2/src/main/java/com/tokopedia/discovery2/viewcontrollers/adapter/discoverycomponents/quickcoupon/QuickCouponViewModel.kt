package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class QuickCouponViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val clickCouponLiveData: MutableLiveData<ClickCouponData> = MutableLiveData()
    private val couponAppliedStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()
    private val phoneVerificationStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val couponVisibilityStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val couponAdded: MutableLiveData<Boolean> = MutableLiveData()
    private val loggedInStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getCouponStatus() = couponAppliedStatus
    fun getComponentPosition() = componentPosition
    fun getPhoneVerificationStatus() = phoneVerificationStatus
    fun getCouponVisibilityStatus() = couponVisibilityStatus
    fun getCouponAddedStatus() = couponAdded
    fun getLoggedInStatusLiveData() = loggedInStatusLiveData
    fun getComponentData() = components

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentPosition.value = position
        fetchCouponDetailData()
    }

    fun loggedInStatus() {
        loggedInStatusLiveData.value = UserSession(application).isLoggedIn
    }

    private fun fetchCouponDetailData() {
        launchCatchError(block = {
            quickCouponUseCase.getCouponDetail(components.pagePath).clickCouponData?.let {
                clickCouponLiveData.value = it
                checkComponentVisibility()
                loggedInStatus()
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun checkComponentVisibility() {
        clickCouponLiveData.value?.isApplicable?.let {
            if (it) {
                updateCouponAppliedStatus()
            }
            components.isApplicable = it
            couponVisibilityStatus.value = it
        }
    }

    private fun updateCouponAppliedStatus() {
        couponAppliedStatus.value = clickCouponLiveData.value?.couponApplied == true
    }

    override fun loggedInCallback() {
        val isLoggedIn = UserSession(application).isLoggedIn
        if(isLoggedIn){
            fetchCouponDetailData()
        }else{
            components.couponDetailClicked = false
            components.couponAppliedClicked = false
        }
    }

    fun getCouponTitle(): String? = if (couponAppliedStatus.value == true) clickCouponLiveData.value?.messageUsingSuccess else clickCouponLiveData.value?.catalogTitle

    fun checkMobileVerificationStatus() {
        launchCatchError(
                block = {
                    quickCouponUseCase.getMobileVerificationStatus().verificationStatus?.let {
                        it.phoneVerified?.let { status ->
                            phoneVerificationStatus.value = status
                        }
                    }
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    override fun isPhoneVerificationSuccess(phoneVerifyStatus: Boolean) {
        if(phoneVerifyStatus){
            applyQuickCoupon()
        }
    }

    fun applyQuickCoupon() {
        clickCouponLiveData.value?.realCode?.let { realCode ->
            launchCatchError(block = {
                quickCouponUseCase.applyQuickCoupon(realCode).applyCouponData?.let { applyCouponData ->
                    applyCouponData.success?.let {
                        couponAppliedStatus.value = it
                        couponAdded.value = it
                    }
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    fun getCouponApplink(): String {
        clickCouponLiveData.value?.couponAppLink?.let {
            return it
        }
        return ""
    }

    fun getCouponAddedFailMessage(): String {
        clickCouponLiveData.value?.messageUsingFailed?.let {
            return it
        }
        return ""
    }

    fun getCouponDetail(): ClickCouponData? {
        clickCouponLiveData.value?.componentPosition = position
        clickCouponLiveData.value?.componentID = components.id
        clickCouponLiveData.value?.componentName = components.name
        return clickCouponLiveData.value
    }

    fun getCouponAppliedStatus(): Boolean? {
        return clickCouponLiveData.value?.couponApplied
    }

    fun getCouponApplicableStatus(): Boolean? {
        return clickCouponLiveData.value?.isApplicable
    }
}