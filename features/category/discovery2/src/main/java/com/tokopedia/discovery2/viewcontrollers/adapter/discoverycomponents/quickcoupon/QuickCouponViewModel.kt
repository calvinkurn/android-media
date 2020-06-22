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
    private val userLoggedInLiveData: MutableLiveData<Boolean?> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()
    private val phoneVerificationStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val couponApplicableStatus: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
        componentPosition.value = position
    }

    fun getCouponStatus() = couponAppliedStatus
    fun isUserLoggedIn() = userLoggedInLiveData
    fun getComponentPosition() = componentPosition
    fun getPhoneVerificationStatus() = phoneVerificationStatus
    fun getQuickCouponData() = clickCouponLiveData
    fun getCouponApplicableStatus() = couponApplicableStatus

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchCouponDetailData()
    }

    private fun fetchCouponDetailData() {
        launchCatchError(block = {
            quickCouponUseCase.getCouponDetail(components.pageEndPoint).clickCouponData?.let {
                clickCouponLiveData.value = it
                checkComponentVisibility()

            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun checkComponentVisibility() {
        val couponApplicable = clickCouponLiveData.value?.isApplicable

        couponApplicable?.let {
            if(it){
                updateCouponAppliedStatus()
            }
            couponApplicableStatus.value = it
        }
    }

    private fun updateCouponAppliedStatus() {
        couponAppliedStatus.value = clickCouponLiveData.value?.couponApplied == true
    }


    fun getCouponTitle(): String? = if (couponAppliedStatus.value == true) clickCouponLiveData.value?.messageUsingSuccess else clickCouponLiveData.value?.catalogTitle

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun onClaimCouponClick() {
        userLoggedInLiveData.value = UserSession(application).isLoggedIn
    }

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

    override fun componentAction() {
        super.componentAction()
        applyQuickCoupon()
    }

    private fun applyQuickCoupon() {
        clickCouponLiveData.value?.realCode?.let { promoCode ->
            launchCatchError(block = {
                quickCouponUseCase.applyQuickCoupon(promoCode).applyCouponData?.let { applyCouponData ->
                    applyCouponData.success?.let {
                        couponAppliedStatus.value = it
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
}