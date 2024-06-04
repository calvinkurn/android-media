package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

/**
 * Created by frenzel on 11/03/23
 */
interface RecommendationVisitable : Visitable<RecommendationTypeFactory> {
    val metadata: RecommendationWidgetMetadata
    val trackingModel: RecommendationWidgetTrackingModel
    val userId: String
    val appLog: RecommendationAppLog
    val appLogAdditionalParam: AppLogAdditionalParam
    companion object {
        fun create(
            metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
            trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
            userId: String = "",
            appLog: RecommendationAppLog = RecommendationAppLog(),
            appLogAdditionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
        ): RecommendationVisitable {
            return object : RecommendationVisitable {
                override val metadata: RecommendationWidgetMetadata
                    get() = metadata
                override val trackingModel: RecommendationWidgetTrackingModel
                    get() = trackingModel
                override val userId: String
                    get() = userId
                override val appLog: RecommendationAppLog
                    get() = appLog
                override val appLogAdditionalParam: AppLogAdditionalParam
                    get() = appLogAdditionalParam

                override fun type(typeFactory: RecommendationTypeFactory?): Int {
                    return 0
                }
            }
        }
    }
}
