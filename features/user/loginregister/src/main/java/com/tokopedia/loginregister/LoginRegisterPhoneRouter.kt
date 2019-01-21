package com.tokopedia.loginregister

import android.content.Context
import android.content.Intent
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel

/**
 * @author by nisie on 18/01/19.
 */
interface LoginRegisterPhoneRouter {

    companion object {
        const val RESULT_SUCCESS_AUTO_LOGIN = 333
    }

    fun getNoTokocashAccountIntent(context: Context, phoneNumber: String): Intent

    fun getCheckRegisterPhoneNumberIntent(context: Context): Intent

    fun getChooseTokocashAccountIntent(context: Context, data: ChooseTokoCashAccountViewModel): Intent

    fun getTokoCashOtpIntent(context: Context, phoneNumber: String,
                             canUseOtherMethod: Boolean,
                             defaultRequestMode: String): Intent

    fun getCheckLoginPhoneNumberIntent(context: Context): Intent
}