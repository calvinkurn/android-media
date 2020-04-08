package com.tokopedia.otp.cotp.view.viewlistener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface OnboardingOtpMiscall {

    interface View: CustomerView {
        fun startAnimation()
        fun stopAnimation()
    }
}