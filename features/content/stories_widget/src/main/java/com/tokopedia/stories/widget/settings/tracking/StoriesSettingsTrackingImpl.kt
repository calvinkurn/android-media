package com.tokopedia.stories.widget.settings.tracking

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 4/19/24
 * MA: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4524
 * SA: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4523
 */

class StoriesSettingsTrackingImpl @Inject constructor(
    managerFactory: ContentAnalyticManager.Factory,
    private val userSessionInterface: UserSessionInterface
) : StoriesSettingsTracking {

    private val analyticManager = managerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.automatedStory
    )
    private val authorId
        get() = userSessionInterface.shopId

    override fun openScreen() {
        analyticManager.sendOpenScreen(
            screenName = "/play automated story",
            mainAppTrackerId = "50194",
            sellerAppTrackerId = "50293"
        )
    }

    override fun clickToggle(option: StoriesSettingOpt) {
        val status = if (option.isSelected) "on" else "off"
        analyticManager.sendClickContent(
            eventAction = "click - pengaturan konten toggle",
            eventLabel = "$authorId - $status",
            mainAppTrackerId = "50195",
            sellerAppTrackerId = "50294"
        )
    }

    override fun clickCheck(option: StoriesSettingOpt) {
        val status = if (option.isSelected) "check" else "uncheck"

        analyticManager.sendClickContent(
            eventAction = "click - pengaturan konten checkbox",
            eventLabel = "$authorId - $status - ${option.optionType}",
            mainAppTrackerId = "50196",
            sellerAppTrackerId = "50295"
        )
    }
}
