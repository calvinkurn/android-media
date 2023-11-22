package com.tokopedia.loyalty.router;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import rx.Observable;

public interface LoyaltyModuleRouter {

    Observable<VoucherViewModel> checkTrainVoucher(String reservationId, String reservationCode, String galaCode);
}