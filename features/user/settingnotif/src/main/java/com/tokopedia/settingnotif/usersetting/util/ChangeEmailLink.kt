package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.WEBVIEW
import com.tokopedia.url.TokopediaUrl
import java.net.URLEncoder.encode as encode

object ChangeEmailLink {

    private const val OTP_PATH_URL = "otp"
    private const val CHECK_PATH_URL = "c"
    private const val PAGE_PATH_URL = "page"
    private const val USER_PATH_URL = "user"
    private const val PROFILE_PATH_URL = "profile"
    private const val EDIT_PATH_URL = "edit"
    private const val EMAIL_PATH_URL = "email"

    private const val PARAM_B = "b"
    private const val PARAM_IS_BACK = "isBack"
    private const val PARAM_V_OEMAIL = "v_oemail"
    private const val PARAM_EMAIL = "email"
    private const val PARAM_TYPE = "type"
    private const val PARAM_LD = "ld"
    private const val PARAM_OTP_TYPE = "otp_type"

    operator fun invoke(context: Context?, email: String) {
        val encodedUrlB = encode(
                Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
                    appendPath(USER_PATH_URL)
                    appendPath(PROFILE_PATH_URL)
                    appendPath(EDIT_PATH_URL)
                    appendQueryParameter(PARAM_IS_BACK, true.toString())
                }.build().toString(),
                "UTF-8"
        )

        val encodedUrlLd = encode(
                Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
                    appendPath(USER_PATH_URL)
                    appendPath(PROFILE_PATH_URL)
                    appendPath(EMAIL_PATH_URL)
                    appendQueryParameter(PARAM_V_OEMAIL, email)
                    appendQueryParameter(PARAM_TYPE, "change")
                }.build().toString(),
                "UTF-8"
        )

        val encodedEmail = encode(email, "UTF-8")

        val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB).buildUpon().apply {
            appendPath(OTP_PATH_URL)
            appendPath(CHECK_PATH_URL)
            appendPath(PAGE_PATH_URL)
            appendQueryParameter(PARAM_B, encodedUrlB)
            appendQueryParameter(PARAM_EMAIL, encodedEmail)
            appendQueryParameter(PARAM_LD, encodedUrlLd)
            appendQueryParameter(PARAM_OTP_TYPE, 14.toString())
        }.build().toString()

        val emailUrl = WEBVIEW.replace("{url}", encode(url, "UTF-8"))
        RouteManager.route(context, emailUrl)
    }

}