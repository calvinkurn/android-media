package com.tokopedia.home.beranda.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.domain.interactor.GetFeedTabUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetFeedTabsSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.PendingCashbackHomeSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.TokocashHomeSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.TokopointHomeSubscriber;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.usecase.RequestParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 *
 * TODO : Remove context
 * TODO : Remove Old GetShopInfoRetrofit
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private static final String CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED";
    private UserSession userSession;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    @Inject
    GetLocalHomeDataUseCase localHomeDataUseCase;
    @Inject
    GetHomeDataUseCase getHomeDataUseCase;
    @Inject
    GetFeedTabUseCase getFeedTabUseCase;

    private String currentCursor = "";
    private GetShopInfoByDomainUseCase getShopInfoByDomainUseCase;
    private HeaderViewModel headerViewModel;
    private boolean fetchFirstData;
    private long REQUEST_DELAY = 180000;// 3 minutes
    private static long lastRequestTime;

    public HomePresenter(UserSession userSession,
                         GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        this.userSession = userSession;
        this.getShopInfoByDomainUseCase = getShopInfoByDomainUseCase;

        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
        resetPageFeed();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @NonNull
    private Observable<List<Visitable>> getDataFromNetwork() {
        return getHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onFirstLaunch() {
        this.fetchFirstData = true;
    }

    @Override
    public void onResume() {
        boolean needRefresh = (lastRequestTime + REQUEST_DELAY < System.currentTimeMillis());
        if (isViewAttached() && !this.fetchFirstData && needRefresh) {
            updateHomeData();
        }
        getTokocashBalance();
    }

    @Override
    public void updateHomeData() {
        subscription = getHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Visitable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Visitable> visitables) {
                        if (isViewAttached()) {
                            getView().updateListOnResume(visitables);
                        }
                        lastRequestTime = System.currentTimeMillis();
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getHomeData() {
        initHeaderViewModelData();
        subscription = localHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(visitables ->
                        compositeSubscription.add(getDataFromNetwork().subscribe(createHomeDataSubscriber())))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(getDataFromNetwork())
                .subscribe(createHomeDataSubscriber());
        compositeSubscription.add(subscription);
    }

    private void initHeaderViewModelData() {
        if(userSession.isLoggedIn()){
            if (headerViewModel == null) {
                headerViewModel = new HeaderViewModel();
            }
            headerViewModel.setPendingTokocashChecked(false);
        }
    }


    private HomeDataSubscriber createHomeDataSubscriber() {
        return new HomeDataSubscriber(this);
    }

    @Override
    public void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletAction) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setHomeHeaderWalletActionData(homeHeaderWalletAction);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void showPopUpIntroWalletOvo(String applinkActivation) {
        getView().showPopupIntroOvo(applinkActivation);
    }

    @Override
    public void onHeaderTokocashError() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }

        headerViewModel.setWalletDataError();
        headerViewModel.setHomeHeaderWalletActionData(null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoCashPendingData(CashBackData cashBackData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setCashBackData(cashBackData);
        headerViewModel.setPendingTokocashChecked(true);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoPointData(TokopointHomeDrawerData tokoPointDrawerData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();
        headerViewModel.setTokoPointDrawerData(tokoPointDrawerData);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onHeaderTokopointError() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataError();
        headerViewModel.setTokoPointDrawerData(null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onRefreshTokoPoint() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();
        headerViewModel.setTokoPointDrawerData(null);
        getView().updateHeaderItem(headerViewModel);

        getTokopoint();
    }

    @Override
    public void onRefreshTokoCash() {
        if (!userSession.isLoggedIn()) return;

        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setHomeHeaderWalletActionData(null);
        getView().updateHeaderItem(headerViewModel);

        getTokocashBalance();
    }

    @Override
    public void getShopInfo(final String url, String shopDomain) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                if(shopInfo.getInfo() != null) {
                    getView().startShopInfo(shopInfo.getInfo().getShopId());
                }else {
                    getView().openWebViewURL(url);
                }
            }
        });
    }

    @Override
    public void openProductPageIfValid(final String url, String shopDomain) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                if(shopInfo.getInfo() != null) {
                    getView().startDeeplinkShopInfo(url);
                }else {
                    getView().openWebViewURL(url);
                }
            }
        });
    }

    public void getHeaderData(boolean initialStart) {
        if (!userSession.isLoggedIn()) return;

        if (initialStart && headerViewModel != null) {
            if (headerViewModel.getHomeHeaderWalletActionData() == null)
                getTokocashBalance();
            if (headerViewModel.getTokoPointDrawerData() == null)
                getTokopoint();
        } else {
            getTokocashBalance();
            getTokopoint();
        }
    }

    public void resetPageFeed() {
        //TODO will be implemented
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    public boolean hasNextPageFeed() {
        return !CURSOR_NO_NEXT_PAGE_FEED.equals(currentCursor);
    }

    public HeaderViewModel getHeaderViewModel() {
        return headerViewModel;
    }

    public void setFetchFirstData(boolean fetchFirstData) {
        this.fetchFirstData = fetchFirstData;
    }

    public boolean isLogin() {
        return userSession.isLoggedIn();
    }

    public void showNetworkError() {
        getView().showNetworkError();
    }

    private static class HomeDataSubscriber extends Subscriber<List<Visitable>> {

        HomePresenter homePresenter;

        public HomeDataSubscriber(HomePresenter homePresenter) {
            this.homePresenter = homePresenter;
        }

        @Override
        public void onStart() {
            if (homePresenter != null && homePresenter.isViewAttached()) {
                homePresenter.getView().showLoading();
            }
        }

        @Override
        public void onCompleted() {
            if (homePresenter != null && homePresenter.isViewAttached()) {
                homePresenter.getView().hideLoading();
                homePresenter.setFetchFirstData(false);
                homePresenter = null;
            }
        }

        @Override
        public void onError(Throwable e) {
            if (homePresenter != null && homePresenter.isViewAttached()) {
                homePresenter.getView().showNetworkError(ErrorHandler.getErrorMessage(
                        homePresenter.getView().getContext(),e));
                onCompleted();
            }
        }

        @Override
        public void onNext(List<Visitable> visitables) {
            if (homePresenter != null && homePresenter.isViewAttached()) {
                if (homePresenter.isLogin() && homePresenter.getHeaderViewModel() != null) {
                    visitables.add(0, homePresenter.getHeaderViewModel());
                }
                homePresenter.getView().setItems(visitables);
                if (visitables.size() > 0) {
                    homePresenter.getView().showRecomendationButton();
                }
                if (homePresenter.isDataValid(visitables)) {
                    homePresenter.getView().removeNetworkError();
                } else {
                    homePresenter.showNetworkError();
                }
            }
            lastRequestTime = System.currentTimeMillis();
        }

    }

    private boolean isDataValid(List<Visitable> visitables) {
        return containsInstance(visitables, BannerViewModel.class);
    }

    public <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        unsubscribeAllUseCase();
    }

    private void unsubscribeAllUseCase() {
        if (getHomeDataUseCase != null) {
            getHomeDataUseCase.unsubscribe();
        }

        if (localHomeDataUseCase != null) {
            localHomeDataUseCase.unsubscribe();
        }

        if (getFeedTabUseCase != null) {
            getFeedTabUseCase.unsubscribe();
        }

        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    /**
     * Tokocash & Tokopoint
     */
    private void getTokocashBalance() {
        if (getView().getTokocashBalance() != null) {
            compositeSubscription.add(getView().getTokocashBalance().subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new TokocashHomeSubscriber(this)));
        }
    }

    public void getTokocashPendingBalance() {
        if (getView().getTokocashPendingCashback() != null) {
            compositeSubscription.add(getView().getTokocashPendingCashback().subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new PendingCashbackHomeSubscriber(this)));
        }
    }

    public void getTokopoint() {
        if (getView().getTokopoint() != null) {
            compositeSubscription.add(getView().getTokopoint().subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new TokopointHomeSubscriber(this)));
        }
    }

    @Override
    public void hitBannerImpression(BannerSlidesModel slidesModel) {
        if (!slidesModel.isImpressed()
                && slidesModel.getTopadsViewUrl()!=null
                && !slidesModel.getTopadsViewUrl().isEmpty()) {
            compositeSubscription.add(Observable.just(new ImpresionTask(new ImpressionListener() {
                @Override
                public void onSuccess() {
                    slidesModel.setImpressed(true);
                }

                @Override
                public void onFailed() {
                    slidesModel.setImpressed(false);
                }
            }).execute(slidesModel.getTopadsViewUrl()))
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
        }
    }

    @Override
    public void onBannerClicked(BannerSlidesModel slidesModel) {
        if(slidesModel.getRedirectUrl()!=null && !slidesModel.getRedirectUrl().isEmpty()) {
            new ImpresionTask().execute(slidesModel.getRedirectUrl());
        }
    }

    @Override
    public void getFeedTabData() {
        getFeedTabUseCase.execute(new GetFeedTabsSubscriber(getView()));
    }
}
