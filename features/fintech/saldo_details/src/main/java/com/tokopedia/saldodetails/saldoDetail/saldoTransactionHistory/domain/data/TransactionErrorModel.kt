package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

data class TransactionErrorModel(val throwable: Throwable) : ErrorNetworkModel()