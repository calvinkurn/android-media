package com.tokopedia.withdraw.saldowithdrawal.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * RP stand for Rekening Premium Program
 */
class WithdrawAnalytics @Inject constructor(
    val userSession: dagger.Lazy<UserSession>
) {

    fun sendScreen(screenName: String?) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickWithdrawal() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_WITHDRAWAL,
            ""
        )
        sendTrackingData(map)
    }

    fun eventClickWithdrawalAll() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_WITHDRAWAL_ALL,
            ""
        )
        sendTrackingData(map)
    }

    fun eventClickTANDC() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_TANDC,
            ""
        )
        sendTrackingData(map)
    }

    fun eventClickTarikSaldo() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_TARIK_SALDO,
            ""
        )
        sendTrackingData(map)
    }

    fun onBannerItemView(bannerId: Int, isBanner: Boolean) {
        val label = if (isBanner) {
            String.format(EVENT_BANNER_LABEL_YES_WIDGET, bannerId)
        } else {
            String.format(EVENT_BANNER_LABEL_NO_WIDGET, bannerId)
        }
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_PROMO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_VIEW_BANNER,
            label
        )
        sendTrackingData(map)
    }

    fun eventClickAddAccount() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_ACCOUNT_ADD,
            ""
        )
        sendTrackingData(map)
    }

    fun onClickManageAccount() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_MANAGE_ACCOUNT,
            ""
        )
        sendTrackingData(map)
    }

    fun onRekeningPremiumLogoClick() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_REKENING_PREMIUM_LOGO,
            ""
        )
        sendTrackingData(map)
    }

    fun onRekeningPremiumAccountInfoClosed() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLOSE_REKENING_PREMIUM_ACCOUNT_INFO,
            ""
        )
        sendTrackingData(map)
    }

    fun onRekeningPremiumAccountMoreInfo() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_REKENING_PREMIUM_ACCOUNT_MORE_INFO,
            ""
        )
        sendTrackingData(map)
    }

    fun onClickJoinRekeningProgram() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_JOIN_REKENING_PROGRAM,
            ""
        )
        sendTrackingData(map)
    }

    fun onBackFromWithdrawalJoinRPBottomSheet(bankNameStr: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_BACK_FROM_WITHDEAWAL_OPTION,
            bankNameStr ?: ""
        )
        sendTrackingData(map)
    }

    fun onWithdrawalByJoiningOfferOpen(bankNameStr: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_ON_WITHDRAWAL_BY_RP_OFFER_VISIBLE,
            bankNameStr ?: ""
        )
        sendTrackingData(map)
    }

    fun onClickWithdrawalBalanceAndJoin(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_WITHDRAWAL_AND_JOIN_RP,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onClickOnlyWithdrawalSaldo(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_ONLY_WITHDRAWAL_SALDO,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onSuccessPageRekeningPremiumLinkClick(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_ON_CLICK_RP_LINK,
            label
        )
        sendTrackingData(map)
    }

    fun onClickCloseOnSuccessScreen(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_ON_BACK_FROM_SALDO_PROCESSING,
            label
        )
        sendTrackingData(map)
    }

    fun onViewRekeningPremiumApplicationIsINProgress(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_RP_APPLICATION_IS_PROGRESS,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onViewRekeningPremiumApplicationFailed(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_RP_APPLICATION_FAILED,
            label
        )
        sendTrackingData(map)
    }

    fun onClickUpgradeToPowerMerchant(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_UPGRADE_POWER_MERCHANT,
            label
        )
        sendTrackingData(map)
    }

    fun onShowJoinRekeningPremiumWidgetOnSuccessPage(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_VIEW_JOIN_RP_WIDGET,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onViewRekeningPointWidget(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_VIEW_RP_POINT_WIDGET,
            label
        )
        sendTrackingData(map)
    }

    fun onDisableAccountClick(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_DISABLE_ACCOUNT,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onDisableAccountInfoSheetClose(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLOSE_DISABLE_ACCOUNT_INFO,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onDisableAccountInfoSheetOpen(bankName: String?) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_OPEN_DISABLE_ACCOUNT_INFO,
            bankName ?: ""
        )
        sendTrackingData(map)
    }

    fun onClickOpenRekPreInfoFromDisableAccount(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_CHECK_RP,
            label
        )
        sendTrackingData(map)
    }

    fun eventClickContinueBtn() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_CONTINUE,
            ""
        )
        sendTrackingData(map)
    }

    fun eventClickAccountBank() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_ACCOUNT_BANK,
            ""
        )
        sendTrackingData(map)
    }

    fun onNoTickerDisplayedOnSuccessPage(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_WITHDRAWAL_VIEW_PROCESSED,
            label
        )
        sendTrackingData(map)
    }

    fun onBannerItemClick(bannerId: Int, isBanner: Boolean) {
        val label = if (isBanner) {
            String.format(EVENT_BANNER_LABEL_YES_WIDGET, bannerId)
        } else {
            String.format(EVENT_BANNER_LABEL_NO_WIDGET, bannerId)
        }
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_PROMO_CLICK,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_BANNER,
            label
        )
        sendTrackingData(map)
    }

    fun onRekeningBannerClick(label: String) {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO_IRIS,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_CLICK_REKENING_BANNER,
            label
        )
        sendTrackingData(map)
    }

    fun onBackPressFromWithdrawalPage() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SALDO,
            EVENT_CATEGORY_WITHDRAWAL_PAGE,
            EVENT_ACTION_BACK_FROM_WITHDRAWAL,
            ""
        )
        sendTrackingData(map)
    }

    fun eventViewSuccessThankYouWithdrawalPage(withdrawalID: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_VIEW_ITEM,
            EVENT_CATEGORY_WITHDRAWAL_THANK_YOU_PAGE,
            EVENT_VIEW_WD_THANK_YOU_PAGE_SUCCESS,
            ""
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_42131
        map[KEY_ATTRIBUTION] = withdrawalID
        sendTrackingData(map)
    }

    fun eventViewFailedThankYouWithdrawalPage(withdrawalID: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_VIEW_ITEM,
            EVENT_CATEGORY_WITHDRAWAL_THANK_YOU_PAGE,
            EVENT_VIEW_WD_THANK_YOU_PAGE_FAILED,
            ""
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_42133
        map[KEY_ATTRIBUTION] = withdrawalID
        sendTrackingData(map)
    }

    fun eventClickCtaSuccessThankYouWithdrawalPage(withdrawalID: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_SELECT_CONTENT,
            EVENT_CATEGORY_WITHDRAWAL_THANK_YOU_PAGE,
            EVENT_ACTION_CLICK_CTA_THANK_YOU_PAGE_SUCCESS,
            ""
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_42132
        map[KEY_ATTRIBUTION] = withdrawalID
        sendTrackingData(map)
    }

    fun eventClickCtaFailedThankYouWithdrawalPage(withdrawalID: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_SELECT_CONTENT,
            EVENT_CATEGORY_WITHDRAWAL_THANK_YOU_PAGE,
            EVENT_ACTION_CLICK_CTA_THANK_YOU_PAGE_FAILED,
            ""
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_42134
        map[KEY_ATTRIBUTION] = withdrawalID
        sendTrackingData(map)
    }

    fun eventClickAutoTopAdsRecommendationWithdrawal(recommWDAmount: Long, originalWDAmount: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_CLICK_TOPADS,
            EVENT_CATEGORY_SALDO_PAGE,
            EVENT_ACTION_CLICK_RECOMMENDED_AMOUNT_WD,
            "$recommWDAmount|$originalWDAmount"
        )
        sendTrackingData(map)
    }

    fun eventClickAutoTopAdsOriginalWithdrawal(recommWDAmount: Long, originalWDAmount: Long) {
        val map = TrackAppUtils.gtmData(
            EVENT_CLICK_TOPADS,
            EVENT_CATEGORY_SALDO_PAGE,
            EVENT_ACTION_CLICK_ORIGINAL_AMOUNT_WD,
            "$recommWDAmount|$originalWDAmount"
        )
        sendTrackingData(map)
    }

    private fun sendTrackingData(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        map[KEY_BUSINESS_UNIT] = KEY_BUSINESS_UNIT_VALUE
        map[KEY_CURRENT_SITE] = KEY_CURRENT_SITE_VALUE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {

        const val SCREEN_WITHDRAW = "/withdrawpage"
        const val SCREEN_WITHDRAW_SUCCESS_PAGE = "/penarikan-diproses/"
        private const val EVENT_NAME_CLICK_SALDO = "clickWithdrawal"
        private const val EVENT_NAME_CLICK_SALDO_IRIS = "viewWithdrawalIris"
        private const val EVENT_VIEW_ITEM = "view_item"
        private const val EVENT_SELECT_CONTENT = "select_content"
        private const val EVENT_CLICK_TOPADS = "clickTopAds"

        private const val EVENT_NAME_PROMO = "promoView"
        private const val EVENT_NAME_PROMO_CLICK = "promoClick"

        private const val EVENT_CATEGORY_WITHDRAWAL_PAGE = "withdrawal page"
        private const val EVENT_CATEGORY_WITHDRAWAL_THANK_YOU_PAGE = "wd thank you page"
        private const val EVENT_CATEGORY_SALDO_PAGE = "saldo page"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL = "click withdrawal"
        private const val EVENT_ACTION_CLICK_REKENING_PREMIUM_LOGO = "click rekening premium logo"
        private const val EVENT_ACTION_CLOSE_REKENING_PREMIUM_ACCOUNT_INFO = "click back from rekening premium"
        private const val EVENT_ACTION_CLICK_REKENING_PREMIUM_ACCOUNT_MORE_INFO = "click selengkapnya"
        private const val EVENT_ACTION_CLICK_JOIN_REKENING_PROGRAM = "click gabung rekening premium"
        private const val EVENT_ACTION_CLICK_BACK_FROM_WITHDEAWAL_OPTION = "click back from withdraw option"
        private const val EVENT_ACTION_ON_WITHDRAWAL_BY_RP_OFFER_VISIBLE = "view penawaran gabung ke rekening premium"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL_AND_JOIN_RP = "click tarik saldo dan gabung"
        private const val EVENT_ACTION_CLICK_ONLY_WITHDRAWAL_SALDO = "click tarik saldo saja"
        private const val EVENT_ACTION_VIEW_BANNER = "view banner"
        private const val EVENT_ACTION_ON_BACK_FROM_SALDO_PROCESSING = "click back from penarikan diproses"
        private const val EVENT_ACTION_ON_CLICK_RP_LINK = "click rekening premium link"
        private const val EVENT_ACTION_RP_APPLICATION_IS_PROGRESS = "view pendaftaran rekprem sedang diproses"
        private const val EVENT_ACTION_RP_APPLICATION_FAILED = "view pendaftaran rekprem gagal diproses"
        private const val EVENT_ACTION_CLICK_UPGRADE_POWER_MERCHANT = "click upgrade ke power merchant"
        private const val EVENT_ACTION_VIEW_JOIN_RP_WIDGET = "view upgrade ke rekening premium"
        private const val EVENT_ACTION_VIEW_RP_POINT_WIDGET = "view widget rekening premium"
        private const val EVENT_ACTION_CLICK_DISABLE_ACCOUNT = "click disable account"
        private const val EVENT_ACTION_CLOSE_DISABLE_ACCOUNT_INFO = "click back from hanya bisa tarik saldo ke rekprem"
        private const val EVENT_ACTION_CLICK_CHECK_RP = "click cek rekening premium"
        private const val EVENT_ACTION_WITHDRAWAL_VIEW_PROCESSED = "view penarikan diproses"
        private const val EVENT_ACTION_CLICK_BANNER = "click banner"
        private const val EVENT_BANNER_LABEL_NO_WIDGET = "%s - widget no"
        private const val EVENT_BANNER_LABEL_YES_WIDGET = "%s - widget yes"
        private const val EVENT_ACTION_CLICK_REKENING_BANNER = "click widget rekening premium"
        private const val EVENT_ACTION_BACK_FROM_WITHDRAWAL = "click back from penarikan saldo rekening premium"
        private const val EVENT_ACTION_CLICK_RECOMMENDED_AMOUNT_WD = "click - tarik sesuai rekomendasi"
        private const val EVENT_ACTION_CLICK_ORIGINAL_AMOUNT_WD = "click - tetap lanjut tarik"

        private const val EVENT_ACTION_OPEN_DISABLE_ACCOUNT_INFO = "view hanya bisa tarik saldo ke rekprem"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL_ALL = "click tarik semua"
        private const val EVENT_ACTION_CLICK_ACCOUNT_BANK = "click account bank"
        private const val EVENT_ACTION_CLICK_ACCOUNT_ADD = "click tambah rekening"
        private const val EVENT_ACTION_CLICK_MANAGE_ACCOUNT = "click atur rekening"
        private const val EVENT_ACTION_CLICK_TARIK_SALDO = "click tarik saldo"
        private const val EVENT_ACTION_CLICK_TANDC = "click ketentuan penarikan saldo"
        private const val EVENT_ACTION_CLICK_CONTINUE = "click lanjut tarik"
        private const val EVENT_VIEW_WD_THANK_YOU_PAGE_SUCCESS = "view wd thank you page success"
        private const val EVENT_VIEW_WD_THANK_YOU_PAGE_FAILED = "view wd thank you page failed"
        private const val EVENT_ACTION_CLICK_CTA_THANK_YOU_PAGE_SUCCESS = "click button balik saldo tokopedia after wd success"
        private const val EVENT_ACTION_CLICK_CTA_THANK_YOU_PAGE_FAILED = "click button hubungi tokopedia after wd failed"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_BUSINESS_UNIT_VALUE = "payment"
        private const val KEY_CURRENT_SITE_VALUE = "tokopediamarketplace"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_ATTRIBUTION = "attribution"

        private const val TRACKER_ID_42131 = "42131"
        private const val TRACKER_ID_42132 = "42132"
        private const val TRACKER_ID_42133 = "42133"
        private const val TRACKER_ID_42134 = "42134"
    }
}
