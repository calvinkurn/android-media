package com.tokopedia.play.broadcaster.analytic.beautification

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
interface PlayBroadcastBeautificationAnalytic {

    /**
     * MyNakama
     * MA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3756
     * SA : https://mynakama.tokopedia.com/datatracker/requestdetail/3755
     */

    /** Row 1 */
    fun clickBeautificationEntryPointOnPreparationPage(account: ContentAccountUiModel)

    /** Row 2 */
    fun viewBeautificationCoachmark(account: ContentAccountUiModel)

    /** Row 3 */
    fun clickCloseBeautificationCoachmark(account: ContentAccountUiModel)

    /** Row 4 */
    fun openScreenBeautificationEntryPointOnPreparationPage()

    /** Row 5 */
    fun openScreenBeautificationBottomSheet()

    /** Row 6 */
    fun clickCustomFace(account: ContentAccountUiModel, page: Page, customFace: String)

    /** Row 7 */
    fun clickNoneCustomFace(account: ContentAccountUiModel, page: Page)

    /** Row 8 */
    fun clickBeautificationTab(account: ContentAccountUiModel, page: Page, tab: Tab)

    /** Row 9 */
    fun clickBeautyFilterReset(account: ContentAccountUiModel, page: Page)

    /** Row 10 */
    fun clickSliderBeautyFilter(account: ContentAccountUiModel, page: Page, tab: Tab, filterName: String)

    /** Row 11 */
    fun viewResetFilterPopup(account: ContentAccountUiModel, page: Page, tab: Tab)

    /** Row 12 */
    fun clickYesResetFilter(account: ContentAccountUiModel, page: Page)

    /** Row 13 */
    fun clickPresetMakeup(account: ContentAccountUiModel, page: Page, preset: String)

    /** Row 14 */
    fun clickNonePreset(account: ContentAccountUiModel, page: Page)

    /** Row 15 */
    fun clickDownloadPreset(account: ContentAccountUiModel, page: Page, preset: String)

    /** Row 16 */
    fun clickRetryDownloadPreset(account: ContentAccountUiModel, page: Page, preset: String)

    /** Row 17 */
    fun viewFailDownloadPreset(account: ContentAccountUiModel, page: Page, preset: String)

    /** Row 18 */
    fun viewFailApplyBeautyFilter(account: ContentAccountUiModel, page: Page, customFace: String)

    /** Row 19 */
    fun clickRetryApplyBeautyFilter(account: ContentAccountUiModel, page: Page, customFace: String)

    /** Row 20 */
    fun clickBeautificationEntryPointOnLivePage(account: ContentAccountUiModel)

    /** Row 21 */
    fun viewBeautificationEntryPointOnLivePage(account: ContentAccountUiModel)

    /** Row 22 */
    fun clickRetryReapplyBeautyFilter(account: ContentAccountUiModel)

    /** Row 23 */
    fun viewFailReapplyBeautyFilter(account: ContentAccountUiModel)

    enum class Page(val value: String) {
        Unknown(""),
        Preparation("prep page"),
        Live("ongoing livestream")
    }

    enum class Tab(val value: String) {
        Unknown(""),
        FaceShaping("face shaping"),
        Makeup("makeup")
    }
}
