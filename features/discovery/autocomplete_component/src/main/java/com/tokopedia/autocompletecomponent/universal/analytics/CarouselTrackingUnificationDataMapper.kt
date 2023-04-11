package com.tokopedia.autocompletecomponent.universal.analytics

import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselTrackingUnification
import com.tokopedia.discovery.common.model.SearchParameter

object CarouselTrackingUnificationDataMapper {
    private val SearchParameter?.queryKey: String
        get() {
            val searchParameter = this ?: return ""
            return searchParameter.getSearchQuery()
        }

    fun createCarouselTrackingUnificationData(
        product: CarouselDataView.Product,
        searchParameter: SearchParameter?
    ): CarouselTrackingUnification.Data {
        return CarouselTrackingUnification.Data(
            searchParameter.queryKey,
            product,
        )
    }
}