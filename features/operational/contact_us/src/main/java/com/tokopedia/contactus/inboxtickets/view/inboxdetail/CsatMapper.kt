package com.tokopedia.contactus.inboxtickets.view.inboxdetail

import com.tokopedia.csat_rating.dynamiccsat.data.model.DynamicCsat
import com.tokopedia.csat_rating.dynamiccsat.domain.model.CsatModel
import com.tokopedia.csat_rating.dynamiccsat.domain.model.PointModel
import com.tokopedia.kotlin.extensions.view.orZero

object CsatMapper {

    fun mapCsatModel(ticketId: String, response: DynamicCsat): CsatModel {
        return CsatModel(
            caseId = ticketId,
            title = response.title,
            service = response.service,
            points = response.points.map { point ->
                PointModel(
                    score = point.score,
                    caption = point.caption,
                    reasonTitle = point.reasonTitle,
                    otherReasonTitle = point.otherReasonTitle,
                    reasons = point.reasons
                )
            }.toMutableList(),
            minimumOtherReasonChar = response.minimumOtherReasonChar.orZero()
        )
    }
}
