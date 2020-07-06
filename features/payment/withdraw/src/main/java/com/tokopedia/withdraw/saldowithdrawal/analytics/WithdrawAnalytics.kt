package com.tokopedia.withdraw.saldowithdrawal.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * RP stand for Rekening Premium Program
 */
class WithdrawAnalytics @Inject constructor() {
    fun sendScreen(screenName: String?) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickWithdrawal() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL,
                ""
        ))
    }

    fun eventClickWithdrawalAll() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL_ALL,
                ""
        ))
    }

    fun eventClickTANDC() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TANDC,
                ""
        ))
    }


    fun eventClickTarikSaldo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_TARIK_SALDO,
                ""
        ))
    }


    fun onBannerItemView() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_PROMO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_VIEW_BANNER,
                ""
        ))
    }


    fun eventClickAddAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_ADD,
                ""
        ))
    }

    fun onClickManageAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_MANAGE_ACCOUNT,
                ""
        ))
    }

    fun onRekeningPremiumLogoClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_REKENING_PREMIUM_LOGO,
                ""
        ))
    }

    fun onRekeningPremiumAccountInfoClosed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLOSE_REKENING_PREMIUM_ACCOUNT_INFO,
                ""
        ))
    }

    fun onRekeningPremiumAccountMoreInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_REKENING_PREMIUM_ACCOUNT_MORE_INFO,
                ""
        ))
    }


    fun onClickJoinRekeningProgram() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_JOIN_REKENING_PROGRAM,
                ""
        ))

    }

    fun onBackFromWithdrawalJoinRPBottomSheet(bankNameStr: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_FROM_WITHDEAWAL_OPTION,
                bankNameStr ?: ""
        ))
    }

    fun onWithdrawalByJoiningOfferOpen(bankNameStr: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_ON_WITHDRAWAL_BY_RP_OFFER_VISIBLE,
                bankNameStr ?: ""
        ))
    }


    fun onClickWithdrawalBalanceAndJoin(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_WITHDRAWAL_AND_JOIN_RP,
                bankName ?: ""
        ))
    }


    fun onClickOnlyWithdrawalSaldo(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ONLY_WITHDRAWAL_SALDO,
                bankName ?: ""
        ))
    }


    fun onSuccessPageRekeningPremiumLinkClick(label: String) {
        TrackApp.getInstance().gtm.pushEvent(EVENT_NAME_CLICK_SALDO_IRIS,
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                        EVENT_CATEGORY_WITHDRAWAL_PAGE,
                        EVENT_ACTION_ON_CLICK_RP_LINK,
                        label
                ))

    }


    fun onClickCloseOnSuccessScreen(label: String) {
        TrackApp.getInstance().gtm.pushEvent(EVENT_NAME_CLICK_SALDO_IRIS,
                TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                        EVENT_CATEGORY_WITHDRAWAL_PAGE,
                        EVENT_ACTION_ON_BACK_FROM_SALDO_PROCESSING,
                        label
                ))
    }


    fun eventClickBackToSaldoPage(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL,
                label
        ))
    }


    fun onViewRekeningPremiumApplicationIsINProgress(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_RP_APPLICATION_IS_PROGRESS,
                bankName ?: ""
        ))
    }

    fun onViewRekeningPremiumApplicationFailed(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_RP_APPLICATION_FAILED,
                label))
    }


    fun onClickUpgradeToPowerMerchant(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_UPGRADE_POWER_MERCHANT,
                label))
    }


    fun onShowJoinRekeningPremiumWidgetOnSuccessPage(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_VIEW_JOIN_RP_WIDGET,
                bankName ?: ""))
    }


    fun onViewRekeningPointWidget(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_VIEW_RP_POINT_WIDGET,
                label))
    }


    fun onDisableAccountClick(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_DISABLE_ACCOUNT,
                bankName ?: ""))
    }


    fun onDisableAccountInfoSheetClose(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLOSE_DISABLE_ACCOUNT_INFO,
                bankName ?: ""))
    }


    fun onDisableAccountInfoSheetOpen(bankName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_OPEN_DISABLE_ACCOUNT_INFO,
                bankName ?: ""))
    }

    fun onClickOpenRekPreInfoFromDisableAccount(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CHECK_RP,
                label))
    }


    fun eventClickContinueBtn() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_CONTINUE,
                ""
        ))
    }

    fun eventClickAccountBank() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_ACCOUNT_BANK,
                ""
        ))
    }

    fun onNoTickerDisplayedOnSuccessPage(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_WITHDRAWAL_VIEW_PROCESSED,
                label
        ))
    }

    fun onBannerItemClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_PROMO_CLICK,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_BANNER,
                EVENT_BANNER_LABEL
        ))
    }

    fun onRekeningBannerClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO_IRIS,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_CLICK_REKENING_BANNER,
                ""
        ))
    }

    fun onBackPressFromWithdrawalPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_SALDO,
                EVENT_CATEGORY_WITHDRAWAL_PAGE,
                EVENT_ACTION_BACK_FROM_WITHDRAWAL,
                ""
        ))
    }

    companion object {

        const val SCREEN_WITHDRAW = "/withdrawpage"
        const val SCREEN_WITHDRAW_SUCCESS_PAGE = "/penarikan-diproses/"
        private const val EVENT_NAME_CLICK_SALDO = "clickWithdrawal"
        private const val EVENT_NAME_CLICK_SALDO_IRIS = "viewWithdrawalIris"


        private const val EVENT_NAME_PROMO = "promoView"
        private const val EVENT_NAME_PROMO_CLICK = "promoClick"

        private const val EVENT_CATEGORY_WITHDRAWAL_PAGE = "withdrawal page"
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
        private const val EVENT_BANNER_LABEL = "1 - widget no"
        private const val EVENT_ACTION_CLICK_REKENING_BANNER = "click widget rekening premium"
        private const val EVENT_ACTION_BACK_FROM_WITHDRAWAL = "click back from penarikan saldo rekening premium"

        private const val EVENT_ACTION_OPEN_DISABLE_ACCOUNT_INFO = "view hanya bisa tarik saldo ke rekprem"
        private const val EVENT_ACTION_CLICK_WITHDRAWAL_ALL = "click tarik semua"
        private const val EVENT_ACTION_CLICK_ACCOUNT_BANK = "click account bank"
        private const val EVENT_ACTION_CLICK_ACCOUNT_ADD = "click tambah rekening"
        private const val EVENT_ACTION_CLICK_MANAGE_ACCOUNT = "click atur rekening"
        private const val EVENT_ACTION_CLICK_TARIK_SALDO = "click tarik saldo"
        private const val EVENT_ACTION_CLICK_TANDC = "click ketentuan penarikan saldo"
        private const val EVENT_ACTION_CLICK_CONTINUE = "click lanjut tarik"
        private const val EVENT_ACTION_CLICK_BACK_TO_SALDO_DETAIL = "click kembali detail saldo"
    }
}