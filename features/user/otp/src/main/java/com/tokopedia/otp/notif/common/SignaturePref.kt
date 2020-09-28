package com.tokopedia.otp.notif.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.string
import javax.inject.Inject

/**
 * Created by Ade Fulki on 24/09/20.
 */

class SignaturePref @Inject constructor(
        @ApplicationContext context: Context
) {
    private var sharedPreferences = context.getSharedPreferences(PUSH_NOTIF_SHARED_PREF, MODE_PRIVATE)

    var signature by sharedPreferences.string(key = SIGNATURE_PUSH_NOTIF_KEY)

    companion object {
        private const val PUSH_NOTIF_SHARED_PREF = "push_notif_shared_pref"
        private const val SIGNATURE_PUSH_NOTIF_KEY = "signature_push_notif_key"
    }
}