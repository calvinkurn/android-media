package com.tokopedia.transactiondata.repository;


import com.tokopedia.transactiondata.entity.response.thankstoppaydata.ThanksTopPayData;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public interface ITopPayRepository {

    Observable<ThanksTopPayData> getThanksTopPay(Map<String, String> param);
}
