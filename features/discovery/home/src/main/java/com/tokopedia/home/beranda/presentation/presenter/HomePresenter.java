package com.tokopedia.home.beranda.presentation.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.beranda.data.model.KeywordSearchData;
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData;
import com.tokopedia.home.beranda.domain.interactor.*;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.*;
import com.tokopedia.home.beranda.presentation.view.subscriber.*;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 * <p>
 * TODO : Remove context
 * TODO : Remove Old GetShopInfoRetrofit
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private static final String CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED";

    private UserSessionInterface userSession;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    @Inject
    GetLocalHomeDataUseCase localHomeDataUseCase;
    @Inject
    GetHomeDataUseCase getHomeDataUseCase;
    @Inject
    GetFeedTabUseCase getFeedTabUseCase;

    @Inject
    Lazy<GetHomeTokopointsDataUseCase> getHomeTokopointsDataUseCaseLazy;

    @Inject
    Lazy<GetKeywordSearchUseCase> getKeywordSearchUseCaseLazy;


    private String currentCursor = "";
    private GetShopInfoByDomainUseCase getShopInfoByDomainUseCase;
    private HeaderViewModel headerViewModel;
    private boolean fetchFirstData;
    private long REQUEST_DELAY = 180000;// 3 minutes
    private static long lastRequestTime;

    public HomePresenter(UserSessionInterface userSession,
                         GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        this.userSession = userSession;
        this.getShopInfoByDomainUseCase = getShopInfoByDomainUseCase;

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

    @NonNull
    private Observable<List<TrackedVisitable>> getDataFromNetwork() {
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
        getTokopoint();
        getSearhHint();
    }

    @Override
    public void updateHomeData() {
        subscription = getHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TrackedVisitable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TrackedVisitable> visitables) {
                        if (isViewAttached()) {
                            getView().updateListOnResume(new ArrayList<>(visitables));
                            getView().addImpressionToTrackingQueue(visitables);
                        }
                        lastRequestTime = System.currentTimeMillis();
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getHomeData() {
        initHeaderViewModelData();
        HomeDataSubscriber homeLocalSubscriber = createHomeDataSubscriber();
        homeLocalSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_CACHE);
        subscription = localHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(visitables ->
                {
                    HomeDataSubscriber homeNetworkSubscriber = createHomeDataSubscriber();
                    homeNetworkSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_NETWORK);
                    compositeSubscription.add(getDataFromNetwork().subscribe(homeNetworkSubscriber));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    homeLocalSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_NETWORK);
                    return getDataFromNetwork();
                })
                .subscribe(homeLocalSubscriber);
        compositeSubscription.add(subscription);
    }

    private void initHeaderViewModelData() {
        if (userSession.isLoggedIn()) {
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
    public void onHeaderTokopointError() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataError();
        headerViewModel.setTokoPointDrawerData(null);
        headerViewModel.setTokopointsDrawerHomeData(null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onRefreshTokoPoint() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();
        headerViewModel.setTokoPointDrawerData(null);
        headerViewModel.setTokopointsDrawerHomeData(null);
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
                if (shopInfo.getInfo() != null) {
                    getView().startShopInfo(shopInfo.getInfo().getShopId());
                } else {
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
                if (shopInfo.getInfo() != null) {
                    getView().startDeeplinkShopInfo(url);
                } else {
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
            if (headerViewModel.getTokopointsDrawerHomeData() == null)
                getTokopoint();
        } else {
            getTokocashBalance();
            getTokopoint();
        }
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    public boolean hasNextPageFeed() {
        return !CURSOR_NO_NEXT_PAGE_FEED.equals(currentCursor);
    }

    public HeaderViewModel getHeaderViewModel() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setUserLogin(userSession.isLoggedIn());
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

    public static class HomeDataSubscriber extends Subscriber<List<TrackedVisitable>> {
        public static int FLAG_FROM_NETWORK = 99;
        public static int FLAG_FROM_CACHE = 98;
        private int repositoryFlag;

        HomePresenter homePresenter;

        public HomeDataSubscriber(HomePresenter homePresenter) {
            this.homePresenter = homePresenter;
        }

        public void setFlag(int flag) {
            this.repositoryFlag = flag;
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
                        homePresenter.getView().getContext(), e));
                onCompleted();
            }
        }

        @Override
        public void onNext(List<TrackedVisitable> visitables) {
            if (homePresenter != null && homePresenter.isViewAttached()) {
//                if(visitables.get(visitables.size() - 1) instanceof SearchPlaceholderViewModel){
//                    homePresenter.getView().setHint(((SearchPlaceholderViewModel) visitables.get(visitables.size() - 1)).getSearchPlaceholder());
//                    visitables.remove(visitables.size() - 1);
//                }

                if (homePresenter.getHeaderViewModel() != null && visitables.size() > 1) {
                    if (visitables.get(1) instanceof TickerViewModel) {
                        visitables.add(2, homePresenter.getHeaderViewModel());
                    } else {
                        visitables.add(1, homePresenter.getHeaderViewModel());
                    }
                }
                homePresenter.getView().setItems(new ArrayList<>(visitables), repositoryFlag);
                homePresenter.getView().addImpressionToTrackingQueue(visitables);
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

    private boolean isDataValid(List<TrackedVisitable> visitables) {
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
        Observable<GraphqlResponse> graphqlResponseObservable = getTokopointsObservable();
        if (graphqlResponseObservable != null) {
            compositeSubscription.add(graphqlResponseObservable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new TokopointHomeSubscriber(this)));
        }
    }

    public void getSearhHint(){
        Observable<GraphqlResponse> graphqlResponseObservable = getKeywordSearchObservable();
        if (graphqlResponseObservable != null) {
            compositeSubscription.add(graphqlResponseObservable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new KeywordSearchHomeSubscriber(this)));
        }
    }

    private Observable<GraphqlResponse> getTokopointsObservable() {
        if (getHomeTokopointsDataUseCaseLazy != null) {
            GetHomeTokopointsDataUseCase getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCaseLazy.get();
            getHomeTokopointsDataUseCase.clearRequest();
            getHomeTokopointsDataUseCase.addRequest(getHomeTokopointsDataUseCase.getRequest());
            return getHomeTokopointsDataUseCase.getExecuteObservable(RequestParams.EMPTY);
        }
        return null;
    }

    private Observable<GraphqlResponse> getKeywordSearchObservable(){
        if (getHomeTokopointsDataUseCaseLazy != null) {
            GetKeywordSearchUseCase getKeywordSearchUseCase = getKeywordSearchUseCaseLazy.get();
            getKeywordSearchUseCase.clearRequest();
            getKeywordSearchUseCase.addRequest(getKeywordSearchUseCase.getRequest());
            return getKeywordSearchUseCase.getExecuteObservable(RequestParams.EMPTY);
        }
        return null;
    }

    @Override
    public void hitBannerImpression(BannerSlidesModel slidesModel) {
        if (!slidesModel.isImpressed()
                && slidesModel.getTopadsViewUrl() != null
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
        if (slidesModel.getRedirectUrl() != null && !slidesModel.getRedirectUrl().isEmpty()) {
            new ImpresionTask().execute(slidesModel.getRedirectUrl());
        }
    }

    @Override
    public void updateHeaderTokoPointData(TokopointsDrawerHomeData tokopointsDrawerHomeData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();

        headerViewModel.setTokopointsDrawerHomeData(tokopointsDrawerHomeData != null ? tokopointsDrawerHomeData.getTokopointsDrawer() : null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateKeywordSearch(KeywordSearchData keywordSearchData) {
        getView().setHint(keywordSearchData.getSearchData());
    }

    public void getFeedTabData() {
        getFeedTabUseCase.execute(new GetFeedTabsSubscriber(getView()));
    }

    private Visitable mappingHomeFeedModel(List<FeedTabModel> feedTabModelList) {
        HomeRecommendationFeedViewModel feedViewModel = new HomeRecommendationFeedViewModel();
        feedViewModel.setFeedTabModel(feedTabModelList);
        return feedViewModel;
    }
}
