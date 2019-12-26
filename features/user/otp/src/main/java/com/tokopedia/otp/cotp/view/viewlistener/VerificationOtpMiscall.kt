package com.tokopedia.otp.cotp.view.viewlistener

/**
 * @author by rival on 12/10/19.
 */

interface VerificationOtpMiscall {
    interface View : Verification.View {
        fun updatePhoneHint(phoneHint: String)
    }
}
