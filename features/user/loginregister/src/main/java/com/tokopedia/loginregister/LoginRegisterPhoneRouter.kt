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
}