package com.tokopedia.loyalty.router;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkTrainVoucher(String reservationId, String reservationCode, String galaCode);

    Observable<TKPDMapParam<String, Object>> verifyEventPromo(RequestParams requestParams);

    Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams);

    void actionOpenGeneralWebView(Activity activity, String url);

    void sendEventCouponChosen(Context context, String title);

    void sendEventDigitalEventTracking(Context context, String text, String failmsg);
}
