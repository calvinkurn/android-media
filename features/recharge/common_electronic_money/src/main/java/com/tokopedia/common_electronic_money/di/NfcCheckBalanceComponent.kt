package com.tokopedia.common_electronic_money.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_electronic_money.fragment.NfcCheckBalanceFragment
import com.tokopedia.common_electronic_money.util.EmoneyAnalytics
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Component

@NfcCheckBalanceScope
@Component(modules = [NfcCheckBalanceModule::class], dependencies = [BaseAppComponent::class])
interface NfcCheckBalanceComponent {

    fun inject(nfcCheckBalanceFragment: NfcCheckBalanceFragment)

    fun remoteConfig(): RemoteConfig

    fun emoneyAnalytics(): EmoneyAnalytics

    fun userSession(): UserSessionInterface
}