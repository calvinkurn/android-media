package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData

data class RelatedDataView(
        val relatedKeyword: String = "",
        val position: Int = 0,
        val broadMatchDataViewList: List<BroadMatchDataView> = listOf()
) {

    companion object {

        fun create(
            related: SearchProductModel.Related,
            isLocalSearch: Boolean,
            dimension90: String,
            actualKeyword: String,
            externalReference: String,
            byteIOTrackingData: ByteIOTrackingData,
        ) = RelatedDataView(
            related.relatedKeyword,
            related.position,
            related.otherRelatedList.map {
                BroadMatchDataView.create(
                    it,
                    isLocalSearch,
                    dimension90,
                    related.trackingOption,
                    actualKeyword,
                    externalReference,
                    byteIOTrackingData,
                )
            }
        )

        fun create(
            related: SearchProductV5.Data.Related,
            isLocalSearch: Boolean,
            dimension90: String,
            actualKeyword: String,
            externalReference: String,
            byteIOTrackingData: ByteIOTrackingData,
        ) = RelatedDataView(
            related.relatedKeyword,
            related.position,
            related.otherRelatedList.map {
                BroadMatchDataView.create(
                    it,
                    isLocalSearch,
                    dimension90,
                    related.trackingOption,
                    actualKeyword,
                    externalReference,
                    byteIOTrackingData,
                )
            }
        )
    }
}
