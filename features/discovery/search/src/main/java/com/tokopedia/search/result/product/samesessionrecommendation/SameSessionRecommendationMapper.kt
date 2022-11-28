package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_SAME_SESSION_RECOMMENDATION
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselProductDataViewMapper

class SameSessionRecommendationMapper {
    fun convertToSameSessionRecommendationDataView(
        data: SearchSameSessionRecommendationModel,
        anchorProductId: String,
        keyword: String,
        dimension90: String,
        externalReference: String,
    ): SameSessionRecommendationDataView {
        val mapper = InspirationCarouselProductDataViewMapper()
        val products = mapper.convertToInspirationCarouselProductDataView(
            inspirationCarouselProduct = data.products,
            productPosition = 0,
            inspirationCarouselType = TYPE_SAME_SESSION_RECOMMENDATION,
            layout = LAYOUT_INSPIRATION_CAROUSEL_GRID,
            mapLabelGroupDataViewList = this::productLabelGroupToLabelGroupDataView,
            optionTitle = "",
            carouselTitle = "",
            dimension90 = dimension90,
            externalReference = externalReference,
        )
        val valueName = data.products.joinToString(",") { it.id }
        return SameSessionRecommendationDataView(
            title = data.title,
            componentId = data.componentId,
            trackingOption = data.trackingOption.toIntOrZero(),
            products = products,
            feedback = data.feedback.toSameSessionRecommendationFeedback(
                anchorProductId,
                keyword,
                valueName,
                dimension90,
            ),
            anchorProductId = anchorProductId,
            keyword = keyword,
            dimension90 = dimension90,
        )
    }

    private fun productLabelGroupToLabelGroupDataView(
        productLabelGroupList: List<SearchProductModel.ProductLabelGroup>,
    ): List<LabelGroupDataView> {
        return productLabelGroupList.map {
            LabelGroupDataView(
                it.position,
                it.type,
                it.title,
                it.url,
            )
        }
    }

    private fun SearchSameSessionRecommendationModel.Feedback.toSameSessionRecommendationFeedback(
        anchorProductId: String,
        keyword: String,
        valueName: String,
        dimension90: String,
    ): SameSessionRecommendationDataView.Feedback {
        return SameSessionRecommendationDataView.Feedback(
            title = title,
            componentId = componentId,
            trackingOption = trackingOption.toIntOrZero(),
            items = data.toFeedbackItems(this, anchorProductId, keyword, valueName, dimension90),
            keyword = keyword,
            anchorProductId = anchorProductId,
            dimension90 = dimension90,
        )
    }

    private fun List<SearchSameSessionRecommendationModel.Feedback.Data>.toFeedbackItems(
        feedback: SearchSameSessionRecommendationModel.Feedback,
        anchorProductId: String,
        keyword: String,
        valueName: String,
        dimension90: String,
    ): List<SameSessionRecommendationDataView.Feedback.FeedbackItem> {
        return map {
            SameSessionRecommendationDataView.Feedback.FeedbackItem(
                name = it.name,
                applink = it.applink,
                url = it.url,
                imageUrl = it.imageUrl,
                componentId = it.componentId,
                titleOnClick = it.titleOnClick,
                messageOnClick = it.messageOnClick,
                trackingOption = feedback.trackingOption.toIntOrZero(),
                keyword = keyword,
                valueName = valueName,
                anchorProductId = anchorProductId,
                dimension90 = dimension90,
            )
        }
    }
}
