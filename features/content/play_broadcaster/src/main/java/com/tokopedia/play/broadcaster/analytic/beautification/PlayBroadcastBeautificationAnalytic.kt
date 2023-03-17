package com.tokopedia.play.broadcaster.analytic.beautification

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
    fun clickEntryPointOnPreparationPage(authorId: String, authorType: String)

    /** Row 2 */
    fun viewBeautificationCoachmark(authorId: String, authorType: String)

    /** Row 3 */
    fun clickCloseBeautificationCoachmark(authorId: String, authorType: String)

    /** Row 4 */
    fun openScreenEntryPointOnPreparationPage()

    /** Row 5 */
    fun openScreenBeautificationBottomSheet()

    /** Row 6 */
    fun clickCustomFace(authorId: String, authorType: String, page: Page, customFace: String)

    /** Row 7 */
    fun clickNoneCustomFace(authorId: String, authorType: String, page: Page)

    /** Row 8 */
    fun clickBeautificationTab(authorId: String, authorType: String, page: Page, tab: Tab)

    /** Row 9 */
    fun clickBeautyFilterReset(authorId: String, authorType: String, page: Page)

    /** Row 10 */
    fun clickSliderBeautyFilter(authorId: String, authorType: String, page: Page, tab: Tab, customFace: String) /** TODO: not only customFace but also make up */

    /** Row 11 */
    fun viewResetFilterPopup(authorId: String, authorType: String, page: Page, tab: Tab) /** TODO: tab is not important */

    /** Row 12 */
    fun clickYesResetFilter(authorId: String, authorType: String, page: Page)

    /** Row 13 */
    fun clickPresetMakeup(authorId: String, authorType: String, page: Page, preset: String)

    /** Row 14 */
    fun clickNonePreset(authorId: String, authorType: String, page: Page)

    /** Row 15 */
    fun clickDownloadPreset(authorId: String, authorType: String, page: Page, preset: String)

    /** Row 16 */
    fun clickRetryDownloadPreset(authorId: String, authorType: String, page: Page, preset: String)

    /** Row 17 */
    fun viewFailDownloadPreset(authorId: String, authorType: String, page: Page, preset: String)

    /** Row 18 */
    fun viewFailApplyBeautyFilter(authorId: String, authorType: String, page: Page, customFace: String)

    /** Row 19 */
    fun clickRetryApplyBeautyFilter(authorId: String, authorType: String, page: Page, customFace: String)

    /** Row 20 */
    fun clickEntryPointOnLivePage(authorId: String, authorType: String)

    /** Row 21 */
    fun viewEntryPointOnLivePage(authorId: String, authorType: String)

    /** Row 22 */
    fun clickRetryReapplyBeautyFilter(authorId: String, authorType: String) /** TOOD: ini apa ya? */

    /** Row 23 */
    fun viewFailReapplyBeautyFilter(authorId: String, authorType: String) /** TOOD: ini apa ya? */

    enum class Page(val value: String) {
        Preparation("prep page"),
        Live("ongoing livestream")
    }

    enum class Tab(val value: String) {
        FaceShaping("face shaping"),
        Makeup("makeup")
    }
}
