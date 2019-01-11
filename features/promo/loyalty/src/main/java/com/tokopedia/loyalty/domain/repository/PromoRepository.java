package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.apiservice.PromoApi;
import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoRepository implements IPromoRepository {

    private final PromoApi promoApi;
    private final IPromoResponseMapper mapper;

    @Inject
    public PromoRepository(PromoApi promoApi, IPromoResponseMapper mapper) {
        this.promoApi = promoApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<PromoMenuData>> getPromoMenuDataList(final TKPDMapParam<String, String> param) {
        return promoApi.getMenuIndexList(param).map(
                new Func1<Response<List<MenuPromoResponse>>, List<PromoMenuData>>() {
                    @Override
                    public List<PromoMenuData> call(Response<List<MenuPromoResponse>> listResponse) {
                        return mapper.convertPromoMenuDataList(listResponse.body());
                    }
                });
    }

    @Override
    public Observable<List<PromoData>> getPromoDataList(final TKPDMapParam<String, String> param) {
        return promoApi.getPromoList(param).map(
                new Func1<Response<List<PromoResponse>>, List<PromoData>>() {
                    @Override
                    public List<PromoData> call(Response<List<PromoResponse>> listResponse) {
                        return mapper.convertPromoDataList(listResponse.body());
                    }
                }
        );
    }
}
