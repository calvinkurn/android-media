package com.tokopedia.ovop2p.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.ovop2p.OvoFormFragment

import dagger.Component

@OvoP2pTransferScope
@Component(modules = [OvoP2pTransferModule::class], dependencies = [BaseAppComponent::class])
interface OvoP2pTransferComponent {
    fun inject(ovoFormFragment: OvoFormFragment)
}
