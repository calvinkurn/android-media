package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GyroEngineMapperUseCase @Inject constructor() : UseCase<GyroRecommendation>() {
    private lateinit var featureEngineData: FeatureEngineData

    fun getFeatureListData(
        featureEngineData: FeatureEngineData?,
        onSuccess: (GyroRecommendation) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.featureEngineData = featureEngineData ?: return
        execute({
            if (!it.gyroVisitable.isNullOrEmpty())
                onSuccess(it)
        }, { onError(it) })
    }

    override suspend fun executeOnBackground(): GyroRecommendation {
        val gyroRecommendation = FeatureRecommendationMapper.getFeatureList(featureEngineData)
        gyroRecommendation?.let { return it }
        return GyroRecommendation("", "", arrayListOf())
    }

}



