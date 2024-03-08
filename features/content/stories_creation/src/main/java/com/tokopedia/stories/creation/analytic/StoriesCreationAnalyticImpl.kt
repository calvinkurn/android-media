package com.tokopedia.stories.creation.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
class StoriesCreationAnalyticImpl @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory,
) : StoriesCreationAnalytic {

    private val analyticManager = analyticManagerFactory.create(
        BusinessUnit.content,
        EventCategory.storyCreation
    )

    override fun openScreenCreationPage(account: ContentAccountUiModel, storyId: String) {
        analyticManager.sendOpenScreen(
            screenName = analyticManager.concatLabels(
                "/play broadcast story",
                analyticManager.concatLabelsWithAuthor(account.toAnalyticModel()),
                "review page post creation",
                storyId
            ),
            mainAppTrackerId = "47813",
            sellerAppTrackerId = "47931",
        )
    }

    override fun clickAddProduct(account: ContentAccountUiModel) {
        analyticManager.sendClickContent(
            eventAction = "click - tambah product post creation page",
            eventLabel = analyticManager.concatLabelsWithAuthor(account.toAnalyticModel()),
            mainAppTrackerId = "47814",
            sellerAppTrackerId = "47932"
        )
    }

    override fun clickUpload(account: ContentAccountUiModel, storyId: String) {
        analyticManager.sendClickContent(
            eventAction = "click - upload",
            eventLabel = analyticManager.concatLabelsWithAuthor(account.toAnalyticModel(), storyId),
            mainAppTrackerId = "47815",
            sellerAppTrackerId = "47933"
        )
    }
}
