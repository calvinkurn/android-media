package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLOSE_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.OPEN_MEMBERSHIP
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.data.mapper.TokomemberMapper.getMembershipType
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.tokomember.model.MembershipShopResponse
import com.tokopedia.tokomember.usecase.TokomemberUsecase
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GyroEngineMapperUseCase @Inject constructor(
    private val tokomemberUsecase: TokomemberUsecase,
    ) : UseCase<GyroRecommendation>() {
    private lateinit var featureEngineData: FeatureEngineData
    private var queryParamTokomember: TokoMemberRequestParam? = null
    private var deferredTokomemberData: Deferred<MembershipShopResponse>? = null
    private var tokomemberModel: TokomemberModel ? = null

    fun getFeatureListData(
        featureEngineData: FeatureEngineData?,
        queryParamTokomember: TokoMemberRequestParam?,
        onSuccess: (GyroRecommendation) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.featureEngineData = featureEngineData ?: return
        this.queryParamTokomember = queryParamTokomember
        execute({
            if (!it.gyroVisitable.isNullOrEmpty())
                onSuccess(it)
        }, { onError(it) })
    }

    override suspend fun executeOnBackground(): GyroRecommendation {

        return withContext(coroutineContext) {
            val gyroRecommendationList = async {
                FeatureRecommendationMapper.getFeatureList(featureEngineData)
            }
            var gyroRecommendationListItem = gyroRecommendationList.await()
            if (gyroRecommendationListItem == null) {
                gyroRecommendationListItem = GyroRecommendation("", "", ArrayList())
            }
            setTokomemberData(getTokomemberData() , gyroRecommendationListItem)
            gyroRecommendationListItem
        }
    }

    private fun setTokomemberData(tokomemberData: List<GyroTokomemberItem>?, gyroRecommendation: GyroRecommendation?) {
        if (tokomemberData?.isNotEmpty() == true &&
            (tokomemberData.getOrNull(TOKOMEMBER_WAITING_WIDGET)?.isShown == true ||
                    tokomemberData.getOrNull(TOKOMEMBER_SUCCESS_WIDGET)?.isShown == true)
        ) {
            if (queryParamTokomember?.isFirstElement == true) {
                gyroRecommendation?.apply {
                    gyroVisitable.add(
                        0, tokomemberData.getOrNull(
                            TOKOMEMBER_WAITING_WIDGET
                        ) ?: GyroTokomemberItem()
                    )
                    title = queryParamTokomember?.sectionTitle ?: ""
                    description = queryParamTokomember?.sectionSubtitle ?: ""
                }
            } else {
                gyroRecommendation?.gyroVisitable?.add(
                    tokomemberData.getOrNull(
                        TOKOMEMBER_WAITING_WIDGET
                    ) ?: GyroTokomemberItem()
                )
            }
            when (getMembershipType()) {
                CLOSE_MEMBERSHIP -> gyroRecommendation?.gyroMembershipSuccessWidget =
                    tokomemberData.getOrNull(TOKOMEMBER_WAITING_WIDGET)
                        ?: GyroTokomemberItem()
                OPEN_MEMBERSHIP -> {
                    gyroRecommendation?.gyroMembershipSuccessWidget =
                        tokomemberData.getOrNull(TOKOMEMBER_SUCCESS_WIDGET)
                            ?: GyroTokomemberItem()
                }
            }
        }
    }

    private suspend fun getTokomemberData() : List<GyroTokomemberItem>? {
        queryParamTokomember?.let { tokomemberRequestParam->
            tokomemberUsecase.setGqlParams(tokomemberRequestParam.orderData)
            deferredTokomemberData = fetchTokomemberData()
            tokomemberModel = deferredTokomemberData?.await()?.let { it ->
                if (!it.membershipGetShopRegistrationWidget?.widgetContent.isNullOrEmpty()) {
                    TokomemberMapper.getGyroTokomemberItem(
                        it.membershipGetShopRegistrationWidget,
                        tokomemberRequestParam
                    )
                } else {
                    TokomemberModel()
                }
            }
        }
       return tokomemberModel?.listOfTokomemberItem
    }

    suspend fun fetchTokomemberData(): Deferred<MembershipShopResponse> {
        return withContext(coroutineContext) {
            async {
                tokomemberUsecase.executeOnBackground()
            }
        }
    }
}



