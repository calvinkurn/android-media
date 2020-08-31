package com.tokopedia.flight.cancellation.domain.mapper

import com.tokopedia.flight.cancellation.data.cloud.entity.Reason
import com.tokopedia.flight.cancellation.data.cloud.entity.ReasonRequiredDocs
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonModel
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonRequiredDocsModel
import javax.inject.Inject

/**
 * @author by furqan on 26/10/18.
 */
class FlightCancellationReasonsViewModelMapper @Inject constructor() {

    fun transform(reasonList: List<Reason>?): List<FlightCancellationReasonModel> {
        val data = ArrayList<FlightCancellationReasonModel>()
        if (reasonList != null) {
            for (reason in reasonList) {
                data.add(transform(reason))
            }
        }
        return data
    }

    fun transform(reason: Reason): FlightCancellationReasonModel =
            FlightCancellationReasonModel(reason.id,
                    reason.title, transformRequiredDocs(reason.requiredDocs))

    private fun transformRequiredDocs(reasonRequiredDocs: List<ReasonRequiredDocs>): List<FlightCancellationReasonRequiredDocsModel> =
            reasonRequiredDocs.map {
                FlightCancellationReasonRequiredDocsModel(it.docId, it.title)
            }.toList()
}