package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.data.mapper.TokomemberMapper
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.tokomember.usecase.TokomemberUsecase
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GyroEngineMapperUseCase @Inject constructor(
    private val tokomemberUsecase: TokomemberUsecase,
    ) : UseCase<GyroRecommendation>() {
    private lateinit var featureEngineData: FeatureEngineData
    private lateinit var queryParamTokomember: Pair<Int, Float>


    fun getFeatureListData(
        featureEngineData: FeatureEngineData?,
        queryParamTokomember: Pair<Int, Float>?,
        onSuccess: (GyroRecommendation) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.featureEngineData = featureEngineData ?: return
        if (queryParamTokomember != null) {
            this.queryParamTokomember = queryParamTokomember
        }
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
            tokomemberUsecase.setGqlParams(queryParamTokomember)
            val gyroTokomemberData = async {
                tokomemberUsecase.executeOnBackground()
            }

            val tokoMemberData = TokomemberMapper.getGyroTokomemberItem(gyroTokomemberData.await())
            val gyroRecommendationListItem = gyroRecommendationList.await()
            gyroRecommendationListItem?.gyroVisitable?.addAll(listOf(tokoMemberData.listOfTokomemberItem[0]))
            gyroRecommendationListItem?.gyroTokomemberBottomSheet =
                tokoMemberData.listOfBottomSheetContent
            gyroRecommendationListItem?.gyroMembershipSuccessWidget =
                tokoMemberData.listOfTokomemberItem[1]
            gyroRecommendationListItem?: GyroRecommendation("","", ArrayList())
        }
    }
}



