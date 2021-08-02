package com.tokopedia.saldodetails.adapter.model

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

data class TransactionErrorModel(val throwable: Throwable) : ErrorNetworkModel(){

    override fun type(adapterTypeFactory: AdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}