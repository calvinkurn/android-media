package com.tokopedia.tokopoints.view.tokopointhome

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionViewModel
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.util.*
import javax.inject.Inject

@TokoPointScope
class TokoPointsHomeViewModel @Inject constructor(private val repository: TokopointsHomeRepository) : CatalogPurchaseRedeemptionViewModel(repository), TokoPointsHomeContract.Presenter {
    var pagerSelectedItem = 0


    val tokopointDetailLiveData = MutableLiveData<Resources<TokopointSuccess>>()
    val tokoenDetailLiveData = MutableLiveData<LuckyEggEntity>()
    val couponCountLiveData = MutableLiveData<TokoPointSumCoupon>()

    override fun getTokoPointDetail() {
        launchCatchError(block = {
         tokopointDetailLiveData.value = Loading()
            val graphqlResponse = repository.getTokoPointDetailData()
            val data = graphqlResponse.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
            val dataSection = graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
            if (data != null && dataSection != null && dataSection.sectionContent != null) {
                tokopointDetailLiveData.value = Success(TokopointSuccess(data.tokoPoints,dataSection.sectionContent.sectionContent))
            }
            //handling for lucky egg data
            val tokenDetail = graphqlResponse.getData<TokenDetailOuter>(TokenDetailOuter::class.java)
            if (tokenDetail != null && tokenDetail.tokenDetail != null && tokenDetail.tokenDetail.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                tokoenDetailLiveData.value = tokenDetail.tokenDetail
            }
        }){
            tokopointDetailLiveData.value = ErrorMessage(it.localizedMessage)
        }
    }

    override fun tokopointOnboarding2020(view : TokoPointsHomeContract.View) {
        TokoPointsNotificationManager.fetchNotification(view.activityContext, "onboarding", Tokopoint2020Subscriber(view))
    }


    //Handling sum token
    val couponCount: Unit
        get() {
            launchCatchError(block = {
                val couponOuter = repository.getCouponCountData()
                if ( couponOuter.tokopointsSumCoupon != null) {
                    couponCountLiveData.value = couponOuter.tokopointsSumCoupon
                }
            }){}
        }
}

data class TokopointSuccess(val tokoPointEntity: TokoPointEntity,val sectionList: MutableList<SectionContent>)