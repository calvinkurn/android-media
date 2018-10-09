package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.network.api.WalletApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class QrPaymentDataSourceFactory {

    private WalletApi walletApi;

    @Inject
    public QrPaymentDataSourceFactory(WalletApi walletApi) {
        this.walletApi = walletApi;
    }

    private QrPaymentDataSource createQrPaymentDataSource() {
        return new QrPaymentCloudDataSource(walletApi);
    }

    public QrPaymentDataSource create() {
        return createQrPaymentDataSource();
    }
}
