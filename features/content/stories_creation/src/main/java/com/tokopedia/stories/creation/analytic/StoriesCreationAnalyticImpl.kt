package com.tokopedia.stories.creation.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.analytic.helper.StoriesCreationAnalyticHelper
import com.tokopedia.stories.creation.analytic.sender.StoriesCreationAnalyticSender
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
class StoriesCreationAnalyticImpl @Inject constructor(
    private val analyticSender: StoriesCreationAnalyticSender
) : StoriesCreationAnalytic {

    override fun openScreenCreationPage(account: ContentAccountUiModel, storyId: String) {
        analyticSender.sendGeneralOpenScreen(
            screenName = "/play broadcast story - ${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - review page post creation - $storyId",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47813", "47931")
        )
    }

    override fun clickAddProduct(account: ContentAccountUiModel) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - tambah product post creation page",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47814", "47932")
        )
    }

    override fun clickUpload(account: ContentAccountUiModel, storyId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - upload",
            eventLabel = StoriesCreationAnalyticHelper.getEventLabel(account, storyId),
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47815", "47933")
        )
    }
}
