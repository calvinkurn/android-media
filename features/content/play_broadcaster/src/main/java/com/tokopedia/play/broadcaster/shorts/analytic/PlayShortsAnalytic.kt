package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 23, 2022
 */
interface PlayShortsAnalytic {

    fun clickBackOnPreparationPage(account: ContentAccountUiModel)

    fun clickCloseCoachMarkOnPreparationPage(account: ContentAccountUiModel)

    fun clickSwitchAccount(account: ContentAccountUiModel)

    fun clickCloseSwitchAccount(account: ContentAccountUiModel)

    fun clickUserAccount(account: ContentAccountUiModel)

    fun clickShopAccount(account: ContentAccountUiModel)

    fun viewSwitchAccountToShopConfirmation(account: ContentAccountUiModel)

    fun clickCancelSwitchAccountToShop(account: ContentAccountUiModel)

    fun viewSwitchAccountToUserConfirmation(account: ContentAccountUiModel)

    fun clickCancelSwitchAccountToUser(account: ContentAccountUiModel)

    fun clickCancelOnboardingUGC(account: ContentAccountUiModel)

    fun clickTextFieldUsernameOnboardingUGC(account: ContentAccountUiModel)

    fun clickAcceptTncOnboardingUGC(account: ContentAccountUiModel)

    fun viewOnboardingUGC(account: ContentAccountUiModel)

    fun clickContinueOnboardingUGC(account: ContentAccountUiModel)

    fun viewSwitchAccountBottomSheet(account: ContentAccountUiModel)

    fun viewPreparationPage(account: ContentAccountUiModel)

    fun clickMenuTitle(account: ContentAccountUiModel)

    fun clickMenuProduct(account: ContentAccountUiModel)

    fun clickMenuCover(account: ContentAccountUiModel)

    fun clickBackOnTitleForm(account: ContentAccountUiModel)

    fun clickTextFieldOnTitleForm(account: ContentAccountUiModel)

    fun clickSaveOnTitleForm(account: ContentAccountUiModel)

    fun clickClearTextBoxOnTitleForm(account: ContentAccountUiModel)

    fun openScreenTitleForm(account: ContentAccountUiModel)

    fun clickCloseOnCoverForm(account: ContentAccountUiModel)

    fun clickSelectCoverOnCoverForm(account: ContentAccountUiModel)

    fun openScreenCoverForm(account: ContentAccountUiModel)

    fun viewLeavePreparationConfirmationPopup(account: ContentAccountUiModel)

    fun clickContinueOnLeaveConfirmationPopup(account: ContentAccountUiModel)

    fun clickBackOnSummaryPage(account: ContentAccountUiModel)

    fun clickContentTag(tag: String, account: ContentAccountUiModel)

    fun clickUploadVideo(channelId: String, account: ContentAccountUiModel)

    fun openScreenSummaryPage(account: ContentAccountUiModel)

    fun clickRefreshContentTag(account: ContentAccountUiModel)

    fun clickNextOnPreparationPage(account: ContentAccountUiModel)
}
