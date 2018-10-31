package com.tokopedia.loyalty.domain.repository;

import com.google.gson.Gson;
import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.apiservice.TXPaymentVoucherApi;
import com.tokopedia.loyalty.domain.apiservice.TokoPointGqlApi;
import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoPointRepository implements ITokoPointRepository {

    private final TokoPointResponseMapper tokoPointResponseMapper;
    private TXPaymentVoucherApi txPaymentVoucherApi;
    private final ITokoPointDBService tokoPointDBService;
    private TokoPointGqlApi tokoPointGqlApi;

    public TokoPointRepository(TokoPointGqlApi tokoPointGqlApi,
                               ITokoPointDBService tokoPointDBService,
                               TokoPointResponseMapper tokoPointResponseMapper,
                               TXPaymentVoucherApi txPaymentVoucherApi) {
        this.tokoPointGqlApi = tokoPointGqlApi;
        this.tokoPointDBService = tokoPointDBService;
        this.tokoPointResponseMapper = tokoPointResponseMapper;
        this.txPaymentVoucherApi = txPaymentVoucherApi;
    }

    @Override
    public Observable<TokoPointDrawerData> getPointDrawer(final String query) {
        return tokoPointDBService.getPointDrawer().map(new Func1<HachikoDrawerDataResponse, TokoPointDrawerData>() {
            @Override
            public TokoPointDrawerData call(HachikoDrawerDataResponse tokoPointDrawerDataResponse) {
                return tokoPointResponseMapper.convertTokoplusPointDrawer(
                        tokoPointDrawerDataResponse
                );
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends TokoPointDrawerData>>() {
            @Override
            public Observable<? extends TokoPointDrawerData> call(Throwable throwable) {
                throwable.printStackTrace();
                return tokoPointGqlApi.getPointDrawer(query)
                        .flatMap(new Func1<Response<String>, Observable<HachikoDrawerDataResponse>>() {
                            @Override
                            public Observable<HachikoDrawerDataResponse> call(Response<String> tokoPointResponseResponse) {
                                GqlTokoPointResponse gqlTokoPointResponse = new Gson().fromJson(tokoPointResponseResponse.body(), GqlTokoPointResponse.class);

                                return tokoPointDBService.storePointDrawer(gqlTokoPointResponse.getHachikoDrawerDataResponse());
                            }
                        }).map(new Func1<HachikoDrawerDataResponse, TokoPointDrawerData>() {
                            @Override
                            public TokoPointDrawerData call(HachikoDrawerDataResponse gqlTokoPointDrawerDataResponse) {
                                return tokoPointResponseMapper.convertTokoplusPointDrawer(
                                        gqlTokoPointDrawerDataResponse
                                );
                            }
                        });
            }
        });

    }
}
