package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TokomemberModel
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
    private var queryParamTokomember: Triple<Int, Float , PageType?>? = null
    private var deferredTokomemberData: Deferred<MembershipShopResponse>? = null
    private var tokomemberModel: TokomemberModel ? = null

    fun getFeatureListData(
        featureEngineData: FeatureEngineData?,
        queryParamTokomember: Triple<Int, Float, PageType?>,
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
            val gyroRecommendationListItem = gyroRecommendationList.await()
            setTokomemberData(gyroRecommendationListItem)
            gyroRecommendationListItem ?: GyroRecommendation("", "", ArrayList())
        }
    }

    private fun setTokomemberData(gyroRecommendationListItem: GyroRecommendation?) {
        tokomemberModel?.also {
            when (queryParamTokomember?.third) {
                is WaitingPaymentPage -> gyroRecommendationListItem?.gyroVisitable?.addAll(
                    listOf(it.listOfTokomemberItem[0])
                )
                is InstantPaymentPage -> gyroRecommendationListItem?.gyroVisitable?.addAll(
                    listOf(it.listOfTokomemberItem[1])
                )
                else -> {
                }
            }
            gyroRecommendationListItem?.gyroMembershipSuccessWidget = it.listOfTokomemberItem[2]
        }
    }

    suspend fun getTokomemberData() {
        queryParamTokomember?.let {
            tokomemberUsecase.setGqlParams(it)
            deferredTokomemberData = fetchTokomemberData()
            tokomemberModel = deferredTokomemberData?.await()?.let { it1 ->
                TokomemberMapper.getGyroTokomemberItem(
                    it1.membershipGetShopRegistrationWidget
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



