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
            getTokomemberData()
            var gyroRecommendationListItem = gyroRecommendationList.await()
            if (gyroRecommendationListItem == null) {
                gyroRecommendationListItem = GyroRecommendation("", "", ArrayList())
            }
            setTokomemberData(gyroRecommendationListItem)
            gyroRecommendationListItem
        }
    }

    /** When Tokomember is on first index of engine data make Tokomember first item in list and update title and subtitle
    of section **/

    private fun setTokomemberData(gyroRecommendationListItem: GyroRecommendation?) {
        tokomemberModel?.also { tokomemberModel ->

            if (tokomemberModel.listOfTokomemberItem.isNotEmpty() &&
                ( tokomemberModel.listOfTokomemberItem.getOrNull(TOKOMEMBER_WAITING_WIDGET)?.isShown == true ||
                tokomemberModel.listOfTokomemberItem.getOrNull(TOKOMEMBER_SUCCESS_WIDGET)?.isShown == true )) {
                if (queryParamTokomember?.isFirstElement == true) {
                    gyroRecommendationListItem?.gyroVisitable?.add(
                        0,
                        tokomemberModel.listOfTokomemberItem.getOrNull(
                            TOKOMEMBER_WAITING_WIDGET
                        ) ?: GyroTokomemberItem()
                    )
                    gyroRecommendationListItem?.title = queryParamTokomember?.sectionTitle ?: ""
                    gyroRecommendationListItem?.description =
                        queryParamTokomember?.sectionSubtitle ?: ""
                } else {
                    gyroRecommendationListItem?.gyroVisitable?.add(
                        tokomemberModel.listOfTokomemberItem.getOrNull(
                            TOKOMEMBER_WAITING_WIDGET
                        ) ?: GyroTokomemberItem()
                    )
                }

                when (getMembershipType()) {
                    CLOSE_MEMBERSHIP -> gyroRecommendationListItem?.gyroMembershipSuccessWidget =
                        tokomemberModel.listOfTokomemberItem.getOrNull(TOKOMEMBER_WAITING_WIDGET)
                            ?: GyroTokomemberItem()
                    OPEN_MEMBERSHIP -> {
                        gyroRecommendationListItem?.gyroMembershipSuccessWidget =
                            tokomemberModel.listOfTokomemberItem.getOrNull(TOKOMEMBER_SUCCESS_WIDGET)
                                ?: GyroTokomemberItem()
                    }
                }
            }
        }
    }

    private suspend fun getTokomemberData() {
        queryParamTokomember?.let { tokomemberRequestParam->
            tokomemberUsecase.setGqlParams(tokomemberRequestParam.orderData)
            deferredTokomemberData = fetchTokomemberData()
            tokomemberModel = deferredTokomemberData?.await()?.let { it ->
                TokomemberMapper.getGyroTokomemberItem(
                    it.membershipGetShopRegistrationWidget ,
                    tokomemberRequestParam
                )
            }
        }
    }

    suspend fun fetchTokomemberData(): Deferred<MembershipShopResponse> {
        return withContext(coroutineContext) {
            async {
                tokomemberUsecase.executeOnBackground()
            }
        }
    }
}



