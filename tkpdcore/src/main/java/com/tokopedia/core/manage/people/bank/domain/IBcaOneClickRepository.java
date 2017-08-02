package com.tokopedia.core.manage.people.bank.domain;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.manage.people.bank.model.PaymentListModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface IBcaOneClickRepository {

    Observable<BcaOneClickData> getBcaOneClickAccessToken(
            TKPDMapParam<String, String> bcaOneClickParam
    );

    Observable<PaymentListModel> getPaymentListUserData(
            TKPDMapParam<String, String> oneClickListParam
    );

    Observable<PaymentListModel> deleteUserData(
            TKPDMapParam<String, String> oneClickListParam
    );

}
