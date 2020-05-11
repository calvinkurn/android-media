package com.tokopedia.payment.fingerprint.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.activity.TopPayActivity
import dagger.Component

/**
 * Created by zulfikarrahman on 4/2/18.
 */
@FingerprintScope
@Component(modules = [FingerprintModule::class], dependencies = [BaseAppComponent::class])
interface FingerprintComponent {
    fun inject(topPayActivity: TopPayActivity)
}