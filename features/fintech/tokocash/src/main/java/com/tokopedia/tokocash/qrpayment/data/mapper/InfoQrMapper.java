package com.tokopedia.tokocash.qrpayment.data.mapper;

import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class InfoQrMapper implements Func1<InfoQrEntity, InfoQrTokoCash> {

    @Override
    public InfoQrTokoCash call(InfoQrEntity infoQrEntity) {
        if (infoQrEntity != null) {
            InfoQrTokoCash infoQrTokoCash = new InfoQrTokoCash();
            infoQrTokoCash.setName(infoQrEntity.getName());
            infoQrTokoCash.setEmail(infoQrEntity.getEmail());
            infoQrTokoCash.setPhoneNumber(infoQrEntity.getPhoneNumber());
            infoQrTokoCash.setAmount(infoQrEntity.getAmount());

            return infoQrTokoCash;
        }
        return null;
    }
}
