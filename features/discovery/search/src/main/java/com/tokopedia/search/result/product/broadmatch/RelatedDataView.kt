package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.search.result.domain.model.SearchProductModel

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
                )
            }
        )
    }
}
