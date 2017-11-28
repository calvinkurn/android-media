package com.tokopedia.tkpd.beranda.presentation.presenter;

import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.interactor.GetBrandsOfficialStoreUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeBannerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeCategoryUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTickerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTopPicksUseCase;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutSectionsModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func5;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    @Inject
    GetHomeBannerUseCase getHomeBannerUseCase;
    @Inject
    GetTickerUseCase getTickerUseCase;
    @Inject
    GetBrandsOfficialStoreUseCase getBrandsOfficialStoreUseCase;
    @Inject
    GetTopPicksUseCase getTopPicksUseCase;
    @Inject
    GetHomeCategoryUseCase getHomeCategoryUseCase;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;

    public HomePresenter() {
        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getHomeData() {
        subscription = Observable.zip(getHomeBannerUseCase.getExecuteObservableAsync(getHomeBannerUseCase.getRequestParam()),
                getTickerUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getBrandsOfficialStoreUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getTopPicksUseCase.getExecuteObservableAsync(getTopPicksUseCase.getRequestParam()),
                getHomeCategoryUseCase.getExecuteObservableAsync(RequestParams.EMPTY), new HomeDataMapper())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "On Subscribe");
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        Log.w(TAG, "On Terminated");
                    }
                }).subscribe(new HomeDataSubscriber());
        compositeSubscription.add(subscription);
    }

    private class HomeDataSubscriber extends Subscriber<List<Visitable>> {

        @Override
        public void onStart() {
            if (isViewAttached()) {
                getView().removeNetworkError();
                getView().showLoading();
            }
        }

        @Override
        public void onCompleted() {
            if (isViewAttached()) {
                getView().hideLoading();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showNetworkError();
            }
        }

        @Override
        public void onNext(List<Visitable> visitables) {
            Log.d(TAG, "onNext items " + visitables.size());
        }
    }

    private class HomeDataMapper implements Func5<HomeBannerResponseModel, Ticker,
            BrandsOfficialStoreResponseModel, TopPicksResponseModel,
            HomeCategoryResponseModel, List<Visitable>> {
        @Override
        public List<Visitable> call(HomeBannerResponseModel homeBannerResponseModel, Ticker ticker,
                                    BrandsOfficialStoreResponseModel brandsOfficialStoreResponseModel,
                                    TopPicksResponseModel topPicksResponseModel,
                                    HomeCategoryResponseModel homeCategoryResponseModel) {
            List<Visitable> list = new ArrayList<>();
            if (homeBannerResponseModel.isSuccess()) {
                list.add(mappingBanner(homeBannerResponseModel));
            }
            if (ticker.getData().getTickers().size() > 0) {
                list.add(mappingTicker(ticker.getData().getTickers()));
            }
            if(brandsOfficialStoreResponseModel.isSuccess()){
                list.add(mappingBrandsOs(brandsOfficialStoreResponseModel.getData()));
            }
            if(homeCategoryResponseModel.isSuccess()){
                list.addAll(mappingCategory(homeCategoryResponseModel.getData().getLayoutSections()));
            }
            return list;
        }
    }

    private List<Visitable> mappingCategory(List<CategoryLayoutSectionsModel> layoutSections) {
        List<Visitable> list = new ArrayList<>();
        for (CategoryLayoutSectionsModel sections : layoutSections) {
            if(sections.getId() == 22){ //Id 22 == Digitals
                list.add(new DigitalsViewModel());
            } else {
                CategoryItemViewModel viewModel = new CategoryItemViewModel();
                viewModel.setTitle(sections.getTitle());
                viewModel.setItemList(sections.getLayoutRows());
                list.add(viewModel);
            }
        }
        return list;
    }

    private Visitable mappingBrandsOs(List<BrandDataModel> data) {
        BrandsViewModel viewModel = new BrandsViewModel();
        viewModel.setTitle(getView().getString(R.string.title_home_official_store));
        viewModel.setData(data);
        return viewModel;
    }

    private Visitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private Visitable mappingBanner(HomeBannerResponseModel homeBannerResponseModel) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(homeBannerResponseModel.getData().getSlides());
        return viewModel;
    }
}
