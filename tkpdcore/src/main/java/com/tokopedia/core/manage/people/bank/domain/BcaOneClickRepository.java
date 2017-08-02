package com.tokopedia.core.manage.people.bank.domain;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.manage.people.bank.model.PaymentListModel;
import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public class BcaOneClickRepository implements IBcaOneClickRepository {

    private BcaOneClickService bcaOneClickService;

    public BcaOneClickRepository(BcaOneClickService bcaOneClickService) {
        this.bcaOneClickService = bcaOneClickService;
    }

    @Override
    public Observable<BcaOneClickData> getBcaOneClickAccessToken(
            TKPDMapParam<String, String> bcaOneClickParam
    ) {
        return bcaOneClickService.getApi().accessBcaOneClick(bcaOneClickParam)
                .map(new Func1<Response<TkpdResponse>, BcaOneClickData>() {
            @Override
            public BcaOneClickData call(Response<TkpdResponse> response) {
                return response.body().convertDataObj(BcaOneClickData.class);
            }
        });
    }

    @Override
    public Observable<PaymentListModel> getPaymentListUserData(TKPDMapParam<String, String> oneClickListParam) {
        return bcaOneClickService.getApi().getBcaOneClickUserData(oneClickListParam)
                .map(new Func1<Response<TkpdResponse>, PaymentListModel>() {
                    @Override
                    public PaymentListModel call(Response<TkpdResponse> response) {
                        return response.body().convertDataObj(PaymentListModel.class);
                    }
                });
    }

    @Override
    public Observable<PaymentListModel> deleteUserData(TKPDMapParam<String, String> oneClickListParam) {
        return bcaOneClickService.getApi().deleteBcaOneClickUserData(oneClickListParam)
                .map(new Func1<Response<TkpdResponse>, PaymentListModel>() {
                    @Override
                    public PaymentListModel call(Response<TkpdResponse> response) {
                        return response.body().convertDataObj(PaymentListModel.class);
                    }
                });
    }
}
