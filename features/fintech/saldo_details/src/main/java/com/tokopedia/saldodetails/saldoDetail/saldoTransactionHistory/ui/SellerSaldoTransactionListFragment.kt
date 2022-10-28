package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.saldodetails.commom.utils.*
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TickerDownloadFeeTransactionModel as TickerDownloadFeeTransactionModel1

class SellerSaldoTransactionListFragment : BaseSaldoTransactionListFragment() {

    companion object {
        private const val PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE"
        private const val REMOTE_CONFIG_KEY = "android_saldo_download_commission_report_enabled"

        fun getInstance(transactionTitleStr: String): SellerSaldoTransactionListFragment {
            return SellerSaldoTransactionListFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_TRANSACTION_TYPE, transactionTitleStr)
                arguments = bundle
            }
        }
    }

    private val removeConfig: RemoteConfig? by lazy {
        context?.applicationContext?.let {
            return@lazy FirebaseRemoteConfigImpl(it)
        }
        return@lazy null
    }

    override fun onDataLoaded(historyList: List<Visitable<*>>, hasMore: Boolean) {
        val isDownloadCommissionEnabled = getIsDownloadCommissionEnabled()
        val list: List<Visitable<*>> =
            if (GlobalConfig.isSellerApp() && isDownloadCommissionEnabled) {
                if (historyList.isEmpty()) {
                    listOf(TickerDownloadFeeTransactionModel1()) + EmptyModel()
                } else {
                    listOf(TickerDownloadFeeTransactionModel1()) + historyList
                }
            } else {
                if (historyList.isEmpty()) {
                    listOf(EmptyModel())
                } else {
                    historyList
                }
            }
        super.onDataLoaded(list, hasMore)
    }

    private fun getIsDownloadCommissionEnabled(): Boolean {
        return removeConfig?.getBoolean(REMOTE_CONFIG_KEY, false).orFalse()
    }
}