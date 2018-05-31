package com.tokopedia.instantloan.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase;
import com.tokopedia.instantloan.domain.model.BannerModelDomain;
import com.tokopedia.instantloan.view.contractor.BannerContractor;
import com.tokopedia.instantloan.view.mapper.BannerMapper;
import com.tokopedia.instantloan.view.model.BannerViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lavekush on 22/03/18.
 */

public class  BannerListPresenter extends BaseDaggerPresenter<BannerContractor.View>
        implements BannerContractor.Presenter {

    private GetBannersUserCase mGetBannersUserCase;
    private BannerMapper mMapper;

    @Inject
    public BannerListPresenter(GetBannersUserCase getBannersUserCase, com.tokopedia.instantloan.view.mapper.BannerMapper bannerMapper) {
        this.mGetBannersUserCase = getBannersUserCase;
        this.mMapper = bannerMapper;
    }

    @Override
    public void loadBanners() {
        mGetBannersUserCase.getExecuteObservable(RequestParams.EMPTY).map(new Func1<List<BannerModelDomain>, List<BannerViewModel>>() {
            @Override
            public List<BannerViewModel> call(List<BannerModelDomain> bannerDomains) {
                return mMapper.transform(bannerDomains);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BannerViewModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BannerViewModel> bannerViewModels) {
                        getView().renderUserList(bannerViewModels);
                    }
                });
    }
}
