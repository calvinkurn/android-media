package com.tokopedia.tkpd.beranda.presentation.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.constants.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.core.constants.TokoPointDrawerBroadcastReceiverConstant;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.activity.DigitalProductActivity;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.data.mapper.HomeDataMapper;
import com.tokopedia.tkpd.beranda.domain.interactor.GetBrandsOfficialStoreUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeBannerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeCategoryUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTickerUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTopPicksUseCase;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.presentation.view.HomeContract;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;


import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
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
    @Inject
    GetLocalHomeDataUseCase localHomeDataUseCase;
    @Inject
    HomeDataMapper homeDataMapper;

    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    private final Context context;
    private GetShopInfoRetrofit getShopInfoRetrofit;

    private HeaderViewModel headerViewModel;

    public HomePresenter(Context context) {
        this.context = context;
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
    private Observable<List<Visitable>> getDataFromNetwork() {
        return Observable.zip(getHomeBannerUseCase.getExecuteObservableAsync(getHomeBannerUseCase.getRequestParam()),
                getTickerUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getBrandsOfficialStoreUseCase.getExecuteObservableAsync(RequestParams.EMPTY),
                getTopPicksUseCase.getExecuteObservableAsync(getTopPicksUseCase.getRequestParam()),
                getHomeCategoryUseCase.getExecuteObservableAsync(RequestParams.EMPTY), homeDataMapper);
    }

    @Override
    public void getHomeData() {
        subscription = localHomeDataUseCase.getExecuteObservableAsync(RequestParams.EMPTY)
                .doOnNext(refreshData())
                .onErrorResumeNext(getDataFromNetwork())
                .subscribe(new HomeDataSubscriber());
        compositeSubscription.add(subscription);
    }

    @NonNull
    private Action1<List<Visitable>> refreshData() {
        return new Action1<List<Visitable>>() {
            @Override
            public void call(List<Visitable> visitables) {
                compositeSubscription.add(getDataFromNetwork().subscribe(new HomeDataSubscriber()));
            }
        };
    }

    @Override
    public void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletAction) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
            headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_ONLY);
        } else {
            if (headerViewModel.getTokoPointDrawerData() != null) {
                headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_WITH_TOKOPOINT);
            } else {
                headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_ONLY);
            }
            headerViewModel.setHomeHeaderWalletActionData(homeHeaderWalletAction);
        }
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoCashPendingData(CashBackData cashBackData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
            headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_ONLY);
        } else {
            headerViewModel.setCashBackData(cashBackData);
            headerViewModel.setPendingTokocashChecked(true);
        }
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
            headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_WITH_TOKOPOINT);
        } else {
            headerViewModel.setTokoPointDrawerData(tokoPointDrawerData);
        }
        getView().updateHeaderItem(headerViewModel);
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
                            Bundle bundle = ShopInfoActivity.createBundle(
                                    shopModel.getInfo().getShopId(), shopModel.getInfo().getShopDomain());
                            Intent intent = new Intent(context, ShopInfoActivity.class);
                            intent.putExtras(bundle);
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
    public void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition) {
        Activity activity = getView().getActivity();
        if (activity != null && !activity.isFinishing()) {
            UnifyTracking.eventClickCategoriesIcon(data.getName());
            if (String.valueOf(data.getCategoryId()).equalsIgnoreCase("103")
                    && headerViewModel.getHomeHeaderWalletActionData() != null
                    && headerViewModel.getHomeHeaderWalletActionData().getTypeAction()
                    == HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION) {
                WalletRouterUtil.navigateWallet(activity.getApplication(), this,
                        IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                        headerViewModel.getHomeHeaderWalletActionData().getAppLinkActionButton() == null ? ""
                                : headerViewModel.getHomeHeaderWalletActionData().getAppLinkActionButton(),
                        headerViewModel.getHomeHeaderWalletActionData().getRedirectUrlActionButton() == null ? ""
                                : headerViewModel.getHomeHeaderWalletActionData().getRedirectUrlActionButton(),
                        new Bundle()
                );
            } else {
                if (((TkpdCoreRouter) activity.getApplication())
                        .isSupportedDelegateDeepLink(data.getApplinks())) {
                    DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                            .appLinks(data.getApplinks())
                            .categoryId(String.valueOf(data.getCategoryId()))
                            .categoryName(data.getName())
                            .url(data.getUrl())
                            .build();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
                    Intent intent = new Intent(activity, DeeplinkHandlerActivity.class);
                    intent.putExtras(bundle);
                    intent.setData(Uri.parse(data.getApplinks()));
                    activity.startActivity(intent);
                } else {
                    getView().onGimickItemClicked(data, parentPosition, childPosition);
                }
            }
            TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
        }
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

    private class HomeDataSubscriber extends Subscriber<List<Visitable>> {

        public HomeDataSubscriber() {
            if (SessionHandler.isV4Login(context)) {
                if (headerViewModel == null) {
                    headerViewModel = new HeaderViewModel();
                    headerViewModel.setType(HeaderViewModel.TYPE_TOKOCASH_WITH_TOKOPOINT);
                }
                headerViewModel.setPendingTokocashChecked(false);
                sendBroadcastGetHeaderData();
            }
        }

        @Override
        public void onStart() {
            if (isViewAttached()) {
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
                getView().showNetworkError(ErrorHandler.getErrorMessage(e));
                onCompleted();
            }
        }

        @Override
        public void onNext(List<Visitable> visitables) {
            if (isViewAttached()) {
                if(visitables.get(0) instanceof TickerViewModel){
                    visitables.add(1, headerViewModel);
                } else {
                    visitables.add(0, headerViewModel);
                }
                getView().setItems(visitables);
                if (isDataValid(visitables)) {
                    getView().removeNetworkError();
                } else {
                    getView().showNetworkError(context.getString(R.string.msg_network_error));
                }
            }
        }

        private boolean isDataValid(List<Visitable> visitables) {
            return containsInstance(visitables, BannerViewModel.class)
                    && containsInstance(visitables, TopPicksViewModel.class)
                    && containsInstance(visitables, BrandsViewModel.class)
                    && containsInstance(visitables, CategoryItemViewModel.class);
        }

        public <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
            for (E e : list) {
                if (clazz.isInstance(e)) {
                    return true;
                }
            }
            return false;
        }

    }

    private void sendBroadcastGetHeaderData() {
        Intent intentGetTokocash = new Intent(
                DrawerActivityBroadcastReceiverConstant.INTENT_ACTION
        );
        intentGetTokocash.putExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_GET_TOKOCASH_DATA);
        context.sendBroadcast(intentGetTokocash);
        context.sendBroadcast(new Intent(TokoPointDrawerBroadcastReceiverConstant.INTENT_ACTION));
    }
}
