package com.tokopedia.otp.cotp.view.viewlistener

import com.tokopedia.otp.common.util.PhoneCallReceiver

/**
 * @author by rival on 12/10/19.
 */

interface VerificationOtpMiscall {
    interface View : Verification.View {
        fun updatePhoneHint(phoneHint: String)
    }
}
