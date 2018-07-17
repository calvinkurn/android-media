package com.tokopedia.home.beranda.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.constants.TokoPointDrawerBroadcastReceiverConstant;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.feedplus.domain.usecase.GetHomeFeedsUseCase;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.PendingCashbackHomeSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.TokocashHomeSubscriber;
import com.tokopedia.home.beranda.presentation.view.subscriber.TokopointHomeSubscriber;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private static final String CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED";
    private final Context context;
    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    @Inject
    GetLocalHomeDataUseCase localHomeDataUseCase;
    @Inject
    GetHomeDataUseCase getHomeDataUseCase;
    @Inject
    GetHomeFeedsUseCase getHomeFeedsUseCase;

    private SessionHandler sessionHandler;
    private GetShopInfoRetrofit getShopInfoRetrofit;
    private String currentCursor = "";
    private PagingHandler pagingHandler;
    private HomeFeedListener feedListener;
    private HeaderViewModel headerViewModel;
    private boolean fetchFirstData;
    private long REQUEST_DELAY = 180000;// 3 minutes
    private static long lastRequestTime;

    public HomePresenter(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
        this.pagingHandler = new PagingHandler();
        resetPageFeed();
        sessionHandler = new SessionHandler(context);
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
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(refreshData())
                .onErrorResumeNext(getDataFromNetwork())
                .subscribe(createHomeDataSubscriber());
        compositeSubscription.add(subscription);
    }

    private void initHeaderViewModelData() {
        if (SessionHandler.isV4Login(context)) {
            if (headerViewModel == null) {
                headerViewModel = new HeaderViewModel();
            }
            headerViewModel.setPendingTokocashChecked(false);
        }
    }

    @NonNull
    private Action1<List<Visitable>> refreshData() {
        return new Action1<List<Visitable>>() {
            @Override
            public void call(List<Visitable> visitables) {
                compositeSubscription.add(getDataFromNetwork().subscribe(createHomeDataSubscriber()));
            }
        };
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
    public void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData) {
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
        if (!SessionHandler.isV4Login(context)) return;

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
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                if (isViewAttached()) {
                    try {
                        ShopModel shopModel = new Gson().fromJson(result, ShopModel.class);
                        if (shopModel.info != null) {
                            Intent intent = ((IHomeRouter) MainApplication.getAppContext()).getShopPageIntent(MainApplication.getAppContext(), shopModel.getInfo().getShopId());
                            context.startActivity(intent);
                        } else {
                            getView().openWebViewURL(url, context);
                        }
                    } catch (Exception e) {
                        getView().openWebViewURL(url, context);
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }

            @Override
            public void onFailure() {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    @Override
    public void openProductPageIfValid(final String url, String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                if (isViewAttached()) {
                    try {
                        ShopModel shopModel = new Gson().fromJson(result,
                                ShopModel.class);
                        if (shopModel.info != null) {
                            DeepLinkChecker.openProduct(url, context);
                        } else {
                            getView().openWebViewURL(url, context);
                        }
                    } catch (Exception e) {
                        getView().openWebViewURL(url, context);
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }

            @Override
            public void onFailure() {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void getHeaderData(boolean initialStart) {
        if (!SessionHandler.isV4Login(context)) return;

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

    public void setFeedListener(HomeFeedListener feedListener) {
        this.feedListener = feedListener;
    }

    public void resetPageFeed() {
        currentCursor = "";
        pagingHandler.setPage(0);
        if (getHomeFeedsUseCase != null) {
            getHomeFeedsUseCase.unsubscribe();
        }
    }

    public void fetchNextPageFeed() {
        pagingHandler.nextPage();
        fetchCurrentPageFeed();
    }

    public void fetchCurrentPageFeed() {
        if (currentCursor == null)
            return;
        getHomeFeedsUseCase.execute(
                getHomeFeedsUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        sessionHandler.getLoginID(),
                        currentCursor),
                new GetHomeFeedsSubscriber(feedListener, pagingHandler.getPage()));
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    public void setCursorNoNextPageFeed() {
        this.currentCursor = CURSOR_NO_NEXT_PAGE_FEED;
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
        return SessionHandler.isV4Login(context);
    }

    public void showNetworkError() {
        getView().showNetworkError(context.getString(R.string.msg_network_error));
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
                homePresenter.getView().showNetworkError(ErrorHandler.getErrorMessage(e));
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
            getHomeFeedsUseCase.unsubscribe();
        }

        if (getHomeFeedsUseCase != null) {
            getHomeFeedsUseCase.unsubscribe();
        }

        if (localHomeDataUseCase != null) {
            localHomeDataUseCase.unsubscribe();
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
}
