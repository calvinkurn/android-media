package com.tokopedia.otp.verification.common.di

import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by Ade Fulki on 30/05/20.
 */

object VerificationComponentBuilder {

    fun getComponent(application: BaseMainApplication): VerificationComponent =
            DaggerVerificationComponent.builder().baseAppComponent(application.baseAppComponent).build()
}