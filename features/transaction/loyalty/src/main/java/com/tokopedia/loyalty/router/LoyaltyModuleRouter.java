package com.tokopedia.loyalty.router;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkFlightVoucher(String voucherCode, String cartId, String isCoupon);

    Observable<VoucherViewModel> checkTrainVoucher(String reservationId, String reservationCode, String galaCode);

    Observable<TKPDMapParam<String, Object>> verifyEventPromo(RequestParams requestParams);

    Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams);

    void trainSendTrackingOnClickUseVoucherCode(String voucherCode);

    void trainSendTrackingOnCheckVoucherCodeError(String errorMessage);

    void sendEventTracking(String event, String category, String action, String label);

    void sharePromoLoyalty(Activity activity, PromoData promoData);

    void actionOpenGeneralWebView(Activity activity, String url);

    void sendEventImpressionPromoList(List<Object> dataLayerSinglePromoCodeList, String title);

    void eventClickPromoListItem(List<Object> dataLayerSinglePromoCodeList, String title);
}
