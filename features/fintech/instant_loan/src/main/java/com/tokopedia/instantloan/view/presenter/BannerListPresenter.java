package com.tokopedia.instantloan.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer;
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase;
import com.tokopedia.instantloan.view.contractor.BannerContractor;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class BannerListPresenter extends BaseDaggerPresenter<BannerContractor.View>
        implements BannerContractor.Presenter {

    private GetBannersUserCase mGetBannersUserCase;

    @Inject
    public BannerListPresenter(GetBannersUserCase getBannersUserCase) {
        this.mGetBannersUserCase = getBannersUserCase;
    }

    @Override
    public void loadBanners() {

        mGetBannersUserCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse restResponse = typeRestResponseMap.get(ResponseBannerOffer.class);
                ResponseBannerOffer responseBannerOffer = restResponse.getData();
                getView().renderUserList(responseBannerOffer.getBanners());
            }
        });
    }
}
