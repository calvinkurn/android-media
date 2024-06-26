package com.tokopedia.shop.open.common

import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.url.TokopediaUrl

object PageNameConstant {
    const val SPLASH_SCREEN_PAGE = "GREETING_PAGE"
    const val QUISIONER_PAGE = "QUISIONER_PAGE"
    const val FINISH_SPLASH_SCREEN_PAGE = "FINISH_SPLASH_SCREEN_PAGE"
}

object ExitDialog {
    const val TITLE = "Apa kamu yakin ingin \nkeluar?"
    const val DESCRIPTION = "Data telah tersimpan"
}

object TermsAndConditionsLink {
    val URL_TNC = "${TokopediaUrl.getInstance().WEB}terms?lang=id"
    val URL_PRIVACY_POLICY = "${TokopediaUrl.getInstance().WEB}privacy?lang=id"
}

object ImageAssets {
    val IMG_SHOP_OPEN_SPLASH_SCREEN = TokopediaImageUrl.IMG_SHOP_OPEN_SPLASH_SCREEN
}

object ScreenNameTracker {
    const val SCREEN_SHOP_REGISTRATION = "/shop registration"
    const val SCREEN_SHOP_SURVEY = "/shop survey"
    const val SCREEN_CONGRATULATION = "/registration page - shop/congratulation"
    const val SCREEN_HOORAY = "/registration page - shop/hooray"
}

object ErrorConstant {
    const val ERROR_SAVE_LOCATION_SHIPPING = "error save location shipping"
    const val ERROR_SEND_SURVEY = "error send survey"
    const val ERROR_GET_SURVEY_QUESTIONS = "error get survey questions"
    const val ERROR_CHECK_SHOP_NAME = "error check shop name"
    const val ERROR_CHECK_DOMAIN_NAME = "error check domain name"
    const val ERROR_DOMAIN_NAME_SUGGESTIONS = "error domain name suggestions"
    const val ERROR_CREATE_SHOP = "error create shop"
}
