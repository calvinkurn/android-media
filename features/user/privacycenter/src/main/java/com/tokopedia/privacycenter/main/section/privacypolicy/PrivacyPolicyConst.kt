package com.tokopedia.privacycenter.main.section.privacypolicy

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.url.TokopediaUrl

object PrivacyPolicyConst {

//    private val BASE_URL = "${TokopediaUrl.getInstance().API}ent-tnc/v1/privacy/history"

    // for testing purpose
    private val BASE_URL = "https://api-staging.tokopedia.com/ent-tnc/v1/privacy/history"

    val GET_LIST_URL = "$BASE_URL?language=id&showAll=true"

    val WEBVIEW_URL = "${TokopediaUrl.getInstance().MOBILEWEB}privacy/history/"

    fun getPrivacyPolicyDetailUrl(sectionId: String): String {
        return "$BASE_URL/${sectionId.toIntOrZero()}"
    }

    const val RESPONSE_OK = "200"
    const val RESPONSE_SUCCESS = "success"
    const val SECTION_ID = "sectionId"
    const val KEY_TITLE = "title"
    const val DEFAULT_TITLE = "Kebijakan Privasi"

    val CURRENT_PRIVACY_URL = "${TokopediaUrl.getInstance().WEB}privacy?lang=id"
}
