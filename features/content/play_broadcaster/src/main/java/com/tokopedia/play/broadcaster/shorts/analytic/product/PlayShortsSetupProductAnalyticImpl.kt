package com.tokopedia.play.broadcaster.shorts.analytic.product

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 */
class PlayShortsSetupProductAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val analyticSender: PlayShortsAnalyticSender,
) : PlayBroSetupProductAnalytic {

    private var account = ContentAccountUiModel.Empty

    override fun setSelectedAccount(account: ContentAccountUiModel) {
        this.account = account
    }

    override fun clickSearchBarOnProductSetup(search: String) {
        TODO("Not yet implemented")
    }

    override fun clickSaveButtonOnProductSetup() {
        TODO("Not yet implemented")
    }

    /**
     * Row 71
     */
    override fun clickAddMoreProductOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - add product card",
            account = account,
            trackerId = "37594",
        )
    }

    /**
     * Row 72
     */
    override fun clickCloseOnProductSummary() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - back product selection page",
            account = account,
            trackerId = "37595",
        )
    }

    /**
     * Row 73
     */
    override fun clickDeleteProductOnProductSetup(productId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - delete a product tagged",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $productId",
            trackerId = "click - delete a product tagged",
        )
    }

    /**
     * Row 74
     */
    override fun clickDoneOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - save product tag",
            account = account,
            trackerId = "37597",
        )
    }

    /**
     * Row 75
     */
    override fun viewProductSummary() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection summary",
            account = account,
            trackerId = "37598"
        )
    }

    override fun clickSelectProductOnProductSetup(productId: String) {
        TODO("Not yet implemented")
    }

    override fun clickCampaignAndEtalaseFilter() {
        TODO("Not yet implemented")
    }

    override fun clickProductSorting() {
        TODO("Not yet implemented")
    }

    override fun clickProductSortingType(sortName: String) {
        TODO("Not yet implemented")
    }

    override fun clickEtalaseCard() {
        TODO("Not yet implemented")
    }

    override fun clickCampaignCard() {
        TODO("Not yet implemented")
    }

    override fun clickCloseOnProductChooser(isProductSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun clickConfirmCloseOnProductChooser() {
        TODO("Not yet implemented")
    }

    override fun clickCancelCloseOnProductChooser() {
        TODO("Not yet implemented")
    }
}
