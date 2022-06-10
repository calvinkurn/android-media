package com.tokopedia.ovop2p.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.ovop2p.view.fragment.*
import com.tokopedia.webview.BaseSessionWebViewFragment
import dagger.Component

@OvoP2pTransferScope
@Component(
    modules = [OvoP2pTransferModule::class, OvoP2PViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface OvoP2pTransferComponent {
    fun inject(ovoP2PForm: OvoP2PForm)
    fun inject(transferError: TransferError)
    fun inject(transactionDetails: TransactionDetails)
    fun inject(txnSucsOvoUser: TxnSucsOvoUser)
    fun inject(transactionSucsNonOvoUsr: TransactionSucsNonOvoUsr)
    fun inject(allContacts: AllContacts)
    fun inject(baseSessionWebViewFragment: BaseSessionWebViewFragment)
}
