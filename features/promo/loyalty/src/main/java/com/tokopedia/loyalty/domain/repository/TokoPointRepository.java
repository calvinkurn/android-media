package com.tokopedia.loyalty.domain.repository;

import com.google.gson.Gson;
import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.apiservice.DigitalApi;
import com.tokopedia.loyalty.domain.apiservice.TXPaymentVoucherApi;
import com.tokopedia.loyalty.domain.apiservice.TokoPointApi;
import com.tokopedia.loyalty.domain.apiservice.TokoPointGqlApi;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.DigitalVoucherData;
import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.model.TkpdDigitalResponse;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.network.constant.ErrorNetMessage;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoPointRepository implements ITokoPointRepository {

    private final TokoPointResponseMapper tokoPointResponseMapper;
    private final ITokoPointDBService tokoPointDBService;
    private TokoPointGqlApi tokoPointGqlApi;
    private final DigitalApi digitalApi;
    private final TokoPointApi tokoPointApi;

    public TokoPointRepository(TokoPointGqlApi tokoPointGqlApi,
                               ITokoPointDBService tokoPointDBService,
                               TokoPointResponseMapper tokoPointResponseMapper,
                               TXPaymentVoucherApi txPaymentVoucherApi, DigitalApi digitalApi, TokoPointApi tokoPointApi) {
        this.tokoPointGqlApi = tokoPointGqlApi;
        this.tokoPointDBService = tokoPointDBService;
        this.tokoPointResponseMapper = tokoPointResponseMapper;
        this.digitalApi = digitalApi;
        this.tokoPointApi = tokoPointApi;
    }

    @Override
    public Observable<CouponsDataWrapper> getCouponList(Map<String, String> param) {
        return tokoPointApi.getCouponList(param)
                .map(new Func1<Response<TokoPointResponse>, CouponsDataWrapper>() {
                    @Override
                    public CouponsDataWrapper call(Response<TokoPointResponse> tokoplusResponseResponse) {
                        if (tokoplusResponseResponse.body() == null) {
                            throw new LoyaltyErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        } else if (tokoplusResponseResponse
                                .body()
                                .getTokoPointHeaderResponse()
                                .getErrorCode() != null) {
                            throw new LoyaltyErrorException(tokoplusResponseResponse
                                    .body().getTokoPointHeaderResponse().getMessageFormatted());
                        }
                        return tokoPointResponseMapper.convertCouponsDataWraper(
                                tokoplusResponseResponse.body().convertDataObj(
                                        CouponListDataResponse.class
                                )
                        );
                    }
                });
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

    @Override
    public Observable<VoucherViewModel> checkDigitalVoucherValidity(
            Map<String, String> param, final String voucherCode
    ) {
        return digitalApi.checkVoucher(param)
                .map(new Func1<Response<TkpdDigitalResponse>, VoucherViewModel>() {
                    @Override
                    public VoucherViewModel call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tokoPointResponseMapper.digtialVoucherViewModel(
                                tkpdDigitalResponseResponse.body().convertDataObj(DigitalVoucherData.class),
                                voucherCode
                        );
                    }
                });
    }

    @Override
    public Observable<CouponViewModel> checkDigitalCouponValidity(
            Map<String, String> param, final String voucherCode, final String couponTitle
    ) {
        return digitalApi.checkVoucher(param).
                map(new Func1<Response<TkpdDigitalResponse>, CouponViewModel>() {
                    @Override
                    public CouponViewModel call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tokoPointResponseMapper.digitalCouponViewModel(
                                tkpdDigitalResponseResponse.body().convertDataObj(DigitalVoucherData.class),
                                voucherCode,
                                couponTitle
                        );
                    }
                });
    }
}
