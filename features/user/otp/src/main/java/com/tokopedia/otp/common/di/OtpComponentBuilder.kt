package com.tokopedia.otp.common.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by Ade Fulki on 09/09/20.
 */

object OtpComponentBuilder {

    fun getComponent(application: BaseMainApplication, activityContext: Context): OtpComponent =
            DaggerOtpComponent.builder()
                    .otpModule(OtpModule(activityContext))
                    .baseAppComponent(application.baseAppComponent).build()
}