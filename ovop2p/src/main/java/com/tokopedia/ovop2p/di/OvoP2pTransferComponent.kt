package com.tokopedia.ovop2p.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.ovop2p.OvoFormFragment
import com.tokopedia.ovop2p.view.fragment.*
import com.tokopedia.webview.BaseSessionWebViewFragment

import dagger.Component

@OvoP2pTransferScope
@Component(modules = [OvoP2pTransferModule::class], dependencies = [BaseAppComponent::class])
interface OvoP2pTransferComponent {
    fun inject(ovoFormFragment: OvoFormFragment)
    fun inject(fragmentTransferError: FragmentTransferError)
    fun inject(fragmentTransactionDetails: FragmentTransactionDetails)
    fun inject(fragmentTransactionSuccessOvoUser: FragmentTransactionSuccessOvoUser)
    fun inject(fragmentTransactionSuccessNonOvoUser: FragmentTransactionSuccessNonOvoUser)
    fun inject(allContactsFragment: AllContactsFragment)
    fun inject(baseSessionWebViewFragment: BaseSessionWebViewFragment)
}
