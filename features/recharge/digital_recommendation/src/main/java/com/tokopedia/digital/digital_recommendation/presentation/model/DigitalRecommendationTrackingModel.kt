package com.tokopedia.digital.digital_recommendation.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by furqan on 20/09/2021
 */
@Parcelize
class DigitalRecommendationTrackingModel(
        val typeName: String = "",
        val businessUnit: String = "",
        val categoryId: String = "",
        val categoryName: String = "",
        val itemLabel: String = "",
        val itemType: String = "",
        val operatorId: String = "",
        val productId: String = ""
) : Parcelable