package com.tokopedia.loyalty.domain.usecase;

import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 24/07/18.
 */
public class TrainCheckVoucherUseCase extends UseCase<VoucherViewModel> {

    private final String PARAM_TRAIN_RESERVATION_ID = "PARAM_TRAIN_RESERVATION_ID";
    private final String PARAM_TRAIN_RESERVATION_CODE = "PARAM_RESERVATION_CODE";
    private final String PARAM_GALA_CODE = "PARAM_GALA_CODE";

    private LoyaltyModuleRouter loyaltyModuleRouter;

    public TrainCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        this.loyaltyModuleRouter = loyaltyModuleRouter;
    }

    @Override
    public Observable<VoucherViewModel> createObservable(RequestParams requestParams) {
        return loyaltyModuleRouter.checkTrainVoucher(
                requestParams.getString(PARAM_TRAIN_RESERVATION_ID, ""),
                requestParams.getString(PARAM_TRAIN_RESERVATION_CODE, ""),
                requestParams.getString(PARAM_GALA_CODE, "")
        );
    }

    public RequestParams createVoucherRequest(String reservationId, String reservationCode, String galaCode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_TRAIN_RESERVATION_ID, reservationId);
        requestParams.putString(PARAM_TRAIN_RESERVATION_CODE, reservationCode);
        requestParams.putString(PARAM_GALA_CODE, galaCode);
        return requestParams;
    }

}
