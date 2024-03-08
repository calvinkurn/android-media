package com.tokopedia.csat_rating.dynamiccsat.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CsatModel(
    var caseId: String = "",
    var caseChatId: String = "",
    var title: String = "",
    var service: String = "",
    var points: MutableList<PointModel> = mutableListOf(),
    var selectedPoint: PointModel = PointModel(),
    var selectedReasons: MutableList<String> = mutableListOf(),
    var otherReason: String = "",
    var minimumOtherReasonChar: Int = 0
) : Parcelable

@Parcelize
data class PointModel(
    var score: Int = 0,
    var caption: String = "",
    var reasonTitle: String = "",
    var reasons: List<String> = emptyList(),
    var otherReasonTitle: String = ""
) : Parcelable
