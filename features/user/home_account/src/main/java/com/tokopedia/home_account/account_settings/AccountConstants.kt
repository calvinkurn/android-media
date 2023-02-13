package com.tokopedia.home_account.account_settings


/**
 * @author okasurya on 7/20/18.
 */
object AccountConstants {
    object Analytics {
        const val CLICK_HOME_PAGE = "clickHomePage"
        const val CLICK_ACCOUNT = "clickAccount"
        const val CLICK_ACCOUNT_SETTING = "clickAccountSetting"
        const val CLICK_OTP = "clickOtp"
        const val CLICK = "click"
        const val SETTING = "setting"
        const val FINGERPRINT = "fingerprint"
        const val SHOP = "shop"
        const val ACCOUNT = "account"
        const val PASSWORD = "password"
        const val TOKOCASH = "tokocash"
        const val BALANCE = "balance"
        const val GOPAY = "gopay"
        const val ACCOUNT_BANK = "account bank"
        const val CREDIT_CARD = "credit card"
        const val ACTIVITY_NAME_SALDO_DEPOSIT = "com.tokopedia.saldodetails.saldoDetail.SaldoDepositActivity"
        const val CLICK_KYC_SETTING = "click dokumen data diri"
        const val CLICK_BIOMETRIC_BUTTON = "click on button biometric"
        const val EVENT_CLICK_SAMPAI = "clickRegister"
        const val EVENT_CATEGORY_SAMPAI = "register tokopedia corner"
        const val EVENT_ACTION_SAMPAI = "click button daftar"
        const val CLICK_ON_PASSWORD = "click on kata sandi"
        const val CURRENT_SITE = "currentSite"
        const val TRACKER_ID = "trackerId"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val BUSINESS_UNIT = "businessUnit"
        const val USER_PLATFORM = "user platform"
    }

    interface ErrorCodes {
        companion object {
            const val ERROR_CODE_NULL_MENU = "ACC001"
            const val ERROR_CODE_ACCOUNT_SETTING_CONFIG = "ASC001"
        }
    }
}
