package com.tokopedia.tokocash.qrpayment.data.mapper;

import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class QrPaymentMapper implements Func1<QrPaymentEntity, QrPaymentTokoCash> {

    @Override
    public QrPaymentTokoCash call(QrPaymentEntity qrPaymentEntity) {
        if (qrPaymentEntity != null) {
            QrPaymentTokoCash qrPaymentTokoCash = new QrPaymentTokoCash();
            qrPaymentTokoCash.setTransactionId(qrPaymentEntity.getTransactionId());
            qrPaymentTokoCash.setAmount(qrPaymentEntity.getAmount());
            qrPaymentTokoCash.setDateTime(qrPaymentEntity.getDateTime());
            qrPaymentTokoCash.setLogo(qrPaymentEntity.getLogo());
            qrPaymentTokoCash.setPaymentId(qrPaymentEntity.getPaymentId());
            qrPaymentTokoCash.setStatus(qrPaymentEntity.getStatus());

            return qrPaymentTokoCash;
        }
        return null;
    }
}
