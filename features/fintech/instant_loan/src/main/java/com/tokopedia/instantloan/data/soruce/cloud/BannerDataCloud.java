package com.tokopedia.instantloan.data.soruce.cloud;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.instantloan.data.mapper.BannerMapper;
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer;
import com.tokopedia.instantloan.data.soruce.BannerDataStore;
import com.tokopedia.instantloan.data.soruce.cloud.api.InstantLoanApi;
import com.tokopedia.instantloan.domain.model.BannerModelDomain;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lavekush on 21/03/18.
 */

public class BannerDataCloud implements BannerDataStore {

    private final InstantLoanApi mApi;

    private BannerMapper bannerMapper;

    @Inject
    public BannerDataCloud(InstantLoanApi api,BannerMapper mapper) {
        this.mApi = api;
        this.bannerMapper = mapper;
    }

    @Override
    public Observable<List<BannerModelDomain>> bannerList() {
        return mApi.getBanners().map(new Func1<Response<ResponseBannerOffer>, List<BannerModelDomain>>() {
            @Override
            public List<BannerModelDomain> call(Response<ResponseBannerOffer> response) {
                if (response.isSuccessful()) {
                    ResponseBannerOffer responseBannerOffer = response.body();
                    return bannerMapper.transform(responseBannerOffer.getBanners());
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
