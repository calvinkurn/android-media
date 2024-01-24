package com.tokopedia.people.analytic.tracker.review

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.base.BaseContentAnalytic
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
import com.tokopedia.people.analytic.UserProfileAnalytics
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 07, 2023
 */
class UserProfileReviewTrackerImpl @Inject constructor(
    override val userSession: UserSessionInterface,
) : BaseContentAnalytic(), UserProfileReviewTracker {

    override val businessUnit: String = BusinessUnit.content

    override val eventCategory: String = EventCategory.feedUserProfile

    override fun clickReviewTab(userId: String, isSelf: Boolean) {
        sendClickContent(
            eventAction = "click - review tab",
            eventLabel = concatLabels(userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf)),
            mainAppTrackerId = "44098"
        )
    }

    override fun clickUserProfileSettings(userId: String) {
        sendClickContent(
            eventAction = "click - gear icon",
            eventLabel = userId,
            mainAppTrackerId = "44099"
        )
    }

    override fun clickReviewSettingsToggle(userId: String, isOn: Boolean) {
        sendClickContent(
            eventAction = "click - toggle button",
            eventLabel = concatLabels(userId, UserProfileAnalytics.Function.isOnOrOff(isOn)),
            mainAppTrackerId = "44100"
        )
    }

    override fun clickReviewMedia(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendClickContent(
            eventAction = "click - review media",
            eventLabel = concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productId),
            mainAppTrackerId = "44101"
        )
    }

    override fun clickLikeReview(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendClickContent(
            eventAction = "click - like review",
            eventLabel = concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productId),
            mainAppTrackerId = "44102"
        )
    }

    override fun impressReviewCard(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String,
        position: Int
    ) {
        sendEEPromotions(
            event = Event.viewItem,
            eventAction = "impression - review",
            eventLabel = concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productId),
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    itemId = feedbackId,
                    itemName = "/feed user profile - review",
                    creativeName = "",
                    creativeSlot = position.toString(),
                )
            ),
            mainAppTrackerId = "44103"
        )
    }

    override fun openScreenEmptyOrHiddenReviewTab() {
        sendOpenScreen(
            screenName = "/user profile - review tab",
            mainAppTrackerId = "44104",
        )
    }

    override fun clickReviewSeeMoreDescription(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productId: String
    ) {
        sendClickContent(
            eventAction = "click - review selengkapnya",
            eventLabel = concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productId),
            mainAppTrackerId = "44108"
        )
    }

    override fun clickReviewProductInfo(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productReview: UserReviewUiModel.Product,
    ) {
        sendEEProduct(
            event = Event.selectContent,
            eventAction = "click - product on review",
            eventLabel = concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productReview.productID),
            itemList = "/user profile - review tab",
            products = listOf(
                ContentEnhanceEcommerce.Product(
                    itemId = productReview.productID,
                    itemName = productReview.productName,
                    itemBrand = "",
                    itemCategory = "",
                    itemVariant = productReview.productVariant.variantName,
                    price = "",
                    index = "",
                    customFields = emptyMap(),
                )
            ),
            mainAppTrackerId = "44110"
        )
    }
}
