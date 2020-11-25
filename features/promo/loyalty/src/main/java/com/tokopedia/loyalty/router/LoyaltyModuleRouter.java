package com.tokopedia.loyalty.router;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkTrainVoucher(String reservationId, String reservationCode, String galaCode);

    Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams);
}