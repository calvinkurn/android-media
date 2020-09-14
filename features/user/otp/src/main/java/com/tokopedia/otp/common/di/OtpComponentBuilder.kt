package com.tokopedia.otp.common.di

import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by Ade Fulki on 09/09/20.
 */

object OtpComponentBuilder {

    fun getComponent(application: BaseMainApplication): OtpComponent =
            DaggerOtpComponent.builder()
                    .baseAppComponent(application.baseAppComponent).build()
}