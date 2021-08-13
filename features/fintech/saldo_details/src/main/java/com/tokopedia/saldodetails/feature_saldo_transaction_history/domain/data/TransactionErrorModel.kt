package com.tokopedia.saldodetails.feature_saldo_transaction_history.domain.data

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel

data class TransactionErrorModel(val throwable: Throwable) : ErrorNetworkModel()