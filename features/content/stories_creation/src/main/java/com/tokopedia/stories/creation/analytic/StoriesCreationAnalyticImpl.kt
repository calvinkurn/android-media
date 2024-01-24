package com.tokopedia.stories.creation.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.base.BaseContentAnalytic
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
class StoriesCreationAnalyticImpl @Inject constructor(
    override val userSession: UserSessionInterface
) : BaseContentAnalytic(), StoriesCreationAnalytic {

    override val businessUnit: String = BusinessUnit.content

    override val eventCategory: String = EventCategory.storyCreation

    override fun openScreenCreationPage(account: ContentAccountUiModel, storyId: String) {
        sendOpenScreen(
            screenName = concatLabels(
                "/play broadcast story",
                concatLabelsWithAuthor(account.toAnalyticModel()),
                "review page post creation",
                storyId
            ),
            mainAppTrackerId = "47813",
            sellerAppTrackerId = "47931",
        )
    }

    override fun clickAddProduct(account: ContentAccountUiModel) {
        sendClickContent(
            eventAction = "click - tambah product post creation page",
            eventLabel = concatLabelsWithAuthor(account.toAnalyticModel()),
            mainAppTrackerId = "47814",
            sellerAppTrackerId = "47932"
        )
    }

    override fun clickUpload(account: ContentAccountUiModel, storyId: String) {
        sendClickContent(
            eventAction = "click - upload",
            eventLabel = concatLabelsWithAuthor(account.toAnalyticModel(), storyId),
            mainAppTrackerId = "47815",
            sellerAppTrackerId = "47933"
        )
    }
}
